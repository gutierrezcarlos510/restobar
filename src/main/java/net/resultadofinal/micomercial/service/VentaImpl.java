package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.DetalleventaMapper;
import net.resultadofinal.micomercial.mappers.VentaMapper;
import net.resultadofinal.micomercial.model.DetalleVenta;
import net.resultadofinal.micomercial.model.Venta;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@Service
public class VentaImpl extends DbConeccion implements VentaS {
	
	private JdbcTemplate db;
	@Autowired
	public VentaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(VentaImpl.class);
	private static final String ENTITY = "venta";
	private String sqlString;
	
	public List<Venta> listar(int start, boolean estado, String search, int length, Long cod_per, String fini, String ffin, Integer gestion, Integer sucursal){
		try {
			if(fini.equals("") || fini==null)fini="01/01/"+gestion;
			if(ffin.equals("")|| ffin==null)ffin="31/12/"+gestion;
			Fechas f=new Fechas();
			Date ini=f.convertirStringDate(fini, "dd/MM/yyyy");
			Date fin=f.convertirStringDate(ffin, "dd/MM/yyyy");
			if(search==null)search="";
			sqlString = "select * from venta_lista(?,?,?,?,?,?,?,?,?)";
			sqlString +=asObjectAdd(asVenta, ((start<0?"":"rn BIGINT,tot INT,")+"usuario varchar(100),cliente varchar(100),fecha varchar(15)"));
			return db.query(sqlString, new VentaMapper(),start,length,search,estado,cod_per,ini,fin,gestion,sucursal);
		} catch (Exception e) {
			logger.error("error al listar ventas: "+e.toString());
			return null;
		}
		
	}
	public Venta obtener(Long codVen){
		try {
			sqlString = "select * from venta_obtener(?)";
			List<Venta> ventaList=db.query(sqlString+asObjectAdd(asVenta, "usuario varchar(100),cliente varchar(100),fecha varchar(15)"),new VentaMapper(),codVen);
			Venta venta = UtilClass.getFirst(ventaList);
			if(venta != null) {
				venta.setDetalles(obtenerDetalle(codVen));
			}
			return venta;
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public List<DetalleVenta> obtenerDetalle(Long cod_ven){
		try {
			sqlString = "select * from detalleventa_obtener(?)";
			return db.query(sqlString+asObjectAdd(asDetalleVenta, "producto varchar(150)"),new DetalleventaMapper(),cod_ven);
		} catch (DataAccessException e) {
			logger.error(Utils.errorGet("detalle de ventas", e.toString()));
			return null;
		}
		
	}
	@Transactional
	public Boolean adicionar(Venta v, Integer productos[], Integer cantidades[], Float precios[], Float descuentos[], Float subtotales[], Float totales[]){
		try {
			Vectores vec=new Vectores();
			String codPro=vec.convertir_Int_a_String(productos);
			String preDetcom=vec.convertir_float_a_String(precios);
			String canDetcom=vec.convertir_Int_a_String(cantidades);
			String desDetcom=vec.convertir_float_a_String(descuentos);
			String subtotDetcom=vec.convertir_float_a_String(subtotales);
			String totDetcom=vec.convertir_float_a_String(totales);
//			logger.info("adicionar header: "+v.getCodPer()+" | "+v.getCodCli()+" | "+v.getObsVen()+" | "+v.getTotVen()+" | "+v.getDesVen()+" | "+v.getGesGen()+" | "+v.getCodSuc()+" | "+v.getSubtotVen()+" | "+v.getCodSubcuenta());
//			logger.info("adicionar detalles: "+codPro+" | "+preDetcom+" | "+canDetcom+" | "+desDetcom+" | "+subtotDetcom+" | "+totDetcom);
			String sql = "select venta_adicionar(?,?,?,?,?,?,?,?,?,\'"+codPro+"\',\'"+preDetcom+"\',\'"+canDetcom+"\',\'"+desDetcom+"\',\'"+subtotDetcom+"\',\'"+totDetcom+"\');"; 
			return db.queryForObject(sql,Boolean.class,v.getCodPer(),v.getCodCli(),v.getObsVen(),v.getTotVen(),v.getDesVen(),v.getGesGen(),v.getCodSuc(),v.getSubtotVen(),v.getCodSubcuenta());
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean adicionarVentaSimple(Integer cod_pro, Long cod_per, Integer gestion, Float precio, Integer cod_suc){
		try {
//			logger.info("adicionar venta simple: "+cod_per+" | "+gestion+" | "+cod_pro+" | "+precio+" | "+cod_suc);
			return db.queryForObject("select venta_adicionar_simple(?,?,?,?,?);",Boolean.class,cod_per,gestion,cod_pro,precio,cod_suc);
		} catch (Exception e) {
			logger.error("error al adicionar venta simple="+e.toString());
			throw new RuntimeException("error al adicionar venta simple");
		}
	}
	@Transactional
	public Boolean adicionarVentaSimpleManual(Integer cod_pro, Long cod_per, Integer gestion, Float precio, Integer cant, Float total, Integer cod_suc){
		try {
			logger.info("adicionar venta simple manual: "+cod_per+" | "+gestion+" | "+cod_pro+" | "+precio+" | "+cant+" | "+total+" | "+cod_suc);
			return db.queryForObject("select venta_adicionar_simple_manual(?,?,?,?,?,?,?);",Boolean.class,cod_per,gestion,cod_pro,precio,cant,total,cod_suc);
		} catch (Exception e) {
			logger.error("error al adicionar venta simple manual="+e.toString());
			throw new RuntimeException("error al adicionar venta simple manual");
		}
	}
	@Transactional
	public Boolean eliminar(Long cod_ven,Boolean est){
		try {
			return db.queryForObject("select venta_eliminar(?,?);",Boolean.class,cod_ven,est);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	@Override
	public Venta obtenerPorArqueoCaja(Long codArqcaj, Integer codDet) {
		try {
			List<Venta> lista = db.query("select * from venta v where v.cod_arqcaj =? and cod_detarq =?;", new VentaMapper(),codArqcaj, codDet);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error("obtener por arqueo de caja: "+e.toString());
			throw new RuntimeException("error al obtener por arqueo de caja");
		}
	}
}