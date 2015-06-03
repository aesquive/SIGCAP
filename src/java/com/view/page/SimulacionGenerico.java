/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.HashMap;
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
public abstract class SimulacionGenerico extends SimulacionPage {

    public Form form;
    public Regcuenta regCtaSimulada;
    public FormTable table;
    public Map<String, ActionLink> actionLinks;
    private String idMethodName;
    
    @Override
    public void initSimulacionComponents() {
        form = new Form("formSim");
        createPreContent();
        createFSetPrincipal();
        createPostContent();
        addControl(form);
    }

    /**
     * crea el formulario correspondiente a la edicion de variables de cuentas
     *
     * @return
     */
    private void createFSetPrincipal() {
        regCtaSimulada = (Regcuenta) getSessionVar("prySim");

        
        //tabla con contenido
        table = new FormTable("dataTableSmall");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        
        String[] columnsMethods=getColumnMethods();
        String[] columnsNames=getColumnName();
        List<Object> values=getValues();
        idMethodName=getActionLinkIdName();
        String fsName=getFieldSetName();
        
        for (int t = 0; t < columnsMethods.length; t++) {
            Column c = new Column(columnsMethods[t], columnsNames[t]);
            table.addColumn(c);
        }

        actionLinks = new HashMap<String, ActionLink>();
        
        for (int t = 0; t < values.size(); t++) {
            String valId=Util.reflectionString(values.get(t),idMethodName);
            ActionLink actionLink = new ActionLink("link" + valId, "Editar", this, "afterClickEdit");
            actionLink.setValue(valId);
            actionLinks.put(valId,actionLink);
            addControl(actionLink);
        }
        
        //Columna para la edición
        Column colEdit = new Column("accion", "Acción");
        colEdit.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                String valId=Util.reflectionString(object,idMethodName);
                return actionLinks.get(valId).toString();
            }
        });
        
        table.addColumn(colEdit);

        FieldSet fs = new FieldSet(".",fsName);
        fs.add(table);
        table.setRowList(values);
        form.add(fs);
    }

    
    /**
     * accion a realizar despues que se da click a editar
     * @return 
     */
    public abstract boolean afterClickEdit();
    
    /**
     * son los nombres de los metodos que se ejecutaran para obtener las columnas de la tabla
     * @return 
     */
    public abstract String[] getColumnMethods();

    /**
     * son los nombres descriptivos que se pondran en las columnas de las tablas
     * @return 
     */
    public abstract String[] getColumnName();
    
    /**
     * los valores que se desean poner en la tabla, los mismos ya deben estar ordenados como se desea
     * @return 
     */
    public abstract List<Object> getValues();
    
    /**
     * el nombre del metodo que se utilizara para sacar el identificador para los actionLinks
     * @return 
     */
    public abstract String getActionLinkIdName();


    /**
     * Nombre del contenedor principal de la tabla
     * @return 
     */
    public abstract String getFieldSetName();

    /**
     * crea el contenido antes de la tabla principal
     */
    public abstract void createPreContent();

    /**
     * crea el contenido despues de la tabla principal
     */
    public abstract void createPostContent();

}
