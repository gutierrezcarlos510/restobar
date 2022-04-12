package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.GeneralWrap;
import net.resultadofinal.micomercial.model.NotificacionSucursalWrap;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.model.wrap.Organigrama;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface SucursalS {
	Sucursal obtener(Integer cod_suc);
	DataResponse adicionar(Sucursal s);
	DataResponse modificar(Sucursal s);
	Boolean darEstado(Integer cod_suc,Boolean estado);
	Sucursal getSucursalNow(List<GeneralWrap> generalWrapList, General general);
	List<Sucursal> obtenerPorUsuario(Long codUsu);
	void asignarSucursal(Long codUsu,Integer sucursales[]);
	void adicionarNotificaciones(String titulo, String mensaje, Integer[] sucursales);

	void adicionarNotificacion(String titulo, String mensaje, Integer sucursal);

	void eliminarNotificaciones(Integer sucursales[]);

	void eliminarNotificacion(Integer sucursal);

	List<NotificacionSucursalWrap> listarNotificaciones();

	NotificacionSucursalWrap obtenerNotificacionPorSucursal(Integer sucursal);
	DataTableResults<Sucursal> listado(HttpServletRequest request, boolean estado);
	List<Sucursal> listAll();
	Organigrama listarOrganigramaUsuarios(Integer sucursalId);
}
