/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.page;

import db.controller.DAO;
import db.pojos.Permisos;
import db.pojos.Tipousuario;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 *
 * @author desarrollo
 */
public class AltatipousuarioPage extends BorderPage {

    Form form;
    List<Permisos> permisos;
    TextField nombre;
    Select selectPadres;
    Map<Integer, Checkbox> permisos_checkBox;

    @Override
    public void init() {
        title="Alta de Rol";
        form = new Form("form");

        FieldSet fs = new FieldSet("atu", "Alta de Rol");

        permisos = DAO.createQuery(Permisos.class, null);
        nombre = new TextField("nombre", "Nombre del Rol", true);
        selectPadres = new Select("sele", "Usuario Superior ", true);
        permisos_checkBox = new HashMap<Integer, Checkbox>();

        selectPadres.setDefaultOption(new Option("-1", "--Seleccione--"));
        List<Tipousuario> query_tipousuario = DAO.createQuery(Tipousuario.class, null);
        for (Tipousuario t : query_tipousuario) {
            selectPadres.add(new Option(t.getIdtipousuario(), t.getNombre()));
        }
        fs.add(nombre);
        fs.add(selectPadres);
        List<Permisos> query_permisos = DAO.createQuery(Permisos.class, null);
        for (Permisos p : query_permisos) {
            if (p.getCodigo() == null) {
                Checkbox c = new Checkbox("check_" + p.getIdPermiso(), p.getDesPermiso());
                permisos_checkBox.put(p.getIdPermiso(), c);
                fs.add(c);
            }

        }
        fs.add(new Submit("sub", "Guardar", this, "guardarTipoUsuario"));
        form.add(fs);
        addControl(form);
    }

    public boolean guardarTipoUsuario() {
        if (form.isValid()) {
            Tipousuario tu = new Tipousuario();
            tu.setNombre(nombre.getValue());
            List<Tipousuario> query_tipousuario = DAO.createQuery(Tipousuario.class, null);
            for (Tipousuario t : query_tipousuario) {
                if (selectPadres.getValue().equals(String.valueOf(t.getIdtipousuario()))) {
                    tu.setPadre(t);
                }
            }
            List<Integer> permisosAut = new LinkedList<Integer>();
            for (Integer key : permisos_checkBox.keySet()) {
                if (permisos_checkBox.get(key).isChecked()) {
                    permisosAut.add(key);
                }
            }
            Collections.sort(permisosAut);
            String permisosSTR = "";
            for (Integer i : permisosAut) {
                permisosSTR += i + ",";
            }
            tu.setPermisos(permisosSTR.substring(0, permisosSTR.length() - 1));
            DAO.save(tu);
            DAO.saveRecordt(user, "Dio de alta el tipo de usuario " + tu.getNombre() + " con usuario " + tu.getPermisos());
            setRedirect(BienvenidaPage.class);
            return true;
        }
        return false;
    }

    @Override
    public Integer getPermisoNumber() {
        return 13;
    }

}
