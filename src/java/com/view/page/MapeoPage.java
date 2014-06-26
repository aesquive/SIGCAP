package com.view.page;

import db.controller.DAO;
import java.util.List;
import java.util.Vector;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author Admin
 */
public class MapeoPage extends BorderPage {

    Form form;
    Select select;
    private List<Vector> instruments;

    @Override
    public void init() {
        form = new Form("form");
        select = new Select("select", "Instrumentos Actuales");
        fillInstrument();
        form.add(select);
        form.add(new Submit("addInstrument", "Agregar Instrumento", this, "addInstrument"));
        form.add(new Submit("editInstrument", "Editar Instrumento", this, "editInstrument"));
        addControl(form);
    }

    private void fillInstrument() {
        instruments = DAO.createQuery(Vector.class, null);
        for (Vector v : instruments) {
      //      select.add(new Option(v.getIdVector(), v.getIdTipoInstrumento() + v.getEmisioraInstrumento() + v.getSerieInstrumento()));
        }
    }

    public boolean addInstrument() {
        setRedirect(MapeoagregarPage.class);
        return true;
    }

    public boolean editInstrument() {
        message="";
        Vector chosen = null;
        for (Vector v : instruments) {
        //    if (Integer.parseInt(select.getValue()) == v.getIdVector()) {
          //      chosen = v;
            //}
        }
        if (chosen == null) {
            message = "Se debe elegir un Instrumento a Editar";
            return false;
        }
        setRedirect(MapeoeditPage.class);
        return true;
    }
}
