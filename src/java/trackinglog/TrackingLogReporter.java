package trackinglog;

import db.controller.DAO;
import db.pojos.Tracking;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.NDimensionVector;

/**
 *
 * @author Admin
 */
public class TrackingLogReporter {

    public static String generateReport(String fileName, Date initDate, Date lastDate) throws IOException {
        List<Tracking> createQuery = DAO.createQuery(Tracking.class, null);
        List<Tracking> trackingList = new LinkedList<Tracking>();
        setHour(initDate,0);
        setHour(lastDate,0);
        for (Tracking t : createQuery) {
            setHour(t.getFecha(),0);
            if (t.getFecha().compareTo(initDate) >= 0 && t.getFecha().compareTo(lastDate) <= 0) {
                System.out.println("entra con id " + t.getIdTracking());
                trackingList.add(t);
            }
        }
        saveFile(trackingList, new File(manager.configuration.Configuration.getValue("Ruta Reportes") + fileName));
        return fileName;
    }

    private static void saveFile(List<Tracking> trackingList, File file) throws IOException {
        FileInputStream fis = new FileInputStream(new File(manager.configuration.Configuration.getValue("baseTracking")));
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);
        CellReference cr = new CellReference("B8");
        Row row = sheet.getRow(cr.getRow());
        Cell cell = row.getCell(cr.getCol());
        int indexTracking = 0;
        while (row != null && indexTracking < trackingList.size()) {
            Tracking tracking = trackingList.get(indexTracking);
            cell.setCellValue(tracking.getUser().getUser());
            cell = row.getCell(cr.getCol() + 1);
            cell.setCellValue(tracking.getFecha());
            cell = row.getCell(cr.getCol() + 2);
            cell.setCellValue(tracking.getFecha());
            cell = row.getCell(cr.getCol() + 3);
            cell.setCellValue(tracking.getDes());
            indexTracking++;
            row = sheet.getRow(cr.getRow() + indexTracking);
            cell = row.getCell(cr.getCol());
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

    private static void setHour(Date fecha, int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(fecha);
        instance.set(Calendar.HOUR, i);
    }
}
