package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.User;
import java.util.LinkedList;
import java.util.List;
import manager.configuration.Configuration;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Clase que maneja toda la seccion del Login
 *
 * @author zorin
 */
public class LoginPage extends Page {

    private Form loginForm = new Form("form");
    private PasswordField passwordField;
    private TextField userField;
    public String title;
    public String message;
    public boolean login = true;

    /**
     * obtienes la parte del estilo de la pagina
     */
    @Override
    public String getTemplate() {
        return "/border-template.htm";
    }

    /**
     * genera el constructor del login
     *
     */
    public LoginPage() {
        getContext().removeSessionAttribute("msg");
        getContext().removeSessionAttribute("user");
        init();
        //como es la pagina de login limpiamos toda la sesion pasada

        //agregamos el form a la pantalla
        addControl(loginForm);
        //le damos el setup al form
        loginForm.add(userField);
        loginForm.add(passwordField);
        Submit submit = new Submit("okSubmit", " Accesar ", this, "okClicked");
        loginForm.add(submit);
        submit.setAttribute("onclick", "waitPageLogin();");
    }

    /**
     * metodo inicial
     */
    public void init() {
        loginForm = new Form("form");
        passwordField = new PasswordField("password", " Password ", true);
        userField = new TextField("usuario", " Usuario ", true);
        passwordField.setMaxLength(10);
        userField.setMaxLength(10);
        passwordField.setMinLength(2);
    }

    /**
     * evento al dar click en OK para loggear
     *
     * @return
     */
    public boolean okClicked() {
        if (!loginForm.isValid()) {
            message = "Favor de completar los campos";

            return false;
        }
        User user = verifyUser(userField.getValue(), passwordField.getValue());
/*        if (user == null) {
            message = "Usuario y/o password incorrecto";
            return false;
        }
        if(user.getActivo()==1){
            message="El usuario esta actualmente conectado";
            return false;
        }
        if(user.getActivo()==2){
            message="El usuario esta dado de baja";
            return false;
        }*/
        int actualValue=user.getNumlogin();
        int maxValue=Integer.parseInt(Configuration.getValue("MaxLogin").replace("\"", ""));
        
        if(maxValue>0 && actualValue>maxValue){
            message="Esta es una versión de prueba, favor de contactar al administrador";
            return false;
        }
        user.setNumlogin(user.getNumlogin()+1);
        DAO.update(user);
        user.setActivo(1);
        DAO.update(user);
        getContext().setSessionAttribute("user", user);
        DAO.saveRecordt(user,user.getUser()+" ingresó al sistema");
        setRedirect(BienvenidaPage.class);
        return true;
    }

    /**
     * revisa que elk usuario loggeado este dado de alta
     *
     * @param user
     * @param pass
     * @return
     */
    private User verifyUser(String user, String pass) {
        List<User> createQuery = DAO.createQuery(User.class, new Criterion[]{Restrictions.and(
            Restrictions.eq("user", user), Restrictions.eq("password", pass))});
        if (createQuery.isEmpty()) {
            return null;
        }
        return createQuery.get(0);
    }

    

}
