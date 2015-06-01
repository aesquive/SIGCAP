
package com.view.page;

import communications.ProviderServerConnection;
import db.controller.DAO;
import db.pojos.Permisos;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Option;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 *
 * @author Admin
 */
public class ConectserverPage extends Page{

    Form form;
    PasswordField password;
    RadioGroup radios;
    Label label;
        /**
     * obtienes la parte del estilo de la pagina
     */
    @Override
    public String getTemplate() {
        return "/border-template.htm";
    }
    
    public ConectserverPage(){
        init();
    }
    
    public void init() {
        Object atr=getContext().getSessionAttribute("msg");
        String message=atr==null ? "" : (String)atr;
        form=new Form("form");
        password=new PasswordField("pass","Password de Cliente",20,true);
        radios=new RadioGroup("radios", "Opciones", true);
        radios.add(new Radio(String.valueOf(ProviderServerConnection.UNLOCK_USER), "Desbloquear Administrador del Sistema"));
        radios.add(new Radio(String.valueOf(ProviderServerConnection.UNLOCK_MODULE), "Desbloquear Modulos"));
        label=new Label("label", message);
        form.add(password);
        form.add(radios);
        form.add(new Submit("sub","Enviar al Servidor Central", this, "enviar"));
        form.add(label);
        addControl(form);
    }
    
    public boolean enviar(){
        if(form.isValid()){
            try {
                ProviderServerConnection pr=new ProviderServerConnection();
                String message=pr.execute(Integer.parseInt(radios.getValue()),password.getValue());
                getContext().setSessionAttribute("msg",message);
                setRedirect(ConectserverPage.class);
                return true;
            } catch (Exception ex) {
                String message="No se pudo generar la comunicación con el Servidor";
                getContext().setSessionAttribute("msg",message);
                setRedirect(ConectserverPage.class);
                Logger.getLogger(ConectserverPage.class.getName()).log(Level.INFO, null, ex);
                return true;
            }
        }
        return false;
    }
    
}
