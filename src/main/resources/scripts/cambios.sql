--Limpiar transaccion de compra y venta
delete from detallecompra;
delete from detalleventa;
delete from venta;
delete from compra;
delete from inscripcion;
delete from detalleprestacion; 
delete from prestacion;
delete from detallearqueo;
delete from arqueocaja; 
-- Limpieza de productos
delete from cliente ;
delete from proveedor ;
delete from empresa ;
delete from dato;
delete from usurol;
delete from tiene_sucursal;
delete from descargas;
delete from archivos;
delete from carprac;
delete from matprac;
delete from practicos;
delete from docente;
delete from ayu_tipmat;
delete from detallehorarioclase;
delete from horarioclase;
delete from detalleplan; 
delete from mat_pla_ayu;
delete from ayudante;
delete from est_car;
delete from estudiante ;
delete from remuneracion;
delete from secretaria;
delete from ftp;
delete from persona;
delete from plan;
delete from "general" where est_gen =false;
delete from sucursal where estado = false;

INSERT INTO public.persona (ci_per,nom_per,priape_per,segape_per,tel_per,dir_per,ema_per,fot_per,est_per,sex_per,cod_per,codbio_per,codesc_per) VALUES 
('0000000','S','/','N',' ',' ',' ','avatar00.jpg',true,true,0,NULL,'0-20160101');
INSERT INTO public.persona (ci_per,nom_per,priape_per,segape_per,tel_per,dir_per,ema_per,fot_per,est_per,sex_per,cod_per,codbio_per,codesc_per) VALUES ('7167968','Carlos Franz','Gutierrez','Gutierrez','75136609','Calle Mexico entre German','','avatar00.jpg',true,true,1,'','1-99-20160101');
INSERT INTO public.usurol (cod_rol,fecha_alta,fecha_baja,cod_per) VALUES (1,'2016-04-11 07:49:51.000',NULL,1),(2,'2016-04-27 02:42:48.000',NULL,1);
INSERT INTO public.dato (log_dat,cla_dat,fecha_alta,fecha_baja,cod_per) VALUES ('admin','admin7167968',now(),NULL,1);
INSERT INTO public.sucursal (cod_suc,nombre,descripcion,estado) VALUES (1,'Casa Matriz','Sucursal de la Calle Madrid entre O''Connor y Junin',true);
INSERT INTO public."general" (ges_gen,des_gen,est_gen,nom_gen,logtex_gen,logsintex_gen,tel_gen,dir_gen,lug_gen,nit_gen,cod_suc) VALUES (2021,'CENTRO DE DEPORTES Y RECREACION',true,'RESULTADO FITNESS','logo-2021.jpg','loguito-2021.jpg','69327201','Calle Madrid, Zona Central','Tarija - Bolivia','76193133',1);
INSERT INTO public.tiene_sucursal (cod_per,cod_suc) VALUES (1,1);
INSERT INTO public.cliente (cod_per,cod_cli,est_cli) VALUES (0,1,true);
INSERT INTO public.empresa (cod_emp,nom_emp,dir_emp,tel_emp,ema_emp,est_emp) VALUES (0,'S/N','Sin direccion','0',NULL,true);
INSERT INTO public.proveedor (cod_per,cod_pro,cod_emp,est_pro) VALUES (0,1,0,true);

delete from codigoproducto;
delete from producto; 
delete from tipoproducto;


--script para ventas
ALTER TABLE public.venta ADD cod_subcuenta int4 NULL;

ALTER TABLE public.cuenta_contable DROP COLUMN es_interno;


