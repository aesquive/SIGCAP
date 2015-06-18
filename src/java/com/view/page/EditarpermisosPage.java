/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Permisos;
import db.pojos.Permisosuser;
import db.pojos.Tipousuario;
import db.pojos.User;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import util.PermisosCheck;

/**
 *
 * @author Admin
 */
public class EditarpermisosPage extends BorderPage {

     private Form form;
    private TextField nameUser;
    private PasswordField password;
    private PasswordField checkPassword;
    private Select select_tipousuario;
    private Map<Integer,Tipousuario> map_tipousuario;
    private User usuarioEdit;
    
    @Override
    public void init() {
        usuarioEdit=(User) getSessionVar("userEdit");
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
        nameUser.setValue(usuarioEdit.getUser());
        password = new PasswordField("password", "Password", true);
        password.setValue(usuarioEdit.getPassword());
        checkPassword = new PasswordField("checkPassword", "Verificar Password", true);
        checkPassword.setValue(usuarioEdit.getPassword());
        select_tipousuario=new Select("tipUsu", "Rol de Usuario", true);
        select_tipousuario.setDefaultOption(new Option(-1,"--Seleccione--"));
        List<Tipousuario> query_tipousuario = DAO.createQuery(Tipousuario.class,null);
        for(Tipousuario t:query_tipousuario){
            if(t.getIdtipousuario()!=1 && usuarioEdit.getTipousuario().getIdtipousuario()!=t.getIdtipousuario()){
                select_tipousuario.add(new Option(t.getIdtipousuario(),t.getNombre()));
                map_tipousuario.put(t.getIdtipousuario(), t);
            }
        }
        select_tipousuario.setValue(String.valueOf(usuarioEdit.getTipousuario().getIdtipousuario()));
    }

    public boolean guardarAltaUsuario() {
        if (form.isValid()) {
            if (!password.getValue().equals(checkPassword.getValue())) {
                message="Los password no coinciden";
                return false;
            } else {
                usuarioEdit.setPassword(password.getValue());
                usuarioEdit.setTipousuario(map_tipousuario.get(Integer.parseInt(select_tipousuario.getValue())));
                DAO.update(usuarioEdit);
                
                DAO.saveRecordt(user,user.getUser()+" edito de alta a "+usuarioEdit.getUser()+" con tipo usuario "+usuarioEdit.getTipousuario().getNombre());
                message="Usuario modificado correctamente";
                setRedirect(BienvenidaPage.class);
                return true;
            }
        }
        return false;
    }


    @Override
    public Integer getPermisoNumber() {
        return 12;
    }

}
