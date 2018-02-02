/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.security.cert.CertificateException;
import javax.security.cert.CertificateExpiredException;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.commons.ssl.ProbablyNotPKCS8Exception;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class UtilSecurity {

    /**
     * Método para obtener la cadena codificada en Base64 de un archivo de llave privada
     * @param privateKey Arreglo de byte del Archivo de Llave Privada (.Key)
     * @param passwordPrivateKey String que corresponde al password de la Llave Privada
     * @return String con la cadena codificada en Base64 de la Llave Privada
     * @throws GeneralSecurityException
     */
    public static String privateKeyToBase64(byte[] privateKey, String passwordPrivateKey) throws GeneralSecurityException{

        PKCS8Key pkcs8 = new PKCS8Key(privateKey, passwordPrivateKey.toCharArray());

        PrivateKey pk = pkcs8.getPrivateKey();

        /*OBTENEMOS EL STRING EN BASE 64 DEL PRIVATE KEY*/
        byte[] keyBytes = pk.getEncoded();

        // Convert key to BASE64 encoded string
        //BASE64Encoder b64 = new BASE64Encoder();
        Base64 b64 = new Base64();
        java.lang.String strPrivateKey = b64.encodeToString(keyBytes);//encode(keyBytes);

        return strPrivateKey;
    }

    /**
     * M&eacute;todo que genera un hash de la cadena usando una digestión
     * con el algoritmo SHA-1
     * @param message Es el String al que se le va aplicar el hash
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String getHash(String message) throws NoSuchAlgorithmException {
        String hash = "";
        byte[] buffer = message.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        byte[] digest = md.digest();
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /**
     * Método que abre un archivo de tipo certificado y obtiene su representación
     * en objetos reconocidos por el lenguaje java
     * @param pathFile Ruta al archivo de Certificado. P. ej.: "/C:/certificados/acsat1.cer"
     * @return X509Certificate
     */
    public static java.security.cert.X509Certificate openCertificateFile(String pathFile){
        java.security.cert.X509Certificate cert = null;
        
        try {
            //Abrimos el certificado
            File cerFile = new File(pathFile);
            FileInputStream fis = new FileInputStream(cerFile);
            byte[] bytesCert = new byte[(int)cerFile.length()];
            fis.read(bytesCert);
            fis.close();

            //Creamos el objeto X509Certificate
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(bytesCert));
        } catch (Exception e) {
            cert = null;
        }

        return cert;
    }
    
    public static boolean validarCertificadoLlavePrivada(byte[] certificate, byte[] privateKey, String privateKeyPassword ) throws Exception{
        boolean valido = false;
        
        String textoAFirmar = "||Mensaje de Prueba para Firmar 1:2:3-4:5:6 ÁÉÍÓÚ||";
        
        try {
            PKCS8Key pkcs8 = null;
           try{
                 pkcs8 = new PKCS8Key(privateKey, null);                    
            }catch(Exception ex){}
            
            if (pkcs8!=null)
                throw new Exception("La llave privada enviada contiene implicita la contraseña, este formato es incorrecto.");
            
            pkcs8 = new PKCS8Key(privateKey, privateKeyPassword.toCharArray());
            
            System.out.println("Key tipo: " + (pkcs8.isDSA()?"DSA":(pkcs8.isRSA()?"RSA":"Indefinido")) );
            PrivateKey pk = pkcs8.getPrivateKey();
           Signature firma = Signature.getInstance("SHA1withRSA");
           
            firma.initSign(pk);
            firma.update(textoAFirmar.getBytes("UTF-8"));
            byte[] firmado = firma.sign();
            javax.security.cert.X509Certificate cert = javax.security.cert.X509Certificate.getInstance(certificate);
            cert.checkValidity();
            firma.initVerify(cert.getPublicKey());
            firma.update(textoAFirmar.getBytes("UTF-8"));
            valido = firma.verify(firmado);
        } catch (ProbablyNotPKCS8Exception e) {
            throw new Exception("La llave privada no tiene un formato correcto (PKCS8 sin contraseña implicita). " + e.toString());
            //return ERROR_PRIVATE_KEY;
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Codificación UTF-8 no soportada. " + e.toString());
            //return ERROR_PRIVATE_KEY;
        } catch (SignatureException e) {
            throw new Exception("La llave privada es incorrecta o no tiene el formato correcto (PKCS8)." + e.toString());
            //return ERROR_PRIVATE_KEY;
        } catch (GeneralSecurityException e) {
            throw new Exception("El password de la llave privada no corresponde.");
            //return ERROR_PASSWORD;
        } catch (CertificateExpiredException e) {
            throw new Exception("El certificado utilizado ha caducado.");
            //return ERROR_PUBLIC_PRIVATE_KEY_EXPIRIED;
        } catch (CertificateException e) {
            throw new Exception("El certificado no pudo ser cargado, verifique que sea un archivo valido. " + e.toString());
            //return ERROR_PUBLIC_KEY;
        } catch (Exception e) {
            throw new Exception("Error inesperado: " + e.toString());
            //return ERROR_PUBLIC_KEY;
        }
        
        return valido;
    }

}
