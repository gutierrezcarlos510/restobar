package net.resultadofinal.micomercial.util;

public class MyConstant {
	public static final String URL_SYSTEM = "https://restobar.sibol.net";
	public static final int ROL_PERSONAL_VENTA = 4;
	public static final boolean INACTIVE = false;
	public static final int BEBIDA = 1;
	public static final int INSUMO = 2;
	public static final int PLATO = 3;
	public static final int BEBIDA_PREPARADA = 4;
	public static final int MATERIAL_SERVICIO = 5;
	public static final String PRODUCTO_DEFAULT = "notimage.png";
	public final static String FORMAT_IMG_PRODUCTO="producto-";
	public static final String CLAVE = "tarijabolivia";
//	public static final String RAIZ = "../archivosRestobar";//Windows
//	public static final String RAIZ = "/media/carlos/MisDatos/programacion/IdeaProjects/archivos/archivosRestobar";//Linux Server/tomcat
	public static final String RAIZ = "/home/carlos/apache-tomcat-9.0.45/archivosRestobar";//Linux Server/tomcat
	public static final String RUTA_AVATAR =  RAIZ + "/avatars";
	public static final String URL_PATH_AVATAR = "/avatars/";
	public static final String SYSTEM = "-resfit";//Ingresa a resultado fitness COMPRA Y VENTA
	public static class Archivo {
		public static final String RUTA_PRODUCTO = RAIZ+ "/producto";
	public final static String DIR_VIEW_PRODUCTOS="/producto/";
	}
	public static final class Session {
		public static final String ROL = "rol";
		public static final String ROLES = "roles";
		public static final String USER = "user";
		public static final String SUCURSAL = "sucursal";
		public static final String SUCURSALES = "sucursales";
		public static final String GESTION = "gestion";
	}
	public static final class ArqueoCaja {
		public static final Long USER_ADMIN = 1L;
	}
	public static final class Caracteristica {
		public static final short MEDIDA = 1;
	}
	public static final class Caja{
		public static final int COMPRA =4;
		public static final int CAJA_GENERAL =9;
		public static final int BANCO_GENERAL =10;
		public static final int MUEBLE =11;
		public static final int INMUEBLE =12;
		public static final int OTRO_ACTIVO =13;
		public static final int PRESTAMO_BANCARIO =14;
		public static final int OTRO_PASIVO =15;
		public static final int INGRESO_GENERAL=16;
		public static final int EGRESO_GENERAL = 17;
		public static final int VENTA_PAGO_BANCARIO = 20;
	}
	public static final class Cuenta{
		public static final int CAJA= 1;
		public static final int BANCO= 2;
		public static final int CAPITAL= 9;
		public static final int OTRO_PASIVO= 8;
		public static final int PRESTAMO_BANCARIO= 7;
		public static final int CUENTAS_POR_PAGAR= 6;
		public static final int MUEBLES_ENSERES= 4;
		public static final int UTILIDADES= 10;
		public static final int OTRO_EGRESO= 12;
		public static final int INGRESO_OPERACIONAL= 13;
		public static final int CAJA_DIARIA= 3;
		public static final int OTRO_ACTIVO= 5;
		public static final int EGRESO_OPERACIONAL= 11;
		public static final int OTRO_INGRESO= 14;
		public static final int INVENTARIO= 15;
	}
	
}
