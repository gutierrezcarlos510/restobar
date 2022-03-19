/**
 * 
 */
package net.resultadofinal.micomercial.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CARLOS GUTIERREZ
 * Objeto que contiene todo una consulta sql
 */
public class SqlBuilder {
	private String select;
	private String from;
	private List<String> join;
	private List<String> leftJoin;
	private String where;
	private String groupBy;
	private String having;
	private String orderBy;
	private List<SqlParameter> parameters;
	
	public SqlBuilder() {
		parameters=new ArrayList<>();
	}
	/**
	 * Tabla del from
	 * @param fromTable
	 */
	public SqlBuilder(String fromTable) {
		this.from =fromTable;
		parameters=new ArrayList<>();
	}
	public List<String> getLeftJoin() {
		return leftJoin;
	}
	public void setLeftJoin(List<String> leftJoin) {
		this.leftJoin = leftJoin;
	}
	public List<SqlParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<SqlParameter> parameters) {
		this.parameters = parameters;
	}
	/**
	 * Ingrese etiqueta a para reemplazar con el value
	 * (Ejm: select from :tabla) -> etiqueta=tabla y value=persona
	 * Respuesta: select from persona
	 * @param etiqueta
	 * @param value
	 */
	public void addParameter(String etiqueta,String value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public void addParameter(String etiqueta,Float value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public void addParameter(String etiqueta,Double value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public void addParameter(String etiqueta,Long value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public void addParameter(String etiqueta,Integer value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public void addParameter(String etiqueta,Boolean value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public void addParameter(String etiqueta,Short value) {
		parameters.add(new SqlParameter(etiqueta,value));
	}
	public String getSelect() {
		return select;
	}
	/**
	 * Cadena que contiene las columnas que se obtendran de la consulta. Ejemplo: SELECT {{select}} FROM TABLE
	 * @param select
	 */
	public void setSelect(String select) {
		this.select = select;
	}
	public void setSelectConcat(String select) {
		this.select = this.select.concat(select);
	}
	public String getFrom() {
		return from;
	}
	/**
	 * Cadena que contiene las tablas de donde se realiza la consulta. Ejemplo: SELECT column1, column2, column3 FROM {{from}}
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	public List<String> getJoin() {
		return join;
	}
	/**
	 * Lista de string con los diferentes join de la consulta sql, omitir JOIN en la cadena, ya que se autocomplementa
	 * @param join
	 */
	public void setJoin(List<String> join) {
		this.join = join;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}
	
	public String getGroupBy() {
		return groupBy;
	}
	/**
	 * Cadena con las agrupaciones que se realizan en la consulta sql, omitir el GROUP BY en la cadena ya que se autocomplementa
	 * @param groupBy
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getHaving() {
		return having;
	}
	/**
	 * Cadena con la condicional del havinng, omitir el HAVING en la cadena ya que se autocomplementa
	 * @param having
	 */
	public void setHaving(String having) {
		this.having = having;
	}

	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * Cadena con el orden en el listado, omitir el ORDER BY en la cadena ya que se autocomplementa
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * Adicion de un join particular
	 * @param value
	 */
	public void addJoin(String value) {
		if (join == null)
			join = new ArrayList<>();
		join.add(value);
	}
	public void addLeftJoin(String value) {
		if (leftJoin == null)
			leftJoin = new ArrayList<>();
		leftJoin.add(value);
	}
	/**
	 * Obtiene String, generado de la consulta de todos los atributos obtenidos
	 * @param includeSelect, parametro especial que se incluye en los atributos que se recuperan de la consulta SQL
	 * @return
	 */
	public String generate(String includeSelect) {
		String sql = "";
		if (select != null) {
			sql += "select " + select;
			if(includeSelect!=null && !includeSelect.isEmpty())
				sql+=","+includeSelect;
			if (from != null) {
				sql += " from " + from;
				if (join != null && !join.isEmpty()) {
					for (String item : join) {
						sql += " join " + item;
					}
				}
				if (leftJoin != null && !leftJoin.isEmpty()) {
					for (String item : leftJoin) {
						sql += " left join " + item;
					}
				}
				if (where != null && !where.isEmpty()) {
					sql += " where " + where;
				}
				if (groupBy != null && !groupBy.isEmpty()) {
					sql += " group by " + groupBy;
					if (having != null && !having.isEmpty()) {
						sql = " having " + having;
					}
				}
				if (orderBy != null && !orderBy.isEmpty()) {
					sql += " order by " + orderBy;
				}
			} else
				return "";
		}
		
		return replaceParameters(sql);
	}
	public String generate() {
		String sql = "";
		if (select != null) {
			sql += "select " + select;
			if (from != null) {
				sql += " from " + from;
				if (join != null && !join.isEmpty()) {
					for (String item : join) {
						sql += " join " + item;
					}
				}
				if (leftJoin != null && !leftJoin.isEmpty()) {
					for (String item : leftJoin) {
						sql += " left join " + item;
					}
				}
				if (where != null && !where.isEmpty()) {
					sql += " where " + where;
				}
				if (groupBy != null && !groupBy.isEmpty()) {
					sql += " group by " + groupBy;
					if (having != null && !having.isEmpty()) {
						sql = " having " + having;
					}
				}
				if (orderBy != null && !orderBy.isEmpty()) {
					sql += " order by " + orderBy;
				}
			} else
				return "";
		}
		
		return replaceParameters(sql);
	}
	/**
	 * Obtiene String de la consulta generada, la consulta recupera un COUNT
	 * @return
	 */
	public String generateCount() {
		String sql = "";
		if (from != null) {
			sql += "select count(*) from " + from;
			if (join != null && !join.isEmpty()) {
				for (String item : join) {
					sql += " join " + item;
				}
			}
			if (leftJoin != null && !leftJoin.isEmpty()) {
				for (String item : leftJoin) {
					sql += " left join " + item;
				}
			}
			if (where != null && !where.isEmpty()) {
				sql += " where " + where;
			}
			if (groupBy != null && !groupBy.isEmpty()) {
				sql += " group by " + groupBy;
				if (having != null && !having.isEmpty()) {
					sql = " having " + having;
				}
			}
		} else
			return "";
		return replaceParameters(sql);
	}
	public String replaceParameters(String sql) {
		if(parameters!=null && !parameters.isEmpty()) {
			for (SqlParameter item : parameters) {
				if(item.getEtiqueta().indexOf(":")==0)
					sql = sql.replace(item.getEtiqueta(), (item.getValue()==null?"":item.getValue()));
				else
					sql = sql.replace(":"+item.getEtiqueta(), (item.getValue()==null?"":item.getValue()));
			}
		}
		return sql;
	}
}
