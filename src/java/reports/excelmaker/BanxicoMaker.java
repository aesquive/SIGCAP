
package reports.excelmaker;

import db.controller.DAO;
import db.pojos.*;
import java.io.*;
import java.util.*;
import manager.configuration.Configuration;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import util.Util;

public class BanxicoMaker {
    
     public static String report(Regcuenta reg) throws IOException {

        HashMap<String, Double> cuentas = mapCuentas(reg.getCuentas());
         List<String> readFile = Util.readFile(Configuration.getValue("baseApache")+"cuentas.txt");
        
        return writeFile( readFile, cuentas, reg);
    }

    private static HashMap<String, Double> mapCuentas(Set<Cuenta> cuentas) {
        HashMap<String, Double> answer = new HashMap<String, Double>();
        for (Cuenta c : cuentas) {
            answer.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(), c.getValor() == null ? 0.0 : c.getValor());
        }
        return answer;
    }

    /**
     * las cuentas vienen separadas por comas, cada elemento de la lista es un rc
     * @param listaCuentas
     * @param cuentas
     * @param regcta
     * @return
     * @throws IOException 
     */
    private static String writeFile( List<String> listaCuentas, HashMap<String, Double> cuentas, Regcuenta regcta) throws IOException {

        String[] names = new String[]{"RC01", "RC02", "RC03", "RC04", "RC05", "RC06", "RC07", "RC08", "RC09", "RC10", "RC12", "RC13"};
        XSSFWorkbook wb = new XSSFWorkbook();
        CellStyle number = wb.createCellStyle();
        number.setDataFormat(wb.createDataFormat().getFormat("#########.###"));
        CellStyle dateFormat = wb.createCellStyle();
        dateFormat.setDataFormat(wb.createDataFormat().getFormat("yyyy/MM/dd"));
        int consec = 0;
        String codigo = Configuration.getValue("clientId");
        for (String ctasString : listaCuentas) {
            List<Integer> ctasRc=generarCuentasRc(ctasString);
            XSSFSheet sheet = wb.createSheet(names[consec]);
            int actualRow = 0;
            for (Integer cta : ctasRc) {
                Double valorCuenta = cuentas.get(cta.toString());
                if (valorCuenta != null && valorCuenta != 0) {
                    XSSFRow row = sheet.createRow(actualRow);
                    XSSFCell cell = row.createCell(0);
                    cell.setCellValue(codigo);
                    cell = row.createCell(1);
                    cell.setCellValue(regcta.getFecha());
                    cell.setCellStyle(dateFormat);
                    cell = row.createCell(2);
                    cell.setCellValue(cta.toString());
                    cell = row.createCell(3);
                    cell.setCellValue(valorCuenta);
                    cell.setCellStyle(number);
                    cell = row.createCell(4);
                    cell.setCellValue("0.00");
                    actualRow++;
                }
            }
            consec++;
        }

        String originalName = "Banxico-" + regcta.getIdRegCuenta().toString() + ".xlsx";
        File fileOutput = new File(manager.configuration.Configuration.getValue("Ruta Reportes") + originalName);
        FileOutputStream fileOut = new FileOutputStream(fileOutput);
        wb.write(fileOut);

        fileOut.flush();

        fileOut.close();
        return originalName;
    }

    
    public static void main(String[] args) throws IOException {
        
        List<Regcuenta> ejerciciosCalculados = DAO.getEjerciciosCalculados();
        BanxicoMaker.report(ejerciciosCalculados.get(0));
    }

    private static List<Integer> generarCuentasRc(String ctasString) {
        List<Integer> ctas=new LinkedList<Integer>();
        String[] split = ctasString.split(",");
         for(String cta:split){
             if(!cta.equals("")){
                 try{
                     ctas.add(Integer.parseInt(cta));
                 }catch(Exception ex){
                     ex.printStackTrace();
                 }
             }
         }
         return ctas;
    }
    
}
