package com.view.page;

import db.controller.DAO;
import db.export.DBExporter;
import db.pojos.Consistencia;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import db.pojos.Regreportes;
import db.pojos.User;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import model.utilities.ModelComparator;
import org.apache.click.Page;
import reports.excelmaker.ExcelMaker;
import util.Util;

/**
 *
 * @author Admin
 */
public class VistareportesPage extends Page {

    public VistareportesPage() {
        Integer typeReport = Integer.parseInt(getContext().getRequestParameterValues("typ")[0]);
        System.out.println("el tipo de reporte es " + typeReport);
        switch (typeReport) {
            //reportes de rc
            case 0:
                String project = (String) getContext().getRequestParameterValues("pro")[0];
                String report = (String) getContext().getRequestParameterValues("rep")[0];
                processReport(Integer.parseInt(project), report);
                break;
            //reportes de tracking log
            case 1:
                String ini = (String) getContext().getRequestParameterValues("ini")[0];
                String end = (String) getContext().getRequestParameterValues("end")[0];
                processTracking(ini, end);
                break;
//reportes de analisis comparativo
            case 2:
                String pr1 = (String) getContext().getRequestParameterValues("pra")[0];
                String pr2 = (String) getContext().getRequestParameterValues("prb")[0];
                String var = (String) getContext().getRequestParameterValues("var")[0];
                String numF = (String) getContext().getRequestParameterValues("num")[0];
                processComparator(Integer.parseInt(pr1), Integer.parseInt(pr2), Double.parseDouble(var), Integer.parseInt(numF));
                break;
            //reporte congruencia  
            case 3:
                String pr13 = (String) getContext().getRequestParameterValues("pra")[0];
                String pr23 = (String) getContext().getRequestParameterValues("prb")[0];
                String var3 = (String) getContext().getRequestParameterValues("var")[0];
                String numF3 = (String) getContext().getRequestParameterValues("num")[0];
                processCongruencia(Integer.parseInt(pr13), Integer.parseInt(pr23), Double.parseDouble(var3), Integer.parseInt(numF3));
                break;
            //reporte consistencia
            case 4:
                String pra14 = (String) getContext().getRequestParameterValues("pra")[0];
                initConsistencia(Integer.parseInt(pra14));
                break;
            //reporte tenencia
            case 5:
                String pro = (String) getContext().getRequestParameterValues("pro")[0];
                tenenciaReport(Integer.parseInt(pro));
                break;
            //reporte de la base de datos
            case 6:
                reportDataBase();
                break;
            //reporte de integridad
            case 7:
                String pra = (String) getContext().getRequestParameterValues("pra")[0];
                initIntegridad(Integer.parseInt(pra));
                break;
        }

    }

    public boolean processReport(int numberProject, String numberReport) {
        Set<Integer> reportsNumbers = new HashSet<Integer>();
        String[] split = numberReport.split(",");
        for (String s : split) {
            if (s != null && !s.equals("")) {
                reportsNumbers.add(Integer.parseInt(s));
            }
        }
        List<Regcuenta> regCuentas = DAO.createQuery(Regcuenta.class, null);
        List<Regreportes> reportes = DAO.createQuery(Regreportes.class, null);
        List<String> files = new LinkedList<String>();
        Regcuenta selected = null;
        for (Regcuenta r : regCuentas) {
            if (r.getIdRegCuenta() == numberProject) {
                selected = r;
            }
        }
        Set<Cuenta> cuentas = selected.getCuentas();
        Map<String, Cuenta> map = new HashMap<String, Cuenta>();
        for (Cuenta c : cuentas) {
            map.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(), c);
        }

        List<ExcelMaker> runnables=new LinkedList<ExcelMaker>();
        
        for (Regreportes r : reportes) {
            if (reportsNumbers.contains(r.getIdRegReportes())) {
                String fileName = manager.configuration.Configuration.getValue("Ruta Reportes") + selected.getIdRegCuenta().toString() + "-" + r.getNombreCorto() + ".xlsx";
                ExcelMaker excelmaker = new ExcelMaker("thread "+r.getIdRegReportes().toString(),fileName, r.getRuta(), selected, map);
                runnables.add(excelmaker);
            }
        }
        
