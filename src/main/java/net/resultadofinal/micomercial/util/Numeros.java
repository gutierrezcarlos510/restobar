package net.resultadofinal.micomercial.util;

import java.text.DecimalFormat;


/**
 * @author Carlos Franz Gutierrez G
 */
public class Numeros {

    private final String[] UNIDADES = { "", "uno ", "dos ", "tres ",
            "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve ", "diez ",
            "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis",
            "diecisiete", "dieciocho", "diecinueve", "veinte" };

    private final String[] DECENAS = { "","","venti", "treinta ", "cuarenta ",
            "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa ",
            "cien " };

    private final String[] CENTENAS = { "","ciento ", "doscientos ",
            "trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
            "setecientos ", "ochocientos ", "novecientos " };
    
    private final int[] numeros = {100,20,0,0};
    
    private final String[][] LITERALES={CENTENAS,DECENAS,UNIDADES};
    
    public String convertir(int x){
    	
    	String literal="";
    	
    	int i=0;
    	
    	boolean y=false;
    	try {
		
    	while (x>0) {
    		switch (i) {
    		
    		case 0:
    			
    			if(x>numeros[i]){
    				
    				literal+=LITERALES[i][x/numeros[i]];
        			
        			x%=numeros[i];
    				
    			}
    			
    			break;
    			
    		case 1:
    			
    			if(x>numeros[i]){
    				
    				if(x/(numeros[i]-10)==2)y=false;
    				
    				else y=true;
    				
    				literal+=LITERALES[i][x/(numeros[i]-10)];
    			
    				x%=(numeros[i]-10);
    			
    			}
    			
    			break;

    		default:
    			
    			if(x>numeros[i]){
    			
    			if(y)if((x/(numeros[i]+1))==20)literal+=LITERALES[i][x/(numeros[i]+1)];
    				
    			else literal+="y "+LITERALES[i][x/(numeros[i]+1)];
    			
    			else literal+=LITERALES[i][x/(numeros[i]+1)];
    			
    			x%=(numeros[i]+1);
    			
    			break;
    		}
    		
    		}
			
    		i++;
			
		}
    	
		} catch (Exception e) {
			System.out.println("error al convertir="+e.toString());
		}
    	return literal;
    	
    }
    
    public String convertirALiteral(double numero){
    	
    	int entero=(int)numero;
    	
    	String cadena="";
    	
    	if(entero>=1000000){
    		
    		if(entero/1000000==1)cadena+="un millon ";
    		
    		else cadena+=convertir(entero/1000000)+"millones ";
    		
    		entero%=1000000;
    		
    	}
    	
    	if(entero>=1000){
    		
    		if(entero/1000==1)cadena+="mil ";
    		
    		else cadena+=convertir(entero/1000)+"mil ";
    		
    		entero%=1000;
    		
    	}
    	
    	if(entero>0)cadena+=convertir(entero);
    	
    	return cadena;
    	
    }
    
 public String convertirALiteral2(float numero){
    	
    	int entero;
    	String cadena="";
    	try {
			
		
    	entero=(int)numero;
    	
    	System.out.println("ent="+entero);
    	
    	if(entero>=1000000){
    		
    		if(entero/1000000==1)cadena+="un millon ";
    		
    		else cadena+=convertir(entero/1000000)+"millones ";
    		
    		entero%=1000000;
    		
    	}
    	
    	if(entero>=1000){
    		
    		if(entero/1000==1)cadena+="mil ";
    		
    		else cadena+=convertir(entero/1000)+"mil ";
    		
    		entero%=1000;
    		
    	}
    	
    	if(entero>0)cadena+=convertir(entero);
    	} catch (Exception e) {
			System.out.println("error al parsear="+e.toString());
		}
    	return cadena;
    	
    }
    
    public double formato_2_decimales(double x){
    	
    	DecimalFormat format=new DecimalFormat("#########.##");
    	
    	String cad=format.format(x).replace(',','.');
    	
    	if(cad.substring(cad.indexOf('.')+1).length()<3)cad+="0";
    	
    	return Double.parseDouble(cad);
    	
    }
    
    public String obtenerDecimales(double numero){
    	String cadena=String.valueOf(numero);
    	int i=cadena.indexOf(".");
    	if(i>-1){
    		String subcadena=cadena.substring(i+1);
    		if(subcadena.length()>1)return subcadena.substring(0,2);
    		return subcadena+"0";
    	}
    	return "00";
    }

}