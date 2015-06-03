/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Disponibilidad;
import db.pojos.Prestamo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author desarrollo
 */
public class SimulacionprestamoPage extends SimulacionGenerico {

    @Override
    public String[] getColumnMethods() {
        return Prestamo.getSimColumns();
    }

    @Override
    public String[] getColumnName() {
        return Prestamo.getSimDesColumns();
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<Object>(regCtaSimulada.getPrestamos());
    }

    @Override
    public String getActionLinkIdName() {
        return "getIdPrestamo";
    }

    @Override
    public String getFieldSetName() {
        return "Prestamos";
    }


    @Override
    public void createPreContent() {
        title="Simulación de Capital - Prestamos";
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
