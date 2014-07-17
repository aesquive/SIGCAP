package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import db.pojos.Regreportes;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import model.executor.ModelExecutor;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import util.Util;

/**
 *
 * @author Admin
 */
public class ReportesPage extends BorderPage {

    private Select projectCalc;
    private Select selectProject;
    private Select selectReport;
    private Form form;
    private List<Regreportes> reportes;
    private List<Regcuenta> regCuentas;
    private static int numPer = 1;

    @Override
    public void init() {
        form = new Form("form");
        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) && dte.get(numPer) == true) {
            setRedirect(NocontratadoPage.class);
            return;
        }

        FieldSet fsReporte = new FieldSet("fsr", "Reportes");
        FieldSet fsCalc = new FieldSet("fsc", "Cálculo");
        selectReport = new Select("selectReport", "Reporte:");
        selectProject = new Select("selectProject", "Ejercicio:");
        projectCalc = new Select("selectCalc", "Ejercicio a calcular:");
        reportes = DAO.createQuery(Regreportes.class, null);
        for (Regreportes r : reportes) {
            selectReport.add(new Option(r.getIdRegReportes(), r.getDesReportes()));
        }
        selectReport.setDefaultOption(new Option("-1", "Seleccionar"));
        regCuentas = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : regCuentas) {
            selectProject.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
            projectCalc.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        selectProject.setDefaultOption(new Option("-1", "Seleccionar"));
        projectCalc.setDefaultOption(new Option("-1", "Seleccionar"));
        fsReporte.add(selectProject);
        fsReporte.add(selectReport);
        Submit sub = new Submit("sub", "Obtener Reporte", this, "okReport");
        fsReporte.add(sub);
        fsCalc.add(projectCalc);
        fsCalc.add(new Submit("calc", "Calcular Ejercicio", this, "calc"));
        form.add(fsCalc);
        form.add(fsReporte);
        addControl(form);
    }

    public boolean calc() {
        if (form.isValid()) {
            try {
                Regcuenta r = null;
                List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
                for (Regcuenta reg : createQuery) {
                    if (reg.getIdRegCuenta().toString().equals(selectProject.getValue())) {
                        r = reg;
                    }
                }
                ModelExecutor mex = new ModelExecutor(r, false);
                mex.start();
                return true;
            } catch (Exception ex) {
                message="Error al calcular";
                Logger.getLogger(ReportesPage.class.getName()).log(Level.INFO, null, ex);
            }
        }
        return false;
    }

    public boolean okReport() throws BrowserLaunchingInitializingException, UnsupportedOperatingSystemException {
        if (form.isValid()) {
            Regcuenta r = null;
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta reg : createQuery) {
                if (reg.getIdRegCuenta().toString().equals(selectProject.getValue())) {
                    r = reg;
                }
            }
            Set<Cuenta> ctas = r.getCuentas();
            boolean complete = false;
            for (Cuenta c : ctas) {
                if (c.getCatalogocuenta().getIdCatalogoCuenta().toString().equals("1")) {
                    complete = true;
                }
            }
            if (!complete) {
                message = "Favor de calcular el modelo antes de obtener algun reporte";
                return false;
            }
            BrowserLauncher browser = new BrowserLauncher();
            browser.setNewWindowPolicy(true);
            browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=0&pro=" + selectProject.getValue() + "&rep=" + selectReport.getValue());
            return true;
        }
        return false;
    }
}
