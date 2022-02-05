// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.TipoServicio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TiposervicioMapper implements RowMapper<TipoServicio> {
	@Override
	public TipoServicio mapRow(ResultSet rs, int rowNum)throws SQLException{
		TipoServicio tiposervicio = new TipoServicio();
		tiposervicio.setCod_tipser(rs.getInt("cod_tipser")); 
		tiposervicio.setNom_tipser(rs.getString("nom_tipser")); 
		tiposervicio.setDes_tipser(rs.getString("des_tipser")); 
		tiposervicio.setEst_tipser(rs.getBoolean("est_tipser")); 
		return tiposervicio;
}
}