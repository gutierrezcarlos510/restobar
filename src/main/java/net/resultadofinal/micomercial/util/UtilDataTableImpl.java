/**
 * 
 */
package net.resultadofinal.micomercial.util;


import com.google.gson.GsonBuilder;
import net.resultadofinal.micomercial.pagination.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author CARLOS
 * Administra las funciones de datatable
 */
@Service
public class UtilDataTableImpl implements UtilDataTableS {
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Override
	public String toJson(DataTableResults<?> value) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();
		return gsonBuilder.create().toJson(value);
	}
	@Override
	public <T> DataTableResults<T> list(HttpServletRequest request,Class<T> clazz,SqlBuilder sql){
		DataTableRequest<T> dataTableInRQ = new DataTableRequest<T>(request);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		Long tam = db.queryForObject(sql.generateCount(), Long.class);
		Long tamFiltered = db.queryForObject(AppUtil.buildCountQuery(sql.generate(""),pagination), Long.class);
		String paginatedQuery = AppUtil.buildPaginatedQuery(sql.generate(""), pagination);
//		System.out.println("paginateQUERY->"+paginatedQuery);
		List<T> dataList = db.query(paginatedQuery, new BeanPropertyRowMapper<>(clazz));
		DataTableResults<T> dataTableResult = new DataTableResults<T>();
		dataTableResult.setDraw(dataTableInRQ.getDraw());
		dataTableResult.setData(dataList);
		if (dataList!=null && !dataList.isEmpty()) {
			dataTableResult.setRecordsTotal(tam.toString());
			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
				dataTableResult.setRecordsFiltered(tam.toString());
			} else {
				dataTableResult.setRecordsFiltered(tamFiltered.toString());
			}
		}else {
			dataTableResult.setRecordsFiltered("0");
			dataTableResult.setRecordsTotal("0");
		}
		return dataTableResult;
	}
	
}
