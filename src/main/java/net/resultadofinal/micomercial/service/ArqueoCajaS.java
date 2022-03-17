package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.ArqueoCaja;
import net.resultadofinal.micomercial.model.ArqueoTotal;
import net.resultadofinal.micomercial.model.DetalleArqueo;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.CompraVentaWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ArqueoCajaS {
	DataTableResults<ArqueoCaja> listado(HttpServletRequest request, boolean estado, Long xuser, int xgestion);
	DataTableResults<DetalleArqueo> listadoDetalles(HttpServletRequest request, boolean estado,Long arqueo);
	ArqueoCaja arqueocaja_verificar_sesion_actual(Long cod, Integer sucursal);
	List<DetalleArqueo> obtenerDetallexArqueoCaja(Long cod);
	Map<String, Object> obtenerDetalle(Integer cod_detarq, Long cod_arqcaj);
	Long iniciar(ArqueoCaja ac);
	Boolean modificar(ArqueoCaja ac);
	Boolean cerrar(ArqueoCaja ac);
	Boolean darEstado(Long cod, Boolean est, Long cod_per);
	Float obtenertotal(Long cod);
	Integer adicionarDetalle(DetalleArqueo d);
	ArqueoTotal obtenerTotal(Long codArqCaj);
	boolean eliminarDetalle(Long codArqcaj, Integer codDetArq);
	boolean registrarArqueoAsiento(Long codArqueo, Long codAsiento);
	ArqueoCaja obtenerCaja(Long codArq);
	List<DetalleArqueo> obtenerDetallexArqueoCajaAgrupado(Long cod);
	CompraVentaWrap obtenerDescuentoVenta(Long codArqcaj);
	CompraVentaWrap obtenerDescuentoCompra(Long codArqcaj);
	CompraVentaWrap obtenerDescuentoVentaPorBanco(Long codArqcaj, Integer codSubcuenta);
	ArqueoWrap obtenerResumenArqueo(Long codArqcaj);
	ArqueoCaja obtenerUltimaCajaPorUsuario(Long codUsuario, Integer sucursal);
	public Boolean rehabilitarArqueo(Long cod);
}
