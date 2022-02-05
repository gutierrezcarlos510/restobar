// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.TipoProducto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoproductoMapper implements RowMapper<TipoProducto> {
	@Override
	public TipoProducto mapRow(ResultSet rs, int rowNum)throws SQLException{
		TipoProducto tipoproducto = new TipoProducto();
		tipoproducto.setId(rs.getInt("id"));
		tipoproducto.setNombre(rs.getString("nombre"));
		tipoproducto.setDescripcion(rs.getString("descripcion"));
		tipoproducto.setEstado(rs.getBoolean("estado"));
		return tipoproducto;
}
}