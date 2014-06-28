package reports.excelmaker;

import db.pojos.Consistencia;
import db.pojos.Regcuenta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class ConsistenciaReportMaker {

    public static String makeReport(String originalFile, Consistencia consistencias, Regcuenta regcuenta) {
        try {
            FileInputStream fis = new FileInputStream(originalFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);
            setCellValue(sheet, "D6", regcuenta.getDesRegCuenta());
            setCellValue(sheet, "D8", consistencias.getCaptacionLeidos());
            setCellValue(sheet, "D9", consistencias.getDisponibilidadesLeidos());
            setCellValue(sheet, "D10", consistencias.getIngresosLeidos());
            setCellValue(sheet, "D11", consistencias.getPrestamosLeidos());
            setCellValue(sheet, "D12", consistencias.getReservasLeidos());
            setCellValue(sheet, "D13", consistencias.getTarjetaCreditoLeidos());
            setCellValue(sheet, "D14", consistencias.getTenenciaLeidos());
            setCellValue(sheet, "F8", regcuenta.getCaptacions().size());
            setCellValue(sheet, "F9", regcuenta.getDisponibilidads().size());
            setCellValue(sheet, "F10", regcuenta.getIngresosnetoses().size());
            setCellValue(sheet, "F11", regcuenta.getPrestamos().size());
            setCellValue(sheet, "F12", regcuenta.getReservases().size());
            setCellValue(sheet, "F13", regcuenta.getTarjetacreditos().size());
            setCellValue(sheet, "F14", regcuenta.getValoreses().size());
            String fileName="consistencia-"+regcuenta.getIdRegCuenta()+".xlsx";
            FileOutputStream fileOut = new FileOutputStream(new File(Configuration.getValue("Ruta Reportes")+fileName));
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            return fileName;
        } catch (Exception ex) {
            Logger.getLogger(ConsistenciaReportMaker.class.getName()).log(Level.SEVERE, null, ex);
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
    }

}
