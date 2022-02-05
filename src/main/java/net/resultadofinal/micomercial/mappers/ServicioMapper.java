// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.Servicio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioMapper implements RowMapper<Servicio> {
	@Override
	public Servicio mapRow(ResultSet rs, int rowNum)throws SQLException{
		Servicio servicio = new Servicio();
		servicio.setCod_ser(rs.getInt("cod_ser")); 
		servicio.setNom_ser(rs.getString("nom_ser")); 
		servicio.setDes_ser(rs.getString("des_ser")); 
		servicio.setEst_ser(rs.getBoolean("est_ser")); 
		servicio.setCod_tipser(rs.getInt("cod_tipser")); 
		servicio.setPre_ser(rs.getFloat("pre_ser")); 
		servicio.setPorcentaje_multa(rs.getFloat("porcentaje_multa")); 
		servicio.setPorcentaje_ayudante(rs.getFloat("porcentaje_ayudante")); 
		return servicio;
}
}