package com.view.page;

import db.controller.DAO;
import db.pojos.Catalogocuenta;
import db.pojos.Cuenta;
import db.pojos.Moneda;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import db.pojos.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.commons.fileupload.FileItem;
import util.Util;

/**
 *
 * @author Admin
 */
public class CargadatosPage extends BorderPage {

    Form form;
    FileField fileTenencia;
    FileField fileCaptacion;
    FileField fileCarteraComercial;
    FileField fileCarteraConsumo;
    FileField fileDisponibilidades;
    FileField fileTarjetaCredito;
    FileField fileCatalogoMinimo;
    TextField name;
    DateField dateField;
    private static int numPer = 0;

    @Override
    public void init() {
        this.form = new Form("form");
        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) || !dte.get(numPer)) {
            setRedirect(NocontratadoPage.class);
            return;
        }
        name = new TextField("name", "Nombre del Ejercicio", true);
        dateField = new DateField("dateField", "Fecha de Ejercicio (dd/mm/aaaa)", true);
        dateField.setFormatPattern("dd/MM/yyyy");
        fileTenencia = new FileField("fileTenencia", "Tenencia  ", false);
        fileCaptacion = new FileField("fileCaptacion", "Captación   ", false);
        fileCarteraComercial = new FileField("fileComercial", "Cartera Comercial  ", false);
        fileDisponibilidades = new FileField("fileDisponibilidades", "Disponibilidades  ", false);
        fileCarteraConsumo = new FileField("fileConsumo", "Cartera Consumo   ", false);
        fileTarjetaCredito = new FileField("fileTarjeta", "Tarjeta de Crédito   ", false);
        form.add(name);
        form.add(dateField);
        form.add(fileTenencia);
        form.add(fileCaptacion);
        form.add(fileCarteraComercial);
        form.add(fileCarteraConsumo);
        form.add(fileDisponibilidades);
        form.add(fileTarjetaCredito);

        fileCatalogoMinimo = new FileField("fileCatalogo", "Catálogo Mínimo", true);
        form.add(fileCatalogoMinimo);

        Submit sub = new Submit("sub", "Procesar", this, "procesarClicked");
        javaScriptProcess(sub);
        form.add(sub);

        addControl(form);

    }

    public boolean procesarClicked() {
        if (form.isValid()) {
            try {
                message = "Reportes Invalidos :";
                List<String> dataCatalogoMinimo = getFileData(fileCatalogoMinimo);
                boolean validarNumeroCampos = validNumberFields(dataCatalogoMinimo, 4);
                if (!validarNumeroCampos) {
                    message = message + "CatalogoMinimo";
                    return false;
                }
                Regcuenta regCuenta = saveProject();
                User user = (User) getSessionVar("user");
                saveUserRelation(regCuenta, user);
                saveCuenta(regCuenta, dataCatalogoMinimo);
                message = "";
                DAO.saveRecordt(user, user.getUser() + "generó alta del ejercicio " + regCuenta.getDesRegCuenta());
                setRedirect(AdministradormodelosPage.class);
                return true;
            } catch (Exception ex) {
                message = "Algún error ha ocurrido";
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
        }
        return false;
    }

    public List<String> getFileData(FileField fileField) throws IOException {
        FileItem fileItem = fileField.getFileItem();
        List<String> lines = new LinkedList<String>();
        InputStream inp = fileItem.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
        String linea = reader.readLine();
        while (linea != null) {
            lines.add(linea);
            linea = reader.readLine();
        }
        reader.close();
        inp.close();
        return lines;
    }

    private boolean validNumberFields(List<String> dataCatalogoMinimo, int i) {
        if (dataCatalogoMinimo.get(0).split(";").length == i) {
            return true;
        }
        return false;
    }

    private Regcuenta saveProject() {
        Regcuenta regcuenta = new Regcuenta(name.getValue());
        regcuenta.setFecha(dateField.getDate());
        DAO.save(regcuenta);
        return regcuenta;
    }

    private void saveUserRelation(Regcuenta regCuenta, User user) {
        Regcuentauser regcuentauser = new Regcuentauser(regCuenta, user);
        DAO.save(regcuentauser);
    }

    private void saveCuenta(Regcuenta regCuenta, List<String> dataCatalogoMinimo) {
        Map<String, Catalogocuenta> catalogos = new HashMap<String, Catalogocuenta>();
        List<Catalogocuenta> createQuery = DAO.createQuery(Catalogocuenta.class, null);
        for (Catalogocuenta c : createQuery) {
            catalogos.put(c.getIdCatalogoCuenta().toString(), c);
        }
        Map<String, Moneda> monedas = new HashMap<String, Moneda>();
        List<Moneda> qmon = DAO.createQuery(Moneda.class, null);
        for (Moneda m : qmon) {
            monedas.put(m.getIdMoneda().toString(), m);
        }
        for (String c : dataCatalogoMinimo) {
            String[] split = c.split(";");
            Cuenta cta = new Cuenta();
            cta.setRegcuenta(regCuenta);
            cta.setStatus(0);
            cta.setCatalogocuenta(catalogos.get(split[0]));
            String number = quitarRelleno(split[3]);
            cta.setValor(Double.parseDouble(number));
            cta.setMoneda(monedas.get(split[2]));
            DAO.save(cta);
        }
    }

    private String quitarRelleno(String string) {
        while (string.length() > 0 && string.charAt(0) == '0') {
            string = string.substring(1);
        }
        if (string.length() == 0) {
            string = "0";
        }
        return string;
    }

}