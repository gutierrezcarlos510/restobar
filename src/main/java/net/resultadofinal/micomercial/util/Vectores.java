package net.resultadofinal.micomercial.util;

import java.math.BigDecimal;

public class Vectores {
	public String convertShortToString(Short vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++)
			cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertIntegerToString(Integer vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++) 
			cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertLongToString(Long vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++) 
			cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertFloatToString(Float vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++) 
			cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertStringToVector(String vec[]){
		String cad="{";
		if(vec==null || (vec!=null && vec.length==0))return "{}";
		for (int i = 0; i < vec.length; i++) {
			cad+=vec[i]+",";
		} 
				
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertBigDecimalToString(BigDecimal vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++)
			cad+=vec[i].toString()+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
}
