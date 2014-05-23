/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package file.uploader.vector;

import db.pojos.Calificacion;
import db.pojos.Tipotasa;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Vector {
 
     private Integer idVector;
     private Calificacion calificacionByIdCalificacionHr;
     private Tipotasa tipotasa;
     private Calificacion calificacionByIdCalificacionMoody;
     private Calificacion calificacionByIdCalificacionSp;
     private Calificacion calificacionByIdCalificacionFitch;
     private String idTipoInstrumento;
     private String emisioraInstrumento;
     private String serieInstrumento;
     private Double tasaInstrumento;
     private Double precioSucio;
     private Double precioLimpio;
     private Double duracion;
     private Date fecha;

    public Vector() {
    }

	
    public Vector(Date fecha) {
        this.fecha = fecha;
    }
    public Vector(Calificacion calificacionByIdCalificacionHr, Tipotasa tipotasa, Calificacion calificacionByIdCalificacionMoody, Calificacion calificacionByIdCalificacionSp, Calificacion calificacionByIdCalificacionFitch, String idTipoInstrumento, String emisioraInstrumento, String serieInstrumento, Double tasaInstrumento, Double precioSucio, Double precioLimpio, Double duracion, Date fecha) {
       this.calificacionByIdCalificacionHr = calificacionByIdCalificacionHr;
       this.tipotasa = tipotasa;
       this.calificacionByIdCalificacionMoody = calificacionByIdCalificacionMoody;
       this.calificacionByIdCalificacionSp = calificacionByIdCalificacionSp;
       this.calificacionByIdCalificacionFitch = calificacionByIdCalificacionFitch;
       this.idTipoInstrumento = idTipoInstrumento;
       this.emisioraInstrumento = emisioraInstrumento;
       this.serieInstrumento = serieInstrumento;
       this.tasaInstrumento = tasaInstrumento;
       this.precioSucio = precioSucio;
       this.precioLimpio = precioLimpio;
       this.duracion = duracion;
       this.fecha = fecha;
    }
   
    public Integer getIdVector() {
        return this.idVector;
    }
    
    public void setIdVector(Integer idVector) {
        this.idVector = idVector;
    }
    public Calificacion getCalificacionByIdCalificacionHr() {
        return this.calificacionByIdCalificacionHr;
    }
    
    public void setCalificacionByIdCalificacionHr(Calificacion calificacionByIdCalificacionHr) {
        this.calificacionByIdCalificacionHr = calificacionByIdCalificacionHr;
    }
    public Tipotasa getTipotasa() {
        return this.tipotasa;
    }
    
    public void setTipotasa(Tipotasa tipotasa) {
        this.tipotasa = tipotasa;
    }
    public Calificacion getCalificacionByIdCalificacionMoody() {
        return this.calificacionByIdCalificacionMoody;
    }
    
    public void setCalificacionByIdCalificacionMoody(Calificacion calificacionByIdCalificacionMoody) {
        this.calificacionByIdCalificacionMoody = calificacionByIdCalificacionMoody;
    }
    public Calificacion getCalificacionByIdCalificacionSp() {
        return this.calificacionByIdCalificacionSp;
    }
    
    public void setCalificacionByIdCalificacionSp(Calificacion calificacionByIdCalificacionSp) {
        this.calificacionByIdCalificacionSp = calificacionByIdCalificacionSp;
    }
    public Calificacion getCalificacionByIdCalificacionFitch() {
        return this.calificacionByIdCalificacionFitch;
    }
    
    public void setCalificacionByIdCalificacionFitch(Calificacion calificacionByIdCalificacionFitch) {
        this.calificacionByIdCalificacionFitch = calificacionByIdCalificacionFitch;
    }
    public String getIdTipoInstrumento() {
        return this.idTipoInstrumento;
    }
    
    public void setIdTipoInstrumento(String idTipoInstrumento) {
        this.idTipoInstrumento = idTipoInstrumento;
    }
    public String getEmisioraInstrumento() {
        return this.emisioraInstrumento;
    }
    
    public void setEmisioraInstrumento(String emisioraInstrumento) {
        this.emisioraInstrumento = emisioraInstrumento;
    }
    public String getSerieInstrumento() {
        return this.serieInstrumento;
    }
    
    public void setSerieInstrumento(String serieInstrumento) {
        this.serieInstrumento = serieInstrumento;
    }
    public Double getTasaInstrumento() {
        return this.tasaInstrumento;
    }
    
    public void setTasaInstrumento(Double tasaInstrumento) {
        this.tasaInstrumento = tasaInstrumento;
    }
    public Double getPrecioSucio() {
        return this.precioSucio;
    }
    
    public void setPrecioSucio(Double precioSucio) {
        this.precioSucio = precioSucio;
    }
    public Double getPrecioLimpio() {
        return this.precioLimpio;
    }
    
    public void setPrecioLimpio(Double precioLimpio) {
        this.precioLimpio = precioLimpio;
    }
    public Double getDuracion() {
        return this.duracion;
    }
    
    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public static void main(String[] args) {
        String cad="20131231;S;UDIBONO;401115;517.572561;518.247058;0.674497;4;0;SECRETARÃA DE HACIENDA Y CRÃ‰DITO PÃšBLICO;GUBERNAMENTAL;3500000000;40377100000;24/12/2009;11284;15/11/2040;100;[UDI] Unidades de Inversion (MXN);NA;-;-;Cada 182 dia(s);4;12;Tasa Fija;62;54;520.641082;30/12/2013;NA;-;-;3.819202;3.84277;-;-;Aaa.mx;mxAAA;NA;-;1.01%;-1.44%;729.518175;501.897263;NA;1.67%;2.76%;15.7944604;-16.18080233;375.4423649;-11.180972;0.01%;100;AAA(mex);02/05/2013;04/12/2013;-0.139172486;5795.802523;3.862;-;5795.802523";
         String[] split = cad.split(";");
         for(int t=0;t<split.length;t++){
             System.out.println(t+" "+split[t]);
         }
    }
   
}
