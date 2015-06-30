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
public class ReportecongruenciaPage extends BorderPage {

    private Select selectA;
    private Select selectB;
    private DoubleField tolerancia;
    private Form form;

    @Override
    public void init() {
        title="Reporte de congruencia";
        form = new Form("form");
        selectA = new Select("pra", "Seleccionar Ejercicio", true);
        selectB = new Select("prb", "Seleccionar Ejercicio", true);
        selectA.setDefaultOption(new Option("-1", "Seleccione"));
        selectB.setDefaultOption(new Option("-1", "Seleccione"));
        tolerancia = new DoubleField("tolerancia", "% de Tolerancia", 3, true);
        List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
        for (Regcuenta r : createQuery) {
            selectA.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
            selectB.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(selectA);
        form.add(selectB);
        form.add(tolerancia);
        Submit sub = new Submit("submit", "Generar Reporte", this, "getReport");
        sub.setAttribute("onclick", "comparativoCongruencia();");

        form.add(sub);
        addControl(form);
    }

    public boolean getReport() {
        if (form.isValid()) {
            Regcuenta regCuentaUno = null;
            Regcuenta regCuentaDos = null;
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta().toString().equals(selectA.getValue())) {
                    regCuentaUno = r;
                }
                if (r.getIdRegCuenta().toString().equals(selectB.getValue())) {
                    regCuentaDos = r;
                }
            }
            if (regCuentaUno == null || regCuentaDos == null) {
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
        return 7;
    }

}
