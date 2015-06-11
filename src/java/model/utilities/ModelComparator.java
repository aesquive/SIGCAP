package model.utilities;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import manager.configuration.Configuration;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class ModelComparator {

    public Map<Long, Cuenta> mapFirst;
    public Map<Long, Cuenta> mapSecond;
    public Set<Long> mapCuentas;
    public List<SorteableComparator> mapCambios;

    public String compareWriteFile(String archivoBase, String fileName, Regcuenta first, Collection<Cuenta> cuentasFirst, Regcuenta second, Collection<Cuenta> cuentasSecond, Double minVariance, int numberRegisters) throws IOException {
        if (first != null && second != null) {
            mapAllCuentas(cuentasFirst, cuentasSecond, minVariance);
        }
        checkCambios();
        Collections.sort(mapCambios);
       
        return writeFile(archivoBase, fileName, first, second, minVariance);
    }

    public static
            void main(String[] args) throws IOException {
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        ModelComparator comp = new ModelComparator();
        Regcuenta first = createQuery.get(1);
        Regcuenta sec = createQuery.get(0);
        System.out.println(first.getDesRegCuenta());
        System.out.println(sec.getDesRegCuenta());
        comp.compareWriteFile(Configuration.getValue("baseAnalisisComparativo"), "comparativoEjemplo", first, first.getCuentas(), sec, sec.getCuentas(), 0.0, -1);
    }

    private void checkCambios() {
        mapCambios = new LinkedList<SorteableComparator>();
        for (Long key : mapCuentas) {
            Cuenta firstValue = mapFirst.get(key);
            Cuenta secondValue = mapSecond.get(key);
            Double firstDouble = firstValue == null ? Double.NaN : firstValue.getValor() == null ? Double.NaN : firstValue.getValor();
            Double secondDouble = secondValue == null ? Double.NaN : secondValue.getValor() == null ? Double.NaN : secondValue.getValor();
            SorteableComparator sortcomp = null;
//            if (firstDouble == 0 || secondDouble == 0 || firstDouble==Double.NaN || secondDouble==Double.NaN) {
//                    
//            } else{
            sortcomp = new SorteableComparator(secondDouble / firstDouble - 1, firstValue, secondValue, firstDouble, secondDouble);

//            }
            if (sortcomp != null) {
                mapCambios.add(sortcomp);

            }
        }
    }

    private String writeFile(String baseName, String newFileName, Regcuenta first, Regcuenta second, Double minVariance) throws IOException {
        File file = new File(baseName);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);
        
         CellStyle percent = wb.createCellStyle();
        percent.setDataFormat(wb.createDataFormat().getFormat("0.000%"));
        
        CellStyle number = wb.createCellStyle();
        number.setDataFormat(wb.createDataFormat().getFormat("###,###,###.##"));
        
        
        CellStyle date = wb.createCellStyle();
        date.setDataFormat(wb.createDataFormat().getFormat("MM/yyyy"));
        
        //ponemos los datos de las fechas de los ejercicios, la columna D tiene datos del primer ejercicio, la F del segundo
        CellReference cr1 = new CellReference("E7");
        Row row1 = sheet.getRow(cr1.getRow());
        Cell cell1 = row1.createCell(cr1.getCol());
        cell1.setCellValue(first.getFecha());
        cell1.setCellStyle(date);
        CellReference cr2 = new CellReference("G7");
        Row row2 = sheet.getRow(cr2.getRow());
        Cell cell2 = row2.createCell(cr2.getCol());
        cell2.setCellValue(second.getFecha());
        cell2.setCellStyle(date);
        CellReference cr3 = new CellReference("G6");
        Row row3 = sheet.getRow(cr3.getRow());
        Cell cell3 = row3.createCell(cr3.getCol());
        cell3.setCellValue(second.getDesRegCuenta());
        CellReference cr4 = new CellReference("E6");
        Row row4 = sheet.getRow(cr4.getRow());
        Cell cell4 = row4.createCell(cr4.getCol());
        cell4.setCellValue(first.getDesRegCuenta());

        CellReference cr = new CellReference("A8");
        int indexRow = 0;
        int saltoColumnas = 2;

       
        
        for (SorteableComparator comp : mapCambios) {
            if (comp.getCompareValue() != Double.NaN && comp.getCompareValue() > Math.abs(minVariance) && comp.getCompareValue() != 1) {
                Row row = sheet.createRow(cr.getRow() + indexRow);
                Cell cell = row.createCell(cr.getCol());
                cell.setCellValue(comp.getFirstCuenta().getCatalogocuenta().getIdCatalogoCuenta().toString());
                cell = row.createCell(cr.getCol() + saltoColumnas * 1);
                cell.setCellValue(comp.getFirstCuenta().getCatalogocuenta().getDesCatalogoCuenta());
                cell = row.createCell(cr.getCol() + saltoColumnas * 2);
                if (comp.getFirstValue() != Double.NaN) {
                    cell.setCellValue(comp.getFirstValue());
                    cell.setCellStyle(number);
                }
                cell = row.createCell(cr.getCol() + saltoColumnas * 3);
                if (comp.getSecondValue()!= Double.NaN) {
                    cell.setCellValue(comp.getSecondValue());
                    cell.setCellStyle(number);
                }

                cell = row.createCell(cr.getCol() + saltoColumnas * 4);
                if (comp.getCompareValue().isInfinite()) {
                    cell.setCellValue("Infinito");
                } else if (comp.getCompareValue() == 0) {
                    cell.setCellValue("No definido");
                } else {
                    cell.setCellValue(comp.getCompareValue());
                    cell.setCellStyle(percent);
                }
                indexRow++;
            }

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

    /**
     * saca todas las cuentas y las mete en mapeos
     *
     * @param cuentasFirst
     * @param cuentasSecond
     * @param minVariance
     */
    private void mapAllCuentas(Collection<Cuenta> cuentasFirst, Collection<Cuenta> cuentasSecond, Double minVariance) {
        mapFirst = new HashMap<Long, Cuenta>();
        mapSecond = new HashMap<Long, Cuenta>();
        mapCuentas = new HashSet<Long>();
        for (Cuenta cf : cuentasFirst) {
            mapFirst.put(cf.getCatalogocuenta().getIdCatalogoCuenta(), cf);
            mapCuentas.add(cf.getCatalogocuenta().getIdCatalogoCuenta());
        }
        for (Cuenta cs : cuentasSecond) {
            mapSecond.put(cs.getCatalogocuenta().getIdCatalogoCuenta(), cs);
            mapCuentas.add(cs.getCatalogocuenta().getIdCatalogoCuenta());
        }
    }

}
