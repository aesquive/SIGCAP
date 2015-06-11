/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import java.util.List;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.IntegerField;

/**
 *
 * @author Admin
 */
public class ReportecambiosPage extends BorderPage {

    Form form;
    private Select project1;
    private Select project2;
    private DoubleField minVariance;
    private IntegerField numberFields;
    
    @Override
    public void init() {
        title="Auditor - Reporte de Cambios";
        form = new Form("form");
        minVariance = new DoubleField("var", "% de Tolerancia", 3, true);
        //numberFields = new IntegerField("fie", "Número de Registros", 5, true);
        project1 = new Select("project1", "Primer Ejercicio", true);
        project2 = new Select("project2", "Segundo Ejercicio", true);
        List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
        project1.setDefaultOption(new Option("-1","Seleccione"));
        project2.setDefaultOption(new Option("-1","Seleccione"));
        for (Regcuenta ru : createQuery) {
            project1.add(new Option(ru.getIdRegCuenta(), ru.getDesRegCuenta()));
            project2.add(new Option(ru.getIdRegCuenta(), ru.getDesRegCuenta()));
        }
        form.add(project1);
        form.add(project2);
        form.add(minVariance);
        //form.add(numberFields);
        form.add(new Submit("sub", "Generar reporte", this, "reporteCambios"));
        addControl(form);
    }

    public boolean reporteCambios() throws Exception {
        if (form.isValid()) {
            getContext().setSessionAttribute("compareC-0", new String[]{"1"});
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class,null);
            Regcuenta r1=null;
            Regcuenta r2=null;
            for(Regcuenta r:createQuery){
                if(r.getIdRegCuenta().toString().equals(project1.getValue())){
                    r1=r;
                }
                if(r.getIdRegCuenta().toString().equals(project2.getValue())){
                    r2=r;
                }
            }
            if(r1==null || r2==null){
                message="Favor de elegir un ejercicio válido";
                return false;
            }
            getContext().setSessionAttribute("counterCompare", 0);
            getContext().setSessionAttribute("minVariance", minVariance.getDouble());
            //getContext().setSessionAttribute("numRegs", numberFields.getInteger());
            getContext().setSessionAttribute("numRegs", -1);
            getContext().setSessionAttribute("maxCounterCompare", 0);
            getContext().setSessionAttribute("ex1-0",r1);
            getContext().setSessionAttribute("ex2-0",r2);
            setRedirect(ComparePage.class);
            return true;
        }
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 8;
    }
}
