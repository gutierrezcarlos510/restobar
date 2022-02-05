CREATE OR REPLACE FUNCTION public.secretaria_adicionar(codigo bigint, fecha date, sueldo double precision,xrol int)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
BEGIN
	insert into secretaria(cod_sec,fini_sec,sue_sec) values(codigo,fecha,sueldo);
	if not EXISTS(select * from usurol where cod_per=codigo and cod_rol=xrol and fecha_baja is null)THEN
		insert into usurol(cod_per,cod_rol) values(codigo,xrol);
	end if;
	RETURN codigo;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
				RETURN -1;
END
$function$
;
ALTER TABLE public.sucursal ADD telefono varchar(50) NULL;
ALTER TABLE public.sucursal ADD direccion varchar(200) NULL;
ALTER TABLE public.sucursal ADD alias varchar(15) NULL;
ALTER TABLE public.sucursal ADD estado_notificacion bool NULL;
ALTER TABLE public.sucursal ADD titulo_notificacion varchar(50) NULL;
ALTER TABLE public.sucursal ADD mensaje_notificacion varchar(1000) NULL;
update sucursal set estado_notificacion=false;