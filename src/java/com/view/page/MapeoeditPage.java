package com.view.page;

import db.controller.DAO;
import db.pojos.Regcuenta;
import db.pojos.Valores;
import java.util.LinkedList;
import java.util.List;
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
    TextField cuenta;
    TextField descripcion;
    IntegerField numeroTitulos;
    Select sobretasa;
    Select grupoRiesgosEmision;
    DoubleField ponderador;
    
    DateField fechaVencimiento;
    DoubleField precio;
    Select moneda;
    Select calificacion;

    @Override
    public void init() {
        valor = (Valores) getSessionVar("editMapeo");
        form = new Form("form");
        llenarCamposValores();
        llenarCamposVector();
        form.add(new Submit("sub", "Guardar", this, "guardarTenencia"));
        addControl(form);
    }

    public boolean guardarTenencia() {
        if (form.isValid()) {
            Vector vec = new Vector(tipoValor.getValue() + emision.getValue() + serie.getValue(),
                    precio.getDouble(), fechaVencimiento.getDate(), Integer.parseInt(moneda.getValue()), calificacion.getValue(), calificacion.getValue(), calificacion.getValue(), calificacion.getValue(),sobretasa.getValue());
            Map<String, Vector> sessionVar = (Map<String, Vector>) getSessionVar("mapeoVector");
            sessionVar.put(tipoValor.getValue() + emision.getValue() + serie.getValue(), vec);
            addSessionVar("mapeoVector", sessionVar);
            valor.setSerie(serie.getValue());
            valor.setFechaProximoCupon(fechaProximoCupon.getDate());
            valor.setGrupoRc10(vencimiento.getValue());
            valor.setNumeroTitulos(numeroTitulos.getInteger());
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
        fechaEjercicio.setDisabled(true);
        tipoValor = new TextField("tv", "Tipo Valor");
        tipoValor.setDisabled(true);
        emision = new TextField("emi", "Emisora");
        emision.setDisabled(true);
        serie = new TextField("ser", "Serie", true);
        fechaProximoCupon = new DateField("prxCpn", "Fecha Proximo Cupón", true);
        vencimiento = new Select("selven", "Se mantiene a vencimiento", true);
        vencimiento.setDefaultOption(new Option("-1", "Seleccionar"));
        vencimiento.add(new Option("Si", "Si"));
        vencimiento.add(new Option("No", "No"));
        numeroTitulos = new IntegerField("tit", "Número de Títulos", true);
        sobretasa=new Select("stasa","Tiene sobretasa ", true);
        sobretasa.setDefaultOption(new Option("-1", "Seleccionar"));
        sobretasa.add(new Option("Si", "Si"));
        sobretasa.add(new Option("No","No"));
        grupoRiesgosEmision=new Select("remi","Grupo de Riesgo de Emisión", true);
        sobretasa.add(new Option("I","I"));
        sobretasa.add(new Option("II","II"));
        sobretasa.add(new Option("III","III"));
        sobretasa.add(new Option("IV","IV"));
        sobretasa.add(new Option("V","V"));
        sobretasa.add(new Option("VI","VI"));
        sobretasa.add(new Option("VII","II"));
        grupoRiesgosEmision.setDefaultOption(new Option("-1", "Seleccionar"));
        ponderador=new DoubleField("pond", "Ponderador por Riesgo %",3, true);
        fechaEjercicio.setDate(valor.getFecha());
        tipoValor.setValue(valor.getTipoValor());
        emision.setValue(valor.getEmision());
        serie.setValue(valor.getSerie());
        fechaProximoCupon.setDate(valor.getFechaProximoCupon());
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
        fechaVencimiento = new DateField("fecVen", "Fecha de Vencimiento", true);
        moneda = new Select("mon", "Moneda", true);
        moneda.setDefaultOption(new Option("-1","Seleccionar"));
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
        form.add(precio);
        form.add(fechaVencimiento);
        form.add(moneda);
        form.add(calificacion);
    }

}
