package net.resultadofinal.micomercial;

import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

//import com.viva.manager.Utiles.Constantes;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        //Url para avatars
        String resourcePath = Paths.get(MyConstant.RUTA_AVATAR).toAbsolutePath().toUri().toString();
        registry.addResourceHandler(MyConstant.URL_PATH_AVATAR + "**").addResourceLocations(resourcePath);
        //Url para logo de empresa
        resourcePath = Paths.get(MyConstant.RUTA_GENERAL).toAbsolutePath().toUri().toString();
        registry.addResourceHandler(MyConstant.URL_PATH_GENERAL + "**").addResourceLocations(resourcePath);
        resourcePath = Paths.get(MyConstant.RUTA_CARRERA).toAbsolutePath().toUri().toString();
        registry.addResourceHandler(MyConstant.URL_PATH_CARRERA + "**").addResourceLocations(resourcePath);
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
