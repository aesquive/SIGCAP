package com.view.page;

import db.pojos.Regcuenta;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;

/**
 *
 * @author Admin
 */
public  class SimulacionPage extends BorderPage {

    private Form form;
    private Regcuenta regCta;
    private Menu simMenu;
    
    
    public void init() {
        setForm(new Form("form"));
        setRegCta((Regcuenta) getSessionVar("prySim"));
        setSimMenu(new Menu("rootMenuSim"));
        getSimMenu().add(createMenu("Simulador Tenencia", "simulaciontenencia.htm"));
        getSimMenu().add(createMenu("Simulador Captación", "simulacioncaptacion.htm"));
        getSimMenu().add(createMenu("Simulador Disponibilidades", "simulaciondisponibilidad.htm"));
        getSimMenu().add(createMenu("Simulador Prestamos", "simulacionprestamo.htm"));
        getSimMenu().add(createMenu("Simulador Tarjeta", "simulaciontarjeta.htm"));
        form.add(new Label("txt", "Ejercicio :"+regCta.getDesRegCuenta()));
        getForm().add(getSimMenu());
        addControl(getForm());
        
    }

    private Menu createMenu(String label, String path) {
        Menu menu = new Menu();
        menu.setAccessController(new RoleAccessController());
        menu.setLabel(label);
        menu.setPath(path);
        menu.setTitle(label);
        return menu;
    }

    /**
     * @return the form
     */
    public Form getForm() {
        return form;
    }

    /**
     * @param form the form to set
     */
    public void setForm(Form form) {
        this.form = form;
    }

    /**
     * @return the regCta
     */
    public Regcuenta getRegCta() {
        return regCta;
    }

    /**
     * @param regCta the regCta to set
     */
    public void setRegCta(Regcuenta regCta) {
        this.regCta = regCta;
    }

    /**
     * @return the simMenu
     */
    public Menu getSimMenu() {
        return simMenu;
    }

    /**
     * @param simMenu the simMenu to set
     */
    public void setSimMenu(Menu simMenu) {
        this.simMenu = simMenu;
    }

}
