/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Valores;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author desarrollo
 */
public class SimulaciontenenciaPage extends SimulacionGenerico {

    @Override
    public String[] getColumnMethods() {
        return Valores.getTenenciaColumns();
    }

    @Override
    public String[] getColumnName() {
        return Valores.getTenenciaDesColumns();
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<Object>(regCtaSimulada.getValoreses());
    }

    @Override
    public String getActionLinkIdName() {
        return "getIdTenencia";
    }

    @Override
    public String getFieldSetName() {
        return "Tenencia";
    }


    @Override
    public void createPreContent() {
        title="Simulación de Capital - Tenencia";
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
