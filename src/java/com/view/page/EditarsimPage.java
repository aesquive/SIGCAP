/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
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
    Cuenta cuenta;
    
    @Override
    public void init() {
        form = new Form("form");
        cuenta = (Cuenta) getSessionVar("variableEditSim");
        numCuenta=new TextField("num","Cuenta");
        desCuenta=new TextField("des", "Descripción");
        valCuenta=new TextField("val", "Valor");
        numCuenta.setValue(cuenta.getNumCuenta());
        desCuenta.setValue(cuenta.getDetalle());
        valCuenta.setValue(cuenta.getResultado());
        form.add(numCuenta);
        form.add(desCuenta);
        form.add(valCuenta);
        form.add(new Submit("subEdit","Guardar", this,"guardar"));
        addControl(form);
    }
    
    public boolean guardar(){
        cuenta.setValor(Double.parseDouble(valCuenta.getValue().replace(",", "")));
        DAO.update(cuenta);
        setRedirect(SimulacioncuentasPage.class);
        return true;
    }

}
