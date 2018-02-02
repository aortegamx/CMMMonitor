/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.bo.comprobante.UtilNombraArchivosBO;

/**
 *
 * @author ISCesar
 */
public class TestUtilNombraArchivosBO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String seqRenombrar = "%rr%_%fem%_%uuid%_%arc%";
        String nombreArchivoOriginal = "miArchivoDeNomina";
        String uuid = "C7641B28-8CC9-444D-980C-A3F4FAE72F91";
        String resultado = UtilNombraArchivosBO.convertirNombreArchivo(seqRenombrar, nombreArchivoOriginal, null, null, uuid);
        
        System.out.println(resultado);
    }
    
}
