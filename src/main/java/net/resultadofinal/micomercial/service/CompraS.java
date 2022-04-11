package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Compra;
import net.resultadofinal.micomercial.model.PagoCreditoCompra;
import net.resultadofinal.micomercial.model.wrap.HistorialCompraProducto;
import net.resultadofinal.micomercial.util.DataResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CompraS {
	List<Compra> listar(int start, boolean estado, String search, int length, Long cod_per, String fini, String ffin, Integer gestion,Short tipo);
	Compra obtener(Long cod_com);
	Boolean adicionar(Compra c, Long productos[], BigDecimal cantidades[], BigDecimal precios[], BigDecimal descuentos[], BigDecimal subtotales[],
					  BigDecimal totales[], Short tipos[]);
	Boolean eliminar(Long cod_com,Long user);
	Compra obtenerPorArqueoCaja(Long codArqcaj, Integer codDet);
	List<HistorialCompraProducto> obtenerHistorialCompraProducto(Integer codpro);
	DataResponse adicionarPago(PagoCreditoCompra obj);
	List<PagoCreditoCompra> listarPagosCompraCredito(Long id);
	DataResponse eliminarPago(PagoCreditoCompra obj);
}
