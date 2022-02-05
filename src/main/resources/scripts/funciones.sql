CREATE OR REPLACE FUNCTION public.arqueocaja_cerrar(cusfin bigint, monfin real, des character varying, monrea real, cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update arqueocaja set (cusfin_arqcaj,ffin_arqcaj,monfin_arqcaj,des_arqcaj,monrea_arqcaj)=(cusfin,now(),monfin,des,monrea) where cod_arqcaj=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_cerrar_caja_inicial(cusfin bigint, monfin real, des character varying, monrea real, cod bigint, code_asiento bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update arqueocaja set (cusfin_arqcaj,ffin_arqcaj,monfin_arqcaj,des_arqcaj,monrea_arqcaj,cod_asiento)=(cusfin,now(),monfin,des,monrea,code_asiento) where cod_arqcaj=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_darestado(cod bigint, est boolean, code_per bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update arqueocaja set est_arqcaj=est,cusfin_arqcaj=code_per,ffin_arqcaj=now() where cod_arqcaj=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_iniciar(del bigint, cusini bigint, monini real, ges integer, des character varying, suc integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_arqcaj),0)+1 from arqueocaja);
	insert into arqueocaja(cod_arqcaj,del_arqcaj,cusini_arqcaj,fini_arqcaj,monini_arqcaj,ges_gen,des_arqcaj,cod_suc)
	values(cod,del,cusini,now(),monini,ges,des,suc);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_lista(inicio integer, limite integer, buscar character varying, estado boolean, cod_user bigint, fini date, ffin date, gestion integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
		 SELECT cod_arqcaj,del_arqcaj,cusini_arqcaj,fini_arqcaj,monini_arqcaj,ffin_arqcaj,monfin_arqcaj,est_arqcaj,monrea_arqcaj,ges_gen,des_arqcaj,cusfin_arqcaj,cod_suc,cod_asiento,
cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) delegado,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final,cast(to_char(fini_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechai,cast(to_char(ffin_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechaf
			FROM arqueocaja ac
	join persona p1 on p1.cod_per=ac.del_arqcaj
	join persona p2 on p2.cod_per=ac.cusini_arqcaj
	join persona p3 on p3.cod_per=ac.cusfin_arqcaj
			where est_ven=estado and (ac.cusini_arqcaj=cod_user or cod_user=-1) and ges_gen=gestion and fec_ven>=fini and fec_ven<=ffin;
	else
	tot:=(select count(*) from arqueocaja where est_arqcaj=estado); 
	return query 
 SELECT cod_arqcaj,del_arqcaj,cusini_arqcaj,fini_arqcaj,monini_arqcaj,ffin_arqcaj,monfin_arqcaj,est_arqcaj,monrea_arqcaj,ges_gen,des_arqcaj,cusfin_arqcaj,cod_suc,cod_asiento,
		row_number() OVER(ORDER BY ac.cod_arqcaj desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) delegado,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final,cast(to_char(fini_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechai,cast(to_char(ffin_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechaf
		from arqueocaja ac
	join persona p1 on p1.cod_per=ac.del_arqcaj
	join persona p2 on p2.cod_per=ac.cusini_arqcaj
	left join persona p3 on p3.cod_per=ac.cusfin_arqcaj
	where ac.est_arqcaj=estado and (ac.cusini_arqcaj=cod_user or cod_user=-1) and ac.ges_gen=gestion and cast(ac.fini_arqcaj as date)>=fini and cast(ac.fini_arqcaj as date)<=ffin and upper(p1.nom_per||' '||p1.priape_per||' '||p1.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_modificar(del bigint, cusini bigint, monini real, ges integer, des character varying, cod bigint, suc integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update arqueocaja set (del_arqcaj,cusini_arqcaj,fini_arqcaj,monini_arqcaj,ges_gen,des_arqcaj,cod_suc)=(del,cusini,now(),monini,ges,des,suc) where cod_arqcaj=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT cod_arqcaj,del_arqcaj,cusini_arqcaj,fini_arqcaj,monini_arqcaj,ffin_arqcaj,monfin_arqcaj,est_arqcaj,monrea_arqcaj,ges_gen,des_arqcaj,cusfin_arqcaj,cod_suc,cod_asiento,
cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) delegado,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final,cast(to_char(fini_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechai,cast(to_char(ffin_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechaf
  FROM arqueocaja ac
	join persona p1 on p1.cod_per=ac.del_arqcaj
	join persona p2 on p2.cod_per=ac.cusini_arqcaj
	left join persona p3 on p3.cod_per=ac.cusfin_arqcaj
			where cod_arqcaj=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_obtener_sesion(cod bigint)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$declare code_arqcaj int;
BEGIN
	code_arqcaj:=(SELECT coalesce(max(cod_arqcaj),-1) FROM arqueocaja ac where ac.del_arqcaj=cod and ac.cusfin_arqcaj is  null and ac.est_arqcaj=true);
	return code_arqcaj;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_obtenertotal(cod bigint)
 RETURNS real
 LANGUAGE plpgsql
AS $function$declare inicial FLOAT:=0;
ingresos FLOAT:=0;
egresos FLOAT:=0;
BEGIN
	ingresos:=(select coalesce(sum(mon_detarq),0) 
from detallearqueo 
where est_detarq=true and cod_arqcaj=cod and tip_detarq in (5,6,7,8,9,10,13,16));
  egresos:=(select coalesce(sum(mon_detarq),0) from detallearqueo 
 where est_detarq=true and cod_arqcaj=cod and tip_detarq in (1,2,3,4,14,15,17));
	inicial:=(select monini_arqcaj from arqueocaja where cod_arqcaj=cod)+ingresos-egresos;
	return inicial ;
END$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_verificar_sesion_actual(cod bigint, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT ac.*,
cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) delegado,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) custodio_inicial,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) custodio_final,cast(to_char(fini_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechai,cast(to_char(ffin_arqcaj,'dd/MM/yyyy hh:mm') as varchar(25)) fechaf
  FROM arqueocaja ac
	join persona p1 on p1.cod_per=ac.del_arqcaj
	join persona p2 on p2.cod_per=ac.cusini_arqcaj
	left join persona p3 on p3.cod_per=ac.cusfin_arqcaj
	where ac.del_arqcaj=cod and ac.cod_suc = suc and ac.cusfin_arqcaj is  null and ac.est_arqcaj=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.asiento_adicionar(ges integer, suc integer, fec date, creado character varying, obs character varying, cuentas integer[], debes double precision[], haberes double precision[])
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE cod bigint;
indice int:=1;
tam int:=array_length(cuentas,1);
num int;
monto float:=0;
BEGIN
	num:=(select coalesce(max(numero),0)+1 from asiento_contable ac where ges_gen =ges and cod_suc =suc);
	cod:=(select coalesce(max(cod_asiento),0)+1 from asiento_contable);
	insert into asiento_contable(cod_asiento,cod_suc,ges_gen,numero,concepto,created_at,created_by,estado,fecha) 
	values(cod,suc,ges,num,obs,now(),creado,true,fec);
	while indice<=tam
	loop
		insert into detalle_asiento_contable (cod_asiento,cod_detalle,cod_subcuenta,debe,haber) values(cod,indice,cuentas[indice],debes[indice],haberes[indice]);
		indice=indice+1;
	end loop;
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.asistencia_marcar(codigo character varying)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
code_est int;
code_ins int;
actual timestamp;
BEGIN
	cod:=(select coalesce(max(cod_per),0) from persona where codesc_per=codigo);
	if(cod!=0)THEN
		code_est:=(select coalesce(max(cod_est),0) from estudiante where cod_per=cod);
		if(code_est!=0)THEN
			actual:=(select now());
			code_ins=(select coalesce(max(cod_ins),0) from inscripcion
								join plan on inscripcion.cod_pla=plan.cod_pla and fini_pla<=cast(actual as date) and ffin_pla>=cast(actual as date)
								where est_ins=true and cod_est=code_est);
				if(code_ins!=0)THEN
					insert into asistencia_inscripcion(cod_ins,cod_est,fecreg_est) values(code_ins,code_est,actual);
					return query select code_est estudiante,code_ins inscripcion,cast(actual as varchar(25)) fecha;
				else RETURN query select -1 estudiante,-1 inscripcion,cast('' as varchar(25)) fecha;
				end if;
		else RETURN query select -1 estudiante,-1 inscripcion,cast('' as varchar(25)) fecha;
		end if;
		else RETURN query select -1 estudiante,-1 inscripcion,cast('' as varchar(25)) fecha;
	end if;
	--RETURN query select -1 estudiante,-1 inscripcion,cast('' as varchar(25)) fecha;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayu_tipmat_adicionar(code_ayu bigint, tipos integer[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
	indice int:=1;
	tam int:=array_length(tipos,1);
BEGIN
	delete from ayu_tipmat where cod_ayu=code_ayu;
	while indice<=tam
	loop
		insert into ayu_tipmat(cod_ayu,cod_tipmat) values(code_ayu,tipos[indice]);
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_adicionar(code_per bigint, code_car integer, dis character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into ayudante(cod_ayu,cod_car,dis_ayu) values(code_per,code_car,dis);
	if not EXISTS(select * from usurol where cod_per=code_per and cod_rol=5 and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(code_per,5);
	end if;
		RETURN code_per;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_buscar(buscar character varying)
 RETURNS SETOF ayudante
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query
	select a.* from ayudante a
	join ayu_tipmat  atm on atm.cod_ayu=a.cod_ayu
	join tipomateria tm on tm.cod_tipmat=atm.cod_tipmat and upper(tm.nom_tipmat) like upper('%'||buscar||'%')
	join persona p on p.cod_per=a.cod_per
	where est_ayu=true;
END$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update ayudante set est_ayu=est where cod_ayu=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_ayu,est_ayu,carrera.cod_car, dis_ayu,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,nom_car
			FROM ayudante
			JOIN persona on persona.cod_per=ayudante.cod_ayu
			JOIN carrera on carrera.cod_car=ayudante.cod_car
			where est_ayu=estado;
	else
	tot:=(select count(*) from ayudante where est_ayu=estado);
 return query 
	select cod_ayu,est_ayu,carrera.cod_car, dis_ayu,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,nom_car,
		(select  string_agg(tm.nom_tipmat,', ')
		from tipomateria tm 
		join ayu_tipmat atm on tm.cod_tipmat=atm.cod_tipmat and atm.cod_ayu=ayudante.cod_ayu where tm.est_tipmat=true) materias,
		row_number() OVER(ORDER BY ayudante.cod_ayu) as RN,tot as Tot
		from ayudante
		JOIN persona on persona.cod_per=ayudante.cod_ayu
		JOIN carrera on carrera.cod_car=ayudante.cod_car
	where est_ayu=estado and upper(nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_modificar(dis character varying, code_car integer, cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update ayudante set (cod_car,dis_ayu)=(code_car,dis) where cod_ayu=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_obtener(cod bigint)
 RETURNS SETOF ayudante
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT *
  FROM ayudante
			where cod_ayu=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_obtener_deuda_por_inscripcion(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select mpa.cod_matplaayu,cast(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per) as varchar(100)) ayudante
		from mat_pla_ayu mpa
		join ayudante a on mpa.cod_ayu=a.cod_ayu
		join persona p on p.cod_per=a.cod_ayu
		where mpa.cod_pla=cod and mpa.est_pag=1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_obtener_deudas(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN

	RETURN query 
select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,cod_suc,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
			,(select string_agg(concat(ts.nom_tipser,' ',s.nom_ser),',') from detalleprestacion dp
inner join servicio s on s.cod_ser=dp.cod_ser
inner join tiposervicio ts on ts.cod_tipser=s.cod_tipser
where dp.cod_pre=prestacion.cod_pre) servicios
			from prestacion
			join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
			where est_pre=true and prestacion.cod_ayu=cod and tip_pre=2 and tot_pre=sal_pre;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_obtener_materias(code_ayu bigint)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$DECLARE cadena varchar(1000);
BEGIN
	cadena:=(select  string_agg(tm.nom_tipmat,', ')
		from tipomateria tm 
		join ayu_tipmat atm on tm.cod_tipmat=atm.cod_tipmat and atm.cod_ayu=code_ayu where tm.est_tipmat=true);
	return cadena;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_obtener_x_codper(cod bigint)
 RETURNS SETOF ayudante
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT *
  FROM ayudante
			where cod_per=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_pagar_por_plan(code_matplaayu character varying, porcentaje real, total real, cod_usuario bigint, nom character varying, cod bigint, ayudante character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
	code_arqcaj bigint;
	code_detarq int;
BEGIN
	update mat_pla_ayu set por_pag=porcentaje,mon_pag=total,est_pag=2 where cod_matplaayu=code_matplaayu;
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,3, concat('Por pago de plan ',nom,' culminado al ayudante ',ayudante,'. Cod. Plan #',cod), total));
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.ayudante_pagar_por_prestacion(ayudante character varying, prestaciones character varying[], cod_usuario bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
	indice int:=1;
	tam int:=array_length(prestaciones,1);
	code_pre bigint;
	pago float;
	monto float:=0;
	code_arqcaj bigint;
	code_detarq int;
	porcentaje float;
BEGIN
	while indice<=tam
	loop
		code_pre=(cast(split_part(prestaciones[indice],'-', 1) as BIGINT));
		pago=(cast(split_part(prestaciones[indice],'-', 2) as FLOAT));
		porcentaje=(cast(split_part(prestaciones[indice],'-', 3) as FLOAT));
		monto=monto+pago;
		update prestacion set tip_pre=4,pag_pre=pago,por_pre=porcentaje where cod_pre=code_pre;
		indice=indice+1;
	end loop;
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,3, concat('Por pago de servicios prestados al ayudante ',ayudante,'. Cod. Prestamo #',code_pre), monto));
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_adicionar(nom character varying, sig character varying, tippro character varying, code_fac integer, abrtippro character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_car),0)+1 from carrera);
	insert into carrera(cod_car,nom_car,sig_car,tippro_car,abrtippro_car,cod_fac) values(cod,nom,sig,tippro,abrtippro,code_fac);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update carrera set est_car=est where cod_car=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_car, nom_car, sig_car, est_car, carrera.cod_fac, tippro_car, abrtippro_car,nom_fac,nom_ins 
			from carrera
			join facultad on facultad.cod_fac=carrera.cod_fac
			join institucion on institucion.cod_ins=facultad.cod_ins
			where est_fac=estado;
	else
	tot:=(select count(*) from carrera where est_car=estado);
 return query 
	select cod_car, nom_car, sig_car, est_car, carrera.cod_fac, tippro_car, abrtippro_car,nom_fac,nom_ins, 
		row_number() OVER(ORDER BY carrera.cod_car) as RN,tot as Tot
		from carrera 
	join facultad on facultad.cod_fac=carrera.cod_fac
	join institucion on institucion.cod_ins=facultad.cod_ins
	where est_car=estado and upper(nom_car) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_modificar(nom character varying, sig character varying, tippro character varying, abrtippro character varying, code_fac integer, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update carrera set (nom_car,sig_car,tippro_car,abrtippro_car,cod_fac)=(nom,sig,tippro,abrtippro,code_fac) where cod_car=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_obtener(cod integer)
 RETURNS SETOF carrera
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from carrera where cod_car=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_obtenerxcod_est(code_est bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select c.cod_car, nom_car, sig_car, est_car, cod_fac, tippro_car, abrtippro_car,case when ec.cod_car is null then cast('' as varchar(8)) else cast('selected' as varchar(8)) end as tipo  
		from carrera c 
		left join est_car ec on ec.cod_car=c.cod_car and ec.cod_est=code_est 
		where c.est_car=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.carrera_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	--IF EXISTS (select carrera.* from carrera join facultad on facultad.cod_fac=carrera.cod_fac join institucion on institucion.cod_ins=facultad.cod_ins where nom_car=nom) THEN
	RETURN FALSE;
	--END IF;
	--RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cliente_adicionar(codigo bigint)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into cliente(cod_cli) values(codigo);
	if not EXISTS(select * from usurol where cod_per=codigo and cod_rol=7 and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(codigo,7);
	end if;	
	RETURN codigo;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cliente_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update cliente set est_cli=est where cod_cli=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cliente_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_cli,est_cli,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per
			FROM cliente
			JOIN persona on persona.cod_per=cliente.cod_cli
			where est_cli=estado;
	else
	tot:=(select count(*) from cliente where est_cli=estado);
 return query 
	select cliente.cod_cli,est_cli,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,
		row_number() OVER(ORDER BY cliente.cod_cli desc) as RN,tot as Tot
		from cliente
		JOIN persona on persona.cod_per=cliente.cod_cli
	where est_cli=estado and upper(nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cliente_obtener(cod bigint)
 RETURNS SETOF cliente
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT  cod_cli, est_cli
  FROM cliente
			where cod_cli=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.compra_adicionar(cod_usuario bigint, cod_proveedor bigint, fec date, obs character varying, tot double precision, des double precision, gestion integer, suc integer, productos integer[], precios double precision[], cantidades integer[], descuentos double precision[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(productos,1);
code_arqcaj bigint;
code_detarq int;
monto float:=0;
BEGIN
	while indice<=tam
	loop
		monto:=monto+descuentos[indice];
		indice=indice+1;
	end loop;
	indice:=1;
	cod:=(select coalesce(max(cod_com),0)+1 from compra);	
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,4, cast('Por compra de productos.' as varchar), cast((tot-des-monto)as FLOAT)));
	insert into compra(cod_com,cod_per,cod_pro,fec_com,obs_com,tot_com,des_com,ges_gen,cod_arqcaj,cod_detarq,cod_suc) values(cod,cod_usuario,cod_proveedor,fec,obs,tot,des,gestion,code_arqcaj,code_detarq,suc);
	while indice<=tam
	loop
		insert into detallecompra(cod_com,cod_detcom,cod_pro,pre_detcom,can_detcom,des_detcom) values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice]);
		update producto set can_pro=can_pro+cantidades[indice],precom_pro=precios[indice],preven_pro=round(cast((precios[indice]+gan_pro) as decimal),2) where cod_pro=productos[indice];
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.compra_adicionar(cod_usuario bigint, cod_proveedor bigint, fec date, obs character varying, subtot double precision, tot double precision, des double precision, gestion integer, suc integer, productos integer[], precios double precision[], cantidades integer[], descuentos double precision[], subtotales double precision[], totales double precision[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(productos,1);
code_arqcaj bigint;
code_detarq int;
monto float:=0;
BEGIN
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,4, cast('Por compra de productos.' as varchar), tot));
	cod:=(select coalesce(max(cod_com),0)+1 from compra);
	insert into compra(cod_com,cod_per,cod_pro,fec_com,obs_com,subtot_com,tot_com,des_com,ges_gen,cod_arqcaj,cod_detarq,cod_suc) values(cod,cod_usuario,cod_proveedor,fec,obs,subtot,tot,des,gestion,code_arqcaj,code_detarq,suc);
	while indice<=tam
	loop
		insert into detallecompra(cod_com,cod_detcom,cod_pro,pre_detcom,can_detcom,des_detcom,subtot_detcom ,tot_detcom) values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice],subtotales[indice],totales[indice]);
		update producto set can_pro=can_pro+cantidades[indice],precom_pro=precios[indice],preven_pro=round(cast((precios[indice]+gan_pro) as decimal),2) where cod_pro=productos[indice];
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.compra_eliminar(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE codigo INTEGER;
DECLARE cantidad INTEGER;
DECLARE v_cursor CURSOR FOR select cod_pro,can_detcom from detallecompra where cod_com=cod ;
DECLARE sql_result record;
DECLARE code_arqcaj bigint;
DECLARE code_detarq int;
BEGIN
	code_arqcaj=(select cod_arqcaj from compra where cod_com=cod);
	code_detarq=(select cod_detarq from compra where cod_com=cod);
	update detallearqueo set est_detarq=false where cod_arqcaj=code_arqcaj and cod_detarq=code_detarq;
	OPEN v_cursor;
		loop
		FETCH v_cursor INTO codigo,cantidad;
			EXIT WHEN NOT FOUND;
				update producto set can_pro=can_pro-cantidad where cod_pro=codigo;
		end loop;
		close v_cursor;
		update compra set est_com=est where cod_com=cod;
		RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.compra_lista(inicio integer, limite integer, buscar character varying, estado boolean, cod_user bigint, fini date, ffin date, gestion integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select compra.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) proveedor,cast(to_char(fec_com,'dd/MM/yyyy') as varchar(15)) fecha
			from compra
			join persona p1 on p1.cod_per=compra.cod_per
			join proveedor on proveedor.cod_pro=compra.cod_pro
			join persona p2 on p2.cod_per=proveedor.cod_pro
			where est_com=estado and (compra.cod_per=cod_user or cod_user=-1) and ges_gen=gestion and fec_com>=fini and fec_com<=ffin;
	else
	tot:=(select count(*) from compra where est_com=estado); 
	return query 
	select compra.*,
		row_number() OVER(ORDER BY compra.cod_com desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,
		cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) proveedor,cast(to_char(fec_com,'dd/MM/yyyy') as varchar(15)) fecha
		from compra
	join persona p1 on p1.cod_per=compra.cod_per
	join proveedor on proveedor.cod_pro=compra.cod_pro
	join persona p2 on p2.cod_per=proveedor.cod_pro
	where est_com=estado and (compra.cod_per=cod_user or cod_user=-1) and ges_gen=gestion and fec_com>=fini and fec_com<=ffin and cast(cod_com as varchar) like concat('%',buscar,'%') LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.compra_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN query select cod_com, compra.cod_per, compra.cod_pro, fec_com, obs_com, tot_com, est_com, des_com, ges_gen,cod_arqcaj,cod_detarq,cod_suc,subtot_com ,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) proveedor,cast(to_char(fec_com,'dd/MM/yyyy') as varchar(15)) fecha
			from compra
			join persona p1 on p1.cod_per=compra.cod_per
			join proveedor on proveedor.cod_pro=compra.cod_pro
			join persona p2 on p2.cod_per=proveedor.cod_pro
			where cod_com=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cuenta_contable_lista(inicio integer, limite integer, buscar character varying, est boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cc.* ,
case when cc.tipo_cuenta = 1 then 'Activo' else case when cc.tipo_cuenta = 2 then 'Pasivo' else case when cc.tipo_cuenta = 3 then 'Capital' else case when cc.tipo_cuenta = 4 then 'Egreso' else 'Ingreso' end end end end as xtipo
from cuenta_contable cc
		where cc.estado =est;
	else
	tot:=(select count(*) from general where est_gen=est);
 return query 
	select cc.* ,
case when cc.tipo_cuenta = 1 then 'Activo' else case when cc.tipo_cuenta = 2 then 'Pasivo' else case when cc.tipo_cuenta = 3 then 'Capital' else case when cc.tipo_cuenta = 4 then 'Egreso' else 'Ingreso' end end end end as xtipo,
		row_number() OVER(ORDER BY cc.codigo asc) as RN,tot as Tot
		from cuenta_contable cc
	where cc.estado =est and upper(cc.codigo) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cuenta_obtener(cod integer)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare xcuenta varchar(50);
BEGIN
	xcuenta:=(select nombre from subcuenta_contable where cod_subcuenta=cod);
	return xcuenta;
END$function$
;

CREATE OR REPLACE FUNCTION public.dato_adicionar(cod bigint, log character varying, cla character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update dato set fecha_baja=now() where cod_per=cod and fecha_baja is null;
	insert into dato(cod_per,log_dat,cla_dat,fecha_alta) values(cod,log,cla,now());
		RETURN TRUE;
		EXCEPTION
			WHEN OTHERS THEN
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.dato_adicionar_biometrico(cod bigint, code_bio character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update persona set codbio_per=code_bio where cod_per=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.dato_eliminar(cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update dato set fecha_baja=now() where cod_per=cod and fecha_baja is NULL;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.dato_obtener(cod bigint)
 RETURNS SETOF dato
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select dato.* from dato where cod_per=cod and fecha_baja is null;
END
$function$
;

CREATE OR REPLACE FUNCTION public.dato_validarbiometrico(biometrico character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS(select * from persona where codbio_per=biometrico)
	THEN
		RETURN TRUE;
	ELSE
		RETURN FALSE;
	END IF;
END
$function$
;

CREATE OR REPLACE FUNCTION public.dato_validarlogin(log character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS(select * from dato where log_dat=log and fecha_baja is null)
	THEN
		RETURN TRUE;
	ELSE
		RETURN FALSE;
	END IF;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallearqueo_adicionar(code_arqcaj bigint, tip integer, des character varying, mon double precision, codesub integer)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$ DECLARE cod bigint;
BEGIN
		cod:=(select COALESCE(max(cod_detarq),0)+1 from detallearqueo where cod_arqcaj=code_arqcaj);
		insert into detallearqueo(cod_arqcaj,cod_detarq,tip_detarq,des_detarq,mon_detarq,fec_detarq,cod_subcuenta) values(code_arqcaj,cod,tip,des,mon,now(),codesub);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallearqueo_adicionar(code_arqcaj bigint, tip integer, des character varying, mon double precision)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$ DECLARE cod bigint;
BEGIN
		cod:=(select COALESCE(max(cod_detarq),0)+1 from detallearqueo where cod_arqcaj=code_arqcaj);
		insert into detallearqueo(cod_arqcaj,cod_detarq,tip_detarq,des_detarq,mon_detarq,fec_detarq) values(code_arqcaj,cod,tip,des,mon,now());
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallearqueo_eliminar(code_arqcaj bigint, code_det integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	delete from detallearqueo where cod_arqcaj =code_arqcaj and cod_detarq =code_det;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallearqueo_lista(inicio integer, limite integer, buscar character varying, estado boolean, code_arqcaj bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_arqcaj, cod_detarq, tip_detarq, des_detarq, mon_detarq, fec_detarq,cast(to_char(fec_detarq,'dd/MM/yyyy hh:mm:ss') as varchar(25)) fecha,
			case tip_detarq 
			when 1 then 
				'Egreso de Caja.' 
			when 2 then 
				'Pago al ayudante por inscripcion.' 
			when 3 then 
				'Pago al ayudante por prestacion.' 
			when 4 then 
					'Por compra de productos'
			when 5 then 
					'Ingreso a caja.' 
			when 6 then 
					'Ingreso por inscripcion de estudiante.' 
			when 7 then 
					'Ingreso por prestacion de servicio.' 
			when 8 then 
					'Ventas' 
			when 9 then 
					'Caja General' 
			when 10 then 
					'Banco general' 
			when 11 then 
					'Muebles' 
			when 12 then 
					'Inmuebles' 
			when 13 then 
					'Otros activos' 
			when 14 then 
					'Prestamos bancarios' 
			when 15 then 
					'Otros pasivos' 
			when 16 then 
					'Ingreso general' 
			when 17 then 
					'Egreso general'
			when 20 then 
					'Venta con pago bancario'
			else 
				'none'
			end as tipo
			from detallearqueo 
			where cod_arqcaj=code_arqcaj and est_detarq=estado;
	else
	tot:=(select count(*) from detallearqueo where cod_arqcaj=code_arqcaj and est_detarq=estado);
 return query 
	select cod_arqcaj, cod_detarq, tip_detarq, des_detarq, mon_detarq, fec_detarq,
		row_number() OVER(ORDER BY detallearqueo.cod_detarq desc) as RN,tot as Tot,cast(to_char(fec_detarq,'dd/MM/yyyy hh:mm:ss') as varchar(25)) fecha
		from detallearqueo 
	where est_detarq=estado and cod_arqcaj=code_arqcaj and cast(cod_arqcaj as varchar) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallearqueo_obtener(cod integer, code_arqcaj bigint)
 RETURNS SETOF detallearqueo
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from detallearqueo where cod_detarq=cod and cod_arqcaj=code_arqcaj;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallearqueo_obtenerxarqueocaja(cod bigint)
 RETURNS SETOF detallearqueo
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from detallearqueo where cod_arqcaj=cod and est_detarq = true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallecompra_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT d.cod_com, d.cod_detcom, d.cod_pro, d.pre_detcom, d.can_detcom, d.des_detcom,d.subtot_detcom ,d.tot_detcom ,cast(concat(tp.nom_tippro,' ',p.nom_pro) as varchar(150)) producto
	FROM detallecompra d
	join producto p on p.cod_pro=d.cod_pro
	join tipoproducto tp on tp.cod_tippro=p.cod_tippro
	where d.cod_com=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorario_lista(inicio integer, limite integer, buscar character varying, estado boolean, code_horcla integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_horcla, cod_dethorcla,tem_dethorcla,horini,p1.cod_per,cod_usu,est_dethorcla,horfin,pri_dethorcla,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) persona,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario
			from detallehorarioclase
			join persona p1 on p1.cod_per=detallehorarioclase.cod_per
			join persona p2 on p2.cod_per=detallehorarioclase.cod_per
			where cod_dethorcla=code_horcla and est_dethorcla=estado;
	else
	tot:=(select count(*) from detallehorarioclase where cod_horcla=code_horcla and est_dethorcla=estado);
 return query 
	select cod_horcla, cod_dethorcla,tem_dethorcla,horini,p1.cod_per,cod_usu,est_dethorcla,horfin,pri_dethorcla,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) persona,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario,
		row_number() OVER(ORDER BY detallehorarioclase.horini) as RN,tot as Tot
		from detallehorarioclase
			join persona p1 on p1.cod_per=detallehorarioclase.cod_per
			join persona p2 on p2.cod_per=detallehorarioclase.cod_per
	where est_dethorcla=estado and cod_horcla=code_horcla and concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per,' ',horini,' ',horfin) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorarioclase_adicionar(code_per bigint, tem character varying, horaini character varying, code_usu bigint, code_horcla integer, horafin character varying, pri integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_dethorcla),0)+1 from detallehorarioclase where cod_horcla=code_horcla);
	insert into detallehorarioclase(cod_horcla,cod_dethorcla,cod_per,tem_dethorcla,cod_usu,horini,horfin,pri_dethorcla) values(code_horcla,cod,code_per,tem,code_usu,horaini,horafin,pri);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorarioclase_darestado(cod integer, code_dethorcla integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update detallehorarioclase set est_dethorcla=est where cod_horcla=cod and cod_dethorcla=code_dethorcla;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorarioclase_modificar(code_per bigint, tem character varying, horaini character varying, code_usu bigint, code_horcla integer, horafin character varying, code_dethorcla integer, pri integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update detallehorarioclase set (cod_per,tem_dethorcla,cod_usu,horini,horfin,pri_dethorcla)=(code_per,tem,code_usu,horaini,horafin,pri) where cod_dethorcla=code_dethorcla and cod_horcla=code_horcla;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorarioclase_modificar(code_per bigint, tem character varying, horaini character varying, code_usu bigint, code_horcla integer, horafin character varying, code_dethorcla integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update detallehorarioclase set (cod_per,tem_dethorcla,cod_usu,horini,horfin)=(code_per,tem,code_usu,horaini,horafin) where cod_dethorcla=code_dethorcla and cod_horcla=code_horcla;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorarioclase_obtener(cod integer, code_dethorcla integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT d.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) persona,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario
  FROM detallehorarioclase d
	JOIN persona p1 on p1.cod_per=d.cod_per
	JOIN persona p2 on p2.cod_per=d.cod_per
	where cod_horcla=cod and cod_dethorcla=code_dethorcla;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detallehorarioclase_obtener_x_codayu(code_ayu bigint, inicio date, fin date)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
		return query select tem_dethorcla,cast(replace(horini,':','') as varchar(5)) horini,horfin,cast(to_char(fec_horcla,'dd/MM/yyyy') as varchar(10)) fecha,cast(concat(horini,'-',horfin) as varchar(11)) horario,cast(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per) as varchar(150)) persona,pri_dethorcla,
 (DATE_PART('hour', concat(horfin,':00')::time - concat(horini,':00')::time) * 60 +DATE_PART('minute', concat(horfin,':00')::time - concat(horini,':00')::time))/30 lapsos,cast(to_char(fec_horcla,'ddMMyyyy') as varchar(10)) ff,
case when pri_dethorcla=1 then cast('MENSUAL' as varchar(20)) ELSE case when pri_dethorcla=2 then cast('CLASE GRAL.' as varchar(20)) ELSE CASE WHEN pri_dethorcla=3 then cast('PRACTICO' as varchar(20)) ELSE cast('CLASE ONLINE' as varchar(20)) END END END tipo,
case when pri_dethorcla=1 then cast('red' as varchar(20)) ELSE case when pri_dethorcla=2 then cast('orange' as varchar(20)) ELSE CASE WHEN pri_dethorcla=3 then cast('yellow' as varchar(20)) ELSE cast('blue' as varchar(20)) END END END color,d.cod_horcla,d.cod_dethorcla
from detallehorarioclase d
JOIN horarioclase h on h.cod_ayu=code_ayu and h.cod_horcla=d.cod_horcla and h.est_horcla=true and fec_horcla>=inicio and fec_horcla<=fin
join persona p on p.cod_per=d.cod_per
where d.est_dethorcla=true
ORDER BY fec_horcla,horini asc;
END$function$
;

CREATE OR REPLACE FUNCTION public.detalleplan_adicionar(code_matplaayu integer, code_detpla integer, fecha date, hini character varying, hfin character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
		insert into detalleplan(cod_matplaayu,cod_detpla,fec_detpla,hini_detpla,hfin_detpla) values(code_matplaayu,code_detpla,fecha,hini,hfin);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalleplan_adicionar(code_matplaayu character varying, fecha date, ini character varying, fin character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_detpla),0)+1 from detalleplan where cod_matplaayu=code_matplaayu);
	insert into detalleplan(cod_matplaayu,cod_detpla,fec_detpla,hini_detpla,hfin_detpla) values(code_matplaayu,cod,fecha,ini,fin);
		RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalleplan_eliminar(code_matplaayu character varying, code_detpla integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	delete from detalleplan where cod_matplaayu=code_matplaayu and cod_detpla=code_detpla;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalleplan_obtener(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select detalleplan.cod_matplaayu, cod_detpla, fec_detpla, hini_detpla, hfin_detpla,cast(to_char(fec_detpla,'dd/MM/yyyy') as varchar(10)) fecha from detalleplan 
								join mat_pla_ayu on mat_pla_ayu.cod_matplaayu=detalleplan.cod_matplaayu and mat_pla_ayu.cod_pla=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalleplan_obtenerxcodmatplaayu(cod character varying)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select detalleplan.cod_matplaayu, cod_detpla, fec_detpla, hini_detpla, hfin_detpla,cast(to_char(fec_detpla,'dd/MM/yyyy') as varchar(10)) fecha from detalleplan 
								join mat_pla_ayu on mat_pla_ayu.cod_matplaayu=detalleplan.cod_matplaayu and mat_pla_ayu.cod_matplaayu=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalleprestacion_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT d.cod_pre, d.cod_detpre, d.cod_ser, d.pre_detpre, d.cod_tem,cast(concat(ts.nom_tipser,' ',s.nom_ser) as varchar(150)) servicio,t.nom_tem
	FROM detalleprestacion d
	join tema t on t.cod_tem=d.cod_tem
	join servicio s on s.cod_ser=d.cod_ser
	join tiposervicio ts on ts.cod_tipser=s.cod_tipser
	where d.cod_pre=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalleventa_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT d.cod_ven, d.cod_detven, d.cod_pro, d.pre_detven, d.can_detven, d.des_detven,
	d.subtot_detven,d.tot_detven,cast(concat(tp.nom_tippro,' ',p.nom_pro) as varchar(150)) producto
	FROM detalleventa d
	join producto p on p.cod_pro=d.cod_pro
	join tipoproducto tp on tp.cod_tippro=p.cod_tippro
	where d.cod_ven=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.docente_adicionar(code_per bigint, pro character varying, abrpro character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into docente(cod_doc,pro_doc,abrpro_doc) values(code_per,pro,abrpro);
	if not EXISTS(select * from usurol where cod_per=code_per and cod_rol=9 and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(code_per,9);
	end if;		
	RETURN code_per;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.docente_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update docente set est_doc=est where cod_doc=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.docente_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_doc,pro_doc,abrpro_doc,est_doc,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per
			FROM docente
			JOIN persona on persona.cod_per=docente.cod_doc
			where est_doc=estado;
	else
	tot:=(select count(*) from docente where est_doc=estado);
 return query 
	select cod_doc,pro_doc,abrpro_doc,est_doc,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,
		row_number() OVER(ORDER BY docente.cod_doc) as RN,tot as Tot
		from docente
		JOIN persona on persona.cod_per=docente.cod_doc
	where est_doc=estado and upper(nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.docente_modificar(pro character varying, abrpro character varying, cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update docente set (pro_doc,abrpro_doc)=(pro,abrpro) where cod_doc=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.docente_obtener(cod bigint)
 RETURNS SETOF docente
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT *
  FROM docente
			where cod_doc=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.empresa_adicionar(nom character varying, dir character varying, tel character varying, ema character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_emp),0)+1 from empresa);
	insert into empresa(cod_emp,nom_emp,dir_emp,tel_emp,ema_emp) values(cod,upper(nom),dir,tel,ema);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.empresa_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update empresa set est_emp=est where cod_emp=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.empresa_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_emp, nom_emp, dir_emp, tel_emp, ema_emp, est_emp
			FROM empresa
			where est_emp=estado;
	else
	tot:=(select count(*) from empresa where est_emp=estado);
 return query 
	select cod_emp, nom_emp, dir_emp, tel_emp, ema_emp, est_emp,
		row_number() OVER(ORDER BY empresa.cod_emp) as RN,tot as Tot
		from empresa
	where est_emp=estado and upper(nom_emp) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.empresa_modificar(nom character varying, dir character varying, tel character varying, ema character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update empresa set (nom_emp,dir_emp,tel_emp,ema_emp)=(upper(nom),dir,tel,ema) where cod_emp=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.empresa_obtener(cod integer)
 RETURNS SETOF empresa
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT empresa.* FROM empresa where cod_emp=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.empresa_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from empresa where nom_emp=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.est_car_adicionar(code_est bigint, carreras integer[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
	indice int:=1;
	tam int:=array_length(carreras,1);
BEGIN
	delete from est_car where cod_est=code_est;
	while indice<=tam
	loop
		insert into est_car(cod_est,cod_car) values(code_est,carreras[indice]);
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.estudiante_adicionar(code_per bigint, tut character varying, teltut character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into estudiante(cod_est,tut_est,teltut_est) values(code_per,tut,teltut);
	if not EXISTS(select * from usurol where cod_per=code_per and cod_rol=6 and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(code_per,6);
	end if;		
	RETURN code_per;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.estudiante_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update estudiante set est_est=est where cod_est=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.estudiante_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_est, est_est, teltut_est, tut_est,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per
			FROM estudiante
			JOIN persona on persona.cod_per=estudiante.cod_est
			where est_est=estado;
	else
	tot:=(select count(*) from estudiante where est_est=estado);
 return query 
	select cod_est, est_est, teltut_est, tut_est,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,
		row_number() OVER(ORDER BY estudiante.cod_est) as RN,tot as Tot
		from estudiante
		JOIN persona on persona.cod_per=estudiante.cod_est
	where est_est=estado and upper(nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.estudiante_modificar(tut character varying, teltut character varying, cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update estudiante set (tut_est,teltut_est)=(tut,teltut) where cod_est=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.estudiante_obtener(cod bigint)
 RETURNS SETOF estudiante
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT *
  FROM estudiante
			where cod_est=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.facultad_adicionar(nom character varying, sig character varying, fot character varying, cod_ins integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_fac),0)+1 from facultad);
	insert into facultad(cod_fac,nom_fac,sig_fac,fot_fac,cod_ins) values(cod,upper(trim(nom)),sig,fot,$4);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.facultad_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update facultad set est_fac=est where cod_fac=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.facultad_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_fac, nom_fac, sig_fac, fot_fac, est_fac, facultad.cod_ins,nom_ins from facultad 
			join institucion on institucion.cod_ins=facultad.cod_ins
			where est_fac=estado;
	else
	tot:=(select count(*) from facultad where est_fac=estado);
 return query 
	select cod_fac, nom_fac, sig_fac, fot_fac, est_fac, facultad.cod_ins,nom_ins,
		row_number() OVER(ORDER BY facultad.cod_fac) as RN,tot as Tot
		from facultad 
	join institucion on institucion.cod_ins=facultad.cod_ins
	where est_fac=estado and upper(nom_fac) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.facultad_modificar(nom character varying, sig character varying, fot character varying, code_ins integer, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update facultad set (nom_fac,sig_fac,fot_fac,cod_ins)=(upper(trim(nom)),sig,fot,code_ins) where cod_fac=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.facultad_obtener(cod integer)
 RETURNS SETOF facultad
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from facultad where cod_fac=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.facultad_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from facultad where nom_fac=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_adicionar(ges integer, des character varying, nom character varying, logtex character varying, logsintex character varying, tel character varying, dir character varying, lug character varying, nit character varying, suc integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	INSERT INTO general(ges_gen, des_gen, nom_gen, logtex_gen, logsintex_gen, tel_gen, dir_gen, lug_gen,nit_gen, cod_suc)VALUES (ges,des,nom,logtex,logsintex,tel,dir,lug,nit, suc);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RETURN FALSE;
	
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_darestado(cod integer, suc integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update general set est_gen=est where ges_gen=cod and cod_suc = suc;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_lista(inicio integer, limite integer, buscar character varying, est boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select ges_gen, des_gen, est_gen, nom_gen, logtex_gen, logsintex_gen, tel_gen, dir_gen, lug_gen, nit_gen,general.cod_suc,sucursal.nombre as xsucursal from general 
		join sucursal on sucursal.cod_suc=general.cod_suc
		where est_gen=est;
	else
	tot:=(select count(*) from general where est_gen=est);
 return query 
	select ges_gen, des_gen, est_gen, nom_gen, logtex_gen, logsintex_gen, tel_gen, dir_gen, lug_gen, nit_gen,general.cod_suc,sucursal.nombre as xsucursal,
		row_number() OVER(ORDER BY general.ges_gen) as RN,tot as Tot
		from general 
		join sucursal on sucursal.cod_suc = general.cod_suc
	where est_gen=est and upper(des_gen) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_modificar(cod integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
BEGIN
update general set est_gen=false where ges_gen=cod;
IF NOT FOUND THEN
	RETURN 0;
END IF;
RETURN 1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_modificar(des character varying, nom character varying, logtex character varying, logsintex character varying, tel character varying, dir character varying, lug character varying, ges integer, suc integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update general set (des_gen,nom_gen,logtex_gen,logsintex_gen,tel_gen,dir_gen,lug_gen)=(des,nom,logtex,logsintex,tel,dir,lug) where ges_gen=ges and cod_suc = suc;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_obtener(integer, integer)
 RETURNS SETOF general
 LANGUAGE plpgsql
AS $function$
BEGIN
 RETURN QUERY select * from general where ges_gen=$1 and cod_suc=$2;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_obteneractual(integer)
 RETURNS SETOF general
 LANGUAGE plpgsql
AS $function$
BEGIN
 RETURN QUERY select * from general where extract(year from now())=ges_gen and cod_suc = $1 and est_gen=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.general_validar(nom integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from general where ges_gen=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.horarioclase_adicionar(code_ayu bigint, fec date)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_horcla),0)+1 from horarioclase);
	insert into horarioclase(cod_horcla,cod_ayu,fec_horcla) values(cod,code_ayu,fec);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.horarioclase_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update horarioclase set est_horcla=est where cod_horcla=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.horarioclase_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cod_horcla,cod_ayu,fec_horcla,est_horcla from horarioclase where est_horcla=estado;
	else
	tot:=(select count(*) from horarioclase where est_horcla=estado);
 return query 
	select cod_horcla,ayudante.cod_ayu,fec_horcla,est_horcla,cast(to_char(fec_horcla,'dd/MM/yyyy') as varchar(10)) fecha,cast(concat(nom_per,' ',priape_per,' ',segape_per) as varchar(150)) ayudante,
		row_number() OVER(ORDER BY horarioclase.cod_horcla DESC) as RN,tot as Tot
		from horarioclase 
	join ayudante on ayudante.cod_ayu=horarioclase.cod_ayu
	join persona on persona.cod_per=ayudante.cod_per
	where est_horcla=estado and to_char(fec_horcla,'dd/MM/yyyy') like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.horarioclase_modificar(code_ayu bigint, fec date, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update horarioclase set (cod_ayu,fec_horcla)=(code_ayu,fec) where cod_horcla=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.horarioclase_obtener(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select cod_horcla,ayudante.cod_ayu,fec_horcla,est_horcla,cast(to_char(fec_horcla,'dd/MM/yyyy') as varchar(10)) fecha,cast(concat(nom_per,' ',priape_per,' ',segape_per) as varchar(150)) ayudante
		from horarioclase 
	join ayudante on ayudante.cod_ayu=horarioclase.cod_ayu
	join persona on persona.cod_per=ayudante.cod_per
	where cod_horcla=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.horarioclase_validar(cod bigint, fecha date)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from horarioclase where cod_ayu=cod and fec_horcla=fecha) THEN
	RETURN FALSE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_adicionar(ges integer, fec date, code_est bigint, code_pla integer, code_per bigint, tot double precision, sal double precision)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
code_arqcaj bigint;
code_detarq int;
BEGIN
	cod:=(select coalesce(max(cod_ins),0)+1 from inscripcion);
	code_arqcaj:=(select arqueocaja_obtener_sesion(code_per));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,6, concat('Por ingreso de inscripcion, cod. #',cod,' total=',tot,' saldo a favor=',sal),sal ));
	insert into inscripcion(cod_ins, ges_gen, fec_ins, cod_est, cod_pla, cod_per, tot_ins, sal_ins,cod_arqcaj,cod_detarq) 
											values(cod,ges,fec,code_est,code_pla,code_per,tot,sal,code_arqcaj,code_detarq);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$DECLARE code_arqcaj int;
DECLARE code_detarq int;
BEGIN
	if(est=false)THEN
		code_arqcaj=(select cod_arqcaj from inscripcion where cod_ins=cod);
		code_detarq=(select cod_detarq from inscripcion where cod_ins=cod);
		update detallearqueo set est_detarq=false where cod_arqcaj=code_arqcaj and cod_detarq=code_detarq;
	end if;
	update inscripcion set est_ins=est where cod_ins=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_lista(inicio integer, limite integer, buscar character varying, estado boolean, gestion integer, fini date, ffin date, cod_user bigint, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select i.cod_ins,i.ges_gen,i.fec_ins,i.cod_est,i.cod_pla,i.est_ins,i.cod_per,i.tot_ins,i.sal_ins,i.cod_suc,p.nom_pla,p.sig_pla,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) estudiante,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario,cast(to_char(fec_ins,'dd/MM/yyyy') as varchar(15)) fecha
			from inscripcion i
			join plan p on p.cod_pla=i.cod_pla
			join estudiante e on e.cod_est=i.cod_est
			join persona p1 on p1.cod_per=e.cod_est
			join persona p2 on p2.cod_per=i.cod_per
			where est_ins=estado and (i.cod_per=cod_user or cod_user=-1) and i.ges_gen=gestion and i.cod_suc = suc and fec_ins>=fini and fec_ins<=ffin;
	else
	tot:=(select count(*) from inscripcion where est_ins=estado);
 return query 
	select i.cod_ins,i.ges_gen,i.fec_ins,i.cod_est,i.cod_pla,i.est_ins,i.cod_per,i.tot_ins,i.sal_ins,i.cod_suc,p.nom_pla,p.sig_pla,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) estudiante,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario,cast(to_char(fec_ins,'dd/MM/yyyy') as varchar(15)) fecha,
		row_number() OVER(ORDER BY i.cod_ins) as RN,tot as Tot
		from inscripcion i
			join plan p on p.cod_pla=i.cod_pla
			join estudiante e on e.cod_est=i.cod_est
			join persona p1 on p1.cod_per=e.cod_est
			join persona p2 on p2.cod_per=i.cod_per 
	where i.est_ins=estado and (i.cod_per=cod_user or cod_user=-1)  and i.ges_gen=gestion and i.cod_suc = suc and fec_ins>=fini and fec_ins<=ffin and cast(cod_ins as varchar) like concat('%',buscar,'%') LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_modificar(ges integer, fec date, code_est bigint, code_pla integer, code_per bigint, tot double precision, sal double precision, code_ins bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update inscripcion set (ges_gen, fec_ins, cod_est, cod_pla, cod_per, tot_ins, sal_ins, tip_ins) =(ges,fec,code_est,code_pla,code_per,tot,sal,tip) where cod_ins=code_ins;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
	select cod_ins, inscripcion.ges_gen, fec_ins, inscripcion.cod_est, inscripcion.cod_pla, est_ins, inscripcion.cod_per, tot_ins, sal_ins,inscripcion.cod_suc,cast(to_char(fec_ins,'dd/MM/yyyy') as varchar(15)) fecha,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) estudiante,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario,nom_pla
	from inscripcion
	join estudiante on estudiante.cod_est=inscripcion.cod_est
	join persona p1 on p1.cod_per=estudiante.cod_est
	join persona p2 on p2.cod_per=inscripcion.cod_per
	join plan on plan.cod_pla=inscripcion.cod_pla
	where cod_ins=cod;
END$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_obtener_por_plan(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
	select cod_ins, inscripcion.ges_gen, fec_ins, inscripcion.cod_est, inscripcion.cod_pla, est_ins, inscripcion.cod_per, tot_ins, sal_ins,cod_suc,cast(to_char(fec_ins,'dd/MM/yyyy') as varchar(15)) fecha,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(150)) estudiante,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(150)) usuario,nom_pla
	from inscripcion
	join estudiante on estudiante.cod_est=inscripcion.cod_est
	join persona p1 on p1.cod_per=estudiante.cod_est
	join persona p2 on p2.cod_per=inscripcion.cod_per
	join plan on plan.cod_pla=inscripcion.cod_pla
	where inscripcion.cod_pla=cod and est_ins=true;
END$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_pagar(cod bigint, pag double precision, cod_usuario bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$DECLARE code_arqcaj int;
DECLARE code_detarq int;
BEGIN
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,6,concat('Pago de inscripcion faltante. Cod. #',cast(cod as varchar(15))),pag));
	update inscripcion set sal_ins=sal_ins+pag where cod_ins=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.inscripcion_pago_lista(inicio integer, limite integer, buscar character varying, estado boolean, tipo integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select DISTINCT p.cod_pla,p.nom_pla,p.sig_pla 
			from plan p
			join inscripcion i on i.cod_pla=p.cod_pla and i.est_ins=true
			join mat_pla_ayu mpa on mpa.cod_pla=p.cod_pla and mpa.est_pag=1
			where p.est_pla=estado and p.tra_pla=tipo;
	else
	tot:=(select count(*) from plan p where p.est_pla=estado and p.tra_pla=tipo and p.cod_pla in (select distinct i.cod_pla from inscripcion i where i.est_ins=true) );
 return query 
	select DISTINCT p.cod_pla,p.nom_pla,p.sig_pla ,
		row_number() OVER(ORDER BY p.cod_pla) as RN,tot as Tot
		from plan p
			join inscripcion i on i.cod_pla=p.cod_pla and i.est_ins=true
			join mat_pla_ayu mpa on mpa.cod_pla=p.cod_pla and mpa.est_pag=1
			where p.est_pla=estado and p.tra_pla=tipo and upper(p.nom_pla) like upper(concat('%',buscar,'%')) 
			group by p.cod_pla,p.nom_pla,p.sig_pla
			LIMIT limite OFFSET inicio;
	end if;
END$function$
;

CREATE OR REPLACE FUNCTION public.institucion_adicionar(nom character varying, sig character varying, fot character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_ins),0)+1 from institucion);
	insert into institucion(cod_ins,nom_ins,sig_ins,fot_ins) values(cod,upper(trim(nom)),upper(trim(sig)),fot);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.institucion_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update institucion set est_ins=est where cod_ins=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.institucion_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cod_ins, nom_ins, sig_ins, fot_ins, est_ins from institucion where est_ins=estado;
	else
	tot:=(select count(*) from institucion where est_ins=estado);
 return query 
	select cod_ins, nom_ins, sig_ins, fot_ins, est_ins,
		row_number() OVER(ORDER BY institucion.cod_ins) as RN,tot as Tot
		from institucion 
	where est_ins=estado and upper(nom_ins) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.institucion_modificar(nom character varying, sig character varying, fot character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update institucion set (nom_ins,sig_ins,fot_ins)=(upper(trim(nom)),upper(trim(sig)),fot) where cod_ins=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.institucion_obtener(cod integer)
 RETURNS SETOF institucion
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from institucion where cod_ins=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.institucion_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from institucion where nom_ins=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.librodiario_lista(inicio integer, limite integer, buscar character varying, xestado boolean, xsuc integer, gestion integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	tot:=(select count(*) from asiento_contable where estado =xestado and cod_suc=xsuc and ges_gen=gestion); 
	return query 
 SELECT cod_asiento, numero, fecha, concepto, estado, created_by, created_at, ges_gen, cod_suc,
		row_number() OVER(ORDER BY numero desc) as RN,tot as tot,cast(to_char(fecha,'dd/MM/yyyy') as varchar(10)) xfecha
		from asiento_contable
	where estado =xestado and ges_gen=gestion and cod_suc = xsuc and cast(numero as text) like concat('%',buscar,'%') LIMIT limite OFFSET inicio;
END
$function$
;

CREATE OR REPLACE FUNCTION public.mat_pla_ayu_eliminar(cod character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	delete from detalleplan where cod_matplaayu=cod;
	delete from mat_pla_ayu where cod_matplaayu=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.mat_pla_ayu_obtener(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select cod_pla, mat_pla_ayu.cod_mat, ayudante.cod_ayu, cod_matplaayu,nom_car,nom_mat,p.nom_per,p.priape_per,p.segape_per
								from mat_pla_ayu
								join materia on materia.cod_mat=mat_pla_ayu.cod_mat
								join carrera on carrera.cod_car=materia.cod_car
								join ayudante on ayudante.cod_ayu=mat_pla_ayu.cod_ayu
								join persona p on p.cod_per=ayudante.cod_ayu
								where mat_pla_ayu.cod_pla=cod;
								
END
$function$
;

CREATE OR REPLACE FUNCTION public.materia_adicionar(nom character varying, sig character varying, code_car integer, code_tipmat integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_mat),0)+1 from materia);
	insert into materia(cod_mat,nom_mat,sig_mat,cod_car,cod_tipmat) values(cod,upper(trim(nom)),upper(trim(sig)),code_car,code_tipmat);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.materia_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update materia set est_mat=est where cod_mat=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.materia_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_mat, nom_mat, sig_mat, est_mat, materia.cod_car, materia.cod_tipmat,nom_car,nom_tipmat from materia 
			join tipomateria on tipomateria.cod_tipmat=materia.cod_tipmat
			join carrera on carrera.cod_car=materia.cod_car
			where est_mat=estado;
	else
	tot:=(select count(*) from materia where est_mat=estado);
 return query 
	select cod_mat, nom_mat, sig_mat, est_mat, materia.cod_car, materia.cod_tipmat,nom_car,nom_tipmat,
		row_number() OVER(ORDER BY materia.cod_mat) as RN,tot as Tot
		from materia 
	join tipomateria on tipomateria.cod_tipmat=materia.cod_tipmat
	join carrera on carrera.cod_car=materia.cod_car
	where est_mat=estado and upper(nom_mat) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.materia_modificar(nom character varying, sig character varying, code_car integer, code_tipmat integer, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update materia set (nom_mat,sig_mat,cod_car,cod_tipmat)=(upper(trim(nom)),upper(trim(sig)),code_car,code_tipmat) where cod_mat=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.materia_obtener(cod integer)
 RETURNS SETOF materia
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from materia where cod_mat=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.materia_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from materia where nom_mat=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_adicionar(nom character varying, des character varying, ico character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_men),0)+1 from menu);
	insert into menu(cod_men,nom_men,des_men,ico_men) values(cod,upper(trim(nom)),des,ico);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
	
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update menu set est_men=est where cod_men=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
DECLARE r RECORD;
BEGIN
	if(inicio<0)THEN
		return query select cod_men,nom_men,des_men,ico_men,est_men from menu where est_men=estado;
	else
	tot:=(select count(*) from menu where est_men=estado);
 return query 
	select cod_men,nom_men,des_men,ico_men,est_men,
		row_number() OVER(ORDER BY menu.cod_men) as RN,tot as Tot,(select cast(count(*)  as int)from mepro where mepro.cod_men=menu.cod_men) procesos
		from menu 
	where est_men=estado and upper(nom_men) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_modificar(nom character varying, des character varying, ico character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update menu set (nom_men,des_men,ico_men)=(upper(trim(nom)),des,ico) where cod_men=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_obtener(cod integer)
 RETURNS SETOF menu
 LANGUAGE plpgsql
AS $function$
BEGIN
 RETURN QUERY select * from menu where cod_men=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_obtener_primer_rol_usuario(cod bigint)
 RETURNS SETOF menu
 LANGUAGE plpgsql
AS $function$declare code_rol int4;
BEGIN
	code_rol:=(select min(r.cod_rol) from rol r join usurol ru on ru.cod_rol=r.cod_rol and ru.cod_per=cod and ru.fecha_baja is null where r.est_rol=true);
	RETURN QUERY 
		select DISTINCT m.* 
		from menu m 
		join rolmen rm on rm.cod_men=m.cod_men 
		join rol r on rm.cod_rol=r.cod_rol and r.est_rol=true and r.cod_rol=code_rol
		where m.est_men=true order by cod_men;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_obtener_x_codrol(code_rol integer)
 RETURNS SETOF menu
 LANGUAGE plpgsql
AS $function$BEGIN
	RETURN QUERY 
		select DISTINCT m.* 
		from menu m 
		join rolmen rm on rm.cod_men=m.cod_men 
		join rol r on rm.cod_rol=r.cod_rol and r.est_rol=true and r.cod_rol=code_rol
		where m.est_men=true order by cod_men;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_obtenerprocesos(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select proceso.cod_pro,nom_pro,des_pro,ico_pro,est_pro,url_pro,case when cod_men is null then cast('' as varchar(8)) else cast('selected' as varchar(8)) end as tipo 
		from proceso 
		left join mepro on mepro.cod_pro=proceso.cod_pro and mepro.cod_men=cod 
		where est_pro=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_obtenerxusuario(cod bigint)
 RETURNS SETOF menu
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select DISTINCT m.* 
		from menu m 
		join rolmen rm on rm.cod_men=m.cod_men 
		join rol r on rm.cod_rol=r.cod_rol and r.est_rol=true 
		join usurol ru on ru.cod_rol=r.cod_rol 
		join persona pe on pe.cod_per=cod and pe.cod_per=ru.cod_per and pe.est_per=true 
		where m.est_men=true order by cod_men;
END
$function$
;

CREATE OR REPLACE FUNCTION public.menu_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from menu where nom_men=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.mepro_adicionar(codm integer, codp integer[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE r int;
BEGIN
	delete from mepro where cod_men=codm;
	for r in select * from codp
	loop
		insert into mepro(cod_men,cod_pro) values(codm,r);
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.pago_ayudante_lista(inicio integer, limite integer, buscar character varying, estado boolean, tipo bigint, ges integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select DISTINCT a.cod_ayu,cast(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per) as varchar(100)) ayudante
			from ayudante a
			join persona p on p.cod_per=a.cod_ayu
			join prestacion pr on pr.cod_ayu=a.cod_ayu and pr.tip_pre=tipo and pr.tot_pre=pr.sal_pre and pr.est_pre=true and pr.ges_gen = ges and pr_cod_suc = suc
			where a.est_ayu=estado;
	else
	tot:=(select count(*) from ayudante a where a.est_ayu=estado and a.cod_ayu in(select pr.cod_ayu from prestacion pr 
	where pr.cod_ayu=a.cod_ayu and pr.tip_pre=tipo and pr.tot_pre=pr.sal_pre and pr.est_pre=estado and pr.ges_gen = ges and pr.cod_suc = suc));
 return query 
	select DISTINCT a.cod_ayu,cast(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per) as varchar(100)) ayudante,
		row_number() OVER(ORDER BY a.cod_ayu) as RN,tot as Tot
		from ayudante a
			join persona p on p.cod_per=a.cod_ayu
			join prestacion pr on pr.cod_ayu=a.cod_ayu and pr.tip_pre=tipo and pr.tot_pre=pr.sal_pre and pr.est_pre=estado and pr.ges_gen = ges and pr.cod_suc = suc
	where est_ayu=estado and upper(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per)) like upper(concat('%',buscar,'%')) group by a.cod_ayu,p.nom_per,p.priape_per,p.segape_per LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.pago_ayudante_lista(inicio integer, limite integer, buscar character varying, estado boolean, tipo bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select DISTINCT a.cod_ayu,cast(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per) as varchar(100)) ayudante
			from ayudante a
			join persona p on p.cod_per=a.cod_ayu
			join prestacion pr on pr.cod_ayu=a.cod_ayu and pr.tip_pre=tipo and pr.tot_pre=pr.sal_pre and pr.est_pre=true
			where a.est_ayu=estado;
	else
	tot:=(select count(*) from ayudante a where a.est_ayu=estado and a.cod_ayu in(select pr.cod_ayu from prestacion pr where pr.cod_ayu=a.cod_ayu and pr.tip_pre=tipo and pr.tot_pre=pr.sal_pre and pr.est_pre=estado));
 return query 
	select DISTINCT a.cod_ayu,cast(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per) as varchar(100)) ayudante,
		row_number() OVER(ORDER BY a.cod_ayu) as RN,tot as Tot
		from ayudante a
			join persona p on p.cod_per=a.cod_ayu
			join prestacion pr on pr.cod_ayu=a.cod_ayu and pr.tip_pre=tipo and pr.tot_pre=pr.sal_pre and pr.est_pre=estado
	where est_ayu=estado and upper(concat(p.nom_per,' ',p.priape_per,' ',p.segape_per)) like upper(concat('%',buscar,'%')) group by a.cod_ayu,p.nom_per,p.priape_per,p.segape_per LIMIT limite OFFSET inicio;

	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_activar(cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update persona set est_per=true,fot_per='user.png' where cod_per=cod;
	IF NOT FOUND THEN
		return FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_adicionar(ci character varying, nom character varying, priape character varying, segape character varying, tel character varying, dir character varying, ema character varying, fot character varying, sex boolean)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
cod_escanner varchar(25);
BEGIN
	cod:=(select coalesce(max(cod_per),0)+1 from persona);
	cod_escanner:=concat(cod,'-',to_char(now(),'yyyyMMdd'));
	INSERT INTO persona(cod_per,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, sex_per,codesc_per)
    VALUES (cod, ci, trim(upper(nom)), trim(upper(priape)), trim(upper(coalesce(segape,''))), tel, dir, ema, fot, sex,cod_escanner);
		RETURN cod;
END$function$
;

CREATE OR REPLACE FUNCTION public.persona_buscar(ci character varying)
 RETURNS SETOF persona
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query select * from persona where ci_per=ci;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_buscar_nombres(cad character varying)
 RETURNS SETOF persona
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query select * from persona where upper(concat(nom_per,' ',priape_per,' ',segape_per))=upper(cad);
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_eliminar(cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update persona set est_per=false where cod_per=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_guardarfoto(foto character varying, cod bigint)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	update persona set fot_per=foto where cod_per=cod;
	IF NOT FOUND THEN
	RETURN 0;
	END IF;
	RETURN 1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_iniciar_sesion(log character varying, cla character varying)
 RETURNS SETOF persona
 LANGUAGE plpgsql
AS $function$
DECLARE r persona;
BEGIN
 for r in select p.* from persona p join dato d on p.cod_per=d.cod_per and d.log_dat=log and d.cla_dat=cla and d.fecha_baja is null where p.est_per=true
 LOOP
  return next r;
 END LOOP;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per, cod_per,codbio_per,codesc_per from persona where est_per=estado;
	else
	tot:=(select count(*) from persona where est_per=estado);
 return query 
	select ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per, persona.cod_per,codbio_per,codesc_per,
		row_number() OVER(ORDER BY persona.cod_per desc) as RN,tot as Tot,log_dat 
		from persona 
		left join dato on dato.cod_per=persona.cod_per and fecha_baja is null 
	where est_per=estado and UPPER(nom_per||priape_per||segape_per) like concat('%',upper(buscar),'%') LIMIT limite OFFSET inicio;
	end if;
END

$function$
;

CREATE OR REPLACE FUNCTION public.persona_modificar(ci character varying, nombre character varying, ap character varying, am character varying, dir character varying, telf character varying, email character varying, foto character varying, genero boolean, cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update persona set (ci_per,nom_per,priape_per,segape_per,dir_per,tel_per,ema_per,fot_per,sex_per)=(ci,trim(upper(nombre)),trim(upper(ap)),trim(upper(coalesce(am,''))),dir,telf,email,foto,genero) where cod_per=cod;
IF NOT FOUND THEN
RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_obtener(cod bigint)
 RETURNS SETOF persona
 LANGUAGE plpgsql
AS $function$
BEGIN
 RETURN QUERY select * from persona where cod_per=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_obtener_sucursales(cod_per bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select sucursal.cod_suc, nombre, descripcion, estado,case when tiene_sucursal.cod_per is null then cast('' as varchar(15)) else cast('selected' as varchar(15)) end as tipo 
		from sucursal
		left join tiene_sucursal on tiene_sucursal.cod_suc=sucursal.cod_suc and tiene_sucursal.cod_per=$1 where estado;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_validarci(ci character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS(select * from persona where ci_per=ci)
	THEN
		RETURN TRUE;
	ELSE
		RETURN FALSE;
	END IF;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_adicionar(nom character varying, sig character varying, fini date, ffin date, hor integer, mon double precision, tip integer, des double precision, obs character varying, ges integer, ayudantes bigint[], materias integer[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(materias,1);
code_matplaayu varchar(24);
BEGIN
	cod:=(select coalesce(max(cod_pla),0)+1 from plan);
	insert into plan(cod_pla,nom_pla,sig_pla,fini_pla,ffin_pla,hor_pla,mon_pla,tip_pla,des_pla,obs_pla,ges_gen) values(cod,upper(trim(nom)),upper(trim(sig)),fini,ffin,hor,mon,tip,des,obs,ges);
	while indice<=tam
	loop
		code_matplaayu=concat(cast(materias[indice] as varchar),'-',cast(cod as varchar),'-',cast(ayudantes[indice] as varchar));
		delete from detalleplan where cod_matplaayu=code_matplaayu;
		insert into mat_pla_ayu(cod_matplaayu,cod_mat,cod_pla,cod_ayu) values(code_matplaayu,materias[indice],cod,ayudantes[indice]);
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update plan set est_pla=est where cod_pla=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_finalizar(cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update plan set tra_pla=2 where cod_pla=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_lista(inicio integer, limite integer, buscar character varying, estado boolean, gestion integer, tipo integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select plan.*,cast(to_char(fini_pla, 'dd/MM/yyyy') as varchar(10)) fechai,cast(to_char(ffin_pla, 'dd/MM/yyyy') as varchar(10)) fechaf 
			from plan 
			where est_pla=estado and (tra_pla=tipo or tipo=-1);
	else
	tot:=(select count(*) from plan where est_pla=estado and (tra_pla=tipo or tipo=-1));
 return query 
	select plan.*,cast(to_char(fini_pla, 'dd/MM/yyyy') as varchar(10)) fechai,cast(to_char(ffin_pla, 'dd/MM/yyyy') as varchar(10)) fechaf ,
		row_number() OVER(ORDER BY plan.cod_pla) as RN,tot as Tot
		from plan 
	where est_pla=estado and (tra_pla=tipo or tipo=-1) and upper(nom_pla) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_lista_x_ayudante(inicio integer, limite integer, buscar character varying, estado boolean, tipo integer, gestio integer, code_ayu bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select DISTINCT p.cod_pla,p.nom_pla,p.sig_pla 
			from plan p
			join inscripcion i on i.cod_pla=p.cod_pla and i.est_ins=true
			join mat_pla_ayu mpa on mpa.cod_pla=p.cod_pla and mpa.est_pag=tipo and mpa.cod_ayu=code_ayu
			where p.est_pla=estado and ges_gen=gestion;
	else
	tot:=(select count(*) from plan p where p.est_pla=estado and p.tra_pla=tipo and p.cod_pla in (select distinct i.cod_pla from inscripcion i where i.est_ins=true) );
 return query 
	select DISTINCT p.cod_pla,p.nom_pla,p.sig_pla ,
		row_number() OVER(ORDER BY p.cod_pla) as RN,tot as Tot
		from plan p
			join inscripcion i on i.cod_pla=p.cod_pla and i.est_ins=true
			join mat_pla_ayu mpa on mpa.cod_pla=p.cod_pla and mpa.est_pag=tipo and mpa.cod_ayu=code_ayu
			where p.est_pla=estado and upper(p.nom_pla) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_modificar(nom character varying, sig character varying, fini date, ffin date, hor integer, mon double precision, tip integer, des double precision, obs character varying, ges integer, ayudantes integer[], materias bigint[], code_pla integer)
 RETURNS "char"
 LANGUAGE plpgsql
AS $function$
DECLARE indice int:=1;
tam int:=array_length(materias,1);
code_matplaayu varchar(24);
BEGIN
	 update plan set (nom_pla,sig_pla,fini_pla,ffin_pla,hor_pla,mon_pla,tip_pla,des_pla,obs_pla,ges_gen)=(upper(trim(nom)),upper(trim(sig)),fini,ffin,hor,mon,tip,des,obs,ges) where cod_pla=code_pla;
	while indice<=tam
	loop
		if not exists(select * from mat_pla_ayu where cod_mat=materias[indice] and cod_ayu=ayudantes[indice] and cod_pla=code_pla)then
		code_matplaayu=concat(cast(materias[indice] as varchar),'-',cast(code_pla as varchar),'-',cast(ayudantes[indice] as varchar));
		insert into mat_pla_ayu(cod_matplaayu,cod_mat,cod_pla,cod_ayu) values(code_matplaayu,materias[indice],code_pla,ayudantes[indice]);
		end if;
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_obtener(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN query select cod_pla, nom_pla, sig_pla, fini_pla, ffin_pla, hor_pla, mon_pla, tip_pla, des_pla, est_pla, obs_pla, ges_gen,tra_pla,cod_suc,cast(to_char(fini_pla, 'dd/MM/yyyy') as varchar(10)) fechai,cast(to_char(ffin_pla, 'dd/MM/yyyy') as varchar(10)) fechaf
			from plan
			where cod_pla=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.plan_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from plan where nom_pla=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_adicionar(code_usu bigint, code_est bigint, obs character varying, tot double precision, des double precision, gestion integer, code_ayu bigint, tip integer, fecent date, horent character varying, code_doc bigint, sal double precision, suc integer, servicios integer[], precios double precision[], temas integer[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(servicios,1);
code_arqcaj bigint;
code_detarq int;
BEGIN
	cod:=(select coalesce(max(cod_pre),0)+1 from prestacion);
	code_arqcaj:=(select arqueocaja_obtener_sesion(code_usu));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,7,concat('Por ingreso de prestacion de servicio. Cod. #',cod,'.'),sal));
	insert into prestacion(cod_pre,cod_per,cod_est,fec_pre,obs_pre,tot_pre,des_pre,ges_gen,cod_ayu,tip_pre,fecent_pre,horent_pre,cod_doc,sal_pre,cod_arqcaj,cod_detarq,cod_suc) 
									values(cod,code_usu,code_est,now(),obs,tot,des,gestion,code_ayu,1,fecent,horent,code_doc,sal,code_arqcaj,code_detarq,suc);
	while indice<=tam
	loop
		insert into detalleprestacion(cod_pre,cod_detpre,cod_ser,pre_detpre,cod_tem) values(cod,indice,servicios[indice],precios[indice],temas[indice]);
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_cambiar(cod bigint, code_ayu bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$BEGIN
		update prestacion set cod_ayu=code_ayu where cod_pre=cod;
		RETURN TRUE;
END$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$DECLARE code_arqcaj FLOAT;
code_detarq FLOAT;
BEGIN
		if(est=false)THEN
			code_arqcaj=(select cod_arqcaj from prestacion where cod_pre=cod);
			code_detarq=(select cod_detarq from prestacion where cod_pre=cod);
			update detallearqueo set est_detarq=est where cod_arqcaj=code_arqcaj and cod_detarq=code_detarq;
		end if;
		update prestacion set est_pre=est where cod_pre=cod;
		RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_lista(inicio integer, limite integer, buscar character varying, estado boolean, code_ayu bigint, fini date, ffin date, gestion integer, tipo integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select prestacion.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
			from prestacion
			join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
			where est_pre=estado and cod_suc = suc and (prestacion.cod_per=code_ayu or code_ayu=-1) and tip_pre=tipo and ges_gen=gestion and fec_pre>=fini and fec_pre<=ffin;
	else
	tot:=(select count(*) from prestacion 
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and (prestacion.cod_per=code_ayu or code_ayu=-1) and (tip_pre=tipo or tipo=-1) and ges_gen=gestion and cast(fec_pre as date)>=fini and cast(fec_pre as date)<=ffin 
	and (upper(cast(cod_pre as varchar)) like upper(concat('%',upper(buscar),'%')) or upper(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per)) like upper(concat('%',upper(buscar),'%')) 
		or upper(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per)) like upper(concat('%',upper(buscar),'%')))); 
	return query 
	select prestacion.*,
		row_number() OVER(ORDER BY prestacion.fec_pre desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and (prestacion.cod_per=code_ayu or code_ayu=-1) and (tip_pre=tipo or tipo=-1) and ges_gen=gestion and cast(fec_pre as date)>=fini and cast(fec_pre as date)<=ffin 
	and (upper(cast(cod_pre as varchar)) like upper(concat('%',upper(buscar),'%')) or upper(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per)) like upper(concat('%',upper(buscar),'%')) 
		or upper(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per)) like upper(concat('%',upper(buscar),'%'))) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_lista_x_ayudante(inicio integer, limite integer, buscar character varying, estado boolean, code_ayu bigint, gestion integer, tipo integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
			from prestacion
			join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
			where est_pre=estado and prestacion.cod_ayu=code_ayu and tip_pre=tipo and ges_gen=gestion and cod_suc = suc;
	else
	tot:=(select count(*) from prestacion join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%'))); 
	return query 
	select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,
		row_number() OVER(ORDER BY prestacion.fec_pre desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_lista_x_ayudante(inicio integer, limite integer, buscar character varying, estado boolean, code_ayu bigint, gestion integer, tipo integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
			from prestacion
			join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
			where est_pre=estado and prestacion.cod_ayu=code_ayu and tip_pre=tipo and ges_gen=gestion;
	else
	tot:=(select count(*) from prestacion where est_pre=estado and cod_ayu=code_ayu and tip_pre=tipo and ges_gen=gestion); 
	return query 
	select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,
		row_number() OVER(ORDER BY prestacion.fec_pre desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN query select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,cod_suc,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
			from prestacion
			join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
			where cod_pre=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_pagar(cod bigint, saldo double precision, cod_usuario bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$DECLARE code_arqcaj int;
DECLARE code_detarq int;
BEGIN
		code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
		code_detarq:=(select detallearqueo_adicionar(code_arqcaj,7,concat('Pago de prestacion faltante. Cod. #',cast(cod as varchar(15))),saldo));
		update prestacion set sal_pre=sal_pre+saldo where cod_pre=cod;
		RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_revisar(cod bigint, tipo integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
		update prestacion set tip_pre=tipo where cod_pre=cod;
		RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_adicionar(nom character varying, des character varying, ico character varying, url character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_pro),0)+1 from proceso);
	insert into proceso(cod_pro,nom_pro,des_pro,ico_pro,url_pro) values(cod,upper(trim(nom)),des,ico,url);
		RETURN TRUE;
		EXCEPTION
			WHEN OTHERS THEN
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update proceso set est_pro=est where cod_pro=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
DECLARE r RECORD;
BEGIN
	if(inicio<0)THEN
		return query select cod_pro,nom_pro,des_pro,ico_pro,est_pro,url_pro from proceso where est_pro=estado;
	else
	tot:=(select count(*) from proceso where est_pro=estado);
 return query 
	select cod_pro,nom_pro,des_pro,ico_pro,est_pro,url_pro,
		row_number() OVER(ORDER BY proceso.cod_pro) as RN,tot as Tot
		from proceso 
	where est_pro=estado and upper(nom_pro) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_modificar(nom character varying, des character varying, ico character varying, url character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update proceso set (nom_pro,des_pro,ico_pro,url_pro)=(upper(trim(nom)),des,ico,url) where cod_pro=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_obtener(cod integer)
 RETURNS SETOF proceso
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select proceso.*
		from proceso 
		where cod_pro=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_obtenerxcodm(codm integer)
 RETURNS SETOF proceso
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select p.* from proceso p 
		join mepro me on me.cod_pro=p.cod_pro and me.cod_men=codm 
		where p.est_pro=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proceso_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from proceso where nom_pro=nom) THEN
		RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_adicionar(nom character varying, can integer, cod_tippro integer, precom double precision, preven double precision, gan double precision)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_pro),0)+1 from producto);
	insert into producto(cod_pro,nom_pro,can_pro,cod_tippro,precom_pro,preven_pro,gan_pro) values(cod,upper(trim(nom)),can,cod_tippro,precom,preven,gan);
	insert into codigoproducto(cod_pro,cod_bar) values(cod,concat(to_char(now(),'ddMMyyyy'),'-',cod_tippro,'-',cod));
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN 0;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_asignar(cod integer, codigos character varying[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
	indice int:=1;
	tam int:=array_length(codigos,1);
BEGIN
	delete from codigoproducto where cod_pro=cod;
	while indice<=tam
	loop
		if(codigos[indice]!='')then
		insert into codigoproducto(cod_pro,cod_bar) values(cod,codigos[indice]);
		end IF; 
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_aumentar(cod integer, can integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update producto set can_pro=can_pro+can where cod_pro=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update producto set est_pro=est where cod_pro=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_disminuir(cod integer, can integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update producto set can_pro=can_pro-can where cod_pro=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select tipoproducto.nom_tippro,cod_pro,nom_pro,precom_pro,preven_pro,gan_pro,can_pro,est_pro,producto.cod_tippro 
			from producto 
			join tipoproducto on tipoproducto.cod_tippro=producto.cod_tippro
			where est_pro=estado;
	else
	tot:=(select count(*) from producto where est_pro=estado);
 return query 
	select tipoproducto.nom_tippro, cod_pro,nom_pro,precom_pro,preven_pro,gan_pro,can_pro,est_pro,producto.cod_tippro,
		row_number() OVER(ORDER BY producto.cod_pro desc) as RN,tot as Tot
		from producto
		join tipoproducto on tipoproducto.cod_tippro=producto.cod_tippro
	where est_pro=estado and upper(nom_pro) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_modificar(nom character varying, can integer, cod_tippro integer, precom double precision, preven double precision, gan double precision, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update producto set (nom_pro,can_pro,cod_tippro,precom_pro,preven_pro,gan_pro)=(upper(trim(nom)),can,$3,precom,preven,gan) where cod_pro=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_obtener(cod integer)
 RETURNS SETOF producto
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select producto.* from producto where cod_pro=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_obtenerxcodigo(barcod character varying)
 RETURNS SETOF producto
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select producto.* from producto
		join codigoproducto on codigoproducto.cod_pro=producto.cod_pro and cod_bar=barcod
		where est_pro=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.producto_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from producto join tipoproducto on tipoproducto.cod_tippro=producto.cod_tippro where upper(nom_tippro||' '||nom_pro)=upper(nom)) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proveedor_adicionar(codigo bigint, codemp integer)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into proveedor(cod_pro,cod_emp) values(codigo,codemp);
	if not EXISTS(select * from usurol where cod_per=codigo and cod_rol=8 and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(codigo,8);
	end if;		
	RETURN codigo;
EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proveedor_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update proveedor set est_pro=est where cod_pro=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proveedor_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_pro, proveedor.cod_emp,est_pro,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,nom_emp
			FROM proveedor
			JOIN persona on persona.cod_per=proveedor.cod_pro
			JOIN empresa on empresa.cod_emp=proveedor.cod_emp
			where est_pro=estado;
	else
	tot:=(select count(*) from proveedor where est_pro=estado);
 return query 
	select proveedor.cod_pro, proveedor.cod_emp,est_pro,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,nom_emp,
		row_number() OVER(ORDER BY proveedor.cod_pro) as RN,tot as Tot
		from proveedor
		JOIN persona on persona.cod_per=proveedor.cod_pro
		JOIN empresa on empresa.cod_emp=proveedor.cod_emp
	where est_pro=estado and upper(nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proveedor_modificar(codemp integer, codpro bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update proveedor set cod_emp=codemp where cod_pro=codpro;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.proveedor_obtener(cod bigint)
 RETURNS SETOF proveedor
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT cod_pro, cod_emp, est_pro
  FROM proveedor
			where cod_pro=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.remuneracion_adicionar(code_per bigint, code_sec bigint, fini date, ffin date, mes integer, ges integer, des character varying, mon double precision, p_ges integer, suc integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod bigint;
BEGIN
	cod:=(select coalesce(max(cod_rem),0)+1 from remuneracion);
	INSERT INTO remuneracion(cod_per,cod_sec,fec_rem,fini_rem,ffin_rem,mes_rem,ges_rem,des_rem,cod_rem,mon_rem,cod_gen,cod_suc)
VALUES (code_per,code_sec,now(),fini,ffin,mes,ges,des,cod,mon,p_ges,suc);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.remuneracion_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update remuneracion set est_rem=est where cod_rem=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.remuneracion_lista(inicio integer, limite integer, buscar character varying, estado boolean, ges integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select  r.cod_per, r.cod_sec, r.fec_rem, r.fini_rem, r.ffin_rem, r.mes_rem, r.ges_rem, r.des_rem, r.est_rem, r.cod_rem, r.mon_rem,r.cod_suc,r.cod_gen,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) secretaria,cast(to_char(fini_rem,'dd/MM/yyyy') as varchar(15)) fechai,cast(to_char(ffin_rem,'dd/MM/yyyy') as varchar(15)) fechaf,cast(to_char(fec_rem,'dd/MM/yyyy') as varchar(15)) fecha
		from remuneracion r
		join persona p1 on p1.cod_per=r.cod_per
		join secretaria s on s.cod_sec=r.cod_sec
		join persona p2 on p2.cod_per=s.cod_sec
		where est_rem=estado;
	else
	tot:=(select count(*) from remuneracion r 
	join persona p1 on p1.cod_per=r.cod_per
		join secretaria s on s.cod_sec=r.cod_sec
		join persona p2 on p2.cod_per=s.cod_sec
	where r.cod_suc = suc and r.cod_gen = ges and r.est_rem=estado and upper(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per)) like upper(concat('%',buscar,'%')));
 return query 
	select r.cod_per, r.cod_sec, r.fec_rem, r.fini_rem, r.ffin_rem, r.mes_rem, r.ges_rem, r.des_rem, r.est_rem, r.cod_rem, r.mon_rem,r.cod_suc,r.cod_gen,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) secretaria,cast(to_char(fini_rem,'dd/MM/yyyy') as varchar(15)) fechai,cast(to_char(ffin_rem,'dd/MM/yyyy') as varchar(15)) fechaf,cast(to_char(fec_rem,'dd/MM/yyyy') as varchar(15)) fecha,
		row_number() OVER(ORDER BY r.cod_rem) as RN,tot as Tot
		from remuneracion r
		join persona p1 on p1.cod_per=r.cod_per
		join secretaria s on s.cod_sec=r.cod_sec
		join persona p2 on p2.cod_per=s.cod_sec
	where r.cod_suc = suc and r.cod_gen = ges and r.est_rem=estado and upper(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per)) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.remuneracion_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select  r.cod_per, r.cod_sec, r.fec_rem, r.fini_rem, r.ffin_rem, r.mes_rem, r.ges_rem, r.des_rem, r.est_rem, r.cod_rem, r.mon_rem,r.cod_suc,
	cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) secretaria,
	cast(to_char(fini_rem,'dd/MM/yyyy') as varchar(15)) fechai,cast(to_char(ffin_rem,'dd/MM/yyyy') as varchar(15)) fechaf,cast(to_char(fec_rem,'dd/MM/yyyy') as varchar(15)) fecha
		from remuneracion r
		join persona p1 on p1.cod_per=r.cod_per
		join secretaria s on s.cod_sec=r.cod_sec
		join persona p2 on p2.cod_per=s.cod_sec
		where cod_rem=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_adicionar(nom character varying, des character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_rol),0)+1 from rol);
	insert into rol(cod_rol,nom_rol,des_rol) values(cod,upper(trim(nom)),des);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
	
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update rol set est_rol=est where cod_rol=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cod_rol,nom_rol,des_rol,est_rol from rol where est_rol=estado;
	else
	tot:=(select count(*) from rol where est_rol=estado);
 return query 
	select cod_rol,nom_rol,des_rol,est_rol,
		row_number() OVER(ORDER BY rol.cod_rol) as RN,tot as Tot,(select cast(count(*) as int) from rolmen where rolmen.cod_rol=rol.cod_rol) menus
		from rol 
	where est_rol=estado and upper(nom_rol) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_modificar(nom character varying, des character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update rol set (nom_rol,des_rol)=(upper(trim(nom)),des) where cod_rol=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_obtener(cod integer)
 RETURNS SETOF rol
 LANGUAGE plpgsql
AS $function$
BEGIN
 RETURN QUERY select * from rol where cod_rol=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_obtenermenus(cod integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select menu.cod_men,nom_men,des_men,ico_men,est_men,case when cod_rol is null then cast('' as varchar(8)) else cast('selected' as varchar(8)) end as tipo 
		from menu 
		left join rolmen on rolmen.cod_men=menu.cod_men and rolmen.cod_rol=cod 
		where est_men=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_obtenerroles(cod_per bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select rol.cod_rol, nom_rol, des_rol, est_rol,case when usurol.cod_per is null then cast('' as varchar(15)) else cast('selected' as varchar(15)) end as tipo 
		from rol 
		left join usurol on usurol.cod_rol=rol.cod_rol and usurol.cod_per=$1 and fecha_baja is null where est_rol=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_obtenerxcodper(cod bigint)
 RETURNS SETOF rol
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select r.*
		from rol r 
		join usurol ur on ur.cod_rol=r.cod_rol 
		join persona p on p.cod_per=ur.cod_per and p.est_per=true and p.cod_per=cod 
		where r.est_rol=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rol_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from rol where nom_rol=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.rolmen_adicionar(codr integer, menus integer[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
	indice int:=1;
	tam int:=array_length(menus,1);
BEGIN
	delete from rolmen where cod_rol=codr;
	while indice<=tam
	loop
		insert into rolmen(cod_rol,cod_men) values(codr,menus[indice]);
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.secretaria_adicionar(codigo bigint, fecha date, sueldo double precision)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into secretaria(cod_sec,fini_sec,sue_sec) values(codigo,fecha,sueldo);
	if not EXISTS(select * from usurol where cod_per=codigo and cod_rol=4 and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(codigo,4);
	end if;	
	RETURN codigo;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.secretaria_darestado(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update secretaria set est_sec=est where cod_sec=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.secretaria_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			SELECT cod_sec, fini_sec, sue_sec, est_sec,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,cast(to_char(fini_sec,'dd/MM/yyyy') as varchar(15)) fecha
			FROM secretaria
			JOIN persona on persona.cod_per=secretaria.cod_sec
			where est_sec=estado;
	else
	tot:=(select count(*) from secretaria where est_sec=estado);
 return query 
	select cod_sec, fini_sec, sue_sec, est_sec,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,cast(to_char(fini_sec,'dd/MM/yyyy') as varchar(15)) fecha,
		row_number() OVER(ORDER BY secretaria.cod_sec) as RN,tot as Tot
		from secretaria
		JOIN persona on persona.cod_per=secretaria.cod_sec
	where est_sec=estado and upper(nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.secretaria_modificar(fecha date, sueldo double precision, cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update secretaria set (fini_sec,sue_sec)=(fecha,sueldo) where cod_sec=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.secretaria_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query 
	SELECT cod_sec, fini_sec, sue_sec, est_sec,cast(to_char(fini_sec,'dd/MM/yyyy') as varchar(15)) 
	FROM secretaria 
	where cod_sec=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.servicio_adicionar(nom character varying, des character varying, code_tipser integer, pre double precision)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_ser),0)+1 from servicio);
	insert into servicio(cod_ser,nom_ser,des_ser,cod_tipser,pre_ser) values(cod,upper(trim(nom)),des,code_tipser,pre);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.servicio_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update servicio set est_ser=est where cod_ser=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.servicio_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_ser,nom_ser,des_ser,est_ser,servicio.cod_tipser,pre_ser,nom_tipser 
			from servicio 
			join tiposervicio on tiposervicio.cod_tipser=servicio.cod_tipser
			where est_ser=estado;
	else
	tot:=(select count(*) from servicio where est_ser=estado);
 return query 
	select cod_ser,nom_ser,des_ser,est_ser,servicio.cod_tipser,pre_ser,nom_tipser ,
		row_number() OVER(ORDER BY servicio.cod_ser) as RN,tot as Tot
		from servicio 
join tiposervicio on tiposervicio.cod_tipser=servicio.cod_tipser
	where est_ser=estado and upper(nom_ser) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.servicio_modificar(nom character varying, des character varying, cod integer, code_tipser integer, pre double precision)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update servicio set (nom_ser,des_ser,pre_ser,cod_tipser)=(upper(trim(nom)),des,pre,code_tipser) where cod_ser=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.servicio_obtener(cod integer)
 RETURNS SETOF servicio
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from servicio where cod_ser=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.servicio_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from servicio where nom_ser=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.subcuenta_contable_lista(inicio integer, limite integer, buscar character varying, est boolean, xcuenta integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	tot:=(select count(*) from subcuenta_contable where estado =est and cod_cuenta=xcuenta);
 return query 
	select sc.*,row_number() OVER(ORDER BY sc.codigo desc) as rn,tot as tot
		from subcuenta_contable sc
	where sc.estado=est and sc.cod_cuenta = xcuenta and upper(sc.codigo) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
END
$function$
;

CREATE OR REPLACE FUNCTION public.sucursal_adicionar(des character varying, nom character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	DECLARE cod int;
BEGIN
cod:=(select coalesce(max(cod_suc),0)+1 from sucursal);
	INSERT INTO sucursal(cod_suc,nombre,descripcion,estado) values(cod,nom,des,true);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RETURN -1;
	
END
$function$
;

CREATE OR REPLACE FUNCTION public.sucursal_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
update sucursal set estado=est where cod_suc=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.sucursal_lista(inicio integer, limite integer, buscar character varying, est boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cod_suc,nombre,descripcion,estado from sucursal where estado=est;
	else
	tot:=(select count(*) from sucursal where estado=est and upper(nombre) like upper(concat('%',buscar,'%')));
 return query 
	select cod_suc,nombre,descripcion,estado,
		row_number() OVER(ORDER BY sucursal.cod_suc) as RN,tot as Tot
		from sucursal 
	where estado=est and upper(descripcion) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.sucursal_modificar(nom character varying, des character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update sucursal set (nombre,descripcion)=(nom,des) where cod_suc=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.sucursal_obtener(integer)
 RETURNS SETOF general
 LANGUAGE plpgsql
AS $function$
BEGIN
 RETURN QUERY select * from sucursal where cod_suc=$1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tema_adicionar(nom character varying, des character varying, code_mat integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_tem),0)+1 from tema);
	insert into tema(cod_tem,nom_tem,des_tem,cod_mat) values(cod,upper(trim(nom)),des,code_mat);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tema_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tema set est_tem=est where cod_tem=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tema_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_tem,nom_tem,des_tem,est_tem,tema.cod_mat,nom_mat 
			from tema 
			join materia on materia.cod_mat=tema.cod_mat
			where est_tem=estado;
	else
	tot:=(select count(*) from tema where est_tem=estado);
 return query 
	select cod_tem,nom_tem,des_tem,est_tem,tema.cod_mat,nom_mat ,
		row_number() OVER(ORDER BY tema.cod_tem) as RN,tot as Tot
		from tema 
join materia on materia.cod_mat=tema.cod_mat
	where est_tem=estado and upper(nom_tem) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tema_modificar(nom character varying, des character varying, cod integer, code_mat integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tema set (nom_tem,des_tem,cod_mat)=(upper(trim(nom)),des,code_mat) where cod_tem=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tema_obtener(cod integer)
 RETURNS SETOF tema
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from tema where cod_tem=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tema_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from tema where nom_tem=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_adicionar(nom character varying, des character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_tipmat),0)+1 from tipomateria);
	insert into tipomateria(cod_tipmat,nom_tipmat,des_tipmat) values(cod,upper(trim(nom)),des);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tipomateria set est_tipmat=est where cod_tipmat=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cod_tipmat,nom_tipmat,des_tipmat,est_tipmat from tipomateria where est_tipmat=estado;
	else
	tot:=(select count(*) from tipomateria where est_tipmat=estado);
 return query 
	select cod_tipmat,nom_tipmat,des_tipmat,est_tipmat,
		row_number() OVER(ORDER BY tipomateria.cod_tipmat) as RN,tot as Tot
		from tipomateria 
	where est_tipmat=estado and upper(nom_tipmat) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_modificar(nom character varying, des character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tipomateria set (nom_tipmat,des_tipmat)=(upper(trim(nom)),des) where cod_tipmat=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_obtener(cod integer)
 RETURNS SETOF tipomateria
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from tipomateria where cod_tipmat=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_obtenerxcodayu(code_ayu integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select tm.cod_tipmat, nom_tipmat, des_tipmat, est_tipmat,case when atm.cod_tipmat is null then cast('' as varchar(8)) else cast('selected' as varchar(8)) end as tipo  
		from tipomateria tm 
		left join ayu_tipmat atm on tm.cod_tipmat=atm.cod_tipmat and atm.cod_ayu=code_ayu 
		where tm.est_tipmat=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_obtenerxcodayu(code_ayu bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY 
		select tm.cod_tipmat, nom_tipmat, des_tipmat, est_tipmat,case when atm.cod_tipmat is null then cast('' as varchar(8)) else cast('selected' as varchar(8)) end as tipo  
		from tipomateria tm 
		left join ayu_tipmat atm on tm.cod_tipmat=atm.cod_tipmat and atm.cod_ayu=code_ayu 
		where tm.est_tipmat=true;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipomateria_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from tipomateria where nom_tipmat=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipoproducto_adicionar(nom character varying, des character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_tippro),0)+1 from tipoproducto);
	insert into tipoproducto(cod_tippro,nom_tippro,des_tippro) values(cod,upper(trim(nom)),des);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipoproducto_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tipoproducto set est_tippro=est where cod_tippro=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipoproducto_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query select cod_tippro,nom_tippro,des_tippro,est_tippro from tipoproducto where est_tippro=estado;
	else
	tot:=(select count(*) from tipoproducto where est_tippro=estado);
 return query 
	select cod_tippro,nom_tippro,des_tippro,est_tippro,
		row_number() OVER(ORDER BY tipoproducto.cod_tippro) as RN,tot as Tot
		from tipoproducto 
	where est_tippro=estado and upper(nom_tippro) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipoproducto_modificar(nom character varying, des character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tipoproducto set (nom_tippro,des_tippro)=(upper(trim(nom)),des) where cod_tippro=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipoproducto_obtener(cod integer)
 RETURNS SETOF tipoproducto
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from tipoproducto where cod_tippro=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tipoproducto_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from tipoproducto where nom_tippro=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tiposervicio_adicionar(nom character varying, des character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_tipser),0)+1 from tiposervicio);
	insert into tiposervicio(cod_tipser,nom_tipser,des_tipser) values(cod,upper(trim(nom)),des);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tiposervicio_darestado(cod integer, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tiposervicio set est_tipser=est where cod_tipser=cod;
IF NOT FOUND THEN
	RETURN FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tiposervicio_lista(inicio integer, limite integer, buscar character varying, estado boolean)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select cod_tipser,nom_tipser,des_tipser,est_tipser
			from tiposervicio 
			where est_tipser=estado;
	else
	tot:=(select count(*) from tiposervicio where est_tipser=estado);
 return query 
	select cod_tipser,nom_tipser,des_tipser,est_tipser,
		row_number() OVER(ORDER BY tiposervicio.cod_tipser) as RN,tot as Tot
		from tiposervicio 
	where est_tipser=estado and upper(nom_tipser) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tiposervicio_modificar(nom character varying, des character varying, cod integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	update tiposervicio set (nom_tipser,des_tipser)=(upper(trim(nom)),des) where cod_tipser=cod;
	IF NOT FOUND THEN
		RETURN FALSE;
	END IF;
	RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tiposervicio_obtener(cod integer)
 RETURNS SETOF tiposervicio
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY select * from tiposervicio where cod_tipser=cod;
END
$function$
;

CREATE OR REPLACE FUNCTION public.tiposervicio_validar(nom character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	IF EXISTS (select * from tiposervicio where nom_tipser=nom) THEN
	RETURN TRUE;
	END IF;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.usurol_adicionar(cod_p bigint, cod_r integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into usurol(cod_per,cod_rol) values(cod_p,cod_r);
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.usurol_modificar(codp bigint, codr integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE resp boolean;
BEGIN
update usurol set fecha_baja=now() where cod_per=codp and cod_rol=codr and fecha_baja is null;
IF NOT FOUND THEN
	return FALSE;
END IF;
RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_adicionar(cod_usuario bigint, cod_cliente bigint, obs character varying, tot double precision, des double precision, gestion integer, suc integer, productos integer[], precios double precision[], cantidades integer[], descuentos double precision[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(productos,1);
code_arqcaj bigint;
code_detarq int;
monto float:=0;
BEGIN
	while indice<=tam
	loop
		monto:=monto+descuentos[indice];
		indice=indice+1;
	end loop;
	indice:=1;
	cod:=(select coalesce(max(cod_ven),0)+1 from venta);
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,8,'Por ingreso de venta.',(tot-des-monto)));
	
	insert into venta(cod_ven,cod_per,cod_cli,fec_ven,obs_ven,tot_ven,des_ven,ges_gen,cod_arqcaj,cod_detarq,cod_suc) values(cod,cod_usuario,cod_cliente,now(),obs,tot,des,gestion,code_arqcaj,code_detarq,suc);
	while indice<=tam
	loop
		insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven) values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice]);
		update producto set can_pro=can_pro-cantidades[indice] where cod_pro=productos[indice];
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_adicionar(cod_usuario bigint, cod_cliente bigint, obs character varying, tot double precision, des double precision, gestion integer, suc integer, subtot double precision, productos integer[], precios double precision[], cantidades integer[], descuentos double precision[], subtotales double precision[], totales double precision[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(productos,1);
code_arqcaj bigint;
code_detarq int;
monto float:=0;
BEGIN
	while indice<=tam
	loop
		monto:=monto+descuentos[indice];
		indice=indice+1;
	end loop;
	indice:=1;
	cod:=(select coalesce(max(cod_ven),0)+1 from venta);	
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	code_detarq:=(select detallearqueo_adicionar(code_arqcaj,8,'Por ingreso de venta.',tot));
	insert into venta(cod_ven,cod_per,cod_cli,fec_ven,obs_ven,subtot_ven,tot_ven,des_ven,ges_gen,cod_arqcaj,cod_detarq,cod_suc) values(cod,cod_usuario,cod_cliente,now(),obs,subtot,tot,des,gestion,code_arqcaj,code_detarq,suc);
	while indice<=tam
	loop
		insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven,subtot_detven,tot_detven) 
	values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice],subtotales[indice],totales[indice]);
		update producto set can_pro=can_pro-cantidades[indice] where cod_pro=productos[indice];
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_adicionar(cod_usuario bigint, cod_cliente bigint, obs character varying, tot double precision, des double precision, gestion integer, suc integer, subtot double precision, xcuenta integer, productos integer[], precios double precision[], cantidades integer[], descuentos double precision[], subtotales double precision[], totales double precision[])
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(productos,1);
code_arqcaj bigint;
code_detarq int;
monto float:=0;
nombre_cuenta varchar(50);
BEGIN
	while indice<=tam
	loop
		monto:=monto+descuentos[indice];
		indice=indice+1;
	end loop;
	indice:=1;
	
	cod:=(select coalesce(max(cod_ven),0)+1 from venta);	
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	
	if xcuenta is null then
		code_detarq:=(select detallearqueo_adicionar(code_arqcaj,8,'Por ingreso de venta.',tot,xcuenta));
	else
		nombre_cuenta:=(select cuenta_obtener(xcuenta));
		code_detarq:=(select detallearqueo_adicionar(code_arqcaj,20,concat('Por ingreso de venta con pago bancario.',nombre_cuenta),tot,xcuenta));
	end if;
	insert into venta(cod_ven,cod_per,cod_cli,fec_ven,obs_ven,subtot_ven,tot_ven,des_ven,ges_gen,cod_arqcaj,cod_detarq,cod_suc,cod_subcuenta) values(cod,cod_usuario,cod_cliente,now(),obs,subtot,tot,des,gestion,code_arqcaj,code_detarq,suc,xcuenta);
	while indice<=tam
	loop
		insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven,subtot_detven,tot_detven) 
	values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice],subtotales[indice],totales[indice]);
		update producto set can_pro=can_pro-cantidades[indice] where cod_pro=productos[indice];
		indice=indice+1;
	end loop;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_adicionar_simple(cod_usuario bigint, gestion integer, codigo integer, precio double precision, suc integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
DECLARE code_detven int;
code_arqcaj bigint;
code_detarq int;
BEGIN
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	cod=(select COALESCE(max(cod_ven),0) from venta where cod_arqcaj=code_arqcaj and tip_ven=0);
	IF(cod=0)THEN
		code_detarq:=(select detallearqueo_adicionar(code_arqcaj,8,'Por ingreso de ventas simples.',precio));
		cod:=(select coalesce(max(cod_ven),0)+1 from venta);
		insert into venta(cod_ven,cod_per,cod_cli,fec_ven,obs_ven,tot_ven,des_ven,ges_gen,cod_arqcaj,cod_detarq,tip_ven,cod_suc) values(cod,cod_usuario,0,now(),'Por venta general',precio,0,gestion,code_arqcaj,code_detarq,0,suc);
		insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven) values(cod,1,codigo,precio,1,0);
	ELSE
		code_detven=(select coalesce(max(cod_detven),0) from detalleventa where cod_pro=codigo and cod_ven=cod);
		if(code_detven>0)then
			update detalleventa set can_detven=can_detven+1 where cod_ven=cod and cod_detven=code_detven;
		else
			code_detven=(select coalesce(max(cod_detven),0)+1 from detalleventa where cod_ven=cod);
			insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven) values(cod,code_detven,codigo,precio,1,0);
		end if;
		update venta set tot_ven=tot_ven+precio where cod_ven=cod;
		code_detarq=(select cod_detarq from venta where cod_ven=cod);
		update detallearqueo set mon_detarq=mon_detarq+precio where cod_arqcaj=code_arqcaj and cod_detarq=code_detarq;
	END IF;
	update producto set can_pro=can_pro-1 where cod_pro=codigo;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_adicionar_simple_manual(cod_usuario bigint, gestion integer, codigo integer, precio double precision, cant integer, total double precision, suc integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
DECLARE code_detven int;
code_arqcaj int;
code_detarq int;
BEGIN
	code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	cod=(select COALESCE(max(cod_ven),0) from venta where cod_arqcaj=code_arqcaj and tip_ven=0);
	IF(cod=0)THEN
		code_detarq:=(select detallearqueo_adicionar(code_arqcaj,8,'Por ingreso de ventas simples.',precio));
		cod:=(select coalesce(max(cod_ven),0)+1 from venta);
		insert into venta(cod_ven,cod_per,cod_cli,fec_ven,obs_ven,tot_ven,des_ven,ges_gen,cod_arqcaj,cod_detarq,tip_ven,cod_suc) values(cod,cod_usuario,0,now(),'Por venta general',total,0,gestion,code_arqcaj,code_detarq,0,suc);
		insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven) values(cod,1,codigo,precio,cant,0);
	ELSE
		code_detven=(select coalesce(max(cod_detven),0) from detalleventa where cod_pro=codigo and cod_ven=cod);
		if(code_detven>0)then
			update detalleventa set can_detven=can_detven+1 where cod_ven=cod and cod_detven=code_detven;
		else
			code_detven=(select coalesce(max(cod_detven),0)+1 from detalleventa where cod_ven=cod);
			insert into detalleventa(cod_ven,cod_detven,cod_pro,pre_detven,can_detven,des_detven) values(cod,code_detven,codigo,precio,cant,0);
		end if;
		update venta set tot_ven=tot_ven+total where cod_ven=cod;
		code_detarq=(select cod_detarq from venta where cod_ven=cod);
		update detallearqueo set mon_detarq=mon_detarq+total where cod_arqcaj=code_arqcaj and cod_detarq=code_detarq;
	END IF;
	update producto set can_pro=can_pro-cant where cod_pro=codigo;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_eliminar(cod bigint, est boolean)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE codigo INTEGER;
DECLARE cantidad INTEGER;
DECLARE v_cursor1 CURSOR FOR select cod_pro,can_detven from detalleventa where cod_ven=cod ;
DECLARE code_arqcaj int;
DECLARE code_detarq int;
BEGIN
	code_arqcaj=(select cod_arqcaj from venta where cod_ven=cod);
	code_detarq=(select cod_detarq from venta where cod_ven=cod);
	update detallearqueo set est_detarq=false where cod_arqcaj=code_arqcaj and cod_detarq=code_detarq;
	OPEN v_cursor1;
		loop
		FETCH v_cursor1 INTO codigo,cantidad;
			EXIT WHEN NOT FOUND;
				update producto set can_pro=can_pro+cantidad where cod_pro=codigo;
		end loop;
		close v_cursor1;
		update venta set est_ven=est where cod_ven=cod;
		RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_lista(inicio integer, limite integer, buscar character varying, estado boolean, cod_user bigint, fini date, ffin date, gestion integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select venta.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) cliente,cast(to_char(fec_ven,'dd/MM/yyyy') as varchar(15)) fecha
			from venta
			join persona p1 on p1.cod_per=venta.cod_per
			join cliente on cliente.cod_cli=venta.cod_cli
			join persona p2 on p2.cod_per=cliente.cod_cli
			where est_ven=estado and (venta.cod_per=cod_user or cod_user=-1) and cod_suc=suc and ges_gen=gestion and fec_ven>=fini and fec_ven<=ffin;
	else
	tot:=(select count(*) from venta
	join persona p1 on p1.cod_per=venta.cod_per
	join cliente on cliente.cod_cli=venta.cod_cli
	join persona p2 on p2.cod_per=cliente.cod_cli
	where est_ven=estado and (venta.cod_per=cod_user or cod_user=-1) and cod_suc = suc and ges_gen=gestion and cast(fec_ven as date)>=fini and cast(fec_ven as date)<=ffin and upper(to_char(fec_ven,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%'))); 
	return query 
	select venta.*,
		row_number() OVER(ORDER BY venta.fec_ven desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) cliente,cast(to_char(fec_ven,'dd/MM/yyyy') as varchar(15)) fecha
		from venta
	join persona p1 on p1.cod_per=venta.cod_per
	join cliente on cliente.cod_cli=venta.cod_cli
	join persona p2 on p2.cod_per=cliente.cod_cli
	where est_ven=estado and (venta.cod_per=cod_user or cod_user=-1) and ges_gen=gestion and cod_suc = suc and cast(fec_ven as date)>=fini and cast(fec_ven as date)<=ffin and upper(to_char(fec_ven,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN query select cod_ven, venta.cod_per, venta.cod_cli, fec_ven, obs_ven, tot_ven,  des_ven, ges_gen,est_ven,cod_arqcaj,cod_detarq,tip_ven,cod_suc,subtot_ven,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) cliente,cast(to_char(fec_ven,'dd/MM/yyyy') as varchar(15)) fecha
			from venta
			join persona p1 on p1.cod_per=venta.cod_per
			join cliente on cliente.cod_cli=venta.cod_cli
			join persona p2 on p2.cod_per=cliente.cod_cli
			where cod_ven=cod;
END
$function$
;


--Despues de entregar sistema RESULTADO FINAL
CREATE OR REPLACE FUNCTION public.persona_asignar_sucursal(cod_p bigint, xsucursal integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN
	if not exists (select * from tiene_sucursal where cod_per=cod_p and cod_suc=xsucursal) then 
		insert into tiene_sucursal(cod_per,cod_suc) values(cod_p,xsucursal);
	end if;
	RETURN TRUE;
	EXCEPTION
			WHEN OTHERS THEN
				RETURN FALSE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_lista_x_ayudante(inicio integer, limite integer, buscar character varying, estado boolean, code_ayu bigint, gestion integer, tipo integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
		select prestacion.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,
		cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,
		cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
		join persona p1 on p1.cod_per=prestacion.cod_per
		join estudiante on estudiante.cod_est=prestacion.cod_est
		join persona p2 on p2.cod_per=estudiante.cod_est
		join docente on docente.cod_doc=prestacion.cod_doc
		join persona p3 on p3.cod_per=docente.cod_doc
		join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
		join persona p4 on p4.cod_per=ayudante.cod_ayu
		where est_pre=estado and (prestacion.cod_per=code_ayu or code_ayu=-1) and (tip_pre=tipo or tipo=-1) and ges_gen=gestion and cod_suc =suc;
	else
	tot:=(select count(*) from prestacion join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%'))); 
	return query 
	select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,
		row_number() OVER(ORDER BY prestacion.fec_pre desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_obtener_resumen_por_ayudante(cod bigint, xgestion integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query select a.cod_ayu,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre=1) tpendientes,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre =2) tcumplidos,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre =5) tabandonados,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre=3) tincumplidos,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre=4) tpagado_por_cumplido,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre=6) tpagado_por_abandono,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre=7) tmultado_por_incumplido,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = cod and p.ges_gen =xgestion and p.tip_pre=8) tperdonado_por_incumplido
from ayudante a 
where a.cod_ayu =cod; 
END
$function$
;
CREATE OR REPLACE FUNCTION public.prestacion_lista_x_ayudante(inicio integer, limite integer, buscar character varying, estado boolean, code_ayu bigint, gestion integer, tipo integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
		select prestacion.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,
		cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,
		cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
		join persona p1 on p1.cod_per=prestacion.cod_per
		join estudiante on estudiante.cod_est=prestacion.cod_est
		join persona p2 on p2.cod_per=estudiante.cod_est
		join docente on docente.cod_doc=prestacion.cod_doc
		join persona p3 on p3.cod_per=docente.cod_doc
		join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
		join persona p4 on p4.cod_per=ayudante.cod_ayu
		where est_pre=estado and (prestacion.cod_ayu =code_ayu or code_ayu=-1) and (tip_pre=tipo or tipo=-1) and ges_gen=gestion and cod_suc =suc
	order by cod_pre asc;
	else
	tot:=(select count(*) from prestacion join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%'))); 
	return query 
	select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,
		row_number() OVER(ORDER BY prestacion.fec_pre desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

drop function public.tipomateria_adicionar;
CREATE OR REPLACE FUNCTION public.tipomateria_adicionar(nom character varying, des character varying)
 RETURNS int
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
BEGIN
	cod:=(select coalesce(max(cod_tipmat),0)+1 from tipomateria);
	insert into tipomateria(cod_tipmat,nom_tipmat,des_tipmat) values(cod,upper(trim(nom)),des);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;


CREATE OR REPLACE FUNCTION public.prestacion_lista_x_fecha(inicio integer, limite integer, buscar character varying, estado boolean, 
code_ayu bigint, gestion integer, tipo integer, suc integer,fini character varying(10),ffin character varying(10))
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$	
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
		select prestacion.*,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,
		cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,
		cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
		join persona p1 on p1.cod_per=prestacion.cod_per
		join estudiante on estudiante.cod_est=prestacion.cod_est
		join persona p2 on p2.cod_per=estudiante.cod_est
		join docente on docente.cod_doc=prestacion.cod_doc
		join persona p3 on p3.cod_per=docente.cod_doc
		join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
		join persona p4 on p4.cod_per=ayudante.cod_ayu
		where est_pre=estado and (prestacion.cod_ayu =code_ayu or code_ayu=-1) and (tip_pre=tipo or tipo=-1) and ges_gen=gestion and cod_suc =suc
		 and fecent_pre between to_date(fini,'DD/MM/YYYY') and to_date(ffin,'DD/MM/YYYY')
	order by cod_pre asc;
else
	tot:=(select count(*) from prestacion join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion  and fecent_pre between to_date(fini,'DD/MM/YYYY') and to_date(ffin,'DD/MM/YYYY')
and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%'))); 
	return query 
	select cod_pre, prestacion.cod_per, prestacion.cod_est, fec_pre, obs_pre, tot_pre,  des_pre, ges_gen,est_pre,prestacion.cod_ayu,tip_pre,fecent_pre,horent_pre,prestacion.cod_doc,sal_pre,cod_arqcaj,cod_detarq,pag_pre,por_pre,
		row_number() OVER(ORDER BY prestacion.fec_pre desc) as RN,tot as Tot,cast(concat(p1.nom_per,' ',p1.priape_per,' ',p1.segape_per) as varchar(100)) usuario,cast(concat(p2.nom_per,' ',p2.priape_per,' ',p2.segape_per) as varchar(100)) estudiante,cast(concat(p3.nom_per,' ',p3.priape_per,' ',p3.segape_per) as varchar(100)) docente,cast(concat(p4.nom_per,' ',p4.priape_per,' ',p4.segape_per) as varchar(100)) ayudante,cast(to_char(fec_pre,'dd/MM/yyyy') as varchar(15)) fecha,cast(to_char(fecent_pre,'dd/MM/yyyy') as varchar(15)) entrega
		from prestacion
	join persona p1 on p1.cod_per=prestacion.cod_per
			join estudiante on estudiante.cod_est=prestacion.cod_est
			join persona p2 on p2.cod_per=estudiante.cod_est
			join docente on docente.cod_doc=prestacion.cod_doc
			join persona p3 on p3.cod_per=docente.cod_doc
			join ayudante on ayudante.cod_ayu=prestacion.cod_ayu
			join persona p4 on p4.cod_per=ayudante.cod_ayu
	where est_pre=estado and prestacion.cod_ayu=code_ayu and cod_suc = suc and tip_pre=tipo and ges_gen=gestion and fecent_pre between to_date(fini,'DD/MM/YYYY') and to_date(ffin,'DD/MM/YYYY') and upper(to_char(fec_pre,'dd/MM/yyyy')||' '||p2.nom_per||' '||p2.priape_per||' '||p2.segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.prestacion_obtener_resumen_pendiente(xgestion integer, fini character varying, ffin character varying)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query select a.cod_ayu,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = a.cod_ayu and p.ges_gen =xgestion and p.tip_pre=1
and fecent_pre between to_date(fini,'DD/MM/YYYY') and to_date(ffin,'DD/MM/YYYY')) tpendientes,p2.fot_per,obtener_nombre_persona(p2.cod_per) xayudante
from ayudante a 
inner join persona p2 on p2.cod_per = a.cod_ayu 
where a.est_ayu =true; 
END
$function$
;