ALTER TABLE public.ayudante DROP CONSTRAINT ayudante_cod_per_fkey;
ALTER TABLE public.ayudante DROP COLUMN cod_per;
ALTER TABLE public.ayudante ADD CONSTRAINT ayudante_fk FOREIGN KEY (cod_ayu) REFERENCES public.persona(cod_per);
ALTER TABLE public.docente DROP CONSTRAINT docente_cod_per_fkey;
ALTER TABLE public.docente DROP COLUMN cod_per;
ALTER TABLE public.docente ADD CONSTRAINT docente_fk FOREIGN KEY (cod_doc) REFERENCES public.persona(cod_per);
ALTER TABLE public.estudiante DROP CONSTRAINT estudiante_cod_per_fkey;
ALTER TABLE public.estudiante DROP COLUMN cod_per;
ALTER TABLE public.estudiante ADD CONSTRAINT estudiante_fk FOREIGN KEY (cod_est) REFERENCES public.persona(cod_per);
ALTER TABLE public.secretaria DROP COLUMN cod_per;
ALTER TABLE public.secretaria ADD CONSTRAINT secretaria_fk FOREIGN KEY (cod_sec) REFERENCES public.persona(cod_per);
--migracion de compras y proveedor
ALTER TABLE public.compra DROP CONSTRAINT compra_cod_pro_fkey;
update compra set cod_pro =subquery.cod_per  from (select cod_per,cod_pro from proveedor) subquery
where subquery.cod_pro=compra.cod_pro;
ALTER TABLE public.proveedor DROP CONSTRAINT proveedor_pkey;
update proveedor set cod_pro = cod_per;
ALTER TABLE public.proveedor ADD CONSTRAINT proveedor_pk PRIMARY KEY (cod_pro);
ALTER TABLE public.proveedor DROP COLUMN cod_per;
ALTER TABLE public.proveedor ADD CONSTRAINT proveedor_fk FOREIGN KEY (cod_pro) REFERENCES public.persona(cod_per);
ALTER TABLE public.compra ADD CONSTRAINT compra_fk FOREIGN KEY (cod_pro) REFERENCES public.proveedor(cod_pro);

ALTER TABLE public.venta DROP CONSTRAINT venta_cod_cli_fkey;
update venta set cod_cli =subquery.cod_per  from (select cod_per,cod_cli from cliente) subquery
where subquery.cod_cli=venta.cod_cli;
ALTER TABLE public.cliente DROP CONSTRAINT cliente_cod_per_fkey;
update cliente set cod_cli = cod_per;
ALTER TABLE public.cliente ADD CONSTRAINT cliente_fk FOREIGN KEY (cod_cli) REFERENCES public.persona(cod_per);
ALTER TABLE public.cliente DROP COLUMN cod_per;
ALTER TABLE public.cliente ADD CONSTRAINT cliente_fk FOREIGN KEY (cod_cli) REFERENCES persona(cod_per);
ALTER TABLE public.venta ADD CONSTRAINT venta_fk FOREIGN KEY (cod_cli) REFERENCES public.cliente(cod_cli);



--INSERT
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(43, 'Inventario', 'Personal de ventas', 'glyphicon glyphicon-th-large', true, '../producto/gestionInventario');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(44, 'Reportes Generales', 'Para administracion', 'glyphicon glyphicon-th-large', true, '../reporte/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(45, 'Ayudantes', 'Sistemas', 'glyphicon glyphicon-th-large', true, '../ayudante/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(46, 'Carrera', 'Carreras de una universidad', 'glyphicon glyphicon-th-large', true, '../carrera/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(47, 'Docente', 'Docente de una materia', 'glyphicon glyphicon-th-large', true, '../docente/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(48, 'Facultad', 'Facultad de una universidad', 'glyphicon glyphicon-th-large', true, '../facultad/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(49, 'Horarios', 'Horario de un curso', 'glyphicon glyphicon-th-large', true, '../horarioclase/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(50, 'Inscripcion', 'Inscripciones', 'glyphicon glyphicon-th-large', true, '../inscripcion/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(51, 'Entidades De Estudio', 'Universidad, instituto o escuela', 'glyphicon glyphicon-th-large', true, '../institucion/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(52, 'Materia', 'Materias de una carrera', 'glyphicon glyphicon-th-large', true, '../materia/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(53, 'Planes', 'Planes de clases', 'glyphicon glyphicon-th-large', true, '../plan/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(54, 'Prestaciones', 'Prestacion de servicios', 'glyphicon glyphicon-th-large', true, '../prestacion/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(55, 'Remuneraciones', 'Remuneraciones a ayudantes', 'glyphicon glyphicon-th-large', true, '../remuneracion/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(56, 'Servicio', 'Servicios que presta', 'glyphicon glyphicon-th-large', true, '../servicio/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(57, 'Temas', 'Temas de clases, examen o practicos', 'glyphicon glyphicon-th-large', true, '../tema/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(58, 'Tipo De Materias', 'Tipos de materias', 'glyphicon glyphicon-th-large', true, '../tipomateria/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(59, 'Tipos De Servicios', 'Tipos de servicios que se prestan', 'glyphicon glyphicon-th-large', true, '../tiposervicio/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(60, 'Estudiante', 'Estudiante de clases practicos o examenes', 'glyphicon glyphicon-th-large', true, '../estudiante/gestion');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(61, 'Pago De Ayudantes', 'Pago de ayudantes', 'glyphicon glyphicon-th-large', true, '../ayudante/gestion_pago_ayudante');

INSERT INTO public.menu
(cod_men, nom_men, des_men, ico_men, est_men)
VALUES(23, 'Servicios', 'Sistemas RESFIN', 'glyphicon glyphicon-book', true);


INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(12, 43);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(4, 44);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 45);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 46);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 47);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 48);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 49);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 50);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 51);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 52);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 53);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 54);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 55);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 56);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 57);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 58);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 59);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 60);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(23, 61);


