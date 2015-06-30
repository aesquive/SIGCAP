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
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class EditarusuariosPage extends BorderPage {

    private Select select;
    private Form form;

    @Override
    public void init() {
        message=null;
        title=" Editar Usuario";
        form = new Form("form");
        addControl(form);
        select = new Select("select", "Seleccionar Usuario", true);
        select.setDefaultOption(new Option(-1, "--Seleccionar--"));
        List<User> createQuery = DAO.createQuery(User.class, null);
        User userSess = (User) getSessionVar("user");
        for (User u : createQuery) {
            if (u.getTipousuario().getIdtipousuario()!=1) {
                select.add(new Option(u.getIduser(), u.getUser()));
            }
        }
        form.add(select);
        form.add(new Submit("sub", "Editar Permisos", this, "editarUsuario"));
    }

    public boolean editarUsuario() {
        if (form.isValid()) {
            Map<String, String> map = new HashMap<String, String>();
            List<User> createQuery = DAO.createQuery(User.class, null);
            User selectedUSer = null;
            for (User u : createQuery) {                
                if (u.getIduser() == Integer.parseInt(select.getValue())) {
                    selectedUSer = u;
                }
            }
            
            addSessionVar("userEdit", selectedUSer);
            setRedirect(EditarpermisosPage.class, map);
            return true;
        }
        message="Seleccionar un usuario a editar";
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 15;
    }
}
