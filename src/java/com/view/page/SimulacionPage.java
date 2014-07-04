package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import db.pojos.User;
import interpreter.MathInterpreterException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import model.executor.ModelExecutor;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;

/**
 *
 * @author Admin
 */
public class SimulacionPage extends BorderPage {

    FormTable table;
    Form form;
    @Resource(name = "data")
    List<Cuenta> data;

    /**
     * constructor
     */
    public SimulacionPage() {
        super();
        fillData();
    }

    @Override
    public void init() {
        data =(List<Cuenta>)getSessionVar("dataSim");
        form = new Form("form");
        table = new FormTable("table", form);
        table.setName("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        String subtit = data.size() > 1 ? data.get(0).getEjercicio().equals(data.get(1).getEjercicio()) ? data.get(0).getEjercicio() : "Datos" : data.get(0).getEjercicio();
//ponemos los links de los montos para hacerlo recursivo
        for (int t = 0; t < data.size(); t++) {
            ActionLink actionLink = new ActionLink("link" + data.get(t).getIdCuenta(), data.get(t).getResultado(), this, "onLinkClick");
            actionLink.setValue(data.get(t).getIdCuenta().toString());
            data.get(t).setActionLink(actionLink);
            addControl(actionLink);
        }
//ponemos los links de Editar
        for (int t = 0; t < data.size(); t++) {
            ActionLink actionLink = new ActionLink("editlink" + data.get(t).getIdCuenta(), data.get(t).getEditStatus(), this, "onEditClick");
            actionLink.setValue(data.get(t).getIdCuenta().toString());
            data.get(t).setEditLink(actionLink);
            addControl(actionLink);
        }
//ponemos las primeras columnas
        String[] columnsTmp = Cuenta.getSimulationColumns();
        String[] columns = null;
        if (subtit.equals("Datos")) {
            columns = columnsTmp;
        } else {
            String[] subArray = new String[columnsTmp.length - 1];
            for (int t = 0; t < subArray.length; t++) {
                subArray[t] = columnsTmp[t + 1];
            }
            columns = subArray;
        }

        for (String c : columns) {
            Column col = new Column(c);
            col.setWidth("900 px");
            //col.setWidth(String.valueOf(100 / columns.length) + "%");
            if (c.substring(c.length() - 1, c.length()).equals("*")) {
                col.setName(c.substring(0, c.length() - 1));
                col.setDecorator(new Decorator() {
                    @Override
                    public String render(Object object, Context context) {
                        Cuenta c = (Cuenta) object;
                        return c.getActionLink().toString();
                    }
                });

            }
            if (c.substring(c.length() - 1, c.length()).equals("?")) {
                col.setWidth("150px");
                col.setName(c.substring(0, c.length() - 1));
                col.setDecorator(new Decorator() {
                    @Override
                    public String render(Object object, Context context) {
                        Cuenta c = (Cuenta) object;
                        return c.getEditLink().toString();
                    }
                });

            }
            table.addColumn(col);
        }
        form.add(new Submit("startSimulation", "Simular", this, "simularClicked"));
        //pegamos todo en nuestro fieldset
        FieldSet fs = new FieldSet(subtit);
        form.add(fs);
        fs.add(table);
        //System.out.println("la cantidad de links " + links.length);
        addControl(form);
    }

    /**
     * llena la tabla de informacion;
     */
    private void fillData() {
        table.setRowList(data);
    }

    public boolean simularClicked() throws IOException {
        try {
            Regcuenta regcuenta = data.get(0).getRegcuenta();
            User user = (User) getSessionVar("user");
            ModelExecutor modelExecutor = new ModelExecutor( regcuenta, true);
            modelExecutor.start();
            DAO.saveRecordt(user,user.getUser()+" generó simulación de "+regcuenta.getDesRegCuenta());
            cambiarPantalla(data.get(0).getRegcuenta());
            return true;
        } catch (MathInterpreterException ex) {
            Logger.getLogger(SimulacionPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * evento que nos ayuda a checar si se da click en alguno de los valores
     *
     * @return
     */
    public boolean onLinkClick() {
        for (int t = 0; t < data.size(); t++) {
            if (data.get(t).getActionLink().isClicked()) {
                Cuenta ref = data.get(t);
                List<Cuenta> newData = new LinkedList<Cuenta>();
                List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
                if (ref.getRef() == null || ref.getRef().equals("")) {
                    newData.add(ref);
                } else {
                    String[] split = ref.getRef().split(",");
                    for (String sp : split) {
                        for (Cuenta c : createQuery) {
                            if (c.getIdCuenta().toString().equals(sp)) {
                                newData.add(c);
                            }
                        }
                    }
                }
                setRedirect(SimulacionPage.class);
                return true;
            }
        }
        return true;
    }

    /**
     * evento que nos ayuda a checar si se da click en alguno de los valores
     *
     * @return
     */
    public boolean onEditClick() {
        for (int t = 0; t < data.size(); t++) {
            if (data.get(t).getEditLink().isClicked()) {
                setRedirect(EditsimulationPage.class);
                return true;
            }
        }
        return true;
    }

    private void cambiarPantalla(Regcuenta regCuenta) {
        List<Cuenta> data = new LinkedList<Cuenta>();
        List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
        for (Cuenta c : createQuery) {
            if (c.getCatalogocuenta().getIdCatalogoCuenta() == 1 && c.getRegcuenta().getIdRegCuenta() == regCuenta.getIdRegCuenta()) {
                data.add(c);
                break;
            }
        }
        addSessionVar("dataSimulacion", data);
        addSessionVar("titleSimulacion","");
        setRedirect(SimulacionPage.class);
    }
}
