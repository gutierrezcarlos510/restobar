CREATE OR REPLACE FUNCTION public.compra_adicionar(cod_usuario bigint, cod_proveedor bigint, fec date, obs character varying, subtot double precision, tot double precision, des double precision, gestion integer, suc integer, productos integer[], precios double precision[], cantidades integer[], descuentos double precision[], subtotales double precision[], totales double precision[])
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE cod int;
indice int:=1;
tam int:=array_length(productos,1);
--code_arqcaj bigint;
--code_detarq int;
monto float:=0;
BEGIN
	--code_arqcaj:=(select arqueocaja_obtener_sesion(cod_usuario));
	--code_detarq:=(select detallearqueo_adicionar(code_arqcaj,4, cast('Por compra de productos.' as varchar), tot));
	cod:=(select coalesce(max(cod_com),0)+1 from compra);
	insert into compra(cod_com,cod_per,cod_pro,fec_com,obs_com,subtot_com,tot_com,des_com,ges_gen,cod_suc) values(cod,cod_usuario,cod_proveedor,fec,obs,subtot,tot,des,gestion,suc);
	while indice<=tam
	loop
		insert into detallecompra(cod_com,cod_detcom,cod_pro,pre_detcom,can_detcom,des_detcom,subtot_detcom ,tot_detcom) values(cod,indice,productos[indice],precios[indice],cantidades[indice],descuentos[indice],subtotales[indice],totales[indice]);
		update producto set can_pro=can_pro+cantidades[indice],precom_pro=precios[indice],preven_pro=round(cast((precios[indice]+gan_pro) as decimal),2) where cod_pro=productos[indice];
		indice=indice+1;
	end loop;
	RETURN cod;
	EXCEPTION
			WHEN OTHERS THEN
				RAISE EXCEPTION '%',sqlerrm;
	RETURN cod;
END
$function$
;
ALTER TABLE public.compra ADD cod_arqcaj int8 NULL;

ALTER TABLE public.compra ADD cod_detarq int4 NULL;
