package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Regreportes;
import java.util.List;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class ReportesPage extends BorderPage {

    private Select selectProject;
    private Select selectReport;
    private Form form;
    private List<Regreportes> reportes;
    private List<Regcuenta> regCuentas;
    private static int numPer = 1;

    @Override
    public void init() {
        title="Generador de RC's";
        form = new Form("form");
//        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) && dte.get(numPer) == true) {
//            setRedirect(NocontratadoPage.class);
//            return;
//        }
        FieldSet fsReporte = new FieldSet("fsr", "Reportes");
        selectReport = new Select("selectReport", "Reporte:");
        selectProject = new Select("selectProject", "Ejercicio:");
        reportes = DAO.createQuery(Regreportes.class, null);
        for (Regreportes r : reportes) {
            selectReport.add(new Option(r.getIdRegReportes(), r.getDesReportes()));
        }
        selectReport.setDefaultOption(new Option("-1", "Seleccionar"));
        regCuentas = DAO.createQuery(Regcuenta.class, null);
        
        for(Regcuenta r:regCuentas){
            selectProject.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        selectProject.setDefaultOption(new Option("-1", "Seleccionar"));
        fsReporte.add(selectProject);
        fsReporte.add(selectReport);
        Submit sub = new Submit("sub", "Obtener Reporte", this, "okReport");
        sub.setAttribute("onclick", "reportes();");
        fsReporte.add(sub);
        form.add(fsReporte);
        addControl(form);
    }
    
    public boolean okReport(){
        setRedirect(BienvenidaPage.class);
        message="Generando reporte en pestaña nueva ... favor de esperar";
        return true;
    }

}
