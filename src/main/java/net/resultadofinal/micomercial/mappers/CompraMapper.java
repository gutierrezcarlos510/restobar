// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.Compra;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraMapper implements RowMapper<Compra> {
	@Override
	public Compra mapRow(ResultSet rs, int rowNum)throws SQLException{
		Compra compra = new Compra();
		compra.setCod_com(rs.getLong("cod_com")); 
		compra.setCod_per(rs.getLong("cod_per")); 
		compra.setCod_pro(rs.getLong("cod_pro")); 
		compra.setFec_com(rs.getString("fec_com")); 
		compra.setObs_com(rs.getString("obs_com")); 
		compra.setTot_com(rs.getFloat("tot_com")); 
		compra.setEst_com(rs.getBoolean("est_com")); 
		compra.setDes_com(rs.getFloat("des_com")); 
		compra.setGes_gen(rs.getInt("ges_gen")); 
		compra.setCod_arqcaj(rs.getLong("cod_arqcaj")); 
		compra.setCod_detarq(rs.getInt("cod_detarq")); 
		compra.setCod_suc(rs.getInt("cod_suc")); 
		compra.setSubtotCom(rs.getFloat("subtot_com")); 
		compra.setUsuario(rs.getString("usuario"));
		compra.setProveedor(rs.getString("proveedor"));
		compra.setFecha(rs.getString("fecha"));
		if(compra.hasColumn(rs, "rn")) {
			compra.setRn(rs.getLong("rn"));
		}
		if(compra.hasColumn(rs, "tot")) {
			compra.setTot(rs.getInt("tot"));
		}
		return compra;
}
}