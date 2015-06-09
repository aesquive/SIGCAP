package com.view.page;

import db.controller.DAO;
import db.pojos.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.configuration.Configuration;
import model.executor.ModelExecutor;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Table;
import org.apache.click.extras.control.FormTable;
import util.Util;
import util.Vector;

/**
 *
 * @author Admin
 */
public class MapeoPage extends BorderPage {

    private Form form;
    private FormTable formTable;
    private Regcuenta regcuenta;
    private List<Valores> tenencia;
    private Map<String, Vector> vector;
    private Map<String, Valores> tenenciaMapeada;
    private List<Valores> tenenciaNoMapeada;

    @Override
    public void init() {
        regcuenta = getRegCuenta();
        if (regcuenta == null) {
            message = "Error en la carga del ejercicio para el mapeo";
            setRedirect(BienvenidaPage.class);
            return;
        }
        checarCalculado();
        form = new Form("form");
        formTable = new FormTable("formTable");
        
        title = "Mapeo de Datos " + regcuenta.getDesRegCuenta();
        tenencia = new LinkedList<Valores>(regcuenta.getValoreses());
        //sacar los datos del vector
        vector = generarMapeoVector();
        revisarTenenciaVector();
        if (tenenciaNoMapeada == null || tenenciaNoMapeada.isEmpty()) {
            generadorRc();
            return;
        }
        formTable.setName("dataTable");
        formTable.setPageNumber(0);
        formTable.setClass(Table.CLASS_ORANGE2);
        String[] columnsMethods = Valores.getColumnsMethods();
        String[] columnsDescr = Valores.getColumnsDescriptions();
        for (int t = 0; t < columnsMethods.length; t++) {
            Column c = new Column(columnsMethods[t], columnsDescr[t]);
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
        try {
            cruzarVector(vector, tenenciaMapeada);
            ModelExecutor executor = new ModelExecutor(regcuenta, true);
            executor.start();
            message="Ejercicio calculado correctamente";
            setRedirect(IcapPage.class);
            return true;
        } catch (Exception ex) {
            message = "Sucedio un error al calcular el ejercicio";
            Logger.getLogger(MapeoPage.class.getName()).log(Level.INFO, null, ex);
            return false;
        }
    }

    private void revisarTenenciaVector() {
        tenenciaMapeada = new HashMap<String, Valores>();
        tenenciaNoMapeada = new LinkedList<Valores>();
        for (Valores val : tenencia) {
            String tv = val.getTipoValor() == null ? "" : val.getTipoValor();
            String emision = val.getEmision() == null ? "" : val.getEmision();
            String serie = val.getSerie() == null ? "" : val.getSerie();
            Vector get = vector==null ? null :vector.get(tv + emision + serie);
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
        message=null;
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
            Integer value = get.getMapeada() == null ? 0 : get.getMapeada();
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
            if (valor.getTipoValor().toUpperCase().equals("LD")) {
                valor.setSobretasa("Si");
                doneStasa = true;
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

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

    private Regcuenta getRegCuenta() {
        Regcuenta regSesion = (Regcuenta) getSessionVar("mapeoRegCuenta");
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        for (Regcuenta r : createQuery) {
            if (r.getIdRegCuenta() == regSesion.getIdRegCuenta()) {
                return r;
            }
        }
        return null;
    }

    private Map<String, Vector> generarMapeoVector() {
        try {
            return mapVector();
        } catch (Exception ex) {
            
            Logger.getLogger(MapeoPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    /**
     * mapea el vector de precios
     * @return
     * @throws Exception 
     */
    private Map<String, Vector> mapVector() throws Exception {
        System.out.println("--MAPEANDO VECTOR--");
        Map<String, Vector> mapping = new HashMap<String, Vector>();
        String date = Util.parseDateString("ddMMyyyy",regcuenta.getFecha())+"-"+regcuenta.getIdRegCuenta();
        List<String> fileData = getFileData(Configuration.getValue("RutaVectores")+date+".txt");
        for (String row : fileData) {
            String[] split = row.split(";");
            String concat = split[1] + split[2] + split[3];
            Double precio = Double.parseDouble(split[5]);
            Date fecVenc = parseDate(split[15]);
            int moneda = split[17].toUpperCase().contains("UDI") ? 1 : split[17].toUpperCase().contains("USD") ? 4 : 14;
            String mdy = split[36];
            String fitch = split[53];
            String sp = split[37];
            String hr = split[59];
            String stasa = split[20] == null || split[20].equals("") || split[20].equals("0") || split[20].equals("-") ? "No" : "Si";
            Vector vec = new Vector(concat, precio, fecVenc, moneda, mdy, fitch, sp, hr, stasa);
            mapping.put(concat, vec);
        }
        System.out.println("--VECTOR MAPEADO--");
        return mapping;
    }
    
    
     
    private Date parseDate(String string)  {
        try{
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
        return sp.parse(string);
             
        }catch(Exception ex){
            return null;
        }
    }

    
    /**
     * obtiene la informacion del archivo que se pasa en el campo fileField
     * @param fileField
     * @return
     * @throws IOException 
     */
    public List<String> getFileData(String fileName) throws IOException {
        List<String> lines = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        reader.readLine();
        String linea = reader.readLine();
        while (linea != null) {
            lines.add(linea);
            linea = reader.readLine();
        }
        reader.close();
        return lines;
    }

    /**
     * checa si el ejercicio ya esta calculado, si ya entonces no es necesario hacer nada
     */
    private void checarCalculado() {
        List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
        for(Cuenta c:createQuery){
            if(c.getCatalogocuenta().getIdCatalogoCuenta()==1 && c.getRegcuenta().getIdRegCuenta()==regcuenta.getIdRegCuenta()){
                message="El ejercicio ya fue mapeado correctamente";
                setRedirect(IcapPage.class);
                return;
            }
        }
    }

    
}