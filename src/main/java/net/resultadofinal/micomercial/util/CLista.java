package net.resultadofinal.micomercial.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CLista {

	
	public String[] ordenar_archivo(BufferedReader archivo){
		String cadena="",linea;
		try {
			while((linea = archivo.readLine()) != null){
				cadena+=linea+",";
			}
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		String lista[]=cadena.split(",");
		return burbujaPalabras(lista); 
	}

	public static void imprimirArray (int lista[]){
        for(int i=0;i<lista.length;i++){
            System.out.println(lista[i]);
        }
    }

    public static void imprimirArray (String lista[]){
        for(int i=0;i<lista.length;i++){
            System.out.println(lista[i]);
        }
    }
 
    public static void rellenarArray (int lista[]){
        for(int i=0;i<lista.length;i++){
            lista[i]=numeroAleatorio();
        }
    }
 
    private static int numeroAleatorio (){
        return ((int)Math.floor(Math.random()*1000));
    }
 
    public static void burbuja (int lista[]){
        int cuentaintercambios=0;
        //Usamos un bucle anidado, saldra cuando este ordenado el array
        for (boolean ordenado=false;!ordenado;){
            for (int i=0;i<lista.length-1;i++){
                if (lista[i]>lista[i+1]){
                    //Intercambiamos valores
                    int variableauxiliar=lista[i];
                    lista[i]=lista[i+1];
                    lista[i+1]=variableauxiliar;
                    //indicamos que hay un cambio
                    cuentaintercambios++;
                }
            }
            //Si no hay intercambios, es que esta ordenado.
            if (cuentaintercambios==0){
                ordenado=true;
            }
            //Inicializamos la variable de nuevo para que empiece a contar de nuevo
            cuentaintercambios=0;
        }
    }
	public static String[] burbujaPalabras (String lista_palabras[]){
        boolean ordenado=false;
        int cuentaIntercambios=0;
        //Usamos un bucle anidado, saldra cuando este ordenado el array
        while(!ordenado){
            for(int i=0;i<lista_palabras.length-1;i++){
                if ((lista_palabras[i].substring(7)).compareToIgnoreCase((lista_palabras[i+1]).substring(7))>0){
                    //Intercambiamos valores
                    String aux=lista_palabras[i];
                    lista_palabras[i]=lista_palabras[i+1];
                    lista_palabras[i+1]=aux;
                    //indicamos que hay un cambio
                    cuentaIntercambios++;
                }
            }
            //Si no hay intercambios, es que esta ordenado.
            if (cuentaIntercambios==0){
                ordenado=true;
            }
            //Inicializamos la variable de nuevo para que empiece a contar de nuevo
            cuentaIntercambios=0;
        }
        return lista_palabras;
    }
	public int[] obtenerDatosLista(List<Map<String, Object>> lista,String campo){
		int vec[]=new int[lista.size()];
		try {
			System.out.println("tam="+lista.size());
			System.out.println(lista!=null);
		if(lista!=null){
			int i=0;
			for (Map<String, Object> map : lista) 
				vec[i++]=Integer.parseInt(map.get(campo).toString());
		}
		} catch (Exception e) {
			System.out.println("obtenerListaDato, clase CLISTA");
			e.printStackTrace();
		}
		return vec;
	}
	public List<String> lista_repetidos_nuevos(int antiguos[],int nuevos[]){//metodo para encontrar los nuevos y eliminar los que no se encuentran de una lista
		ArrayList<String> vector3=new ArrayList<>();
		boolean res;
		try {
			for (int i = 0; i < nuevos.length; i++) {
				res=true;
				for (int j = 0; j < antiguos.length; j++) if(res)if(nuevos[i]==antiguos[j])res=false;
				if(res)vector3.add(nuevos[i]+"-"+1);//adiciona
			}
			for (int i = 0; i < antiguos.length; i++) {
				res=true;
				for (int j = 0; j < nuevos.length; j++) if(res)if(antiguos[i]==nuevos[j])res=false;
				if(res)vector3.add(antiguos[i]+"-"+0);//elimina
			}
		} catch (Exception e) {
			System.out.println("errorrrc="+e.toString());
		}
		return vector3;
	}
}