        for(int t=0;t<runnables.size();t++){
            System.out.println("antes de empezar el thread "+runnables.get(t).getThreadName());
            runnables.get(t).start();
            System.out.println("inicio el thread de "+runnables.get(t).getThreadName());
        }
        for(Thread t:runnables){
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(VistareportesPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(ExcelMaker t:runnables){
            files.add(t.getFileName());
        }
        
        System.out.println("esta linea no se imprime");

        String fileName;
        try {
            fileName = createZip(Configuration.getValue("baseReportesZip"), selected, files);
            String urlBase = Configuration.getValue("baseApacheReportesZip");
            setRedirect("downloadreport.html?url=" + urlBase + fileName);
            return true;

        } catch (IOException ex) {
            Logger.getLogger(VistareportesPage.class.getName()).log(Level.INFO, null, ex);
        }
        return false;
    }


    private void processComparator(int project1, int project2, double variance, int numRegs) {
        try {
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            Regcuenta reg1 = null;
            Regcuenta reg2 = null;
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == project1) {
                    reg1 = r;
                }
                if (r.getIdRegCuenta() == project2) {
                    reg2 = r;
                }
            }
            ModelComparator comp = new ModelComparator();
            String compareWriteFile = comp.compareWriteFile(manager.configuration.Configuration.getValue("baseAnalisisComparativo"), "comparativo", reg1, reg1.getCuentas(), reg2, reg2.getCuentas(), variance / 100, numRegs);
            String urlBase = Configuration.getValue("baseApacheReportes");
            User user = (User) getContext().getSessionAttribute("user");
            DAO.saveRecordt(user, user.getUser() + " generó reporte comparativo de " + reg1.getDesRegCuenta() + " y " + reg2.getDesRegCuenta());
            setRedirect("downloadreport.html?url=" + urlBase + compareWriteFile);
        } catch (IOException ex) {
            Logger.getLogger(VistareportesPage.class.getName()).log(Level.INFO, null, ex);
        }

    }

    private void processCongruencia(int project1, int project2, double variance, int numRegs) {
        try {
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            Regcuenta reg1 = null;
            Regcuenta reg2 = null;
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == project1) {
                    reg1 = r;
                }
                if (r.getIdRegCuenta() == project2) {
                    reg2 = r;
                }
            }
            List<Cuenta> cuentasInicialesUno = obtenerCuentasIniciales(reg1);
            List<Cuenta> cuentasInicialesDos = obtenerCuentasIniciales(reg2);
            ModelComparator comp = new ModelComparator();
            String compareWriteFile = comp.compareWriteFile(manager.configuration.Configuration.getValue("baseAnalisisCongruencia"), "congruencia", reg1, cuentasInicialesUno, reg2, cuentasInicialesDos, variance / 100, numRegs);
            User user = (User) getContext().getSessionAttribute("user");
            String reg1name = reg1 == null ? "" : reg1.getDesRegCuenta();
            String reg2name = reg2 == null ? "" : reg2.getDesRegCuenta();
            DAO.saveRecordt(user, user.getUser() + " generó reporte comparativo de " + reg1name + " y " + reg2name);
            String urlBase = Configuration.getValue("baseApacheReportes");
            setRedirect("downloadreport.html?url=" + urlBase + compareWriteFile);

        } catch (IOException ex) {
            Logger.getLogger(VistareportesPage.class.getName()).log(Level.INFO, null, ex);
        }

    }

    private void processTracking(String ini, String end) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dateIn = format.parse(ini);
            Date dateEn = format.parse(end);
            Calendar cin = Calendar.getInstance();
            Calendar cen = Calendar.getInstance();
            cin.setTime(dateIn);
            cen.setTime(dateEn);
            cin.set(Calendar.HOUR, 0);
            cen.set(Calendar.HOUR, 0);
            User user = (User) getContext().getSessionAttribute("user");
            DAO.saveRecordt(user, user.getUser() + " generó reporte Tracking Log");
            String generateReport = reports.excelmaker.TrackingLogReporter.generateReport("tracking-" + user.getIduser() + ".xlsx", cin.getTime(), cen.getTime());
            String urlBase = Configuration.getValue("baseApacheReportes");
            setRedirect("downloadreport.html?url=" + urlBase + generateReport);
        } catch (Exception ex) {
            Logger.getLogger(VistareportesPage.class.getName()).log(Level.INFO, null, ex);
        }
    }

    private List<Cuenta> obtenerCuentasIniciales(Regcuenta reg1) {
        List<Cuenta> list = new LinkedList<Cuenta>();
        Set<Cuenta> cuentas = reg1.getCuentas();
        Iterator<Cuenta> iterator = cuentas.iterator();
        while (iterator.hasNext()) {
            Cuenta next = iterator.next();
            if (next.getCatalogocuenta().getIdCatalogoCuenta().toString().equals("790000000000")) {
                System.out.println("Valor de la cta movida" + next.getValor() + " ejercicio " + reg1.getDesRegCuenta());
            }
            if (next.getMoneda() != null) {
                list.add(next);
            }
        }
        return list;
    }

    private void initConsistencia(int parseInt) {
        try {

            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            Regcuenta reg1 = null;
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == parseInt) {
                    reg1 = r;
                }
            }
            Set<Consistencia> consistencias = reg1.getConsistencias();
            Consistencia next = consistencias.iterator().next();
            String nameFile = reports.excelmaker.ConsistenciaReportMaker.makeReport(Configuration.getValue("baseAnalisisConsistencia"), next, reg1);
            String urlBase = Configuration.getValue("baseApacheReportes");
            setRedirect("downloadreport.html?url=" + urlBase + nameFile);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private void tenenciaReport(int parseInt) {
        System.out.println("llega aqui del reporte de tenencia " + parseInt);
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        Regcuenta reg1 = null;
        for (Regcuenta r : createQuery) {
            if (r.getIdRegCuenta() == parseInt) {
                reg1 = r;
            }
        }
        System.out.println("el reg1 " + reg1);
        String nameFile = reports.excelmaker.TenenciaReportMaker.makeReport(Configuration.getValue("baseAnalisisTenencia"), reg1);
        String urlBase = Configuration.getValue("baseApacheReportes");
        setRedirect("downloadreport.html?url=" + urlBase + nameFile);
    }

    private void reportDataBase() {
        Calendar instance = Calendar.getInstance();
        String nombre = "sigcap" + instance.get(Calendar.DAY_OF_MONTH) + instance.get(Calendar.MONTH) + instance.get(Calendar.YEAR) + "-" + instance.get(Calendar.HOUR_OF_DAY) + instance.get(Calendar.MINUTE) + ".sql";
        User user = (User) getContext().getSessionAttribute("user");
        DAO.saveRecordt(user, "Genero la exportación de la base de datos llamada " + nombre);
        DBExporter.export(Configuration.getValue("RutaRespaldos") + nombre);
        setRedirect("downloadreport.html?url=" + Configuration.getValue("basePostgresReportes") + nombre);

    }

    private void initIntegridad(int parseInt) {
        try {

            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            Regcuenta reg1 = null;
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == parseInt) {
                    reg1 = r;
                }
            }
            String nameFile = reports.excelmaker.IntegridadReportMaker.makeReport(Configuration.getValue("baseAnalisisIntegridad"), reg1);
            String urlBase = Configuration.getValue("baseApacheReportes");
            setRedirect("downloadreport.html?url=" + urlBase + nameFile);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private String createZip(String directorio, Regcuenta reg, List<String> files) throws IOException {
        Calendar instance = Calendar.getInstance();
        String name = reg.getDesRegCuenta() + "RC" + instance.get(Calendar.DATE) + instance.get(Calendar.MONTH) + instance.get(Calendar.YEAR) + "-" + instance.get(Calendar.HOUR_OF_DAY) + instance.get(Calendar.MINUTE)+".zip";
        Util.zipFiles(directorio + name, files);
        return name;
    }

}
