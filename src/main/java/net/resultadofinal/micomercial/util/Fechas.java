package net.resultadofinal.micomercial.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Fechas {
	public static int getYearNow() {
		LocalDate localDate = LocalDate.now();
		return localDate.getYear();
	}

	public String obtenerFechaActual(String formato) {
		SimpleDateFormat format = new SimpleDateFormat(formato, new Locale("es", "ES"));
		return format.format(new Date());
	}

	@SuppressWarnings("static-access")
	public static int obtenerDiaSemanaAnio(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.setTime(d);
		return calendar.get(calendar.WEEK_OF_YEAR);
	}

	public static int obtenerDiaSemanaFecha(Date d) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_WEEK);

	}

	public static String ObtenerFechaLiteral(Date f) {
		LocalDate fecha = f.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return fecha.getDayOfMonth() + " DE " + obtenerMesLiteral(fecha.getMonthValue() + 1) + " DE " + (fecha.getYear() + 1900);
	}

	public Date convertirStringDate(String fec, String formato) {
		SimpleDateFormat format = new SimpleDateFormat(formato, new Locale("es", "ES"));
		Date fecha = null;
		try {
			fecha = new java.sql.Date(format.parse(fec).getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fecha;
	}

	public static String obtenerMesLiteral(int num) {
		switch (num) {
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Septiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		default:
			return null;
		}

	}

	public Date sumarRestarDiasFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

	public Date sumarRestarHorasFecha(Date fecha, int horas) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, horas);
		return calendar.getTime();
	}

	public static long DiferenciaFechas(String vinicio, String vfinal) {
		Date dinicio = null, dfinal = null;
		long milis1, milis2, diff;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dinicio = sdf.parse(vinicio);
			dfinal = sdf.parse(vfinal);
		} catch (ParseException e) {
			System.out.println("Se ha producido un error en el parseo");
		}
		// INSTANCIA DEL CALENDARIO GREGORIANO
		Calendar cinicio = Calendar.getInstance();
		Calendar cfinal = Calendar.getInstance();
		// ESTABLECEMOS LA FECHA DEL CALENDARIO CON EL DATE GENERADO ANTERIORMENTE
		cinicio.setTime(dinicio);
		cfinal.setTime(dfinal);
		milis1 = cinicio.getTimeInMillis();
		milis2 = cfinal.getTimeInMillis();
		diff = milis2 - milis1;
		// calcular la diferencia en dias
		long diffdias = Math.abs(diff / (24 * 60 * 60 * 1000));
		return diffdias + 1;
	}

	public static String ultimodiaMes(int mes, int anio) {
		switch (mes) {
		case 0: // Enero
		case 2: // Marzo
		case 4: // Mayo
		case 6: // Julio
		case 7: // Agosto
		case 9: // Octubre
		case 11: // Diciembre
			return "31";
		case 3: // Abril
		case 5: // Junio
		case 8: // Septiembre
		case 10: // Noviembre
			return "30";
		case 1: // Febrero
			if (((anio % 100 == 0) && (anio % 400 == 0)) || ((anio % 100 != 0) && (anio % 4 == 0)))
				return "29"; // A�o Bisiesto
			else
				return "28";
		default:
		}
		return "0";
	}

	public String obtenerFecha(String formato) {
		SimpleDateFormat fecha = new SimpleDateFormat(formato);
		return fecha.format(new Date());
	}

	public String obtenerFecha(Date fecha, String formato) {
		SimpleDateFormat forma = new SimpleDateFormat(formato);
		return forma.format(fecha);
	}

	public Time ConvertirHora(String hora) {
		if (hora.equals(""))
			return null;
		return Time.valueOf(hora + ":00");
	}

	public String obtenerDiaSemanaLiteral(Date d) {
		String[] dias = { "Domingo", "Lunes", "Martes", "Mi�rcoles", "Jueves", "Viernes", "S�bado" };
		int numeroDia = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		numeroDia = cal.get(Calendar.DAY_OF_WEEK);
		return dias[numeroDia - 1];
	}
}