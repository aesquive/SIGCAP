/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class EditarusuariosPage extends BorderPage {

    private Select select;
    private Form form;

    @Override
    public void init() {
        form = new Form("form");
        addControl(form);
        select = new Select("select", "Seleccionar Usuario", true);
        select.setDefaultOption(new Option(-1, "--Seleccionar--"));
        List<User> createQuery = DAO.createQuery(User.class, null);
        SessionController controller = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext);
        User userSess = (User) controller.getVariable("user").getValue();
        for (User u : createQuery) {
            if (!u.getUser().toUpperCase().equals(userSess.getUser().toUpperCase())) {
                select.add(new Option(u.getIduser(), u.getUser()));
            }
        }
        form.add(select);
        form.add(new Submit("sub", "Editar Permisos", this, "editar"));
    }

    public boolean editar() {
        if (form.isValid()) {
            Map<String, String> map = new HashMap<String, String>();
            List<User> createQuery = DAO.createQuery(User.class, null);
            String user = "";
            for (User u : createQuery) {
                if (u.getIduser()==Integer.parseInt(select.getValue())) {
                    user=u.getUser();
                }
            }
            getContext().setSessionAttribute("userEdit", user);
            setRedirect(EditarpermisosPage.class, map);
            return true;
        }
        return false;
    }
}
