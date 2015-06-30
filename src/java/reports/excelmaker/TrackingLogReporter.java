package reports.excelmaker;

import db.controller.DAO;
import db.pojos.Tracking;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.Util;

/**
 *
 * @author Admin
 */
public class TrackingLogReporter {

    public static String generateReport(String fileName, Date initDate, Date lastDate) throws IOException {
        List<Tracking> createQuery = DAO.createQuery(Tracking.class, null);
        List<Tracking> trackingList = new LinkedList<Tracking>();
        for (Tracking t : createQuery) {
            Date fecha = t.getFecha();
            int diasVsInicial = Util.daysBetweenDates(initDate,fecha);
            int diasVsFinal=Util.daysBetweenDates(lastDate,fecha);
            //System.out.println(fecha+" vsINi "+diasVsInicial +" vsfin "+diasVsFinal);
            if (diasVsInicial >= 0 && diasVsFinal <= 0) {
                System.out.println("entra con fecha " + t.getFecha());
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
        //instance.set(Calendar.DATE,instance.get(Calendar.DATE)-10);
        TrackingLogReporter.generateReport("tracking.xlsx", instance.getTime(), Calendar.getInstance().getTime());
    }

    private static void setField(int field, Date fecha, int i) {
        Calendar instance = Calendar.getInstance();

        instance.setTime(fecha);
        instance.set(field, i);
    }
}
