package net.resultadofinal.micomercial.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorGeneralController implements ErrorController {

@RequestMapping(value = "error", method = RequestMethod.GET)
public String handleError(HttpServletRequest httpRequest, Model model) {
    String errorMsg = "";
    int httpErrorCode = getErrorCode(httpRequest);

    switch (httpErrorCode) {
        case 400: {
            errorMsg = "Http Error: 400. Peticion incorrecta";
            break;
        }
        case 401: {
            errorMsg = "Http Error: 401. Sin permiso";
            break;
        }
        case 404: {
            errorMsg = "Http Error: 404. Pagina no encontrada";
            break;
        }
        case 500: {
            errorMsg = "Http Error: 500. Error interno del servidor";
            break;
        }
    }
    model.addAttribute("errorMsg", errorMsg);
    return "error/404";
}

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}