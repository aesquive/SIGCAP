/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import model.comparator.ModelComparator;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.extras.control.NumberField;

/**
 *
 * @author Admin
 */
public class ReportecambiosPage extends BorderPage {

    Form form;
    private Select project1;
    private Select project2;
    private DoubleField minVariance;
    private IntegerField numberFields;

    @Override
    public void init() {
        form = new Form("form");
        minVariance = new DoubleField("var", "% de Variación Mínima", 6, true);
        numberFields = new IntegerField("fie", "Número de Registros", 5, true);
        project1 = new Select("project1", "Primer Ejercicio", true);
        project2 = new Select("project2", "Segundo Ejercicio", true);
        List<Regcuentauser> createQuery = DAO.createQuery(Regcuentauser.class, null);
        project1.setDefaultOption(new Option("Seleccione"));
        for (Regcuentauser ru : createQuery) {
            project1.add(new Option(ru.getRegcuenta(), ru.getRegcuenta().getDesRegCuenta()));
            project2.add(new Option(ru.getRegcuenta(), ru.getRegcuenta().getDesRegCuenta()));
        }
        form.add(project1);
        form.add(project2);
        form.add(minVariance);
        form.add(numberFields);
        form.add(new Submit("sub", "Generar reporte", this, "reporteCambios"));
        addControl(form);
    }

    public boolean reporteCambios() throws Exception {
        if (form.isValid()) {
            BrowserLauncher browser = new BrowserLauncher();
            browser.setNewWindowPolicy(true);
            browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=2&pra=" + project1.getValue() + "&prb=" + project2.getValue()+"&var="+minVariance.getDouble()/100+"&num="+numberFields.getInteger());
            return true;
        }
        return false;
    }
}
