/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.util;

import com.cmm.cvs2xml.util.StringManage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 578
 */
public class GenericMethods {
    
     public String getFileExtension(String path) {
   
        try {
            return path.substring(path.lastIndexOf("."));

        } catch (Exception e) {
            return "";
        }

    }
    
    public static List<String> getListaCorreosValidos(String strListaCorreos, String correoCharSeparator){
        List<String> listaCorreosValidos = new ArrayList<String>();
        String[] arrayCorreos  = getArrayCorreos(strListaCorreos, correoCharSeparator);
        for (String correo :  arrayCorreos){
            if (!StringManage.getValidString(correo).equals("")){
                if (new GenericValidator().isEmail(StringManage.getValidString(correo)))
                    listaCorreosValidos.add(correo);
            }
        }
        return listaCorreosValidos;
    }
     
    public static String[] getArrayCorreos(String strListaCorreos, String correoCharSeparator){
        String[] arrayCorreos = new String[0];
        try{
            arrayCorreos = strListaCorreos.split(correoCharSeparator);
        }catch(Exception ex){}
        return arrayCorreos;
    }
    
    public static String exceptionStackTraceToString(Exception ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
    
    public static String getDateHourString(Date date){
        if (date == null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(date);
    }
    
    
    public static String[] getArrayData(String stringConcatenado, String correoCharSeparator){
        String[] arrrayData = new String[0];
        try{
            arrrayData = stringConcatenado.split(correoCharSeparator);
        }catch(Exception ex){}
        return arrrayData;
    }
    
}
