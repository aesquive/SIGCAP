
package model.wrappers;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class TarjetaCreditoWrapper {

        private Date fecha;
        private String cuenta;
        private String credito;
        private String descripcion;
        private Double saldo;
        private Date fechaCorte;
        private String tipoTarjeta;
        private String creditoRelevante;

        
    public TarjetaCreditoWrapper(Date fecha, String cuenta, String credito, String descripcion, Double saldo, Date fechaCorte, String tipoTarjeta, String creditoRelevante) {
        this.fecha = fecha;
        this.cuenta = cuenta;
        this.credito = credito;
        this.descripcion = descripcion;
        this.saldo = saldo;
        this.fechaCorte = fechaCorte;
        this.tipoTarjeta = tipoTarjeta;
        this.creditoRelevante = creditoRelevante;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the cuenta
     */
    public String getCuenta() {
        return cuenta;
    }

    /**
     * @param cuenta the cuenta to set
     */
    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * @return the credito
     */
    public String getCredito() {
        return credito;
    }

    /**
     * @param credito the credito to set
     */
    public void setCredito(String credito) {
        this.credito = credito;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the saldo
     */
    public Double getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the fechaCorte
     */
    public Date getFechaCorte() {
        return fechaCorte;
    }

    /**
     * @param fechaCorte the fechaCorte to set
     */
    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    /**
     * @return the tipoTarjeta
     */
    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    /**
     * @param tipoTarjeta the tipoTarjeta to set
     */
    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    /**
     * @return the creditoRelevante
     */
    public String getCreditoRelevante() {
        return creditoRelevante;
    }

    /**
     * @param creditoRelevante the creditoRelevante to set
     */
    public void setCreditoRelevante(String creditoRelevante) {
        this.creditoRelevante = creditoRelevante;
    }
        
}
