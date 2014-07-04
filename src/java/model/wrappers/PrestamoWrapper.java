
package model.wrappers;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class PrestamoWrapper {
    
        private Date fecha;
        private String cuenta;
        private String prestamo;
        private String descripcion;
        private Double saldo;
        private Date fechaCorte;
        private String tipoPrestamo;
        private String prestamoRelevante;

    public PrestamoWrapper(Date fecha, String cuenta, String prestamo, String descripcion, Double saldo, Date fechaCorte, String tipoPrestamo, String prestamoRelevante) {
        this.fecha = fecha;
        this.cuenta = cuenta;
        this.prestamo = prestamo;
        this.descripcion = descripcion;
        this.saldo = saldo;
        this.fechaCorte = fechaCorte;
        this.tipoPrestamo = tipoPrestamo;
        this.prestamoRelevante = prestamoRelevante;
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
     * @return the prestamo
     */
    public String getPrestamo() {
        return prestamo;
    }

    /**
     * @param prestamo the prestamo to set
     */
    public void setPrestamo(String prestamo) {
        this.prestamo = prestamo;
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
     * @return the tipoPrestamo
     */
    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    /**
     * @param tipoPrestamo the tipoPrestamo to set
     */
    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    /**
     * @return the prestamoRelevante
     */
    public String getPrestamoRelevante() {
        return prestamoRelevante;
    }

    /**
     * @param prestamoRelevante the prestamoRelevante to set
     */
    public void setPrestamoRelevante(String prestamoRelevante) {
        this.prestamoRelevante = prestamoRelevante;
    }

        
}
