package com.view.page;

import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import util.Util;

/**
 *
 * @author Admin
 */
public class ControlusuariosPage extends BorderPage {

    Form form;
    private static int numPer = 6;

    @Override
    public void init() {

        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) && dte.get(numPer) == true) {
            setRedirect(NocontratadoPage.class);
            return;
        }
        form = new Form("form");
        addControl(form);
        form.add(new Submit("alta", "Alta de Usuario", this, "alta"));
        form.add(new Submit("editar", "Editar Usuario", this, "editar"));
        form.add(new Submit("baja", "Estatus Usuario", this, "modificarEstado"));

    }

    public boolean alta() {
        setRedirect(AltausuariosPage.class);
        return true;
    }

    public boolean editar() {
        setRedirect(EditarusuariosPage.class);
        return true;
    }

    public boolean modificarEstado() {
        setRedirect(BajausuariosPage.class);
        return true;
    }
}
