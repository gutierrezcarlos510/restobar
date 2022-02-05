// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.contable.AsientoContable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AsientoContableMapper implements RowMapper<AsientoContable> {
	@Override
	public AsientoContable mapRow(ResultSet rs, int rowNum)throws SQLException{
		AsientoContable asientoContable = new AsientoContable();
		asientoContable.setCodAsiento(rs.getLong("cod_asiento")); 
		asientoContable.setNumero(rs.getInt("numero")); 
		asientoContable.setFecha(rs.getDate("fecha")); 
		asientoContable.setConcepto(rs.getString("concepto")); 
		asientoContable.setEstado(rs.getBoolean("estado")); 
		asientoContable.setCreatedBy(rs.getString("created_by")); 
		asientoContable.setCreatedAt(rs.getTimestamp("created_at")); 
		asientoContable.setGesGen(rs.getInt("ges_gen")); 
		asientoContable.setCodSuc(rs.getInt("cod_suc")); 
		return asientoContable;
}
}