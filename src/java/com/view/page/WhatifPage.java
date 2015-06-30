package com.view.page;

import db.controller.DAO;
import db.pojos.Captacion;
import db.pojos.Catalogominimo;
import db.pojos.Consistencia;
import db.pojos.Cuenta;
import db.pojos.Disponibilidad;
import db.pojos.Ingresosnetos;
import db.pojos.Prestamo;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import db.pojos.Reservas;
import db.pojos.Tarjetacredito;
import db.pojos.User;
import db.pojos.Valores;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.executor.ModelExecutor;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.hibernate.Transaction;

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

    public WhatifPage() {
        super();
    }

    /**
     * crea los menus y datos de inicio de la pagina
     */
    @Override
    public void init() {
        message = null;
        title = "Simulador de Capital";
        form = new Form("form");
        formView = new Form("formView");
        onceClicked = true;
        selectProject = new Select("Ejercicio Base", true);
        selectProject.setDefaultOption(new Option("-1", "--Seleccione--"));
        selectView = new Select("Ejercicio", true);

        selectView.setDefaultOption(new Option("-1", "--Seleccione--"));
        nameSimulation = new TextField("Nombre de la Simulación", true);
        selectProject.setId("selectwhatif");
        user = (User) getSessionVar("user");
        List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
        for (Regcuenta ru : createQuery) {

            selectProject.add(new Option(ru.getIdRegCuenta(), ru.getDesRegCuenta()));
            selectView.add(new Option(ru.getIdRegCuenta(), ru.getDesRegCuenta()));

        }
        form.add(selectProject);
        form.add(nameSimulation);
        Submit crear = new Submit("okWhatIf", "Crear Simulación", this, "okWhatIfClicked");
        crear.setAttribute("onclick", "waitPage();");
        form.add(crear);

        formView.add(selectView);
        formView.add(new Submit("okEditWhatIf", "Editar Simulación", this, "okViewClicked"));
        addControl(formView);
        addControl(form);
    }

    /**
     * evento de inicio de la simulación, inicia con el copiado del proyecto y
     * despues te manda a la pantalla de simulación
     *
     * @return
     */
    public boolean okWhatIfClicked() {
        if (form.isValid()) {

            if (nameSimulation.getValue().equals("")) {
                return false;
            }
            if (onceClicked) {
                try {
                    Regcuenta nuevoEjercicio = copiarProyecto();
                    DAO.refresh(nuevoEjercicio);
                    ModelExecutor executor = new ModelExecutor(nuevoEjercicio, true);
                    Map<String, Cuenta> start = executor.start();
                    addSessionVar("prySim", nuevoEjercicio);
                    DAO.saveRecordt(user, "Genera una simulación llamada " + nameSimulation);
                    System.out.println("termina de calcular y el icap es " + start.get("1").getValor());
                    setRedirect(SimulacioninicialPage.class);
                    return true;
                } catch (Exception ex) {
                    message = "Ocurrio algun error " + ex;
                    Logger.getLogger(WhatifPage.class.getName()).log(Level.INFO, null, ex);
                    return false;
                }
            }
        }
        message = "Favor de completar los campos";
        return false;
    }

    /**
     * evento que inicia la simulacion , este evento no copia el proyecto
     *
     * @return
     */
    public boolean okViewClicked() {
        if (formView.isValid()) {

            Regcuenta regCta = null;
            List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
            for (Regcuenta rc : createQuery) {
                if (Integer.parseInt(selectView.getValue()) == rc.getIdRegCuenta()) {
                    regCta = rc;
                }
            }
            addSessionVar("prySim", regCta);
            DAO.saveRecordt(user, "Acceso a editar simulación sobre " + regCta.getDesRegCuenta());
            setRedirect(SimulacioninicialPage.class);
            return true;
        }
        message = "Favor de completar los campos";
        return false;
    }

    /**
     * proceso para copiar un ejercicio, solo copia valores iniciales, no copia
     * nada de las cuentas calculadas
     */
    private Regcuenta copiarProyecto() {
        try {
            Regcuenta regCuenta = new Regcuenta();
            regCuenta.setDesRegCuenta(nameSimulation.getValue());
            Regcuenta base = null;
            List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
            for (Regcuenta rc : createQuery) {
                if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                    base = (Regcuenta) rc.clone();
                }
            }
            regCuenta.setFecha(base.getFecha());
            DAO.save(regCuenta);
            copiarCuentas(regCuenta);
            Regcuenta selected = null;
            for (Regcuenta rc : createQuery) {
                if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                    selected = (Regcuenta) rc.clone();
                }
            }
            Regcuentauser rUser = new Regcuentauser(regCuenta, user);
            DAO.save(rUser);
            return regCuenta;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(WhatifPage.class.getName()).log(Level.FINE, null, ex);
        }
        return null;
    }

    /**
     * copia cada una de las cuentas iniciales del proyecto
     *
     * @param regNuevo
     * @throws CloneNotSupportedException
     */
    private void copiarCuentas(Regcuenta regNuevo) throws CloneNotSupportedException {
        Regcuenta regCta = null;
        List<Regcuenta> createQuery = DAO.getEjerciciosCalculados();
        for (Regcuenta rc : createQuery) {
            if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                regCta = (Regcuenta) rc.clone();
            }
        }
        //prestamos
        Set<Prestamo> prestamos = regCta.getPrestamos();
        //captacion
        Set<Captacion> captacions = regCta.getCaptacions();
        //catalogo
        Set<Catalogominimo> catMin = regCta.getCatalogominimos();
        Consistencia consistenciaAnterior = (Consistencia) regCta.getConsistencias().iterator().next();
        //disponibilidades
        Set<Disponibilidad> disponibilidades = regCta.getDisponibilidads();
        //ingresos
        Set<Ingresosnetos> ingresos = regCta.getIngresosnetoses();
        //reservas
        Set<Reservas> reservas = regCta.getReservases();
        //tarjeta
        Set<Tarjetacredito> tarjeta = regCta.getTarjetacreditos();
        //tenencia
        Set<Valores> valores = regCta.getValoreses();
        List<Object> items = new LinkedList<Object>();
        Consistencia cons = new Consistencia(regNuevo, consistenciaAnterior.getDisponibilidadesLeidos(),
                consistenciaAnterior.getTenenciaLeidos(), consistenciaAnterior.getCaptacionLeidos(), consistenciaAnterior.getTarjetaCreditoLeidos(), consistenciaAnterior.getPrestamosLeidos(), consistenciaAnterior.getCatalogoMinimoLeidos(), consistenciaAnterior.getReservasLeidos(), consistenciaAnterior.getIngresosLeidos());
        items.add(cons);
        for (Captacion c : captacions) {
            Captacion nueva = new Captacion(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getIdCuentaCaptacion(), c.getMonto(), c.getFechaVencimiento());
            items.add(nueva);
        }
        for (Catalogominimo c : catMin) {
            Catalogominimo nueva = new Catalogominimo(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getValor(), c.getMoneda());
            items.add(nueva);
        }
        for (Disponibilidad c : disponibilidades) {
            Disponibilidad nueva = new Disponibilidad(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getMonto(), c.getFechaVencimiento());
            items.add(nueva);
        }
        for (Ingresosnetos c : ingresos) {
            Ingresosnetos nueva = new Ingresosnetos(regNuevo, c.getFecha(), c.getNumeroMes(), c.getIngresoNeto(), c.getReqMerCred());
            items.add(nueva);
        }
        for (Reservas c : reservas) {
            Reservas nueva = new Reservas(regNuevo, c.getFecha(), c.getEstatusCrediticio(), c.getMonto());
            items.add(nueva);
        }
        for (Tarjetacredito c : tarjeta) {
            Tarjetacredito nueva = new Tarjetacredito(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getIdCredito(), c.getSaldoInsoluto(), c.getFechaCorte(), c.getTipoTarjeta(), c.getRelevante());
            items.add(nueva);
        }
        for (Valores c : valores) {
            Valores nueva = new Valores(regNuevo, c.getFecha(), c.getIdCuentaContable(), c.getDescripcion(), c.getNumeroTitulos(), c.getTipoValor(), c.getEmision(), c.getSerie(), c.getFechaProximoCupon(), c.getGrupoRc10(), c.getPrecio(), c.getSobretasa(), c.getCalificacion(), c.getGrupoRc07(), c.getPonderador(), c.getPlazo(), c.getFechaVencimiento(), c.getMoneda(), c.getGradoRiesgo());
            nueva.setMapeado(c.getMapeado());
            items.add(nueva);
        }
        for (Prestamo c : prestamos) {
            Prestamo nueva = new Prestamo(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getIdCuentaPrestamo(), c.getSaldo(), c.getFechaDeCorte(), c.getTipoPrestamo(), c.getRelevante());
            items.add(nueva);
        }
        DAO.saveMultiple(items);
    }

    @Override
    public Integer getPermisoNumber() {
        return 13;
    }

}
