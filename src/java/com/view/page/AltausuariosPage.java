/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Tipousuario;
import db.pojos.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 *
 * @author Admin
 */
public class AltausuariosPage extends BorderPage {

    private Form form;
    private TextField nameUser;
    private PasswordField password;
    private PasswordField checkPassword;
    private Select select_tipousuario;
    private Map<Integer,Tipousuario> map_tipousuario;
    
    @Override
    public void init() {
        title="Alta de Usuario";
        message=null;
        map_tipousuario=new HashMap<Integer, Tipousuario>();
        initComponents();
        addControl(form);
        FieldSet fs = new FieldSet("fs", "Datos de Usuario");
        fs.add(nameUser);
        fs.add(password);
        fs.add(checkPassword);
        fs.add(select_tipousuario);
        form.add(fs);
        form.add(new Submit("guardar", "Guardar", this, "guardarAltaUsuario"));
    }

  

    private void initComponents() {
        form = new Form("form");
        nameUser = new TextField("nameUser", "Nombre de Usuario", 15,true);
        password = new PasswordField("password", "Password", true);
        checkPassword = new PasswordField("checkPassword", "Verificar Password", true);
        select_tipousuario=new Select("tipUsu", "Tipo de Usuario", true);
        select_tipousuario.setDefaultOption(new Option(-1,"--Seleccione--"));
        List<Tipousuario> query_tipousuario = DAO.createQuery(Tipousuario.class,null);
        for(Tipousuario t:query_tipousuario){
            if(t.getIdtipousuario()!=1){
                select_tipousuario.add(new Option(t.getIdtipousuario(),t.getNombre()));
                map_tipousuario.put(t.getIdtipousuario(), t);
            }
        }
    }

    public boolean guardarAltaUsuario() {
        if (form.isValid()) {
            if (!password.getValue().equals(checkPassword.getValue())) {
                message="Los password no coinciden";
                return false;
            } else {
                List<User> createQuery = DAO.createQuery(User.class, null);
                for (User u : createQuery) {
                    if (u.getUser().toUpperCase().equals(nameUser.getValue().toUpperCase())) {
                        message = "Favor de registrar otro nombre de usuario";
                        return false;
                    }
                }
                User usuarioNuevo = new User(nameUser.getValue());
                usuarioNuevo.setPassword(password.getValue());
                usuarioNuevo.setActivo(0);
                usuarioNuevo.setNumlogin(0);
                usuarioNuevo.setTipousuario(map_tipousuario.get(Integer.parseInt(select_tipousuario.getValue())));
                DAO.save(usuarioNuevo);
                
                DAO.saveRecordt(user,user.getUser()+" dio de alta a "+usuarioNuevo.getUser()+" con tipo usuario "+usuarioNuevo.getTipousuario().getNombre());
                message="Usuario dado de alta correctamente";
                setRedirect(BienvenidaPage.class);
                return true;
            }
        }
        return false;
    }


    @Override
    public Integer getPermisoNumber() {
        return 11;
    }

}
