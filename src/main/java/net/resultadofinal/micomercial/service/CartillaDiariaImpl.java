package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.controller.CartillaDiariaC;
import net.resultadofinal.micomercial.enumeration.HistoricoE;
import net.resultadofinal.micomercial.enumeration.TipoMovimientoE;
import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleMovimiento;
import net.resultadofinal.micomercial.model.Movimiento;
import net.resultadofinal.micomercial.model.form.CartillaDiariaForm;
import net.resultadofinal.micomercial.model.form.CartillaSucursalForm;
import net.resultadofinal.micomercial.model.form.DetalleCartillaForm;
import net.resultadofinal.micomercial.model.form.ProductoCartillaForm;
import net.resultadofinal.micomercial.model.wrap.CartillaDiariaCierreWrap;
import net.resultadofinal.micomercial.model.wrap.CierreWrap;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartillaDiariaImpl extends DbConeccion implements CartillaDiariaS {

	protected JdbcTemplate db;
	@Autowired
	public CartillaDiariaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private MovimientoS movimientoS;
	private static final Logger logger = LoggerFactory.getLogger(CartillaDiariaImpl.class);
	private static final String ENTITY = "CartillaDiaria";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	
	@Override
	public DataTableResults<CartillaDiaria> listado(HttpServletRequest request, boolean estado, Integer sucursalId) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("cartilla_diaria cd");
			sqlBuilder.setSelect("cd.*,concat(p.nom_per,' ',p.priape_per) xusuario");
			sqlBuilder.addJoin("persona p on p.cod_per = cd.usuario_id");
			sqlBuilder.setWhere("cd.estado=:xestado and cd.cod_suc=:xsuc");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xsuc", sucursalId);
			return utilDataTableS.list(request, CartillaDiaria.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}

	
	@Override
	public CartillaDiaria obtener(Long id){
		try {
			List<CartillaDiaria> lista = db.query("select * from cartilla_diaria where id=?", BeanPropertyRowMapper.newInstance(CartillaDiaria.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public List<DetalleCartillaDiaria> obtenerDetalles(Long id){
		try {
			return db.query("select dcd.*,tp.nombre as xtipo_producto from detalle_cartilla_diaria dcd inner join producto p on dcd.producto_id = p.id inner join tipo_producto tp on tp.id = p.tipo_id where cartilla_diaria_id=? order by id asc", BeanPropertyRowMapper.newInstance(DetalleCartillaDiaria.class), id);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	
	
	@Override
	@Transactional
	public DataResponse adicionar(CartillaDiariaForm obj){
		try {
			if(obj != null && obj.getCartillaSucursalFormList() != null && !obj.getCartillaSucursalFormList().isEmpty()) {
				//Si existe anterior, dar de baja
				db.update("update cartilla_diaria set estado_cartilla = false, ffin=now() where estado_cartilla = true and estado=true;");
				Long id = db.queryForObject("select coalesce(max(id),0)+1 from cartilla_diaria", Long.class);
				sqlString = "insert into cartilla_diaria(id,finicio,usuario_id,cod_suc,estado,estado_cartilla) values(?,now(),?,?,true,true)";
				boolean save = db.update(sqlString, id, obj.getUsuarioId(),obj.getCodSuc()) > 0;
				obj.setId(id);
				//Lista de productos agregados
				List<DetalleMovimiento> detalleMovimientoList = new ArrayList<>();
				adicionarDetalles(obj,detalleMovimientoList);
				if(!detalleMovimientoList.isEmpty()) {
					Movimiento movimiento = new Movimiento();
					movimiento.setDetalles(detalleMovimientoList);
					movimiento.setUsuarioRevision(obj.getUsuarioId());
					movimiento.setSucursalOrigen(obj.getCodSuc());
					movimiento.setUpdatedBy(obj.getUsuarioId());
					movimiento.setCreatedBy(obj.getUsuarioId());
					movimiento.setObs("Registrado desde registro adicionar cartilla diaria");
					movimiento.setTipo(TipoMovimientoE.REGISTRO_MOVIMIENTO.getTipo());
					movimiento.setEstadoMovimiento((short)1);//1= estado aceptado
//					registroMovimientoCierreDiario(movimiento);
				}
				return new DataResponse(save, Utils.getSuccessFailedAdd(ENTITY, save));
			} else {
				return new DataResponse(false, "No se encontro detalles de la cartilla");
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Override
	@Transactional
	public void adicionarDetalles(CartillaDiariaForm obj,List<DetalleMovimiento> detalleMovimientoList){
		if(UtilClass.isNotNullEmpty(obj.getCartillaSucursalFormList())) {
			sqlString = "select coalesce(max(id),0)+1 from detalle_cartilla_diaria where cartilla_diaria_id=?";
			int i= db.queryForObject(sqlString, Integer.class, obj.getId());
			sqlString = "insert into detalle_cartilla_diaria(cartilla_diaria_id,id,cartilla_sucursal_id,detalle_cartilla_sucursal_id,producto_id,precio_individual,precio_compuesto,cantidad) values(?,?,?,?,?,?,?,?);";

			List<DetalleCartillaDiaria> detallesBD = obtenerDetalles(obj.getId());
			List<ProductoCartillaForm> detallesModificados = new ArrayList<>();

			for (CartillaSucursalForm det: obj.getCartillaSucursalFormList()) {
				if (UtilClass.isNotNullEmpty(det.getDetalleCartillaList())) {
					for(DetalleCartillaForm subdet: det.getDetalleCartillaList()) {
						if (UtilClass.isNotNullEmpty(subdet.getProductos())) {
							for(ProductoCartillaForm item : subdet.getProductos()) {
								boolean esProductoFabricado = db.queryForObject("select count(*)>0 from producto p inner join tipo_producto tp on tp.id = p.tipo_id and es_preparado=false and es_comerciable = true where p.id=?", Boolean.class, item.getProductoId());
								item.setEsProductoFabricado(esProductoFabricado);
								if(item.getCartillaDiariaId() != null && item.getId() != null) { // Modificar
									detallesModificados.add(item);
									if(item.getCantidadModificar() != null) {
										db.update("update detalle_cartilla_diaria set cantidad=?+cantidad where id=? and cartilla_diaria_id=?",
												item.getCantidadModificar(),item.getId(), item.getCartillaDiariaId());
										if(!esProductoFabricado) {
											almacenS.registrarAlmacen(item.getProductoId(),obj.getCodSuc(),item.getCantidadModificar(),obj.getUsuarioId(), HistoricoE.MODIFICACION_CARTILLA_DIARIA.getTipo(),"");
											DetalleMovimiento detalleMovimiento = new DetalleMovimiento(item.getProductoId(),true,true,item.getCantidadModificar(),item.getCantidadModificar());
											detalleMovimientoList.add(detalleMovimiento);
										}
									}
								} else { // Adicionar
									db.update(sqlString, obj.getId(),i,det.getId(),subdet.getId(),item.getProductoId(),item.getPrecioIndividual(),item.getPrecioCompuesto(),item.getCantidad());
									if(!esProductoFabricado) {
										almacenS.registrarAlmacen(item.getProductoId(),obj.getCodSuc(),item.getCantidad(),obj.getUsuarioId(), HistoricoE.MODIFICACION_CARTILLA_DIARIA.getTipo(),"");
										DetalleMovimiento detalleMovimiento = new DetalleMovimiento(item.getProductoId(),true,true,item.getCantidad(),item.getCantidad());
										detalleMovimientoList.add(detalleMovimiento);
									}
									i++;
								}
							}
						} else {
							throw new RuntimeException("Lista Productos nula");
						}
					}
				} else {
					throw new RuntimeException("Lista DetalleCartilla nula");
				}
			}
			//Luego de adicionar y modificar, revisamos si hay elementos por eliminar datos que se hayan ingresado anteriormente en BD, pero q ahora ya no estan
			if(UtilClass.isNotNullEmpty(detallesBD)) {
				for (DetalleCartillaDiaria data: detallesBD) {
					Optional<ProductoCartillaForm> detalleBd = detallesModificados.stream().filter(it -> it.getCartillaDiariaId() == data.getCartillaDiariaId()
							&& it.getId() == data.getId()).findFirst();
					if(!detalleBd.isPresent()) {
						db.update("delete from detalle_cartilla_diaria where id = ? and cartilla_diaria_id = ?", data.getId(), data.getCartillaDiariaId());
						boolean esProductoFabricado = db.queryForObject("select count(*)>0 from producto p inner join tipo_producto tp on tp.id = p.tipo_id and es_preparado=false and es_comerciable = true where p.id=?", Boolean.class, data.getProductoId());
						if(!esProductoFabricado) {
							almacenS.registrarAlmacen(data.getProductoId(),obj.getCodSuc(),data.getCantidad().negate(),obj.getUsuarioId(), HistoricoE.REVERSION_CARTILLA_DIARIA.getTipo(), "Eliminado al modificar cartilla diaria, y se quito el detalle, cartilla diaria #"+data.getCartillaDiariaId());
							DetalleMovimiento detalleMovimiento = new DetalleMovimiento(data.getProductoId(),true,false,data.getCantidad(),data.getCantidad());
							detalleMovimientoList.add(detalleMovimiento);
						}
					}
				}
			}
		} else {
			throw new RuntimeException("Lista CartillaSucursalForm nula");
		}
	}

	@Override
	@Transactional
	public DataResponse modificar(CartillaDiariaForm obj){
		try {
			if(obj != null && UtilClass.isNotNullEmpty(obj.getCartillaSucursalFormList())) {
				sqlString = "update cartilla_diaria set usuario_id=? where id=?";
				boolean update = db.update(sqlString, obj.getUsuarioId(), obj.getId()) > 0;
//				db.update("delete from detalle_cartilla_diaria where cartilla_diaria_id = ?", obj.getId());
				//Lista de productos agregados
				List<DetalleMovimiento> detalleMovimientoList = new ArrayList<>();
				adicionarDetalles(obj,detalleMovimientoList);
				if(!detalleMovimientoList.isEmpty()) {
					Movimiento movimiento = new Movimiento();
					movimiento.setDetalles(detalleMovimientoList);
					movimiento.setUsuarioRevision(obj.getUsuarioId());
					movimiento.setSucursalOrigen(obj.getCodSuc());
					movimiento.setUpdatedBy(obj.getUsuarioId());
					movimiento.setCreatedBy(obj.getUsuarioId());
					movimiento.setObs("Registrado desde modificacion de cartilla diaria");
					movimiento.setTipo(TipoMovimientoE.MODIFICACION_CARTILLA_DIARIA.getTipo());
					movimiento.setEstadoMovimiento((short)1);//1= estado aceptado
					registroMovimientoCierreDiario(movimiento);
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
	@Transactional
	public DataResponse darEstado(Integer id, Boolean estado) {
		try {
			sqlString = "update cartilla_diaria set estado = ? where id=?";
			boolean update = db.update(sqlString, estado, id) > 0;
			return estado ? Utils.getResponseDataAct(ENTITY, update) : Utils.getResponseDataEli(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public Boolean eliminar(Long cartillaDiariaId,Long user, Integer sucursalId){
		try {
			Short res = db.queryForObject("select cartilla_diaria_eliminar(?,?,?);",Short.class,cartillaDiariaId,user, sucursalId);
			if(res > 0) {
				CartillaDiaria cartilla = obtener(cartillaDiariaId);
				List<DetalleCartillaDiaria> detalles = obtenerDetalles(cartillaDiariaId);
				List<DetalleMovimiento> detallesMovimiento = new ArrayList<>();
				for(DetalleCartillaDiaria item : detalles) {
					if(item.getCantidad() != null && item.getCantidad().floatValue() > 0f) {
						DetalleMovimiento det = new DetalleMovimiento(item.getProductoId(),true,false,item.getCantidad(),item.getCantidad());
						detallesMovimiento.add(det);
					}
				}
				if(!detallesMovimiento.isEmpty()) {
					Movimiento movimiento = new Movimiento();
					movimiento.setDetalles(detallesMovimiento);
					movimiento.setUsuarioRevision(cartilla.getUsuarioId());
					movimiento.setSucursalOrigen(cartilla.getCodSuc());
					movimiento.setUpdatedBy(cartilla.getUsuarioId());
					movimiento.setCreatedBy(cartilla.getUsuarioId());
					movimiento.setObs("Registrado desde eliminacion de cartilla diaria");
					movimiento.setTipo(TipoMovimientoE.REGISTRO_CARTILLA_DIARIA.getTipo());
					movimiento.setEstadoMovimiento((short)1);//1= estado aceptado
					registroMovimientoCierreDiario(movimiento);
				}
				return true;
			} else {
				if(res == -2) {
					throw new RuntimeException("No se puede eliminar, ya que parte de los productos de almacen, no estan completos.");
				} else {
					throw new RuntimeException("Error al realizar la eliminacion de la cartilla diaria");
				}
			}
		} catch (Exception e) {
			logger.error("error al eliminar compra="+e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}
	public CartillaDiariaForm obtenerCartillaDiariaForm (Long cartillaDiariaId) {
		try {
			sqlString = "select * from cartilla_diaria where id = ?";
			List<CartillaDiariaForm> lista = db.query(sqlString, BeanPropertyRowMapper.newInstance(CartillaDiariaForm.class), cartillaDiariaId);
			CartillaDiariaForm obj = UtilClass.getFirst(lista);
			if(obj != null) {
				sqlString = "select distinct cs.* from detalle_cartilla_diaria dcd inner join cartilla_sucursal cs on cs.id = dcd.cartilla_sucursal_id where dcd.cartilla_diaria_id = ?";
				List<CartillaSucursalForm> cartillaSucursalList = db.query(sqlString, BeanPropertyRowMapper.newInstance(CartillaSucursalForm.class), cartillaDiariaId);
				if(UtilClass.isNotNullEmpty(cartillaSucursalList)) {
					sqlString = "select distinct dcs.*,tp.nombre as xtipo_producto,tp.es_preparado,tp.es_comerciable from detalle_cartilla_diaria dcd inner join detalle_cartilla_sucursal dcs on dcd.cartilla_sucursal_id = dcs.cartilla_sucursal_id and dcd.detalle_cartilla_sucursal_id = dcs.id inner join tipo_producto tp on tp.id=dcs.tipo_producto_id where dcd.cartilla_diaria_id =?;";
					List<DetalleCartillaForm> detalleCartillaFormList = db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleCartillaForm.class), cartillaDiariaId);

					sqlString = "select dcd.cartilla_diaria_id,dcd.id,dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id,dcd.id,dcd.producto_id,dcd.precio_individual,dcd.precio_compuesto,dcd.cantidad,coalesce(a.cantidad,0) as cantidad_almacen,p.nombre as xproducto,p.foto from detalle_cartilla_diaria dcd inner join producto p on p.id=dcd.producto_id left join almacen a on a.producto_id = p.id and a.sucursal_id = ? where dcd.cartilla_diaria_id = ?";
					List<ProductoCartillaForm> productoList = db.query(sqlString, BeanPropertyRowMapper.newInstance(ProductoCartillaForm.class), obj.getCodSuc(),cartillaDiariaId);
					if(UtilClass.isNotNullEmpty(productoList)) {
						for(ProductoCartillaForm pro: productoList) {
							for (DetalleCartillaForm det: detalleCartillaFormList) {
								if(det.getId() == pro.getDetalleCartillaSucursalId() && det.getCartillaSucursalId() == pro.getCartillaSucursalId()) {
									if(det.getProductos()!= null) {
										det.getProductos().add(pro);
									} else {
										List<ProductoCartillaForm> aux = new ArrayList<>();
										aux.add(pro);
										det.setProductos(aux);
									}
									break;
								}
							}
						}
					}
					Map<Integer,List<DetalleCartillaForm>> agrupadoPorCartillaSucursal = detalleCartillaFormList.stream().collect(Collectors.groupingBy(DetalleCartillaForm::getCartillaSucursalId));
					for (Map.Entry<Integer,List<DetalleCartillaForm>> item : agrupadoPorCartillaSucursal.entrySet()) {
						if(UtilClass.isNotNullEmpty(item.getValue())) {
							for(CartillaSucursalForm it : cartillaSucursalList) {
								if(it.getId() == item.getKey()) {
									it.setDetalleCartillaList(item.getValue());
									break;
								}
							}
						}
					}
					obj.setCartillaSucursalFormList(cartillaSucursalList);
					return obj;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			System.out.println("Exception founded:"+ex.toString());
			throw new RuntimeException("Error al obtener");
		}
	}
	public CartillaDiariaForm obtenerCartillaActivaSucursal(Integer sucursalId) {
		try {
			sqlString = "select coalesce(max(id),0) from cartilla_diaria cd where estado_cartilla = true and estado = true and cd.cod_suc = ?;";
			Long id = db.queryForObject(sqlString, Long.class, sucursalId);
			if(id == 0) {
				return null;
			} else {
				return obtenerCartillaDiariaForm(id);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	@Transactional
	public DataResponse cerrarCartilla(Integer sucursalId, Long userId, CierreWrap obj){
		try {
			boolean existeComandaPendiente = db.queryForObject("select count(*)> 0 from venta v where estado = true and tipo = 1 and sucursal_id = ?", Boolean.class, sucursalId);
			if(existeComandaPendiente) {
				return new DataResponse(false, "No se puede cerrar, ya que se tiene comandas pendientes, finalice las comandas primeramente");
			}
			if(UtilClass.isNotNullEmpty(obj.getListaCierre())) {
				sqlString = "update detalle_cartilla_diaria set cantidad_editada_final = ?, cantidad_final_almacen = ? where cartilla_diaria_id = ? and id =?;";
				List<DetalleMovimiento> detalleMovimientoList = new ArrayList<>();
				for (CartillaDiariaCierreWrap it : obj.getListaCierre()) {
					BigDecimal diferencia = it.getCantidadFinalAlmacen().subtract(it.getCantidadAlmacen());
					if(diferencia.compareTo(BigDecimal.ZERO) != 0) {
						DetalleMovimiento detalleMovimiento = null;
						if(diferencia.compareTo(BigDecimal.ZERO) > 0) {
							detalleMovimiento = new DetalleMovimiento(it.getProductoId(),true,true,diferencia,diferencia);
						} else {
							detalleMovimiento = new DetalleMovimiento(it.getProductoId(), true,false, diferencia.negate(),diferencia.negate());
						}
						detalleMovimientoList.add(detalleMovimiento);
						db.update(sqlString, diferencia,it.getCantidadFinalAlmacen(), it.getCartillaDiariaId(), it.getId());
						almacenS.registrarAlmacen(it.getProductoId(), sucursalId, diferencia,userId, HistoricoE.MODIFICACION_CIERRE_CARTILLA.getTipo(), "Modificacion por cierre diario, #"+obj.getCartillaDiariaId());
					}
				}
				//Si existe detalles, hay que registrar en movimientos
				if(!detalleMovimientoList.isEmpty()) {
					Movimiento movimiento = new Movimiento();
					movimiento.setDetalles(detalleMovimientoList);
					movimiento.setUsuarioRevision(userId);
					movimiento.setSucursalOrigen(sucursalId);
					movimiento.setUpdatedBy(userId);
					movimiento.setCreatedBy(userId);
					movimiento.setObs("Registrado desde cierre de cartilla diaria");
					movimiento.setTipo(TipoMovimientoE.MODIFICACION_CIERRE_CARTILLA.getTipo());
					movimiento.setEstadoMovimiento((short)1);//1= estado aceptado
					registroMovimientoCierreDiario(movimiento);
				}
			}
			boolean status = db.update("update cartilla_diaria set usuario_cierre =?, fecha_cierre = now(),estado_cartilla = false  where id=?",userId, obj.getCartillaDiariaId())>0;
			return Utils.getResponseDataAdd("Registro Cierre Diario",status);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}
	//Si existe modificacion del inventario, se registra como movimiento
	public void registroMovimientoCierreDiario(Movimiento movimiento){
		DataResponse resp = movimientoS.adicionar(movimiento);
		if(resp.isStatus()) {
			movimiento.setId((long)resp.getData());
			DataResponse resp2 = movimientoS.revisar(movimiento);
			if(!resp2.isStatus()) {
				throw new RuntimeException("Error al revisar el registro del movimiento");
			}
		} else {
			throw new RuntimeException("Error al registrar el movimiento de los productos.");
		}
	}
	public DataResponse obtenerResumenDetalleCierre(Long cartillaDiariaId, Integer sucursalId) {
		try {
			sqlString = "select p.nombre as xproducto, dcd.*,a.cantidad as cantidad_almacen ," +
					"(select coalesce(sum(dv.cantidad_unitaria),0) from detalle_venta dv inner join venta v on v.id = dv.venta_id and v.estado = true where dv.cartilla_diaria_id = dcd.cartilla_diaria_id and dv.detalle_cartilla_diaria_id = dcd.id) as cantidad_vendida " +
					"from detalle_cartilla_diaria dcd " +
					"inner join producto p on p.id = dcd.producto_id " +
					"inner join tipo_producto tp on tp.id = p.tipo_id and tp.es_preparado " +
					"inner join almacen a on a.producto_id = dcd.producto_id and a.sucursal_id = ?" +
					"where dcd.cartilla_diaria_id =?;";
			List<CartillaDiariaCierreWrap> lista = db.query(sqlString, BeanPropertyRowMapper.newInstance(CartillaDiariaCierreWrap.class), sucursalId, cartillaDiariaId);

			if(UtilClass.isNotNullEmpty(lista)) {
				List<CartillaDiariaCierreWrap> productosUnicos = new ArrayList<>();
				boolean esRepetido = false;
				for (CartillaDiariaCierreWrap item : lista) {
					if(!productosUnicos.isEmpty()) {
						esRepetido = false;
						for (CartillaDiariaCierreWrap productosUnico : productosUnicos) {
							if(productosUnico.getProductoId() == item.getProductoId()) {
								esRepetido = true;
							}
						}
						if(!esRepetido) {
							productosUnicos.add(item);
						}
					} else {
						productosUnicos.add(item);
					}
				}
				return new DataResponse(true, productosUnicos,Utils.successGet("Resumen detalle cierre"));
			}
			return new DataResponse(true, lista,Utils.successGet("Resumen detalle cierre"));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}
	public DataResponse obtenerUltimaCartillaDiaria(Integer sucursalId) {
		try {
			sqlString = "select coalesce(max(cd.id),0) from cartilla_diaria cd where cd.cod_suc =? and cd.estado = true and cd.estado_cartilla = false;";
			Integer cartillaId = db.queryForObject(sqlString, Integer.class, sucursalId);
			return new DataResponse(true, cartillaId, "Se realizo con exito");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	public DataResponse duplicar(CartillaDiaria obj){
		try {
			Long id = db.queryForObject("select coalesce(max(id),0)+1 from cartilla_diaria", Long.class);
			sqlString = "insert into cartilla_diaria(id,finicio,usuario_id,cod_suc,estado,estado_cartilla) values(?,cast(? as timestamp),?,?,true,true)";
			boolean save = db.update(sqlString, id, obj.getXfinicio(),obj.getUsuarioId(),obj.getCodSuc()) > 0;
			if(save) {
				sqlString = "insert into detalle_cartilla_diaria(cartilla_diaria_id,id,cartilla_sucursal_id,detalle_cartilla_sucursal_id,producto_id,precio_individual,precio_compuesto,cantidad) " +
						"select ?, dcd.id,dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id ,dcd.producto_id ,dcd.precio_individual,dcd.precio_compuesto,0 " +
						"from detalle_cartilla_diaria dcd where dcd.cartilla_diaria_id = ?;";
				db.update(sqlString, id, obj.getId());
			}
			return new DataResponse(save, Utils.getSuccessFailedAdd(ENTITY, save));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
}
