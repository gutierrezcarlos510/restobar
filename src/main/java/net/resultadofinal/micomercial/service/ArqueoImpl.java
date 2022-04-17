package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.ArqueoWrapMapper;
import net.resultadofinal.micomercial.model.Arqueo;
import net.resultadofinal.micomercial.model.ArqueoTotal;
import net.resultadofinal.micomercial.model.DetalleArqueo;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.CompraVentaWrap;
import net.resultadofinal.micomercial.model.wrap.DetalleResumenArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.ResumenArqueoWrap;
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
public class ArqueoImpl extends DbConeccion implements ArqueoS {
	
	private JdbcTemplate db;
	@Autowired
	public ArqueoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(ArqueoImpl.class);
	private static final String ENTITY = "arqueo de caja";
	private String sqlString;
	public BeanPropertyRowMapper<Arqueo> mapperToArqueoCaja(){
		return new BeanPropertyRowMapper<Arqueo>(Arqueo.class);
	}
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Arqueo> listado(HttpServletRequest request, boolean estado, Long xuser, int xgestion, boolean estaActivoCaja) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("arqueo ac");
			sqlBuilder.setSelect("ac.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) xdelegado,");
			sqlBuilder.setSelectConcat("cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) xcustodio_inicial,");
			sqlBuilder.setSelectConcat("cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) xcustodio_final");
			sqlBuilder.addJoin("persona p1 on p1.cod_per=ac.delegado_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per=ac.custodio_inicial_id");
			sqlBuilder.addLeftJoin("persona p3 on p3.cod_per=ac.custodio_final_id");
			sqlBuilder.setWhere("ac.estado=:xestado and (ac.custodio_inicial_id=:xuser or :xuser=-1) and ac.gestion=:xgestion" + (estaActivoCaja?" and ac.custodio_final_id is null":" and ac.custodio_final_id is not null"));
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xuser",xuser);
			sqlBuilder.addParameter("xgestion",xgestion);
			return utilDataTableS.list(request, Arqueo.class, sqlBuilder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public DataTableResults<DetalleArqueo> listadoDetalles(HttpServletRequest request, boolean estado,Long arqueo) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("detalle_arqueo da");
			sqlBuilder.setSelect("da.*");
			sqlBuilder.setWhere("da.estado=:xestado and da.arqueo_id= :xcod");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xcod", arqueo);
			return utilDataTableS.list(request, DetalleArqueo.class, sqlBuilder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public DataTableResults<DetalleArqueo> listadoDetallesIngreseEgreso(HttpServletRequest request, boolean estado,Long arqueo) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("detalle_arqueo da");
			sqlBuilder.setSelect("da.*");
			sqlBuilder.setWhere("da.estado=:xestado and da.arqueo_id= :xcod and (da.tipo=3 or da.tipo=4)");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xcod", arqueo);
			return utilDataTableS.list(request, DetalleArqueo.class, sqlBuilder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Arqueo obtenerCaja(Long id) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("arqueo a");
			sqlBuilder.setSelect("a.*,concat(p1.nom_per,' ', p1.priape_per) xdelegado,");
			sqlBuilder.setSelectConcat("concat(p2.nom_per,' ', p2.priape_per) xcustodio_inicial,");
			sqlBuilder.setSelectConcat("concat(p3.nom_per,' ', p3.priape_per) xcustodio_final");
			sqlBuilder.addJoin("persona p1 on p1.cod_per = a.delegado_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per = a.custodio_inicial_id");
			sqlBuilder.addLeftJoin("persona p3 on p3.cod_per = a.custodio_final_id");
			sqlBuilder.setWhere("a.id=?");
			List<Arqueo> arqueoList = db.query(sqlBuilder.generate(), BeanPropertyRowMapper.newInstance(Arqueo.class), id);
			return UtilClass.getFirst(arqueoList);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			e.printStackTrace();
			return null;
		}
	}
	public Arqueo arqueocajaVerificarSesionActual(Long codPer, Integer sucursal){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("arqueo ac");
			sqlBuilder.setSelect("ac.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) delegado,");
			sqlBuilder.setSelectConcat("cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,");
			sqlBuilder.setSelectConcat("cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final");
			sqlBuilder.addJoin("persona p1 on p1.cod_per=ac.delegado_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per=ac.custodio_inicial_id");
			sqlBuilder.addLeftJoin("persona p3 on p3.cod_per=ac.custodio_final_id");
			sqlBuilder.setWhere("ac.delegado_id=:userId and ac.sucursal_id = :sucId and ac.custodio_final_id is null and ac.estado=true");
			sqlBuilder.addParameter("userId", codPer);
			sqlBuilder.addParameter("sucId", sucursal);
			List<Arqueo> arqueoCaja=db.query(sqlBuilder.generate(),BeanPropertyRowMapper.newInstance(Arqueo.class));
			Arqueo arqueo = UtilClass.getFirst(arqueoCaja);
			if(arqueo != null) {
				arqueo.setDetalles(obtenerDetallexArqueoCaja(arqueo.getId()));
			}
			return arqueo;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Utils.errorGet("verificacion actual de "+ENTITY, e.toString()));
			return null;
		}
	}
	public Long obtenerArqueoSesionActiva(Long codPer, Integer sucursal){
		try {
			sqlString = "select coalesce(max(ac.id),0) from arqueo ac where ac.delegado_id=? and ac.sucursal_id = ? and ac.custodio_final_id is null and ac.estado=true";
			return db.queryForObject(sqlString,Long.class, codPer, sucursal);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Utils.errorGet("verificacion actual de "+ENTITY, e.toString()));
			return null;
		}
	}
	public List<DetalleArqueo> obtenerDetallexArqueoCaja(Long cod){
		try {
			sqlString = "select * from detalle_arqueo where arqueo_id=? and estado = true;";
			return db.query(sqlString ,BeanPropertyRowMapper.newInstance(DetalleArqueo.class), cod);
		} catch (Exception e) {
//			logger.error(Utils.errorGet("detalles de "+ENTITY, e.toString()));
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<DetalleArqueo> obtenerDetallexArqueoCajaAgrupado(Long cod){
		try {
			sqlString = "select tipo,subcuenta_id,sum(monto) monto from detallearqueo where arqueo_id = ? and estado =true group by tipo,subcuenta_id";
			return db.query(sqlString,BeanPropertyRowMapper.newInstance(DetalleArqueo.class),cod);
		} catch (Exception e) {
			logger.error(Utils.errorGet("detalles agrupados de "+ENTITY, e.toString()));
			return null;
		}
		
	}
	public DetalleArqueo obtenerDetalle(Integer detalleArqueoId,Long arqueoId){
		try {
			sqlString = "select * from detallearqueo where cod_detarq=cod and cod_arqcaj=code_arqcaj;";
			List<DetalleArqueo> lista = db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleArqueo.class),detalleArqueoId, arqueoId);
			return  UtilClass.getFirst(lista);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public DataResponse iniciar(Arqueo ac){
		try {
			Long arqueoId = db.queryForObject("select arqueocaja_iniciar(?,?,?,?,?,?);",Long.class,ac.getDelegadoId(),ac.getCustodioInicialId(),ac.getMontoInicial(),
					ac.getGestion(),ac.getDescripcion(),ac.getSucursalId());
			if(arqueoId > 0) {
				return new DataResponse(true, arqueoId, Utils.getSuccessFailedAdd("Arqueo inicial",true));
			} else {
				if(arqueoId == -1) {
					return new DataResponse(false, Utils.getSuccessFailedAdd("Arqueo inicial",false));
				} else {
					return new DataResponse(false, "No se realizo el arqueo, ya que existe una sesion del usuario abierta. Por favor cierre primeramente.");
				}
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd("iniciar "+ENTITY, e.toString()));
			throw new RuntimeException("Error al iniciar caja: "+e.getMessage());
		}
	}
	public Boolean modificar(Arqueo ac){
		try {
//			logger.info("modificar: "+ac.getDel_arqcaj()+" | "+ac.getCusini_arqcaj()+" | "+ac.getMonini_arqcaj()+" | "+ac.getGes_gen()+" | "+ac.getDes_arqcaj()+" | "+ac.getCod_arqcaj()+" | "+ac.getCod_suc());
			sqlString ="update arqueo set (delegado_id,custodio_inicial_id,finicio,monto_inicial,gestion,descripcion,sucursal_id)=(?,?,now(),?,?,?,?) where id=?;";
			return db.update(sqlString,ac.getDelegadoId(),ac.getCustodioInicialId(), ac.getMontoInicial(),ac.getGestion(),ac.getDescripcion(), ac.getSucursalId(),ac.getId()) > 0;
		} catch (Exception e) {
//			logger.error(Utils.errorMod(ENTITY, e.toString()));
			e.printStackTrace();
			return false;
		}
	}
	public Boolean cerrar(Arqueo ac){
		try {
//			logger.info("cerrar: "+ac.getCusfin_arqcaj()+" | "+ac.getMonfin_arqcaj()+" | "+ac.getDes_arqcaj()+" | "+ac.getMonrea_arqcaj()+" | "+ac.getCod_arqcaj());
			sqlString = "select (select coalesce(sum(monto),0) from detalle_arqueo da where da.arqueo_id = ? and es_debe = true)-(select coalesce(sum(monto),0) from detalle_arqueo da where da.arqueo_id = ? and es_debe = false) + (select monto_inicial from arqueo a where a.id = ?);";
			BigDecimal montoFinal = db.queryForObject(sqlString, BigDecimal.class, ac.getId(),ac.getId(),ac.getId());
			sqlString = "update arqueo set (custodio_final_id,ffin,monto_final,descripcion,monto_real)=(?,now(),?,?,?) where id=?;";
			return db.update(sqlString,ac.getCustodioFinalId(),montoFinal, ac.getDescripcion(), ac.getMontoReal(),ac.getId())>0;
		} catch (Exception e) {
//			logger.error(Utils.errorAdd("cerrar "+ENTITY, e.toString()));
			e.printStackTrace();
			return false;
		}
	}
	public Boolean darEstado(Long cod,Boolean est,Long cod_per){
		try {
//			logger.info("darEstado: "+cod+" | "+est+" | "+cod_per);
			sqlString = "update arqueo set estado=?,custodio_final_id=?,ffin=now() where id=?;";
			return db.update(sqlString,est,cod_per,cod)>0;
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			return false;
		}
	}
	public BigDecimal obtenertotal(Long cod){
		try {
			return db.queryForObject("select arqueocaja_obtenertotal(?);",BigDecimal.class,cod);
		} catch (Exception e) {
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}
	@Transactional
	public Short adicionarDetalle(DetalleArqueo d){
		try {
			sqlString = "select detalle_arqueo_adicionar(?,?,?,?,?,?)";
			short detalleId = db.queryForObject(sqlString, Short.class, d.getArqueoId(),d.getTipo(),d.getDescripcion(),d.getMonto(),d.getFormaPagoId(),d.getEsDebe());
			return detalleId > 0 ? detalleId : (short)-1;
		} catch (Exception e) {
//			logger.error(Utils.errorAdd("detalles de "+ENTITY, e.toString()));
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public ArqueoTotal obtenerTotal(Long codArqCaj) {
		try {
			sqlString = "select a.id, " +
					"(select coalesce(sum(monto),0) from detalle_arqueo d where d.arqueo_id=a.id and d.tipo=5) tingresos," +
					"(select coalesce(sum(monto),0) from detalle_arqueo d where d.arqueo_id=a.id and d.tipo=1) tegresos," +
					"(select coalesce(sum(monto),0) from detalle_arqueo d where d.arqueo_id=a.id and d.tipo=8) tventas," +
					"(select coalesce(sum(monto),0) from detalle_arqueo d where d.arqueo_id=a.id and d.tipo=4) tcompras " +
					"from arqueo a where a.id = ?;";
			return db.queryForObject(sqlString, BeanPropertyRowMapper.newInstance(ArqueoTotal.class), codArqCaj);
		} catch (Exception e) {
			logger.error(Utils.errorGet("total de "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public boolean eliminarDetalle(Long codArqcaj, Integer codDetArq) {
		try {
//			logger.info("eliminarDetalle: "+codArqcaj+" | "+codDetArq);
			return db.update("delete from detalle_arqueo where arqueo_id =? and id =?;",codArqcaj,codDetArq)>0;
		} catch (Exception e) {
			logger.error(Utils.errorEli("detalle de "+ENTITY, e.toString()));
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public CompraVentaWrap obtenerDescuentoCompra(Long codArqcaj) {
		try {
			sqlString = "select string_agg(concat('#',cast(c.cod_com as text)),',') lista,coalesce(sum(v.des_com),0) total from compra c.est_com = true and c where c.cod_arqcaj =? ";
			List<CompraVentaWrap> lista = db.query(sqlString, BeanPropertyRowMapper.newInstance(CompraVentaWrap.class),codArqcaj);
			if(lista!=null && !lista.isEmpty()) {
				return lista.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error(Utils.errorEli("descuento de compra en "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public CompraVentaWrap obtenerDescuentoVenta(Long codArqcaj) {
		try {
			List<CompraVentaWrap> lista = db.query("select string_agg(concat('#',cast(v.cod_ven as text)),',') lista,coalesce(sum(v.des_ven),0) total from venta v where v.est_ven = true and v.cod_arqcaj =? and cod_subcuenta is null",
					BeanPropertyRowMapper.newInstance(CompraVentaWrap.class),codArqcaj);
			if(lista!=null && !lista.isEmpty()) {
				return lista.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error(Utils.errorEli("descuento de venta en "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public CompraVentaWrap obtenerDescuentoVentaPorBanco(Long codArqcaj, Integer codSubcuenta) {
		try {
			List<CompraVentaWrap> lista = db.query("select string_agg(concat('#',cast(v.cod_ven as text)),',') lista,coalesce(sum(v.des_ven),0) total from venta v where v.est_ven = true and v.cod_arqcaj =? and cod_subcuenta =?",
					BeanPropertyRowMapper.newInstance(CompraVentaWrap.class),codArqcaj, codSubcuenta);
			if(lista!=null && !lista.isEmpty()) {
				return lista.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error(Utils.errorEli("descuento de venta por banco en "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public ResumenArqueoWrap obtenerResumenArqueo(Long codArqcaj) {
		try {
			sqlString = "select a.id,concat(p.nom_per, ' ', p.priape_per) as xusuario,a.finicio,a.ffin,a.custodio_final_id is null es_activo from arqueo a inner join persona p on p.cod_per = a.delegado_id where a.id = ?;";
			List<ResumenArqueoWrap> resumenList = db.query(sqlString, BeanPropertyRowMapper.newInstance(ResumenArqueoWrap.class),codArqcaj);
			ResumenArqueoWrap resumen = UtilClass.getFirst(resumenList);
			if(resumen != null) {
				sqlString = "select fp.id,fp.nombre,da.es_debe , fp.es_efectivo_caja,sum(da.monto) as total from detalle_arqueo da " +
						"inner join forma_pago fp on fp.id = da.forma_pago_id where da.arqueo_id = ? group by fp.id,fp.nombre,da.es_debe, fp.es_efectivo_caja order by fp.nombre, da.es_debe";
				List<DetalleResumenArqueoWrap> detalleResumen = db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleResumenArqueoWrap.class), codArqcaj);
				resumen.setDetalleResumen(detalleResumen);
			}
			return resumen;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public Arqueo obtenerUltimaCajaPorUsuario(Long codUsuario, Integer sucursal){
		try {
			String sql = "SELECT ac.* FROM arqueo ac where ac.id = (select coalesce(max(a2.id),0) from arqueo a2 where a2.delegado_id = ? and a2.sucursal_id = ? and a2.estado=true);";
			List<Arqueo> arqueoList = db.query(sql, BeanPropertyRowMapper.newInstance(Arqueo.class),codUsuario, sucursal);
			return UtilClass.getFirst(arqueoList);
		} catch (Exception e) {
			logger.error(Utils.errorGet("Error obtenerUltimaCajaPorUsuario: "+ENTITY, e.toString()));
			return null;
		}
	}
	public Boolean rehabilitarArqueo(Long cod){
		try {
			return db.update("update arqueo set (custodio_final_id,ffin,monto_final,monto_real)=(null,null,null,null) where id=?;",cod)>0;
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			return false;
		}
	}
	public Arqueo obtenerUltimoArqueoUsuario(Long userId, Integer sucursalId){
		try {
			sqlString = "select coalesce(max(a.id),0) from arqueo a where delegado_id = ? and a.estado = true and sucursal_id = ?";
			Long arqueoId = db.queryForObject(sqlString, Long.class, userId, sucursalId);
			if(arqueoId > 0) {
				return obtenerCaja(arqueoId);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al obtener ultimo arqueo de usuario");
		}
	}
	public DataResponse existeVentasConArqueo(Long arqueoId){
		try {
			sqlString = "select count(*) from venta v where arqueo_id = ? and estado = true";
			Long cantidadVentas = db.queryForObject(sqlString, Long.class, arqueoId);
			if(cantidadVentas > 0) {
				return new DataResponse(true, "Existe ventas anexadas a este arqueo, favor de revertir primeramente las ventas, realizadas");
			} else {
				return new DataResponse(false, "Sin ventas realizadas con este arqueo");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al obtener ultimo arqueo de usuario");
		}
	}
}