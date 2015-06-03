/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.FormTable;

/**
 *
 * @author desarrollo
 */
public class SimulacioncuentasPage extends SimulacionPage {

    Form form;
    Regcuenta regCta;
    List<Cuenta> cuentas;
    TextField fieldSearch;

    @Override
    public void initSimulacionComponents() {
        form = new Form("formSim");
//creamos el panel de edicion de cuentas
        createFSEditVar();

        addControl(form);
    }

    /**
     * crea el formulario correspondiente a la edicion de variables de cuentas
     *
     * @return
     */
    private FieldSet createFSEditVar() {
        regCta = (Regcuenta) getSessionVar("prySim");
        //fieldset de busqueda de variable
        FieldSet fset = new FieldSet("Simulador de Cuentas", "Editar Variable de " + regCta.getDesRegCuenta());
        fieldSearch = new TextField("search", "Nombre variable:");
        fieldSearch.setValue(getSessionVar("busquedaSim") != null ? (String) getSessionVar("busquedaSim") : "");
        fset.add(fieldSearch);
        Submit buscar = new Submit("sub", "Buscar", this, "editVar_buscarVariable");
        buscar.setAttribute("onclick", "waitPage();");
        fset.add(buscar);
        form.add(fset);
        FormTable editVar_filtrarCuentas = editVar_filtrarCuentas(fieldSearch.getValue(), fset);
        form.add(editVar_filtrarCuentas);
        return fset;
    }

    /**
     * filtra las cuentas que contengan el valor value dentro de su nombre o el
     * numero de cuenta
     *
     * @param value
     * @param form
     * @return
     */
    public FormTable editVar_filtrarCuentas(String value, FieldSet form) {
        if (!value.equals("")) {
            cuentas = new LinkedList<Cuenta>();
            cuentas = editVar_buscarCuentas(value);
            if (cuentas != null && !cuentas.isEmpty()) {
                FieldSet fs = new FieldSet("fsCtas", "Cuentas");
                FormTable generarCuadroCuentas = editVar_generarCuadroCuentas(cuentas);
                fs.add(generarCuadroCuentas);
                return generarCuadroCuentas;
            }
        }
        return new FormTable("Cuentas");
    }

    /**
     * agrega el valor de la variable a la sesion para buscarlo en la siguiente
     * carga
     *
     * @param value
     * @return
     */
    public boolean editVar_buscarVariable() {
        addSessionVar("busquedaSim", fieldSearch.getValue());
        setRedirect(SimulacionPage.class);
        return true;
    }

    /**
     * busca las cuentas que contengan el valor del parametro dentro del nombre
     * o el numero de cuenta
     *
     * @param value
     * @return
     */
    private List<Cuenta> editVar_buscarCuentas(String value) {
        Set<Cuenta> cuentasSet = regCta.getCuentas();
        List<Cuenta> resultado = new LinkedList<Cuenta>();
        for (Cuenta cuenta : cuentasSet) {
            if (cuenta.getRef() == null || cuenta.getRef().equals("")) {
                String datosCuenta = cuenta.getCatalogocuenta().getDesCatalogoCuenta() + cuenta.getCatalogocuenta().getIdCatalogoCuenta().toString();
                String datosCuentaMayus = datosCuenta.trim().toUpperCase();
                String[] argumentos = value.split(" ");
                boolean paso = true;
                for (String argumento : argumentos) {
                    if (paso && !datosCuentaMayus.contains(argumento.toUpperCase())) {
                        paso = false;
                    }
                }
                if (paso) {
                    resultado.add(cuenta);
                }
            }
        }
        return resultado;
    }

    /**
     * cuadro de cuentas para cuando se editan variables
     *
     * @param cuentas
     * @param form
     * @return
     */
    private FormTable editVar_generarCuadroCuentas(List<Cuenta> cuentas) {
        FormTable table = new FormTable("table");
        table.setName("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        for (int t = 0; t < cuentas.size(); t++) {
            ActionLink actionLink = new ActionLink("link" + cuentas.get(t).getIdCuenta(), "Editar", this, "editVar_onVarEdit");
            actionLink.setValue(cuentas.get(t).getIdCuenta().toString());
            cuentas.get(t).setActionLink(actionLink);
            addControl(actionLink);
        }
        String[] columns = new String[]{"NumCuenta", "Detalle", "Resultado", "Accion?"};
        for (String c : columns) {
            Column col = new Column(c);
            col.setWidth("900 px");
            if (c.substring(c.length() - 1, c.length()).equals("?")) {
                col.setName(c.substring(0, c.length() - 1));
                col.setDecorator(new Decorator() {
                    @Override
                    public String render(Object object, Context context) {
                        Cuenta c = (Cuenta) object;
                        return c.getActionLink().toString();
                    }
                });
            }

            table.addColumn(col);
        }
        table.setRowList(cuentas);
        return table;
    }

    /**
     * evento que nos ayuda a checar si se da click en alguno de los valores
     *
     * @return
     */
    public boolean editVar_onVarEdit() {

        for (int t = 0; t < cuentas.size(); t++) {
            if (cuentas.get(t).getActionLink().isClicked()) {
                Cuenta ref = cuentas.get(t);
                addSessionVar("variableEditSim", ref);
                setRedirect(EditarsimPage.class);
            }
        }
        return true;
    }

}
