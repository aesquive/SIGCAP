/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.wrappers;

import java.util.Date;

/**
 *
 * @author desarrollo
 */
public class IngresosNetosWrapper {
    
    private Date fecha;
    private Integer numeroMes;
    private Double ingresoNeto;

    public IngresosNetosWrapper(Date fecha, Integer numeroMes, Double ingresoNeto, Double reqMerCred) {
        this.fecha = fecha;
        this.numeroMes = numeroMes;
        this.ingresoNeto = ingresoNeto;
        this.reqMerCred = reqMerCred;
    }
    private Double reqMerCred;

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
     * @return the numeroMes
     */
    public Integer getNumeroMes() {
        return numeroMes;
    }

    /**
     * @param numeroMes the numeroMes to set
     */
    public void setNumeroMes(Integer numeroMes) {
        this.numeroMes = numeroMes;
    }

    /**
     * @return the ingresoNeto
     */
    public Double getIngresoNeto() {
        return ingresoNeto;
    }

    /**
     * @param ingresoNeto the ingresoNeto to set
     */
    public void setIngresoNeto(Double ingresoNeto) {
        this.ingresoNeto = ingresoNeto;
    }

    /**
     * @return the reqMerCred
     */
    public Double getReqMerCred() {
        return reqMerCred;
    }

    /**
     * @param reqMerCred the reqMerCred to set
     */
    public void setReqMerCred(Double reqMerCred) {
        this.reqMerCred = reqMerCred;
    }
    
}
