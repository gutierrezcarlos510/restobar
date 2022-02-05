// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.Venta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaMapper implements RowMapper<Venta> {
	@Override
	public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
		Venta venta = new Venta();
		venta.setCodVen(rs.getLong("cod_ven"));
		venta.setCodPer(rs.getLong("cod_per"));
		venta.setCodCli(rs.getLong("cod_cli"));
		venta.setFecVen(rs.getString("fec_ven"));
		venta.setObsVen(rs.getString("obs_ven"));
		venta.setTotVen(rs.getFloat("tot_ven"));
		venta.setDesVen(rs.getFloat("des_ven"));
		venta.setGesGen(rs.getInt("ges_gen"));
		venta.setEstVen(rs.getBoolean("est_ven"));
		venta.setCodArqcaj(rs.getLong("cod_arqcaj"));
		venta.setCodDetarq(rs.getInt("cod_detarq"));
		venta.setTipVen(rs.getInt("tip_ven"));
		venta.setCodSuc(rs.getInt("cod_suc"));
		venta.setSubtotVen(rs.getFloat("subtot_ven"));
		venta.setUsuario(rs.getString("usuario"));
		venta.setCliente(rs.getString("cliente"));
		venta.setFecha(rs.getString("fecha"));
		venta.setCodSubcuenta(rs.getInt("cod_subcuenta"));
		if(venta.hasColumn(rs, "rn")) {
			venta.setRn(rs.getLong("rn"));
		}
		if(venta.hasColumn(rs, "tot")) {
			venta.setTot(rs.getInt("tot"));
		}
		return venta;
	}
}