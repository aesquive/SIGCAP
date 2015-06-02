package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.List;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.OptionGroup;
import org.apache.click.control.Select;
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
public class Menutenencia extends BorderPage{

    private Form form;
    private Select select;
    @Override
    public void init() {
        title="Reporte de Tenencia";
        form=new Form("form");
        select=new Select("ten","Ejercicio", true);
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for(Regcuenta r:createQuery){
            select.add(new Option(r.getIdRegCuenta(),r.getDesRegCuenta()));
        }
        select.setDefaultOption(new Option("-1", "Seleccionar"));
        form.add(select);
        form.add(new Submit("sub","Ver Tenencia", this, "tenencia"));
        addControl(form);
    }
    
    public boolean tenencia(){
        if(form.isValid()){
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            Regcuenta reg=null;
            for(Regcuenta r:createQuery){
                if(r.getIdRegCuenta().toString().equals(select.getValue())){
                    reg=r;
                }
            }
            addSessionVar("regCtaTenencia", reg);
            setRedirect(Tenencia.class);
            return true;
        }
        return false;
    }
}
