/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.io.comparator.NameFileComparator;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class FileManage {

    /**
     * Método para crear un objeto tipo java.io.File a partir de un arreglo de bytes
     * @param bytesFile Los bytes que representan al archivo
     * @param pathFile Ruta en donde se grabara el archivo
     * @param nameFile Nombre del archivo
     * @return Objeto tipo File creado
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File createFileFromByteArray(byte[] bytesFile, String pathFile, String nameFile) throws FileNotFoundException, IOException {

        File dir = new File (pathFile);
        dir.mkdirs();
        
        File newTempFile = new File(pathFile + nameFile);
        FileOutputStream fos = new FileOutputStream(newTempFile);
        fos.write(bytesFile);
        fos.flush();
        fos.close();

        return newTempFile;

    }

    /**
     * Método para crear un objeto tipo java.io.File a partir de un arreglo de bytes
     * @param contentFile El string con el contenido del archivo
     * @param pathFile Ruta en donde se grabara el archivo
     * @param nameFile Nombre del archivo
     * @return Objeto tipo File creado
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File createFileFromString(String contentFile, String pathFile, String nameFile) throws FileNotFoundException, IOException {

        File dir = new File (pathFile);
        dir.mkdirs();
        
        File newTempFile = new File(pathFile + nameFile);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newTempFile), "UTF8"));
        bw.write(contentFile);
        bw.flush();
        bw.close();

        return newTempFile;

    }

    /**
     * Obtiene el arreglo de bytes de un Archivo
     * @param fileToRead Archivo a leer
     * @return Arreglo de bytes
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public static byte[] getBytesFromFile(File fileToRead) throws FileNotFoundException, IOException, Exception {
        byte[] bytes = null;

        FileInputStream fis = new FileInputStream(fileToRead);
        bytes = new byte[(int) fileToRead.length()];
        fis.read(bytes);
        fis.close();

        return bytes;
    }
    
    /**
     * Para los archivos que contienen texto tales como XML, txt, html, etc.
     * Se puede recuperar su contenido en formato Cadena con codificación solicitada
     * con este método.
     * @param fileToRead Archivo origen
     * @param charsetCodification NULL en caso de no necesitar especificar, se utilizara por defecto UTF8
     * @return contenido del archivo expresado en formato cadena
     */
    public static String getStringFromFile(File fileToRead, String charsetCodification){
        charsetCodification = (charsetCodification==null?"UTF8":charsetCodification);
        String contentTextArchivo=null;
        try {
             contentTextArchivo = new String(getBytesFromFile(fileToRead), charsetCodification);
        }catch(Exception e){
                
        }
        return contentTextArchivo;
    }

    /**
     * Realiza una copia de un archivo, indicando la ruta de origen
     * y la ruta de destino
     * @param archivoOrigen Ruta absoluta del archivo Origen
     * @param archivoDestino Ruta absoluta del archivo Destino
     * @return true en caso de ocurrir una copia correcta, false en caso contrario
     */
    public static boolean copyFile(String archivoOrigen, String archivoDestino) {
        boolean exito = false;

        try {
            File origen = new File(archivoOrigen);
            File destino = new File(archivoDestino);
            
            //Creamos carpetas de destino en caso de que no existan
            destino.getParentFile().mkdirs();

            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);

            try {
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }

                exito = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                exito = false;
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exito = false;
        }

        return exito;
    }
    
    public static void copyFile2(File archivoOrigen, File archivoDestino) throws Exception {
        
        //Creamos carpetas de destino en caso de que no existan
        if (!archivoDestino.getParentFile().exists())
            archivoDestino.getParentFile().mkdirs();
        
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(archivoOrigen).getChannel();
            outputChannel = new FileOutputStream(archivoDestino).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
    
    /**
     * Mueve un archivo, indicando la ruta de origen
     * y la ruta de destino
     * @param archivoOrigen Ruta absoluta del archivo Origen
     * @param archivoDestino Ruta absoluta del archivo Destino
     * @return true en caso de ocurrir una copia correcta, false en caso contrario
     */
    public static boolean moveFile(String archivoOrigen, String archivoDestino) {
        //Primero copiamos archivo
        if (copyFile(archivoOrigen, archivoDestino)){
            //eliminamos archivo Origen
            File fileOrigen = new File(archivoOrigen);
            boolean eliminado = false;
            for (int k=0; k<100; k++){
                if (fileOrigen.delete()){
                    eliminado = true;
                    break;
                }
            }
            if (!eliminado)
                fileOrigen.deleteOnExit();
            
            return true;
        }else
            return false;
    }

    /**
     * Busca un archivo con la coincidencia con alguna parte de su nombre
     * después lo elimina
     * P. ejem: si nameLike es "ABC" y en el repositorio se encuentra el archivo
     *          miArchivo_ABC_123944.xml  , lo eliminara
     * @param directoryToExplore Ruta del directorio a explorar en busca del archivo
     * @param nameLike Cadena a buscar dentro del nombre del archivo
     * @return true en caso de encontrar y borrar un archivo, false en caso contrario
     */
    public static boolean findAndDeleteFileNameLike(String directoryToExplore, String nameLike) {
        boolean exito = false;

        try {
            File path = new File(directoryToExplore);
            if (!path.isDirectory()) {
                System.out.println("La carpeta específicada "
                        + "para hacer la busqueda del archivo no es válida.");
            }

            File[] archivos = path.listFiles();
            for (int i = 0; i < archivos.length; i++) {
                File archivo = archivos[i];

                //Compara contra el nombre del archivo, busca la cadena en su nombre
                if (archivo.getName().contains(nameLike)) {
                    //Intentamos repetidamente el borrado del archivo hasta que sea exitoso
                    for (int j=0;j<1000;j++){
                        if (archivo.delete()){
                            exito=true;
                            break;
                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            exito = false;
        }

        return exito;
    }
    
    public static String getParentPathString(File file){
        Path path = Paths.get(file.getAbsolutePath());
        return path.getParent().toString();
    }
    
    /**
     * Método para crear un objeto tipo java.io.File a partir de ByteArrayOutputStream
     * @param baos El objeto ByteArrayOutputStream con el contenido del archivo
     * @param pathFile Ruta en donde se grabara el archivo
     * @param nameFile Nombre del archivo
     * @return Objeto tipo File creado
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File createFileFromByteArrayOutputStream(ByteArrayOutputStream baos, String pathFile, String nameFile) throws FileNotFoundException, IOException {

        File newFile = new File(pathFile + nameFile);

        return createFileFromByteArrayOutputStream(baos, newFile);

    }
    
    public static File createFileFromByteArrayOutputStream(ByteArrayOutputStream baos, File newFile) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            baos.writeTo(fos);
            
            fos.flush();
        }

        return newFile;

    }
    
    
     /**
     * Returns the character encoding of an input stream containin an XML file.<br/> 
     * 
     * The encoding is detected using the guidelines specified in the
     * <a href='http://www.w3.org/TR/xml/#sec-guessing'>XML W3C Specification</a>,
     * and the method was designed to be as fast as possible, without extensive
     * string operations or regular expressions<br/> <br/> 
     * 
     * <code>
     * A sample use would be<br/><br/>
     * InputStream in = ...; <br/>
     * String encoding = detectEncoding(in);<br/>
     * BufferedReader reader = new BufferedReader(new InputStreamReader(in,encoding)); <br/>
     * </code><br/> 
     * 
     * and from that point you can happily read text from the input stream.
     * 
     * @param in
     *          Stream containing the data to be read. The stream must support
     *          mark()/reset(), otherwise the caller should wrap that stream in a
     *          {@link BufferedInputStream} before invokin the method. After the
     *          call, the stream is positioned at the &lt; character (this means
     *          that if there were any byte-order-marks, they are skipped).
     * 
     * @return Detected encoding, using the canonical name in java.io (see <a
     *         href=
     *         'http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html'>Supported
     *         Encodings</a> ).
     * 
     * @author Alexander Hristov
     * @throws java.io.IOException
     */
    public static String detectEncoding(InputStream in) throws IOException, Exception {
        String encoding = null;
        in.mark(400);
        int ignoreBytes = 0;
        boolean readEncoding = false;
        byte[] buffer = new byte[400];
        int read = in.read(buffer, 0, 4);
        switch (buffer[0]) {
            case (byte) 0x00:
                    if (buffer[1] == (byte) 0x00 && buffer[2] == (byte) 0xFE
                                    && buffer[3] == (byte) 0xFF) {
                            ignoreBytes = 4;
                            encoding = "UTF_32BE";
                    } else if (buffer[1] == (byte) 0x00 && buffer[2] == (byte) 0x00
                                    && buffer[3] == (byte) 0x3C) {
                            encoding = "UTF_32BE";
                            readEncoding = true;
                    } else if (buffer[1] == (byte) 0x3C && buffer[2] == (byte) 0x00
                                    && buffer[3] == (byte) 0x3F) {
                            encoding = "UnicodeBigUnmarked";
                            readEncoding = true;
                    }
                    break;
            case (byte) 0xFF:
                    if (buffer[1] == (byte) 0xFE && buffer[2] == (byte) 0x00
                                    && buffer[3] == (byte) 0x00) {
                            ignoreBytes = 4;
                            encoding = "UTF_32LE";
                    } else if (buffer[1] == (byte) 0xFE) {
                            ignoreBytes = 2;
                            encoding = "UnicodeLittleUnmarked";
                    }
                    break;

            case (byte) 0x3C:
                    readEncoding = true;
                    if (buffer[1] == (byte) 0x00 && buffer[2] == (byte) 0x00
                                    && buffer[3] == (byte) 0x00) {
                            encoding = "UTF_32LE";
                    } else if (buffer[1] == (byte) 0x00 && buffer[2] == (byte) 0x3F
                                    && buffer[3] == (byte) 0x00) {
                            encoding = "UnicodeLittleUnmarked";
                    } else if (buffer[1] == (byte) 0x3F && buffer[2] == (byte) 0x78
                                    && buffer[3] == (byte) 0x6D) {
                            encoding = "ASCII";
                    }
                    break;
            case (byte) 0xFE:
                    if (buffer[1] == (byte) 0xFF) {
                            encoding = "UnicodeBigUnmarked";
                            ignoreBytes = 2;
                    }
                    break;
            case (byte) 0xEF:
                    if (buffer[1] == (byte) 0xBB && buffer[2] == (byte) 0xBF) {
                            encoding = "UTF-8 con BOM";
                            ignoreBytes = 3;
                    }
                    break;
            case (byte) 0x4C:
                    if (buffer[1] == (byte) 0x6F && buffer[2] == (byte) 0xA7
                                    && buffer[3] == (byte) 0x94) {
                            encoding = "CP037";
                    }
                    break;
        }
        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }
        if (readEncoding) {
            read = in.read(buffer, 4, buffer.length - 4);
            Charset cs = Charset.forName(encoding);
            String s = new String(buffer, 4, read, cs);
            int pos = s.indexOf("encoding");
            if (pos == -1) {
                encoding = System.getProperty("file.encoding");
            } else {
                char delim;
                int start = s.indexOf(delim = '\'', pos);
                if (start == -1)
                        start = s.indexOf(delim = '"', pos);
                if (start == -1)
                        throw new Exception("Error en codificacion");//notifyEncodingError(buffer);
                int end = s.indexOf(delim, start + 1);
                if (end == -1)
                        throw new Exception("Error en codificacion");//notifyEncodingError(buffer);
                encoding = s.substring(start + 1, end);
            }
        }

        /*in.reset();
        while (ignoreBytes-- > 0)
                in.read();
        */
        in.close();
        in = null;
        
        return encoding;
    }

    /**
     * Obtiene la extensión de un archivo
     * @param f Nombre del archivo
     * @return Extensión
     */
    public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }
    
    /**
     * Obtiene el nombre del archivo sin extensión
     * @param fileName Nombre del archivo completo
     * @return Nombre sin extension
     */
    public static String getFileNameWithoutExtension(String fileName){
        String name = fileName;
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        return name;
    }
    
    /**
     * Calcula el hash MD5 de un archivo
     * @param file Archivo origen
     * @return Cadena de HASH MD5 Checksum del archivo
     * @throws Exception 
     */
    public static String getMD5ChecksumFromFile(File file) throws Exception {
        String md5 = null;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        }catch(IOException ex){
            ex.printStackTrace();
        }finally{
            if (fis!=null)
                fis.close();
        }
                
        return md5;
    }
    
    /**
     * Obtiene un arreglo con los archivos contenidos en un directorio
     * en order Ascendente por Fecha de Modificación, usando el filtro
     * de nombres enviado como parametro opcionalmente.
     * @param folder File apuntador a directorio carpeta
     * @param filenameFilter Filtro de nombres de archivos, null en caso de no requerir filtro
     * @return Arreglo de archivos ordenados
     */
    public static File[] dirListByAscendingDate(File folder, FilenameFilter filenameFilter) {
        if (!folder.isDirectory()) {
            return null;
        }
        File files[] = folder.listFiles(filenameFilter);
        Arrays.sort(files, new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return new Long(((File) o1).lastModified()).compareTo(new Long(((File) o2).lastModified()));
            }
        });
        return files;
    }

    /**
     * Obtiene un arreglo con los archivos contenidos en un directorio
     * en order Descendente por Fecha de Modificación, usando el filtro
     * de nombres enviado como parametro opcionalmente.
     * @param folder File apuntador a directorio carpeta
     * @param filenameFilter Filtro de nombres de archivos, null en caso de no requerir filtro
     * @return Arreglo de archivos ordenados
     */
    public static File[] dirListByDescendingDate(File folder, FilenameFilter filenameFilter) {
        if (!folder.isDirectory()) {
            return null;
        }
        File files[] = folder.listFiles(filenameFilter);
        Arrays.sort(files, new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return new Long(((File) o2).lastModified()).compareTo(new Long(((File) o1).lastModified()));
            }
        });
        return files;
    }
    
    /**
     * Obtiene un arreglo con los archivos contenidos en un directorio
     * en order Ascendente por Nombre de archivos, usando el filtro
     * de nombres enviado como parametro opcionalmente.
     * @param folder File apuntador a directorio carpeta
     * @param filenameFilter Filtro de nombres de archivos, null en caso de no requerir filtro
     * @return Arreglo de archivos ordenados
     */
    public static File[] dirListByAscendingName(File folder, FilenameFilter filenameFilter) {
        if (!folder.isDirectory()) {
            return null;
        }
        File files[] = folder.listFiles(filenameFilter);
        Arrays.sort(files, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
        return files;
    }
    
    /**
     * Obtiene un arreglo con los archivos contenidos en un directorio
     * en order Descendente por Nombre de archivos, usando el filtro
     * de nombres enviado como parametro opcionalmente.
     * @param folder File apuntador a directorio carpeta
     * @param filenameFilter Filtro de nombres de archivos, null en caso de no requerir filtro
     * @return Arreglo de archivos ordenados
     */
    public static File[] dirListByDescendingName(File folder, FilenameFilter filenameFilter) {
        if (!folder.isDirectory()) {
            return null;
        }
        File files[] = folder.listFiles(filenameFilter);
        Arrays.sort(files, NameFileComparator.NAME_INSENSITIVE_REVERSE);
        return files;
    }
    
    
    public static boolean createFileString(File file, String content){
        try{
            FileWriter fileWrite=new FileWriter(file,true);

            //Escribimos en el archivo con el metodo write
            fileWrite.write(content);

            //Cerramos la conexion
            fileWrite.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
