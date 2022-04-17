package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.FormaPago;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Mesa;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;

@Service
public class FormaPagoImpl extends DbConeccion implements FormaPagoS {

	private JdbcTemplate db;
	@Autowired
	public FormaPagoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final String ENTITY = "FormaPagompl";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<FormaPago> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("forma_pago fp");
			sqlBuilder.setSelect("fp.*,s.nombre as xsucursal");
			sqlBuilder.addJoin("sucursal s on s.cod_suc = fp.sucursal_id");
			sqlBuilder.setWhere("fp.estado=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, FormaPago.class, sqlBuilder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<FormaPago> listAll(int sucursalId) {
		try {
			sqlString = "select fp.* from forma_pago fp where fp.estado = true and fp.sucursal_id=?;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(FormaPago.class), sucursalId);
		} catch(Exception ex) {
			return null;
		}
	}
	public DataResponse adicionar(FormaPago obj){
		try {
			Short id = db.queryForObject("select coalesce(max(id),0)+1 from forma_pago", Short.class);
			sqlString = "insert into forma_pago(id,nombre,es_efectivo_caja,estado,sucursal_id,alias) values(?,?,?,true,?,?)";
			boolean isSave = db.update(sqlString, id, obj.getNombre(), obj.getEsEfectivoCaja(), obj.getSucursalId(), obj.getAlias()) > 0;
			return new DataResponse(isSave, isSave ? id : null, Utils.getSuccessFailedAdd(ENTITY, isSave));
		} catch (Exception e) {
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	public DataResponse modificar(FormaPago obj){
		try {
			sqlString = "update forma_pago set nombre=?,es_efectivo_caja=?,sucursal_id=?,alias=? where id=?";
			boolean isUpdate = db.update(sqlString, obj.getNombre(), obj.getEsEfectivoCaja(), obj.getSucursalId(), obj.getAlias(), obj.getId()) > 0;
			return new DataResponse(isUpdate, Utils.getSuccessFailedMod(ENTITY, isUpdate));
		} catch (Exception e) {
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}

	public DataResponse eliminar(Short id){
		try {
			boolean isUpdate = db.update("update forma_pago set estado = false where id = ?;", id) > 0;
			return new DataResponse(isUpdate, Utils.getSuccessFailedAct(ENTITY, isUpdate));
		} catch (Exception e) {
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
}
