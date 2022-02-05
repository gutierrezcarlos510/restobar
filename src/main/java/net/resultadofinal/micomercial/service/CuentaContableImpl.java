package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.CuentaContableMapper;
import net.resultadofinal.micomercial.mappers.SubcuentaContableMapper;
import net.resultadofinal.micomercial.model.contable.CuentaContable;
import net.resultadofinal.micomercial.model.contable.SubcuentaContable;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class CuentaContableImpl extends DbConeccion implements CuentaContableS {
	
	private JdbcTemplate db;
	@Autowired
	public CuentaContableImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CuentaContableImpl.class);
	private String sqlString;
	@Override
	public List<CuentaContable> listar(int start, boolean estado, String search, int length){
		try {
			if(search==null)search="";
			sqlString = "select * from cuenta_contable_lista(?,?,?,?)"+asObjectAdd(asCuentaContable, (start<0?"":"xtipo varchar,rn BIGINT,tot INT"));
			return db.query(sqlString, new CuentaContableMapper(),start,length,search,estado);
		} catch (Exception e) {
			logger.error("error al listar cuenta contable: "+e.toString());
			return null;
		}
		
	}
	@Override
	public SubcuentaContable obtenerSubcuenta(Integer codSubcuenta) {
		try {
			List<SubcuentaContable> lista = db.query("select * from subcuenta_contable where cod_subcuenta =?",new SubcuentaContableMapper(), codSubcuenta);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error("obtenerSubcuenta: "+e.toString());
			return null;
		}
	}
	@Override
	public List<SubcuentaContable> listarCajas(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 1 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listarCajas: "+e.toString());
			return null;
		}
	}
	@Override
	public List<SubcuentaContable> listarBancos(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 2 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar bancos: "+e.toString());
			return null;
		}
	}
	@Override
	public List<SubcuentaContable> listarActivosGenerales(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 4 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar activos generales: "+e.toString());
			throw new RuntimeException("error al listar activos generales");
		}
	}
	@Override
	public List<SubcuentaContable> listarPrestamosBancarios(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 5 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar prestamos bancarios: "+e.toString());
			throw new RuntimeException("error al listar prestamos bancarios");
		}
	}
	@Override
	public List<SubcuentaContable> listarPasivosGeneral(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 6 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar pasivos generales: "+e.toString());
			throw new RuntimeException("error al listar pasivos generales");
		}
	}
	@Override
	public List<SubcuentaContable> listarEgresoGeneral(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 7 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar egresos generales: "+e.toString());
			throw new RuntimeException("error al listar egresos generales");
		}
	}
	@Override
	public List<SubcuentaContable> listarEgresoOperacional(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.cod_cuenta="+ MyConstant.Cuenta.EGRESO_OPERACIONAL +
					" where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar egresos operacionales: "+e.toString());
			throw new RuntimeException("error al listar egresos operacionales");
		}
	}
	@Override
	public List<SubcuentaContable> listarIngresoGeneral(Boolean filtrarEsExterno){
		try {
			sqlString = "select sc.* from subcuenta_contable sc " + 
					"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true and cc.tipo_grupo = 8 " + 
					"where sc.estado = true"+(filtrarEsExterno!=null?" and sc.es_externo="+filtrarEsExterno:"");
			return db.query(sqlString, new SubcuentaContableMapper());
		} catch (Exception e) {
			logger.error("listar ingresos generales: "+e.toString());
			throw new RuntimeException("error al listar ingresos generales");
		}
	}
	@Override
	public int obtenerCuentaMueble() {
		return 5;
	}
	@Override
	public int obtenerCuentaInmueble() {
		return 4;
	}
	@Override
	public int obtenerCuentaCapital() {
		return 13;
	}
	@Override
	public int obtenerCuentaCajaDiaria() {
		return 3;
	}
	@Override
	public int obtenerCuentaCompra() {
		return 15;
	}
	@Override
	public int obtenerCuentaVenta() {
		return 27;
	}
	@Override
	public int obtenerCuentaInventario() {
		return 33;
	}
	@Override
	public int obtenerCuentaPerdidaDineroCajaDiaria() {
		return 21;
	}
	@Override
	public int obtenerCuentaGananciaDineroCajaDiaria() {
		return 31;
	}
	@Override
	public int obtenerCuentaSueldoSalario() {
		return 16;
	}
	@Override
	public int obtenerCuentaAlquiler() {
		return 18;
	}
	@Override
	public int obtenerCuentaServicioBasico() {
		return 19;
	}
	@Override
	public int obtenerCuentaFleteTransporte() {
		return 17;
	}
	@Override
	public int obtenerCuentaPerdidaProducto() {
		return 20;
	}
	@Override
	public int obtenerCuentaMaterialOficina() {
		return 24;
	}
	@Override
	public int obtenerCuentaServicioLimpieza() {
		return 25;
	}
	@Override
	public int obtenerCuentaEquipoComputacional() {
		return 6;
	}
	@Override
	public int obtenerCuentaDescuentoPorCompra() {
		return 28;
	}
	@Override
	public int obtenerCuentaDescuentoPorVenta() {
		return 22;
	}
	public int obtenerCuentaIngresoPorPrestacionServicio() {
		return 35;
	}
	public int obtenerCuentaIngresoPorMultas() {
		return 36;
	}
	public int obtenerCuentaPagoPrestacionCumplida() {
		return 37;
	}
	public int obtenerCuentaPagoPrestacionAbandonado() {
		return 38;
	}
	public int obtenerCuentaDevolucionPorPrestacion() {
		return 39;
	}
	public int obtenerCuentaMultasPerdonadas() {
		return 40;
	}
	@Override
	public CuentaContable obtener(Integer codCuenta) {
		try {
			List<CuentaContable> cuentaList = db.query("select * from cuenta_contable where cod_cuenta=?", new CuentaContableMapper(),codCuenta);
			if(UtilClass.isNotNullEmpty(cuentaList)) {
				return cuentaList.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error("error al obtener cuenta: "+e.toString());
			return null;
		}
	}
	@Override
	public List<SubcuentaContable> obtenerSubcuentas(Integer codCuenta){
		try {
			return db.query("select * from subcuenta_contable where cod_cuenta=? and estado=true", new SubcuentaContableMapper(), codCuenta);
		} catch (Exception e) {
			logger.error("");
			return null;
		}
	}
	@Override
	public List<SubcuentaContable> listarSubcuentas(int start, boolean estado, String search, int length, Integer codCuenta){
		try {
			if(search==null)search="";
			sqlString = "select * from subcuenta_contable_lista(?,?,?,?,?)"+asObjectAdd(asSubcuentaContable, (start<0?"":"rn BIGINT,tot INT"));
			return db.query(sqlString, new SubcuentaContableMapper(),start,length,search,estado,codCuenta);
		} catch (Exception e) {
			logger.error("error al listar subcuentas"+e.toString());
			return null;
		}
		
	}
	@Override
	public DataResponse adicionarSubcuenta(SubcuentaContable obj) {
		try {
			sqlString = "select coalesce(max(cod_subcuenta),0)+1 from subcuenta_contable";
			Integer codSubcuenta = db.queryForObject(sqlString, Integer.class);
			sqlString = "INSERT INTO subcuenta_contable(cod_cuenta, cod_subcuenta, nombre, descripcion, estado, codigo, es_externo) VALUES(?,?,trim(upper(?)),?,?,?,?)";
			int res = db.update(sqlString,obj.getCodCuenta(),codSubcuenta,obj.getNombre(),obj.getDescripcion(),true,obj.getCodigo(),obj.isEsExterno());
			return new DataResponse(res>0, res>0?"Se realizo con exito el registro de la subcuenta":"No se logro realizar el registro");
		} catch (Exception e) {
			logger.error("error al adicionar subcuenta: "+e.toString());
			throw new RuntimeException("Error al registrar subcuenta: "+e.getMessage());
		}
	}
	@Override
	public boolean existeCodigo(String code) {
		try {
			sqlString = "select count(*) from subcuenta_contable where codigo =? and estado =true";
			long res = db.queryForObject(sqlString, Long.class,code);
			return res>0;
		} catch (Exception e) {
			logger.error("error al consultar existe codigo:"+e.toString());
			return false;
		}
	}
	@Override
	public DataResponse modificarSubcuenta(SubcuentaContable obj) {
		try {
			sqlString = "update subcuenta_contable set nombre=trim(upper(?)), descripcion=?, codigo=?, es_externo=? where cod_subcuenta=?";
			int res = db.update(sqlString,obj.getNombre(),obj.getDescripcion(),obj.getCodigo(),obj.isEsExterno(),obj.getCodSubcuenta());
			return new DataResponse(res>0, res>0?"Se realizo con exito la actualizacion de la subcuenta":"No se logro realizar la actualizacion");
		} catch (Exception e) {
			logger.error("error al modificar subcuenta: "+e.toString());
			throw new RuntimeException("Error al modificar subcuenta: "+e.getMessage());
		}
	}
	@Override
	public DataResponse eliminar(Integer codSubcuenta) {
		try {
			
			sqlString = "update subcuenta_contable set estado=false where cod_subcuenta=?";
			int res = db.update(sqlString,codSubcuenta);
			return new DataResponse(res>0, res>0?"Se realizo con exito la eliminacion de la subcuenta":"No se logro realizar la eliminacion de la subcuenta");
		} catch (Exception e) {
			logger.error("error al eliminar subcuenta: "+e.toString());
			throw new RuntimeException("Error al eliminar subcuenta: "+e.getMessage());
		}
	}
}
