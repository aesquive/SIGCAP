/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.click.control.Column;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.FormTable;
import util.Util;

/**
 *
 * @author Admin
 */
public class Tenencia extends BorderPage {

    private Form form;
    private FormTable table;
    private Regcuenta sessionVar;
    
    private Select selectOrder;
    
    @Override
    public void init() {
        form = new Form("form");
        FieldSet fsB=new FieldSet("fsN","Ordenar");
        selectOrder=new Select("select","Ordenar por:", true);
        selectOrder.setDefaultOption(new Option("-1", "Seleccionar"));
        selectOrder.add(new Option("TipoValorEmisionSerie","Tipo Valor"));
        selectOrder.add(new Option("Emision","Emisión"));
        selectOrder.add(new Option("Precio", "Precio"));
        selectOrder.add(new Option("NumeroTitulosFormato", "Número de Títulos"));
        fsB.add(selectOrder);
        fsB.add(new Submit("ok", "Ordenar", this, "ordenar"));
        form.add(fsB);
        table = new FormTable("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
         sessionVar = (Regcuenta) getSessionVar("regCtaTenencia");
        String columnSort= getSessionVar("tenenciaOrdenar")==null ? "TipoValorEmisionSerie" : (String)getSessionVar("tenenciaOrdenar");
        String[] tenenciaColumns = Valores.getTenenciaColumns();
        String[] tenenciaDesColumns = Valores.getTenenciaDesColumns();
        for (int t = 0; t < tenenciaColumns.length; t++) {
            Column c = new Column(tenenciaColumns[t], tenenciaDesColumns[t]);
            c.setWidth("900 px");
            table.addColumn(c);
        }
        FieldSet fs = new FieldSet("fs", "Tenencia " + sessionVar.getDesRegCuenta());
        fs.add(table);

        table.setRowList(orderList(new LinkedList<Valores>(sessionVar.getValoreses()),columnSort));
        form.add(fs);
        addControl(form);
    }

    public boolean ordenar(){
        addSessionVar("tenenciaOrdenar",selectOrder.getValue());
        setRedirect(Tenencia.class);
        return true;
    }
    
    private List orderList(LinkedList<Valores> linkedList,String param) {
        return Util.sortValoresBy(sessionVar.getValoreses(), param, true);
    }

}
