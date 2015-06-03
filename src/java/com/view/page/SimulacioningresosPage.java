/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Disponibilidad;
import db.pojos.Ingresosnetos;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author desarrollo
 */
public class SimulacioningresosPage extends SimulacionGenerico {

    @Override
    public String[] getColumnMethods() {
        return Ingresosnetos.getSimColumns();
    }

    @Override
    public String[] getColumnName() {
        return Ingresosnetos.getSimDesColumns();
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<Object>(regCtaSimulada.getIngresosnetoses());
    }

    @Override
    public String getActionLinkIdName() {
        return "getIdIngresosNetos";
    }

    @Override
    public String getFieldSetName() {
        return "Ingresos Netos";
    }


    @Override
    public void createPreContent() {
        title="Simulación de Capital - Ingresos Netos";
        return;
    }

    @Override
    public void createPostContent() {
        return;
    }

    @Override
    public boolean afterClickEdit() {
        setRedirect(LoginPage.class);
        return true;
    }

}