INSERT INTO public.rolmen
(cod_rol, cod_men)
VALUES(1, 23);



CREATE OR REPLACE FUNCTION public.detalleventa_obtener(cod bigint)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query SELECT d.cod_ven, d.cod_detven, d.cod_pro, d.pre_detven, d.can_detven, d.des_detven,
	d.subtot_detven,d.tot_detven,cast(p.nom_pro as varchar(150)) producto
	FROM detalleventa d
	join producto p on p.cod_pro=d.cod_pro
	join tipoproducto tp on tp.cod_tippro=p.cod_tippro
	where d.cod_ven=cod;
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
	where est_pro=estado and concat('#',cast(cod_pro as text),' ',upper(nom_pro)) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

--Despues del backup
CREATE OR REPLACE FUNCTION public.ayudante_buscar(buscar character varying)
 RETURNS SETOF ayudante
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query
	select a.* from ayudante a
	join ayu_tipmat  atm on atm.cod_ayu=a.cod_ayu
	join tipomateria tm on tm.cod_tipmat=atm.cod_tipmat and upper(tm.nom_tipmat) like upper('%'||buscar||'%')
	join persona p on p.cod_per=a.cod_ayu
	where est_ayu=true;
END$function$
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
		row_number() OVER(ORDER BY carrera.cod_car desc) as RN,tot as Tot
		from carrera 
	join facultad on facultad.cod_fac=carrera.cod_fac
	join institucion on institucion.cod_ins=facultad.cod_ins
	where est_car=estado and upper(nom_car) like upper(concat('%',buscar,'%')) LIMIT limite OFFSET inicio;
	end if;
END
$function$
;

ALTER TABLE public.servicio ADD porcentaje_multa float4 NULL;
COMMENT ON COLUMN public.servicio.porcentaje_multa IS 'Porcentaje ';
update servicio set porcentaje_multa =0;

-- public.multa definition

-- Drop table

-- DROP TABLE public.multa;

CREATE TABLE public.multa (
	cod_multa int8 NOT NULL,
	monto float4 NOT NULL,
	cod_ayu int8 NOT NULL,
	tipo int2 NOT NULL, -- 1=pendiente, 2=realizado, 3=perdonado
	concepto varchar(500) NOT NULL,
	observacion varchar(250) NULL,
	cod_pre int8 NULL,
	estado bool NOT NULL DEFAULT true,
	fecha date NOT NULL,
	created_by int8 NOT NULL,
	created_at timestamp(0) NOT NULL,
	cod_arqcaj int8 NULL,
	cod_detarq int4 NULL,
	CONSTRAINT multa_pk PRIMARY KEY (cod_multa)
);

-- Column comments

COMMENT ON COLUMN public.multa.tipo IS '1=pendiente, 2=realizado, 3=perdonado';


-- public.multa foreign keys

ALTER TABLE public.multa ADD CONSTRAINT detallearqueo_fk FOREIGN KEY (cod_arqcaj, cod_detarq) REFERENCES detallearqueo(cod_arqcaj, cod_detarq);
ALTER TABLE public.multa ADD CONSTRAINT multa_fk FOREIGN KEY (cod_ayu) REFERENCES ayudante(cod_ayu);
ALTER TABLE public.multa ADD CONSTRAINT prestacion_fk FOREIGN KEY (cod_pre) REFERENCES prestacion(cod_pre);


