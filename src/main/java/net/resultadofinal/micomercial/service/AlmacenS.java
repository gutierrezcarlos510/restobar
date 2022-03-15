package net.resultadofinal.micomercial.service;

import org.springframework.transaction.annotation.Transactional;

public interface AlmacenS {
    @Transactional
    boolean registrarAlmacen(Long productoId, Integer sucursalId, Integer cantidad, Long userId, Short tipo, String obs);
}
