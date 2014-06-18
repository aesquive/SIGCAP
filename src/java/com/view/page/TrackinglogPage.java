package com.view.page;

import edu.stanford.ejalbert.BrowserLauncher;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.DateField;

/**
 *
 * @author Admin
 */
public class TrackinglogPage extends BorderPage {

    private Form form;
    private DateField initDate;
    private DateField endDate;

    @Override
    public void init() {
        form = new Form("form");
        initDate = new DateField("init", "Fecha Inicial", true);
        endDate = new DateField("end", "Fecha Final", true);
        form.add(initDate);
        form.add(endDate);
        form.add(new Submit("submit", "Generar Reporte", this, "generateTracking"));
        addControl(form);
    }

    public boolean generateTracking() {
        if (form.isValid()) {
            try {
                if (initDate.getDate().compareTo(endDate.getDate()) > 0) {
                    message = "La Fecha Inicial debe ser menor a la Fecha Final";
                    return false;
                }
                SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
                BrowserLauncher browser = new BrowserLauncher();
                browser.setNewWindowPolicy(true);
                browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=1&ini=" + format.format(initDate.getDate()) + "&end=" + format.format(endDate.getDate()));
                return true;
            } catch (Exception ex) {
                Logger.getLogger(TrackinglogPage.class.getName()).log(Level.INFO, null, ex);
                message="Algun error sucedio";
            }
        }
        return false;
    }
}
