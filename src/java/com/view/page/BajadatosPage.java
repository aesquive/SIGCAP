package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import java.util.List;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author desarrollo
 */
public class BajadatosPage extends BorderPage {

    private Select selectProject;
    private Form form;
    
    private List<Regcuenta> regCuentas;
    
    @Override
    public void init() {
        title = "DataWarehouse - Baja de Ejercicio";
        form = new Form("form");
        FieldSet fsReporte = new FieldSet("fsr", "Reportes");
        selectProject = new Select("selectProject", "Ejercicio:");
        regCuentas = DAO.createQuery(Regcuenta.class, null);
        for(Regcuenta r:regCuentas){
            selectProject.add(new Option(r.getIdRegCuenta().toString(), r.getDesRegCuenta()));
        }
        selectProject.setDefaultOption(new Option("-1", "Seleccionar"));
        fsReporte.add(selectProject);
        Submit sub = new Submit("sub", "Borrar Ejercicio", this, "borrar");
        fsReporte.add(sub);
        form.add(fsReporte);
        addControl(form);
    }
    
    public boolean borrar(){
        String value = selectProject.getValue();
        Regcuenta selec=null;
        for(Regcuenta r:regCuentas){
            if(value.equals(r.getIdRegCuenta().toString())){
                selec=r;
            }
        }
        DAO.delete(selec);
        message="Ejercicio eliminado";
        setRedirect(BienvenidaPage.class);
        return true;
    }

}
