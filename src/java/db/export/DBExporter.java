
package db.export;

import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;

/**
 *
 * @author desarrollo
 */
public class DBExporter {

    
    
    
    /**
     * Guarda una exportacion de la base de datos dentro del archivo que se pase como parametro.
     * @param fileName 
     */
    public static void export(String fileName){
        String dbUser=Configuration.getValue("dbUser");
        String dbName=Configuration.getValue("dbName");
        try {
            String executed="";
            executed="pg_dump -U "+dbUser+" "+dbName+" -f "+fileName+ " -h localhost";
            
            Process runtimeProcess=Runtime.getRuntime().exec(executed);
            int processComplete=runtimeProcess.waitFor();
            if(processComplete==0){
                System.out.println("Success");;
            }else{
                System.out.println("Fail");
            }
        } catch (Exception ex) {
            Logger.getLogger(DBExporter.class.getName()).log(Level.INFO, null, ex);
        }
    }
    
    public static void main(String[] args) {
        DBExporter.export("/var/www/html/SIGCAP/respaldos/sigcapexport.sql");
        
    }
    
    
}
