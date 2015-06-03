package db.pojos;
// Generated 26/06/2014 12:43:36 AM by Hibernate Tools 3.6.0


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Disponibilidad generated by hbm2java
 */
public class Disponibilidad  implements java.io.Serializable {


     private Integer idDisponibilidad;
     private Regcuenta regcuenta;
     private Catalogocuenta catalogocuenta;
     private Date fecha;
     private String descripcion;
     private Double monto;
     private Date fechaVencimiento;

    public Disponibilidad() {
    }

    public Disponibilidad(Regcuenta regcuenta, Catalogocuenta catalogocuenta, Date fecha, String descripcion, Double monto, Date fechaVencimiento) {
       this.regcuenta = regcuenta;
       this.catalogocuenta = catalogocuenta;
       this.fecha = fecha;
       this.descripcion = descripcion;
       this.monto = monto;
       this.fechaVencimiento = fechaVencimiento;
    }
   
    public Integer getIdDisponibilidad() {
        return this.idDisponibilidad;
    }
    
    public void setIdDisponibilidad(Integer idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }
    public Regcuenta getRegcuenta() {
        return this.regcuenta;
    }
    
    public void setRegcuenta(Regcuenta regcuenta) {
        this.regcuenta = regcuenta;
    }
    public Catalogocuenta getCatalogocuenta() {
        return this.catalogocuenta;
    }
    
    public void setCatalogocuenta(Catalogocuenta catalogocuenta) {
        this.catalogocuenta = catalogocuenta;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public static String[] getDisponibilidadColumns(){
        return new String[]{"IdDisponibilidad","IdCuentaContable","Descripcion","MontoFormato","FechaVencimientoFormato"};
    }
    
    public String getIdCuentaContable(){
        return catalogocuenta.getIdCatalogoCuenta().toString();
    }
    
    public String getMontoFormato(){
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        instance.setMaximumFractionDigits(2);
        try {
            return instance.format(getMonto());
        }catch(Exception e){
            
        }
        return "0";
    }
    
    public String getFechaVencimientoFormato(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(fechaVencimiento);
    }
    
    public static String[] getDisponibilidadColumnsDes(){
        return new String[]{"ID","Cuenta Contable","Descripción","Monto","Fecha de Vencimiento"};
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    public Date getFechaVencimiento() {
        return this.fechaVencimiento;
    }
    
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }




}


