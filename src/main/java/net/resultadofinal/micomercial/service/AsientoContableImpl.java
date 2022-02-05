package net.resultadofinal.micomercial.service;


import net.resultadofinal.micomercial.model.contable.AsientoContable;
import net.resultadofinal.micomercial.model.contable.CuentaContableGrupo;
import net.resultadofinal.micomercial.model.contable.DetalleAsientoContable;
import net.resultadofinal.micomercial.model.contable.SubcuentaContable;
import net.resultadofinal.micomercial.model.wrap.LibroMayorSubcuenta;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.Vectores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class AsientoContableImpl extends DbConeccion implements AsientoContableS {
	
	private JdbcTemplate db;
	@Autowired
	public AsientoContableImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(AsientoContableImpl.class);
	private String sqlString;
	@Autowired
	private CuentaContableS cuentaContableS;
	@Autowired
	private ProductoS productoS;
	
	@Override
	@Transactional
	public Long adicionar(AsientoContable obj, Integer subcuentas[], Float debes[], Float haberes[]) {
		try {
			Vectores vec=new Vectores();
			String cuentas=vec.convertir_Int_a_String(subcuentas);
			String tdebes=vec.convertir_float_a_String(debes);
			String thaberes=vec.convertir_float_a_String(haberes);

			String sql = "select asiento_adicionar(?,?,?,?,?,\'"+cuentas+"\',\'"+tdebes+"\',\'"+thaberes+"\');"; 
			Long codAsiento = db.queryForObject(sql,Long.class,obj.getGesGen(),obj.getCodSuc(),obj.getFecha(),obj.getCreatedBy(),obj.getConcepto());
			if(codAsiento <= 0) {
				throw new RuntimeException("Error en el proceso de transaccion contable.");
			}
			return codAsiento;
		} catch (Exception e) {
			logger.error("adicionar: "+e.toString());
			throw new RuntimeException("Error al realizar la transaccion contable:"+e.getMessage());
		}
	}
	@Override
	public List<CuentaContableGrupo> listarSumaSaldo(Integer codSuc, Integer gesGen){
		try {
			 sqlString = "select cc.tipo_cuenta ,cc.nombre as xcuenta,sc.cod_subcuenta,sc.codigo,sc.nombre as xsubcuenta," + 
			 		"sum(debe) tdebe,sum(haber) thaber " + 
			 		"from asiento_contable ac " + 
			 		"inner join detalle_asiento_contable dac on dac.cod_asiento = ac.cod_asiento " + 
			 		"inner join subcuenta_contable sc on sc.cod_subcuenta = dac.cod_subcuenta and sc.estado = true " + 
			 		"inner join cuenta_contable cc on cc.cod_cuenta = sc.cod_cuenta and cc.estado = true " + 
			 		"where ac.estado = true and ac.cod_suc = ? and ac.ges_gen = ? " + 
			 		"group by cc.tipo_cuenta,cc.nombre,sc.cod_subcuenta,sc.codigo,sc.nombre " + 
			 		"order by cc.tipo_cuenta,sc.codigo asc ";
			 return db.query(sqlString, new BeanPropertyRowMapper<CuentaContableGrupo>(CuentaContableGrupo.class),codSuc,gesGen);
		} catch (Exception e) {
			System.out.println("error listaSumaSaldo:"+e.toString());
			return null;
		}
	}
	@Override
	public List<Map<String, Object>> listarLibroDiario(int start,boolean estado,String search,int length,Integer gestion,Integer sucursal){
		if(search==null)search="";
		return db.queryForList("select * from librodiario_lista(?,?,?,?,?,?)"+asObjectAdd(asAsientoContable, ("RN BIGINT,tot INT,xfecha varchar(10)")),start,length,search,estado,sucursal,gestion);
	}
	@Override
	public DataResponse obtenerDetalles(Long cod){
		try {
			sqlString = "select dac.*,sc.nombre as xsubcuenta from detalle_asiento_contable dac " + 
					"inner join subcuenta_contable sc on sc.cod_subcuenta =dac.cod_subcuenta " + 
					"where dac.cod_asiento =? " + 
					"order by dac.haber,dac.debe desc";
			List<DetalleAsientoContable> lista = db.query(sqlString, new BeanPropertyRowMapper<DetalleAsientoContable>(DetalleAsientoContable.class), cod);
			if(lista != null)
				return new DataResponse(true, lista,"Se recupero los datos exitosamente");
			else
				return new DataResponse(false, "No se tiene detalles de ese asiento.");
		} catch (Exception e) {
			throw new RuntimeException("Error al listar detalle de asiento contable:"+e.getMessage());
		}
	}
	@Override
	public LibroMayorSubcuenta obtenerLibroMayorSubcuenta(Integer subcuenta, Integer gestion, Integer sucursal) {
		sqlString = "select ac.numero,dac.* from detalle_asiento_contable dac " + 
				"inner join asiento_contable ac on ac.cod_asiento =dac.cod_asiento  and ac.estado =true and ac.cod_suc = ? and ac.ges_gen =? " + 
				"where dac.cod_subcuenta =?";
		try {
			LibroMayorSubcuenta mayor = new LibroMayorSubcuenta();
			SubcuentaContable cuenta = cuentaContableS.obtenerSubcuenta(subcuenta);
			mayor.setCodigo(cuenta.getCodigo());
			mayor.setXsubcuenta(cuenta.getNombre());
			mayor.setCodSubcuenta(subcuenta);
			List<DetalleAsientoContable> lista = db.query(sqlString, new BeanPropertyRowMapper<DetalleAsientoContable>(DetalleAsientoContable.class),sucursal,gestion,subcuenta);
			mayor.setDetalles(lista);
			return mayor;
		} catch (Exception e) {
			System.out.println("error al listar libro mayor cuenta "+e.getMessage());
			return null;
		}
	}
	@Override
	@Transactional
	public Long adicionarAsientoProductoPerdido(AsientoContable obj, Integer subcuentas[], Float debes[], Float haberes[], Integer productos[], Integer cantidades[]) {
		try {
			Vectores vec=new Vectores();
			String cuentas=vec.convertir_Int_a_String(subcuentas);
			String tdebes=vec.convertir_float_a_String(debes);
			String thaberes=vec.convertir_float_a_String(haberes);

			String sql = "select asiento_adicionar(?,?,?,?,?,\'"+cuentas+"\',\'"+tdebes+"\',\'"+thaberes+"\');"; 
			Long codAsiento = db.queryForObject(sql,Long.class,obj.getGesGen(),obj.getCodSuc(),obj.getFecha(),obj.getCreatedBy(),obj.getConcepto());
			if(codAsiento <= 0) {
				throw new RuntimeException("Error en el proceso de registro de asiento contable.");
			}
			productoS.registrarProductoPerdido(productos, cantidades, codAsiento);
			return codAsiento;
		} catch (Exception e) {
			throw new RuntimeException("Error al realizar la transaccion contable:"+e.getMessage());
		}
	}
}
