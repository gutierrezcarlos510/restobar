package net.resultadofinal.micomercial.util;

public final class MyConstant {
	public static final int ROL_PERSONAL_VENTA = 4;
	public static final boolean INACTIVE = false;
	public static final int BEBIDA = 1;
	public static final int INSUMO = 2;
	public static final int PLATO = 3;
	public static final int BEBIDA_PREPARADA = 4;
	public static final String PRODUCTO_DEFAULT = "notimage.png";
	public final static String FORMAT_IMG_PRODUCTO="producto-";
	public static final String CLAVE = "tarijabolivia";
//	public static final String RAIZ = "../archivosRestobar";//Windows
	public static final String RAIZ = "/home/carlos/apache-tomcat-9.0.45/archivosRestobar";//Linux Server/tomcat
	public static final String RUTA_AVATAR = RAIZ + "/avatars";
	public static final String RUTA_GENERAL = RAIZ + "/general";
	public static final String RUTA_CARRERA = RAIZ + "/carrera";
	public static final String URL_PATH_AVATAR = "/avatars/";
	public static final String URL_PATH_GENERAL = "/general/";
	public static final String URL_PATH_CARRERA = "/carrera/";
//	public static final String SYSTEM = "-resfin";//Ingresa a resultado final INSTITUTO
	public static final String SYSTEM = "-resfit";//Ingresa a resultado fitness COMPRA Y VENTA
	public static final boolean VENTA_CON_SELECT = true;//Venta con select para buscar producto POTOSI
//	public static final boolean VENTA_CON_SELECT = false;//Venta DATATABLE para buscar producto
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
	public static final class Caja{
		public static final int EGRESO_CAJA =1;
		public static final int PAGO_AYUDANTE_INSCRIPCION =2;
		public static final int PAGO_AYUDANTE_PRESTACION =3;
		public static final int COMPRA =4;
		public static final int INGRESO_CAJA =5;
		public static final int INGRESO_BANCO =6;
		public static final int PRESTACION_SERVICIO =7;
		public static final int VENTA =8;
		public static final int CAJA_GENERAL =9;
		public static final int BANCO_GENERAL =10;
		public static final int MUEBLE =11;
		public static final int INMUEBLE =12;
		public static final int OTRO_ACTIVO =13;
		public static final int PRESTAMO_BANCARIO =14;
		public static final int OTRO_PASIVO =15;
		public static final int INGRESO_GENERAL=16;
		public static final int EGRESO_GENERAL = 17;
		public static final int DESCUENTO_VENTA = 18;
		public static final int DESCUENTO_COMPRA = 19;
		public static final int VENTA_PAGO_BANCARIO = 20;
		public static final int DEVOLUCION_INCUMPLIMIENTO_PRESTACION = 21;
		public static final int PRESTACION_CON_PAGO_BANCARIO = 22;
		public static final int MULTA_COBRADA = 23;
		public static final int DEVOLUCION_CON_DEPOSITO_BANCARIO = 24;
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
