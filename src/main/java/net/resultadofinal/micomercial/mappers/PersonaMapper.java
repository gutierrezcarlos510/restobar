// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.Persona;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonaMapper implements RowMapper<Persona> {
	@Override
	public Persona mapRow(ResultSet rs, int rowNum)throws SQLException{
		Persona persona = new Persona();
		persona.setCi_per(rs.getString("ci_per")); 
		persona.setNom_per(rs.getString("nom_per")); 
		persona.setPriape_per(rs.getString("priape_per")); 
		persona.setSegape_per(rs.getString("segape_per")); 
		persona.setTel_per(rs.getString("tel_per")); 
		persona.setDir_per(rs.getString("dir_per")); 
		persona.setEma_per(rs.getString("ema_per")); 
		persona.setFot_per(rs.getString("fot_per")); 
		persona.setEst_per(rs.getBoolean("est_per")); 
		persona.setSex_per(rs.getBoolean("sex_per")); 
		persona.setCod_per(rs.getLong("cod_per")); 
		persona.setCodbio_per(rs.getString("codbio_per")); 
		persona.setCodesc_per(rs.getString("codesc_per")); 
		return persona;
}
}