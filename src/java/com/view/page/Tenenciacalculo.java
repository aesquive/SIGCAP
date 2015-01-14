package com.view.page;

import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.LinkedList;
import java.util.List;
import model.executor.ModelExecutor;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.Util;

/**
 *
 * @author Admin
 */
public class Tenenciacalculo extends BorderPage {

    private Form form;
    private FormTable tableNoMapeada;
    private FormTable tableMapeada;
    private Regcuenta sessionVar;

    private List<Valores> tenenciaNoMapeadaLista;
    private List<Valores> tenenciaMapeada;
    @Override
    public void init() {
        form = new Form("form");
        sessionVar = (Regcuenta) getSessionVar("regCtaTenencia");
        List<Valores> todoMapeo = new LinkedList<Valores>(sessionVar.getValoreses());
        tenenciaNoMapeadaLista = new LinkedList<Valores>();
        tenenciaMapeada = new LinkedList<Valores>();
        for (Valores val : todoMapeo) {
            if (val.getMapeado() != null && val.getMapeado() == 1) {
                tenenciaNoMapeadaLista.add(val);
            }
            if (val.getMapeado() != null && val.getMapeado() == 0) {
                tenenciaMapeada.add(val);
            }
            if (val.getMapeado() == null || val.getMapeado() == 2) {
                message = "El layout de Tenencia no fue cargado correctamente . Se debe reprocesar el ejercicio";
                form.add(new Submit("mapeo", "Ir a DataWarehouse", this, "datawarehouse"));
                addControl(form);
                return;
            }
        }
        form.add(new HiddenField("pryName", sessionVar.getIdRegCuenta()));
        tableMapeada = new FormTable("dataTableSmall");
        tableMapeada.setPageNumber(0);
        tableMapeada.setClass(Table.CLASS_ORANGE2);
        tableNoMapeada = new FormTable("dataTableSmall");
        tableNoMapeada.setPageNumber(0);
        tableNoMapeada.setClass(Table.CLASS_ORANGE2);
        String[] tenenciaColumns = Valores.getTenenciaColumns();
        String[] tenenciaDesColumns = Valores.getTenenciaDesColumns();
        for (int t = 0; t < tenenciaColumns.length; t++) {
            Column c = new Column(tenenciaColumns[t], tenenciaDesColumns[t]);
            c.setWidth("900 px");
            tableNoMapeada.addColumn(c);
            tableMapeada.addColumn(c);
        }

        for (int t = 0; t < tenenciaNoMapeadaLista.size(); t++) {
            ActionLink act = new ActionLink("act" + tenenciaNoMapeadaLista.get(t).getIdTenencia(), "Editar", this, "editar");
            act.setValue(tenenciaNoMapeadaLista.get(t).getIdTenencia().toString());
            tenenciaNoMapeadaLista.get(t).setEditLink(act);
            addControl(act);
        }
        Column c = new Column("editar", "Editar");
        c.setDecorator(new Decorator() {
           @Override
            public String render(Object object, Context context) {
                Valores val=(Valores)object;
                return val.getEditLink().toString();
            }
        });
        tableNoMapeada.addColumn(c);
        
        FieldSet fs = new FieldSet("fs", "Tenencia Mapeada Manualmente " + sessionVar.getDesRegCuenta());
        fs.add(tableNoMapeada);
        tableNoMapeada.setRowList(orderList(tenenciaNoMapeadaLista, "TipoValorEmisionSerie"));

        FieldSet fsm = new FieldSet("fsM", "Tenencia Mapeada Automaticamente " + sessionVar.getDesRegCuenta());
        fsm.add(tableMapeada);
        tableMapeada.setRowList(orderList(tenenciaMapeada, "TipoValorEmisionSerie"));
        Submit sub = new Submit("calc", "Calcular", this, "calcular");
//        javaScriptProcess(sub);
        FieldSet acciones = new FieldSet("fsA", "Acciones");
        acciones.add(sub);
        form.add(acciones);
        form.add(fs);
        form.add(fsm);
        addControl(form);
    }

    public boolean calcular() {
        try {
            ModelExecutor mex = new ModelExecutor(sessionVar);
            mex.start();
            setRedirect(IcapPage.class);
            return true;
        } catch (Exception ex) {
            message = "Error al calcular el ejercicio";
            System.out.println("exception -------------");
            System.out.println(ex.getStackTrace());
            return false;
        }

    }

    public boolean datawarehouse() {
        setRedirect(CargadatosPage.class);
        return true;
    }

    private List orderList(List<Valores> linkedList, String param) {
        return Util.sortValoresBy(linkedList, param, true);
    }

    public boolean editar(){
        Valores val=null;
        for(Valores v:tenenciaNoMapeadaLista){
            if(v.getEditLink().isClicked()){
                val=v;
            }
        }
        addSessionVar("editCalcTenencia", val);
        setRedirect(TenenciaeditarcalculoPage.class);
        return true;
    }
    
}
