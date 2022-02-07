package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Categoria;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CategoriaS {
    DataTableResults<Categoria> listado(HttpServletRequest request, boolean estado);

    List<Categoria> listAll();

    Categoria obtener(Integer id);

    @Transactional
    DataResponse adicionar(Categoria obj);

    @Transactional
    DataResponse modificar(Categoria t);

    @Transactional
    DataResponse darEstado(Integer id, boolean est);
}
