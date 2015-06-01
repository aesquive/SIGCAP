package com.view.page;

import org.apache.click.control.Label;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;

/**
 *
 * @author Admin
 */
public abstract class SimulacionPage extends BorderPage {

    Menu menuSim;
    Label name;
    
    @Override
    public void init() {
        name=new Label("name", "Simulador");
        addControl(name);
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
