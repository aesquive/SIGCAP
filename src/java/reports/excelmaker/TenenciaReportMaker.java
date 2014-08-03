package reports.excelmaker;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import util.Reflector;
import util.Util;

/**
 *
 * @author Admin
 */
public class TenenciaReportMaker {

    public static String makeReport(String originalFile, Regcuenta regcuenta) {
        try {
            FileInputStream fis = new FileInputStream(originalFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);
            setCellValue(sheet, "D6", regcuenta.getDesRegCuenta());
            Set<Valores> setVals = regcuenta.getValoreses();
            List<Valores> valoreses = Util.sortValoresBy(new LinkedList<Valores>(setVals), "TipoValorEmisionSerie", true);
            CellReference ref=new CellReference("A9");
            String[] tenenciaDesColumns = Valores.getTenenciaReport();
            int numberRegisters=0;
            for(Valores val:valoreses){
                Row createRow = sheet.createRow(ref.getRow()+numberRegisters);
                for(int t=0;t<tenenciaDesColumns.length;t++){
                    Object callMethod = Reflector.callMethod(val, null,"get"+tenenciaDesColumns[t]);
                    Cell cell=createRow.createCell(t);
                    setCellValue(cell, callMethod);
                }
                numberRegisters++;
            }
            String fileName = "tenencia-" + regcuenta.getIdRegCuenta() + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(new File(Configuration.getValue("Ruta Reportes") + fileName));
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            
            return fileName;
        } catch (Exception ex) {
            Logger.getLogger(TenenciaReportMaker.class.getName()).log(Level.INFO, null, ex);
        }
        return null;

    }

    public static void setCellValue(Sheet sheet, String cellRef, Object value) {
        CellReference cr = new CellReference(cellRef);
        Row row = sheet.getRow(cr.getRow());
        Cell cell = row.getCell(cr.getCol());
        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            return;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
            return;
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
            return;
        }
        if (value instanceof Integer) {
            Double d = new Double(String.valueOf(value));
            cell.setCellValue(d);
            return;
        }
    }

    
        public static void setCellValue(Cell cell, Object value) {
        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            return;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
            return;
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
            return;
        }
        if (value instanceof Integer) {
            Double d = new Double(String.valueOf(value));
            cell.setCellValue(d);
            return;
        }
    }

}
