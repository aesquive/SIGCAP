/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class ReporteconsistenciaPage extends BorderPage {

    private Form form;
    private Select projects;

    @Override
    public void init() {
        form = new Form("form");
        projects = new Select("select", "Ejercicio", true);
        projects.setDefaultOption(new Option("-1", "Seleccionar"));
        projects.add(new Submit("sub", "Generar Reporte", this, "generarConsistencia"));
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : createQuery) {
            projects.add(new Option(r.getIdRegCuenta().toString(), r.getDesRegCuenta()));
        }
        form.add(projects);
        addControl(form);
    }

    public boolean generarConsistencia() {
        if (form.isValid()) {
            try {
                Regcuenta regCuenta = null;
                List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
                for (Regcuenta r : createQuery) {
                    if (r.getIdRegCuenta().toString().equals(projects.getValue())) {
                        regCuenta = r;
                    }
                }
                BrowserLauncher browser = new BrowserLauncher();
                browser.setNewWindowPolicy(true);
                browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=4&pra=" + regCuenta.getIdRegCuenta());
                return true;
            } catch (BrowserLaunchingInitializingException ex) {
                Logger.getLogger(ReporteconsistenciaPage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedOperatingSystemException ex) {
                Logger.getLogger(ReporteconsistenciaPage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return false;
    }

}
