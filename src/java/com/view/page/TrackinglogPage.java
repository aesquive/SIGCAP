package com.view.page;

import java.util.logging.Level;
import java.util.logging.Logger;
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
        title="Auditor - Tracking Log";
        form = new Form("form");
        initDate = new DateField("init", "Fecha Inicial", true);
        initDate.setFormatPattern("dd/MM/yyyy");
        endDate = new DateField("end", "Fecha Final", true);
        endDate.setFormatPattern("dd/MM/yyyy");
        form.add(initDate);
        form.add(endDate);
        Submit sub=new Submit("submit", "Generar Reporte", this, "generateTracking");
        sub.setAttribute("onclick", "trackingLog();");
        form.add(sub);
        addControl(form);
    }

    public boolean generateTracking() {
        if (form.isValid()) {
            try {
                if (initDate.getDate().compareTo(endDate.getDate()) > 0) {
                    message = "La Fecha Inicial debe ser menor a la Fecha Final";
                    return false;
                }
//                SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
//                BrowserLauncher browser = new BrowserLauncher();
//                browser.setNewWindowPolicy(true);
//                browser.openURLinBrowser(Configuration.getValue("direccionReportes") + "?typ=1&ini=" + format.format(initDate.getDate()) + "&end=" + format.format(endDate.getDate()));
                return true;
            } catch (Exception ex) {
                Logger.getLogger(TrackinglogPage.class.getName()).log(Level.INFO, null, ex);
                message="Algun error sucedio";
            }
        }
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 9;
    }
}
