package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.CompraMapper;
import net.resultadofinal.micomercial.mappers.DetallecompraMapper;
import net.resultadofinal.micomercial.model.Compra;
import net.resultadofinal.micomercial.model.DetalleCompra;
import net.resultadofinal.micomercial.model.contable.AsientoContable;
import net.resultadofinal.micomercial.model.contable.UtilAsientoContable;
import net.resultadofinal.micomercial.model.wrap.HistorialCompraProducto;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.Fechas;
import net.resultadofinal.micomercial.util.UtilClass;
import net.resultadofinal.micomercial.util.Vectores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CompraImpl extends DbConeccion implements CompraS {
	
	private JdbcTemplate db;
	@Autowired
	private CuentaContableS cuentaContableS;
	@Autowired
	private AsientoContableS asientoS;
	@Autowired
	public CompraImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CompraImpl.class);
	private String sqlString;
	public List<Compra> listar(int start, boolean estado, String search, int length, Long cod_per, String fini, String ffin, Integer gestion){
		if(fini.equals("") || fini==null)fini="01/01/"+gestion;
		if(ffin.equals("")|| ffin==null)ffin="31/12/"+gestion;
		Fechas f=new Fechas();
		Date ini=f.convertirStringDate(fini, "dd/MM/yyyy");
		Date fin=f.convertirStringDate(ffin, "dd/MM/yyyy");
		if(search==null)search="";
		sqlString = "select * from compra_lista(?,?,?,?,?,?,?,?)";
		sqlString += asObjectAdd(asCompra, ((start<0?"":"RN BIGINT,Tot INT,")+"usuario varchar(100),proveedor varchar(100),fecha varchar(15)"));
		return db.query(sqlString, new CompraMapper(),start,length,search,estado,cod_per,ini,fin,gestion);
	}
	public Compra obtener(Long cod_com){
		try {
			sqlString = "select * from compra_obtener(?)";
			sqlString += asObjectAdd(asCompra, "usuario varchar(100),proveedor varchar(100),fecha varchar(15)");
			List<Compra> compraList=db.query(sqlString, new CompraMapper(),cod_com);
			Compra compra = UtilClass.getFirst(compraList);
			if(compra != null) {
				compra.setDetalles(obtenerDetalles(cod_com));
			}
			return compra;
		} catch (Exception e) {
			logger.error("error obtenerCompra="+e.toString());
			return null;
		}
	}
	public List<DetalleCompra> obtenerDetalles(Long codCom){
		try {
			sqlString = "select * from detallecompra_obtener(?)"+asObjectAdd(asDetalleCompra, "producto varchar(150)");
			return db.query(sqlString, new DetallecompraMapper(),codCom);
		} catch (Exception e) {
			logger.error("error al obtener detalle de compras: "+e.toString());
			return null;
		}
	}
	@Transactional
	public Boolean adicionar(Compra c, Integer productos[], Integer cantidades[], Float precios[], Float descuentos[], Float subtotales[], Float totales[]){
		try {
			Fechas f=new Fechas();
			Date fecha=f.convertirStringDate(c.getFec_com(), "dd/MM/yyyy");
			Vectores v=new Vectores();
			String codPro=v.convertir_Int_a_String(productos);
			String preDetcom=v.convertir_float_a_String(precios);
			String canDetcom=v.convertir_Int_a_String(cantidades);
			String desDetcom=v.convertir_float_a_String(descuentos);
			String subtotDetcom=v.convertir_float_a_String(subtotales);
			String totDetcom=v.convertir_float_a_String(totales);
			logger.info("adicionar header: "+c.getCod_per()+" | "+c.getCod_pro()+" | "+fecha+" | "+c.getObs_com()+" | "+c.getSubtotCom()+" | "+c.getTot_com()+" | "+c.getDes_com()+" | "+c.getGes_gen()+" | "+c.getCod_suc());
			logger.info("adicionar detalles: "+codPro+" | "+preDetcom+" | "+canDetcom+" | "+desDetcom+" | "+subtotDetcom+" | "+totDetcom);
			Long codCompra = db.queryForObject("select compra_adicionar(?,?,?,?,?,?,?,?,?,\'"+codPro+"\',\'"+preDetcom+"\',\'"+canDetcom+"\',\'"+desDetcom+"\',\'"+subtotDetcom+"\',\'"+totDetcom+"\');",Long.class,c.getCod_per(),c.getCod_pro(),fecha,c.getObs_com(),c.getSubtotCom(),c.getTot_com(),c.getDes_com(),c.getGes_gen(),c.getCod_suc());
			registroAsientoContable(c, descuentos, codCompra);
			return true;
		} catch (Exception e) {
			logger.error("error al adicionarCompra="+e.toString());
			return false;
		}
	}
	private Float sumatoriaDescuentoDetalle(Float descuentos[]) {
		Float des = 0f;
		if(descuentos !=null && descuentos.length > 0) {
			for (int i = 0; i < descuentos.length; i++) {
				des += descuentos[i];
			}
		}
		return des;
	}
	private void registroAsientoContable(Compra c, Float descuentos[], Long codCompra) {
		UtilAsientoContable utilAsiento = new UtilAsientoContable();
		AsientoContable asiento = new AsientoContable();
		Float totalDescuentoDetalle = sumatoriaDescuentoDetalle(descuentos);
		asiento.setGesGen(c.getGes_gen());
		asiento.setCodSuc(c.getCod_suc());
		asiento.setCreatedBy(c.getUsuario());
		asiento.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		asiento.setFecha(java.sql.Date.valueOf(LocalDate.now()));
		asiento.setConcepto("Registro de compra realizada #"+codCompra);
		if (c.getDes_com() != null && (c.getDes_com()+totalDescuentoDetalle) > 0f) {
			utilAsiento.addDetalle(cuentaContableS.obtenerCuentaCompra(), (c.getTot_com()+(c.getDes_com()+totalDescuentoDetalle)), true);
			utilAsiento.addDetalle(1, c.getTot_com(), false);//Por el momento la cuenta de donde se descontara sera: Caja
			utilAsiento.addDetalle(cuentaContableS.obtenerCuentaDescuentoPorCompra(),(totalDescuentoDetalle+c.getDes_com()), false);// Agregamos un detalle para el descuento de la compra
		} else {// Si no se encuentra el descuento de la compra, se registra en base al detalle de arqueo
			utilAsiento.addTransactionSimple(cuentaContableS.obtenerCuentaCompra(),cuentaContableS.obtenerCuentaCajaDiaria(), c.getSubtotCom());
		}
		asientoS.adicionar(asiento, utilAsiento.getVectorCuenta(), utilAsiento.getVectorDebe(),
				utilAsiento.getVectorHaber());
	}
	public Boolean eliminar(Long cod_com,Boolean est){
		try {
			logger.info("eliminar: "+cod_com+"  "+est);
			return db.queryForObject("select compra_eliminar(?,?);",Boolean.class,cod_com,est);
		} catch (Exception e) {
			logger.error("error al eliminar compra="+e.toString());
			return false;
		}
	}
	@Override
	public Compra obtenerPorArqueoCaja(Long codArqcaj, Integer codDet) {
		try {
			List<Compra> lista = db.query("select * from compra c where c.cod_arqcaj =? and c.cod_detarq =?", new BeanPropertyRowMapper<Compra>(Compra.class), codArqcaj, codDet);
			if(lista!=null) {
				return lista.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error("error al obtener compra por arqueo de caja: "+e.toString());
			throw new RuntimeException("Error al obtener compra por arqueo de caja");
		}
	}
	@Override
	public List<HistorialCompraProducto> obtenerHistorialCompraProducto(Integer codpro){
		try {
			String sqlString = "select to_char(c.fec_com,'DD/MM/YY') xfecha,c.cod_com ,d.pre_detcom from detallecompra d " + 
					"inner join compra c ON d.cod_com = c.cod_com " + 
					"where d.cod_pro =? " + 
					"order by c.fec_com desc";
			return db.query(sqlString, new BeanPropertyRowMapper<HistorialCompraProducto>(HistorialCompraProducto.class),codpro);
		} catch (Exception e) {			
			throw new RuntimeException("No se logro obtener el historial de las compras del producto");
		}
	}
}