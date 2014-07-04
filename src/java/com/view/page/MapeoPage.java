package com.view.page;

import db.pojos.Captacion;
import db.pojos.Catalogominimo;
import db.pojos.Consistencia;
import db.pojos.Disponibilidad;
import db.pojos.Ingresosnetos;
import db.pojos.Prestamo;
import db.pojos.Regcuenta;
import db.pojos.Reservas;
import db.pojos.Tarjetacredito;
import db.pojos.Valores;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.Vector;

/**
 *
 * @author Admin
 */
public class MapeoPage extends BorderPage {

    private Form form;
    private FormTable formTable;
    private Regcuenta regcuenta;
    private Consistencia consistencia;
    private List<Captacion> captacion;
    private List<Catalogominimo> catalogominimo;
    private List<Disponibilidad> disponibilidades;
    private List<Ingresosnetos> ingresos;
    private List<Prestamo> prestamos;
    private List<Reservas> reservas;
    private List<Tarjetacredito> tarjeta;
    private List<Valores> tenencia;
    private Map<String, Vector> vector;
    private Map<String,Valores> tenenciaMapeada;
    private List<Valores> tenenciaNoMapeada;
    
    @Override
    public void init() {
        form = new Form("form");
        formTable=new FormTable("formTable");
        consistencia = (Consistencia) getSessionVar("mapeoConsistencia");
        regcuenta=(Regcuenta)getSessionVar("mapeoRegCuenta");
        captacion=(List<Captacion>)getSessionVar("mapeoCaptación");
        catalogominimo =(List<Catalogominimo>)getSessionVar("mapeoCatalogo");
        disponibilidades=(List<Disponibilidad>) getSessionVar("mapeoDisponibilidades");
        ingresos=(List<Ingresosnetos>)getSessionVar("mapeoIngresos");
        prestamos=(List<Prestamo>)getSessionVar("mapeoPrestamos");
        reservas=(List<Reservas>)getSessionVar("mapeoReservas");
        tarjeta=(List<Tarjetacredito>)getSessionVar("mapeoTarjeta");
        tenencia=(List<Valores>)getSessionVar("mapeoTenencia");
        vector=(Map<String,Vector>)getSessionVar("mapeoVector");
        revisarTenenciaVector();
        if(tenenciaNoMapeada==null || tenenciaNoMapeada.isEmpty()){
            form.add(new Label("lab", "La tenencia esta mapeada correctamente"));
            form.add(new Submit("sub","Guardar Ejercicio", this, "generadorRc"));
            addControl(form);
            return;
        }
        formTable.setName("dataTable");
        formTable.setPageNumber(0);
        formTable.setClass(Table.CLASS_ORANGE2);
        String[] columnsMethods=Valores.getColumnsMethods();
        String[] columnsDescr=Valores.getColumnsDescriptions();
        for(int t=0;t<columnsMethods.length;t++){
            Column c=new Column(columnsMethods[t], columnsDescr[t]);
            c.setWidth("900 px");
            formTable.addColumn(c);
        }
        for(int t=0;t<tenenciaNoMapeada.size();t++){
            ActionLink ac=new ActionLink("ac"+t,"Editar",this, "editarNoMapeada");
            ac.setValue(tenenciaNoMapeada.get(t).getIdTenencia().toString());
            addControl(ac);
            tenenciaNoMapeada.get(t).setEditLink(ac);
        }
        Column edit=new Column("editColumn", "Acción");
        edit.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                Valores val=(Valores)object;
                return val.getEditLink().toString();
            }
        });
        formTable.addColumn(edit);
        FieldSet fs=new FieldSet("fs", "Tenencia no mapeada");
        fs.add(formTable);
        form.add(fs);
        addControl(form);
        formTable.setRowList(tenenciaNoMapeada);
    }
    
    public boolean generadorRc(){
        cruzarVector(vector,tenenciaMapeada);
        setRedirect(ReportesPage.class);
        return true;
    }

    private void revisarTenenciaVector() {
        tenenciaMapeada=new HashMap<String, Valores>();
        tenenciaNoMapeada=new LinkedList<Valores>();
        for(Valores val:tenencia){
            String tv=val.getTipoValor()==null ? "":val.getTipoValor();
            String emision=val.getEmision()==null ?"":val.getEmision();
            String serie=val.getSerie()==null ? "":val.getSerie();
            Vector get = vector.get(tv+emision+serie);
            if(get!=null){
                tenenciaMapeada.put(tv+emision+serie,val);
            }else{
                tenenciaNoMapeada.add(val);
            }
        }
    }

    
    public boolean editarNoMapeada(){
        Valores valorNoMapeado=null; 
        for(Valores val:tenenciaNoMapeada){
             if(val.getEditLink().isClicked()){
                 valorNoMapeado=val;
             }
         }
        addSessionVar("editMapeo", valorNoMapeado);
        setRedirect(MapeoeditPage.class);
        return true;
    }

    private void cruzarVector(Map<String, Vector> vector, Map<String, Valores> tenenciaMapeada) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
