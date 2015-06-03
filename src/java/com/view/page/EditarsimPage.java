/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Catalogominimo;
import db.pojos.Regcuenta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.executor.ModelExecutor;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 *
 * @author desarrollo
 */
public class EditarsimPage extends BorderPage {

    Form form;
    TextField numCuenta;
    TextField desCuenta;
    TextField valCuenta;
    Catalogominimo cuenta;
    private Regcuenta regCuenta;
    public String ejercicio;

    @Override
    public void init() {
        title = "Simulación de Capital - Editar Cuenta Catalogo Mínimo";
        form = new Form("form");
        regCuenta = (Regcuenta) getSessionVar("prySim");
        ejercicio = regCuenta.getDesRegCuenta();
        cuenta = (Catalogominimo) getSessionVar("variableEditSim");
        numCuenta = new TextField("num", "Cuenta");
        desCuenta = new TextField("des", "Descripción");
        valCuenta = new TextField("val", "Valor");
        numCuenta.setValue(cuenta.getIdCatalogoMinimo().toString());
        desCuenta.setValue(cuenta.getDetalle());
        valCuenta.setValue(cuenta.getResultado());
        form.add(numCuenta);
        form.add(desCuenta);
        form.add(valCuenta);
        form.add(new Submit("subEdit", "Guardar", this, "guardar"));
        form.add(new Submit("cancelEdit", "Cancelar", this,"cancelar"));
        addControl(form);
    }
    
    public boolean cancelar(){
        setRedirect(SimulacioninicialPage.class);
        return true;
    }

    public boolean guardar() {
        try {
            cuenta.setValor(Double.parseDouble(valCuenta.getValue().replace(",", "")));
            DAO.update(cuenta);
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta() == regCuenta.getIdRegCuenta()) {
                    ModelExecutor exec = new ModelExecutor(r, true);
                    exec.start();
                    setRedirect(SimulacioncuentasPage.class);
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            message = "Error en el calculo";
            Logger.getLogger(EditarsimPage.class.getName()).log(Level.INFO, null, ex);
        }
        return false;
    }

}
