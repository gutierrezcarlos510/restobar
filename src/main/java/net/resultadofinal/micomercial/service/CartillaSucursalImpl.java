package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaSucursal;
import net.resultadofinal.micomercial.model.DetalleCartillaSucursal;
import net.resultadofinal.micomercial.model.form.CartillaSucursalForm;
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

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartillaSucursalImpl extends DbConeccion implements CartillaSucursalS {

	protected JdbcTemplate db;
	@Autowired
	public CartillaSucursalImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CartillaSucursalImpl.class);
	private static final String ENTITY = "CartillaSucursal";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Override
	public DataTableResults<CartillaSucursal> listado(HttpServletRequest request, boolean estado, Integer sucursalId) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("cartilla_sucursal cs");
			sqlBuilder.setSelect("cs.*");
			sqlBuilder.setWhere("cs.estado=:xestado and cs.cod_suc=:xsuc");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xsuc", sucursalId);
			return utilDataTableS.list(request, CartillaSucursal.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CartillaSucursal obtener(Integer id){
		try {
			List<CartillaSucursal> lista = db.query("select * from cartilla_sucursal where id=?", BeanPropertyRowMapper.newInstance(CartillaSucursal.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public List<DetalleCartillaSucursal> obtenerDetalles(Integer id){
		try {
			return db.query("select dcs.*,tp.nombre as xtipo_producto from detalle_cartilla_sucursal dcs inner join tipo_producto tp on dcs.tipo_producto_id = tp.id where cartilla_sucursal_id=? order by id asc", BeanPropertyRowMapper.newInstance(DetalleCartillaSucursal.class), id);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	
	@Override
	public DataResponse adicionar(CartillaSucursal obj,Integer tipos[], BigDecimal precios[]){
		try {
			if(tipos != null && tipos.length > 0) {
				Integer id = db.queryForObject("select coalesce(max(id),0)+1 from cartilla_sucursal", Integer.class);
				sqlString = "insert into cartilla_sucursal(id,nombre,total,cod_suc,esta_compuesto,estado) values(?,?,?,?,?,true)";
				boolean save = db.update(sqlString, id, obj.getNombre(),obj.getTotal(),obj.getCodSuc(),obj.isEstaCompuesto()) > 0;
				adicionarDetalles(id, tipos, precios);
				return new DataResponse(save, Utils.getSuccessFailedAdd(ENTITY, save));
			} else {
				return new DataResponse(false, "No se encontro detalles de la cartilla");
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	public void adicionarDetalles(Integer id, Integer tipos[], BigDecimal precios[]){
		for (int i = 0; i < tipos.length; i++) {
			sqlString = "insert into detalle_cartilla_sucursal(cartilla_sucursal_id,id,tipo_producto_id,precio) values(?,?,?,?);";
			db.update(sqlString, id,(i+1),tipos[i],precios[i]);
		}
	}
	
	@Override
	public DataResponse modificar(CartillaSucursal obj, Integer tipos[], BigDecimal precios[]){
		try {
			if(tipos != null && tipos.length > 0) {
				sqlString = "update cartilla_sucursal set nombre=?,total=?,esta_compuesto=? where id=?";
				boolean update = db.update(sqlString, obj.getNombre(), obj.getTotal(), obj.isEstaCompuesto(), obj.getId()) > 0;
				db.update("delete from detalle_cartilla_sucursal where cartilla_sucursal_id = ?", obj.getId());
				adicionarDetalles(obj.getId(), tipos, precios);
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
	public DataResponse darEstado(Integer id, Boolean estado) {
		try {
			sqlString = "update cartilla_sucursal set estado = ? where id=?";
			boolean update = db.update(sqlString, estado, id) > 0;
			return estado ? Utils.getResponseDataAct(ENTITY, update) : Utils.getResponseDataEli(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
	@Override
	public List<CartillaSucursalForm> listarPorSucursal(Integer sucursalId) {
		try {
			List<CartillaSucursalForm> lista = db.query("select * from cartilla_sucursal where cod_suc = ? and estado=true;", BeanPropertyRowMapper.newInstance(CartillaSucursalForm.class), sucursalId);
			if(lista != null && !lista.isEmpty()) {
				sqlString = "select dcs.cartilla_sucursal_id,dcs.id,dcs.tipo_producto_id,es_preparado,es_comerciable,dcs.precio,tp.nombre as xtipo_producto from detalle_cartilla_sucursal dcs inner join cartilla_sucursal cs on cs.estado=true and cs.id = dcs.cartilla_sucursal_id  and cs.cod_suc = ? inner join tipo_producto tp on dcs.tipo_producto_id = tp.id";
				List<DetalleCartillaForm> detalles = db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleCartillaForm.class), sucursalId);
				if(detalles != null && !detalles.isEmpty()) {
					for (CartillaSucursalForm det: lista) {
						List<DetalleCartillaForm> subdetalle = detalles.stream().filter(it -> it.getCartillaSucursalId() == det.getId()).collect(Collectors.toList());
						det.setDetalleCartillaList(subdetalle);
					}
					return lista;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}
}
