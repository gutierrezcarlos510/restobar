package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.DetalleVenta;
import net.resultadofinal.micomercial.model.Venta;
import net.resultadofinal.micomercial.model.form.VentaForm;
import net.resultadofinal.micomercial.model.wrap.VentaInfoWrap;
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
    Boolean eliminar(Long ventaId, Long userId);

    Venta obtenerPorArqueoCaja(Long arqueoId, Integer detalleArqueoId);
    @Transactional
    DataResponse guardarComanda(VentaForm obj);
    @Transactional
    DataResponse actualizarComanda(VentaForm obj);
    VentaInfoWrap obtenerVentaInfo(Long codVen);
}
