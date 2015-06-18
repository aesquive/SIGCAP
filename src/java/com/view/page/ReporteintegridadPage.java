/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.view.page;

import db.controller.DAO;
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
public class ReporteintegridadPage extends BorderPage{

     private Select selectA;
   
    private Form form;

    @Override
    public void init() {
        title="Reporte de Integridad";
        form = new Form("form");
        selectA = new Select("pra", "Seleccionar Ejercicio", true);
        selectA.setDefaultOption(new Option("-1", "Seleccione"));
        List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
        for (Regcuenta r : createQuery) {
            selectA.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(selectA);
        Submit sub = new Submit("submit", "Generar Reporte", this, "getReport");
        sub.setAttribute("onclick", "reporteIntegridad();");

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
        return 16;
    }
    
}
