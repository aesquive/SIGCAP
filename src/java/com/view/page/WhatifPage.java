package com.view.page;

import db.controller.DAO;
import db.pojos.Captacion;
import db.pojos.Catalogominimo;
import db.pojos.Consistencia;
import db.pojos.Cuenta;
import db.pojos.Disponibilidad;
import db.pojos.Ingresosnetos;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import db.pojos.Reservas;
import db.pojos.Tarjetacredito;
import db.pojos.User;
import db.pojos.Valores;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import util.Util;

/**
 *
 * @author Admin
 */
public class WhatifPage extends BorderPage {

    Form formView;

    Form form;
    Select selectProject;
    Select selectView;
    TextField nameSimulation;
    boolean onceClicked;
    User user;
    private static int numPer = 5;

    public WhatifPage() {
        super();
    }

    @Override
    public void init() {
        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer))&& dte.get(numPer)==true) {
            setRedirect(NocontratadoPage.class);
            return;
        }
        form = new Form("form");
        formView = new Form("formView");
        onceClicked = true;
        selectProject = new Select("Ejercicio Base");
        selectView = new Select("Ejercicio");
        nameSimulation = new TextField("Nombre de la Simulación");
        selectProject.setId("selectwhatif");
        nameSimulation.setRequired(true);
        user = (User) getSessionVar("user");
        List<Regcuentauser> createQuery = DAO.createQuery(Regcuentauser.class, null);
        for (Regcuentauser ru : createQuery) {
            if (ru.getUser().getIduser() == user.getIduser()) {
                selectProject.add(new Option(ru.getRegcuenta(), ru.getRegcuenta().getDesRegCuenta()));
                selectView.add(new Option(ru.getRegcuenta(), ru.getRegcuenta().getDesRegCuenta()));
            }
        }
        form.add(selectProject);
        form.add(nameSimulation);
        form.add(new Submit("okWhatIf", "Crear Simulación", this, "okWhatIfClicked"));
        formView.add(selectView);
        formView.add(new Submit("okEditWhatIf", "Editar Simulación", this, "okViewClicked"));
        addControl(formView);
        addControl(form);
    }

    /**
     * evento de inicio de la simulación
     *
     * @return
     */
    public boolean okWhatIfClicked() {
        if (nameSimulation.getValue().equals("")) {
            return false;
        }
        if (onceClicked) {
            copiarProyecto();
            setRedirect(ViewsimulationPage.class);
        }
        return true;
    }

    public boolean okViewClicked() {
        Regcuenta regCta = null;
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta rc : createQuery) {
            if (Integer.parseInt(selectView.getValue()) == rc.getIdRegCuenta()) {
                try {
                    regCta = (Regcuenta) rc.clone();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(WhatifPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        addSessionVar("simex", regCta);
        setRedirect(ViewsimulationPage.class);
        return true;
    }

    private void copiarProyecto() {
        try {
            Regcuenta regCuenta = new Regcuenta();
            Calendar c = Calendar.getInstance();
            regCuenta.setDesRegCuenta(nameSimulation.getValue());
            regCuenta.setFecha(c.getTime());
            DAO.save(regCuenta);
            copiarCuentas(regCuenta);
            Regcuenta selected = null;
            List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta rc : createQuery) {
                if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                    selected = (Regcuenta) rc.clone();
                }
            }
            DAO.saveRecordt(user, "Creo una simulación de " + selected.getDesRegCuenta() + " llamada " + regCuenta.getDesRegCuenta());
            addSessionVar("simex", regCuenta);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(WhatifPage.class.getName()).log(Level.FINE, null, ex);
        }
    }

    private void copiarCuentas(Regcuenta regCuenta) throws CloneNotSupportedException {
        Regcuenta regCta = null;
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta rc : createQuery) {
            if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                regCta = (Regcuenta) rc.clone();
            }
        }
        Set<Captacion> captacions = regCta.getCaptacions();
        Set<Catalogominimo> catMin=regCta.getCatalogominimos();
        Consistencia consistenciaAnterior=(Consistencia) regCuenta.getConsistencias().iterator().next();
        Set<Disponibilidad> disponibilidades=regCta.getDisponibilidads();
        Set<Ingresosnetos> ingresos=regCta.getIngresosnetoses();
        Set<Reservas> reservas=regCta.getReservases();
        Set<Tarjetacredito> tarjeta=regCta.getTarjetacreditos();
        Set<Valores> valores=regCta.getValoreses();
        List<Object> items=new LinkedList<Object>();
        Consistencia cons=new Consistencia(regCuenta, consistenciaAnterior.getDisponibilidadesLeidos(),
                consistenciaAnterior.getTenenciaLeidos(), consistenciaAnterior.getCaptacionLeidos(),consistenciaAnterior.getTarjetaCreditoLeidos()
                , consistenciaAnterior.getPrestamosLeidos(), consistenciaAnterior.getCatalogoMinimoLeidos(), consistenciaAnterior.getReservasLeidos(), consistenciaAnterior.getIngresosLeidos());
        items.add(cons);
        for(Captacion c:captacions){
            Captacion nueva=new Captacion(regCta, c.getCatalogocuenta(),c.getFecha(), c.getDescripcion(), c.getIdCuentaCaptacion(), c.getMonto(), c.getFechaVencimiento());
            items.add(nueva);
        }
        for(Catalogominimo c:catMin){
            Catalogominimo nueva=new Catalogominimo(regCta, c.getCatalogocuenta(), c.getFecha(), c.getValor(), c.getMoneda());
            items.add(nueva);
        }
        for(Disponibilidad c:disponibilidades){
            Disponibilidad nueva=new Disponibilidad(regCta, c.getCatalogocuenta(),c.getFecha(), c.getDescripcion(), c.getMonto(), c.getFechaVencimiento());
            items.add(nueva);
        }
        for(Ingresosnetos c:ingresos){
            Ingresosnetos nueva=new Ingresosnetos(regCta, c.getFecha(), c.getNumeroMes(), c.getIngresoNeto());
            items.add(nueva);
        }
        for(Reservas c:reservas){
            Reservas nueva=new Reservas(regCta, c.getFecha(), c.getEstatusCrediticio(), c.getMonto());
            items.add(nueva);
        }
        for(Tarjetacredito c:tarjeta){
            Tarjetacredito nueva=new Tarjetacredito(regCta, c.getCatalogocuenta(),c.getFecha(), c.getDescripcion(), c.getIdCredito(), c.getSaldoInsoluto(), c.getFechaCorte(),c.getTipoTarjeta(), c.getRelevante());
            items.add(nueva);
        }
        
        for(Valores c:valores){
            Valores nueva=new Valores(regCta, c.getFecha(), c.getIdCuentaContable(), c.getDescripcion(), c.getNumeroTitulos(), c.getTipoValor(), c.getEmision(), c.getSerie(), c.getFechaProximoCupon(), c.getGrupoRc10()
                    ,c.getPrecio(),c.getSobretasa(),c.getCalificacion(),c.getGrupoRc07(), c.getPonderador(), c.getPlazo(), c.getFechaVencimiento(),c.getMoneda(), c.getGradoRiesgo());
            items.add(nueva);
        }
        DAO.saveUpdateMultiple(items);
    }

    
}
