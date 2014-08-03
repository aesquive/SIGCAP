package com.view.page;

import db.controller.DAO;
import db.pojos.Consistencia;
import db.pojos.Regcuenta;
import java.util.List;
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
        List<Consistencia> createQuery = DAO.createQuery(Consistencia.class, null);
        for (Consistencia c: createQuery) {
            Regcuenta r=c.getRegcuenta();
            projects.add(new Option(r.getIdRegCuenta().toString(), r.getDesRegCuenta()));
        }
        form.add(projects);
        form.add(new Submit("sub", "Generar Reporte", this, "generarConsistencia"));
        addControl(form);
    }

    public boolean generarConsistencia() {
        if (form.isValid()) {
                Regcuenta regCuenta = null;
                List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
                for (Regcuenta r : createQuery) {
                    if (r.getIdRegCuenta().toString().equals(projects.getValue())) {
                        regCuenta = r;
                    }
                }
//                BrowserLauncher browser = new BrowserLauncher();
//                browser.setNewWindowPolicy(true);
//                browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=4&pra=" + regCuenta.getIdRegCuenta());
                return true;
            

        }
        return false;
    }

}
