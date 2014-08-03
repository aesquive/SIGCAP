package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import db.pojos.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.executor.ModelExecutor;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import util.Util;

/**
 *
 * @author Admin
 */
public class AdministradormodelosPage extends BorderPage {

    Form form;
    Select selectProject;
    User user;
    private static int numPer = 1;

    @Override
    public void init() {
        form = new Form("form");

        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) && dte.get(numPer) == true) {
            setRedirect(NocontratadoPage.class);
            return;
        }
        selectProject = new Select("selectProject", "Ejercicio", true);
        user = (User) getSessionVar("user");
        List<Regcuentauser> createQuery = DAO.createQuery(Regcuentauser.class, null);
        selectProject.setDefaultOption(new Option("Seleccione"));
        for (Regcuentauser ru : createQuery) {
            if (ru.getUser().getIduser() == user.getIduser()) {
                selectProject.add(new Option(ru.getRegcuenta(), ru.getRegcuenta().getDesRegCuenta()));
            }
        }
        form.add(selectProject);
        Submit process = new Submit("subModel", "Procesar Modelo", this, "processModel");
        javaScriptProcess(process);
        form.add(process);
        Submit delete = new Submit("subDelete", "Eliminar Modelo", this, "deleteModel");
        javaScriptProcess(delete);
        form.add(delete);
        addControl(form);
    }

    public boolean processModel() {
        if (form.isValid()) {
            try {
                Regcuenta regCta = null;
                List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
                for (Regcuenta rc : createQuery) {
                    if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                        regCta = rc;
                    }
                }

                List<Cuenta> qcta = DAO.createQuery(Cuenta.class, null);
                boolean update = false;
                for (Cuenta c : qcta) {
                    if (c.getCatalogocuenta().getIdCatalogoCuenta() == 1 && c.getRegcuenta().getIdRegCuenta() == regCta.getIdRegCuenta()) {
                        update = true;
                    }
                }
                ModelExecutor m = new ModelExecutor( regCta);
                m.start();
                DAO.saveRecordt(user, user.getUser() + " calculó el ICAP de " + regCta.getDesRegCuenta());
                setRedirect(IcapPage.class);
                return true;
            } catch (Exception ex) {
                message = "Imposible procesar el modelo";
                Logger.getLogger(AdministradormodelosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
        }
        return false;
    }

    public boolean deleteModel() {
        if (form.isValid()) {
            try {
                Regcuenta regCta = null;
                List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
                for (Regcuenta rc : createQuery) {
                    if (Integer.parseInt(selectProject.getValue()) == rc.getIdRegCuenta()) {
                        regCta = rc;
                    }
                }
                DAO.saveRecordt(user, user.getUser() + " eliminó la información del " + regCta.getDesRegCuenta());
                DAO.delete(regCta);
                setRedirect(IcapPage.class);
                return true;
            } catch (Exception ex) {
                message = "Imposible borrar el modelo";
                Logger.getLogger(AdministradormodelosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
        }
        return false;
    }
}
