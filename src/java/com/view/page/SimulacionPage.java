package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;

/**
 *
 * @author Admin
 */
public abstract class SimulacionPage extends BorderPage {

    
    Menu menuSim;
    public Regcuenta regCuenta;
    //dice si quiere que se calcule cada ocasion que se modifique una variable
    public String ejercicio;
    public String icapActual;
    
    @Override
    public void init() {
        title="Simulación de Capital";
        regCuenta=(Regcuenta) getSessionVar("prySim");
        ejercicio=regCuenta.getDesRegCuenta();
        DAO.refresh(regCuenta);
        icapActual=regCuenta.getCuenta("1",regCuenta.getCuentas()).getResultado();
        initMenu();
        initSimulacionComponents();
    }
    

    public abstract void initSimulacionComponents();

    private void initMenu() {
        menuSim = new Menu("menuSim");

        //menuSim.add(createMenu("Simulador de Cuentas Catalogo Mínimo", "simulacioncuentas.htm"));
        menuSim.add(createMenu("Tenencia", "simulaciontenencia.htm"));
        menuSim.add(createMenu("Disponbilidades", "simulaciondisponibilidad.htm"));
        menuSim.add(createMenu("Captación", "simulacioncaptacion.htm"));
        menuSim.add(createMenu("Ingresos Netos", "simulacioningresos.htm"));
        menuSim.add(createMenu("Prestamos", "simulacionprestamo.htm"));
        menuSim.add(createMenu("Tarjeta de Crédito", "simulaciontarjetacredito.htm"));
        
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
