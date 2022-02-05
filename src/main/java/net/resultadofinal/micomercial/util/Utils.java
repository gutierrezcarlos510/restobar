package net.resultadofinal.micomercial.util;

import net.resultadofinal.micomercial.model.General;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Utils {
	public static String getSuccessFailedAdd(String entity,boolean status) {
		if(status) {
			return successAdd(entity);
		}else {
			return failedAdd(entity);
		}
	}
	public static String getSuccessFailedMod(String entity,boolean status) {
		if(status) {
			return successMod(entity);
		}else {
			return failedMod(entity);
		}
	}
	public static String getSuccessFailedEli(String entity,boolean status) {
		if(status) {
			return successEli(entity);
		}else {
			return failedEli(entity);
		}
	}
	public static String getSuccessFailedAct(String entity,boolean status) {
		if(status) {
			return successAct(entity);
		}else {
			return failedAct(entity);
		}
	}
	public static String successGet(String entidad) {
		return "Se realizo con exito la consulta de "+entidad;
	}
	public static String failedGet(String entidad) {
		return "No se logro realizar la obtencion de "+entidad;
	}
	/**
	 * Se realizo con exito el registro de ?
	 * @param entidad
	 * @return
	 */
	public static String successAdd(String entidad) {
		return "Se realizo con exito el registro de "+entidad;
	}
	/**
	 * Se realizo con exito la actualizacion de ?
	 * @param entidad
	 * @return
	 */
	public static String successMod(String entidad) {
		return "Se realizo con exito la actualizacion de "+entidad;
	}
	/**
	 * Se realizo con exito la eliminacion de ?
	 * @param entidad
	 * @return
	 */
	public static String successEli(String entidad) {
		return "Se realizo con exito la eliminacion de "+entidad;
	}
	/**
	 * Se realizo con exito la activacion de ? 
	 * @param entidad
	 * @return
	 */
	public static String successAct(String entidad) {
		return "Se realizo con exito la activacion de "+entidad;
	}
	/**
	 * No se logro realizar el registro de  ?
	 * @param entidad
	 * @return
	 */
	public static String failedAdd(String entidad) {
		return "No se logro realizar el registro de "+entidad;
	}
	/**
	 * No se logro realizar la actualizacion de  ?
	 * @param entidad
	 * @return
	 */
	public static String failedMod(String entidad) {
		return "No se logro realizar la actualizacion de "+entidad;
	}
	/**
	 * No se logro realizar el registro de la eliminacion de  ?
	 * @param entidad
	 * @return
	 */
	public static String failedEli(String entidad) {
		return "No se logro realizar el registro de la eliminacion de "+entidad;
	}
	/**
	 * No se logro cambiar el estado de  ? 
	 * @param entidad
	 * @return
	 */
	public static String failedAct(String entidad) {
		return "No se logro cambiar el estado de "+entidad;
	}
	/**
	 * Error al adicionar ? : ?
	 * @param entidad
	 * @param msg
	 * @return
	 */
	public static String errorAdd(String entidad,String msg) {
		return "Error al adicionar "+entidad+(msg.isEmpty()?"":": ")+msg;
	}
	/**
	 * Error al modificar ?
	 * @param entidad
	 * @param msg
	 * @return
	 */
	public static String errorMod(String entidad,String msg) {
		return "Error al modificar "+entidad+(msg.isEmpty()?"":": ")+msg;
	}
	/**
	 * Error al eliminar ? : ?
	 * @param entidad
	 * @param msg
	 * @return
	 */
	public static String errorEli(String entidad,String msg) {
		return "Error al eliminar "+entidad+(msg.isEmpty()?"":": ")+msg;
	}
	/**
	 * Error al activar ? : ?
	 * @param entidad
	 * @param msg
	 * @return
	 */
	public static String errorAct(String entidad,String msg) {
		return "Error al activar "+entidad+(msg.isEmpty()?"":": ")+msg;
	}
	/**
	 * Error al obtener ?
	 * @param entidad
	 * @param msg
	 * @return
	 */
	public static String errorGet(String entidad,String msg) {
		return "Error al obtener "+entidad+": "+msg;
	}
	public static String expiredSession(String controller) {
		return "No se encontro sesion abierta en:"+controller;
	}
	@SuppressWarnings("hiding")
	public static <T> boolean isNotEmptyList(List<T> lista) {
		return lista !=null && !lista.isEmpty();
	}
	public static String SubirArchivo(MultipartFile archivo,String nombre,String repositorio){
		if(archivo!=null && archivo.getSize()>0){
			try {
  	              archivo.transferTo(new File(repositorio+nombre));
  	    	   	  return nombre;
			} catch (IllegalStateException|IOException e) {
  	    	   		e.printStackTrace();	
  	    	}
		}
		return null;
	}
	
	public static void deleteFile(HttpServletRequest r,String folder,String filename) {			
		new File(r.getSession().getServletContext().getRealPath("images") + "/"+folder+"/" + filename).delete();	
	}	
	
	public static String generarAlias(String nombres,String PriApe,String SegApe){
		String[]noms=nombres.split(" ");
		String alias="";
		if(noms.length==1)alias+=nombres.substring(0,2);
		else for(int i=0;i<noms.length;i++){
					if(noms[i].length()>2)alias+=noms[i].substring(0,1);	
				}
		if(SegApe!=null&&SegApe.length()>1)alias+=PriApe.substring(0,2)+SegApe.substring(0,2);
		else try{alias+=PriApe.substring(0,4);}catch(Exception e){alias+=PriApe;}
			
		return alias.toUpperCase();
	}
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	public static void loadDataReport(Map<String, Object> param, General general) {
		param.put("nom_gen", general.getNom_gen());
		param.put("des_gen", general.getDes_gen());
		param.put("dir_gen", general.getDir_gen());
		param.put("tel_gen", general.getTel_gen());
		param.put("lug_gen", general.getLug_gen());
		param.put("ges_gen", general.getGes_gen());
		param.put("gestion", general.getGes_gen());
		param.put("logo", general.getLogtex_gen());
	}
}

