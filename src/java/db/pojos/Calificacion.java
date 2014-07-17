package db.pojos;
// Generated 14/07/2014 09:36:55 PM by Hibernate Tools 3.6.0



/**
 * Calificacion generated by hbm2java
 */
public class Calificacion  implements java.io.Serializable {


     private Integer idCalificacion;
     private String calificadora;
     private String calificacion;
     private Integer ponderador;
     private String calificadoraReferencia;
     private String plazo;

    public Calificacion() {
    }

    public Calificacion(String calificadora, String calificacion, Integer ponderador, String calificadoraReferencia, String plazo) {
       this.calificadora = calificadora;
       this.calificacion = calificacion;
       this.ponderador = ponderador;
       this.calificadoraReferencia = calificadoraReferencia;
       this.plazo = plazo;
    }
   
    public Integer getIdCalificacion() {
        return this.idCalificacion;
    }
    
    public void setIdCalificacion(Integer idCalificacion) {
        this.idCalificacion = idCalificacion;
    }
    public String getCalificadora() {
        return this.calificadora;
    }
    
    public void setCalificadora(String calificadora) {
        this.calificadora = calificadora;
    }
    public String getCalificacion() {
        return this.calificacion;
    }
    
    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }
    public Integer getPonderador() {
        return this.ponderador;
    }
    
    public void setPonderador(Integer ponderador) {
        this.ponderador = ponderador;
    }
    public String getCalificadoraReferencia() {
        return this.calificadoraReferencia;
    }
    
    public void setCalificadoraReferencia(String calificadoraReferencia) {
        this.calificadoraReferencia = calificadoraReferencia;
    }
    public String getPlazo() {
        return this.plazo;
    }
    
    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }




}


