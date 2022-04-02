package net.resultadofinal.micomercial.util;

public class DbConeccion {
	public String asDato=" as (log_dat character varying(50),cla_dat character varying(50),fecha_alta timestamp(6),fecha_baja timestamp(6),cod_per bigint)";
	public String asMenu=" as (cod_men integer ,nom_men character varying(100) ,des_men character varying(500),ico_men character varying(100),est_men boolean)";
	public String asRol=" as (cod_rol integer,nom_rol character varying(100),des_rol character varying(1000),est_rol boolean)";
	public String asTipoProducto=" as (cod_tippro integer,nom_tippro character varying(100),des_tippro character varying(250),est_tippro boolean)";
	public String asProducto=" as (nom_tippro character varying(100),cod_pro integer,nom_pro character varying(100) ,precom_pro real,preven_pro real,gan_pro real,can_pro integer,est_pro boolean,cod_tippro integer)";
	public String asCompra=" as (cod_com bigint ,cod_per bigint,cod_pro bigint,fec_com date,obs_com character varying(500),tot_com numeric(10,2),est_com boolean,des_com numeric(10,2),ges_gen integer,cod_arqcaj bigint,cod_detarq integer,cod_suc integer,subtot_com numeric(10,2),tipo boolean, saldo_deber numeric(10,2), acuenta numeric(10,2))";
	public String asDetalleCompra=" as (cod_com bigint,cod_detcom integer,cod_pro bigint,pre_detcom numeric(10,2),can_detcom integer,des_detcom numeric(10,2),subtot_detcom numeric(10,2),tot_detcom numeric(10,2))";
	public String asProveedor=" as (cod_pro bigint,cod_emp integer,est_pro boolean)";
	public String asCliente=" as (cod_cli bigint,est_cli boolean)";
	public String asVenta=" as (cod_ven bigint,cod_per bigint,cod_cli bigint,fec_ven timestamp,obs_ven character varying(500),tot_ven real,des_ven real,ges_gen integer,est_ven boolean,cod_arqcaj bigint,cod_detarq integer,tip_ven integer,cod_suc integer,subtot_ven real,cod_subcuenta integer)";
	public String asFacultad=" as (cod_fac integer,nom_fac character varying(150),sig_fac character varying(25),fot_fac character varying(50),est_fac boolean,cod_ins integer)";
	public String asTipoServicio=" as (cod_tipser integer,nom_tipser character varying(150),des_tipser character varying(500),est_tipser boolean)";
	public String asServicio=" as (cod_ser integer,nom_ser character varying(150),des_ser character varying(500),est_ser boolean,cod_tipser integer,pre_ser real,porcentaje_multa real,porcentaje_ayudante real)";
	public String asAyudante=" as (cod_ayu bigint,est_ayu boolean,cod_car integer,dis_ayu character varying(1500))";
	public String asEstudiante=" as (cod_est bigint,est_est boolean,teltut_est character varying(30),tut_est character varying(150))";
	public String asTema=" as (cod_tem integer,nom_tem character varying(150),des_tem character varying(500),est_tem boolean,cod_mat integer)";
	public String asDocente=" as (cod_doc bigint,pro_doc character varying(50),abrpro_doc character varying(25),est_doc boolean)";
	public String asPrestacion=" as (cod_pre bigint,cod_per bigint,cod_est bigint,fec_pre timestamp(6),obs_pre character varying(500),tot_pre real,des_pre real,ges_gen integer,est_pre boolean,cod_ayu bigint,tip_pre integer, fecent_pre date,horent_pre character varying(5),cod_doc bigint,sal_pre real,cod_arqcaj bigint,cod_detarq integer,pag_pre real,por_pre real,cod_suc int4,cod_subcuenta integer)";
	public String asDetallePrestacion=" as (cod_pre bigint,cod_detpre integer,cod_ser integer,pre_detpre real,cod_tem integer)";
	public String asPlan=" as (cod_pla integer ,nom_pla character varying(150) ,sig_pla character varying(25) ,fini_pla date ,ffin_pla date ,hor_pla integer ,mon_pla real ,tip_pla smallint ,des_pla real , est_pla boolean ,obs_pla character varying(1500) ,ges_gen integer,tra_pla integer,cod_suc integer)";
	public String asMatPlaAyu=" as (cod_pla integer,cod_mat integer,cod_ayu bigint,cod_matplaayu character varying(24))";
	public String asRemuneracion=" as (cod_per bigint,cod_sec bigint,fec_rem timestamp,fini_rem date,ffin_rem date,mes_rem integer,ges_rem integer,des_rem character varying(1500),est_rem boolean,cod_rem bigint,mon_rem real,cod_suc integer,cod_gen integer)";
	public String asHorarioClase=" as (cod_horcla integer,cod_ayu bigint,fec_horcla date,est_horcla boolean)";
	public String asSucursal = " as (cod_suc integer,nombre character varying(150),descripcion character varying(500),estado boolean)";
	public String asAsientoContable = "as (cod_asiento int8, numero int4, fecha date, concepto varchar(250), estado bool, created_by varchar(100), created_at timestamp, ges_gen int4, cod_suc int4)";
	public String asDetalleAsientoContable = " as (cod_asiento int8, cod_detalle int4, cod_subcuenta int4, debe numeric(10,2), haber numeric(10,2))";
	public String asCuentaContable = "as (tipo_cuenta int4,nombre varchar(50),descripcion varchar(150),codigo varchar(10),tipo_grupo int4,cod_cuenta int4,estado bool)";
	public String asSubcuentaContable = "as (cod_cuenta int4,cod_subcuenta int4,nombre varchar(50),descripcion varchar(150),estado bool,codigo varchar(10),es_externo bool)";
	public String asMulta = "as (cod_multa int8,monto float4,cod_ayu int8,tipo int2,concepto varchar(500),observacion varchar(250),cod_pre int8 ,estado bool,fecha date,created_by int8,created_at timestamp(0),cod_arqcaj bigint,cod_detarq int4)";
	public String asPagoPrestacion = "as (cod_pago_prestacion int8,estado bool,cod_arqcaj int8,cod_ayu int8,created_at timestamp)";
	public String asDetallePagoMulta = "as detalle_pago_multa (cod_pago_prestacion int8,cod_multa int8)";
	public String asCotizacion = "as cotizacion (cotizacion_id int8,celular varchar(50),cod_tipser int4,tipo_estudio varchar(50),ciudad varchar(50),fecha timestamp(0),estado bool,cod_pre int8,celular_propietario varchar(150),precio float4,docente varchar(150),created_by int8,created_at timestamp(0),gestion int4,cod_suc int4,materia varchar(150),tema varchar(200),cod_ayu bigint)";
	//Wrap
	public String asAyudantePrestacion = "as (cod_ayu int8,ayudante varchar(100),total int8, prestaciones text)";
	
	
	public String asObjectAdd(String tabla,String add){
		if(add.equals(""))
			return tabla;
		else
			return tabla.substring(0,tabla.length()-1)+","+add+")";
	}

}
