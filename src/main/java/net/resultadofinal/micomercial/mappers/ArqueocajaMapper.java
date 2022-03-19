// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.Arqueo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArqueocajaMapper implements RowMapper<Arqueo> {
	@Override
	public Arqueo mapRow(ResultSet rs, int rowNum)throws SQLException{
		Arqueo arqueocaja = new Arqueo();
		arqueocaja.setId(rs.getLong("id"));
		arqueocaja.setDelegadoId(rs.getLong("delegado_id"));
		arqueocaja.setCustodioInicialId(rs.getLong("custodio_inicial_id"));
		arqueocaja.setFinicio(rs.getTimestamp("finicio"));
		arqueocaja.setMontoInicial(rs.getBigDecimal("monto_inicial"));
		arqueocaja.setFfin(rs.getTimestamp("ffin"));
		arqueocaja.setMontoFinal(rs.getBigDecimal("monto_final"));
		arqueocaja.setEstado(rs.getBoolean("estado"));
		arqueocaja.setMontoReal(rs.getBigDecimal("monto_real"));
		arqueocaja.setGestion(rs.getInt("gestion"));
		arqueocaja.setDescripcion(rs.getString("descripcion"));
		arqueocaja.setCustodioFinalId(rs.getLong("custodio_final_id"));
		arqueocaja.setSucursalId(rs.getInt("sucursal_id"));
		arqueocaja.setAsientoId(rs.getLong("asiento_id"));
		return arqueocaja;
}
}