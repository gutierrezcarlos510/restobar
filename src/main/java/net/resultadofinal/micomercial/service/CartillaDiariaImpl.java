package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.model.form.CartillaDiariaForm;
import net.resultadofinal.micomercial.model.form.CartillaSucursalForm;
import net.resultadofinal.micomercial.model.form.DetalleCartillaForm;
import net.resultadofinal.micomercial.model.form.ProductoCartillaForm;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	public DataResponse adicionar(CartillaDiariaForm obj){
		try {
			if(obj != null && obj.getCartillaSucursalFormList() != null && !obj.getCartillaSucursalFormList().isEmpty()) {
				//Si existe anterior, dar de baja
				db.update("update cartilla_diaria set estado_cartilla = false, ffin=now() where estado_cartilla = true and estado=true;");
				Long id = db.queryForObject("select coalesce(max(id),0)+1 from cartilla_diaria", Long.class);
				sqlString = "insert into cartilla_diaria(id,finicio,usuario_id,cod_suc,estado,estado_cartilla) values(?,now(),?,?,true,true)";
				boolean save = db.update(sqlString, id, obj.getUsuarioId(),obj.getCodSuc()) > 0;
				obj.setId(id);
				adicionarDetalles(obj);
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
	public void adicionarDetalles(CartillaDiariaForm obj){
		if(UtilClass.isNotNullEmpty(obj.getCartillaSucursalFormList())) {
			sqlString = "insert into detalle_cartilla_diaria(cartilla_diaria_id,id,cartilla_sucursal_id,detalle_cartilla_sucursal_id,producto_id,precio_individual,precio_compuesto,cantidad) values(?,?,?,?,?,?,?,?);";
			int i= 1;
			for (CartillaSucursalForm det: obj.getCartillaSucursalFormList()) {
				if (UtilClass.isNotNullEmpty(det.getDetalleCartillaList())) {
					for(DetalleCartillaForm subdet: det.getDetalleCartillaList()) {
						if (UtilClass.isNotNullEmpty(subdet.getProductos())) {
							for(ProductoCartillaForm item : subdet.getProductos()) {
								db.update(sqlString, obj.getId(),i,det.getId(),subdet.getId(),item.getProductoId(),item.getPrecioIndividual(),item.getPrecioCompuesto(),item.getCantidad());
								i++;
							}
						} else {
							throw new RuntimeException("Lista Productos nula");
						}
					}
				} else {
					throw new RuntimeException("Lista DetalleCartilla nula");
				}
			}
		} else {
			throw new RuntimeException("Lista CartillaSucursalForm nula");
		}
	}

	@Override
	public DataResponse modificar(CartillaDiariaForm obj){
		try {
			if(obj != null && UtilClass.isNotNullEmpty(obj.getCartillaSucursalFormList())) {
				sqlString = "update cartilla_diaria set finicio=now(),usuario_id=? where id=?";
				boolean update = db.update(sqlString, obj.getUsuarioId(), obj.getId()) > 0;
				db.update("delete from detalle_cartilla_diaria where cartilla_diaria_id = ?", obj.getId());
				adicionarDetalles(obj);
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
	public CartillaDiariaForm obtenerCartillaDiariaForm (Long cartillaDiariaId) {
		try {
			sqlString = "select * from cartilla_diaria where id = ?";
			List<CartillaDiariaForm> lista = db.query(sqlString, BeanPropertyRowMapper.newInstance(CartillaDiariaForm.class), cartillaDiariaId);
			CartillaDiariaForm obj = UtilClass.getFirst(lista);
			if(obj != null) {
				sqlString = "select distinct cs.* from detalle_cartilla_diaria dcd inner join cartilla_sucursal cs on cs.id = dcd.cartilla_sucursal_id where dcd.cartilla_diaria_id = ?";
				List<CartillaSucursalForm> cartillaSucursalList = db.query(sqlString, BeanPropertyRowMapper.newInstance(CartillaSucursalForm.class), cartillaDiariaId);
				if(UtilClass.isNotNullEmpty(cartillaSucursalList)) {
					sqlString = "select distinct dcs.*,tp.nombre as xtipo_producto from detalle_cartilla_diaria dcd inner join detalle_cartilla_sucursal dcs on dcd.cartilla_sucursal_id = dcs.cartilla_sucursal_id and dcd.detalle_cartilla_sucursal_id = dcs.id inner join tipo_producto tp on tp.id=dcs.tipo_producto_id where dcd.cartilla_diaria_id =?;";
					List<DetalleCartillaForm> detalleCartillaFormList = db.query(sqlString, BeanPropertyRowMapper.newInstance(DetalleCartillaForm.class), cartillaDiariaId);

					sqlString = "select dcd.cartilla_sucursal_id,dcd.detalle_cartilla_sucursal_id,dcd.id,dcd.producto_id,dcd.precio_individual,dcd.precio_compuesto,dcd.cantidad,p.nombre as xproducto from detalle_cartilla_diaria dcd inner join producto p on p.id=dcd.producto_id where dcd.cartilla_diaria_id = ?";
					List<ProductoCartillaForm> productoList = db.query(sqlString, BeanPropertyRowMapper.newInstance(ProductoCartillaForm.class), cartillaDiariaId);
					if(UtilClass.isNotNullEmpty(productoList)) {
						for(ProductoCartillaForm pro: productoList) {
							for (DetalleCartillaForm det: detalleCartillaFormList) {
								if(det.getId() == pro.getDetalleCartillaSucursalId() && det.getCartillaSucursalId() == pro.getCartillaSucursalId()) {
									if(det.getProductos()!= null) {
										det.getProductos().add(pro);;
									} else {
										det.setProductos(Arrays.asList(pro));
									}
									break;
								}
							}
						}
					}

					Map<Integer,List<DetalleCartillaForm>> agrupadoPorCartillaSucursal = detalleCartillaFormList.stream().collect(Collectors.groupingBy(DetalleCartillaForm::getCartillaSucursalId));
					for (Map.Entry<Integer,List<DetalleCartillaForm>> item : agrupadoPorCartillaSucursal.entrySet()) {
						if(UtilClass.isNotNullEmpty(item.getValue())) {
							for(CartillaSucursalForm it : cartillaSucursalList) {
								if(it.getId() == item.getKey()) {
									it.setDetalleCartillaList(item.getValue());
									break;
								}
							}
						}
					}
					obj.setCartillaSucursalFormList(cartillaSucursalList);
					return obj;
				} else {
					System.out.println("cartillaSucursalList NULO");
					return null;
				}
			} else {
				System.out.println("CartillaDiariaForm NULO");
				return null;
			}
		} catch (Exception ex) {
			System.out.println("Exception founded:"+ex.toString());
			throw new RuntimeException("Error al obtener");
		}
	}
}
