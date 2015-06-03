/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Captacion;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author desarrollo
 */
public class SimulacioncaptacionPage extends SimulacionGenerico {

    @Override
    public String[] getColumnMethods() {
        return Captacion.getSimColumns();
    }

    @Override
    public String[] getColumnName() {
        return Captacion.getSimDesColumns();
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<Object>(regCtaSimulada.getCaptacions());
    }

    @Override
    public String getActionLinkIdName() {
        return "getIdCaptacion";
    }

    @Override
    public String getFieldSetName() {
        return "Captación";
    }


    @Override
    public void createPreContent() {
        title="Simulación de Capital - Captación";
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
