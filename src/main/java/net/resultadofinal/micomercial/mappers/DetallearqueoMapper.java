// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.DetalleArqueo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DetallearqueoMapper implements RowMapper<DetalleArqueo> {
	@Override
	public DetalleArqueo mapRow(ResultSet rs, int rowNum) throws SQLException {
		//cod_arqcaj bigint ,cod_detarq integer ,tip_detarq integer ,des_detarq character varying(500) ,mon_detarq real ,fec_detarq timestamp
		DetalleArqueo detallearqueo = new DetalleArqueo();
		detallearqueo.setCod_arqcaj(rs.getLong("cod_arqcaj"));
		detallearqueo.setCod_detarq(rs.getInt("cod_detarq"));
		detallearqueo.setTip_detarq(rs.getInt("tip_detarq"));
		detallearqueo.setDes_detarq(rs.getString("des_detarq"));
		detallearqueo.setMon_detarq(rs.getFloat("mon_detarq"));
		Timestamp fec = rs.getTimestamp("fec_detarq");
		detallearqueo.setFec_detarq(fec!=null?""+fec.getTime():null);
		detallearqueo.setEst_detarq(rs.getBoolean("est_detarq"));
		detallearqueo.setCodSubcuenta(rs.getInt("cod_subcuenta"));
		if(detallearqueo.hasColumn(rs, "rn")) {
			detallearqueo.setRn(rs.getLong("rn"));
		}
		if(detallearqueo.hasColumn(rs, "tot")) {
			detallearqueo.setTot(rs.getInt("tot"));
		}
		if(detallearqueo.hasColumn(rs, "fecha")) {
			detallearqueo.setFecha(rs.getString("fecha"));
		}
		return detallearqueo;
	}
}