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
import manager.session.SessionController;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import util.PermisosCheck;
import util.UserManager;

/**
 *
 * @author Admin
 */
public class AltausuariosPage extends BorderPage {

    private Form form;
    private List<Checkbox> checkBox;
    private List<PermisosCheck> permisos;
    private TextField nameUser;
    private PasswordField password;
    private PasswordField checkPassword;

    @Override
    public void init() {
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
        form.add(new Submit("guardar", "Guardar", this, "guardar"));
    }

    private void createCheckBoxes() {
        permisos = new LinkedList<PermisosCheck>();
        List<Permisos> createQuery = DAO.createQuery(Permisos.class, null);
        for (Permisos p : createQuery) {
            permisos.add(new PermisosCheck(p.getDesPermiso(), false));
        }
        checkBox = new LinkedList<Checkbox>();
        for (int t = 0; t < permisos.size(); t++) {
            Checkbox box = new Checkbox("c" + t, "Módulo " + permisos.get(t).getColumnDetail());
            box.setChecked(false);
            checkBox.add(box);
        }

    }

    private void initComponents() {
        form = new Form("form");
        nameUser = new TextField("nameUser", "Nombre de Usuario", true);
        password = new PasswordField("password", "Password", true);
        checkPassword = new PasswordField("checkPassword", "Verificar Password", true);
    }

    public boolean guardar() {
        if (form.isValid()) {
            if (!password.getValue().equals(checkPassword.getValue())) {
                message = "Los password no coinciden";
                return false;
            } else {
                List<User> createQuery = DAO.createQuery(User.class, null);
                for (User u : createQuery) {
                    if (u.getUser().toUpperCase().equals(nameUser.getValue().toUpperCase())) {
                        message = "Favor de registrar otro nombre de usuario";
                        return false;
                    }
                }

                User user = new User(nameUser.getValue());
                user.setPassword(password.getValue());
                user.setActivo(0);
                DAO.save(user);
                List<Permisos> todosPermisos = DAO.createQuery(Permisos.class, null);
                for (int t = 0; t < todosPermisos.size(); t++) {
                    int value = checkBox.get(t).isChecked() ? 1 : 0;
                    Permisosuser pu = new Permisosuser(user, todosPermisos.get(t), value);
                    DAO.save(pu);
                }
                SessionController controller = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext);
                User userSess = (User) controller.getVariable("user").getValue();
                DAO.saveRecordt(userSess,userSess.getUser()+" dio de alta a "+user.getUser());
                setRedirect(ControlusuariosPage.class);

                return true;
            }
        }
        return false;
    }

    public boolean cancelar() {
        form.setJavaScriptValidation(false);
        setRedirect(ControlusuariosPage.class);
        return true;
    }

}
