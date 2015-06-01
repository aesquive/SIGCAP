/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import static com.view.page.BorderPage.lic;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import util.Util;

/**
 *
 * @author Admin
 */
public class AuditorPage extends BorderPage {

    Form form;
    private static int numPer = 4;

    @Override
    public void init() {
        
//        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer))&& dte.get(numPer)==true) {
//            setRedirect(NocontratadoPage.class);
//            return;
//        }
        form = new Form("form");
        form.add(new Submit("comp","Reporte de Validación", this, "comparator"));
        form.add(new Submit("subTracking", "Tracking Log",this,"tracking"));
        
        addControl(form);
    }

    public boolean comparator(){
        setRedirect(ReportecambiosPage.class);
        return true;
    }
    
    public boolean tracking(){
        setRedirect(TrackinglogPage.class);
        return true;
    }
}
