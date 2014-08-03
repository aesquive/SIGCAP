/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.view.page;

import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;

/**
 *
 * @author Admin
 */
public class CaptacioneditPage extends  SimulacionPage{
    
    private TextField catalogoCuenta;
    private TextField descripcion;
    private TextField idCaptacion;
    private DoubleField monto;
    private DateField fechaVencimiento;
    
    public CaptacioneditPage(){
        catalogoCuenta=new TextField("cata","Catalogo Mínimo",true);
        descripcion=new TextField("des", "Descripción", false);
        idCaptacion=new TextField("idCap","Identificador Captación", true);
        monto=new DoubleField("monto","Monto", true);
        fechaVencimiento=new DateField("fecha", "Fecha de Vencimiento", true);
        getForm().add(catalogoCuenta);
        getForm().add(descripcion);
        getForm().add(idCaptacion);
        getForm().add(monto);
        getForm().add(fechaVencimiento);
        getForm().add(new Submit("sub","Guardar", this, "submit"));
    }
    
    public boolean submit(){
        return true;
    }
}