ALTER TABLE public.prestacion ADD cod_subcuenta int4 NULL;


CREATE TABLE public.pago_prestacion (
	cod_pago_prestacion int8 NOT NULL,
	estado bool NOT NULL,
	cod_arqcaj int8 NOT NULL,
	CONSTRAINT pago_prestacion_pk PRIMARY KEY (cod_pago_prestacion)
);

CREATE TABLE public.detalle_pago (
	cod_pago_prestacion int8 NOT NULL,
	cod_arqcaj int8 NOT NULL,
	cod_detarq int4 NOT NULL,
	cod_pre int8 NOT NULL,
	CONSTRAINT detalle_pago_pk PRIMARY KEY (cod_pago_prestacion, cod_arqcaj, cod_detarq)
);


ALTER TABLE public.detalle_pago ADD CONSTRAINT detalle_pago_fk FOREIGN KEY (cod_arqcaj, cod_detarq) REFERENCES detallearqueo(cod_arqcaj, cod_detarq);
ALTER TABLE public.detalle_pago ADD CONSTRAINT detalle_pago_fk_1 FOREIGN KEY (cod_pago_prestacion) REFERENCES pago_prestacion(cod_pago_prestacion);

CREATE TABLE public.detalle_pago_multa (
	cod_pago_prestacion int8 NOT NULL,
	cod_multa int8 NOT NULL,
	CONSTRAINT detalle_pago_multa_pk PRIMARY KEY (cod_pago_prestacion,cod_multa),
	CONSTRAINT detalle_pago_multa_fk FOREIGN KEY (cod_pago_prestacion) REFERENCES public.pago_prestacion(cod_pago_prestacion),
	CONSTRAINT multa_fk FOREIGN KEY (cod_multa) REFERENCES public.multa(cod_multa)
);
ALTER TABLE public.pago_prestacion ADD cod_ayu int8 NOT NULL;
ALTER TABLE public.pago_prestacion ADD created_at timestamp(0) NOT NULL;



--APra avatars Fechas 22/05/2021
update persona set fot_per ='avatar00.jpg';
update persona set fot_per ='avatarx.jpg' where cod_per =1;
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(64, 'MI PERFIL', 'Perfil del ayudante', 'glyphicon glyphicon-th-large', true, '../ayudante/perfil');
INSERT INTO public.proceso
(cod_pro, nom_pro, des_pro, ico_pro, est_pro, url_pro)
VALUES(65, 'ACTIVIDADES', 'Tablero de prestaciones pendientes', 'glyphicon glyphicon-th-large', true, '../prestacion/dashboard');

INSERT INTO public.menu
(cod_men, nom_men, des_men, ico_men, est_men)
VALUES(24, 'MI MENU', 'Menu para ayudante', 'glyphicon glyphicon-th-large', true);

INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(4, 65);
INSERT INTO public.mepro
(cod_men, cod_pro)
VALUES(24, 64);

-- Despues de actualizar Avatar

CREATE OR REPLACE FUNCTION public.prestacion_obtener_resumen_pendiente(cod bigint, xgestion integer, fini character varying, ffin character varying)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
BEGIN
	return query select a.cod_ayu,
