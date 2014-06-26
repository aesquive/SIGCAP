/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Permisos;
import db.pojos.Permisosuser;
import db.pojos.User;
import java.util.LinkedList;
import java.util.List;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import util.PermisosCheck;

/**
 *
 * @author Admin
 */
public class EditarpermisosPage extends BorderPage {

    private Form form;
    private List<Checkbox> checkBox;
    private List<PermisosCheck> permisos;
    private TextField nameUser;
    private PasswordField password;
    private PasswordField checkPassword;
    private User user;
    private String userNameRequest;

    @Override
    public void init() {
        userNameRequest = (String) getContext().getSessionAttribute("userEdit");
        List<User> createQuery = DAO.createQuery(User.class, null);
        System.out.println(userNameRequest);
        for (User u : createQuery) {
            if (userNameRequest != null && u.getUser().toUpperCase().equals(userNameRequest.toUpperCase())) {
                user = u;
            }
        }
        initComponents();
        createCheckBoxes();
        addControl(form);
        FieldSet fs = new FieldSet("fs", "Datos de Usuario");
        fs.add(nameUser);
        fs.add(password);
        fs.add(checkPassword);
        form.add(fs);

        FieldSet fsp = new FieldSet("fsp", "Permisos");
        for (Checkbox c : checkBox) {
            fsp.add(c);
        }
        form.add(fsp);
        Submit sub = new Submit("guardar", "Guardar", this, "guardar");
        form.add(sub);
    }

    private void createCheckBoxes() {
        permisos = new LinkedList<PermisosCheck>();
        List<Permisosuser> createQuery = DAO.createQuery(Permisosuser.class, null);
        for (Permisosuser p : createQuery) {
            if (p.getUser().getIduser() == user.getIduser()) {
                boolean value = p.getValor() > 0 ? true : false;
                permisos.add(new PermisosCheck(p.getPermisos().getDesPermiso(), value));
            }
        }
        checkBox = new LinkedList<Checkbox>();
        for (int t = 0; t < permisos.size(); t++) {
            Checkbox box = new Checkbox("c" + t, permisos.get(t).getColumnDetail());
            box.setChecked(permisos.get(t).isActive());
            checkBox.add(box);
        }

    }

    private void initComponents() {
        form = new Form("form");
        nameUser = new TextField("nameUser", "Nombre de Usuario", true);
        nameUser.setDisabled(true);
        password = new PasswordField("password", "Password", true);
        checkPassword = new PasswordField("checkPassword", "Verificar Password", true);
        password.setDisabled(true);
        checkPassword.setDisabled(true);
        if (user != null) {
            nameUser.setValue(user.getUser());
            password.setValue(user.getPassword());
            checkPassword.setValue(user.getPassword());
        }

    }

    public boolean guardar() {
        if (form.isValid()) {
            List<User> createQuery = DAO.createQuery(User.class, null);
            for (User u : createQuery) {
                if (user != null && u.getUser().toUpperCase().equals(user.getUser().toUpperCase())) {
                    user = u;
                }
            }
            List<Permisos> todosPermisos = DAO.createQuery(Permisos.class, null);
            List<Permisosuser> createQuery1 = DAO.createQuery(Permisosuser.class, null);
            for (int t = 0; t < todosPermisos.size(); t++) {
                int value = checkBox.get(t).isChecked() ? 1 : 0;
                Permisosuser pud = null;
                for (Permisosuser pu : createQuery1) {
                    if (pu.getUser().getIduser() == user.getIduser() && pu.getPermisos().getDesPermiso().toUpperCase().equals(checkBox.get(t).getLabel().toUpperCase())) {
                        pud = pu;
                    }
                }
                pud.setValor(value);
                DAO.update(pud);
            }
            User userSess = (User) getSessionVar("user");
            DAO.saveRecordt(userSess, userSess.getUser() + " modificó permisos del usuario " + user.getUser());
            setRedirect(ControlusuariosPage.class);
            return true;
        }
        return false;
    }

}
