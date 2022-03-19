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
		venta.setId(rs.getLong("id"));
		venta.setUsuarioId(rs.getLong("usuario_id"));
		venta.setClienteId(rs.getLong("cliente_id"));
		venta.setFecha(rs.getDate("fecha"));
		venta.setObs(rs.getString("obs"));
		venta.setTotal(rs.getBigDecimal("total"));
		venta.setDescuento(rs.getBigDecimal("descuento"));
		venta.setGestion(rs.getInt("gestion"));
		venta.setEstado(rs.getBoolean("estado"));
		venta.setArqueoId(rs.getLong("arqueo_id"));
		venta.setDetalleArqueoId(rs.getInt("detalle_arqueo_id"));
		venta.setTipo(rs.getShort("tipo"));
		venta.setSucursalId(rs.getInt("sucursal_id"));
		venta.setSubtotal(rs.getBigDecimal("subtotal"));
		venta.setXusuario(rs.getString("xusuario"));
		venta.setXcliente(rs.getString("xcliente"));
		return venta;
	}
}