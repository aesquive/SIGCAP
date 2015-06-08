package com.view.page;

import db.controller.DAO;
import db.pojos.Calificacion;
import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.IntegerField;
import util.Vector;

/**
 *
 * @author Admin
 */
public class MapeoeditPage extends BorderPage {

    private Form form;
    private Valores valor;

    DateField fechaEjercicio;
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
    Vector vector;

    @Override
    public void init() {
        valor = (Valores) getSessionVar("editMapeo");
        vector = (Vector) getSessionVar("vectorEditMapeo");
        form = new Form("form");
        form.setColumns(2);
        llenarCamposValores();
        llenarCamposVector();
        String tipoValor = valor.getTipoValor();
        String[] noStasa = new String[]{"BI", "I", "G", "MC", "SC", "MP", "SP", "3P", "4P", "3U", "4U", "6U", "CC", "IL", "M", "S", "PI", "97", "2P", "2U", "FA", "FB", "FC", "FD", "FI", "FM", "FS", "OA", "OD", "OI"};
        for (String ns : noStasa) {
            if (ns.toUpperCase().trim().equals(tipoValor.toUpperCase().trim())) {
                sobretasa.setValue("NO");
                sobretasa.setDisabled(true);
                sobretasa.setStyle("background-color", "white");
                message = "Por definición el instrumento no tiene sobretasa";
            }
        }
        form.add(new Submit("sub", "Guardar", this, "guardarTenencia"));
        addControl(form);
    }

    public boolean guardarTenencia() {
        if (form.isValid()) {
            Vector vec = new Vector(tipoValor.getValue() + emision.getValue() + serie.getValue(),
                    precio.getDouble(), fechaVencimiento.getDate(), Integer.parseInt(moneda.getValue()), calificacion.getValue(), calificacion.getValue(), calificacion.getValue(), calificacion.getValue(), sobretasa.getValue());
            vec.setGradoRiesgo(gradoRiesgo.getValue());
            vec.setGrupoRiesgo(grupoRiesgosEmision.getValue());
            vec.setPonderador(ponderador.getDouble()/100);
            vec.setSp(calificacion.getValue());
            vec.setMapeada(1);
            Map<String, Vector> sessionVar = (Map<String, Vector>) getSessionVar("mapeoVector");
            sessionVar.put(tipoValor.getValue() + emision.getValue() + serie.getValue(), vec);
            addSessionVar("mapeoVector", sessionVar);
            valor.setSerie(serie.getValue());
            valor.setFechaProximoCupon(fechaProximoCupon.getDate());
            valor.setGrupoRc10(vencimiento.getValue());
            valor.setNumeroTitulos(numeroTitulos.getInteger());
            valor.setSobretasa(sobretasa.getValue());
            valor.setPrecio(precio.getDouble());
            valor.setMoneda(moneda.getValue());
            valor.setCalificacion(calificacion.getValue());
            valor.setFechaVencimiento(fechaVencimiento.getDate());
            valor.setGradoRiesgo(Integer.parseInt(gradoRiesgo.getValue()));
            valor.setPonderador(ponderador.getDouble()/100);
            valor.setGrupoRc07(grupoRiesgosEmision.getValue());
            DAO.update(valor);
            List<Valores> createQuery = DAO.createQuery(Valores.class, null);
            Regcuenta regCta = (Regcuenta) getSessionVar("mapeoRegCuenta");
            List<Valores> valNvos = new LinkedList<Valores>();
            for (Valores val : createQuery) {
                if (val.getRegcuenta().getIdRegCuenta() == regCta.getIdRegCuenta()) {
                    valNvos.add(val);
                }
            }
            addSessionVar("mapeoTenencia", valNvos);
            setRedirect(MapeoPage.class);
            return true;
        }
        return false;
    }

    private void llenarCamposValores() {
        fechaEjercicio = new DateField("fecEje", "Fecha de Ejercicio", 0, false);
        fechaEjercicio.setFormatPattern("dd/MM/yyyy");
        fechaEjercicio.setDisabled(true);
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
        sobretasa = new Select("stasa", "Tiene sobretasa ", true);
        sobretasa.setDefaultOption(new Option("-1", "Seleccionar"));
        sobretasa.add(new Option("SI", "SI"));
        sobretasa.add(new Option("NO", "NO"));
        grupoRiesgosEmision = new Select("remi", "Grupo de Riesgo de Emisión", true);
        String[] valuesRiesgos = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        for (String s : valuesRiesgos) {
            grupoRiesgosEmision.add(new Option(s, s));
        }
        grupoRiesgosEmision.setDefaultOption(new Option("-1", "Seleccionar"));
        ponderador = new DoubleField("pond", "Ponderador por Riesgo %", 3, true);
        fechaEjercicio.setDate(valor.getFecha());
        tipoValor.setValue(valor.getTipoValor());
        emision.setValue(valor.getEmision());
        serie.setValue(valor.getSerie());
        fechaProximoCupon.setDate(valor.getFechaProximoCupon());
        fechaProximoCupon.setFormatPattern("dd/MM/yyyy");

        vencimiento.setValue(valor.getGrupoRc10());
        numeroTitulos.setInteger(valor.getNumeroTitulos());

        form.add(fechaEjercicio);
        form.add(tipoValor);
        form.add(emision);
        form.add(serie);
        form.add(fechaProximoCupon);
        form.add(vencimiento);
        form.add(numeroTitulos);
        form.add(sobretasa);
        form.add(grupoRiesgosEmision);
        form.add(ponderador);
    }

    private void llenarCamposVector() {
        precio = new DoubleField("pr", "Precio Sucio", true);
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        instance.setMaximumFractionDigits(6);
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
        if (vector != null) {
            if (vector.getPrecioSucio() != null) {
                precio.setDouble(vector.getPrecioSucio());
                precio.setRequired(false);
                precio.setDisabled(true);
                precio.setStyle("background-color", "white");
            }
            
            if (vector.getMoneda()>0) {
                moneda.setValue(String.valueOf(vector.getMoneda()));
                fechaVencimiento.setRequired(false);
                fechaVencimiento.setDisabled(true);
                fechaVencimiento.setStyle("background-color", "white");
            }
            if (vector.getFechaVencimiento() != null) {
                fechaVencimiento.setDate(vector.getFechaVencimiento());
                fechaVencimiento.setRequired(false);
                fechaVencimiento.setDisabled(true);
                fechaVencimiento.setStyle("background-color", "white");
            }
            Map<String, Calificacion> califsMap = new HashMap<String, Calificacion>();
            List<Calificacion> createQuery = DAO.createQuery(Calificacion.class, null);
            for (Calificacion c : createQuery) {
                califsMap.put(c.getCalificacion(), c);
                califsMap.put(c.getCalificacion().toUpperCase(), c);
            }
        }
        form.add(precio);
        form.add(moneda);
        form.add(fechaVencimiento);
        form.add(calificacion);
        form.add(gradoRiesgo);
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

}
