/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Regcuenta;
import db.pojos.Valores;
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
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.FormTable;
import util.Util;

/**
 *
 * @author desarrollo
 */
public class SimulaciontenenciaPage extends SimulacionPage {

    Form form;
    Regcuenta regCta;
    TextField fieldSearch;
    private FormTable table;
    private Select selectOrder;
    private Map<String, ActionLink> actionLinks;

    @Override
    public void initSimulacionComponents() {
        name.setLabel("Simulador de Tenencia");
        form = new Form("formSim");
        createFSEditTenencia();
        addControl(form);
    }

    /**
     * crea el formulario correspondiente a la edicion de variables de cuentas
     *
     * @return
     */
    private void createFSEditTenencia() {
        regCta = (Regcuenta) getSessionVar("prySim");

        FieldSet fsBotones = new FieldSet("fsTenencia", "Tenencia");
        selectOrder = new Select("select", "Ordenar por:", true);
        selectOrder.setDefaultOption(new Option("-1", "Seleccionar"));
        selectOrder.add(new Option("TipoValorEmisionSerie", "Tipo Valor"));
        selectOrder.add(new Option("Emision", "Emisión"));
        selectOrder.add(new Option("Precio", "Precio"));
        selectOrder.add(new Option("NumeroTitulosFormato", "Número de Títulos"));
        fsBotones.add(selectOrder);
        fsBotones.add(new Submit("ok", "Ordenar", this, "ordenar"));
        table = new FormTable("dataTableSmall");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        String columnSort = getSessionVar("tenenciaOrdenar") == null ? "TipoValorEmisionSerie" : (String) getSessionVar("tenenciaOrdenar");
        String[] tenenciaColumns = Valores.getTenenciaColumns();
        String[] tenenciaDesColumns = Valores.getTenenciaDesColumns();
        for (int t = 0; t < tenenciaColumns.length; t++) {
            Column c = new Column(tenenciaColumns[t], tenenciaDesColumns[t]);
            c.setWidth("900 px");
            table.addColumn(c);
        }

        List<Valores> vals = new LinkedList<Valores>(regCta.getValoreses());
        List<Valores> orderList = orderList(vals, columnSort);

        actionLinks = new HashMap<String, ActionLink>();
        for (int t = 0; t < orderList.size(); t++) {
            ActionLink actionLink = new ActionLink("link" + orderList.get(t).getIdTenencia(), "Editar", this, "editTenencia");
            actionLink.setValue(orderList.get(t).getIdTenencia().toString());
            actionLinks.put(orderList.get(t).getIdTenencia().toString(), actionLink);
            addControl(actionLink);
        }

        //Columna para la edición
        Column colEdit = new Column("accion", "Acción");
        colEdit.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                Valores val = (Valores) object;
                return actionLinks.get(val.getIdTenencia().toString()).toString();
            }
        });
        table.addColumn(colEdit);

        FieldSet fs = new FieldSet(".", "Tenencia " + regCta.getDesRegCuenta());
        fs.add(table);
        table.setRowList(orderList);

        form.add(fsBotones);
        form.add(fs);
    }

    public boolean ordenar() {
        addSessionVar("tenenciaOrdenar", selectOrder.getValue());
        setRedirect(SimulaciontenenciaPage.class);
        return true;
    }

    private List orderList(List<Valores> linkedList, String param) {
        return Util.sortValoresBy(linkedList, param, true);
    }

    public boolean editTenencia() {
        Set<String> keySet = actionLinks.keySet();
        for (String s : keySet) {
            boolean clicked = actionLinks.get(s).isClicked();
            if (clicked) {
                addSessionVar("idTenenciaEdit",s);
//                setRedirect(Tenenciaeditsimulacion.class);
                return true;
            }
        }
        return false;
    }

}
