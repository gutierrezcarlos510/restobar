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

CREATE OR REPLACE FUNCTION almacen_adicionar(xproducto bigint, xsucursal integer, cant integer, xuser bigint,xtipo int2,xobs varchar(50))
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
declare inicial integer;
begin
	if exists (select * from almacen where producto_id=xproducto and sucursal_id = xsucursal) then
		inicial := (select cantidad from almacen where producto_id=xproducto and sucursal_id = xsucursal);
		update almacen set cantidad = cant + cantidad where producto_id=xproducto and sucursal_id = xsucursal;
		insert into historico_almacen(producto_id, sucursal_id, fecha, usuario_id, cantidad_inicial, cantidad_entrante, cantidad_final, tipo, observacion)
		values(xproducto, xsucursal, now(),xuser,inicial,cant,(inicial + cant), xtipo, xobs);
	else
		insert into almacen(producto_id, sucursal_id, cantidad) values(xproducto, xsucursal, cant);
		inicial := 0;
		insert into historico_almacen(producto_id, sucursal_id, fecha, usuario_id, cantidad_inicial, cantidad_entrante, cantidad_final, tipo, observacion)
		values(xproducto, xsucursal, now(),xuser,inicial,cant,(inicial + cant), xtipo, xobs);
	end if;
	RETURN true;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN false;
END
$function$
;
