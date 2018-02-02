package com.fens.desktopMonitor.util;

import com.cmm.cvs2xml.util.StringManage;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.StringTokenizer;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatUtil {

	public static String doubleToString(double numero) {
		DecimalFormat formateador = new DecimalFormat("#0.00;-#0.00");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		formateador.setDecimalFormatSymbols(dfs);
		return formateador.format(numero);
	}
	
	public static String doubleToStringPuntoComas(double numero){		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		//DecimalFormat formateador = new DecimalFormat("###,###,###,###.000",simbolos);
		//DecimalFormat formateador = new DecimalFormat("#,##0.00;(#,##0.00)",simbolos);
		DecimalFormat formateador = new DecimalFormat("#,##0.00;-#,##0.00",simbolos);
		return formateador.format(numero);
	}

	public static String formatoCadenaOriginal(String cadena) {		
		if (cadena == null || "".equals(cadena)) {			
			return "";
		}
		if(cadena != null){
			cadena = cadena.trim();
			if("".equals(cadena)){
				return "";
			}
		}
		cadena = cadena.replaceAll(String.valueOf('\n'), " ");
		cadena = cadena.replaceAll(String.valueOf('\u0009'), " ");
		cadena = cadena.replaceAll(String.valueOf('\r'), " ");
		while (cadena.contains("  ")) {
			cadena = cadena.replace("  ", " ");
		}
		cadena = cadena.trim();
		return cadena + "|";
	}

	public static String formatoSelloDigitalXML(String selloDigital) {
		if (selloDigital == null || "".equals(selloDigital)) {
			return "";
		}
		selloDigital = selloDigital.replaceAll(String.valueOf('\n'), "");
		selloDigital = selloDigital.replaceAll(String.valueOf('\r'), "");
		selloDigital.trim();
		return selloDigital;
	}

	public static String formatoCadenaOriginal(int cadena) {
		return formatoCadenaOriginal(String.valueOf(cadena));
	}

	public static String formatoCadenaOriginal(double cadena) {
		if (cadena != 0) {
			return formatoCadenaOriginal(doubleToString(cadena));
		}
		return "";		
	}
	
	public static String formatoCadenaOriginalConcepto(double cadena) {
		//if (cadena != 0) {
			return formatoCadenaOriginal(doubleToString(cadena));
		//}
		//return "";		
	}
	
	public static String formatoCadenaOriginalImpuesto(double cadena) {
		//if (cadena != 0) {
			return formatoCadenaOriginal(doubleToString(cadena));
		//}
		//return "";		
	}
	
	public static String formatoCadenaOriginalTasaCero(double cadena) {
		return formatoCadenaOriginal(doubleToString(cadena));		
	}
        
	public static String formatoNumero(int numero, int digitos) {
		String respuesta = String.valueOf(numero);
		for (int i = 0; i < digitos; i++) {
			if (respuesta.length() < digitos) {
				respuesta = "0" + respuesta;
			}
		}
		return respuesta;
	}

	/**
	 * Metodo que convierte un input stream con la llave privada o publica a un
	 * array de bytes
	 * 
	 * @param is
	 *            InputSteam con la clave privada
	 * @return Arreglo de bytes con la clave privada
	 */
	/*public static byte[] getBytes(InputStream is) {
		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/

	public static String formatRounded(double numero, int decimales) {
		String decimalesStr = "";
		for (int i = 0; i < decimales; i++) {
			decimalesStr += "#";
		}
		DecimalFormat format = new DecimalFormat("###." + decimalesStr);
		return format.format(numero);
	}


	public static String noCertificadoToString(BigInteger noCertificadoHex) {
		String hexa = noCertificadoHex.toString(16);
		String resultado = "";
		while (hexa.length() > 0) {
			resultado += String.valueOf((char) Integer.parseInt(hexa.substring(0, 2), 16));
			hexa = hexa.substring(2, hexa.length());
		}
		return resultado;
	}
	
	public static String formatoCadenaOriginalSAT(String cadena) {
		if (cadena != null) {
			String cadOrFormat = "";
			cadOrFormat = "|";
			StringTokenizer st = new StringTokenizer(cadena, "|");
			while (st.hasMoreTokens()) {
				cadOrFormat += "|" + st.nextToken();
			}
			return cadOrFormat+"||";
		}
		return cadena;
	}
	
	public static String formatoCadenaOriginalCero(double cadena){		
			return formatoCadenaOriginal(doubleToString(cadena));		
	}
        
        public static BigDecimal obtenerTasaRetencion(BigDecimal importe, BigDecimal subtotal){
            if (subtotal.doubleValue()!=0){
                double tasa = (importe.doubleValue()/subtotal.doubleValue())*100;
                BigDecimal tasabd = new BigDecimal(tasa).setScale(2, RoundingMode.HALF_UP);
                return tasabd;
            }else{
                return BigDecimal.ZERO;
            }
        }
        
        /**
         * Recibe el valor del CFDI del atributo metodoDePago.
         * Lo analiza y revisa si tiene varios metodos de pago indicados de acuerdo al SAT,
         * separados por coma. 
         * P. ej: "01,02,04"
         * Retorna el texto correspondiente a dichos métodos de pago, separado 
         * por coma en caso de ser mas de uno. 
         * P. ej: "Efectivo, Cheque nominativo, Tarjeta de Crédito"
         * 
         * En caso de ser un solo método de pago, retorna el nombre unico
         * asociado de acuerdo a catalogo de SAT. P. ej. "Efectivo"
         * 
         * Si el valor no corresponde a ninguno del catalogo, se marca al inicio
         * y al final con el texto " - ' " y se copia el texto tal cual.
         * P. ej: "- 'Tarjeta Oxxo' -"
         * @param metodoDePago dato metodoDePago del CFDI
         * @return Cadena con el nombre correspondiente al o los metodo(s) de pago
         */
        public static String getNombreMetodosPagoCatalogoSAT(String metodoDePago) {
            String textoMetodoPago = "";
            String[] metodosPago = GenericMethods.getArrayData(StringManage.getValidString(metodoDePago), ",");
            
            int cantidadMetodosPagoUsados = metodosPago.length;
            int i = 0;
            for (String mp : metodosPago){
                int claveMetodoPago = -1;
                try{ claveMetodoPago = Integer.parseInt(mp); }catch(Exception ex){}
                
                switch (claveMetodoPago){
                    case 1:
                        textoMetodoPago += "Efectivo";
                        break;
                    case 2:
                        textoMetodoPago += "Cheque nominativo";
                        break;
                    case 3:
                        textoMetodoPago += "Transferencia electrónica de fondos";
                        break;
                    case 4:
                        textoMetodoPago += "Tarjeta de Crédito";
                        break;
                    case 5:
                        textoMetodoPago += "Monedero Electrónico";
                        break;
                    case 6:
                        textoMetodoPago += "Dinero electrónico";
                        break;
                    case 8:
                        textoMetodoPago += "Vales de despensa";
                        break;
                    case 28:
                        textoMetodoPago += "Tarjeta de Débito";
                        break;
                    case 29:
                        textoMetodoPago += "Tarjeta de Servicio";
                        break;
                    case 99:
                        textoMetodoPago += "Otros";
                        break;
                    default:
                        textoMetodoPago += "- '" + mp + "' -";
                        break;
                }
                
                if ( (i==0 && cantidadMetodosPagoUsados>1) || (i>0 && i<(cantidadMetodosPagoUsados-1))  ){
                    //si se utilizo mas de un metodo de pago, en los intermedios agregamos una coma
                    textoMetodoPago += ",";
                }
                
                i++;
            }
            
            return textoMetodoPago;
        }
}
