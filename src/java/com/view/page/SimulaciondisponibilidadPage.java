/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Disponibilidad;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author desarrollo
 */
public class SimulaciondisponibilidadPage extends SimulacionGenerico {

    @Override
    public String[] getColumnMethods() {
        return Disponibilidad.getDisponibilidadColumns();
    }

    @Override
    public String[] getColumnName() {
        return Disponibilidad.getDisponibilidadColumnsDes();
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<Object>(regCtaSimulada.getDisponibilidads());
    }

    @Override
    public String getActionLinkIdName() {
        return "getIdDisponibilidad";
    }

    @Override
    public String getFieldSetName() {
        return "Disponibilidades";
    }


    @Override
    public void createPreContent() {
        title="Simulación de Capital - Disponibilidades";
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
