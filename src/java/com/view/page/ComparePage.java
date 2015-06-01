package com.view.page;

import db.pojos.Catalogocuenta;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import java.util.LinkedList;
import java.util.List;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.CuentaWrapper;

/**
 * Clase que se encarga de poner detalles de cuentas pasados los datos deben de
 * estar en el SessionController con el nombre "data"
 *
 * @author zorin
 */
public class ComparePage extends BorderPage {

    FormTable table;
    Form form;
    List<CuentaWrapper> cuentas;
    private Regcuenta regCuentaUno;
    private Regcuenta regCuentaDos;
    private Catalogocuenta catalogoCuenta;
    private ActionLink downloadReport;

    /**
     * constructor
     */
    public ComparePage() {
        super();
        fillData();
    }

    @Override
    public void init() {
        form = new Form("form");
        Submit sub = new Submit("descargar", "Descargar Reporte", this, "makeReport");
        sub.setAttribute("onclick", "createComparativo()");
        form.add(sub);
        table = new FormTable("table", form);
        table.setName("dataTable");
        table.setPageNumber(0);
        table.setClass(Table.CLASS_ORANGE2);
        Integer counter = (Integer) getContext().getSessionAttribute("counterCompare");
        String[] compareCount = (String[]) getContext().getSessionAttribute("compareC-" + counter);
        regCuentaUno = (Regcuenta) getContext().getSessionAttribute("ex1-" + counter);
        regCuentaDos = (Regcuenta) getContext().getSessionAttribute("ex2-" + counter);
        obtenerWrappers(compareCount);
        checkBackForward(counter, (Integer) getContext().getSessionAttribute("maxCounterCompare"));
        for (int t = 0; t < cuentas.size(); t++) {
            CuentaWrapper wrapper = cuentas.get(t);
            ActionLink actionLinkUno = new ActionLink("link" + wrapper.getPrimerCuenta().getIdCuenta().toString(), wrapper.getPrimerCuenta().getResultado(), this, "onLinkClick");
            ActionLink actionLinkDos = new ActionLink("link" + wrapper.getSegundaCuenta().getIdCuenta().toString(), wrapper.getSegundaCuenta().getResultado(), this, "onLinkClick");
            actionLinkUno.setValue(wrapper.getPrimerCuenta().getRef());
            actionLinkDos.setValue(wrapper.getSegundaCuenta().getRef());
            addControl(actionLinkUno);
            addControl(actionLinkDos);
            wrapper.setActionLinkUno(actionLinkUno);
            wrapper.setActionLinkDos(actionLinkDos);
        }
//ponemos las primeras columnas

        Column col1 = new Column("cuenta", "Cuenta");
        Column col2 = new Column("primerValor", regCuentaUno.getDesRegCuenta());
        Column col3 = new Column("segundoValor", regCuentaDos.getDesRegCuenta());

        form.add(new HiddenField("hiddenPrimer", regCuentaUno.getIdRegCuenta()));
        form.add(new HiddenField("hiddenSegundo", regCuentaDos.getIdRegCuenta()));
        form.add(new HiddenField("hiddenVar", ((Double) getContext().getSessionAttribute("minVariance")) / 100));

        col1.setWidth("900 px");
        col2.setWidth("900 px");
        col3.setWidth("900 px");

        col2.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                CuentaWrapper c = (CuentaWrapper) object;
                return c.getActionLinkUno().toString();
            }
        });

        col3.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                CuentaWrapper c = (CuentaWrapper) object;
                return c.getActionLinkDos().toString();
            }
        });

        table.addColumn(col1);
        table.addColumn(col2);
        table.addColumn(col3);

        //pegamos todo en nuestro fieldset
        FieldSet fs = new FieldSet("Comparativo");
        form.add(fs);
        fs.add(table);
        //System.out.println("la cantidad de links " + links.length);
        addControl(form);
    }

    public boolean onLinkClick() {
        System.out.println("entra al link click");
        for (CuentaWrapper c : cuentas) {
            if (c.getActionLinkUno().isClicked() || c.getActionLinkDos().isClicked()) {
                String value = c.getActionLinkUno().getValue();
                if (value == null) {
                    message = "No existen mas registros";
                } else {
                    List<String> l = new LinkedList<String>();
                    String[] split = value.split(",");
                    for (String idCount : split) {
                        if (!idCount.equals("")) {
                            Cuenta cuentaById = regCuentaUno.getCuentaByCatalogoId(idCount, regCuentaUno.getCuentas());
                            if (cuentaById != null) {
                                l.add(cuentaById.getCatalogocuenta().getIdCatalogoCuenta().toString());
                            }
                        }
                    }
                    String[] array = new String[l.size()];
                    String[] toArray = l.toArray(array);
                    System.out.println("llega aca");
                    Integer counter = (Integer) getContext().getSessionAttribute("counterCompare");
                    int newcounter = counter + 1;
                    Integer maxCounter = (Integer) getContext().getSessionAttribute("maxCounterCompare");
                    int newMaxCounter = newcounter > maxCounter ? newcounter : maxCounter;
                    getContext().setSessionAttribute("maxCounterCompare", newMaxCounter);
                    getContext().setSessionAttribute("counterCompare", newcounter);
                    getContext().setSessionAttribute("compareC-" + newcounter, toArray);
                    getContext().setSessionAttribute("ex1-" + newcounter, regCuentaUno);
                    getContext().setSessionAttribute("ex2-" + newcounter, regCuentaDos);
                    setRedirect(ComparePage.class);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * llena la tabla de informacion;
     */
    private void fillData() {
        table.setRowList(cuentas);
    }

    private void obtenerWrappers(String[] compareCount) {
        cuentas = new LinkedList<CuentaWrapper>();
        for (String cta : compareCount) {
            Cuenta cuenta = regCuentaUno.getCuenta(cta, regCuentaUno.getCuentas());
            Cuenta cuenta2 = regCuentaDos.getCuenta(cta, regCuentaDos.getCuentas());
            if (regCuentaUno != null && regCuentaDos != null) {
                CuentaWrapper wrapper = new CuentaWrapper(catalogoCuenta, cuenta, cuenta2);
                cuentas.add(wrapper);
            }
        }

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
        Integer counter = (Integer) getContext().getSessionAttribute("counterCompare");
        getContext().setSessionAttribute("counterCompare", counter + 1);
        setRedirect(ComparePage.class);
        return true;
    }

    public boolean counterBack() {
        Integer counter = (Integer) getContext().getSessionAttribute("counterCompare");
        getContext().setSessionAttribute("counterCompare", counter - 1);
        setRedirect(ComparePage.class);
        return true;
    }

    public boolean makeReport() {

//            BrowserLauncher browser = new BrowserLauncher();
//            browser.setNewWindowPolicy(true);
//            browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=2&pra=" + regCuentaUno.getIdRegCuenta() + "&prb=" + regCuentaDos.getIdRegCuenta() + "&var=" + ((Double) getContext().getSessionAttribute("minVariance")) / 100 + "&num=" + ((Integer) getContext().getSessionAttribute("numRegs")));
        return true;
    }
}
