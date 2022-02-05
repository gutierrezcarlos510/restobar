package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/reporte/*")
public class ReporteC {
	@Autowired
	private DataSource dataSource;
	private static final Logger logger = LoggerFactory.getLogger(ReporteC.class);
	@Autowired
	private SucursalS sucursalS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "reporte/gestion";
	}
	@RequestMapping("gestionResfin")
	public String gestionResfin(HttpServletRequest request,Model model){
		return "reporte/gestion-resfin";
	}
	@RequestMapping("reporte1")
	public void plantilla1(HttpServletRequest request,HttpServletResponse response,Integer tipo,String fini,String ffin) {
		String nameReport="",nameFile, typeReport, reportUrl;
		
		Map<String, Object> parametros;
		switch (tipo) {
		case 1:
			nameReport="informe_venta";
			break;
		case 2:
			nameReport="informe_compra";
			break;
		default:
			break;
		}

		if(!nameReport.isEmpty()) {
			try {
				nameFile = nameReport+"_"+fini+"_"+ffin;
				typeReport ="pdf";
				reportUrl="/Reportes/"+nameReport+".jasper";
				parametros = new HashMap<String, Object>();
				parametros.put("fini", fini);
				parametros.put("ffin", ffin);
				obtenerEncabezado(request, parametros);
				GeneradorReportes g = new GeneradorReportes();
				g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(), nameFile, "inline");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("plantilla1 : "+e.toString());
			}
		}
	}
	@RequestMapping("reporte2")
	public void plantilla2(HttpServletRequest request,HttpServletResponse response,Integer tipo,String fini,String ffin) {
		String nameReport="",nameFile, typeReport, reportUrl;
		
		Map<String, Object> parametros;
		switch (tipo) {
		case 1:
			nameReport="informe_prestacion";
			break;
		default:
			break;
		}

		if(!nameReport.isEmpty()) {
			try {
				nameFile = nameReport+"_"+fini+"_"+ffin;
				typeReport ="pdf";
				reportUrl="/Reportes/"+nameReport+".jasper";
				String SubRep=getClass().getResource("/Reportes/informe_prestacion.jasper").toString();
				parametros = new HashMap<String, Object>();
				parametros.put("subrep",SubRep.substring(0, SubRep.lastIndexOf("/"))+"/");
				parametros.put("fini", fini);
				parametros.put("ffin", ffin);
				obtenerEncabezado(request, parametros);
				GeneradorReportes g = new GeneradorReportes();
				g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(), nameFile, "inline");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("plantilla1 : "+e.toString());
			}
		}
	}
	public void obtenerEncabezado(HttpServletRequest request,Map<String, Object> param) {
		Persona user = (Persona)request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if(user != null && gestion != null) {
			Sucursal sucursal = sucursalS.obtener(gestion.getCod_suc());
			param.put("empresa",gestion.getNom_gen());
			param.put("usuario", user.getNom_per()+" "+user.getPriape_per());
			param.put("codSuc", sucursal.getCod_suc());
			param.put("gestion", gestion.getGes_gen());
			if(sucursal!=null) {
				param.put("sucursal", sucursal.getNombre());
				param.put("telefono", gestion.getTel_gen());
				param.put("direccion", gestion.getDir_gen());
			}
		}
	}
}
