package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reporteAdministrativo/*")
public class ReporteAdministrativoC {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private SucursalS sucursalS;
    @RequestMapping("gestion")
    public String gestion(HttpServletRequest request, Model model){
        model.addAttribute("sucursales", sucursalS.listAll());
        return "reporte-administracion/gestion";
    }
    @RequestMapping("compra")
    public void plantilla1(HttpServletRequest request, HttpServletResponse response, Integer tipo, String fini, String ffin,String tipoImpresion,Long userId, Integer sucursalId) {
        String nameReport="",nameFile, reportUrl;

        Map<String, Object> parametros = new HashMap<String, Object>();
        switch (tipo) {
            case 1:
                nameReport="informe_compra";
                break;
            case 2:
                nameReport="informe_venta";
                break;
            default:
                break;
        }
        if(userId > -1L) {
            nameReport = "_usuario";
            parametros.put("userId", userId);
        }
        if(!nameReport.isEmpty()) {
            try {
                nameFile = nameReport+"_"+fini+"_"+ffin;
                reportUrl="/Reportes/"+nameReport+".jasper";
                parametros.put("fini", fini);
                parametros.put("ffin", ffin);
                obtenerEncabezado(request, parametros, sucursalId);
                GeneradorReportes g = new GeneradorReportes();
                g.generarReporte(response, getClass().getResource(reportUrl),tipoImpresion, parametros, dataSource.getConnection(), nameFile, "inline");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void obtenerEncabezado(HttpServletRequest request,Map<String, Object> param, Integer sucursalId) {
        Persona user = (Persona)request.getSession().getAttribute(MyConstant.Session.USER);
        General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
        if(user != null && gestion != null) {
            Sucursal sucursal = sucursalS.obtener(sucursalId);
            param.put("empresa",sucursal.getNombre());
            param.put("usuario", user.getNom_per()+" "+user.getPriape_per());
            param.put("codSuc", sucursal.getCod_suc());
            param.put("gestion", gestion.getGes_gen());
            if(sucursal!=null) {
                param.put("sucursal", sucursal.getNombre());
                param.put("telefono", sucursal.getTelefono());
                param.put("direccion", sucursal.getDireccion());
            }
        }
    }
}
