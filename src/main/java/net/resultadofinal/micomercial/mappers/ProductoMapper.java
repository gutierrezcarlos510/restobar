package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.Producto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoMapper implements RowMapper<Producto> {
	@Override
	public Producto mapRow(ResultSet rs, int rowNum)throws SQLException{
		Producto producto = new Producto();
		producto.setCod_pro(rs.getInt("cod_pro")); 
		producto.setNom_pro(rs.getString("nom_pro")); 
		producto.setPrecom_pro(rs.getFloat("precom_pro")); 
		producto.setPreven_pro(rs.getFloat("preven_pro")); 
		producto.setGan_pro(rs.getFloat("gan_pro")); 
		producto.setCan_pro(rs.getInt("can_pro")); 
		producto.setEst_pro(rs.getBoolean("est_pro")); 
		producto.setCod_tippro(rs.getInt("cod_tippro"));
		if(producto.hasColumn(rs, "rn")) {
			producto.setRn(rs.getLong("rn"));
		}
		if(producto.hasColumn(rs, "tot")) {
			producto.setTot(rs.getInt("tot"));
		}
		return producto;
}
}