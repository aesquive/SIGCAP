package com.view.page;

import db.controller.DAO;
import db.pojos.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.Vector;

/**
 *
 * @author Admin
 */
public class MapeoPage extends BorderPage {

    private Form form;
    private FormTable formTable;
    private Regcuenta regcuenta;
    private Consistencia consistencia;
    private List<Captacion> captacion;
    private List<Catalogominimo> catalogominimo;
    private List<Disponibilidad> disponibilidades;
    private List<Ingresosnetos> ingresos;
    private List<Prestamo> prestamos;
    private List<Reservas> reservas;
    private List<Tarjetacredito> tarjeta;
    private List<Valores> tenencia;
    private Map<String, Vector> vector;
    private Map<String, Valores> tenenciaMapeada;
    private List<Valores> tenenciaNoMapeada;

    @Override
    public void init() {
        form = new Form("form");
        formTable = new FormTable("formTable");
        consistencia = (Consistencia) getSessionVar("mapeoConsistencia");
        regcuenta = (Regcuenta) getSessionVar("mapeoRegCuenta");
        captacion = (List<Captacion>) getSessionVar("mapeoCaptación");
        catalogominimo = (List<Catalogominimo>) getSessionVar("mapeoCatalogo");
        disponibilidades = (List<Disponibilidad>) getSessionVar("mapeoDisponibilidades");
        ingresos = (List<Ingresosnetos>) getSessionVar("mapeoIngresos");
        prestamos = (List<Prestamo>) getSessionVar("mapeoPrestamos");
        reservas = (List<Reservas>) getSessionVar("mapeoReservas");
        tarjeta = (List<Tarjetacredito>) getSessionVar("mapeoTarjeta");
        tenencia = (List<Valores>) getSessionVar("mapeoTenencia");
        vector = (Map<String, Vector>) getSessionVar("mapeoVector");
        revisarTenenciaVector();
        if (tenenciaNoMapeada == null || tenenciaNoMapeada.isEmpty()) {
            form.add(new Label("lab", "La tenencia esta mapeada correctamente"));
            form.add(new Submit("sub", "Guardar Ejercicio", this, "generadorRc"));
            addControl(form);
            return;
        }
        formTable.setName("dataTable");
        formTable.setPageNumber(0);
        formTable.setClass(Table.CLASS_ORANGE2);
        String[] columnsMethods = Valores.getColumnsMethods();
        String[] columnsDescr = Valores.getColumnsDescriptions();
        for (int t = 0; t < columnsMethods.length; t++) {
            Column c = new Column(columnsMethods[t], columnsDescr[t]);
            c.setWidth("900 px");
            formTable.addColumn(c);
        }
        for (int t = 0; t < tenenciaNoMapeada.size(); t++) {
            ActionLink ac = new ActionLink("ac" + t, "Editar", this, "editarNoMapeada");
            ac.setValue(tenenciaNoMapeada.get(t).getIdTenencia().toString());
            addControl(ac);
            tenenciaNoMapeada.get(t).setEditLink(ac);
        }
        Column edit = new Column("editColumn", "Acción");
        edit.setDecorator(new Decorator() {
            @Override
            public String render(Object object, Context context) {
                Valores val = (Valores) object;
                return val.getEditLink().toString();
            }
        });
        formTable.addColumn(edit);
        FieldSet fs = new FieldSet("fs", "Tenencia no mapeada");
        fs.add(formTable);
        form.add(fs);
        addControl(form);
        formTable.setRowList(tenenciaNoMapeada);
    }

    public boolean generadorRc() {
        cruzarVector(vector, tenenciaMapeada);
        setRedirect(ReportesPage.class);
        return true;
    }

    private void revisarTenenciaVector() {
        tenenciaMapeada = new HashMap<String, Valores>();
        tenenciaNoMapeada = new LinkedList<Valores>();
        for (Valores val : tenencia) {
            String tv = val.getTipoValor() == null ? "" : val.getTipoValor();
            String emision = val.getEmision() == null ? "" : val.getEmision();
            String serie = val.getSerie() == null ? "" : val.getSerie();
            Vector get = vector.get(tv + emision + serie);
            if (get != null && (!get.getSp().equals("-") || !get.getFitch().equals("-") || !get.getMoodys().equals("-") || !get.getHr().equals("-"))) {
                tenenciaMapeada.put(tv + emision + serie, val);
            } else {
                tenenciaNoMapeada.add(val);
            }
        }
    }

    public boolean editarNoMapeada() {
        Valores valorNoMapeado = null;
        for (Valores val : tenenciaNoMapeada) {
            if (val.getEditLink().isClicked()) {
                valorNoMapeado = val;
            }
        }
        addSessionVar("editMapeo", valorNoMapeado);
        String tv = valorNoMapeado.getTipoValor() == null ? "" : valorNoMapeado.getTipoValor();
        String emision = valorNoMapeado.getEmision() == null ? "" : valorNoMapeado.getEmision();
        String serie = valorNoMapeado.getSerie() == null ? "" : valorNoMapeado.getSerie();
        Vector get = vector.get(tv + emision + serie);
        addSessionVar("vectorEditMapeo", get);
        setRedirect(MapeoeditPage.class);
        return true;
    }

