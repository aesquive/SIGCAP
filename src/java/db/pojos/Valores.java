package db.pojos;
// Generated 3/07/2014 02:02:56 PM by Hibernate Tools 3.6.0

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.click.control.ActionLink;
import util.Util;

/**
 * Valores generated by hbm2java
 */
public class Valores implements java.io.Serializable {

    
    public void setEditLink(ActionLink ac) {
        this.editLink = ac;
    }

    public ActionLink getEditLink() {
        return editLink;
    }

    
    

    private ActionLink editLink;

    private Integer idTenencia;
    private Regcuenta regcuenta;
    private Date fecha;
    private String idCuentaContable;
    private String descripcion;
    private Integer numeroTitulos;
    private String tipoValor;
    private String emision;
    private String serie;
    private Date fechaProximoCupon;
    private String grupoRc10;
    private Double precio;
    private String sobretasa;
    private String calificacion;
    private String grupoRc07;
    private Double ponderador;
    private String plazo;
    private Date fechaVencimiento;
    private String moneda;
    private Integer gradoRiesgo;
    
    public Valores() {
    }

    public Valores(Regcuenta regcuenta, Date fecha, String idCuentaContable, String descripcion, Integer numeroTitulos, String tipoValor, String emision, String serie, Date fechaProximoCupon, String grupoRc10, Double precio, String sobretasa, String calificacion, String grupoRc07, Double ponderador, String plazo,Date fechaVencimiento,String moneda,Integer gradoRiesgo) {
        this.regcuenta = regcuenta;
        this.fecha = fecha;
        this.idCuentaContable = idCuentaContable;
        this.descripcion = descripcion;
        this.numeroTitulos = numeroTitulos;
        this.tipoValor = tipoValor;
        this.emision = emision;
        this.serie = serie;
        this.fechaProximoCupon = fechaProximoCupon;
        this.grupoRc10 = grupoRc10;
        this.precio = precio;
        this.sobretasa = sobretasa;
        this.calificacion = calificacion;
        this.grupoRc07 = grupoRc07;
        this.ponderador = ponderador;
        this.plazo = plazo;
        this.fechaVencimiento=fechaVencimiento;
        this.moneda=moneda;
        this.gradoRiesgo=gradoRiesgo;
    }

    public Integer getIdTenencia() {
        return this.idTenencia;
    }

    public void setIdTenencia(Integer idTenencia) {
        this.idTenencia = idTenencia;
    }

    public Regcuenta getRegcuenta() {
        return this.regcuenta;
    }

    public void setRegcuenta(Regcuenta regcuenta) {
        this.regcuenta = regcuenta;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdCuentaContable() {
        return this.idCuentaContable;
    }

    public void setIdCuentaContable(String idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getNumeroTitulos() {
        return this.numeroTitulos;
    }

    public void setNumeroTitulos(Integer numeroTitulos) {
        this.numeroTitulos = numeroTitulos;
    }

    public String getTipoValor() {
        return this.tipoValor;
    }

    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getEmision() {
        return this.emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getSerie() {
        return this.serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Date getFechaProximoCupon() {
        return this.fechaProximoCupon;
    }

    public void setFechaProximoCupon(Date fechaProximoCupon) {
        this.fechaProximoCupon = fechaProximoCupon;
    }

    public String getGrupoRc10() {
        return this.grupoRc10;
    }

    public void setGrupoRc10(String grupoRc10) {
        this.grupoRc10 = grupoRc10;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getSobretasa() {
        return this.sobretasa;
    }

    public void setSobretasa(String sobretasa) {
        this.sobretasa = sobretasa;
    }

    public String getCalificacion() {
        return this.calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getGrupoRc07() {
        return this.grupoRc07;
    }

    public void setGrupoRc07(String grupoRc07) {
        this.grupoRc07 = grupoRc07;
    }

    public Double getPonderador() {
        return this.ponderador;
    }

    public void setPonderador(Double ponderador) {
        this.ponderador = ponderador;
    }

    public String getPlazo() {
        return this.plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    
    public Valores(Regcuenta regcuenta, Date fecha, String idCuentaContable, String descripcion, Integer numeroTitulos, String tipoValor, String emision, String serie, Date fechaProximoCupon, String grupoRc10) {
        this.idCuentaContable=idCuentaContable;
        this.regcuenta = regcuenta;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.numeroTitulos = numeroTitulos;
        this.tipoValor = tipoValor;
        this.emision = emision;
        this.serie = serie;
        this.fechaProximoCupon = fechaProximoCupon;
        this.grupoRc10 = grupoRc10;
    }

    public static String[] getColumnsMethods() {
        return new String[]{"FechaTexto", "TipoValor", "Emision", "Serie", "NumeroTitulosFormato"};
    }

    public String getFechaTexto() {
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
        return sp.format(getFecha());
    }

    public String getNumeroTitulosFormato() {
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        instance.setMaximumFractionDigits(2);
        return instance.format(getNumeroTitulos());
    }

    public static String[] getColumnsDescriptions() {
        return new String[]{"Fecha", "Tipo Valor", "Emisora", "Serie", "Numero de Títulos"};
    }
    
    
    public  Double getMonto() {
        return this.numeroTitulos*precio;
    }
    
    public Integer getPlazoRC01(){
        return Util.daysBetweenDates(fecha,fechaProximoCupon);
    }
    
    public Integer getPlazoRC02(){
        return Util.daysBetweenDates(fecha,fechaVencimiento);
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the gradoRiesgo
     */
    public Integer getGradoRiesgo() {
        return gradoRiesgo;
    }

    /**
     * @param gradoRiesgo the gradoRiesgo to set
     */
    public void setGradoRiesgo(Integer gradoRiesgo) {
        this.gradoRiesgo = gradoRiesgo;
    }
}
