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
		tipoproducto.setCod_tippro(rs.getInt("cod_tippro")); 
		tipoproducto.setNom_tippro(rs.getString("nom_tippro")); 
		tipoproducto.setDes_tippro(rs.getString("des_tippro")); 
		tipoproducto.setEst_tippro(rs.getBoolean("est_tippro")); 
		return tipoproducto;
}
}