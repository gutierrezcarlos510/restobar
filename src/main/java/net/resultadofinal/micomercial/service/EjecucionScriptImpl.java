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
	public DataResponse script3(int num[],Integer sucursalId){
		try {
			sqlString = "";
			for (int i = 0; i < num.length; i++) {
				switch (num[i]) {//Limpiar transaccion de compra y venta
					case 1:
						sqlString += "delete from detalle_compra where cod_com in (select cod_com from compra where cod_suc="+sucursalId+");";
						break;
					case 2:
						sqlString += "delete from pago_credito_compra where compra_id in (select cod_com from compra where cod_suc = "+sucursalId+");";
						break;
					case 3:
						sqlString += "delete from compra where cod_suc="+sucursalId+";";
						break;
					case 4:
						sqlString += "delete from detalle_venta where venta_id in (select id from venta where sucursal_id = "+sucursalId+");";
						break;
					case 5:
						sqlString += "delete from detalle_historico_venta where venta_id in (select id from venta where sucursal_id = "+sucursalId+");";
						break;
					case 6:
						sqlString += "delete from historico_venta where venta_id in (select id from venta where sucursal_id = "+sucursalId+"); ";
						break;
					case 7:
						sqlString += "delete from venta where sucursal_id = "+sucursalId+";";
						break;
					case 8:
						sqlString += "delete from detalle_arqueo where arqueo_id in (select id from arqueo where sucursal_id="+sucursalId+");";
						break;
					case 9:
						sqlString += "delete from arqueo where sucursal_id = "+sucursalId+";";
						break;
					case 10://Limpieza de productos
						sqlString += "delete from historico_almacen where sucursal_id = "+sucursalId+" ;";
						break;
					case 11:
						sqlString += "delete from almacen where sucursal_id = "+sucursalId+" ;";
						break;
					case 12:
						sqlString += "delete from detalle_cartilla_diaria where cartilla_diaria_id in (select id from cartilla_diaria where cod_suc = "+sucursalId+") ;";
						break;
					case 13:
						sqlString += "delete from cartilla_diaria where cod_suc = "+sucursalId+";";
						break;
					case 14:
						sqlString += "delete from detalle_movimiento where movimiento_id in (select id from movimiento where sucursal_origen = "+sucursalId+");";
						break;
					case 15:
						sqlString += "delete from movimiento where sucursal_origen = "+sucursalId+";";
						break;
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