(select coalesce(count(*),0) from prestacion p where p.est_pre =true and p.cod_ayu = a.cod_ayu and p.ges_gen =xgestion and p.tip_pre=1
and fecent_pre between to_date(fini,'DD/MM/YYYY') and to_date(ffin,'DD/MM/YYYY')) tpendientes,p2.fot_per,obtener_nombre_persona(p2.cod_per) xayudante
from ayudante a 
inner join persona p2 on p2.cod_per = a.cod_ayu 
where a.est_ayu =true and (a.cod_ayu=cod or cod=-1); 
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_iniciar(del bigint, cusini bigint, monini real, ges integer, des character varying, suc integer)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE cod bigint;
BEGIN
	cod:=(select coalesce(max(id),0)+1 from arqueo);
	insert into arqueo(id,delegado_id,custodio_inicial_id,finicio,monto_inicial ,gestion,descripcion,sucursal_id)
	values(cod,del,cusini,now(),monini,ges,des,suc);
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.arqueocaja_obtenertotal(cod bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare inicial numeric(10,2):=0;
ingresos numeric(10,2):=0;
egresos numeric(10,2):=0;
BEGIN
	ingresos:=(select coalesce(sum(monto),0)
from detalle_arqueo
where estado=true and arqueo_id=cod and tipo in (5,6,7,8,9,10,13,16));
  egresos:=(select coalesce(sum(monto),0) from detalle_arqueo
 where estado=true and arqueo_id=cod and tipo in (1,2,3,4,14,15,17,21));
	inicial:=(select monto_inicial from arqueo where id=cod)+ingresos-egresos;
	return inicial ;
END$function$
;




CREATE OR REPLACE FUNCTION public.venta_adicionar(p_usuario bigint, p_cliente bigint, p_obs character varying,
p_tot numeric(10,2), p_des numeric(10,2), p_gestion integer, p_suc integer, p_subtot numeric(10,2),
productos bigint[], precios numeric(10,2)[], cantidades integer[], descuentos numeric(10,2)[],
subtotales numeric(10,2)[], totales numeric(10,2)[],compuestos boolean[])
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE v_cod bigint;
v_indice int:=1;
v_tam int:=array_length(productos,1);
v_arqueo bigint;
v_detalle_arqueo int2;
v_monto numeric(10,2):=0;
v_tipo_detalle_arqueo int2:=8;
BEGIN
	while v_indice<=tam
	loop
		v_monto := v_monto+descuentos[v_indice];
		v_indice := v_indice+1;
	end loop;
	v_cod:=(select coalesce(max(id),0)+1 from venta);
	v_arqueo:=(select coalesce(max(id),0) from arqueo where ac.delegado_id=p_usuario and ac.sucursal_id = p_suc and ac.custodio_final_id is null and ac.estado=true);
	if(v_arqueo = 0) then
		return -2;
	end if;
	v_detalle_arqueo:=(select detalle_arqueo_adicionar(v_arqueo, v_tipo_detalle_arqueo,'Por ingreso de venta.',p_tot));
	insert into venta(id,usuario_id,cliente_id,fecha,obs,subtotal,total,descuento ,gestion ,arqueo_id ,detalle_arqueo_id ,sucursal_id)
	values(v_cod,p_usuario,p_cliente,now(),obs,p_subtot,p_tot,p_des,gestion,v_arqueo,v_detalle_arqueo);
	v_indice:=1;
	while v_indice<=tam
	loop
		insert into detalle_venta(venta_id,id,producto_id,precio,cantidad,descuento,subtotal,total,es_compuesto)
	values(v_cod,v_indice,productos[v_indice],precios[v_indice],cantidades[indice],descuentos[v_indice],subtotales[v_indice],totales[v_indice],compuestos[v_indice]);
		update almacen set cantidad=cantidad-cantidades[v_indice] where producto_id=productos[v_indice] and sucursal_id = p_suc;
		v_indice=v_indice+1;
	end loop;
	RETURN v_cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;




CREATE OR REPLACE FUNCTION public.detalle_arqueo_adicionar(p_arqueo bigint, p_tipo int2, p_descripcion character varying, p_monto numeric(10,2))
 RETURNS int2
 LANGUAGE plpgsql
AS $function$
DECLARE cod int2;
BEGIN
		cod:=(select COALESCE(max(id),0)+1 from detalle_arqueo where arqueo_id=p_arqueo);
		insert into detalle_arqueo(arqueo_id, id,tipo,descripcion,monto,fecha) values(p_arqueo,cod,p_tipo,p_descripcion,p_monto,now());
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_eliminar(cod bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE v_producto int8;
DECLARE v_cantidad int4;
DECLARE v_sucursal int4;
DECLARE v_cursor1 CURSOR FOR select producto_id,cantidad,v.sucursal from detalle_venta dv inner join venta v on v.id = dv.venta_id where venta_id =cod ;
DECLARE v_arqueo int8;
DECLARE v_detalle_arqueo int2;
BEGIN
	v_arqueo = (select arqueo_id from venta where id = cod);
	v_detalle_arqueo = (select detalle_arqueo_id from venta where id = cod);
	update detalle_arqueo set estado=false where arqueo_id=v_arqueo and id = v_detalle_arqueo;
	OPEN v_cursor1;
		loop
		FETCH v_cursor1 INTO v_producto,v_cantidad,v_sucursal;
			EXIT WHEN NOT FOUND;
				update almacen set cantidad=cantidad+v_cantidad where producto_id=codigo and sucursal_id=v_sucursal;
		end loop;
		close v_cursor1;
		update venta set est_ven=est where cod_ven=cod;
		RETURN TRUE;
END
$function$
;


CREATE OR REPLACE FUNCTION public.compra_adicionar(cod_usuario bigint, cod_proveedor bigint, fec date, obs character varying,
subtot numeric(10,2), tot numeric(10,2), des numeric(10,2), gestion integer, suc integer,
productos int8[], precios numeric(10,2)[], cantidades integer[], descuentos numeric(10,2)[], subtotales numeric(10,2)[], totales numeric(10,2)[])
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE cod int8;
indice int:=1;
tam int:=array_length(productos,1);
cant_inicial int4;
cant_final int4;
monto float:=0;
BEGIN
	cod:=(select coalesce(max(cod_com),0)+1 from compra);
	insert into compra(cod_com,cod_per,cod_pro,fec_com,obs_com,subtot_com,tot_com,des_com,ges_gen,cod_suc) values(cod,cod_usuario,cod_proveedor,fec,obs,subtot,tot,des,gestion,suc);
	while indice<=tam
	loop
		insert into detallecompra(cod_com,cod_detcom,cod_pro,pre_detcom,can_detcom,des_detcom,subtot_detcom ,tot_detcom) values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice],subtotales[indice],totales[indice]);

		if exists (select * from almacen where producto_id=productos[indice] and sucursal_id=suc) then
			cant_inicial := (select cantidad from almacen where producto_id=productos[indice] and sucursal_id=suc);
			cant_final := cant_inicial + cantidades[indice];
			update almacen set cantidad=cantidad+cantidades[indice] where producto_id=productos[indice] and sucursal_id = suc;
		insert into historico_almacen(producto_id, sucursal_id, fecha, usuario_id, cantidad_inicial, cantidad_entrante, cantidad_final, tipo, observacion)
			VALUES(productos[indice], suc, now(),cod_usuario ,cant_inicial , cantidades[indice], cant_final, 1, 'Ingreso por compra de productos por sistema');
		else
			cant_inicial := 0;
			cant_final := cantidades[indice];
			insert into almacen(producto_id, sucursal_id, cantidad) values(productos[indice],suc, cantidades[i]);
			insert into historico_almacen(producto_id, sucursal_id, fecha, usuario_id, cantidad_inicial, cantidad_entrante, cantidad_final, tipo, observacion)
			VALUES(productos[indice], suc, now(),cod_usuario ,cant_inicial , cantidades[indice], cant_final, 1, 'Compra de productos por sistema, Primer registro.');
		end if;

		indice=indice+1;
	end loop;
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;CREATE OR REPLACE FUNCTION public.compra_eliminar(cod bigint, cod_usuario bigint)
 RETURNS int2
 LANGUAGE plpgsql
