package com.view.page;

import db.pojos.User;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.session.SessionController;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.DateField;
import util.UserManager;

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
                if(initDate.getDate().compareTo(endDate.getDate())>0){
                    message="La Fecha Inicial debe ser menor a la Fecha Final";
                    return false;
                }
                Date dateIn = initDate.getDate();
                Date dateEn=endDate.getDate();
                Calendar cin=Calendar.getInstance();
                Calendar cen=Calendar.getInstance();
                cin.setTime(dateIn);
                cen.setTime(dateEn);
                cin.set(Calendar.HOUR, 0);
                cen.set(Calendar.HOUR, 0);
                SessionController controller = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext);
                User user = (User) controller.getVariable("user").getValue();
                String generateReport = trackinglog.TrackingLogReporter.generateReport("tracking-"+user.getIduser()+".xlsx",cin.getTime(), cen.getTime());
                setRedirect("/reportes/"+generateReport);
                return true;
            } catch (IOException ex) {
                Logger.getLogger(TrackinglogPage.class.getName()).log(Level.INFO, null, ex);
                message="Hubo algun error al procesar el Tracking Log";
                return false;
            }
        }
        return false;
    }
}
