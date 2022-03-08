package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartillaDiariaImpl extends DbConeccion implements CartillaDiariaS {

	protected JdbcTemplate db;
	@Autowired
	public CartillaDiariaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CartillaDiariaImpl.class);
	private static final String ENTITY = "CartillaDiaria";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	
	@Override
	public DataTableResults<CartillaDiaria> listado(HttpServletRequest request, boolean estado, Integer sucursalId) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("cartilla_diaria cd");
			sqlBuilder.setSelect("cd.*,concat(p.nom_per,' ',p.priape_per) xusuario");
			sqlBuilder.addJoin("persona p on p.cod_per = cd.usuario_id");
			sqlBuilder.setWhere("cd.estado=:xestado and cd.cod_suc=:xsuc");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xsuc", sucursalId);
			return utilDataTableS.list(request, CartillaDiaria.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}

	
	@Override
	public CartillaDiaria obtener(Integer id){
		try {
			List<CartillaDiaria> lista = db.query("select * from cartilla_diaria where id=?", BeanPropertyRowMapper.newInstance(CartillaDiaria.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public List<DetalleCartillaDiaria> obtenerDetalles(Integer id){
		try {
			return db.query("select dcd.*,tp.nombre as xtipo_producto from detalle_cartilla_diaria dcd inner join producto p on dcd.producto_id = p.id where cartilla_diaria_id=? order by id asc", BeanPropertyRowMapper.newInstance(DetalleCartillaDiaria.class), id);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	
	
	@Override
	public DataResponse adicionar(CartillaDiaria obj){
		try {
			if(obj.getDetalles() != null && !obj.getDetalles().isEmpty()) {
				Long id = db.queryForObject("select coalesce(max(id),0)+1 from cartilla_diaria", Long.class);
				sqlString = "insert into cartilla_diaria(id,finicio,usuario_id,cod_suc,estado,estado_cartilla) values(?,now(),?,true,?,true)";
				boolean save = db.update(sqlString, id, obj.getUsuarioId(),obj.getCodSuc()) > 0;
				adicionarDetalles(id,obj.getDetalles());
				return new DataResponse(save, Utils.getSuccessFailedAdd(ENTITY, save));
			} else {
				return new DataResponse(false, "No se encontro detalles de la cartilla");
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Override
	public void adicionarDetalles(Long cartillaDiariaId, List<DetalleCartillaDiaria> detalles){
		if(detalles != null && !detalles.isEmpty()) {
			sqlString = "insert into detalle_cartilla_diaria(cartilla_diaria_id,id,cartilla_sucursal_id,detalle_cartilla_sucursal_id,producto_id,precio_individual,precio_compuesto,cantidad) values(?,?,?,?,?,?,?,?);";
			int i= 1;
			for (DetalleCartillaDiaria det: detalles) {
				db.update(sqlString, cartillaDiariaId,i,det.getCartillaSucursalId(),det.getDetalleCartillaSucursalId(),det.getProductoId(),det.getPrecioIndividual(),det.getPrecioCompuesto(),det.getCantidad());
			}
		}
	}
	
	
	@Override
	public DataResponse modificar(CartillaDiaria obj){
		try {
			if(obj.getDetalles() != null && !obj.getDetalles().isEmpty()) {
				sqlString = "update cartilla_diaria set finicio=now(),usuario_id=? where id=?";
				boolean update = db.update(sqlString, obj.getUsuarioId(), obj.getId()) > 0;
				db.update("delete from detalle_cartilla_diaria where cartilla_diaria_id = ?", obj.getId());
				adicionarDetalles(obj.getId(), obj.getDetalles());
				return new DataResponse(update, Utils.getSuccessFailedMod(ENTITY, update));
			} else {
				return new DataResponse(false, "No se encontro detalles de la cartilla");
			}

		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	
	
	@Override
	public DataResponse darEstado(Integer id, Boolean estado) {
		try {
			sqlString = "update cartilla_diaria set estado = ? where id=?";
			boolean update = db.update(sqlString, estado, id) > 0;
			return estado ? Utils.getResponseDataAct(ENTITY, update) : Utils.getResponseDataEli(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
}