AS $function$
DECLARE codigo int8;
DECLARE v_cantidad int4;
declare suc int4;
declare cant_inicial int4;
declare cant_final int4;
DECLARE v_cursor CURSOR FOR select dc.cod_pro,dc.can_detcom ,c.cod_suc from detalle_compra dc inner join compra c on dc.cod_com = c.cod_com where c.cod_com=cod ;
DECLARE sql_result record;
BEGIN
	OPEN v_cursor;
		loop
		FETCH v_cursor INTO codigo,v_cantidad,suc;
			EXIT WHEN NOT FOUND;
			cant_inicial := (select cantidad from almacen where producto_id=codigo and sucursal_id=suc);
			cant_final := cant_inicial - v_cantidad ;
			if (cant_final < 0) then
				return -2;
			end if;
				update almacen set cantidad = cant_final where producto_id=codigo and sucursal_id=suc;
				insert into historico_almacen(producto_id, sucursal_id, fecha, usuario_id, cantidad_inicial, cantidad_entrante, cantidad_final, tipo, observacion)
			VALUES(codigo, suc, now(),cod_usuario ,cant_inicial , v_cantidad, cant_final, 2, 'Reversion de la compra');
		end loop;
		close v_cursor;
		update compra set est_com=false where cod_com=cod;
		RETURN 1;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

