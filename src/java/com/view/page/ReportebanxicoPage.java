package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import java.util.List;
import org.apache.click.control.*;

/**
 *
 * @author desarrollo
 */
public class ReportebanxicoPage extends BorderPage{
    
    
     private Select selectA;
   
    private Form form;

    @Override
    public void init() {
        title="Reporte de Banxico";
        form = new Form("form");
        selectA = new Select("pra", "Seleccionar Ejercicio", true);
        selectA.setDefaultOption(new Option("-1", "Seleccione"));
        List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
        for (Regcuenta r : createQuery) {
            selectA.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(selectA);
        Submit sub = new Submit("submit", "Generar Reporte", this, "getReport");
        sub.setAttribute("onclick", "reporteBanxico();");

        form.add(sub);
        addControl(form);
    }

    public boolean getReport() {
        if (form.isValid()) {
            Regcuenta regCuentaUno = null;
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta r : createQuery) {
                if (r.getIdRegCuenta().toString().equals(selectA.getValue())) {
                    regCuentaUno = r;
                }
            }
            if (regCuentaUno == null ) {
                message = "Favor de seleccionar un ejercicio válido";
                return false;
            }
            
            DAO.saveRecordt(user, "Genera el reporte de Banxico sobre "+regCuentaUno.getDesRegCuenta());
            return true;

        }
        message = "Favor de llenar todos los campos";
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 10;
    }
    
}
