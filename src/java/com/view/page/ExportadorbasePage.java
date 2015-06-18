/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.export.DBExporter;
import java.util.Calendar;
import java.util.Locale;
import manager.configuration.Configuration;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/**
 *
 * @author desarrollo
 */
public class ExportadorbasePage extends BorderPage{

    @Override
    public void init() {
        title="Respaldo de Datos";
        Form form=new Form("form");
        Submit sub=new Submit("exp", "Exportar Base de Datos", this, "exportarBase");
        sub.setAttribute("onclick", "reporteBase();");
        form.add(sub);
        addControl(form);
    }
    
    public boolean exportarBase(){
        setRedirect(BienvenidaPage.class);
        return true;
    }
    

    @Override
    public Integer getPermisoNumber() {
        return 15;
    }
    
}
