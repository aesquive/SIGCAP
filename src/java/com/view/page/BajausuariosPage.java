package com.view.page;

import db.controller.DAO;
import db.pojos.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import manager.session.SessionController;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import util.UserManager;

/**
 *
 * @author Admin
 */
public class BajausuariosPage extends BorderPage {

    private Select select;
    private Form form;

    @Override
    public void init() {
        form = new Form("form");
        addControl(form);
        select = new Select("select", "Seleccionar Usuario", true);
        select.setDefaultOption(new Option(-1, "--Seleccionar--"));
        List<User> createQuery = DAO.createQuery(User.class, null);
        for (User u : createQuery) {
            select.add(new Option(u.getIduser(), u.getUser()));
        }
        form.add(select);
        form.add(new Submit("sub", "Eliminar Usuario", this, "eliminar"));
    }

    public boolean eliminar() {
        if (form.isValid()) {
            Map<String, String> map = new HashMap<String, String>();
            List<User> createQuery = DAO.createQuery(User.class, null);
            User user = null;
            for (User u : createQuery) {
                if (u.getIduser() == Integer.parseInt(select.getValue())) {
                    user = u;
                }
            }
            DAO.delete(user);
            SessionController controller = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext);
            User userSess = (User) controller.getVariable("user").getValue();
            DAO.saveRecordt(userSess, userSess.getUser() + " elimino al usuario " + user.getUser());
            setRedirect(ControlusuariosPage.class);
            return true;
        }
        return false;
    }

}
