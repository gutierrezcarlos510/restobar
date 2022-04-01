package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.CompraMapper;
import net.resultadofinal.micomercial.mappers.DetallecompraMapper;
import net.resultadofinal.micomercial.model.Compra;
import net.resultadofinal.micomercial.model.DetalleCompra;
import net.resultadofinal.micomercial.model.PagoCreditoCompra;
import net.resultadofinal.micomercial.model.wrap.HistorialCompraProducto;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CompraImpl extends DbConeccion implements CompraS {
	
	private JdbcTemplate db;
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
	public Boolean adicionar(Compra c, Long productos[], Integer cantidades[], BigDecimal precios[], BigDecimal descuentos[], BigDecimal subtotales[], BigDecimal totales[]){
		try {
			Vectores v=new Vectores();
			String codPro=v.convertir_Long_a_String(productos);
			String preDetcom=v.convertirBigDecimalString(precios);
			String canDetcom=v.convertir_Int_a_String(cantidades);
			String desDetcom=v.convertirBigDecimalString(descuentos);
			String subtotDetcom=v.convertirBigDecimalString(subtotales);
			String totDetcom=v.convertirBigDecimalString(totales);
//			logger.info("adicionar header: "+c.getCod_per()+" | "+c.getCod_pro()+" | "+fecha+" | "+c.getObs_com()+" | "+c.getSubtotCom()+" | "+c.getTot_com()+" | "+c.getDes_com()+" | "+c.getGes_gen()+" | "+c.getCod_suc());
//			logger.info("adicionar detalles: "+codPro+" | "+preDetcom+" | "+canDetcom+" | "+desDetcom+" | "+subtotDetcom+" | "+totDetcom);
			sqlString = "select compra_adicionar(?,?,to_date(?,'DD/MM/YYYY'),?,?,?,?,?,?,\'"+codPro+"\',\'"+preDetcom+"\',\'"+canDetcom+"\',\'"+desDetcom+"\',\'"+subtotDetcom+"\',\'"+totDetcom+"\');";
			Long codCompra = db.queryForObject(sqlString,Long.class, c.getCod_per(),c.getCod_pro(),c.getFecha(),c.getObs_com(),c.getSubtotCom(),
					c.getTot_com(),c.getDes_com(),c.getGes_gen(),c.getCod_suc());
			if(codCompra > 0) {
				if(c.getTipo()) { // Si es al credito
					db.update("update compra set tipo = ?,saldo_deber = ?, acuenta = ? where cod_com =?", c.getTipo(), c.getTot_com(), 0, codCompra);
				} else {
					db.update("update compra set tipo = ?,saldo_deber = ?, acuenta = ? where cod_com =?", c.getTipo(), 0, c.getTot_com(), codCompra);
				}
				return true;
			} else {
				throw new RuntimeException("error al guardar la compra");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error al adicionarCompra="+e.toString());
			return false;
		}
	}

	public Boolean eliminar(Long cod_com,Long user){
		try {
//			logger.info("eliminar: "+cod_com+"  "+est);
			Short res = db.queryForObject("select compra_eliminar(?,?);",Short.class,cod_com,user);
			if(res > 0) {
				return true;
			} else {
				if(res == -2) {
					throw new RuntimeException("No se puede eliminar, ya que parte de los productos de almacen, no estan completos.");
				} else {
					throw new RuntimeException("Error al realizar la eliminacion de la compra");
				}
			}
		} catch (Exception e) {
			logger.error("error al eliminar compra="+e.toString());
			throw new RuntimeException(e.getMessage());
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
			String sqlString = "select to_char(c.fec_com,'DD/MM/YY') xfecha,c.cod_com ,d.pre_detcom from detalle_compra d " +
					"inner join compra c ON d.cod_com = c.cod_com " + 
					"where d.cod_pro =? " + 
					"order by c.fec_com desc limit 10";
			return db.query(sqlString, new BeanPropertyRowMapper<HistorialCompraProducto>(HistorialCompraProducto.class),codpro);
		} catch (Exception e) {			
			throw new RuntimeException("No se logro obtener el historial de las compras del producto");
		}
	}
	public DataResponse adicionarPago(PagoCreditoCompra obj) {
		try {
			Compra compra = obtener(obj.getCompraId());
			if(compra != null) {
				BigDecimal diferencia = compra.getSaldoDeber().subtract(obj.getMonto());
				if(diferencia.compareTo(new BigDecimal(0)) < 0) {
					return new DataResponse(false, "El monto pagado es mayor al saldo de la deuda. Realizar la correccion, por favor.");
				}
				sqlString = "select coalesce(max(id),0)+1 from pago_credito_compra where compra_id = ?";
				Short id = db.queryForObject(sqlString, Short.class, obj.getCompraId());
				String sqlString = "INSERT INTO pago_credito_compra(compra_id, id, monto, forma_pago_id, created_by, created_at, estado)" +
						" VALUES(?, ?, ?, ?, ?, now(), true);";
				db.update(sqlString, obj.getCompraId(), id, obj.getMonto(), obj.getFormaPagoId(), obj.getCreatedBy());
				db.update("update compra set acuenta = acuenta + ?,saldo_deber = saldo_deber - ? where cod_com = ?", obj.getMonto(), obj.getMonto(), obj.getCompraId());
				return Utils.getResponseDataAdd("Pago de compra al credito", true);
			} else {
				return new DataResponse(false, "No se logro obtener la compra");
			}

		} catch (Exception e) {
			throw new RuntimeException("No se logro al guardar pago de compra al credito: "+e.getMessage());
		}
	}
	public List<PagoCreditoCompra> listarPagosCompraCredito(Long id) {
		try {
			sqlString = "select pc.*,concat(p.nom_per, ' ', p.priape_per) xcreated_by,fp.nombre as xforma_pago from pago_credito_compra pc inner join persona p on p.cod_per = pc.created_by inner join forma_pago fp on fp.id = pc.forma_pago_id where pc.compra_id = ? and pc.estado = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(PagoCreditoCompra.class), id);
		} catch (Exception e) {
			throw new RuntimeException("No se logro obtener la lista de pagos de  la compra al credito:"+e.getMessage());
		}
	}
	public DataResponse eliminarPago(PagoCreditoCompra obj) {
		try {
			List<PagoCreditoCompra> pagos = db.query("select * from pago_credito_compra where compra_id = ? and id = ?", BeanPropertyRowMapper.newInstance(PagoCreditoCompra.class), obj.getCompraId(), obj.getId());
			PagoCreditoCompra pago = UtilClass.getFirst(pagos);
			if(pago != null) {
				sqlString = "update pago_credito_compra set estado = false,updated_by =?, updated_at = now() where compra_id = ? and id = ?;";
				db.update(sqlString, obj.getUpdatedBy(),obj.getCompraId(), obj.getId());
				db.update("update compra set acuenta = acuenta - ?,saldo_deber = saldo_deber + ? where cod_com = ?", pago.getMonto(), pago.getMonto(), pago.getCompraId());
				return Utils.getResponseDataEli("Pago de credito", true);
			} else {
				return new DataResponse(false, "No se encontro el pago en la base de datos");
			}

		} catch (Exception e) {
			throw new RuntimeException("No se logro obtener la lista de pagos de  la compra al credito:"+e.getMessage());
		}
	}
}