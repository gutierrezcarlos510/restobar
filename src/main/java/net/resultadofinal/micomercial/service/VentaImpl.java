package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.enumeration.TipoArqueoE;
import net.resultadofinal.micomercial.mappers.DetalleventaMapper;
import net.resultadofinal.micomercial.mappers.VentaMapper;
import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.model.form.DetalleVentaForm;
import net.resultadofinal.micomercial.model.form.VentaForm;
import net.resultadofinal.micomercial.model.wrap.*;
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
	private void adicionarDetalleHistoricoVenta(Long ventaId, Long productoId, BigDecimal cantidad, Short historicoVentaId){
		try {
			sqlString = "insert into detalle_historico_venta(venta_id, producto_id, cantidad, esta_impreso, historico_venta_id) values(?,?,?,false,?)";
			db.update(sqlString, ventaId, productoId, cantidad,historicoVentaId);
		} catch (Exception ex) {
			throw new RuntimeException("Error al crear historico de venta");
		}
	}
	private Short adicionarHistoricoVenta(Long ventaId){
		try {
			Short historicoVentaId = db.queryForObject("select coalesce(max(id),0)+1 from historico_venta where venta_id=?", Short.class, ventaId);
			sqlString = "insert into historico_venta(venta_id, fecha, id) values(?,now(),?)";
			db.update(sqlString, ventaId, historicoVentaId);
			return historicoVentaId;
		} catch (Exception ex) {
			throw new RuntimeException("Error al crear historico de venta");
		}
	}
	public String validarExistenciaPreviaProductos(Integer sucursalId,List<DetalleVentaForm> productos, List<DetalleVentaForm> compuestos){
		String faltantes = "";
		boolean existe = false;
		if(UtilClass.isNotNullEmpty(productos)) {
			if(UtilClass.isNotNullEmpty(compuestos)) {
				for (DetalleVentaForm compuesto: compuestos) {
					existe = false;
					for (DetalleVentaForm producto: productos) {
						if(compuesto.getProductoId() == producto.getProductoId()) {
							producto.setCantidadUnitaria(producto.getCantidadUnitaria().add(compuesto.getCantidadUnitaria()));
							existe = true;
							break;
						}
					}
					if(!existe) {
						productos.add(compuesto);
					}
				}
			}
		} else {
			if(UtilClass.isNotNullEmpty(compuestos)) {
				productos = compuestos;
			} else {
				faltantes = "No se encontraron detalles";
			}
		}
		if(!faltantes.isEmpty()) {
			return faltantes;
		}

		return validarFaltantesLista(faltantes, productos, sucursalId);
	}
	public String validarFaltantesLista(String faltantes, List<DetalleVentaForm> productos, Integer sucursalId) {
		if(UtilClass.isNotNullEmpty(productos)) {
			sqlString ="select * from almacen where sucursal_id = ? and producto_id in (";
			for (DetalleVentaForm det : productos) {
				sqlString += det.getProductoId() + ",";
			}
			sqlString = sqlString.substring(0, sqlString.length()-1) + ")";
			List<Almacen> almacenList = db.query(sqlString, BeanPropertyRowMapper.newInstance(Almacen.class), sucursalId);
			if(UtilClass.isNotNullEmpty(almacenList)) {
				for (DetalleVentaForm prod : productos) {
					Almacen alm = almacenList.stream().filter(it -> it.getProductoId().equals(prod.getProductoId())).findFirst().get();
					if(alm != null) {
						if(prod.getCantidadUnitaria().compareTo(alm.getCantidad()) > 0) {
							faltantes += prod.getXproducto() + " = " + alm.getCantidad() + ", ";
						}
					} else {
						faltantes += prod.getXproducto() + " = No existe" ;
					}
				}
				if(!faltantes.isEmpty()) {
					faltantes = "Cantidad actual de los siguentes productos: "+ faltantes.substring(0, faltantes.length()-2);
				}
			} else {
				faltantes = "No se encontraron ningun producto en almacen";
			}
		}
		return faltantes;
	}
	private List<DetalleVentaForm> copyListObject(List<DetalleVentaForm> lista) {
		List<DetalleVentaForm> auxDetalle = new ArrayList<>();
		for (DetalleVentaForm d: lista) {
			auxDetalle.add(copyObject(d));
		}
		return auxDetalle;
	}
	private DetalleVentaForm copyObject(DetalleVentaForm d) {
		DetalleVentaForm it = new DetalleVentaForm();
		it.setProductoId(d.getProductoId());
		it.setCantidad(d.getCantidad());
		it.setCantidadUnitaria(d.getCantidadUnitaria());
		it.setId(d.getId());
		it.setPrecio(d.getPrecio());
		it.setCartillaDiariaId(d.getCartillaDiariaId());
		it.setCartillaSucursalId(d.getCartillaSucursalId());
		it.setDetalleCartillaDiariaId(d.getDetalleCartillaDiariaId());
		it.setXproducto(d.getXproducto());
		it.setXtipoProducto(d.getXtipoProducto());
		it.setEsCompuesto(d.getEsCompuesto());
		it.setTipoVenta(d.getTipoVenta());
		return it;
	}
	@Transactional
	public DataResponse guardarComanda(VentaForm obj) {
		try {
			List<DetalleVentaForm> auxDetalle = copyListObject(obj.getDetalleVenta());
			List<DetalleVentaForm> auxDetalleCompuesto = copyListObject(obj.getDetalleVentaCompuesto());
			String msgValidacion = validarExistenciaPreviaProductos(obj.getSucursalId(), auxDetalle, auxDetalleCompuesto);
			if(!msgValidacion.isEmpty()) { // Si es invalido cantidad en almacen
				return new DataResponse(false, msgValidacion);
			}
			Long ventaId = db.queryForObject("select coalesce(max(id),0)+1 from venta where sucursal_id =?", Long.class, obj.getSucursalId());
			obj.setId(ventaId);
			sqlString = "insert into venta(id, numero, usuario_id, cliente_id, fecha, obs, total, descuento, costo_adicional, gestion, estado, tipo, sucursal_id, subtotal, mesa_id, cantidad_personas, forma_pago_id, total_pagado, total_cambio, created_by, created_at) " +
					"VALUES(?, ?, ?, ?, now(), ?, ?, ?, ?,?, true, ?, ?, ?, ?, ?, ?, ?, ?, ?, now());";
			if(obj.getTipo() == 2) { // si es venta finalizada
				Long numero = db.queryForObject("select coalesce(max(numero),0)+1 from venta where sucursal_id = ?", Long.class, obj.getSucursalId());
				obj.setNumero(numero);
			}
			boolean saveVenta = db.update(sqlString,ventaId, obj.getNumero(),obj.getUsuarioId(), obj.getClienteId(),obj.getObs(), obj.getTotal(), obj.getDescuento(),obj.getCostoAdicional(), obj.getGestion(),
					obj.getTipo(), obj.getSucursalId(),obj.getSubtotal(),obj.getMesaId(), obj.getCantidadPersonas(),obj.getFormaPagoId(),obj.getTotalPagado(),obj.getTotalCambio(),obj.getCreatedBy())>0;
			if(saveVenta) {
				// Se registra el historico de venta
				Short historicoVentaId = adicionarHistoricoVenta(ventaId);

				short detalleId = 1;
				detalleId = adicionarListaDetalleComanda(ventaId, detalleId, obj.getSucursalId(), obj.getUsuarioId(), obj.getDetalleVenta(), historicoVentaId);
				detalleId = adicionarListaDetalleComanda(ventaId, detalleId, obj.getSucursalId(), obj.getUsuarioId(), obj.getDetalleVentaCompuesto(), historicoVentaId);
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
	private short adicionarListaDetalleComanda(Long ventaId, short detalleId, Integer sucursalId, Long userId, List<DetalleVentaForm> detalles,Short historicoVentaId) {
		if(detalles != null && !detalles.isEmpty()) {
			for (DetalleVentaForm det: detalles) {
				adicionarDetalleComanda(ventaId,detalleId,sucursalId,userId, det);
				detalleId++;
				//adicionar detalle de venta historico
				adicionarDetalleHistoricoVenta(ventaId, det.getProductoId(), det.getCantidadUnitaria(), historicoVentaId);
			}
		}
		return detalleId;
	}
	private void adicionarDetalleComanda(Long ventaId, short detalleId, Integer sucursalId, Long userId, DetalleVentaForm det) {
		sqlString ="INSERT INTO detalle_venta(venta_id, id, producto_id, precio, cantidad, descuento, subtotal, total, cartilla_diaria_id, detalle_cartilla_diaria_id, es_compuesto, tipo_venta, cantidad_unitaria) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		db.update(sqlString, ventaId, detalleId, det.getProductoId(), det.getPrecio(), det.getCantidad(), 0, 0, det.getTotal(), det.getCartillaDiariaId(),
				det.getDetalleCartillaDiariaId(), det.getEsCompuesto() == null ? false : det.getEsCompuesto(), det.getTipoVenta() == null ? 1: det.getTipoVenta(), det.getCantidadUnitaria());
		almacenS.registrarAlmacen(det.getProductoId(), sucursalId, det.getCantidadUnitaria().negate(), userId, VENTA_PRODUCTO, "Disminucion por venta #"+ventaId);
	}
	public String validarExistenciaPreviaProductosActualizacion(Integer sucursalId,List<DetalleVentaForm> detalleVentaRequest, List<DetalleVentaForm> compuestos,List<DetalleVentaForm> detallesBD){
		List<DetalleVentaForm> listaValidar = new ArrayList<>();
		revisarObtenerDetalleValidacion(detalleVentaRequest,listaValidar,detallesBD, false);
		revisarObtenerDetalleValidacion(compuestos,listaValidar,detallesBD, true);
		return validarFaltantesLista("", listaValidar, sucursalId);
	}
	public void revisarObtenerDetalleValidacion(List<DetalleVentaForm> detalleVentaRequest, List<DetalleVentaForm> listaValidar,List<DetalleVentaForm> detallesBD, boolean esCompuesto){
		if(detallesBD != null && !detallesBD.isEmpty()) {
			for (DetalleVentaForm det : detalleVentaRequest) {
				if (det.getCartillaDiariaId() != null && det.getDetalleCartillaDiariaId() != null) { //Es preparado
					Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
							&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && it.getProductoId() == det.getProductoId() && it.getEsCompuesto() == det.getEsCompuesto()).findFirst();
					if (!found.isPresent()) {
						//Adicionar DB
						ingresarProductoListaValidacion(listaValidar, det);
					}
				} else { // Es producto
					Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getProductoId() == det.getProductoId()).findFirst();
					if (!found.isPresent()) {
						//Adicionar DB
						ingresarProductoListaValidacion(listaValidar, det);;
					}
				}
			}
			//Revisar los detalles de ventas eliminados
			for (DetalleVentaForm det : detallesBD) {
				if (det.getEsCompuesto() == esCompuesto) {
					Optional<DetalleVentaForm> found = detalleVentaRequest.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
							&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && det.getProductoId() == it.getProductoId()).findFirst();
					if (found.isPresent()) {// Si se encuentra se modifica segun
						BigDecimal diferencia = found.get().getCantidadUnitaria().subtract(det.getCantidadUnitaria());
						if (diferencia.compareTo(new BigDecimal(0)) > 0) {
							det.setCantidadUnitaria(diferencia);
							listaValidar.add(det);
						}
					}
				}
			}
		}
	}
	public void ingresarProductoListaValidacion(List<DetalleVentaForm> listaValidar, DetalleVentaForm det) {
		boolean existe = false;
		for (DetalleVentaForm item : listaValidar) {
			if(item.getProductoId() == det.getProductoId()) {
				existe= true;
				item.setCantidadUnitaria(item.getCantidadUnitaria().add(det.getCantidadUnitaria()));
			}
		}
		if(!existe) {
			listaValidar.add(det);
		}
	}
	@Transactional
	public DataResponse actualizarComanda(VentaForm obj) {
		try {
			List<DetalleVentaForm> detallesBD = obtenerDetallesForm(obj.getId(), null);
			List<DetalleVentaForm> auxDetalle = copyListObject(obj.getDetalleVenta());
			List<DetalleVentaForm> auxDetalleCompuesto = copyListObject(obj.getDetalleVentaCompuesto());
			String msgValidacion = validarExistenciaPreviaProductosActualizacion(obj.getSucursalId(),auxDetalle,auxDetalleCompuesto,detallesBD);
			if(!msgValidacion.isEmpty()) { // Si es invalido cantidad en almacen
				return new DataResponse(false, msgValidacion);
			}
			sqlString = "update venta set numero=?,tipo=?,usuario_id=?, cliente_id=?, obs=?, total=?, descuento=?, costo_adicional=?, subtotal=?, mesa_id=?, cantidad_personas=?, updated_by=?, updated_at=now() where id=?";
			if(obj.getTipo() == 2) { // si es venta finalizada
				Long numero = db.queryForObject("select coalesce(max(numero),0)+1 from venta where sucursal_id = ?", Long.class, obj.getSucursalId());
				obj.setNumero(numero);
			}
			boolean saveVenta = db.update(sqlString,obj.getNumero(),obj.getTipo(),obj.getUsuarioId(), obj.getClienteId(),obj.getObs(), obj.getTotal(), obj.getDescuento(), obj.getCostoAdicional(),obj.getSubtotal(),obj.getMesaId(),
					obj.getCantidadPersonas(),obj.getCreatedBy(), obj.getId())>0;
			if(saveVenta) {
				short detalleVentaId = db.queryForObject("select coalesce(max(id),0)+1 from detalle_venta where venta_id=?", Short.class, obj.getId());
				Short historicoVentaId = null;
				detalleVentaId = adicionarDetallesComandaModificar(detalleVentaId, detallesBD,obj.getDetalleVenta(),obj.getSucursalId(), obj.getCreatedBy(),obj.getId(),false, historicoVentaId);
				detalleVentaId = adicionarDetallesComandaModificar(detalleVentaId, detallesBD,obj.getDetalleVentaCompuesto(),obj.getSucursalId(), obj.getCreatedBy(),obj.getId(),true, historicoVentaId);
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
	private void adicionarDetalleHistoricoVentaModificar(Long ventaId, Long productoId,BigDecimal cantidad, Short historicoVentaId) {
		try {
			if(historicoVentaId == null) {
				historicoVentaId = adicionarHistoricoVenta(ventaId);
			}
			adicionarDetalleHistoricoVenta(ventaId, productoId, cantidad, historicoVentaId);
		} catch (Exception ex) {
			throw new RuntimeException("error al guardar historico");
		}
	}
	private short adicionarDetallesComandaModificar(short detalleVentaId,List<DetalleVentaForm> detallesBD,List<DetalleVentaForm> detalleVentaRequest,
													Integer sucId, Long userId, Long ventaId, boolean soloCompuestos, Short historicoVentaId){

		if(detallesBD != null && !detallesBD.isEmpty()) {
			for (DetalleVentaForm det: detalleVentaRequest) {
				if(det.getCartillaDiariaId() != null && det.getDetalleCartillaDiariaId() != null) { //Es preparado
					Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getCartillaDiariaId() == det.getCartillaDiariaId()
							&& it.getDetalleCartillaDiariaId() == det.getDetalleCartillaDiariaId() && it.getProductoId() == det.getProductoId() && it.getEsCompuesto() == det.getEsCompuesto()).findFirst();
					if(!found.isPresent()) {
						//Adicionar DB
						adicionarDetalleComanda(ventaId,detalleVentaId,sucId,userId,det);
						detalleVentaId++;
						//Adicionar historico
						adicionarDetalleHistoricoVentaModificar(ventaId,det.getProductoId(),det.getCantidadUnitaria(),historicoVentaId);
					}
				} else { // Es producto
					Optional<DetalleVentaForm> found = detallesBD.stream().filter(it -> it.getProductoId() == det.getProductoId()).findFirst();
					if(!found.isPresent()) {
						//Adicionar DB
						adicionarDetalleComanda(ventaId,detalleVentaId,sucId,userId,det);
						detalleVentaId++;
						//Adicionar historico
						adicionarDetalleHistoricoVentaModificar(ventaId,det.getProductoId(),det.getCantidadUnitaria(),historicoVentaId);
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
						almacenS.registrarAlmacen(det.getProductoId(), sucId, det.getCantidad(), userId,
								REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta");
						//Adicionar historico
						adicionarDetalleHistoricoVentaModificar(ventaId,det.getProductoId(),det.getCantidadUnitaria().negate(),historicoVentaId);
					} else { // Si se encuentra se modifica segun
						BigDecimal diferencia = found.get().getCantidadUnitaria().subtract(det.getCantidadUnitaria());
						if(diferencia.compareTo(new BigDecimal(0)) != 0) {
							//update
							db.update("update detalle_venta set cantidad = ?, cantidad_unitaria = ?, precio =?, tipo_venta =? ,total = ? where venta_id = ? and id = ?",
									found.get().getCantidad(),found.get().getCantidadUnitaria(), found.get().getPrecio(), found.get().getTipoVenta(), found.get().getTotal(), det.getVentaId(), det.getId());
							if(diferencia.compareTo(new BigDecimal(0)) > 0){ // Si es positiva, se aumento la diferencia, caso contrario se disminuyo la diferencia
								//disminuir almacen segun la cantidad: diferencia
								almacenS.registrarAlmacen(det.getProductoId(), sucId, diferencia.negate(), userId,
										VENTA_PRODUCTO, "Aumento detalle, por venta # "+ventaId);
								//Adicionar historico
							} else {
								//aumentar almacen segun la cantidad: diferencia
								almacenS.registrarAlmacen(det.getProductoId(), sucId, diferencia, userId,
										REVERSION_VENTA_PRODUCTO, "Reversion detalle de venta #"+ ventaId);
							}
							//Adicionar historico
							adicionarDetalleHistoricoVentaModificar(ventaId,det.getProductoId(),diferencia,historicoVentaId);
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
			sqlString = "select v.id,v.numero,v.arqueo_id,v.gestion,v.detalle_arqueo_id,v.tipo,v.obs,v.fecha,v.cantidad_personas,v.created_at,v.total,v.descuento,v.subtotal, v.total_pagado, v.total_cambio,v.costo_adicional," +
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
				List<DetalleVentaWrap> detallesGlobales = new ArrayList<>();
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
						detallesGlobales.add(d);
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
								BigDecimal cantidadTotalGrupo = new BigDecimal(0);
								SubDetalleVentaWrap tipoCombo = new SubDetalleVentaWrap();
								tipoCombo.setXtipoProducto(tipoComboItem.getValue().get(0).getXtipoProducto());
								List<ProductoDetalleVenta> productoDetalleVentaList = new ArrayList<>();
								for (DetalleVentaForm producto: tipoComboItem.getValue()) {
									cantidadTotalGrupo = cantidadTotalGrupo.add(producto.getCantidadUnitaria());
									ProductoDetalleVenta pro = new ProductoDetalleVenta();
									pro.setXproducto(producto.getXproducto());
									pro.setCantidad(producto.getCantidadUnitaria());
									productoDetalleVentaList.add(pro);
								}
								detalleVentaCombo.setCantidad(cantidadTotalGrupo);
								detalleVentaCombo.setTotal(detalleVentaCombo.getPrecio().multiply(detalleVentaCombo.getCantidad()));
								tipoCombo.setProductos(productoDetalleVentaList);
								subdetalleVentaList.add(tipoCombo);
							}
							detallesGlobales.add(detalleVentaCombo);
							detalleVentaCombo.setSubdetallesCompuestos(subdetalleVentaList);
							detalleVentaComboList.add(detalleVentaCombo);
						}
						venta.setDetalleVentaCompuesto(detalleVentaComboList);
					}
				}
				venta.setDetalleVentaGlobal(detallesGlobales);
			}
			return venta;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			e.printStackTrace();
			return null;
		}
	}
	public DataTableResults<VentaPedidoWrap> listadoPedidoCocina(HttpServletRequest request, int xsucursal, Short area) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("historico_venta hv");
			sqlBuilder.setSelect("hv.venta_id,hv.id historico_venta_id,hv.fecha,m.nombre as xmesa,concat(p2.nom_per, ' ', p2.priape_per) as xusuario");
			sqlBuilder.setSelectConcat(",concat(p3.nom_per, ' ', p3.priape_per) as xcliente,tp.area_destino");
			sqlBuilder.addJoin("detalle_historico_venta dhv on dhv.venta_id = hv.venta_id and hv.id = dhv.historico_venta_id and dhv.esta_impreso = false");
			sqlBuilder.addJoin("producto p on p.id = dhv.producto_id");
			sqlBuilder.addJoin("tipo_producto tp on tp.id = p.tipo_id and (tp.area_destino = :xarea or -1 = :xarea)");
			sqlBuilder.addJoin("venta v on v.id = hv.venta_id and v.estado = true and v.tipo = 1");
			sqlBuilder.addJoin("mesa m on m.id = v.mesa_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per = v.usuario_id");
			sqlBuilder.addJoin("persona p3 on p3.cod_per = v.cliente_id");
			sqlBuilder.setGroupBy("hv.venta_id,hv.id,hv.fecha,m.nombre,p2.nom_per, p2.priape_per,p3.nom_per, p3.priape_per,tp.area_destino");
			sqlBuilder.addParameter("xarea", area);
			sqlBuilder.addParameter("xsucursal",xsucursal);
			return utilDataTableS.list(request, VentaPedidoWrap.class, sqlBuilder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public DataResponse registrarPedidoRealizado(Long ventaId, Short historicoVentaId, Short areaId){
		try {
			sqlString = "update detalle_historico_venta set esta_impreso = true from (select dhv.venta_id,dhv.historico_venta_id, producto_id from detalle_historico_venta dhv " +
					"inner join producto p on p.id = dhv.producto_id " +
					"inner join tipo_producto tp on tp.id = p.tipo_id and (tp.area_destino = ? or -1 = ?) " +
					"where dhv.venta_id = ? and dhv.historico_venta_id = ?) subquery " +
					"where subquery.venta_id=detalle_historico_venta.venta_id and subquery.historico_venta_id=detalle_historico_venta.historico_venta_id and subquery.producto_id=detalle_historico_venta.producto_id ";
			db.update(sqlString, areaId, areaId, ventaId, historicoVentaId);
			return new DataResponse(true, "Se realizo con exito");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	public HistoricoVenta obtenerHistoricoVenta(Long ventaId, Short historicoId) {
		try {
			sqlString = "select hv.venta_id,hv.fecha,m.nombre as xmesa,concat(p2.nom_per, ' ', p2.priape_per) as xusuario,concat(p3.nom_per, ' ', p3.priape_per) as xcliente from historico_venta hv " +
					"inner join venta v on v.id = hv.venta_id " +
					"inner join mesa m on m.id = v.mesa_id " +
					"inner join persona p2 on p2.cod_per = v.usuario_id " +
					"inner join persona p3 on p3.cod_per = v.cliente_id " +
					"where hv.venta_id = ? and hv.id =?";
			List<HistoricoVenta> lista = db.query(sqlString, BeanPropertyRowMapper.newInstance(HistoricoVenta.class), ventaId, historicoId);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			throw new RuntimeException("Error: "+e.getMessage());
		}
	}
}