CREATE OR REPLACE FUNCTION public.venta_eliminar(cod bigint, user_id bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE v_producto int8;
DECLARE v_cantidad int4;
DECLARE v_sucursal int4;
DECLARE v_cursor1 CURSOR FOR select producto_id,cantidad,v.sucursal_id from detalle_venta dv inner join venta v on v.id = dv.venta_id where venta_id =cod ;
DECLARE v_arqueo int8;
DECLARE v_detalle_arqueo int2;
DECLARE cant_inicial int4;
DECLARE cant_final int4;
DECLARE REVERSION_VENTA int4:=2;
DECLARE numero_venta int8;
DECLARE total_venta numeric(10,2);
DECLARE forma_pago_venta int2;
begin
	v_arqueo = (SELECT coalesce(max(a.id),-1) FROM arqueo a where a.delegado_id=user_id and a.custodio_final_id is  null and a.estado = true);
--	v_arqueo = (select coalesce(max(arqueo_id),0) from venta where id = cod);
	v_detalle_arqueo = (select coalesce(max(detalle_arqueo_id),0) from detalle_arqueo where arqueo_id = v_arqueo);
	if(v_arqueo > 0 and v_detalle_arqueo > 0) then
	--	update detalle_arqueo set estado=false where arqueo_id=v_arqueo and id = v_detalle_arqueo;
		numero_venta = (select coalesce(max(numero),0) from venta where id=cod);
		total_venta = (select coalesce(max(total),0) from venta where id=cod);
		forma_pago_venta = (select coalesce(max(forma_pago_id),0) from venta where id=cod);
		INSERT INTO detalle_arqueo(arqueo_id, id, tipo, descripcion, monto, fecha, estado, forma_pago_id) VALUES(v_arqueo, v_detalle_arqueo, REVERSION_VENTA, concat('Reversion de venta #',numero_venta), total_venta, now(), false, form_pago_venta);
	end if;
	OPEN v_cursor1;
		loop
		FETCH v_cursor1 INTO v_producto,v_cantidad,v_sucursal;
			EXIT WHEN NOT FOUND;
			cant_inicial := (select cantidad from almacen where producto_id=v_producto and sucursal_id=v_sucursal);
			cant_final := cant_inicial + v_cantidad ;
			update almacen set cantidad = cant_final where producto_id = v_producto and sucursal_id = v_sucursal;
				insert into historico_almacen(producto_id, sucursal_id, fecha, usuario_id, cantidad_inicial, cantidad_entrante, cantidad_final, tipo, observacion)
			VALUES(v_producto, v_sucursal, now(),user_id ,cant_inicial , v_cantidad, cant_final, 4, 'Reversion de la venta');
		end loop;
		close v_cursor1;
		update venta set estado=false, updated_by = user_id, updated_at = now() where id=cod;
		RETURN TRUE;
END
$function$
;

CREATE OR REPLACE FUNCTION public.detalle_arqueo_adicionar(p_arqueo int8, p_tipo int2, p_descripcion character varying, p_monto numeric,p_forma_pago int2,p_es_debe boolean)
 RETURNS int2
 LANGUAGE plpgsql
AS $function$
DECLARE detalle_id int2;
BEGIN
		detalle_id:=(select COALESCE(max(id),0)+1 from detalle_arqueo where arqueo_id=p_arqueo);
		insert into detalle_arqueo(arqueo_id, id, tipo, descripcion, monto, fecha, estado, forma_pago_id, es_debe) values(p_arqueo,detalle_id,p_tipo,p_descripcion,p_monto,now(),true,p_forma_pago, p_es_debe);
	RETURN detalle_id;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN -1;
END
$function$
;

--SCRIPT para eliminar transacciones
delete from detalle_compra dc;
delete from compra ;
delete from detalle_venta ;
delete from detalle_historico_venta ;
delete from historico_venta ;
delete from venta;
delete from detalle_arqueo ;
delete from arqueo ;
delete from historico_almacen ;
delete from almacen ;
delete from detalle_cartilla_diaria ;
delete from cartilla_diaria ;




delete from dato where cod_per > 1;
delete from tiene_sucursal where cod_per > 1
delete from usurol where cod_per > 1;
delete from proveedor where cod_pro > 1;
delete from cliente where cod_cli > 1;
delete from secretaria where cod_sec > 1;
delete from persona where cod_per > 1;
