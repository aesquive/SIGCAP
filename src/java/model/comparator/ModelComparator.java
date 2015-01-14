package model.comparator;

import db.controller.DAO;
import db.pojos.Catalogocuenta;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.CompareObjectReport;
import util.NDimensionVector;

/**
 *
 * @author Admin
 */
public class ModelComparator {

    public static String compareWriteFile(String baseName, String newFileName, Regcuenta first, Collection<Cuenta> cuentasFirst, Regcuenta second, Collection<Cuenta> cuentasSecond, Double minVariance, int numberRegisters) throws IOException {
        List<CompareObjectReport> compareProjects = null;
        if (first != null && second != null) {
            compareProjects = first.compareProjects(second, minVariance, numberRegisters, cuentasFirst, cuentasSecond);
        }
        Collections.sort(compareProjects);
        return writeFile(baseName, newFileName, compareProjects, first, second);
    }

    private static String writeFile(String baseName, String newFileName, List<CompareObjectReport> list, Regcuenta first, Regcuenta second) throws IOException {
        File file = new File(baseName);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);
        CellReference cr1 = new CellReference("D7");
        Row row1 = sheet.getRow(cr1.getRow());
        Cell cell1 = row1.getCell(cr1.getCol());
        cell1.setCellValue(first.getFecha());
        CellReference cr2 = new CellReference("F7");
        Row row2 = sheet.getRow(cr2.getRow());
        Cell cell2 = row2.getCell(cr2.getCol());
        cell2.setCellValue(second.getFecha());
        CellReference cr3 = new CellReference("F6");
        Row row3 = sheet.getRow(cr3.getRow());
        Cell cell3 = row3.getCell(cr3.getCol());
        cell3.setCellValue(second.getDesRegCuenta());
        CellReference cr4 = new CellReference("D6");
        Row row4 = sheet.getRow(cr4.getRow());
        Cell cell4 = row4.getCell(cr4.getCol());
        cell4.setCellValue(first.getDesRegCuenta());
        CellReference cr = new CellReference("A8");
        Row row = sheet.getRow(cr.getRow());
        Cell cell = row.getCell(cr.getCol());
        int numberRows = 0;

        for (int t = 0; t < list.size(); t++) {
            CompareObjectReport comp = list.get(t);
            if (comp != null && comp.getCuenta() != null && comp.getCuenta().getIdCatalogoCuenta() != null
                    && (comp.getValorPrimero() != Double.NaN && comp.getValorSegundo() != Double.NaN)
                    && (comp.getValorPrimero() != 0 && comp.getValorSegundo() != 0) &&
                    (comp.getValorPrimero()!=null && comp.getValorSegundo()!=null) && 
                    comp.getRazon()!=0 ) {

                cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellValue(comp.getCuenta().getIdCatalogoCuenta());
                cell = row.getCell(2);
                if (cell == null) {
                    cell = row.createCell(2);
                }
                cell.setCellValue(comp.getCuenta().getDesCatalogoCuenta());
                if (comp.getValorPrimero() != Double.NaN && comp.getValorPrimero()!=null) {
                    cell = row.getCell(3);
                    if (cell == null) {
                        cell = row.createCell(3);
                    }
                    cell.setCellValue(comp.getValorPrimero());
                }
                if (comp.getValorSegundo() != Double.NaN && comp.getValorSegundo()!=null) {
                    cell = row.getCell(5);
                    if (cell == null) {
                        cell = row.createCell(5);
                    }
                    cell.setCellValue(comp.getValorSegundo());
                }

                cell = row.getCell(7);
                if (cell == null) {
                    cell = row.createCell(7);
                }
                cell.setCellValue(Math.abs(comp.getRazon()));
                numberRows++;
                row = sheet.getRow(cr.getRow() + numberRows);

                if (row == null) {
                    row = sheet.createRow(cr.getRow() + numberRows);
                }
            }
        }
        int lastRow = 7 + numberRows;
        Row row5 = sheet.getRow(lastRow);
        while (row5 != null) {
            sheet.removeRow(row5);
            lastRow++;
            row5 = sheet.getRow(lastRow);
        }
        File fileOutput = new File(manager.configuration.Configuration.getValue("Ruta Reportes") + newFileName + "-" + first.getIdRegCuenta().toString() + "-" + second.getIdRegCuenta() + ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(fileOutput);

        wb.write(fileOut);

        fileOut.flush();

        fileOut.close();
        return newFileName
                + "-" + first.getIdRegCuenta()
                .toString() + "-" + second.getIdRegCuenta() + ".xlsx";
    }

    private static Map<String, Catalogocuenta> mapCuentas(List<Catalogocuenta> createQuery) {
        Map<String, Catalogocuenta> map = new HashMap<String, Catalogocuenta>();
        for (Catalogocuenta c : createQuery) {
            map.put(c.getIdCatalogoCuenta().toString(), c);
        }
        return map;
    }

    public static
            void main(String[] args) throws IOException {
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        //ModelComparator.compareWriteFile(manager.configuration.Configuration.getValue("baseAnalisisComparativo"), createQuery.get(0), createQuery.get(1), 0.0, -1);
    }

    private static void imprimirMap(Map<Integer, NDimensionVector> compareProjects) {
        Set<Integer> keySet = compareProjects.keySet();
        for (Integer t : keySet) {

            System.out.println(t);
            NDimensionVector get = compareProjects.get(t);
            System.out.println(get);
        }
    }

}
