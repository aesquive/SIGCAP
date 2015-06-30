package reports.excelmaker;

import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class ExcelMaker extends Thread{

    private String fileName;
    private String baseName;
    private Map<String, Cuenta> datos;
    private Regcuenta reg;
    private String threadName;
    
    
    
    public ExcelMaker (String threadName,String fileName, String baseName, Regcuenta reg,Map<String, Cuenta> datos) {
        this.threadName=threadName;
        this.fileName = fileName;
        this.baseName = baseName;
        this.datos = datos;
        this.reg=reg;
    }
    
    @Override
    public void run() {
        makeFile();
    }

    public void makeFile(){
        try {
            File file = new File(baseName);
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);
            int maxRows = 200;
            int maxColumns = 120;
            for (int t = 0; t < maxRows; t++) {
                Row row = sheet.getRow(t);
                for (int c = 0; c < maxColumns; c++) {
                    Cell cell = null;
                    if (row != null) {
                        cell = row.getCell(c);
                    }
                    if (cell != null) {
                        boolean isFormula = isFormula(cell);
                        if (!isFormula) {
                            String cellContents = getValue(cell);
                            if (!cellContents.equals("") && cellContents.charAt(0) == '?') {
                                mapGetValue(cell,cellContents.substring(1, cellContents.length()));
                            }
                        }else{
                            cell.setCellFormula(cell.getCellFormula());
                        }
                    }
                }
            }
            File fileOutput=new File(getFileName());
            FileOutputStream fileOut = new FileOutputStream(fileOutput);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(ExcelMaker.class.getName()).log(Level.INFO, null, ex);
        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException, InvalidFormatException {
        List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
        Map<String, Cuenta> map = new HashMap<String, Cuenta>();
        for (Cuenta c : createQuery) {
            map.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(), c);
        }
    }

    private String getValue(Cell cell) {

        try {
            return cell.getStringCellValue();
        } catch (IllegalStateException ex) {
            return String.valueOf(cell.getNumericCellValue());
        }

    }

    private void mapGetValue(Cell cell,String substring) {
        Cuenta get = datos.get(substring);
        if(get==null){
            cell.setCellValue("");
        }
        if (get != null) {
            cell.setCellValue(get.getValor());
        }
        if(substring.equalsIgnoreCase("fecha")){
            cell.setCellValue(reg.getFecha());
        }
    }

    private boolean isFormula(Cell cell) {
        try{
            cell.getCellFormula();
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            return true;
        }catch(IllegalStateException is){
            return false;
        }
    }

    /**
     * @return the threadName
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * @param threadName the threadName to set
     */
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    

}
