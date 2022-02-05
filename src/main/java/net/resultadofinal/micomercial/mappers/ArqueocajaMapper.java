// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.ArqueoCaja;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArqueocajaMapper implements RowMapper<ArqueoCaja> {
	@Override
	public ArqueoCaja mapRow(ResultSet rs, int rowNum)throws SQLException{
		ArqueoCaja arqueocaja = new ArqueoCaja();
		arqueocaja.setCod_arqcaj(rs.getLong("cod_arqcaj")); 
		arqueocaja.setDel_arqcaj(rs.getLong("del_arqcaj")); 
		arqueocaja.setCusini_arqcaj(rs.getLong("cusini_arqcaj")); 
		arqueocaja.setFini_arqcaj(rs.getString("fini_arqcaj")); 
		arqueocaja.setMonini_arqcaj(rs.getFloat("monini_arqcaj")); 
		arqueocaja.setFfin_arqcaj(rs.getString("ffin_arqcaj")); 
		arqueocaja.setMonfin_arqcaj(rs.getFloat("monfin_arqcaj")); 
		arqueocaja.setEst_arqcaj(rs.getBoolean("est_arqcaj")); 
		arqueocaja.setMonrea_arqcaj(rs.getFloat("monrea_arqcaj")); 
		arqueocaja.setGes_gen(rs.getInt("ges_gen")); 
		arqueocaja.setDes_arqcaj(rs.getString("des_arqcaj")); 
		arqueocaja.setCusfin_arqcaj(rs.getLong("cusfin_arqcaj")); 
		arqueocaja.setCod_suc(rs.getInt("cod_suc")); 
		arqueocaja.setCodAsiento(rs.getLong("cod_asiento")); 
		return arqueocaja;
}
}