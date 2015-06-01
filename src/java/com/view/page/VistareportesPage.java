package com.view.page;

import db.controller.DAO;
import db.pojos.Consistencia;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import db.pojos.Regreportes;
import db.pojos.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import model.comparator.ModelComparator;
import org.apache.click.Page;
import reports.excelmaker.ExcelMaker;

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
                processReport(Integer.parseInt(project), Integer.parseInt(report));
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
            case 3:
                String pr13 = (String) getContext().getRequestParameterValues("pra")[0];
                String pr23 = (String) getContext().getRequestParameterValues("prb")[0];
                String var3 = (String) getContext().getRequestParameterValues("var")[0];
                String numF3 = (String) getContext().getRequestParameterValues("num")[0];
                initComparator(Integer.parseInt(pr13), Integer.parseInt(pr23), Double.parseDouble(var3), Integer.parseInt(numF3));
                break;
            case 4:
                String pra14 = (String) getContext().getRequestParameterValues("pra")[0];
                initConsistencia(Integer.parseInt(pra14));
                break;
            case 5:
                String pro = (String) getContext().getRequestParameterValues("pro")[0];
                tenenciaReport(Integer.parseInt(pro));
                break;

        }

    }

    public boolean processReport(int numberProject, int numberReport) {
        List<Regcuenta> regCuentas = DAO.createQuery(Regcuenta.class, null);
        List<Regreportes> reportes = DAO.createQuery(Regreportes.class, null);
        Regcuenta selected = null;
        for (Regcuenta r : regCuentas) {
            if (r.getIdRegCuenta() == numberProject) {
                selected = r;
            }
        }
        Regreportes selectedReport = null;
        for (Regreportes r : reportes) {
            if (r.getIdRegReportes() == numberReport) {
                selectedReport = r;
            }
        }

        String fileName = manager.configuration.Configuration.getValue("Ruta Reportes") + selected.getIdRegCuenta().toString() + "-" + selectedReport.getNombreCorto()+ ".xlsx";
        Set<Cuenta> cuentas = selected.getCuentas();
        Map<String, Cuenta> map = new HashMap<String, Cuenta>();
        for (Cuenta c : cuentas) {
            map.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(), c);
        }
        ExcelMaker excelmaker = new ExcelMaker(fileName, selectedReport.getRuta(),selected, map);
        File makeFile = null;
        try {
            makeFile = excelmaker.makeFile();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(makeFile));
            reader.read();
            reader.close();
        } catch (Exception e) {
            System.out.println("waisting time");
        }
        User user = (User) getContext().getSessionAttribute("user");
        DAO.saveRecordt(user, user.getUser() + " generó reporte " + selectedReport.getDesReportes());
        setRedirect("/reportes/" + selected.getIdRegCuenta().toString() + "-" + selectedReport.getNombreCorto()+ ".xlsx");
        return true;
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
            String compareWriteFile = ModelComparator.compareWriteFile(manager.configuration.Configuration.getValue("baseAnalisisComparativo"), "comparativo", reg1, reg1.getCuentas(), reg2, reg2.getCuentas(), variance, numRegs);
            setRedirect("/reportes/" + compareWriteFile);
            User user = (User) getContext().getSessionAttribute("user");
            DAO.saveRecordt(user, user.getUser() + " generó reporte comparativo de " + reg1.getDesRegCuenta() + " y " + reg2.getDesRegCuenta());
        } catch (IOException ex) {
            Logger.getLogger(VistareportesPage.class.getName()).log(Level.INFO, null, ex);
        }

    }

    private void initComparator(int project1, int project2, double variance, int numRegs) {
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
            String compareWriteFile = ModelComparator.compareWriteFile(manager.configuration.Configuration.getValue("baseAnalisisCongruencia"), "congruencia", reg1, cuentasInicialesUno, reg2, cuentasInicialesDos, variance, numRegs);
            setRedirect("/reportes/" + compareWriteFile);
            User user = (User) getContext().getSessionAttribute("user");
            String reg1name=reg1==null? "":reg1.getDesRegCuenta();
            String reg2name=reg2==null?"":reg2.getDesRegCuenta();
            DAO.saveRecordt(user, user.getUser() + " generó reporte comparativo de " + reg1name + " y " + reg2name);
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
            setRedirect("/reportes/" + generateReport);
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
            setRedirect("/reportes/" + nameFile);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private void tenenciaReport(int parseInt) {
        System.out.println("llega aqui del reporte de tenencia "+parseInt);
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            Regcuenta reg1 = null;
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == parseInt) {
                    reg1 = r;
                }
            }
            System.out.println("el reg1 "+reg1);
            String nameFile=reports.excelmaker.TenenciaReportMaker.makeReport(Configuration.getValue("baseAnalisisTenencia"),reg1);
            setRedirect("/reportes/"+nameFile);
    }

}
