package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.DetalleVenta;
import net.resultadofinal.micomercial.model.Venta;
import net.resultadofinal.micomercial.model.form.VentaForm;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface VentaS {
    DataTableResults<Venta> listado(HttpServletRequest request, boolean estado, Long xuser, int xsucursal,Short tipo);

    Venta obtener(Long codVen);
    VentaForm obtenerVentaForm(Long codVen);
    List<DetalleVenta> obtenerDetalle(Long ventaId);

    @Transactional
    DataResponse adicionar(Venta v, Long productos[], Integer cantidades[], BigDecimal precios[], BigDecimal descuentos[], BigDecimal subtotales[], BigDecimal totales[]);

    @Transactional
    Boolean eliminar(Long ventaId, Long userId);

    Venta obtenerPorArqueoCaja(Long arqueoId, Integer detalleArqueoId);
    @Transactional
    DataResponse guardarComanda(VentaForm obj);
    @Transactional
    DataResponse actualizarComanda(VentaForm obj);
}
