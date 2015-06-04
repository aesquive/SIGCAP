/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.Util;

/**
 *
 * @author desarrollo
 */
public class SimulaciondataPage extends SimulacionPage {

    public Form form;
    public Regcuenta regCtaSimulada;
    public FormTable table;
    public Map<String, ActionLink> actionLinks;
    private String idMethodName;
    private List<Object> values;
    private Map<String, Object> mapActionLinkValues;

    @Override
    public void initSimulacionComponents() {
        form = new Form("formSim");
        createFSetPrincipal();
        addControl(form);
    }

    /**
     * crea el formulario correspondiente a la edicion de variables de cuentas
     *
     * @return
     */
    private void createFSetPrincipal() {
        Regcuenta tmp = (Regcuenta) getSessionVar("prySim");
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : createQuery) {
            if (r.getIdRegCuenta() == tmp.getIdRegCuenta()) {
                regCtaSimulada = r;
            }
        }

        //tabla con contenido
        table = new FormTable("dataTableSmall");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);

        //saca los valores de acuerdo al boton que se apreto en el simulation page
        //valores a mostrar en la tabla
        values = new LinkedList<Object>((Set<Object>) Util.reflectionInvoke(regCtaSimulada, (String) getSessionVar("simValues")));
        //metodos que ejecutara sobre los objetos para llenar las tablas
        String[] columnsMethods = (String[]) getSessionVar("simColumns");
        //nombres que se mostraran como cabeceras en las tablas
        String[] columnsNames = (String[]) getSessionVar("simDesColumns");
        //metodo con el cual podemos sacar el identificador de cada uno de los objetos
        idMethodName = (String) getSessionVar("simMethodName");
        //nombre del fieldset
        String fsName = (String) getSessionVar("simFSName");

        //creamos las columnas
        for (int t = 0; t < columnsMethods.length; t++) {
            Column c = new Column(columnsMethods[t], columnsNames[t]);
            table.addColumn(c);
        }

        //generamos los links de edicion para cada uno de los valores en las tablas
        actionLinks = new HashMap<String, ActionLink>();
        mapActionLinkValues = new HashMap<String, Object>();
        for (int t = 0; t < values.size(); t++) {
            String valId = Util.reflectionString(values.get(t), idMethodName);
            ActionLink actionLink = new ActionLink("link" + valId, "Editar", this, "afterClickEdit");
            actionLink.setValue(valId);
            actionLinks.put(valId, actionLink);
            mapActionLinkValues.put(valId, values.get(t));
            addControl(actionLink);
        }

        //Columna para la edición
        Column colEdit = new Column("accion", "Acción");
        colEdit.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                String valId = Util.reflectionString(object, idMethodName);
                return actionLinks.get(valId).toString();
            }
        });

        table.addColumn(colEdit);

        FieldSet fs = new FieldSet(".", fsName);
        fs.add(table);
        table.setRowList(values);
        form.add(fs);
    }

    //metodo que se ejecutara cuando se de click a algun action link
    public boolean afterClickEdit() {
        //sacamos el objeto en el que se realizo el click
        ActionLink actionClicked = null;
        Object target=null;
        Set<String> keySet = actionLinks.keySet();
        for (String key : keySet) {
            if (actionLinks.get(key).isClicked()) {
                actionClicked = actionLinks.get(key);
                target=mapActionLinkValues.get(key);
            }
        }
        System.out.println("el objeto clickeado es "+target);
        //Sacamos las columnas que no se pueden editar pero si ver
        addSessionVar("simNoEditColumns",Util.reflectionInvoke(target, "getSimNoEditColumns"));
        addSessionVar("simNoEditColumnsDes",Util.reflectionInvoke(target, "getSimNoEditColumnsDes"));
        
        //Sacamos las columnas que se pueden editar 
        addSessionVar("simEditColumns",Util.reflectionInvoke(target, "getSimEditColumns"));
        addSessionVar("simEditColumnsDes",Util.reflectionInvoke(target, "getSimEditColumnsDes"));
        addSessionVar("simEditScreenName", Util.reflectionInvoke(target, "getSimEditScreenName"));
        //agregamos el target sobre el que se ejecutara la edicion
        addSessionVar("simEditTarget", target);
        //redireccionamos al editor de variables
        setRedirect(EditarvariablesimulacionPage.class);
        return true;
    }

}
