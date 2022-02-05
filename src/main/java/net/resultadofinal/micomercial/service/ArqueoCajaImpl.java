package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.ArqueoWrapMapper;
import net.resultadofinal.micomercial.mappers.DetallearqueoMapper;
import net.resultadofinal.micomercial.model.ArqueoCaja;
import net.resultadofinal.micomercial.model.ArqueoTotal;
import net.resultadofinal.micomercial.model.DetalleArqueo;
import net.resultadofinal.micomercial.model.General;
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
			SqlBuilder sqlBuilder = new SqlBuilder("arqueocaja ac");
			sqlBuilder.setSelect("cod_arqcaj,del_arqcaj,cusini_arqcaj,fini_arqcaj,monini_arqcaj,ffin_arqcaj,monfin_arqcaj,");
			sqlBuilder.setSelectConcat("est_arqcaj,monrea_arqcaj,ges_gen,des_arqcaj,cusfin_arqcaj,cod_suc,cod_asiento,");
			sqlBuilder.setSelectConcat("cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) delegado,");
			sqlBuilder.setSelectConcat("cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,");
			sqlBuilder.setSelectConcat("cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final,");
			sqlBuilder.setSelectConcat("cast(to_char(fini_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechai,cast(to_char(ffin_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechaf");
			sqlBuilder.addJoin("persona p1 on p1.cod_per=ac.del_arqcaj");
			sqlBuilder.addJoin("persona p2 on p2.cod_per=ac.cusini_arqcaj");
			sqlBuilder.addLeftJoin("persona p3 on p3.cod_per=ac.cusfin_arqcaj");
			sqlBuilder.setWhere("ac.est_arqcaj=:xestado and (ac.cusini_arqcaj=:xuser or :xuser=-1) and ac.ges_gen=:xgestion");
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
			SqlBuilder sqlBuilder = new SqlBuilder("detallearqueo");
			sqlBuilder.setSelect("*,cast(to_char(fec_detarq,'dd/MM/yyyy hh:mm:ss') as varchar(25)) fecha");
			sqlBuilder.setWhere("est_detarq=:xestado and cod_arqcaj= :xcod");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xcod", arqueo);
			return utilDataTableS.list(request, DetalleArqueo.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	public Map<String, Object> obtener(Long cod){
		try {
			sqlString = "select * from arqueocaja_obtener(?)";
			sqlString += asObjectAdd(asArqueoCaja,"delegado varchar(100),custodio_inicial varchar(100),custodio_final varchar(100),fechai varchar(25),fechaf varchar(25)");
			Map<String, Object> arqueocaja=db.queryForMap(sqlString,cod);
			arqueocaja.put("detalles", obtenerDetallexArqueoCaja(cod));
			arqueocaja.put("total", obtenertotal(cod));
			return arqueocaja;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public ArqueoCaja obtenerCaja(Long codArq) {
		try {
			sqlString = "select * from arqueocaja_obtener(?)";
			sqlString += asObjectAdd(asArqueoCaja,"delegado varchar(100),custodio_inicial varchar(100),custodio_final varchar(100),fechai varchar(25),fechaf varchar(25)");
			List<ArqueoCaja> arqueoList = db.query(sqlString, new BeanPropertyRowMapper<ArqueoCaja>(ArqueoCaja.class), codArq);
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
			arqueoCaja.setDetalles(obtenerDetallexArqueoCaja(arqueoCaja.getCod_arqcaj()));
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
			sqlString = "select tip_detarq,cod_subcuenta,sum(mon_detarq) mon_detarq from detallearqueo where cod_arqcaj = ? and est_detarq =true group by tip_detarq,cod_subcuenta";
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
			return db.queryForObject("select arqueocaja_iniciar(?,?,?,?,?,?);",Long.class,ac.getDel_arqcaj(),ac.getCusini_arqcaj(),ac.getMonini_arqcaj(),ac.getGes_gen(),ac.getDes_arqcaj(),ac.getCod_suc());
		} catch (Exception e) {
			logger.error(Utils.errorAdd("iniciar "+ENTITY, e.toString()));
			return -1L;
		}
	}
	public Boolean modificar(ArqueoCaja ac){
		try {
			logger.info("modificar: "+ac.getDel_arqcaj()+" | "+ac.getCusini_arqcaj()+" | "+ac.getMonini_arqcaj()+" | "+ac.getGes_gen()+" | "+ac.getDes_arqcaj()+" | "+ac.getCod_arqcaj()+" | "+ac.getCod_suc());
			return db.queryForObject("select arqueocaja_modificar(?,?,?,?,?,?,?);",Boolean.class,ac.getDel_arqcaj(),ac.getCusini_arqcaj(),ac.getMonini_arqcaj(),ac.getGes_gen(),ac.getDes_arqcaj(),ac.getCod_arqcaj(),ac.getCod_suc());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			return false;
		}
	}
	public Boolean cerrar(ArqueoCaja ac){
		try {
			logger.info("cerrar: "+ac.getCusfin_arqcaj()+" | "+ac.getMonfin_arqcaj()+" | "+ac.getDes_arqcaj()+" | "+ac.getMonrea_arqcaj()+" | "+ac.getCod_arqcaj());
			return db.queryForObject("select arqueocaja_cerrar(?,?,?,?,?);",Boolean.class,ac.getCusfin_arqcaj(),ac.getMonfin_arqcaj(),ac.getDes_arqcaj(),ac.getMonrea_arqcaj(),ac.getCod_arqcaj());
		} catch (Exception e) {
			logger.error(Utils.errorAdd("cerrar "+ENTITY, e.toString()));
			return false;
		}
	}
	public Boolean darEstado(Long cod,Boolean est,Long cod_per){
		try {
			logger.info("darEstado: "+cod+" | "+est+" | "+cod_per);
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
			logger.info("adicionarDetalle: "+d.getCod_arqcaj()+" | "+d.getTip_detarq()+" | "+d.getDes_detarq()+" | "+d.getMon_detarq()+" | "+d.getCodSubcuenta());
			return db.queryForObject("select detallearqueo_adicionar(?,?,?,?,?);",Integer.class,d.getCod_arqcaj(),d.getTip_detarq(),d.getDes_detarq(),d.getMon_detarq(),d.getCodSubcuenta());
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
					"from arqueocaja a where a.cod_arqcaj = ?;";
			return db.queryForObject(sqlString, new BeanPropertyRowMapper<ArqueoTotal>(ArqueoTotal.class), codArqCaj);
		} catch (Exception e) {
			logger.error(Utils.errorGet("total de "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public ArqueoCaja obtenerArqueoInicial(int gestion, int sucursal) {
		try {
			logger.info("obtenerArqueoInicial: "+ gestion+" | "+sucursal);
			sqlString = "select a.*,(p1.nom_per||' '||p1.priape_per||' '||p1.segape_per) ::varchar(100) delegado,(p2.nom_per||' '||p2.priape_per||' '||p2.segape_per)::varchar(100) custodio_inicial," +
					"(p3.nom_per||' '||p3.priape_per||' '||p3.segape_per)::varchar(100) custodio_final,to_char(fini_arqcaj,'dd/MM/yyyy hh:mm')::varchar(25) fechai," +
					"to_char(ffin_arqcaj,'dd/MM/yyyy hh:mm')::varchar(25) fechaf from arqueocaja a " +
					"inner join persona p1 on p1.cod_per=a.del_arqcaj inner join persona p2 on p2.cod_per=a.cusini_arqcaj left join persona p3 on p3.cod_per=a.cusfin_arqcaj " +
					"where a.cod_suc = ? and a.ges_gen = ? and a.est_arqcaj = true order by a.cod_arqcaj asc limit 1;";
			return db.queryForObject(sqlString,mapperToArqueoCaja(),sucursal, gestion);
		}catch (Exception e) {
			logger.error(Utils.errorGet("inicio de "+ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public boolean existeArqueInicialGestion(int gestion, int sucursal) {
		General gestionAnterior = generalS.existeGestionAnterior(gestion, sucursal);
		if(gestionAnterior != null) {//Existe una gestion anterior recuperar almacen compras, prestamos bancarios 
			return true;
		}else {
			ArqueoCaja arqueoInicial = obtenerArqueoInicial(gestion, sucursal);
			return arqueoInicial !=null;
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
				float debes[]=new float[lista.size()];
				float haberes[]=new float[lista.size()];
				int ind=0;
				for (DetalleArqueo det : lista) {
					if(det.getCodSubcuenta()!=null) {
						cuentas[ind]=det.getCodSubcuenta();
						debes[ind]=det.getTip_detarq()==1?det.getMon_detarq():0f;
						haberes[ind]=det.getTip_detarq()==0?det.getMon_detarq():0f;
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
	@Transactional
	public Boolean cerrarCajaInicial(ArqueoCaja ac){
		try {
			logger.info("cerrarCajaInicial: "+ac.getCusfin_arqcaj()+" | "+ac.getMonfin_arqcaj()+" | "+ac.getDes_arqcaj()+" | "+ac.getMonrea_arqcaj()+" | "+ac.getCod_arqcaj()+" | "+ac.getCodAsiento());
			return db.queryForObject("select arqueocaja_cerrar_caja_inicial(?,?,?,?,?,?);",Boolean.class,ac.getCusfin_arqcaj(),ac.getMonfin_arqcaj(),ac.getDes_arqcaj(),ac.getMonrea_arqcaj(),ac.getCod_arqcaj(),ac.getCodAsiento());
		} catch (Exception e) {
			logger.error(Utils.errorAdd("cierre de "+ENTITY, e.toString()));
			throw new RuntimeException("error al cerrar ArqueoCaja Inicial="+e.toString());
		}
	}
	@Override
	public boolean registrarArqueoAsiento(Long codArqueo,Long codAsiento) {
		try {
			logger.info("registrarArqueoAsiento: "+codArqueo+" | "+codAsiento);
			db.update("update arqueocaja set cod_asiento=? where cod_arqcaj=?",codAsiento,codArqueo);
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
			return Utils.isNotEmptyList(lista) ? lista.get(0) : null;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public ArqueoCaja obtenerUltimaCajaPorUsuario(Long codUsuario, Integer sucursal){
		try {
			String sql = "SELECT ac.* FROM arqueocaja ac where ac.cod_arqcaj = (select coalesce(max(a2.cod_arqcaj),0) from arqueocaja a2 where a2.del_arqcaj = ? and a2.cod_suc = ? and a2.est_arqcaj=true);";
			List<ArqueoCaja> arqueoList = db.query(sql, new BeanPropertyRowMapper<ArqueoCaja>(ArqueoCaja.class),codUsuario, sucursal);
			return UtilClass.isNotNullEmpty(arqueoList)?arqueoList.get(0):null;
		} catch (Exception e) {
			logger.error(Utils.errorGet("Error obtenerUltimaCajaPorUsuario: "+ENTITY, e.toString()));
			return null;
		}
	}
	public Boolean rehabilitarArqueo(Long cod){
		try {
			db.update("update arqueocaja set (cusfin_arqcaj,ffin_arqcaj,monfin_arqcaj)=(null,null,null) where cod_arqcaj=?;",cod);
			return true;
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			return false;
		}
	}
}