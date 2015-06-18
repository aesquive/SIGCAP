/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports.excelmaker;

import db.controller.DAO;
import db.pojos.Catalogocuenta;
import db.pojos.Regcuenta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.awt.X11.XConstants;
import util.ComparatorCapsule;
import util.Util;

/**
 *
 * @author desarrollo
 *
 * Clase que se encarga de generar el reporte de integridad de los datos subidos
 * contra los reportados en catalogo minimo
 */
public class IntegridadReportMaker {

    /**
     * metodo que genera el reporte de integridad dado un ejercicio, regresa el
     * nombre del archivo generado
     *
     * @param fileName
     * @param regCuenta
     * @return
     */
    public static String makeReport(String baseName, Regcuenta regCuenta) {
        try {
            Map<String, Double> data_acumulada = new HashMap<String, Double>();
            acumularDatos(regCuenta.getCaptacions(), data_acumulada);
            acumularDatos(regCuenta.getDisponibilidads(), data_acumulada);
            acumularDatos(regCuenta.getPrestamos(), data_acumulada);
            acumularDatos(regCuenta.getTarjetacreditos(), data_acumulada);
            acumularDatos(regCuenta.getValoreses(), data_acumulada);

            Map<String, Double> mapCatalogoMinimo = new HashMap<String, Double>();
            Map<String, Catalogocuenta> mapDescripcionCatalogoCuenta = createMapCuentas();
            acumularDatos(regCuenta.getCatalogominimos(), mapCatalogoMinimo);
            List<ComparatorCapsule> capsulasCompare = new LinkedList<ComparatorCapsule>();
            for (String key : data_acumulada.keySet()) {
                Double valorAcumulado = data_acumulada.get(key);
                Double valorCatMinimo = mapCatalogoMinimo.get(key) == null ? 0 : mapCatalogoMinimo.get(key);
                Double resta = valorAcumulado - valorCatMinimo;
                //la capsula trae de comparable la resta del acumulado y catminimo
                //la capsula acarrea la descripcion, el valor acumulado, el valor de cat minimo y la resta
                capsulasCompare.add(new ComparatorCapsule(resta, new Object[]{mapDescripcionCatalogoCuenta.get(key), valorAcumulado, valorCatMinimo, resta}));
            }
            Collections.sort(capsulasCompare);
            return writeFile(baseName, capsulasCompare, regCuenta);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * mediante reflexion utiliza el metodo "getCatalogoCuentaEstandar" de cada
     * objeto y saca su valor con "getValorEstandar" si el mapeo ya tiene la
     * cuenta entonces suma, si no la agrega
     *
     * @param datosCuenta
     * @param mapeoAcumulado
     */
    private static void acumularDatos(Set datosCuenta, Map<String, Double> mapeoAcumulado) {
        for (Object target : datosCuenta) {
            String cuenta = (String) Util.reflectionInvoke(target, "getCatalogoCuentaEstandar");
            Double valor = (Double) Util.reflectionInvoke(target, "getValorEstandar");
            Double guardado = mapeoAcumulado.get(cuenta) == null ? 0.0 : mapeoAcumulado.get(cuenta);
            if (cuenta != null) {
                mapeoAcumulado.put(cuenta, guardado + valor);

            }
        }
    }

    /**
     * regresa el numero de catalogo minimo con su descripcion en un mapeo
     *
     * @return
     */
    private static Map<String, Catalogocuenta> createMapCuentas() {
        List<Catalogocuenta> query = DAO.createQuery(Catalogocuenta.class, null);
        Map<String, Catalogocuenta> map = new HashMap<String, Catalogocuenta>();
        for (Catalogocuenta c : query) {
            map.put(c.getIdCatalogoCuenta().toString(), c);
        }
        return map;
    }

    private static String writeFile(String archivoBase, List<ComparatorCapsule> capsulasCompare, Regcuenta regCta) throws IOException {
        File file = new File(archivoBase);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        CellStyle number = wb.createCellStyle();
        number.setDataFormat(wb.createDataFormat().getFormat("###,###,###.##"));

        //ponemos los datos de las fechas de los ejercicios, la columna D tiene datos del primer ejercicio, la F del segundo
        CellReference cr1 = new CellReference("D6");
        Row row1 = sheet.getRow(cr1.getRow());
        Cell cell1 = row1.createCell(cr1.getCol());
        cell1.setCellValue(regCta.getDesRegCuenta());

        CellReference cr = new CellReference("B8");
        int indexRow = 0;

        for (ComparatorCapsule comp : capsulasCompare) {
            Object[] valoresAlmacenados = (Object[]) comp.getValue();
            Catalogocuenta cuenta = (Catalogocuenta) valoresAlmacenados[0];
            if (cuenta != null) {
                Double acumulado = (Double) valoresAlmacenados[1];
                Double catMin = (Double) valoresAlmacenados[2];
                Double diferencia = (Double) valoresAlmacenados[3];

                Row row = sheet.createRow(cr.getRow() + indexRow);
                Cell cell = row.createCell(cr.getCol());
                cell.setCellValue(cuenta.getIdCatalogoCuenta().toString());
                cell = row.createCell(cr.getCol() + 1);
                cell.setCellValue(cuenta.getDesCatalogoCuenta());
                cell = row.createCell(cr.getCol() + 2);
                cell.setCellValue(catMin);
                cell.setCellStyle(number);
                cell = row.createCell(cr.getCol() + 4);
                cell.setCellValue(acumulado);
                cell.setCellStyle(number);
                cell = row.createCell(cr.getCol() + 6);
                cell.setCellValue(diferencia);
                cell.setCellStyle(number);

                indexRow++;
            }

        }
        String originalName = "Integridad-" + regCta.getIdRegCuenta().toString() + ".xlsx";
        File fileOutput = new File(manager.configuration.Configuration.getValue("Ruta Reportes") + originalName);
        FileOutputStream fileOut = new FileOutputStream(fileOutput);

        wb.write(fileOut);

        fileOut.flush();

        fileOut.close();
        return originalName;
    }

    public static void main(String[] args) {
        IntegridadReportMaker.makeReport(Configuration.getValue("baseAnalisisIntegridad"), DAO.getEjerciciosCalculados().get(1));
    }

}
