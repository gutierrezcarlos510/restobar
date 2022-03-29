package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Arqueo;
import net.resultadofinal.micomercial.model.ArqueoTotal;
import net.resultadofinal.micomercial.model.DetalleArqueo;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.CompraVentaWrap;
import net.resultadofinal.micomercial.model.wrap.ResumenArqueoWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface ArqueoS {
	DataTableResults<Arqueo> listado(HttpServletRequest request, boolean estado, Long xuser, int xgestion, boolean estaActivoCaja);
	DataTableResults<DetalleArqueo> listadoDetalles(HttpServletRequest request, boolean estado,Long arqueo);
	Arqueo arqueocajaVerificarSesionActual(Long cod, Integer sucursal);
	Long obtenerArqueoSesionActiva(Long codPer, Integer sucursal);
	List<DetalleArqueo> obtenerDetallexArqueoCaja(Long cod);
	DetalleArqueo obtenerDetalle(Integer detalleArqueoId,Long arqueoId);
	DataResponse iniciar(Arqueo ac);
	Boolean modificar(Arqueo ac);
	Boolean cerrar(Arqueo ac);
	Boolean darEstado(Long cod, Boolean est, Long cod_per);
	BigDecimal obtenertotal(Long cod);
	Short adicionarDetalle(DetalleArqueo d);
	ArqueoTotal obtenerTotal(Long codArqCaj);
	boolean eliminarDetalle(Long codArqcaj, Integer codDetArq);
	Arqueo obtenerCaja(Long codArq);
	List<DetalleArqueo> obtenerDetallexArqueoCajaAgrupado(Long cod);
	CompraVentaWrap obtenerDescuentoVenta(Long codArqcaj);
	CompraVentaWrap obtenerDescuentoCompra(Long codArqcaj);
	CompraVentaWrap obtenerDescuentoVentaPorBanco(Long codArqcaj, Integer codSubcuenta);
	ResumenArqueoWrap obtenerResumenArqueo(Long codArqcaj);
	Arqueo obtenerUltimaCajaPorUsuario(Long codUsuario, Integer sucursal);
	Boolean rehabilitarArqueo(Long cod);
}
