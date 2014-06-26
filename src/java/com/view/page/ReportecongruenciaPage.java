package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.DoubleField;

/**
 *
 * @author Admin
 */
public class ReportecongruenciaPage extends BorderPage {

    private Select selectA;
    private Select selectB;
    private DoubleField tolerancia;
    private Form form;

    @Override
    public void init() {
        form = new Form("form");
        selectA = new Select("pra", "Seleccionar Ejercicio", true);
        selectB = new Select("prb", "Seleccionar Ejercicio", true);
        selectA.setDefaultOption(new Option("-1","Seleccione"));
        selectB.setDefaultOption(new Option("-1","Seleccione"));
        tolerancia = new DoubleField("tolerancia", "% de Tolerancia", 3, true);
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : createQuery) {
            selectA.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
            selectB.add(new Option(r.getIdRegCuenta(), r.getDesRegCuenta()));
        }
        form.add(selectA);
        form.add(selectB);
        form.add(tolerancia);
        form.add(new Submit("submit","Generar Reporte",this,"getReport"));
        addControl(form);
    }

    public boolean getReport() {
        if (form.isValid()) {
            try {
                Regcuenta regCuentaUno=null;
                Regcuenta regCuentaDos=null;
                List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
                for (Regcuenta r : createQuery) {
                    if (r.getIdRegCuenta().toString().equals(selectA.getValue())) {
                        regCuentaUno = r;
                    }
                    if (r.getIdRegCuenta().toString().equals(selectB.getValue())) {
                        regCuentaDos = r;
                    }
                }
                if(regCuentaUno==null || regCuentaDos==null){
                    message="Favor de seleccionar un ejercicio válido";
                    return false;
                }
                BrowserLauncher browser = new BrowserLauncher();
                browser.setNewWindowPolicy(true);
                browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=3&pra=" + regCuentaUno.getIdRegCuenta() + "&prb=" + regCuentaDos.getIdRegCuenta() + "&var=" + tolerancia.getDouble() / 100 + "&num=-1" );
                return true;
            } catch (BrowserLaunchingInitializingException ex) {
                Logger.getLogger(ReportecongruenciaPage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedOperatingSystemException ex) {
                Logger.getLogger(ReportecongruenciaPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        message = "Favor de llenar todos los campos";
        return false;
    }

}
