package model.wrappers;

import java.util.Date;
import util.Util;

/**
 *
 * @author Admin
 */
public class CaptacionWrapper {

    private Date fecha;
    private String cuenta;
    private String descripcion;
    private String idCaptacion;
    private Double monto;
    private Date fechaVencimiento;
    private Integer plazo;

    public CaptacionWrapper(Date fecha, String cuenta, String descripcion, String idCaptacion, Double monto, Date fechaVencimiento) {
        this.fecha = fecha;
        this.cuenta = cuenta;
        this.descripcion = descripcion;
        this.idCaptacion = idCaptacion;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
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
     * @return the idCaptacion
     */
    public String getIdCaptacion() {
        return idCaptacion;
    }

    /**
     * @param idCaptacion the idCaptacion to set
     */
    public void setIdCaptacion(String idCaptacion) {
        this.idCaptacion = idCaptacion;
    }

    /**
     * @return the monto
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(Double monto) {
        this.monto = monto;
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
     * @return the plazo
     */
    public Integer getPlazo() {
        return Util.daysBetweenDates(fecha,fechaVencimiento);
    }

    /**
     * @param plazo the plazo to set
     */
    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }
    
}
