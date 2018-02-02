
package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author luis morales
 *
 * Clase utilizada para el envío de correos desde el WF
 * 
 */
public class MailBO {

    private String SMTP = "";
    private String PORT = "";
    private String USER = "";
    private String PASSWORD = "";
    private String FROM_NAME = "";
    private Session SESSION;
    private MimeMessage MESSAGE;
    private Multipart BODY = new MimeMultipart();
    
    /**
     * Indica si el envío se hara en un hilo aparte para no retrasar la respuesta
     * del sistema
     */
    private boolean envioAsincrono = false;
    
    //Configuracion de la aplicación (DTO)
    protected Configuracion configuracionDto = null;
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }    
    
    /**
     * Constructor
     *
     * @param boolean defaultConfig - Define si se utilizará la configuración por defecto
     *
     * @throws TspSmtpConfigDaoException - En caso de error con la conexión a BD
     */    
    public MailBO(Connection conn, boolean defaultConfig) throws Exception{
        this.conn = conn;        
        if(defaultConfig){
            configuracionDto = new ConfiguracionDaoImpl(conn).findByPrimaryKey(1);
            this.setConfiguration(configuracionDto);
        }
    }

    /**
     * Devuelve el valor de PASSWORD
     * @return String PASSWORD
     */
    public String getPASSWORD() {
        return PASSWORD;
    }

    /**
     * Establece el valor de PASSWORD
     * @param String PASSWORD
     */
    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    /**
     * Devuelve el valor de PORT
     * @return String PORT
     */
    public String getPORT() {
        return PORT;
    }

    /**
     * Establece el valor de PORT
     * @param String PORT
     */
    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    /**
     * Devuelve el valor de SMTP
     * @return String SMTP
     */
    public String getSMTP() {
        return SMTP;
    }

    /**
     * Establece el valor de SMTP
     * @param String SMTP
     */
    public void setSMTP(String SMTP) {
        this.SMTP = SMTP;
    }

    /**
     * Devuelve el valor de USER
     * @return String USER
     */
    public String getUSER() {
        return USER;
    }

    /**
     * Establece el valor de USER
     * @param String USER
     */
    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getFROM_NAME() {
        return FROM_NAME;
    }

    public void setFROM_NAME(String FROM_NAME) {
        this.FROM_NAME = FROM_NAME;
    }

    /**
     * Define el remitente del mensaje
     * @param String strMail - Correo del remitente
     * @param String strMail - Nombre del remitente
     */
    public void setFrom(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null)
            MESSAGE.setFrom(new InternetAddress(strMail, strName));
    }

    /**
     * Agrega un destinatario
     * @param String strMail - Correo del destinatario
     * @param String strMail - Nombre del destinatario
     */
    public void addTo(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null){
            Address address[] = new Address[1];
            address[0] = new InternetAddress(strMail, strName);
            MESSAGE.addRecipients(javax.mail.Message.RecipientType.TO, address);
        }
    }

    /**
     * Agrega un destinatario CC
     * @param String strMail - Correo del destinatario
     * @param String strMail - Nombre del destinatario
     */
    public void addCC(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null){
            Address address[] = new Address[1];
            address[0] = new InternetAddress(strMail, strName);
            MESSAGE.addRecipients(javax.mail.Message.RecipientType.CC, address);
        }
    }

    /**
     * Agrega un destinatario BCC
     * @param String strMail - Correo del destinatario
     * @param String strMail - Nombre del destinatario
     */
    public void addBCC(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null){
            Address address[] = new Address[1];
            address[0] = new InternetAddress(strMail, strName);
            MESSAGE.addRecipients(javax.mail.Message.RecipientType.BCC, address);
        }
    }

    /**
     * Agrega un archivo al correo
     * @param urlFile Ruta absoluta del archivo
     * @param fileName Nombre del archivo
     * @return 
     * @throws javax.mail.MessagingException
     */
    public boolean addFile(String urlFile,String fileName) throws MessagingException{
        File file = new File(urlFile);
        if(file.exists()){
            MimeBodyPart attachmentFile = new MimeBodyPart();
            attachmentFile.setDataHandler(new DataHandler(new FileDataSource(urlFile)));
            attachmentFile.setFileName(fileName);
            BODY.addBodyPart(attachmentFile, BODY.getCount());
            return true;
        }else{
            return false;
        }
    }

    /**
     * Establece el texto del cuerpo del mensaje
     * @param strMessage Mensaje
     * @throws javax.mail.MessagingException
     */
    public void addMessage(String strMessage)throws MessagingException{
        BodyPart bodyText = new MimeBodyPart();
        bodyText.setContent(strMessage, "text/html; charset=UTF-8");
        BODY.addBodyPart(bodyText);
    }

    /**
     * Establece el texto del cuerpo del mensaje con una plantilla
     * @param content Mensaje
     * @throws javax.mail.MessagingException
     */
    public void addMessagePlantilla(String content)throws MessagingException{
        String strMessage;
        String plantillaStr;

        //De prueba
        plantillaStr ="<b>CMM Desktop Monitor informa:</b><br/> %content%";

        /**
         * Invocación a DAO de Plantillas de Correos
         */
         try {
            String plantillaAux = StringManage.getValidString(configuracionDto.getPlantillacorreo());
            plantillaStr = ( !plantillaAux.equals("")? plantillaAux : plantillaStr);
         }catch(Exception e){}
         
         try{
            strMessage = plantillaStr.replaceAll("%content%", content);
         }catch(Exception ex){
             strMessage = content;
            ex.printStackTrace(); 
         }
        
        strMessage+="<br/><hr/>Este correo es generado autom&aacute;ticamente por sistemas informaticos. Favor de NO responder al remitente.";

        BodyPart bodyText = new MimeBodyPart();
        bodyText.setContent(strMessage, "text/html; charset=UTF-8");
        BODY.addBodyPart(bodyText);
    }

    /**
     * Envía el correo a los destinatarios
     * @param strSubject
     * @throws javax.mail.MessagingException
     * @throws java.lang.Exception
     */
    public void send(String strSubject) throws MessagingException, Exception{
        MESSAGE.setSubject(strSubject,"UTF-8");
        MESSAGE.setContent(BODY);
        Transport transport = SESSION.getTransport("smtp");
        /*transport.connect(SMTP, USER, PASSWORD);
        transport.sendMessage(MESSAGE, MESSAGE.getAllRecipients());
        transport.close();*/
        if (envioAsincrono){
           //Envío en hilo aparte, respuesta inmediata sin retraso
            envioAsincrono(transport);
        }else{
            //Envío en línea, (puede retrasarse de 5 segundos hasta varios minutos dependiendo del tamaño de 
            //los adjuntos)
            System.out.println("Conectando a Servidor de Correos... " + new Date());
            transport.connect(SMTP, USER, PASSWORD);
            System.out.println("Conectado a Servidor de Correos [" + SMTP + "] " + new Date());
            transport.sendMessage(MESSAGE, MESSAGE.getAllRecipients());
            System.out.println("Correo enviado. " + new Date());
            transport.close();
        }
    }
    
    /**
     * Realiza el envío asíncrono de un correo, para obtener la respuesta inmediata
     * en el sistema y no retrasar los procesos.
     * @param transport
     * @throws MessagingException
     * @throws Exception 
     */
    public void envioAsincrono(final Transport transport) throws MessagingException, Exception{
        try{
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("[Asíncrono] Conectando a Servidor de Correos... " + new Date());
                        transport.connect(SMTP, USER, PASSWORD);
                        System.out.println("[Asíncrono] Conectado a Servidor de Correos [" + SMTP + "] " + new Date());
                        transport.sendMessage(MESSAGE, MESSAGE.getAllRecipients());
                        System.out.println("[Asíncrono] Correo enviado. " + new Date());
                        transport.close();
                    } catch (MessagingException ex) {
                        System.out.println("[Asíncrono] Error al intentar enviar correo. " + ex.toString());
                        ex.printStackTrace();
                        //POR HACER: Codigo para almacenar correo no enviado y en cron aparte intentar renvío mas tarde
                    }
                }
            };
            
            Thread thr1 = new Thread(r1);
            thr1.start();
        }catch(Exception ex){
            System.out.println("Error al intentar envío asíncrono de correo. " + ex.toString());
            ex.printStackTrace();
            //POR HACER: Codigo para almacenar correo no enviado y en cron aparte intentar renvío mas tarde
            throw ex;
        }
    }

    /**
     * Establece los parámetros de configuración
     * @param smtpConfig smtpConfig - Datos de configuración
     * @throws java.lang.Exception
     */
    public final void setConfiguration(Configuracion smtpConfig) throws Exception{
        this.configuracionDto = smtpConfig;
        if(smtpConfig!=null){

            this.SMTP = smtpConfig.getDominiosmtp();
            this.PORT = ""+smtpConfig.getPuertosmtp();
            this.FROM_NAME = smtpConfig.getUsuarioremitente();
            this.USER = smtpConfig.getCorreoremitente();
            this.PASSWORD = smtpConfig.getPasswordcorreo();
            

            Properties props = new Properties();
            props.put("mail.smtp.user", USER);
            props.put("mail.smtp.host", SMTP);
            props.put("mail.smtp.port", PORT);
            if (smtpConfig.getAutenticacionsmtp()==1){
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.ssl.checkserveridentity", "false");
                props.put("mail.smtp.ssl.trust", "*");
                
            }else {
                props.put("mail.smtp.starttls.enable", "false");
                props.put("mail.smtp.auth", "true");
            }

            props.put("mail.smtp.sendpartial", "true");
            
            props.put("mail.smtp.debug", "true");
            
            SESSION = Session.getInstance(props, null);
            //SESSION.setDebug(true);
            MESSAGE = new MimeMessage(SESSION);
            BODY = new MimeMultipart();
            
            this.setFrom(this.USER, this.FROM_NAME);
        }else
            throw new Exception("Se debe de establecer una configuración");
    }
    
    public int getToQuantity(){
        try{
            javax.mail.Address direcciones[] = MESSAGE.getRecipients(javax.mail.Message.RecipientType.TO);
            return direcciones.length;
        }catch(Exception ex){}
        return 0;
    }
    
    public int getCcQuantity(){
        try{
            javax.mail.Address direcciones[] = MESSAGE.getRecipients(javax.mail.Message.RecipientType.CC);
            return direcciones.length;
        }catch(Exception ex){}
        return 0;
    }
    
    /**
     * Indica si el envío se hara en un hilo aparte para no retrasar la respuesta
     * del sistema
     * @return 
     */
    public boolean isEnvioAsincrono() {
        return envioAsincrono;
    }

    /**
     * Indica si el envío se hara en un hilo aparte para no retrasar la respuesta
     * del sistema
     * @param envioAsincrono
     */
    public void setEnvioAsincrono(boolean envioAsincrono) {
        this.envioAsincrono = envioAsincrono;
    }

}
