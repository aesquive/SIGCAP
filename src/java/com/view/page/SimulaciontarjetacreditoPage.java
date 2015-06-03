/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Disponibilidad;
import db.pojos.Prestamo;
import db.pojos.Tarjetacredito;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author desarrollo
 */
public class SimulaciontarjetacreditoPage extends SimulacionGenerico {

    @Override
    public String[] getColumnMethods() {
        return Tarjetacredito.getSimColumns();
    }

    @Override
    public String[] getColumnName() {
        return Tarjetacredito.getSimDesColumns();
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<Object>(regCtaSimulada.getTarjetacreditos());
    }

    @Override
    public String getActionLinkIdName() {
        return "getIdTarjetaCredito";
    }

    @Override
    public String getFieldSetName() {
        return "Tarjeta de Crédito";
    }


    @Override
    public void createPreContent() {
        title="Simulación de Capital - Tarjeta de Crédito";
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
