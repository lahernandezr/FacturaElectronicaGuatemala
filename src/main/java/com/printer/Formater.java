package com.printer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
    Libreria para el formateo de textos para impresión
    
    @version 1
    @author Luis Angel Hernandez Rodriguez
    
    Correo: lahernandezr@outlook.com
*/
public class Formater {

    /**
    Función para agregar espacios en blanco a la derecha
    @param str cadena que deseamos agregarle espacios
    @param str_length especifica el tamaño de la cadena a rellenar
    */
    public String rightPad(String str, int str_length){
        
        StringBuilder sb = new StringBuilder(str_length);        
        //append original string
        sb.append(str);        
        while(sb.length() < str_length)
            sb.append(' ');        
        return sb.toString();
        
    }

    /**      
    Función para agregar espacios en blanco a la izquierda
    @param str cadena que deseamos agregarle espacios
    @param str_length especifica el tamaño de la cadena a rellenar
    */
    public String leftPad(String str, int str_length){
        
        StringBuilder sb = new StringBuilder(str_length);
        
        while( sb.length() < (str_length - str.length()) )
            sb.append(' ');
        
        //append original string
        sb.append(str);
        
        return sb.toString();
    }    

    /**
    Función para divide una cadena por espacios en blanco y las agrupa en cadenas de 15 caracteres
    @param cadena cadena que deseamos dividir    
    */
    public List<String> rowsString(String name, int length) {
            List<String>  listData = new ArrayList<String>();
            String[] arrOfStr = name.split(" ");
            String back= "";
            String current = "";            
            int index= 0;
            //int free_space = 0;
            if(arrOfStr.length == 1){
                listData.add(arrOfStr[0]); 
                return listData;
            }
            for (int x = 0; x < arrOfStr.length; x++){
                current = arrOfStr[x];                
                if(!back.equals(""))
                {
                    if(back.length()+1+current.length()<=length)
                    {
                        back = back +" "+current;
                        index = 1;                       
                        if(arrOfStr.length-1 == x){
                            listData.add(back);  
                            back = "";
                            index = 0;
                        }
                        
                    }              
                    else{
                        listData.add(back);       
                        back = current;
                        if(arrOfStr.length-1 == x){
                            listData.add(back);  
                            back = "";

                        }                        
                        index = 0;                                     
                    }  
                }else
                {
                    back = current;                    
                    if(arrOfStr.length-1 == x)
                        listData.add(back);  
                    index++;                        
                }  
                
            }
            if(index == 1)
                listData.add(current);  
            
            for(String a: listData) 
                System.out.println(a);   
             

        return listData;
    }

    /**
    Función que formatea un numero double a formato decimal string
    @param amount valor a formatear
    @param symbolo de moneda
    */
    public String doubleToCurrency(double amount, String symbol){
        
          
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        
        dfs.setMonetaryDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);

        
        return symbol + df.format(amount);
    }


    /*
    Función que formatea un numero tipo string a formato decimal string
    @param amount valor a formatear
    @param symbolo de moneda
    */
    public String stringToCurrency(String amount, String symbol){

        double amountDouble = Double.parseDouble(amount);
        return doubleToCurrency(amountDouble, symbol);
    }        

    public Formater() {
    }

}
