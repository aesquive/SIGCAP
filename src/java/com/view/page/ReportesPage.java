package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Regreportes;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.util.List;
import manager.configuration.Configuration;
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

    private Select selectProject;
    private Select selectReport;
    private Form form;
    private List<Regreportes> reportes;
    private List<Regcuenta> regCuentas;
    private static int numPer = 4;

    @Override
    public void init() {
        form = new Form("form");
        
        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer))&& dte.get(numPer)==true) {
            setRedirect(NocontratadoPage.class);
            return;
        }
        selectReport = new Select("selectReport", "Reporte:");
        selectProject = new Select("selectProject", "Ejercicio:");
        reportes = DAO.createQuery(Regreportes.class, null);
        for (Regreportes r : reportes) {
            selectReport.add(new Option(r.getIdRegReportes(), r.getDesReportes()));
        }
        regCuentas = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : regCuentas) {
            selectProject.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(selectProject);
        form.add(selectReport);
        Submit sub = new Submit("sub", "Obtener Reporte", this, "okReport");
        form.add(sub);
        addControl(form);
    }

    public boolean okReport() throws BrowserLaunchingInitializingException, UnsupportedOperatingSystemException {
        if (form.isValid()) {
            BrowserLauncher browser = new BrowserLauncher();
            browser.setNewWindowPolicy(true);
            browser.openURLinBrowser(Configuration.getValue("direccionReportes")+"?typ=0&pro="+selectProject.getValue()+"&rep="+selectReport.getValue());
            return true;
        }
        return false;
    }
}
