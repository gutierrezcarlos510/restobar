package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.DbConeccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class EjecucionScriptImpl extends DbConeccion implements EjecucionScriptS {
	
	private JdbcTemplate db;
	@Autowired
	public EjecucionScriptImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private String sqlString;
	@Override
	public DataResponse script2() {
		try {
			List<Map<String, Object>> query = db.queryForList("select * from pg_stat_activity;");
			return new DataResponse(true, query,"Se realizo con exito la consulta");
		} catch (Exception e) {
			throw new RuntimeException("Error script2:"+e.toString());
		}
	}
	@Override
	public DataResponse script1(int num[]){
		try {
			sqlString = "";
			for (int i = 0; i < num.length; i++) {
				switch (num[i]) {//Limpiar transaccion de compra y venta
				case 1:
					sqlString += "delete from detalle_compra;";
					break;
				case 2:
					sqlString += "delete from compra;";
					break;
				case 3:
					sqlString += "delete from detalle_venta;";
					break;
				case 4:
					sqlString += "delete from detalle_historico_venta;";
					break;
				case 5:
					sqlString += "delete from historico_venta;";
					break;
				case 6:
					sqlString += "delete from venta; ";
					break;
				case 7:
					sqlString += "delete from detalle_arqueo;";
					break;
				case 8:
					sqlString += "delete from arqueo;";
					break;
				case 9:
					sqlString += "delete from historico_almacen;";
					break;
				case 10://Limpieza de productos
					sqlString += "delete from almacen ;";
					break;
				case 11:
					sqlString += "delete from detalle_cartilla_diaria ;";
					break;
				case 12:
					sqlString += "delete from cartilla_diaria ;";
					break;
				case 13:
					sqlString += "delete from detalle_movimiento;";
					break;
				case 14:
					sqlString += "delete from movimiento;";
					break;
				case 15:
					sqlString += "delete from pago_credito_compra;";
					break;
//				case 16:
//					sqlString += "delete from tiene_sucursal where cod_per > 1;";
//					break;
//				case 17:
//					sqlString += "delete from usurol where cod_per > 1;";
//					break;
//				case 18:
//					sqlString += "delete from proveedor where cod_pro > 1;";
//					break;
//				case 19:
//					sqlString += "delete from cliente where cod_cli > 1;";
//					break;
//				case 20:
//					sqlString += "delete from secretaria where cod_sec > 1;";
//					break;
//				case 21:
//					sqlString += "delete from persona where cod_per > 1;";
//					break;
				default:
					break;
				}
			}
			db.update(sqlString);
			return new DataResponse(true, "Se realizo con exito el script.");
		} catch (Exception e) {
			throw new RuntimeException("No se realizo el script: "+e.getMessage());
		}
	}
}
