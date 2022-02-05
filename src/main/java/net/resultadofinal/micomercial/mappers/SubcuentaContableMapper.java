// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.contable.SubcuentaContable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubcuentaContableMapper implements RowMapper<SubcuentaContable> {
	@Override
	public SubcuentaContable mapRow(ResultSet rs, int rowNum)throws SQLException{
		SubcuentaContable subcuentaContable = new SubcuentaContable();
		subcuentaContable.setCodCuenta(rs.getInt("cod_cuenta")); 
		subcuentaContable.setCodSubcuenta(rs.getInt("cod_subcuenta")); 
		subcuentaContable.setNombre(rs.getString("nombre")); 
		subcuentaContable.setDescripcion(rs.getString("descripcion")); 
		subcuentaContable.setEstado(rs.getBoolean("estado")); 
		subcuentaContable.setCodigo(rs.getString("codigo")); 
		subcuentaContable.setEsExterno(rs.getBoolean("es_externo"));
		if(subcuentaContable.hasColumn(rs, "rn")) {
			subcuentaContable.setRn(rs.getLong("rn"));
		}
		if(subcuentaContable.hasColumn(rs, "tot")) {
			subcuentaContable.setTot(rs.getInt("tot"));
		}
		return subcuentaContable;
}
}