package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.enumeration.HistoricoE;
import net.resultadofinal.micomercial.enumeration.TipoMovimientoE;
import net.resultadofinal.micomercial.model.Movimiento;
import net.resultadofinal.micomercial.model.DetalleMovimiento;
import net.resultadofinal.micomercial.model.form.DetalleCartillaForm;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimientoImpl extends DbConeccion implements MovimientoS {

	protected JdbcTemplate db;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	public MovimientoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(MovimientoImpl.class);
	private static final String ENTITY = "Movimiento";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Override
	public DataTableResults<Movimiento> listado(HttpServletRequest request, boolean estado, Integer sucursalId) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("movimiento m");
			sqlBuilder.setSelect("m.*, concat(p.nom_per, ' ', p.priape_per) as xcreated_by,s1.nombre as xsucursal_origen,s2.nombre as xsucursal_destino");
			sqlBuilder.addJoin("persona p on m.created_by = p.cod_per");
			sqlBuilder.addJoin("sucursal s1 on s1.cod_suc = m.sucursal_origen");
			sqlBuilder.addLeftJoin("sucursal s2 on s2.cod_suc = m.sucursal_destino");
			sqlBuilder.setWhere("m.estado=:xestado and (m.sucursal_origen=:xsuc or m.sucursal_destino=:xsuc)");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xsuc", sucursalId);
			return utilDataTableS.list(request, Movimiento.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Movimiento obtener(Long id){
		try {
			List<Movimiento> lista = db.query("select m.*,concat(p.nom_per,' ',p.priape_per) as xcreated_by,concat(p2.nom_per,' ',p2.priape_per) as xusuario_revision,s1.nombre as xsucursal_origen,s2.nombre as xsucursal_destino from movimiento m inner join persona p on p.cod_per = m.created_by inner join sucursal s1 on s1.cod_suc = m.sucursal_origen left join sucursal s2 on s2.cod_suc = m.sucursal_destino left join persona p2 on p2.cod_per = m.usuario_revision where id=?", BeanPropertyRowMapper.newInstance(Movimiento.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public List<DetalleMovimiento> obtenerDetalles(Long id){
		try {
			return db.query("select dm.*,p.nombre as xproducto from detalle_movimiento dm inner join producto p on dm.producto_id = p.id where movimiento_id=? order by id asc", BeanPropertyRowMapper.newInstance(DetalleMovimiento.class), id);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}

	@Override
	@Transactional
	public DataResponse adicionar(Movimiento obj){
		try {
			if(UtilClass.isNotNullEmpty(obj.getDetalles())) {
				Long id = db.queryForObject("select coalesce(max(id),0)+1 from movimiento", Long.class);
				obj.setId(id);
				sqlString = "insert into movimiento(id, created_at, created_by, sucursal_origen, sucursal_destino, tipo, estado, obs, estado_movimiento) " +
						"values(?,now(),?,?,?,?,true,?,?)";
				boolean save = db.update(sqlString, id, obj.getCreatedBy(),obj.getSucursalOrigen(),obj.getSucursalDestino(),obj.getTipo(), obj.getObs(), obj.getEstadoMovimiento()) > 0;
				adicionarDetalles(obj);
				return new DataResponse(save, id,Utils.getSuccessFailedAdd(ENTITY, save));
			} else {
				return new DataResponse(false, "No se encontro detalles de la cartilla");
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Override
	public void adicionarDetalles(Movimiento obj){
		sqlString = "insert into detalle_movimiento(id, movimiento_id, producto_id, cantidad, tipo, cantidad_unitaria, es_ingreso) values(?,?,?,?,?,?,?);";
		int ind = 1;
		for (DetalleMovimiento det: obj.getDetalles()) {
			db.update(sqlString, ind, obj.getId(), det.getProductoId(),det.getCantidad(), det.getTipo(), det.getCantidadUnitaria(), det.getEsIngreso());
			ind++;
		}
	}

	@Override
	@Transactional
	public DataResponse revisar(Movimiento obj){
		try {
			Movimiento movimiento = obtener(obj.getId());
			if(obj != null) {
				sqlString = "update movimiento set estado_movimiento=?,usuario_revision=?,fecha_revision=now() where id=?";
				boolean update = db.update(sqlString, obj.getEstadoMovimiento(), obj.getUsuarioRevision(), obj.getId()) > 0;
				if(obj.getEstadoMovimiento() == 1) { // Movimiento aceptado
					List<DetalleMovimiento> detalles = obtenerDetalles(obj.getId());
					if(movimiento.getTipo() == TipoMovimientoE.TRASPASO_SUCURAL.getTipo()) {
						for (DetalleMovimiento det: detalles) {
							almacenS.registrarAlmacen(det.getProductoId(),movimiento.getSucursalOrigen(), det.getCantidadUnitaria().negate() , obj.getUsuarioRevision(), HistoricoE.MOVIMIENTO_ENTRE_SUCURSALES.getTipo(), "Salida para sucursal: #"+movimiento.getSucursalDestino());
							almacenS.registrarAlmacen(det.getProductoId(),movimiento.getSucursalDestino(), det.getCantidadUnitaria(), obj.getUsuarioRevision(),HistoricoE.MOVIMIENTO_ENTRE_SUCURSALES.getTipo(), "Ingreso desde sucursal: #"+movimiento.getSucursalOrigen());
						}
					}else {
						if(movimiento.getTipo() == TipoMovimientoE.REGISTRO_MOVIMIENTO.getTipo()) {
							for (DetalleMovimiento det: detalles) {
								if(det.getEsIngreso()) {
									almacenS.registrarAlmacen(det.getProductoId(),movimiento.getSucursalOrigen(), det.getCantidadUnitaria(), obj.getUsuarioRevision(), HistoricoE.REGISTRO_DESDE_MOVIMIENTO.getTipo(), "Aumento de producto desde movimiento: #"+obj.getId());
								} else {
									almacenS.registrarAlmacen(det.getProductoId(),movimiento.getSucursalOrigen(), det.getCantidadUnitaria().negate(), obj.getUsuarioRevision(), HistoricoE.REGISTRO_DESDE_MOVIMIENTO.getTipo(), "Disminucion de producto desde movimiento: #"+obj.getId());
								}
							}
						} else {
							if(movimiento.getTipo() == TipoMovimientoE.MODIFICACION_ALMACEN.getTipo()) {
								for (DetalleMovimiento det: detalles) {
									if(det.getEsIngreso()) {
										almacenS.registrarAlmacen(det.getProductoId(),movimiento.getSucursalOrigen(), det.getCantidadUnitaria(), obj.getUsuarioRevision(), HistoricoE.MODIFICACION_SUPERUSUARIO.getTipo(), "Aumento de producto desde modificacion almacen, movimiento : #"+obj.getId());
									} else {
										almacenS.registrarAlmacen(det.getProductoId(),movimiento.getSucursalOrigen(), det.getCantidadUnitaria().negate(), obj.getUsuarioRevision(), HistoricoE.MODIFICACION_SUPERUSUARIO.getTipo(), "Disminucion de producto desde modificacion almacen, movimiento: #"+obj.getId());
									}
								}
							}
						}
					}
				}
				return new DataResponse(update, Utils.getSuccessFailedMod(ENTITY, update));
			} else {
				return new DataResponse(false, "No se encontro detalles de la cartilla");
			}
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}

	@Override
	public DataResponse eliminar(Movimiento obj) {
		try {
			sqlString = "update movimiento set estado = false, updated_by =?, updated_at=now() where id=?";
			boolean update = db.update(sqlString, obj.getUpdatedBy(),obj.getId()) > 0;
			return Utils.getResponseDataEli(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
}
