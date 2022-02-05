package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Compra;
import net.resultadofinal.micomercial.model.wrap.HistorialCompraProducto;

import java.util.List;

public interface CompraS {
	List<Compra> listar(int start, boolean estado, String search, int length, Long cod_per, String fini, String ffin, Integer gestion);
	Compra obtener(Long cod_com);
	Boolean adicionar(Compra c, Integer productos[], Integer cantidades[], Float precios[], Float descuentos[], Float subtotales[], Float totales[]);
	Boolean eliminar(Long cod_com,Boolean est);
	Compra obtenerPorArqueoCaja(Long codArqcaj, Integer codDet);
	List<HistorialCompraProducto> obtenerHistorialCompraProducto(Integer codpro);
}
