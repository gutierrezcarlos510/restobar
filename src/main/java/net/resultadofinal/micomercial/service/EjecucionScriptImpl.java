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
					sqlString += "delete from detallecompra;";
					break;
				case 2:
					sqlString += "delete from detalleventa;";
					break;
				case 3:
					sqlString += "delete from venta;";
					break;
				case 4:
					sqlString += "delete from compra;";
					break;
				case 5:
					sqlString += "delete from inscripcion;";
					break;
				case 6:
					sqlString += "delete from detalleprestacion; ";
					break;
				case 7:
					sqlString += "delete from prestacion;";
					break;
				case 8:
					sqlString += "delete from detallearqueo;";
					break;
				case 0:
					sqlString += "delete from arqueocaja;";
					break;
				case 9:
					sqlString += "delete from arqueocaja; ";
					break;
				case 10://Limpieza de productos
					sqlString += "delete from cliente ;";
					break;
				case 11:
					sqlString += "delete from proveedor ;";
					break;
				case 12:
					sqlString += "delete from empresa ;";
					break;
				case 13:
					sqlString += "delete from dato;";
					break;
				case 14:
					sqlString += "delete from usurol;";
					break;
				case 15:
					sqlString += "delete from tiene_sucursal;";
					break;
				case 16:
					sqlString += "delete from descargas;";
					break;
				case 17:
					sqlString += "delete from archivos;";
					break;
				case 18:
					sqlString += "delete from carprac;";
					break;
				case 19:
					sqlString += "delete from matprac;";
					break;
				case 20:
					sqlString += "delete from practicos;";
					break;
				case 21:
					sqlString += "delete from docente;";
					break;
				case 22:
					sqlString += "delete from ayu_tipmat;";
					break;
				case 23:
					sqlString += "delete from detallehorarioclase;";
					break;
				case 24:
					sqlString += "delete from horarioclase;";
					break;
				case 25:
					sqlString += "delete from detalleplan;";
					break;
				case 26:
					sqlString += "delete from mat_pla_ayu;";
					break;
				case 27:
					sqlString += "delete from ayudante;";
					break;
				case 28:
					sqlString += "delete from est_car;";
					break;
				case 29:
					sqlString += "delete from estudiante ;";
					break;
				case 30:
					sqlString += "delete from remuneracion;";
					break;
				case 31:
					sqlString += "delete from secretaria;";
					break;
				case 32:
					sqlString += "delete from ftp;";
					break;
				case 33:
					sqlString += "delete from persona;";
					break;
				case 34:
					sqlString += "delete from plan;";
					break;
				case 35:
					sqlString += "delete from \"general\" where est_gen =false;";
				case 36:
					sqlString += "delete from sucursal where estado = false;";
					break;
				case 37:
					sqlString += "INSERT INTO public.persona (ci_per,nom_per,priape_per,segape_per,tel_per,dir_per,ema_per,fot_per,est_per,sex_per,cod_per,codbio_per,codesc_per) VALUES ('0000000','S','/','N',' ',' ',' ','avatar00.jpg',true,true,0,NULL,'0-20160101');";
					break;
				case 38:
					sqlString += "INSERT INTO public.persona (ci_per,nom_per,priape_per,segape_per,tel_per,dir_per,ema_per,fot_per,est_per,sex_per,cod_per,codbio_per,codesc_per) VALUES ('7167968','Carlos Franz','Gutierrez','Gutierrez','75136609','Calle Mexico entre German','','avatar00.jpg',true,true,1,'','1-99-20160101');";
					break;
				case 39:
					sqlString += "INSERT INTO public.usurol (cod_rol,fecha_alta,fecha_baja,cod_per) VALUES (1,'2016-04-11 07:49:51.000',NULL,1),(2,'2016-04-27 02:42:48.000',NULL,1);";
					break;
				case 40:
					sqlString += "INSERT INTO public.dato (log_dat,cla_dat,fecha_alta,fecha_baja,cod_per) VALUES ('admin','admin7167968',now(),NULL,1);";
					break;
				case 41:
					sqlString += "INSERT INTO public.tiene_sucursal (cod_per,cod_suc) VALUES (1,1);";
					break;
				case 42:
					sqlString += "INSERT INTO public.cliente (cod_per,cod_cli,est_cli) VALUES (0,1,true);";
					break;
				case 43:
					sqlString += "INSERT INTO public.empresa (cod_emp,nom_emp,dir_emp,tel_emp,ema_emp,est_emp) VALUES (0,'S/N','Sin direccion','0',NULL,true);";
					break;
				case 44:
					sqlString += "INSERT INTO public.proveedor (cod_per,cod_pro,cod_emp,est_pro) VALUES (0,1,0,true);";
					break;
				case 45:
					sqlString += "delete from codigoproducto;";
					break;
				case 46:
					sqlString += "delete from producto; ";
					break;
				case 47:
					sqlString += "delete from tipoproducto;";
					break;
				case 48:
					sqlString += "INSERT INTO public.sucursal (cod_suc,nombre,descripcion,estado) VALUES (1,'Casa Matriz','Sucursal de la Calle Madrid entre O''Connor y Junin',true);";
					break;
				case 49:
					sqlString += "INSERT INTO public.\"general\" (ges_gen,des_gen,est_gen,nom_gen,logtex_gen,logsintex_gen,tel_gen,dir_gen,lug_gen,nit_gen,cod_suc) VALUES (2021,'CENTRO DE DEPORTES Y RECREACION',true,'RESULTADO FITNESS','logo-2021.jpg','loguito-2021.jpg','69327201','Calle Madrid, Zona Central','Tarija - Bolivia','76193133',1);";
					break;
				case 50:
					sqlString += "update producto set can_pro = 0;";
					break;
				case 51:
					sqlString += "delete from detalle_asiento_contable;";
					break;
				case 52:
					sqlString += "delete from asiento_contable;";
					break;
				case 53:
					sqlString += "delete from detalle_asiento_contable where cod_asiento >1;delete from asiento_contable where cod_asiento >1;";
					sqlString += "update arqueocaja set cod_asiento = null where cod_asiento >1;";
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
