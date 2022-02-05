/**
 * 
 */
package net.resultadofinal.micomercial.util;


import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CARLOS
 * Controla el utilitario de Datatable
 */
public interface UtilDataTableS {
	/**
	 * Convierte a json Datatable para obtener en el Front
	 * @param value .- objeto Datatable para convertirlo en json tipo string
	 * @return String del JSON
	 */
	public String toJson(DataTableResults<?> value);
	/**
	 * Obtiene el objeto con los datos en el formato Datatable
	 * @param <T> .- Datatable del tipo de objeto que se quiere recuperar la tabla
	 * @param request .- Request que tiene los datos del datatable
	 * @param clazz .- Clase del objeto que se quiere recuperar
	 * @param sql .- Objeto SQL que tiene los datos de la consulta nativa
	 * @return DataTableResults<T>
	 */
	public <T> DataTableResults<T> list(HttpServletRequest request, Class<T> clazz, SqlBuilder sql);
}
