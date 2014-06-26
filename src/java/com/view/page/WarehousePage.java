package com.view.page;


import com.view.page.BorderPage;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class WarehousePage extends BorderPage{

    private Form form;
    
    @Override
    public void init() {
        form=new Form("form");
        FieldSet fsc=new FieldSet("fsc","Carga de Información");
        fsc.add(new Submit("carga","Cargar Información", this, "cargaDatos"));
        FieldSet fsr=new FieldSet("fsr","Reportes");
        fsr.setColumns(1);
        fsr.add(new Submit("consistencia","Reporte de Consistencia", this, "consistencia"));
        fsr.add(new Submit("congruencia","Reporte de Congruencia", this, "congruencia"));
        fsr.add(new Submit("integridad","Reporte de Integridad", this, "integridad"));
        form.add(fsc);
        form.add(fsr);
        addControl(form);
    }
    
    public boolean cargaDatos(){
        setRedirect(CargadatosPage.class);
        return true;
    }
    
    public boolean consistencia(){
        setRedirect(ReporteconsistenciaPage.class);
        return true;
    }
    
    public boolean congruencia(){
            setRedirect(ReportecongruenciaPage.class);
        return true;
    
    }
    
    public boolean integridad(){
        setRedirect(ReporteintegridadPage.class);
        return true;
    }
}
