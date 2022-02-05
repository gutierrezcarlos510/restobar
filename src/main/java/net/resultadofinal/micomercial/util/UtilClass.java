package net.resultadofinal.micomercial.util;

import java.util.List;

public abstract class UtilClass {
	public static <T> T getFirst(List<T> bean) {
		if(isNotNullEmpty(bean)) {
			return bean.get(0);
		}
		return null;
	}
	public static <T> boolean isNotNullEmpty(List<T> lista) {
		return lista !=null && !lista.isEmpty();
	}
}
