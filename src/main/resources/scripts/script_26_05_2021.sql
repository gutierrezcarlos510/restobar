CREATE TABLE public.cotizacion (
	cotizacion_id int8 NOT NULL,
	celular varchar(50) NOT NULL,
	cod_tipser int4 NOT NULL,
	tipo_estudio varchar(50) NOT NULL,
	ciudad varchar(50) NOT NULL,
	fecha timestamp(0) NOT NULL,
	estado bool NOT NULL,
	cod_pre int8 NULL,
	celular_propietario varchar(150) NOT NULL,
	precio float4 NULL,
	docente varchar(150) NOT NULL,
	created_by int8 NOT NULL,
	created_at timestamp(0) NOT NULL,
	gestion int4 NOT NULL,
	cod_suc int4 NOT NULL,
	materia varchar(150) NULL,
	tema varchar(200) NULL,
	cod_ayu int8 NULL
);
COMMENT ON TABLE public.cotizacion IS 'Cotizaciones de prestacion';

CREATE OR REPLACE FUNCTION public.cotizacion_lista(inicio integer, limite integer, buscar character varying, xestado boolean, tipo boolean, pgestion integer, suc integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE tot int;
BEGIN
	if(inicio<0)THEN
		return query 
			select c.*,obtener_nombre_persona(c.created_by) xusuario,t.nom_tipser as xtiposervicio,obtener_nombre_persona(c.cod_ayu) as xayudante
			from cotizacion c 
			inner join tiposervicio t on t.cod_tipser = c.cod_tipser 
			where c.estado =xestado and c.gestion =gestion and c.cod_suc =suc  and ((c.cod_pre is null)=tipo);
	else
		tot:=(select count(*) from cotizacion c where c.estado =xestado and c.gestion =pgestion and c.cod_suc =suc and concat(c.celular,' ',c.celular_propietario,' #' ,c.cotizacion_id) like concat('%','','%') and ((c.cod_pre is null)=tipo)); 
		return query 
			select c.*,row_number() OVER(ORDER BY c.cotizacion_id desc) as RN,tot as Tot,obtener_nombre_persona(c.created_by) xusuario,t.nom_tipser as xtiposervicio,obtener_nombre_persona(c.cod_ayu) as xayudante
			from cotizacion c 
			inner join tiposervicio t on t.cod_tipser = c.cod_tipser 
			where c.estado =xestado and c.gestion =pgestion and c.cod_suc =suc and concat(c.celular,' ',c.celular_propietario,' #' ,c.cotizacion_id) like concat('%',buscar,'%') 
			and ((c.cod_pre is null)=tipo)  LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cotizacion_adicionar(created bigint, fec character varying, pcelular character varying, pcod_tipser integer, ptipo_estudio character varying, pciudad character varying, p_cel_propietario character varying, pprecio real, pdocente character varying, pgestion integer, suc integer, pmateria character varying, ptema character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE cod bigint;
BEGIN
	cod:=(select coalesce(max(cotizacion_id),0)+1 from cotizacion);	
	INSERT INTO public.cotizacion (cotizacion_id, celular, cod_tipser, tipo_estudio, ciudad, fecha, estado, celular_propietario, precio, docente, created_by, created_at, gestion, cod_suc,materia,tema)
VALUES(cod,pcelular,pcod_tipser,ptipo_estudio,pciudad,to_timestamp(fec,'DD/MM/YYYY HH:mi:ss'),true,p_cel_propietario,pprecio,pdocente,created,now(),pgestion,suc,pmateria,ptema);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cotizacion_modificar(pcotizacion_id bigint, fec character varying, pcelular character varying, pcod_tipser integer, ptipo_estudio character varying, pciudad character varying, p_cel_propietario character varying, pprecio real, pdocente character varying, pmateria character varying, ptema character varying, payudante bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN	
	update cotizacion set celular=pcelular, cod_tipser=pcod_tipser, tipo_estudio=ptipo_estudio, ciudad=pciudad, fecha=to_timestamp(fec,'DD/MM/YYYY HH:mi:ss'), 
	celular_propietario=p_cel_propietario, precio=pprecio, docente=pdocente,materia=pmateria,tema=ptema,cod_ayu=payudante where cotizacion_id  = pcotizacion_id;
	RETURN true;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN false;
END
$function$
;

CREATE OR REPLACE FUNCTION public.cotizacion_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN query select c.*,obtener_nombre_persona(c.created_by) xusuario,t.nom_tipser as xtiposervicio,obtener_nombre_persona(c.cod_ayu) xayudante
			from cotizacion c 
			inner join tiposervicio t on t.cod_tipser = c.cod_tipser 
			where c.cotizacion_id = cod;			
END
$function$
;

--Nuevos
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
	tot:=(select count(*) from estudiante inner JOIN persona on persona.cod_per=estudiante.cod_est 
where est_est=estado and upper('#'||cod_est||' '||tel_per||' '||nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')));
 return query 
	select cod_est, est_est, teltut_est, tut_est,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,
		row_number() OVER(ORDER BY estudiante.cod_est desc) as RN,tot as Tot
		from estudiante
		inner JOIN persona on persona.cod_per=estudiante.cod_est
	where est_est=estado and upper('#'||cod_est||' '||tel_per||' '||nom_per||' '||priape_per||' '||segape_per) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

CREATE OR REPLACE FUNCTION public.persona_buscar_por_telefono(cel character varying)
 RETURNS SETOF persona
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query select * from persona where tel_per = cel;
END
$function$
;

update persona set tel_per='' where cod_per =13;

UPDATE public.tiposervicio
SET nom_tipser='VIRTUAL', des_tipser='Realizacion de examenes', est_tipser=true
WHERE cod_tipser=7;


CREATE OR REPLACE FUNCTION public.cotizacion_adicionar(created bigint, fec character varying, pcelular character varying, pcod_tipser integer, ptipo_estudio character varying, pciudad character varying, p_cel_propietario character varying, pprecio real, pdocente character varying, pgestion integer, suc integer, pmateria character varying, ptema character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE cod bigint;
BEGIN
	cod:=(select coalesce(max(cotizacion_id),0)+1 from cotizacion);	
	INSERT INTO public.cotizacion (cotizacion_id, celular, cod_tipser, tipo_estudio, ciudad, fecha, estado, celular_propietario, precio, docente, created_by, created_at, gestion, cod_suc,materia,tema)
VALUES(cod,pcelular,pcod_tipser,ptipo_estudio,pciudad,to_timestamp(fec,'DD/MM/YYYY HH24:mi:ss'),true,p_cel_propietario,pprecio,pdocente,created,now(),pgestion,suc,pmateria,ptema);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;


CREATE OR REPLACE FUNCTION public.cotizacion_modificar(pcotizacion_id bigint, fec character varying, pcelular character varying, pcod_tipser integer, ptipo_estudio character varying, pciudad character varying, p_cel_propietario character varying, pprecio real, pdocente character varying, pmateria character varying, ptema character varying, payudante bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
BEGIN	
	update cotizacion set celular=pcelular, cod_tipser=pcod_tipser, tipo_estudio=ptipo_estudio, ciudad=pciudad, fecha=to_timestamp(fec,'DD/MM/YYYY HH24:mi:ss'), 
	celular_propietario=p_cel_propietario, precio=pprecio, docente=pdocente,materia=pmateria,tema=ptema,cod_ayu=payudante where cotizacion_id  = pcotizacion_id;
	RETURN true;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN false;
END
$function$
;
