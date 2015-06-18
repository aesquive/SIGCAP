/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Tipousuario;
import java.util.List;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;

/**
 *
 * @author desarrollo
 */
public class EditartipousuarioselectPage extends BorderPage {

    Form form;
    Select select_tipousuario;
    List<Tipousuario> query_tipousuario;

    @Override
    public void init() {
        form = new Form("form");
        query_tipousuario = DAO.createQuery(Tipousuario.class, null);
        select_tipousuario = new Select("sel", "Seleccionar tipo de Usuario", true);
        select_tipousuario.setDefaultOption(new Option("-1", "--Seleccione--"));
        for (Tipousuario t : query_tipousuario) {
            if (t.getIdtipousuario() != 1) {

                select_tipousuario.add(new Option(t.getIdtipousuario(), t.getNombre()));
            }
        }
        form.add(select_tipousuario);
        form.add(new Submit("sub", "Editar", this, "editarTipoUsuario"));
        addControl(form);
    }

    public boolean editarTipoUsuario() {
        if (form.isValid()) {
            for (Tipousuario t : query_tipousuario) {
                if (select_tipousuario.getValue().equals(String.valueOf(t.getIdtipousuario()))) {
                    addSessionVar("tipoUsuarioEdit", t);
                    setRedirect(EditartipousuarioPage.class);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 14;
    }

}
