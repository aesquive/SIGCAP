package com.view.page;

import org.apache.click.control.Column;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;

/**
 *
 * @author Admin
 */
public class ControlusuariosPage extends BorderPage {

    Form form;
    
    @Override
    public void init() {
        form=new Form("form");
        addControl(form);
        form.add(new Submit("alta","Alta de Usuario", this, "alta"));
        form.add(new Submit("editar","Editar Usuario",this, "editar"));
        form.add(new Submit("baja","Eliminar Usuario",this, "eliminar"));
        form.add(new Submit("desbloquear","Desbloquear Usuario",this,"desbloquear"));
    }

    public boolean alta(){
        setRedirect(AltausuariosPage.class);
        return true;
    }
    
    public boolean editar(){
        setRedirect(EditarusuariosPage.class);
        return true;
    }
    
    public boolean eliminar(){
        setRedirect(BajausuariosPage.class);
        return true;
    }
}
