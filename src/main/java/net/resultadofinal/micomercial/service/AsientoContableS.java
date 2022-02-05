package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.contable.AsientoContable;
import net.resultadofinal.micomercial.model.contable.CuentaContableGrupo;
import net.resultadofinal.micomercial.model.wrap.LibroMayorSubcuenta;
import net.resultadofinal.micomercial.util.DataResponse;

import java.util.List;
import java.util.Map;

public interface AsientoContableS {

	Long adicionar(AsientoContable obj, Integer subcuentas[], Float debes[], Float haberes[]) ;

	List<CuentaContableGrupo> listarSumaSaldo(Integer codSuc, Integer gesGen);

	List<Map<String, Object>> listarLibroDiario(int start, boolean estado, String search, int length, Integer gestion, Integer sucursal);

	DataResponse obtenerDetalles(Long cod);

	LibroMayorSubcuenta obtenerLibroMayorSubcuenta(Integer subcuenta, Integer gestion, Integer sucursal);

	Long adicionarAsientoProductoPerdido(AsientoContable obj, Integer subcuentas[], Float debes[], Float haberes[], Integer productos[],
                                         Integer cantidades[]);

}