package net.resultadofinal.micomercial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Controller
@RequestMapping("/reporteAdministrativo/*")
public class ReporteAdministrativoC {
    @Autowired
    private DataSource dataSource;
    @RequestMapping("gestion")
    public String gestion(HttpServletRequest request, Model model){
        return "reporte-administracion/gestion";
    }
}
