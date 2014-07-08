/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.view.page;

import db.pojos.Regcuenta;
import interpreter.MathInterpreterException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.executor.ModelExecutor;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class ViewsimulationPage extends BorderPage{

    private Form form;
    private Regcuenta regCta;
    
    @Override
    public void init() {
        regCta=(Regcuenta)getSessionVar("simex");
        form=new Form("form");
        FieldSet fs=new FieldSet("fs", "Activos");
        fs.add(new Submit("editTarjeta","Editar Tarjeta de Crédito",this, "editarTarjeta"));
        fs.add(new Submit("editPrestamo","Editar Prestamos",this, "editarPrestamo"));
        fs.add(new Submit("editTenencia","Editar Tenencia",this, "editarTenenencia"));
        FieldSet fsp=new FieldSet("fsp", "Pasivos");
        fsp.add(new Submit("editCaptacion","Editar Captación",this, "editarCaptacion"));
        fsp.add(new Submit("editDisponibilidad","Editar Disponibilidad",this, "editarDisponibilidad"));
        form.add(fs);
        form.add(fsp);
        form.add(new Submit("sub","Calcular Ejercicio", this, "calcular"));
        addControl(form);
    }
    
    
    public boolean editarPrestamo(){
        setRedirect(SimulacionprestamoPage.class);
        return true;
    }
    public boolean editarTarjeta(){
        setRedirect(SimulaciontarjetaPage.class);
        return true;
    }
    
    public boolean editarCaptacion(){
        setRedirect(SimulacioncaptacionPage.class);
        return true;
    }
    
    public boolean editarDisponibilidad(){
        setRedirect(SimulaciondisponibilidadPage.class);
        return true;
    }
    public boolean editarTenencia(){
        setRedirect(SimulaciontenenciaPage.class);
        return true;
    }
    
    public boolean calcular(){
        try {
            ModelExecutor mc= new ModelExecutor(regCta,false);
            mc.start();
            
        setRedirect(IcapPage.class);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ViewsimulationPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
