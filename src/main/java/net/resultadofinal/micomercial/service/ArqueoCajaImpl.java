package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.ArqueoWrapMapper;
import net.resultadofinal.micomercial.model.ArqueoCaja;
import net.resultadofinal.micomercial.model.ArqueoTotal;
import net.resultadofinal.micomercial.model.DetalleArqueo;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.CompraVentaWrap;
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
import java.util.Map;

@Service
public class ArqueoCajaImpl extends DbConeccion implements ArqueoCajaS {
	
	private JdbcTemplate db;
	@Autowired
	public ArqueoCajaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(ArqueoCajaImpl.class);
	private static final String ENTITY = "arqueo de caja";
	@Autowired
	private GeneralS generalS;
	private String sqlString;
	public BeanPropertyRowMapper<ArqueoCaja> mapperToArqueoCaja(){
		return new BeanPropertyRowMapper<ArqueoCaja>(ArqueoCaja.class);
	}
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<ArqueoCaja> listado(HttpServletRequest request, boolean estado,Long xuser, int xgestion) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("arqueo ac");
			sqlBuilder.setSelect("ac.*cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) xdelegado,");
			sqlBuilder.setSelectConcat("cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) xcustodio_inicial,");
			sqlBuilder.setSelectConcat("cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) xcustodio_final");
			sqlBuilder.addJoin("persona p1 on p1.cod_per=ac.delegado_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per=ac.custodio_inicial_id");
			sqlBuilder.addLeftJoin("persona p3 on p3.cod_per=ac.custodio_final_id");
			sqlBuilder.setWhere("ac.estado=:xestado and (ac.custodio_inicial_id=:xuser or :xuser=-1) and ac.gestion=:xgestion");
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xuser",xuser);
			sqlBuilder.addParameter("xgestion",xgestion);
			return utilDataTableS.list(request, ArqueoCaja.class, sqlBuilder);
		} catch (Exception e) {
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
	@Override
	public ArqueoCaja obtenerCaja(Long id) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("arqueo a");
			sqlBuilder.setSelect("a.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) xdelegado,");
			sqlBuilder.setSelectConcat("cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,");
			sqlBuilder.setSelectConcat("cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final");
			sqlBuilder.addJoin("persona p1 on p1.cod_per=ac.delegado_id");
			sqlBuilder.addJoin("persona p2 on p2.cod_per=ac.custodio_inicial_id");
			sqlBuilder.addLeftJoin("persona p3 on p3.cod_per=ac.custodio_final_id");
			sqlBuilder.setWhere("id=?");
			List<ArqueoCaja> arqueoList = db.query(sqlBuilder.generate(), new BeanPropertyRowMapper<ArqueoCaja>(ArqueoCaja.class), id);
			return UtilClass.isNotNullEmpty(arqueoList)?arqueoList.get(0):null;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public ArqueoCaja arqueocaja_verificar_sesion_actual(Long codPer, Integer sucursal){
		try {
			String sql = "select * from arqueocaja_verificar_sesion_actual(?,?)"+asObjectAdd(asArqueoCaja,"delegado varchar(100),custodio_inicial varchar(100),custodio_final varchar(100),fechai varchar(25),fechaf varchar(25)");
			ArqueoCaja arqueoCaja=db.queryForObject(sql,new BeanPropertyRowMapper<ArqueoCaja>(ArqueoCaja.class),codPer,sucursal);
			if(arqueoCaja!=null)
			arqueoCaja.setDetalles(obtenerDetallexArqueoCaja(arqueoCaja.getId()));
			return arqueoCaja;
		} catch (Exception e) {
			logger.error(Utils.errorGet("verificacion actual de "+ENTITY, e.toString()));
			return null;
		}
	}
	public List<DetalleArqueo> obtenerDetallexArqueoCaja(Long cod){
		try {
			return db.query("select * from detallearqueo_obtenerxarqueocaja(?)",new BeanPropertyRowMapper<DetalleArqueo>(DetalleArqueo.class),cod);
		} catch (Exception e) {
			logger.error(Utils.errorGet("detalles de "+ENTITY, e.toString()));
			return null;
		}
		
	}
	@Override
	public List<DetalleArqueo> obtenerDetallexArqueoCajaAgrupado(Long cod){
		try {
			sqlString = "select tipo,subcuenta_id,sum(monto) monto from detallearqueo where arqueo_id = ? and estado =true group by tipo,subcuenta_id";
			return db.query(sqlString,new BeanPropertyRowMapper<DetalleArqueo>(DetalleArqueo.class),cod);
		} catch (Exception e) {
			logger.error(Utils.errorGet("detalles agrupados de "+ENTITY, e.toString()));
			return null;
		}
		
	}
	public Map<String, Object> obtenerDetalle(Integer cod_detarq,Long cod_arqcaj){
		return db.queryForMap("select * from detallearqueo_obtener(?,?)",cod_detarq,cod_arqcaj);
	}
	public Long iniciar(ArqueoCaja ac){
		try {
			return db.queryForObject("select arqueocaja_iniciar(?,?,?,?,?,?);",Long.class,ac.getDelegadoId(),ac.getCustodioInicialId(),ac.getMontoInicial(),
					ac.getGestion(),ac.getDescripcion(),ac.getSucursalId());
		} catch (Exception e) {
			logger.error(Utils.errorAdd("iniciar "+ENTITY, e.toString()));
			return -1L;
		}
	}
	public Boolean modificar(ArqueoCaja ac){
		try {
//			logger.info("modificar: "+ac.getDel_arqcaj()+" | "+ac.getCusini_arqcaj()+" | "+ac.getMonini_arqcaj()+" | "+ac.getGes_gen()+" | "+ac.getDes_arqcaj()+" | "+ac.getCod_arqcaj()+" | "+ac.getCod_suc());
			return db.queryForObject("select arqueocaja_modificar(?,?,?,?,?,?,?);",Boolean.class,ac.getDelegadoId(),ac.getCustodioInicialId(),
					ac.getMontoInicial(),ac.getGestion(),ac.getDescripcion(),ac.getId(),ac.getSucursalId());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			return false;
		}
	}
	public Boolean cerrar(ArqueoCaja ac){
		try {
//			logger.info("cerrar: "+ac.getCusfin_arqcaj()+" | "+ac.getMonfin_arqcaj()+" | "+ac.getDes_arqcaj()+" | "+ac.getMonrea_arqcaj()+" | "+ac.getCod_arqcaj());
			return db.queryForObject("select arqueocaja_cerrar(?,?,?,?,?);",Boolean.class,ac.getCustodioFinalId(),ac.getMontoFinal(),
					ac.getDescripcion(),ac.getMontoReal(),ac.getId());
		} catch (Exception e) {
			logger.error(Utils.errorAdd("cerrar "+ENTITY, e.toString()));
			return false;
		}
	}
	public Boolean darEstado(Long cod,Boolean est,Long cod_per){
		try {
//			logger.info("darEstado: "+cod+" | "+est+" | "+cod_per);
			return db.queryForObject("select arqueocaja_darestado(?,?,?);",Boolean.class,cod,est,cod_per);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			return false;
		}
	}
	public Float obtenertotal(Long cod){
		try {
			return db.queryForObject("select arqueocaja_obtenertotal(?);",Float.class,cod);
		} catch (Exception e) {
			return 0F;
		}
	}
	public Integer adicionarDetalle(DetalleArqueo d){
		try {
//			logger.info("adicionarDetalle: "+d.getCod_arqcaj()+" | "+d.getTip_detarq()+" | "+d.getDes_detarq()+" | "+d.getMon_detarq()+" | "+d.getCodSubcuenta());
			return db.queryForObject("select detallearqueo_adicionar(?,?,?,?,?);",Integer.class,d.getId(),d.getTipo(),
					d.getDescripcion(),d.getMonto(),d.getSubcuentaId());
		} catch (Exception e) {
			logger.error(Utils.errorAdd("detalles de "+ENTITY, e.toString()));
			return -1;
		}
	}
	@Override
	public ArqueoTotal obtenerTotal(Long codArqCaj) {
		try {
			String sqlString = "select a.cod_arqcaj, " + 
					"(select coalesce(sum(mon_detarq),0) from detallearqueo d where d.cod_arqcaj=a.cod_arqcaj and d.tip_detarq=5) tingresos," + 
					"(select coalesce(sum(mon_detarq),0) from detallearqueo d where d.cod_arqcaj=a.cod_arqcaj and d.tip_detarq=1) tegresos," + 
					"(select coalesce(sum(mon_detarq),0) from detallearqueo d where d.cod_arqcaj=a.cod_arqcaj and d.tip_detarq=8) tventas," + 
					"(select coalesce(sum(mon_detarq),0) from detallearqueo d where d.cod_arqcaj=a.cod_arqcaj and d.tip_detarq=4) tcompras " + 
					"from arqueo a where a.cod_arqcaj = ?;";
			return db.queryForObject(sqlString, new BeanPropertyRowMapper<ArqueoTotal>(ArqueoTotal.class), codArqCaj);
		} catch (Exception e) {
			logger.error(Utils.errorGet("total de "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public boolean eliminarDetalle(Long codArqcaj, Integer codDetArq) {
		try {
			logger.info("eliminarDetalle: "+codArqcaj+" | "+codDetArq);
			return db.queryForObject("select detallearqueo_eliminar(?,?);", Boolean.class,codArqcaj,codDetArq);
		} catch (Exception e) {
			logger.error(Utils.errorEli("detalle de "+ENTITY, e.toString()));
			return false;
		}
	}
	@Transactional
	public DataResponse registrarAperturaInicial(Long codArqcaj) {
		try {
			logger.info("registrarAperturaInicial: "+ codArqcaj);
			List<DetalleArqueo> lista = obtenerDetallexArqueoCaja(codArqcaj);
			if(lista != null) {//Existe datos
				int cuentas[]=new int[lista.size()];
				BigDecimal debes[]=new BigDecimal[lista.size()];
				BigDecimal haberes[]=new BigDecimal[lista.size()];
				int ind=0;
				for (DetalleArqueo det : lista) {
					if(det.getSubcuentaId()!=null) {
						cuentas[ind]=det.getSubcuentaId();
						debes[ind] = det.getTipo() == (short)1?det.getMonto():new BigDecimal(0);
						haberes[ind]=det.getTipo()==(short)0?det.getMonto():new BigDecimal(0);
						ind++;
					}
				}
				
			}else {//No existe nada
				logger.info("No se encontro detalles de arqueo por caja");
			}
			return new DataResponse(true, "Se registro con exito la apertura inicial");
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException("Error al registrar apertura inicial: "+e.getMessage());
		}
	}
	@Override
	public boolean registrarArqueoAsiento(Long codArqueo,Long codAsiento) {
		try {
//			logger.info("registrarArqueoAsiento: "+codArqueo+" | "+codAsiento);
			db.update("update arqueo set cod_asiento=? where id=?",codAsiento,codArqueo);
			return true;
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			return false;
		}
	}
	@Override
	public CompraVentaWrap obtenerDescuentoCompra(Long codArqcaj) {
		try {
			List<CompraVentaWrap> lista = db.query("select string_agg(concat('#',cast(c.cod_com as text)),',') lista,coalesce(sum(v.des_com),0) total from compra c.est_com = true and c where c.cod_arqcaj =? ",
					new BeanPropertyRowMapper<CompraVentaWrap>(CompraVentaWrap.class),codArqcaj);
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
					new BeanPropertyRowMapper<CompraVentaWrap>(CompraVentaWrap.class),codArqcaj);
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
					new BeanPropertyRowMapper<CompraVentaWrap>(CompraVentaWrap.class),codArqcaj, codSubcuenta);
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
	public ArqueoWrap obtenerResumenArqueo(Long codArqcaj) {
		try {
			sqlString = "select * from arqueocaja_obtener_resumen(?) ";
			sqlString += "as (cod_arqcaj bigint,tingresos real, tegresos real,tbanco real,monto_real real,es_activo bool,xusuario varchar,finicio text,ffinal text)";
			List<ArqueoWrap> lista = db.query(sqlString, new ArqueoWrapMapper(),codArqcaj);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public ArqueoCaja obtenerUltimaCajaPorUsuario(Long codUsuario, Integer sucursal){
		try {
			String sql = "SELECT ac.* FROM arqueo ac where ac.cod_arqcaj = (select coalesce(max(a2.cod_arqcaj),0) from arqueocaja a2 where a2.del_arqcaj = ? and a2.cod_suc = ? and a2.est_arqcaj=true);";
			List<ArqueoCaja> arqueoList = db.query(sql, new BeanPropertyRowMapper<ArqueoCaja>(ArqueoCaja.class),codUsuario, sucursal);
			return UtilClass.isNotNullEmpty(arqueoList)?arqueoList.get(0):null;
		} catch (Exception e) {
			logger.error(Utils.errorGet("Error obtenerUltimaCajaPorUsuario: "+ENTITY, e.toString()));
			return null;
		}
	}
	public Boolean rehabilitarArqueo(Long cod){
		try {
			db.update("update arqueo set (cusfin_arqcaj,ffin_arqcaj,monfin_arqcaj)=(null,null,null) where cod_arqcaj=?;",cod);
			return true;
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			return false;
		}
	}
}