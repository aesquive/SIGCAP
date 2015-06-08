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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.control.Column;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Label;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
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
        title="Reporte de Tenencia";
        form = new Form("form");
        sessionVar = (Regcuenta) getSessionVar("regCtaTenencia");
        form.add(new HiddenField("pryName", sessionVar.getIdRegCuenta()));
        FieldSet fsB = new FieldSet("fsN", "Ordenar");
        selectOrder = new Select("select", "Ordenar por:", true);
        selectOrder.setDefaultOption(new Option("-1", "Seleccionar"));
        selectOrder.add(new Option("TipoValorEmisionSerie", "Tipo Valor"));
        selectOrder.add(new Option("Emision", "Emisión"));
        selectOrder.add(new Option("Precio", "Precio"));
        selectOrder.add(new Option("NumeroTitulosFormato", "Número de Títulos"));
        fsB.add(selectOrder);
        fsB.add(new Submit("ok", "Ordenar", this, "ordenar"));
        Submit sub=new Submit("expr", "Exportar", this, "exportar");
        sub.setAttribute("onclick", "createTenencia();");
        fsB.add(sub);
        form.add(fsB);
        table = new FormTable("dataTableSmall");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        String columnSort = getSessionVar("tenenciaOrdenar") == null ? "TipoValorEmisionSerie" : (String) getSessionVar("tenenciaOrdenar");
        String[] tenenciaColumns = Valores.getSimColumns();
        String[] tenenciaDesColumns = Valores.getSimDesColumns();
        for (int t = 0; t < tenenciaColumns.length; t++) {
            Column c = new Column(tenenciaColumns[t], tenenciaDesColumns[t]);
            table.addColumn(c);
        }
        FieldSet fs = new FieldSet("fs", "Tenencia " + sessionVar.getDesRegCuenta());
        fs.add(table);

        table.setRowList(orderList(new LinkedList<Valores>(sessionVar.getValoreses()), columnSort));
        form.add(fs);
        addControl(form);
    }

    public boolean ordenar() {
        addSessionVar("tenenciaOrdenar", selectOrder.getValue());
        setRedirect(Tenencia.class);
        return true;
    }

    public boolean exportar() {
        try {
//            BrowserLauncher browser = new BrowserLauncher();
//            browser.setNewWindowPolicy(true);
//            browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=5&pro="+sessionVar.getIdRegCuenta());
            return true;
        } catch (Exception ex) {
            message="Existe algun error al generar el reporte";
            Logger.getLogger(Tenencia.class.getName()).log(Level.INFO, null, ex);
            return false;
        } 

    }

    private List orderList(LinkedList<Valores> linkedList, String param) {
        return Util.sortValoresBy(linkedList, param, true);
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

}
