package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.enumeration.TipoArqueoE;
import net.resultadofinal.micomercial.mappers.DetalleventaMapper;
import net.resultadofinal.micomercial.mappers.VentaMapper;
import net.resultadofinal.micomercial.model.DetalleArqueo;
import net.resultadofinal.micomercial.model.DetalleVenta;
import net.resultadofinal.micomercial.model.Venta;
import net.resultadofinal.micomercial.model.form.DetalleVentaForm;
import net.resultadofinal.micomercial.model.form.VentaForm;
import net.resultadofinal.micomercial.model.wrap.DetalleVentaWrap;
import net.resultadofinal.micomercial.model.wrap.ProductoDetalleVenta;
import net.resultadofinal.micomercial.model.wrap.SubDetalleVentaWrap;
import net.resultadofinal.micomercial.model.wrap.VentaInfoWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VentaImpl extends DbConeccion implements VentaS {
	
	private JdbcTemplate db;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private ArqueoS arqueoS;
	@Autowired
	public VentaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(VentaImpl.class);
	private static final String ENTITY = "venta";
	private static final short VENTA_PRODUCTO = 3;
	private static final short REVERSION_VENTA_PRODUCTO = 4;
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Override
	public DataTableResults<Venta> listado(HttpServletRequest request, boolean estado, Long xuser, int xsucursal, Short tipo) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("venta v");
			sqlBuilder.setSelect("v.*,trim(concat(p1.nom_per,' ',p1.priape_per,' ',coalesce(p1.segape_per,''))) xusuario,trim(concat(p2.nom_per,' ',p2.priape_per,' ',coalesce(p2.segape_per,''))) xcliente");
			sqlBuilder.addJoin("persona p1 on p1.cod_per = v.usuario_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per = v.cliente_id");
			sqlBuilder.setWhere("v.estado=:xestado and (v.usuario_id=:xuser or :xuser=-1) and (v.tipo=:xtipo or :xtipo=0) and v.sucursal_id=:xsucursal");
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xuser",xuser);
			sqlBuilder.addParameter("xsucursal",xsucursal);
			sqlBuilder.addParameter("xtipo",tipo);
			return utilDataTableS.list(request, Venta.class, sqlBuilder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Venta obtener(Long codVen){
		try {
			sqlString = "select v.*,concat(p.nom_per,' ',p.priape_per) as xusuario from venta v inner join persona p on p.cod_per = v.usuario_id where v.id=?";
			List<Venta> ventaList=db.query(sqlString, BeanPropertyRowMapper.newInstance(Venta.class),codVen);
			Venta venta = UtilClass.getFirst(ventaList);
			if(venta != null) {
				venta.setDetalles(obtenerDetalle(codVen));
			}
			return venta;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			e.printStackTrace();
			return null;
		}
	}
	public VentaForm obtenerVentaForm(Long codVen){
		try {
			sqlString = "select v.*,concat(p.nom_per,' ',p.priape_per) as xusuario from venta v inner join persona p on p.cod_per = v.usuario_id where v.id=?";
			List<VentaForm> ventaList=db.query(sqlString, BeanPropertyRowMapper.newInstance(VentaForm.class),codVen);
			VentaForm venta = UtilClass.getFirst(ventaList);
			if(venta != null) {
				sqlString = "select dv.*, dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id from detalle_venta dv inner join detalle_cartilla_diaria dcd on dcd.cartilla_diaria_id = dv.cartilla_diaria_id and dcd.id = dv.detalle_cartilla_diaria_id where dv.es_compuesto = true and dv.venta_id = ?";
				venta.setDetalleVentaCompuesto(db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleVentaForm.class), codVen));
				sqlString = "select dv.*, dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id from detalle_venta dv left join detalle_cartilla_diaria dcd on dcd.cartilla_diaria_id = dv.cartilla_diaria_id and dcd.id = dv.detalle_cartilla_diaria_id where dv.es_compuesto = false and dv.venta_id = ?";
				venta.setDetalleVenta(db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleVentaForm.class), codVen));
			}
			return venta;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			e.printStackTrace();
			return null;
		}
	}
	public List<DetalleVentaForm> obtenerDetallesForm(Long ventaId, Boolean estaCompuesto) {
		try {
			sqlString = "select dv.*, dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id from detalle_venta dv left join detalle_cartilla_diaria dcd on dcd.cartilla_diaria_id = dv.cartilla_diaria_id and dcd.id = dv.detalle_cartilla_diaria_id where dv.venta_id = ?";
			if(estaCompuesto != null) {
				sqlString += " and dv.es_compuesto = ?";
				return db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleVentaForm.class), estaCompuesto, ventaId);
			} else {
				return db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleVentaForm.class), ventaId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<DetalleVenta> obtenerDetalle(Long ventaId){
		try {
			sqlString = "select dv.* from detalle_venta dv  where dv.venta_id = ? order by dv.id asc;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleVenta.class),ventaId);
		} catch (DataAccessException e) {
			logger.error(Utils.errorGet("detalle de ventas", e.toString()));
			e.printStackTrace();
			return null;
		}
		
	}
	@Transactional
	public DataResponse guardarComanda(VentaForm obj) {
		try {
			Long ventaId = db.queryForObject("select coalesce(max(id),0)+1 from venta where sucursal_id =?", Long.class, obj.getSucursalId());
			obj.setId(ventaId);
			sqlString = "insert into venta(id, numero, usuario_id, cliente_id, fecha, obs, total, descuento, gestion, estado, tipo, sucursal_id, subtotal, mesa_id, cantidad_personas, forma_pago_id, total_pagado, total_cambio, created_by, created_at) " +
					"VALUES(?, ?, ?, ?, now(), ?, ?, ?, ?, true, ?, ?, ?, ?, ?, ?, ?, ?, ?, now());";
			if(obj.getTipo() == 2) { // si es venta finalizada
				Long numero = db.queryForObject("select coalesce(max(numero),0)+1 from venta where sucursal_id = ?", Long.class, obj.getSucursalId());
				obj.setNumero(numero);
			}
			boolean saveVenta = db.update(sqlString,ventaId, obj.getNumero(),obj.getUsuarioId(), obj.getClienteId(),obj.getObs(), obj.getTotal(), obj.getDescuento(), obj.getGestion(),
					obj.getTipo(), obj.getSucursalId(),obj.getSubtotal(),obj.getMesaId(), obj.getCantidadPersonas(),obj.getFormaPagoId(),obj.getTotalPagado(),obj.getTotalCambio(),obj.getCreatedBy())>0;
			if(saveVenta) {
				short detalleId = 1;
				detalleId = adicionarListaDetalleComanda(ventaId, detalleId, obj.getSucursalId(), obj.getUsuarioId(), obj.getDetalleVenta());
				detalleId = adicionarListaDetalleComanda(ventaId, detalleId, obj.getSucursalId(), obj.getUsuarioId(), obj.getDetalleVentaCompuesto());
				if(detalleId <= 1) {
					throw new RuntimeException("no se encontro detalles de la comanda para guardar");
				}
				adicionarVentaArqueo(obj);
				return new DataResponse(true, "Registro de comanda exitosa");
			} else {
				return new DataResponse(false, "Error al guardar la venta");
			}
		} catch (Exception ex) {
			throw new RuntimeException(Utils.errorAdd(ENTITY, ex.getMessage()));
		}
	}
	private short adicionarListaDetalleComanda(Long ventaId, short detalleId, Integer sucursalId, Long userId, List<DetalleVentaForm> detalles) {
		if(detalles != null && !detalles.isEmpty()) {
			for (DetalleVentaForm det: detalles) {
				adicionarDetalleComanda(ventaId,detalleId,sucursalId,userId, det);
				detalleId++;
			}
		}
		return detalleId;
	}
	private void adicionarDetalleComanda(Long ventaId, short detalleId, Integer sucursalId, Long userId, DetalleVentaForm det) {
		sqlString ="INSERT INTO detalle_venta(venta_id, id, producto_id, precio, cantidad, descuento, subtotal, total, cartilla_diaria_id, detalle_cartilla_diaria_id, es_compuesto, tipo_venta, cantidad_unitaria) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		db.update(sqlString, ventaId, detalleId, det.getProductoId(), det.getPrecio(), det.getCantidad(), 0, 0, det.getTotal(), det.getCartillaDiariaId(),
				det.getDetalleCartillaDiariaId(), det.getEsCompuesto() == null ? false : det.getEsCompuesto(), det.getTipoVenta() == null ? 1: det.getTipoVenta(), det.getCantidadUnitaria());
		almacenS.registrarAlmacen(det.getProductoId(), sucursalId, -1*det.getCantidadUnitaria(), userId, VENTA_PRODUCTO, "Disminucion por venta #"+ventaId);
	}
	@Transactional
	public DataResponse actualizarComanda(VentaForm obj) {
		try {
			sqlString = "update venta set numero=?,tipo=?,usuario_id=?, cliente_id=?, obs=?, total=?, descuento=?, subtotal=?, mesa_id=?, cantidad_personas=?, updated_by=?, updated_at=now() where id=?";
			if(obj.getTipo() == 2) { // si es venta finalizada
				Long numero = db.queryForObject("select coalesce(max(numero),0)+1 from venta where sucursal_id = ?", Long.class, obj.getSucursalId());
				obj.setNumero(numero);
			}
			boolean saveVenta = db.update(sqlString,obj.getNumero(),obj.getTipo(),obj.getUsuarioId(), obj.getClienteId(),obj.getObs(), obj.getTotal(), obj.getDescuento(),obj.getSubtotal(),obj.getMesaId(),
					obj.getCantidadPersonas(),obj.getCreatedBy(), obj.getId())>0;
			if(saveVenta) {
				List<DetalleVentaForm> detallesBD = obtenerDetallesForm(obj.getId(), null);
				short detalleVentaId = db.queryForObject("select coalesce(max(id),0)+1 from detalle_venta where venta_id=?", Short.class, obj.getId());
				detalleVentaId = adicionarDetallesComandaModificar(detalleVentaId, detallesBD,obj.getDetalleVenta(),obj.getSucursalId(), obj.getCreatedBy(),obj.getId(),false);
				detalleVentaId = adicionarDetallesComandaModificar(detalleVentaId, detallesBD,obj.getDetalleVentaCompuesto(),obj.getSucursalId(), obj.getCreatedBy(),obj.getId(),true);
//				if(obj.getDetalleVenta() != null && !obj.getDetalleVenta().isEmpty()
//				&& detallesBD != null && !detallesBD.isEmpty()) {
//					for (DetalleVentaForm det: obj.getDetalleVenta()) {
//						if(det.getCartillaDiariaId() != null && det.getDetalleCartillaDiariaId() != null) { //Es preparado
//							Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
//							&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && it.getProductoId() == det.getProductoId()).findFirst();
//							if(!found.isPresent()) {
//								//Adicionar DB
//								adicionarDetalleComanda(obj.getId(),detalleVentaId,obj.getSucursalId(),obj.getCreatedBy(),det);
//								detalleVentaId++;
//							}
//						} else { // Es producto
//							Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getProductoId() == det.getProductoId()).findFirst();
//							if(!found.isPresent()) {
//								//Adicionar DB
//								adicionarDetalleComanda(obj.getId(),detalleVentaId,obj.getSucursalId(),obj.getCreatedBy(),det);
//								detalleVentaId++;
//							}
//						}
//					}
//					//Revisar los detalles de ventas eliminados
//					for (DetalleVentaForm det : detallesBD) {
//						if(!det.getEsCompuesto()) {
//							Optional<DetalleVentaForm> found = obj.getDetalleVenta().stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
//									&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && det.getProductoId() == it.getProductoId()).findFirst();
//							if(!found.isPresent()) {
//								//Eliminar
//								db.update("delete from detalle_venta where venta_id =? and id=?", det.getVentaId(), det.getId());
//								almacenS.registrarAlmacen(det.getProductoId(), obj.getSucursalId(), -1*det.getCantidad(), obj.getCreatedBy(),
//										REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta");
//							} else { // Si se encuentra se modifica segun
//								int diferencia = found.get().getCantidadUnitaria() - det.getCantidadUnitaria();
//								if(diferencia != 0) {
//									//update
//									db.update("update detalle_venta set cantidad = ?, cantidad_unitaria = ? where venta_id = ? and detalle_venta_id = ?",
//											found.get().getCantidad(),found.get().getCantidadUnitaria(), det.getVentaId(), det.getId());
//									if(diferencia > 0){ // Si es positiva, se aumento la diferencia, caso contrario se disminuyo la diferencia
//										almacenS.registrarAlmacen(det.getProductoId(), obj.getSucursalId(), diferencia, obj.getCreatedBy(),
//												VENTA_PRODUCTO, "Aumento detalle, por venta # "+obj.getId());
//									} else {
//										almacenS.registrarAlmacen(det.getProductoId(), obj.getSucursalId(), diferencia, obj.getCreatedBy(),
//												REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta #"+obj.getId());
//									}
//								}
//							}
//						}
//					}
//				}
//				if(obj.getDetalleVentaCompuesto() != null && !obj.getDetalleVentaCompuesto().isEmpty()
//						&& detallesBD != null && !detallesBD.isEmpty()) {
//					for (DetalleVentaForm det: obj.getDetalleVentaCompuesto()) {
//						Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
//								&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && it.getProductoId() == det.getProductoId()).findFirst();
//						if(!found.isPresent()) {
//							//Adicionar DB
//							adicionarDetalleComanda(obj.getId(),detalleVentaId,obj.getSucursalId(),obj.getCreatedBy(),det);
//							detalleVentaId++;
//						}
//					}
//					//Revisar los detalles de ventas eliminados
//					for (DetalleVentaForm det : detallesBD) {
//						if(det.getEsCompuesto()) {
//							Optional<DetalleVentaForm> found = obj.getDetalleVentaCompuesto().stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
//									&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && det.getProductoId() == it.getProductoId()).findFirst();
//							if(!found.isPresent()) {
//								//Eliminar
//								db.update("delete from detalle_venta where venta_id =? and id=?", det.getVentaId(), det.getId());
//							} else { // Si se encuentra se modifica segun
//								int diferencia = found.get().getCantidadUnitaria() - det.getCantidadUnitaria();
//								if(diferencia != 0) {
//									//Update
//									db.update("update detalle_venta set cantidad = ?, cantidad_unitaria = ? where venta_id = ? and detalle_venta_id = ?",
//											found.get().getCantidad(),found.get().getCantidadUnitaria(), det.getVentaId(), det.getId());
//									if(diferencia > 0){ // Si es positiva, se aumento la diferencia, caso contrario se disminuyo la diferencia
//										almacenS.registrarAlmacen(det.getProductoId(), obj.getSucursalId(), diferencia, obj.getCreatedBy(),
//												VENTA_PRODUCTO, "Aumento detalle, por venta # "+obj.getId());
//									} else {
//										almacenS.registrarAlmacen(det.getProductoId(), obj.getSucursalId(), diferencia, obj.getCreatedBy(),
//												REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta #"+obj.getId());
//									}
//								}
//							}
//						}
//					}
//				}
				adicionarVentaArqueo(obj);
				return new DataResponse(true, "Registro de comanda exitosa");
			} else {
				return new DataResponse(false, "Error al guardar la venta");
			}
		} catch (Exception ex) {
			throw new RuntimeException(Utils.errorAdd(ENTITY, ex.getMessage()));
		}
	}
	private void adicionarVentaArqueo(VentaForm obj) {
		if(obj.getTipo() != null && obj.getTipo() == 2) {//Arqueo
			Long arqueoIdSesionado = arqueoS.obtenerArqueoSesionActiva(obj.getCreatedBy(), obj.getSucursalId());
			if(arqueoIdSesionado > 0) {
				DetalleArqueo det = new DetalleArqueo(arqueoIdSesionado, TipoArqueoE.INGRESO_VENTA.getTipo(), obj.getFormaPagoId(), "Venta #"+obj.getNumero(),obj.getTotal(), true);
				short detalleArqueoId = arqueoS.adicionarDetalle(det);
				if(detalleArqueoId > 0) {
					boolean saveArqueo = db.update("update venta set arqueo_id=?, detalle_arqueo_id=?,total_pagado=?,total_cambio=?,forma_pago_id=? where id=?",
							arqueoIdSesionado, detalleArqueoId, obj.getTotalPagado(), obj.getTotalCambio(), obj.getFormaPagoId(), obj.getId()) > 0;
					if(!saveArqueo) {
						throw new RuntimeException("Error al cruzar datos de aruqeo con venta");
					}
				}
			} else { //no se tiene0
				throw new RuntimeException("Venta cancelada, ya que no se tiene una caja abierta");
			}
		}
	}
	private short adicionarDetallesComandaModificar(short detalleVentaId,List<DetalleVentaForm> detallesBD,List<DetalleVentaForm> detalleVentaRequest,Integer sucId, Long userId, Long ventaId, boolean soloCompuestos){
		if(detalleVentaRequest != null && !detalleVentaRequest.isEmpty()
				&& detallesBD != null && !detallesBD.isEmpty()) {
			for (DetalleVentaForm det: detalleVentaRequest) {
				if(det.getCartillaDiariaId() != null && det.getDetalleCartillaDiariaId() != null) { //Es preparado
					Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
							&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && it.getProductoId() == det.getProductoId()).findFirst();
					if(!found.isPresent()) {
						//Adicionar DB
						adicionarDetalleComanda(ventaId,detalleVentaId,sucId,userId,det);
						detalleVentaId++;
					}
				} else { // Es producto
					Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getProductoId() == det.getProductoId()).findFirst();
					if(!found.isPresent()) {
						//Adicionar DB
						adicionarDetalleComanda(ventaId,detalleVentaId,sucId,userId,det);
						detalleVentaId++;
					}
				}
			}
			//Revisar los detalles de ventas eliminados
			for (DetalleVentaForm det : detallesBD) {
				if(det.getEsCompuesto() == soloCompuestos) {
					Optional<DetalleVentaForm> found = detalleVentaRequest.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
							&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && det.getProductoId() == it.getProductoId()).findFirst();
					if(!found.isPresent()) {
						//Eliminar
						db.update("delete from detalle_venta where venta_id =? and id=?", det.getVentaId(), det.getId());
						almacenS.registrarAlmacen(det.getProductoId(), sucId, -1*det.getCantidad(), userId,
								REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta");
					} else { // Si se encuentra se modifica segun
						int diferencia = found.get().getCantidadUnitaria() - det.getCantidadUnitaria();
						if(diferencia != 0) {
							//update
							db.update("update detalle_venta set cantidad = ?, cantidad_unitaria = ?, precio =?, tipo_venta =? ,total = ? where venta_id = ? and id = ?",
									found.get().getCantidad(),found.get().getCantidadUnitaria(), found.get().getPrecio(), found.get().getTipoVenta(), found.get().getTotal(), det.getVentaId(), det.getId());
							if(diferencia > 0){ // Si es positiva, se aumento la diferencia, caso contrario se disminuyo la diferencia
								almacenS.registrarAlmacen(det.getProductoId(), sucId, diferencia, userId,
										VENTA_PRODUCTO, "Aumento detalle, por venta # "+ventaId);
							} else {
								almacenS.registrarAlmacen(det.getProductoId(), sucId, diferencia, userId,
										REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta #"+ ventaId);
							}
						}
					}
				}
			}
		}
		return detalleVentaId;
	}

	@Override
	@Transactional
	public Boolean eliminar(Long ventaId, Long userId){
		try {
			return db.queryForObject("select venta_eliminar(?,?);",Boolean.class,ventaId,userId);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}

	@Override
	public Venta obtenerPorArqueoCaja(Long arqueoId, Integer detalleArqueoId) {
		try {
			List<Venta> lista = db.query("select * from venta v where v.arqueo_id =? and detalle_arqueo_id =?;", new VentaMapper(),arqueoId, detalleArqueoId);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error("obtener por arqueo de caja: "+e.toString());
			throw new RuntimeException("error al obtener por arqueo de caja");
		}
	}
	public VentaInfoWrap obtenerVentaInfo(Long codVen){
		try {
			sqlString = "select v.id,v.numero,v.arqueo_id,v.gestion,v.detalle_arqueo_id,v.tipo,v.obs,v.fecha,v.cantidad_personas,v.created_at,v.total,v.descuento,v.subtotal, v.total_pagado, v.total_cambio," +
					"concat(p.nom_per,' ',p.priape_per) as xusuario,concat(p3.nom_per,' ',p3.priape_per) as xcliente,concat(p2.nom_per,' ',p2.priape_per) as xcreated_by,m.nombre as xmesa from venta v " +
					"inner join persona p on p.cod_per = v.usuario_id " +
					"inner join mesa m on m.id=v.mesa_id " +
					"inner join persona p2 on p2.cod_per = v.created_by " +
					"inner join persona p3 on p3.cod_per = v.cliente_id " +
					"left join forma_pago fp on fp.id =v.forma_pago_id where v.id=?";
			List<VentaInfoWrap> ventaList=db.query(sqlString, BeanPropertyRowMapper.newInstance(VentaInfoWrap.class),codVen);
			VentaInfoWrap venta = UtilClass.getFirst(ventaList);
			if(venta !=null) {
				sqlString = "select dv.*, dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id,tp.nombre as xtipo_producto,cs.nombre as xcombo,p.nombre as xproducto,cs.total as precio_combo " +
						"from detalle_venta dv " +
						"inner join producto p on p.id = dv.producto_id " +
						"left join detalle_cartilla_diaria dcd on dcd.cartilla_diaria_id = dv.cartilla_diaria_id and dcd.id = dv.detalle_cartilla_diaria_id " +
						"left join detalle_cartilla_sucursal dcs on dcs.cartilla_sucursal_id = dcd.cartilla_sucursal_id and dcs.id = dcd.detalle_cartilla_sucursal_id " +
						"left join tipo_producto tp on tp.id = dcs.tipo_producto_id " +
						"left join cartilla_sucursal cs on cs.id = dcs.cartilla_sucursal_id " +
						"where dv.venta_id = ?";
				List<DetalleVentaForm> det = db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleVentaForm.class), codVen);
				List<DetalleVentaForm> detalles = det.stream().filter(it -> !it.getEsCompuesto()).collect(Collectors.toList());
				List<DetalleVentaForm> detallesCompuestos = det.stream().filter(it -> it.getEsCompuesto()).collect(Collectors.toList());
				if(UtilClass.isNotNullEmpty(detalles)) {
					List<DetalleVentaWrap> detalleVentaWrapList = new ArrayList<>();
					for (DetalleVentaForm it: detalles) {
						DetalleVentaWrap d = new DetalleVentaWrap();
						d.setXproducto(it.getXproducto());
						d.setCantidad(it.getCantidad());
						d.setPrecio(it.getPrecio());
						d.setXtipoVenta(it.getTipoVenta()==1?"Unid.":"caja");
						d.setTotal(it.getTotal());
						detalleVentaWrapList.add(d);
					}
					venta.setDetalleVenta(detalleVentaWrapList);
				}
				//Desarrollo de detalles compuestos
				if(detallesCompuestos != null && !detallesCompuestos.isEmpty()) {
					Map<Integer, List<DetalleVentaForm>> grupoSucursal = detallesCompuestos.stream().collect(Collectors.groupingBy(DetalleVentaForm::getCartillaSucursalId));
					if(grupoSucursal.entrySet() != null && !grupoSucursal.entrySet().isEmpty()) {
						List<DetalleVentaWrap> detalleVentaComboList = new ArrayList<>();
						for (Map.Entry<Integer, List<DetalleVentaForm>> comboItemList : grupoSucursal.entrySet()) {//Almuerzo, desayuno
							DetalleVentaWrap detalleVentaCombo = new DetalleVentaWrap();
							detalleVentaCombo.setXproducto(comboItemList.getValue().get(0).getXcombo());
							detalleVentaCombo.setPrecio(comboItemList.getValue().get(0).getPrecioCombo());
							List<SubDetalleVentaWrap> subdetalleVentaList = new ArrayList<>();
							Map<Short,List<DetalleVentaForm>> tipoComboList = comboItemList.getValue().stream().collect(Collectors.groupingBy(DetalleVentaForm::getDetalleCartillaSucursalId));
							for (Map.Entry<Short, List<DetalleVentaForm>> tipoComboItem: tipoComboList.entrySet()) {// Sopa, Segundo
								int cantidadTotalGrupo = 0;
								SubDetalleVentaWrap tipoCombo = new SubDetalleVentaWrap();
								tipoCombo.setXtipoProducto(tipoComboItem.getValue().get(0).getXtipoProducto());
								List<ProductoDetalleVenta> productoDetalleVentaList = new ArrayList<>();
								for (DetalleVentaForm producto: tipoComboItem.getValue()) {
									cantidadTotalGrupo += producto.getCantidadUnitaria();
									ProductoDetalleVenta pro = new ProductoDetalleVenta();
									pro.setXproducto(producto.getXproducto());
									pro.setCantidad(producto.getCantidadUnitaria());
									productoDetalleVentaList.add(pro);
								}
								detalleVentaCombo.setCantidad(cantidadTotalGrupo);
								detalleVentaCombo.setTotal(detalleVentaCombo.getPrecio().multiply(new BigDecimal(detalleVentaCombo.getCantidad())));
								tipoCombo.setProductos(productoDetalleVentaList);
								subdetalleVentaList.add(tipoCombo);
							}
							detalleVentaCombo.setSubdetallesCompuestos(subdetalleVentaList);
							detalleVentaComboList.add(detalleVentaCombo);
						}
						venta.setDetalleVentaCompuesto(detalleVentaComboList);
					}
				}
			}
			return venta;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			e.printStackTrace();
			return null;
		}
	}
}