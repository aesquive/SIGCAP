package db.pojos;
// Generated 5/05/2014 10:56:00 PM by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Disponibilidad generated by hbm2java
 */
public class Disponibilidad  implements java.io.Serializable {


     private Integer idDisponibilidad;
     private Regcuenta regcuenta;
     private Date fecha;
     private Long idCuentaContable;
     private String descripcion;
     private Double monto;
     private Date fechaVencimiento;

    public Disponibilidad() {
    }

    public Disponibilidad(Regcuenta regcuenta, Date fecha, Long idCuentaContable, String descripcion, Double monto, Date fechaVencimiento) {
       this.regcuenta = regcuenta;
       this.fecha = fecha;
       this.idCuentaContable = idCuentaContable;
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
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Long getIdCuentaContable() {
        return this.idCuentaContable;
    }
    
    public void setIdCuentaContable(Long idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }
    public String getDescripcion() {
        return this.descripcion;
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


