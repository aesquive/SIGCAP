package db.pojos;
// Generated 26/06/2014 12:43:36 AM by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Reservas generated by hbm2java
 */
public class Reservas  implements java.io.Serializable {


     private Integer idReservas;
     private Regcuenta regcuenta;
     private Date fecha;
     private String estatusCrediticio;
     private Double monto;

    public Reservas() {
    }

    public Reservas(Regcuenta regcuenta, Date fecha, String estatusCrediticio, Double monto) {
       this.regcuenta = regcuenta;
       this.fecha = fecha;
       this.estatusCrediticio = estatusCrediticio;
       this.monto = monto;
    }
   
    public Integer getIdReservas() {
        return this.idReservas;
    }
    
    public void setIdReservas(Integer idReservas) {
        this.idReservas = idReservas;
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
    public String getEstatusCrediticio() {
        return this.estatusCrediticio;
    }
    
    public void setEstatusCrediticio(String estatusCrediticio) {
        this.estatusCrediticio = estatusCrediticio;
    }
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }




}

