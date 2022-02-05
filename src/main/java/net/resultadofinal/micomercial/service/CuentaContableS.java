package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.contable.CuentaContable;
import net.resultadofinal.micomercial.model.contable.SubcuentaContable;
import net.resultadofinal.micomercial.util.DataResponse;

import java.util.List;

public interface CuentaContableS {

	List<SubcuentaContable> listarCajas(Boolean filtrarEsExterno);

	List<SubcuentaContable> listarBancos(Boolean filtrarEsExterno);

	List<SubcuentaContable> listarIngresoGeneral(Boolean filtrarEsExterno);

	List<SubcuentaContable> listarEgresoGeneral(Boolean filtrarEsExterno);

	List<SubcuentaContable> listarPasivosGeneral(Boolean filtrarEsExterno);

	List<SubcuentaContable> listarPrestamosBancarios(Boolean filtrarEsExterno);

	List<SubcuentaContable> listarActivosGenerales(Boolean filtrarEsExterno);

	int obtenerCuentaMueble();

	int obtenerCuentaInmueble();

	int obtenerCuentaCapital();

	int obtenerCuentaCajaDiaria();

	int obtenerCuentaVenta();

	int obtenerCuentaCompra();

	int obtenerCuentaInventario();

	int obtenerCuentaGananciaDineroCajaDiaria();

	int obtenerCuentaPerdidaDineroCajaDiaria();

	List<SubcuentaContable> listarEgresoOperacional(Boolean filtrarEsExterno);

	int obtenerCuentaSueldoSalario();

	int obtenerCuentaAlquiler();

	int obtenerCuentaEquipoComputacional();

	int obtenerCuentaServicioLimpieza();

	int obtenerCuentaMaterialOficina();

	int obtenerCuentaPerdidaProducto();

	int obtenerCuentaFleteTransporte();

	int obtenerCuentaServicioBasico();

	int obtenerCuentaDescuentoPorVenta();

	int obtenerCuentaDescuentoPorCompra();

	SubcuentaContable obtenerSubcuenta(Integer codSubcuenta);

	List<CuentaContable> listar(int start, boolean estado, String search, int length);

	List<SubcuentaContable> obtenerSubcuentas(Integer codCuenta);

	CuentaContable obtener(Integer codCuenta);

	List<SubcuentaContable> listarSubcuentas(int start, boolean estado, String search, int length, Integer codCuenta);

	DataResponse adicionarSubcuenta(SubcuentaContable obj);

	boolean existeCodigo(String code);

	DataResponse modificarSubcuenta(SubcuentaContable obj);

	DataResponse eliminar(Integer codSubcuenta);

}