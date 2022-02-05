// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.contable.CuentaContable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaContableMapper implements RowMapper<CuentaContable> {
	@Override
	public CuentaContable mapRow(ResultSet rs, int rowNum)throws SQLException{
		CuentaContable cuentaContable = new CuentaContable();
		cuentaContable.setTipoCuenta(rs.getInt("tipo_cuenta")); 
		cuentaContable.setNombre(rs.getString("nombre")); 
		cuentaContable.setDescripcion(rs.getString("descripcion")); 
		cuentaContable.setCodigo(rs.getString("codigo")); 
		cuentaContable.setTipoGrupo(rs.getInt("tipo_grupo")); 
		cuentaContable.setCodCuenta(rs.getInt("cod_cuenta")); 
		cuentaContable.setEstado(rs.getBoolean("estado")); 
		if(cuentaContable.hasColumn(rs, "rn")) {
			cuentaContable.setRn(rs.getLong("rn"));
		}
		if(cuentaContable.hasColumn(rs, "tot")) {
			cuentaContable.setTot(rs.getInt("tot"));
		}
		return cuentaContable;
}
}