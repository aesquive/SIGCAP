package reports.excelmaker;

import db.controller.DAO;
import db.pojos.Tracking;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class TrackingLogReporter {

    public static String generateReport(String fileName, Date initDate, Date lastDate) throws IOException {
        List<Tracking> createQuery = DAO.createQuery(Tracking.class, null);
        List<Tracking> trackingList = new LinkedList<Tracking>();
        setField(Calendar.HOUR, initDate, 0);
        setField(Calendar.MINUTE, initDate, 0);
        setField(Calendar.SECOND, initDate, 0);
        setField(Calendar.MILLISECOND, initDate, 0);
        setField(Calendar.HOUR, lastDate, 0);
        setField(Calendar.MINUTE, lastDate, 0);
        setField(Calendar.SECOND, lastDate, 0);
        setField(Calendar.MILLISECOND, lastDate, 0);
        for (Tracking t : createQuery) {
            Date fecha = t.getFecha();
            setField(Calendar.HOUR, fecha, 0);
            setField(Calendar.MINUTE, fecha, 0);
            setField(Calendar.SECOND, fecha, 0);
            setField(Calendar.MILLISECOND, fecha, 0);
            if (fecha.compareTo(initDate) >= 0 && fecha.compareTo(lastDate) <= 0) {
                System.out.println("entra con id " + t.getIdTracking());
                trackingList.add(t);
            }
        }
        System.out.println("saving the file");
        saveFile(trackingList, new File(manager.configuration.Configuration.getValue("Ruta Reportes") + fileName));
        return fileName;
    }

    private static void saveFile(List<Tracking> trackingList, File file) throws IOException {
        FileInputStream fis = new FileInputStream(new File(manager.configuration.Configuration.getValue("baseTracking")));
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);
        CellReference cr = new CellReference("B8");
        System.out.println("la row es " + cr.getRow());
        Cell cell = null;
        int sumRow = 0;
        CellStyle cellStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy  hh:mm"));
        for (Tracking tr : trackingList) {
            Row row = sheet.createRow(cr.getRow() + sumRow);
            cell = row.createCell(cr.getCol());
            cell.setCellValue(tr.getUser().getUser());
            cell = row.createCell(cr.getCol() + 1);
            cell.setCellValue(tr.getFecha());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(cr.getCol() + 2);
            cell.setCellValue(tr.getDes());
            sumRow++;
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

    }

    public static void main(String[] args) throws IOException {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.DAY_OF_MONTH, 4);
        TrackingLogReporter.generateReport("tracking.xlsx", instance.getTime(), Calendar.getInstance().getTime());
    }

    private static void setField(int field, Date fecha, int i) {
        Calendar instance = Calendar.getInstance();

        instance.setTime(fecha);
        instance.set(field, i);
    }
}
