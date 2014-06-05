package db.pojos;
// Generated 4/06/2014 10:53:47 PM by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Captacion generated by hbm2java
 */
public class Captacion  implements java.io.Serializable {


     private Integer idCaptacion;
     private Regcuenta regcuenta;
     private Date fecha;
     private Long idCuentaContable;
     private Long idOperacion;
     private String descripcion;
     private Double monto;
     private Date fechaVencimiento;

    public Captacion() {
    }

    public Captacion(Regcuenta regcuenta, Date fecha, Long idCuentaContable, Long idOperacion, String descripcion, Double monto, Date fechaVencimiento) {
       this.regcuenta = regcuenta;
       this.fecha = fecha;
       this.idCuentaContable = idCuentaContable;
       this.idOperacion = idOperacion;
       this.descripcion = descripcion;
       this.monto = monto;
       this.fechaVencimiento = fechaVencimiento;
    }
   
    public Integer getIdCaptacion() {
        return this.idCaptacion;
    }
    
    public void setIdCaptacion(Integer idCaptacion) {
        this.idCaptacion = idCaptacion;
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
    public Long getIdOperacion() {
        return this.idOperacion;
    }
    
    public void setIdOperacion(Long idOperacion) {
        this.idOperacion = idOperacion;
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


