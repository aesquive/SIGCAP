package com.view.page;

import db.pojos.Catalogominimo;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.FormTable;

/**
 *
 * @author desarrollo
 */
public class SimulacioncuentasPage extends SimulacionPage {

    Form form;
    Regcuenta regCta;
    List<Catalogominimo> cuentas;
    TextField fieldSearch;
    Table resultadoBusqueda;
    
    @Override
    public void initSimulacionComponents() {
        form = new Form("formSim");
        //creamos el panel de edicion de cuentas
        createFSEditVar();

        addControl(form);
    }

    /**
     * crea el formulario correspondiente a la edicion de variables de cuentas
     *
     * @return
     */
    private FieldSet createFSEditVar() {
        regCta = (Regcuenta) getSessionVar("prySim");
        //fieldset de busqueda de variable
        FieldSet fset = new FieldSet("Simulador de Cuentas", "Simulador de Cuentas Catalogo Mínimo");
        fieldSearch = new TextField("search", "Nombre variable:");
        fieldSearch.setValue(getSessionVar("busquedaSim") != null ? (String) getSessionVar("busquedaSim") : "");
        fset.add(fieldSearch);
        Submit buscar = new Submit("sub", "Buscar", this, "editVar_buscarVariable");
        buscar.setAttribute("onclick", "waitPage();");
        fset.add(buscar);
        form.add(fset);
        resultadoBusqueda = editVar_filtrarCuentas(fieldSearch.getValue());
        form.add(resultadoBusqueda);
        return fset;
    }

    /**
     * filtra las cuentas que contengan el valor value dentro de su nombre o el
     * numero de cuenta
     *
     * @param value
     * @param form
     * @return
     */
    public Table editVar_filtrarCuentas(String value) {
        if (!value.equals("")) {
            cuentas = new LinkedList<Catalogominimo>();
            cuentas = editVar_buscarCuentas(value);
            if (cuentas != null && !cuentas.isEmpty()) {
                Table generarCuadroCuentas = editVar_generarCuadroCuentas();
                return generarCuadroCuentas;
            }
        }
        return new FormTable("Cuentas");
    }

    /**
     * agrega el valor de la variable a la sesion para buscarlo en la siguiente
     * carga de la misma pagina
     *
     * @param value
     * @return
     */
    public boolean editVar_buscarVariable() {
        addSessionVar("busquedaSim",fieldSearch.getValue());
        setRedirect(SimulacioncuentasPage.class);
        return true;
    }

    /**
     * busca las cuentas que contengan el valor del parametro dentro del nombre
     * o el numero de cuenta
     *
     * @param value
     * @return
     */
    private List<Catalogominimo> editVar_buscarCuentas(String value) {
        Set<Catalogominimo> cuentasSet = regCta.getCatalogominimos();
        List<Catalogominimo> resultado = new LinkedList<Catalogominimo>();
        for (Catalogominimo cuenta : cuentasSet) {
                String datosCuenta = cuenta.getCatalogocuenta().getDesCatalogoCuenta() + cuenta.getCatalogocuenta().getIdCatalogoCuenta().toString();
                String datosCuentaMayus = datosCuenta.trim().toUpperCase();
                String[] argumentos = value.split(" ");
                boolean paso = true;
                for (String argumento : argumentos) {
                    if (paso && !datosCuentaMayus.contains(argumento.toUpperCase())) {
                        paso = false;
                    }
                }
                if (paso) {
                    resultado.add(cuenta);
                }
            
        }
        return resultado;
    }

    /**
     * cuadro de cuentas para cuando se editan variables
     *
     * @param cuentas
     * @param form
     * @return
     */
    private Table editVar_generarCuadroCuentas() {
        Table table = new Table("table");
        table.setName("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        for (Catalogominimo c:cuentas) {
            ActionLink actionLink = new ActionLink("link" + c.getIdCatalogoMinimo().toString(), "Editar", this, "clickAction");
            actionLink.setValue(c.getIdCatalogoMinimo().toString());
            c.setActionLink(actionLink);
            addControl(actionLink);
        }
        String[] columns = new String[]{"Cuenta", "Detalle", "Resultado", "Accion?"};
        for (String c : columns) {
            Column col = new Column(c);
            col.setWidth("900 px");
            if (c.substring(c.length() - 1, c.length()).equals("?")) {
                col.setName(c.substring(0, c.length() - 1));
                col.setDecorator(new Decorator() {
                    @Override
                    public String render(Object object, Context context) {
                        Catalogominimo c = (Catalogominimo) object;
                        return c.getActionLink().toString();
                    }
                });
            }

            table.addColumn(col);
        }
        table.setRowList(cuentas);
        return table;
    }

    /**
     * evento que nos ayuda a checar si se da click en alguno de los valores
     *
     * @return
     */
    public boolean clickAction() {
        System.out.println("las cuentas son "+cuentas.size());
        for (Catalogominimo c:cuentas) {
            if (c.getActionLink().isClicked()) {
                addSessionVar("variableEditSim", c);
                setRedirect(EditarsimPage.class);
            }
        }
        return false;
    }

}
