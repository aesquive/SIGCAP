/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.LinkedList;
import java.util.Set;
import org.apache.click.control.Column;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;

/**
 *
 * @author Admin
 */
public class Tenencia extends BorderPage {

    private Form form;
    private FormTable table;

    @Override
    public void init() {
        form = new Form("form");
        table = new FormTable("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        Regcuenta sessionVar = (Regcuenta) getSessionVar("regCtaTenencia");
        Set<Valores> valoreses = sessionVar.getValoreses();
        String[] tenenciaColumns = Valores.getTenenciaColumns();
        String[] tenenciaDesColumns = Valores.getTenenciaDesColumns();
        for(int t=0;t<tenenciaColumns.length;t++){
            Column c=new Column(tenenciaColumns[t], tenenciaDesColumns[t]);
            c.setWidth("900 px");
            table.addColumn(c);
        }
        FieldSet fs=new FieldSet("fs","Tenencia "+sessionVar.getDesRegCuenta());
        fs.add(table);
        table.setRowList(new LinkedList<Valores>(sessionVar.getValoreses()));
        form.add(fs);
        addControl(form);
    }

}
