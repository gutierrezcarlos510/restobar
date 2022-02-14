package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Caracteristica;

import java.util.List;

public interface CaracteristicaS {
    List<Caracteristica> listAll(short tipo);

    Caracteristica obtener(Integer id);
}
