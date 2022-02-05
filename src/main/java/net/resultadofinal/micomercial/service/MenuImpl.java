package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Menu;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilDataTableS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;

@Service
public class MenuImpl extends DbConeccion implements MenuS {

	private JdbcTemplate db;
	@Autowired
	public MenuImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	@Autowired
    ProcesoS procesoS;
	private String sql;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Menu> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("menu");
			sqlBuilder.setSelect("cod_men,nom_men,des_men,ico_men,est_men,(select cast(count(*)  as int)from mepro where mepro.cod_men=menu.cod_men) total_procesos");
			sqlBuilder.setWhere("est_men=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Menu.class, sqlBuilder);
		} catch (Exception e) {

			return null;
		}
	}
	public List<Menu> listAll() {
		try {
			sql = "select * from menu where est_men = true;";
			return db.query(sql, BeanPropertyRowMapper.newInstance(Menu.class));
		} catch(Exception ex) {
			return null;
		}
	}
	public List<Menu> listarPorRol(Integer cod_rol){
		List<Menu> menuList = db.query("select * from menu_obtener_x_codrol(?)", BeanPropertyRowMapper.newInstance(Menu.class),cod_rol);
		if(menuList!=null && !menuList.isEmpty()) {
			menuList.forEach(item->{
				item.setProcesos(procesoS.obtenerPorMenu(item.getCod_men()));
			});
		}
		return menuList;
	}
	public List<Menu> obtenerMenusPorCodrol(Integer cod_rol) {
		try {
			return db.query("select * from rol_obtenermenus(?)"+asObjectAdd(asMenu, "tipo varchar(8)"), BeanPropertyRowMapper.newInstance(Menu.class),cod_rol);
		} catch (Exception e) {
			return null;
		}
	}
	public Menu obtener(Integer cod_men){
		try {
			List<Menu> list = db.query("select * from menu where cod_men =?", BeanPropertyRowMapper.newInstance(Menu.class), cod_men);
			if(list != null && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("error obtenerMenu="+e.toString());
			return null;
		}
	}
	
	public Boolean adicionar(Menu m){
		try {
			String cad[]=m.getIco_men().split("-");
			return db.queryForObject("select menu_adicionar(?,?,?);",Boolean.class,m.getNom_men(),m.getDes_men(),cad[0]+" "+m.getIco_men());
		} catch (Exception e) {
			System.out.println("error al adicionar menu="+e.toString());
			return false;
		}
	}
	public Boolean modificar(Menu m){
		try {
			String cad[]=m.getIco_men().split("-");
			return db.queryForObject("select menu_modificar(?,?,?,?);",Boolean.class,m.getNom_men(),m.getDes_men(),cad[0]+" "+m.getIco_men(),m.getCod_men());
		} catch (Exception e) {
			System.out.println("error al modificar menu="+e.toString());
			return false;
		}
	}
	public Boolean darEstado(Integer cod_men,Boolean est){
		try {
			return db.queryForObject("select menu_darestado(?,?);",Boolean.class,cod_men,est);
		} catch (Exception e) {
			System.out.println("error al eliminar menu="+e.toString());
			return false;
		}
	}
	public Boolean adicionarMenuProceso(Integer codm,Integer obtenidos[]){
		try {
			db.update("delete from mepro where cod_men=?",codm);
			if(obtenidos!=null)
			for (int i = 0; i < obtenidos.length; i++)
					db.update("insert into mepro(cod_men,cod_pro) values(?,?)",codm,obtenidos[i]);
			return true;
		} catch (Exception e) {
			System.out.println("error al adicionar Mepro="+e.toString());
			return false;
		}
	}
	public Boolean validarNom(String nom){
		return db.queryForObject("select menu_validar(?)", Boolean.class,nom);
	}
}