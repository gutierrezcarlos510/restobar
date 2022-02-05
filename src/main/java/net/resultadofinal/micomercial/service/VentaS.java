package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.DetalleVenta;
import net.resultadofinal.micomercial.model.Venta;

import java.util.List;

public interface VentaS {
	List<Venta> listar(int start, boolean estado, String search, int length, Long cod_per, String fini, String ffin, Integer gestion, Integer sucursal);
	Venta obtener(Long cod_ven);
	List<DetalleVenta> obtenerDetalle(Long cod_ven);
	Boolean adicionar(Venta v, Integer productos[], Integer cantidades[], Float precios[], Float descuentos[], Float subtotales[], Float totales[]);
	Boolean adicionarVentaSimple(Integer cod_pro,Long cod_per,Integer gestion,Float precio, Integer cod_suc);
	Boolean adicionarVentaSimpleManual(Integer cod_pro,Long cod_per,Integer gestion,Float precio,Integer cant,Float total, Integer cod_suc);
	Boolean eliminar(Long cod_ven,Boolean est);
	Venta obtenerPorArqueoCaja(Long codArqcaj, Integer codDet);
}
