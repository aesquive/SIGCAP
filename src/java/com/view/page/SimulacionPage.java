package com.view.page;

import db.pojos.Regcuenta;
import org.apache.click.control.Label;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;

/**
 *
 * @author Admin
 */
public abstract class SimulacionPage extends BorderPage {

    
    Menu menuSim;
    public Regcuenta regCuenta;
    public String ejercicio;
    public String icapActual;
    
    @Override
    public void init() {
        title="Simulación de Capital";
        regCuenta=(Regcuenta) getSessionVar("prySim");
        ejercicio=regCuenta.getDesRegCuenta();
        icapActual=regCuenta.getCuenta("1",regCuenta.getCuentas()).getValor().toString();
        initMenu();
        initSimulacionComponents();
    }
    

    public abstract void initSimulacionComponents();

    private void initMenu() {
        menuSim = new Menu("menuSim");

        menuSim.add(createMenu("Simulación de Cuentas", "simulacioncuentas.htm"));
        menuSim.add(createMenu("Simulación de Tenencia", "simulaciontenencia.htm"));
        addControl(menuSim);
    }

    private Menu createMenu(String label, String path) {
        Menu menu = new Menu();
        menu.setAccessController(new RoleAccessController());
        menu.setLabel(label);
        menu.setPath(path);
        menu.setTitle(label);
        return menu;
    }

}
