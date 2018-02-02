/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.export;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author 578
 */
public class ReportExportableXLS {
    
    private File file;
    private List<JTable> tabla;
    List<String> nom_files;
    List<String> head_table;

    public ReportExportableXLS(File file, List<JTable> tabla, List<String> nom_files, List<String> head_table) {
        this.file = file;
        this.tabla = tabla;
        this.nom_files = nom_files;        
        this.head_table = head_table;  
    }

        
    public boolean export(){
        try{
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            WritableWorkbook w = Workbook.createWorkbook(out);
            for(int index=0; index < tabla.size(); index++){
                JTable table = tabla.get(index);
                WritableSheet sheet  = w.createSheet(nom_files.get(index), 0);                
                for(int i=0; i <table.getColumnCount(); i++){                    
                    for(int j=0; j < table.getRowCount();j++){
                        sheet.addCell(new Label(i,0,head_table.get(i)));
                        Object object = table.getValueAt(j,i);
                        sheet.addCell(new Label(i,j+1,String.valueOf(object)));
                        
                    }
                }
                
            }
            w.write();
            w.close();           
            out.close(); //Liberar recursos del sistema.
            return true;
        
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
        
    }

    
    
    
    
}
