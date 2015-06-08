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
public class BajausuariosPage extends BorderPage {

    private Select select;
    private Form form;
    private Select accion;
    
    @Override
    public void init() {
        form = new Form("form");
        accion=new Select("selectAccion","Acción",true);
        accion.setDefaultOption(new Option(-1, "--Seleccionar--"));
        accion.add(new Option(0,"Desbloquear Usuario"));
        accion.add(new Option(2,"Baja de Usuario"));
        addControl(form);
        select = new Select("select", "Seleccionar Usuario", true);
        select.setDefaultOption(new Option(-1, "--Seleccionar--"));
        List<User> createQuery = DAO.createQuery(User.class, null);
        User userSess = (User) getSessionVar("user");
        for (User u : createQuery) {
            if (!u.getUser().toUpperCase().equals(userSess.getUser().toUpperCase())) {
                select.add(new Option(u.getIduser(), u.getUser()));
            }
        }
        form.add(select);
        form.add(accion);
        form.add(new Submit("sub", "Guardar", this, "eliminar"));
    }

    public boolean eliminar() {
        if (form.isValid() && Integer.parseInt(accion.getValue())!=-1) {
            Map<String, String> map = new HashMap<String, String>();
            List<User> createQuery = DAO.createQuery(User.class, null);
            User user = null;
            for (User u : createQuery) {
                if (u.getIduser() == Integer.parseInt(select.getValue())) {
                    user = u;
                }
            }
            user.setActivo(Integer.parseInt(accion.getValue()));
            String texto=Integer.parseInt(accion.getValue())==0 ? " desbloqueó al usuario " : " dio de baja al usuario "; 
            DAO.update(user);
            User userSess = (User) getSessionVar("user");
            DAO.saveRecordt(userSess, userSess.getUser() + texto + user.getUser());
            setRedirect(BienvenidaPage.class);
            return true;
        }
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

}
