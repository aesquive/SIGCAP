package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Regreportes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class ReportesPage extends BorderPage {

    private Select selectProject;
    private Form form;
    private List<Regreportes> reportes;
    private List<Regcuenta> regCuentas;
    private static int numPer = 1;
    private Map<String,Checkbox> mapReports;
    
    @Override
    public void init() {
        title="Generador de RC's";
        mapReports=new HashMap<String, Checkbox>();
        form = new Form("form");
//        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) && dte.get(numPer) == true) {
//            setRedirect(NocontratadoPage.class);
//            return;
//        }
        FieldSet fsReporte = new FieldSet("fsr", "Reportes");
        selectProject = new Select("selectProject", "Ejercicio:");
        reportes = DAO.createQuery(Regreportes.class, null);
        regCuentas = DAO.getEjerciciosCalculados();
        
        for(Regcuenta r:regCuentas){
            selectProject.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        fsReporte.add(selectProject);
        fsReporte.add(new Label("lab", "Seleccione los reportes a generar:"));
        for (Regreportes r : reportes) {
            Checkbox cb=new Checkbox(r.getIdRegReportes().toString(), r.getDesReportes());
            cb.setId("check_box_"+r.getIdRegReportes());
            fsReporte.add(cb);
            mapReports.put(r.getIdRegReportes().toString(),cb);
        }
        selectProject.setDefaultOption(new Option("-1", "Seleccionar"));
        Submit sub = new Submit("sub", "Obtener Reporte", this, "okReport");
        sub.setAttribute("onclick", "reportes();");
        fsReporte.add(sub);
        form.add(fsReporte);
        addControl(form);
    }
    
    public boolean okReport(){
        String ids="";
        String names="";
        for(String s:mapReports.keySet()){
            Checkbox cb = mapReports.get(s);
            if(cb.isChecked()){
                ids+=cb.getValue()+",";
                names+=cb.getLabel()+",";
            }
        }
        DAO.saveRecordt(user, user.getUser() + " generó reportes "+names);
        setRedirect(BienvenidaPage.class);
        //message="Generando reporte en pestaña nueva ... favor de esperar";
        return true;
    }

    @Override
    public Integer getPermisoNumber() {
        return 3;
    }

}
