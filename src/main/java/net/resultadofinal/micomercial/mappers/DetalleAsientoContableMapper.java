// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.contable.DetalleAsientoContable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleAsientoContableMapper implements RowMapper<DetalleAsientoContable> {
	@Override
	public DetalleAsientoContable mapRow(ResultSet rs, int rowNum)throws SQLException{
		DetalleAsientoContable detalleAsientoContable = new DetalleAsientoContable();
		detalleAsientoContable.setCodAsiento(rs.getLong("cod_asiento")); 
		detalleAsientoContable.setCodDetalle(rs.getInt("cod_detalle")); 
		detalleAsientoContable.setCodSubcuenta(rs.getInt("cod_subcuenta")); 
		detalleAsientoContable.setDebe(rs.getBigDecimal("debe")); 
		detalleAsientoContable.setHaber(rs.getBigDecimal("haber")); 
		return detalleAsientoContable;
}
}