    private void cruzarVector(Map<String, Vector> vector, Map<String, Valores> tenenciaMapeada) {
        Set<String> tiposValor = tenenciaMapeada.keySet();
        Map<String, Calificacion> cals = new HashMap<String, Calificacion>();
        List<Calificacion> createQuery = DAO.createQuery(Calificacion.class, null);
        Calificacion defaultCalif = null;
        for (Calificacion c : createQuery) {
            cals.put(c.getCalificacion().toUpperCase(), c);
        }
        defaultCalif = cals.get("MXC");
        Map<String, Emision> emisionesRiesgo = new HashMap<String, Emision>();
        List<Emision> queryEmision = DAO.createQuery(Emision.class, null);
        for (Emision em : queryEmision) {
            String tValorEmision = em.getTipoValor() == null ? "" : em.getTipoValor();
            tValorEmision = em.getEmision() == null ? tValorEmision : tValorEmision + em.getEmision();
            emisionesRiesgo.put(tValorEmision.toUpperCase(), em);
        }
        List<Emisionriesgo> emiRiesgoPonderador = DAO.createQuery(Emisionriesgo.class, null);
        Map<String, Calificacion> califsMap = new HashMap<String, Calificacion>();
        List<Calificacion> createQueryCals = DAO.createQuery(Calificacion.class, null);
        for (Calificacion c : createQueryCals) {
            califsMap.put(c.getCalificacion(), c);
            califsMap.put(c.getCalificacion().toUpperCase(), c);
            califsMap.put(c.getCalificacion().toUpperCase().trim(), c);
        }

        for (String papel : tiposValor) {
            Valores valor = tenenciaMapeada.get(papel);
            Vector get = vector.get(papel);
            Integer value = get.getMapeada()==null ? 0 : get.getMapeada();
            valor.setMapeado(value);
            String moneda = get.getMoneda() == 1 ? "UDI" : "MXN";
            valor.setMoneda(moneda);
            valor.setPrecio(get.getPrecioSucio());
            String[] noStasa = new String[]{"BI", "I", "G", "MC", "SC", "MP", "SP", "3P", "4P", "3U", "4U", "6U", "CC", "IL", "M", "S", "PI", "97", "2P", "2U", "FA", "FB", "FC", "FD", "FI", "FM", "FS", "OA", "OD", "OI"};
            boolean doneStasa = false;
            for (String ns : noStasa) {
                if (ns.toUpperCase().trim().equals(valor.getTipoValor().toUpperCase().trim())) {
                    valor.setSobretasa("NO");
                    doneStasa = true;
                }                
            }
            if(valor.getTipoValor().toUpperCase().equals("LD")){
                valor.setSobretasa("Si");
                doneStasa=true;
            }
            if (!doneStasa) {
                valor.setSobretasa(get.getSobretasa());
            }
            valor.setFechaVencimiento(get.getFechaVencimiento());
            String calstr = get.getCalificacion(califsMap, valor);
            calstr = calstr == null ? "MXC" : calstr;
            Calificacion get1 = cals.get(calstr.toUpperCase());
            get1 = get1 == null ? defaultCalif : get1;
            valor.setCalificacion(get1.getCalificadoraReferencia());
            String grupoRiesgo = get.getGrupoRiesgo() == null ? emisionesRiesgo.get(valor.getTipoValor().toUpperCase() + valor.getEmision().toUpperCase()) == null
                    ? emisionesRiesgo.get(valor.getTipoValor()) == null ? "I" : emisionesRiesgo.get(valor.getTipoValor()).getGrupoRiesgo()
                    : emisionesRiesgo.get(valor.getTipoValor().toUpperCase() + valor.getEmision().toUpperCase()).getGrupoRiesgo() : get.getGrupoRiesgo();
            valor.setGrupoRc07(grupoRiesgo);
            valor.setPlazo(get1.getPlazo());
            if (get.getGradoRiesgo() != null) {
                valor.setGradoRiesgo(Integer.parseInt(get.getGradoRiesgo()));
                valor.setPonderador(get.getPonderador());
            } else {
                valor.setGradoRiesgoPonderador(emiRiesgoPonderador, grupoRiesgo, get.getSp(), get.getMoodys(), get.getFitch(), get.getHr());
            }
            DAO.update(valor);
        }
    }

    public static void main(String[] args) {
        Map<String, Calificacion> cals = new HashMap<String, Calificacion>();
        List<Calificacion> createQuery = DAO.createQuery(Calificacion.class, null);
        for (Calificacion c : createQuery) {
            cals.put(c.getCalificacion(), c);
        }
        Calificacion get = cals.get("MxAA");
        System.out.println(get);
    }
}
