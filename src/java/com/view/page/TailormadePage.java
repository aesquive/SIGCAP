/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.view.page;

import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class TailormadePage extends BorderPage{

     Form form;
    
    @Override
    public void init() {
        form=new Form("form");
        form.setColumns(1);
        form.add(new Submit("tarjeta", "Conectar con Sistemas", this,"conectar"));
        
        addControl(form);
    }
    
    public boolean conectar(){
        message="Imposible conectar : Favor de Contactar al Proveedor";
        return false;
    }
}
