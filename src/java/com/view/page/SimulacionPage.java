package com.view.page;

import db.controller.DAO;
import db.pojos.Captacion;
import db.pojos.Disponibilidad;
import db.pojos.Prestamo;
import db.pojos.Regcuenta;
import db.pojos.Tarjetacredito;
import db.pojos.Valores;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;

/**
 *
 * @author Admin
 */
public abstract class SimulacionPage extends BorderPage {

    public Form formSimMenu;
    //Menu menuSim;
    public Regcuenta regCuenta;
    //dice si quiere que se calcule cada ocasion que se modifique una variable
    public String ejercicio;
    public String icapActual;
    
    public Submit submitTenencia;
    public Submit submitDisponibilidad;
    public Submit submitCaptacion;
    public Submit submitPrestamo;
    public Submit submitTarjeta;
    
    @Override
    public void init() {
        formSimMenu=new Form("formSimMenu");
        title="Simulación de Capital";
        regCuenta=(Regcuenta) getSessionVar("prySim");
        ejercicio=regCuenta.getDesRegCuenta();
        DAO.refresh(regCuenta);
        icapActual=regCuenta.getCuenta("1",regCuenta.getCuentas()).getResultado();
        initMenu();
        initSimulacionComponents();
        addControl(formSimMenu);
    }
    

    public abstract void initSimulacionComponents();

    /**
     * crea los botones de todas las simulaciones posibles
     */
    private void initMenu() {
        //menuSim = new Menu("menuSim");
        submitTenencia=new Submit("submitTenencia","Tenencia", this, "clickTenencia");
        submitDisponibilidad=new Submit("submitDisponibilidad","Disponibilidades", this, "clickDisponibilidad");
        submitCaptacion=new Submit("submitCaptacion", "Captación", this, "clickCaptacion");
        submitPrestamo=new Submit("submitPrestamo", "Prestamos",this, "clickPrestamo");
        submitTarjeta=new Submit("submitTarjeta","Tarjeta de Crédito",this , "clickTarjeta");
        formSimMenu.add(submitTenencia);
        formSimMenu.add(submitDisponibilidad);
        formSimMenu.add(submitCaptacion);
        formSimMenu.add(submitPrestamo);
        formSimMenu.add(submitTarjeta);
        
    }
    
     /**
     * valores que se deben poner en la tabla de tenencia
     * @return 
     */
    public boolean clickTenencia(){
        DAO.refresh(regCuenta);
        addSessionVar("simValues", "getValoreses");
        addSessionVar("simColumns", Valores.getSimColumns());
        addSessionVar("simDesColumns",Valores.getSimDesColumns());
        addSessionVar("title", "Simulación Capital - Tenencia");
        addSessionVar("simMethodName","getIdTenencia");
        addSessionVar("simFSName", "Tenencia");
        removeSessionVar("orderSim");
        setRedirect(SimulaciondataPage.class);
        return true;
    }

     /**
     * valores que se deben poner en la tabla de disponibilidad
     * @return 
     */
    public boolean clickDisponibilidad(){
        DAO.refresh(regCuenta);
        addSessionVar("simValues", "getDisponibilidads");
        
        addSessionVar("simColumns", Disponibilidad.getSimColumns());
        addSessionVar("simDesColumns",Disponibilidad.getSimDesColumns());
        addSessionVar("title", "Simulación Capital - Disponibilidades");
        addSessionVar("simMethodName","getIdDisponibilidad");
        addSessionVar("simFSName", "Disponibilidades");
        removeSessionVar("orderSim");
        setRedirect(SimulaciondataPage.class);
        return true;
    }
    
     /**
     * valores que se deben poner en la tabla de captacion
     * @return 
     */
    public boolean clickCaptacion(){
        DAO.refresh(regCuenta);
        addSessionVar("simValues", "getCaptacions");
        
        addSessionVar("simColumns", Captacion.getSimColumns());
        addSessionVar("simDesColumns",Captacion.getSimDesColumns());
        addSessionVar("title", "Simulación Capital - Captación");
        addSessionVar("simMethodName","getIdCaptacion");
        addSessionVar("simFSName", "Captación");
        removeSessionVar("orderSim");
        setRedirect(SimulaciondataPage.class);
        return true;
    }
    
     /**
     * valores que se deben poner en la tabla de prestamo
     * @return 
     */
    public boolean clickPrestamo(){
        DAO.refresh(regCuenta);
        addSessionVar("simValues", "getPrestamos");
        
        addSessionVar("simColumns", Prestamo.getSimColumns());
        addSessionVar("simDesColumns",Prestamo.getSimDesColumns());
        addSessionVar("title", "Simulación Capital - Prestamos");
        addSessionVar("simMethodName","getIdPrestamo");
        addSessionVar("simFSName", "Prestamo");
        removeSessionVar("orderSim");
        setRedirect(SimulaciondataPage.class);
        return true;
    }
    
    /**
     * valores que se deben poner en la tabla de tarjeta de credito
     * @return 
     */
    public boolean clickTarjeta(){
        DAO.refresh(regCuenta);
        
        addSessionVar("simValues", "getTarjetacreditos");
        
        addSessionVar("simColumns", Tarjetacredito.getSimColumns());
        addSessionVar("simDesColumns",Tarjetacredito.getSimDesColumns());
        addSessionVar("title", "Simulación Capital - Tarjeta de Crédito");
        addSessionVar("simMethodName","getIdTarjetaCredito");
        addSessionVar("simFSName", "Tarjeta de Crédito");
        removeSessionVar("orderSim");
        setRedirect(SimulaciondataPage.class);

        return true;
    }
    
}
