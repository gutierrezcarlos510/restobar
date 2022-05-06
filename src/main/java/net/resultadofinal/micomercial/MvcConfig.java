package net.resultadofinal.micomercial;

import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

//import com.viva.manager.Utiles.Constantes;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    Environment environment;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        String directoryFile = environment.getProperty("url");
        System.out.println(directoryFile);
        String resourcePath = Paths.get(MyConstant.RUTA_AVATAR).toAbsolutePath().toUri().toString();
        registry.addResourceHandler(MyConstant.URL_PATH_AVATAR + "**").addResourceLocations(resourcePath);
        //Url para logo de empresa
        String resourcePathProducto = Paths.get(MyConstant.Archivo.RUTA_PRODUCTO).toAbsolutePath().toUri().toString();
        registry.addResourceHandler(MyConstant.Archivo.DIR_VIEW_PRODUCTOS + "**").addResourceLocations(resourcePathProducto);

    }

    /**
     * Controlador y vistas particulares
     */
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/error_403").setViewName("/error/403");
        registry.addViewController("/").setViewName("redirect:principal/login");
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
