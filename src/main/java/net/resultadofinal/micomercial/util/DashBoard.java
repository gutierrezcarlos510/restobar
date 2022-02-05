package net.resultadofinal.micomercial.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashBoard {

	public List<String> asignarDesignarRoles(List<Map<String, Object>> roles,Integer obtenidos[]){
		List<String> lista=new ArrayList<String>();
		boolean res;
		try {
			for (Map<String, Object> map : roles) {
				res=true;
				for (int i = 0; i < obtenidos.length && res; i++) 
					if(obtenidos[i]==Integer.parseInt(map.get("cod_rol").toString()))res=false;
				if(res){
					lista.add(map.get("cod_rol").toString()+"-false");
					roles.remove(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < obtenidos.length; i++) {
			res=true;
			for (Map<String, Object> map : roles) 
				if(Integer.parseInt(map.get("cod_rol").toString())==obtenidos[i])res=false;
			if(res)lista.add(obtenidos[i]+"-true");
		}
		return lista;
	}
}
