package com.view.page;

import db.controller.DAO;
import db.pojos.Valores;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.IntegerField;

/**
 *
 * @author Admin
 */
public class TenenciaeditarcalculoPage extends BorderPage {

    private Form form;
    private Valores valor;

    TextField tipoValor;
    TextField emision;
    TextField serie;
    DateField fechaProximoCupon;
    Select vencimiento;
    IntegerField numeroTitulos;
    DoubleField precio;
    Select sobretasa;
    Select calificacion;
    Select moneda;
    Select grupoRiesgosEmision;
    DoubleField ponderador;
    DateField fechaVencimiento;
    Select gradoRiesgo;

    @Override
    public void init() {
        valor = (Valores) getSessionVar("editCalcTenencia");
        form = new Form("form");
        form.setColumns(2);
        llenarCamposValores();
        llenarCamposVector();
        form.add(new Submit("sub", "Guardar", this, "guardarTenencia"));
        addControl(form);
    }

    public boolean guardarTenencia() {
        if (form.isValid()) {
            valor.setTipoValor(tipoValor.getValue());
            valor.setEmision(emision.getValue());
            valor.setSerie(serie.getValue());
            valor.setFechaProximoCupon(fechaProximoCupon.getDate());
            valor.setGrupoRc10(vencimiento.getValue());
            valor.setNumeroTitulos(numeroTitulos.getInteger());
            valor.setSobretasa(sobretasa.getValue());
            valor.setPrecio(precio.getDouble());
            String valMoneda=moneda.getValue().equals("1")?"UDI":moneda.getValue().equals("14")?"MXN":"USD";
            valor.setMoneda(valMoneda);
            valor.setCalificacion(calificacion.getValue());
            valor.setFechaVencimiento(fechaVencimiento.getDate());
            valor.setGradoRiesgo(Integer.parseInt(gradoRiesgo.getValue()));
            valor.setPonderador(ponderador.getDouble() / 100);
            valor.setGrupoRc07(grupoRiesgosEmision.getValue());
            DAO.update(valor);
        }
        setRedirect(Tenenciacalculo.class);
        return true;
    }

    private void llenarCamposValores() {
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        instance.setMaximumFractionDigits(6);
        tipoValor = new TextField("tv", "Tipo Valor");
        tipoValor.setDisabled(true);
        emision = new TextField("emi", "Emisora");
        emision.setDisabled(true);
        serie = new TextField("ser", "Serie", true);
        fechaProximoCupon = new DateField("prxCpn", "Fecha Proximo Cupón", true);
        vencimiento = new Select("selven", "Se mantiene a vencimiento", true);
        vencimiento.setDefaultOption(new Option("-1", "Seleccionar"));
        vencimiento.add(new Option("SI", "Si"));
        vencimiento.add(new Option("NO", "No"));
        numeroTitulos = new IntegerField("tit", "Número de Títulos", true);
        numeroTitulos.setNumberFormat(instance);
        sobretasa = new Select("stasa", "Tiene sobretasa ", true);
        sobretasa.setDefaultOption(new Option("-1", "Seleccionar"));
        sobretasa.add(new Option("SI", "Si"));
        sobretasa.add(new Option("NO", "No"));
        grupoRiesgosEmision = new Select("remi", "Grupo de Riesgo de Emisión", true);
        String[] valuesRiesgos = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        for (String s : valuesRiesgos) {
            grupoRiesgosEmision.add(new Option(s, s));
        }
        grupoRiesgosEmision.setDefaultOption(new Option("-1", "Seleccionar"));
        ponderador = new DoubleField("pond", "Ponderador por Riesgo %", 3, true);
        fechaProximoCupon.setFormatPattern("dd/MM/yyyy");
        precio = new DoubleField("pr", "Precio Sucio", true);
        precio.setNumberFormat(instance);
        fechaVencimiento = new DateField("fecVen", "Fecha de Vencimiento", true);
        fechaVencimiento.setFormatPattern("dd/MM/yyyy");
        moneda = new Select("mon", "Moneda", true);
        moneda.setDefaultOption(new Option("-1", "Seleccionar"));
        moneda.add(new Option("14", "MXN"));
        moneda.add(new Option("4", "USD"));
        moneda.add(new Option("1", "UDI"));
        calificacion = new Select("cal", "Calificación", true);
        calificacion.setDefaultOption(new Option("-1", "Seleccionar"));
        String[] califs = new String[]{"MxA-1+", "MxA-1", "MxA-2", "MxA-3", "MxB", "MxC", "MxD", "MxAAA+", "MxAAA", "MxAAA-",
            "MxAA+", "MxAA", "MxAA-", "MxA+", "MxA", "MxA-", "MxBBB+", "MxBBB", "MxBBB-", "MxBB+", "MxBB", "MxBB-",
            "MxB+", "MxB", "MxB-", "MxCCC", "MxCCC", "MxD"};
        for (String s : califs) {
            calificacion.add(new Option(s, s));
        }
        gradoRiesgo = new Select("gradoRiesgo", "Grado de Riesgo", true);
        gradoRiesgo.add(new Option("1", "1"));
        gradoRiesgo.add(new Option("2", "2"));
        gradoRiesgo.add(new Option("3", "3"));
        gradoRiesgo.add(new Option("4", "4"));
        gradoRiesgo.add(new Option("5", "5"));
        gradoRiesgo.setDefaultOption(new Option("-1", "-1"));

        form.add(tipoValor);
        form.add(emision);
        form.add(serie);
        form.add(fechaProximoCupon);
        form.add(vencimiento);
        form.add(numeroTitulos);
        form.add(sobretasa);
        form.add(grupoRiesgosEmision);
        form.add(ponderador);
        form.add(precio);
        form.add(moneda);
        form.add(fechaVencimiento);
        form.add(calificacion);
        form.add(gradoRiesgo);

    }

    private void llenarCamposVector() {
        tipoValor.setValue(valor.getTipoValor());
        emision.setValue(valor.getEmision());
        serie.setValue(valor.getSerie());
        fechaProximoCupon.setDate(valor.getFechaProximoCupon());
        vencimiento.setValue(valor.getGrupoRc10());
        numeroTitulos.setInteger(valor.getNumeroTitulos());
        precio.setDouble(valor.getPrecio());
        sobretasa.setValue(valor.getSobretasa().toUpperCase());
        calificacion.setValue(calificacion.getValue());
        String monedaSel=valor.getMoneda()==null || valor.getMoneda().equalsIgnoreCase("MXN")?"14":valor.getMoneda().equalsIgnoreCase("UDI")?"1":"4";
        moneda.setValue(monedaSel);
        grupoRiesgosEmision.setValue(valor.getGrupoRc07());
        ponderador.setDouble(valor.getPonderador()*100);
        fechaVencimiento.setDate(valor.getFechaVencimiento());
        gradoRiesgo.setValue(valor.getGradoRiesgo().toString());
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

}
