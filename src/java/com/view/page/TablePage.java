package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.Util;

/**
 * Clase que se encarga de poner detalles de cuentas pasados los datos deben de
 * estar en el SessionController con el nombre "data"
 *
 * @author zorin
 */
public class TablePage extends BorderPage {

    FormTable table;
    Form form;
    @Resource(name = "data")
    List<Cuenta> data;
    private static int numPer = 2;
    String titlePage;
    private Integer counter;
    private String regCta;

    /**
     * constructor
     */
    public TablePage() {
        super();
        fillData();
    }

    @Override
    public void init() {
        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) && dte.get(numPer) == true) {
            System.out.println("Pagina valida");
            setRedirect(NocontratadoPage.class);
            return;
        }
        form = new Form("form");
        counter = (Integer) getSessionVar("icapCounter");
        checkBackForward(counter,(Integer) getSessionVar("icapMaxCounter"));
        data = (List<Cuenta>) getSessionVar("icapData-" + counter);
        regCta = (String) getSessionVar("regCta-" + counter);
        titlePage = (String) getSessionVar("title-" + counter);
        table = new FormTable("table", form);
        table.setName("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        title = titlePage;
        for (int t = 0; t < data.size(); t++) {
            ActionLink actionLink = new ActionLink("link" + data.get(t).getIdCuenta(), data.get(t).getResultado(), this, "onLinkClick");
            actionLink.setValue(data.get(t).getIdCuenta().toString());
            data.get(t).setActionLink(actionLink);
            addControl(actionLink);
        }
        //ponemos las primeras columnas
        String[] columns = Cuenta.getColumns();
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

            table.addColumn(col);
        }
        //pegamos todo en nuestro fieldset
        FieldSet fs = new FieldSet(regCta);
        form.add(fs);
        fs.add(table);
        addControl(form);
    }

    /**
     * llena la tabla de informacion;
     */
    private void fillData() {
        table.setRowList(data);
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

                addSessionVar("icapCounter", ((Integer) getSessionVar("icapCounter")) + 1);
                int newCounter = (Integer) getSessionVar("icapCounter");
                int maxCounter = (Integer) getSessionVar("icapMaxCounter");
                int newMax = maxCounter > newCounter ? maxCounter : newCounter;
                addSessionVar("icapMaxCounter", newMax);
                addSessionVar("icapData-" + newCounter, newData);
                addSessionVar("title-" + newCounter, ref.getCatalogocuenta().getDesCatalogoCuenta());
                addSessionVar("regCta-" + newCounter, ref.getRegcuenta().getDesRegCuenta());
                setRedirect(TablePage.class);
                return true;
            }
        }
        return true;
    }

    private void checkBackForward(Integer counter, Integer maxCounter) {
        if (counter < maxCounter) {
            goForward = new ActionLink("forwardLink", "", this, "counterForward");
            goForward.setName("forwardLink");
            goForward.setId("forwardLink");
            goForward.setImageSrc("/img/forward.png");
            form.add(goForward);
        }
        if (counter > 0) {
            goBack = new ActionLink("backLink", "", this, "counterBack");
            goBack.setImageSrc("/img/back.png");
            goBack.setName("backLink");
            goBack.setId("backLink");
            form.add(goBack);
        }
    }
    
    
    public boolean counterForward() {
        Integer counter = (Integer) getContext().getSessionAttribute("icapCounter");
        getContext().setSessionAttribute("icapCounter", counter + 1);
        setRedirect(ComparePage.class);
        return true;
    }

    public boolean counterBack() {
        Integer counter = (Integer) getContext().getSessionAttribute("icapCounter");
        getContext().setSessionAttribute("icapCounter", counter - 1);
        setRedirect(ComparePage.class);
        return true;
    }


}
