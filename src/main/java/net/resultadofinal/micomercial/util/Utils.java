package net.resultadofinal.micomercial.util;

import net.resultadofinal.micomercial.model.General;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public abstract class Utils {
	public static DataResponse getResponseDataAdd(String entity,boolean status) {
		return new DataResponse(status, status ? successAdd(entity) : failedAdd(entity));
	}
	public static DataResponse getResponseDataMod(String entity,boolean status) {
		return new DataResponse(status, status ? successMod(entity) : failedMod(entity));
	}
	public static DataResponse getResponseDataEli(String entity,boolean status) {
		return new DataResponse(status, status ? successEli(entity) : failedEli(entity));
	}
	public static DataResponse getResponseDataAct(String entity,boolean status) {
		return new DataResponse(status, status ? successAct(entity) : failedAct(entity));
	}
	public static String getSuccessFailedAdd(String entity,boolean status) {
		return status ? successAdd(entity) : failedAdd(entity);
	}
	public static String getSuccessFailedMod(String entity,boolean status) {
		return status ? successMod(entity) : failedMod(entity);
	}
	public static String getSuccessFailedEli(String entity,boolean status) {
		return status ? successEli(entity) : failedEli(entity);
	}
	public static String getSuccessFailedAct(String entity,boolean status) {
		return status ? successAct(entity) : failedAct(entity);
	}
	public static String successGet(String entidad) {
		return "Se realizo con exito la obtencion de "+entidad;
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
	/**
	 * Obtiene un File en base al request de la sesion, la direccion donde se guardara la imagen y el nombre del archivo con el cual se guardara
	 * @param direccion
	 * @param nombreArchivo
	 * @return
	 * @throws MalformedURLException
	 */
	public static File obtenerArchivo(String direccion,String nombreArchivo) throws MalformedURLException {
		Path pathImage = getPath(direccion, nombreArchivo);
		Resource recurso = new UrlResource(pathImage.toUri());
		if(!recurso.exists() || !recurso.isReadable())
			return null;//throw new RuntimeException("Error no se puede cargar la imagen: "+ pathImage.toString());
		return pathImage.toFile();
	}
	/**
	 * Devuelve la extension del archivo enviado
	 * @param archivo
	 * @return
	 */
	public static String getExtensionFile(MultipartFile archivo) {
		if(archivo==null)
			return null;
		if(archivo.isEmpty())
			return null;
		return archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
	}
	/**
	 * Devuelve si se subio la imagen en tal directorio y nombre especificado
	 * @param archivo
	 * @param repositorio
	 * @param nombreArchivo
	 * @return Boolean True= se guardo correctamente, False=no se logro guardar
	 */
	public static Boolean SubirArchivo(MultipartFile archivo,String repositorio,String nombreArchivo){
		if(archivo!=null && archivo.getSize()>0){
			try {
				Path rootPath = getPath(repositorio, nombreArchivo);
				Files.copy(archivo.getInputStream(), rootPath);
				return true;
			} catch (IllegalStateException|IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public static Boolean existeArchivo(MultipartFile archivo) {
		if(archivo == null)
			return false;
		if(archivo.getOriginalFilename().isEmpty())
			return false;
		return !archivo.isEmpty();
	}
	/**
	 * Elimina el archivo que se envia si es que se encuentra en el reporsitorio especificado
	 * @param repositorio
	 * @param nombreArchivo
	 * @throws MalformedURLException
	 */
	public static void eliminarArchivo(String repositorio,String nombreArchivo) throws MalformedURLException {
		File direccion=obtenerArchivo(repositorio, nombreArchivo);
		if(direccion != null && direccion.exists() && direccion.canRead())
			direccion.delete();
	}

	public static String generateNameArchiveSalida(Integer codSuc,Long codSalida) {
		return "salida_"+codSuc+"_"+codSalida+".json";
	}
	public static Path getPath(String directory, String filename) {
		return Paths.get(directory).resolve(filename).toAbsolutePath();
	}
}

