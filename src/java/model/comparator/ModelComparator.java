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
import util.NDimensionVector;

/**
 *
 * @author Admin
 */
public class ModelComparator {

    public static String compareWriteFile(String baseName, String newFileName, Regcuenta first, Collection<Cuenta> cuentasFirst, Regcuenta second, Collection<Cuenta> cuentasSecond, Double minVariance, int numberRegisters) throws IOException {
        Map<Integer, NDimensionVector> compareProjects = null;
        if (first != null && second != null) {
            compareProjects = first.compareProjects(second, minVariance, numberRegisters, cuentasFirst, cuentasSecond);
        }
        return writeFile(baseName, newFileName, compareProjects, first, second);
    }

    private static String writeFile(String baseName, String newFileName, Map<Integer, NDimensionVector> map, Regcuenta first, Regcuenta second) throws IOException {
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
        Map<String, Catalogocuenta> ctas = mapCuentas(DAO.createQuery(Catalogocuenta.class, null));
        int numberRows = 0;
        for (int t = 0; t < map.size(); t++) {
            NDimensionVector vals = map.get(t);
            List values = vals.getValues();
            int data = 4;
            Set<String> conj = new HashSet<String>();
            for (int d = 0; d < values.size(); d++) {
                int mod = d % data;
                Object value = values.get(d);
                if (!conj.contains(values.get(0).toString())) {

                    switch (mod) {
                        case 0:
                            if (((Double) values.get(d + 3)) == 0) {

                            } else {
                                cell = row.getCell(0);
                                String idCta = (String) value;
                                Catalogocuenta catCta = ctas.get(idCta);
                                cell.setCellValue(catCta.getIdCatalogoCuenta());
                                cell = row.getCell(2);
                                cell.setCellValue(catCta.getDesCatalogoCuenta());
                                numberRows++;
                            }
                            break;
                        case 1:
                            if (((Double) values.get(d + 2)) == 0) {

                            } else {
                                Double valor = (Double) value;
                                cell = row.getCell(3);
                                cell.setCellValue(valor);
                            }
                            break;

                        case 2:
                            if (((Double) values.get(d + 1)) == 0) {

                            } else {
                                Double valor2 = (Double) value;
                                cell = row.getCell(5);
                                cell.setCellValue(valor2);
                            }
                            break;
                        case 3:
                            Double valor4 = (Double) value;
                            if (valor4.isNaN() || valor4.isInfinite()) {
                                cell = row.getCell(7);
                                cell.setCellValue("NA");
                                cell = row.getCell(9);
                                cell.setCellValue("La variación no esta definida");

                            } else if (valor4 != 0) {
                                cell = row.getCell(7);
                                cell.setCellValue(valor4);
                                cell = row.getCell(9);
                                if (((Double) values.get(d - 1)).isNaN() || ((Double) values.get(d - 1)).isInfinite()) {
                                    cell.setCellValue("El valor del primer ejercicio no esta definido");
                                } else if (((Double) values.get(d - 2)).isNaN() || ((Double) values.get(d - 1)).isInfinite()) {
                                    cell.setCellValue("El valor del segundo ejercicio no esta definido");
                                }
                            }
                            conj.add((String) values.get(d - 3));
                            break;
                    }
                }
            }
            row = sheet.getRow(cell.getRowIndex() + 1);
        }
        int lastRow = 7 + numberRows;
        row = sheet.getRow(lastRow);
        while (row != null) {
            sheet.removeRow(row);
            lastRow++;
            row = sheet.getRow(lastRow);
        }
        File fileOutput = new File(manager.configuration.Configuration.getValue("Ruta Reportes") + newFileName + "-" + first.getIdRegCuenta().toString() + "-" + second.getIdRegCuenta() + ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(fileOutput);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        return newFileName + "-" + first.getIdRegCuenta().toString() + "-" + second.getIdRegCuenta() + ".xlsx";
    }

    private static Map<String, Catalogocuenta> mapCuentas(List<Catalogocuenta> createQuery) {
        Map<String, Catalogocuenta> map = new HashMap<String, Catalogocuenta>();
        for (Catalogocuenta c : createQuery) {
            map.put(c.getIdCatalogoCuenta().toString(), c);
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        //ModelComparator.compareWriteFile(manager.configuration.Configuration.getValue("baseAnalisisComparativo"), createQuery.get(0), createQuery.get(1), 0.0, -1);
    }

}
