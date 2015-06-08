package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import java.util.List;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.DoubleField;

/**
 *
 * @author Admin
 */
public class ReporteconsPage extends BorderPage {

    private Select selectA;
   
    private Form form;

    @Override
    public void init() {
        title="Reporte de consistencia";
        form = new Form("form");
        selectA = new Select("pra", "Seleccionar Ejercicio", true);
        selectA.setDefaultOption(new Option("-1", "Seleccione"));
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : createQuery) {
            selectA.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(selectA);
        Submit sub = new Submit("submit", "Generar Reporte", this, "getReport");
        sub.setAttribute("onclick", "comparativoCons();");

        form.add(sub);
        addControl(form);
    }

    public boolean getReport() {
        if (form.isValid()) {
            Regcuenta regCuentaUno = null;
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta().toString().equals(selectA.getValue())) {
                    regCuentaUno = r;
                }
            }
            if (regCuentaUno == null ) {
                message = "Favor de seleccionar un ejercicio válido";
                return false;
            }
            return true;

        }
        message = "Favor de llenar todos los campos";
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 5;
    }

}
