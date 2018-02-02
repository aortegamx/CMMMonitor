
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.fens.desktopMonitor.config.Configuration;
import java.util.Properties;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 578
 */
public class testBD {
    
    
    private static Connection conn = null;
    private static Configuration config = new Configuration();
        
    public static void main(String[] args){
       try{

             //obtenemos el driver de para derby
             Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
             
            //obtenemos la conexión                        
            Properties properties = new Properties();
            //properties.put("create", "true");
            properties.put("user", config.getJdbc_user());
            //properties.put("password", config.getJdbc_password());

            conn = DriverManager.getConnection(config.getJdbc_url(), properties);
            
             if (conn!=null){
                JOptionPane.showMessageDialog(null,"OK Base de Datos. Lista!");
             }
             
          }catch(SQLException e){
           JOptionPane.showMessageDialog(null,e.getMessage(),"Error" ,  JOptionPane.ERROR_MESSAGE);
          }catch(ClassNotFoundException e){
             JOptionPane.showMessageDialog(null,e.getMessage(),"Error" ,  JOptionPane.ERROR_MESSAGE);
          }
       
    }
    
  
    
    
}
