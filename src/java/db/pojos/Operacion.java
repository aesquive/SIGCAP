package db.pojos;
// Generated 5/05/2014 10:56:00 PM by Hibernate Tools 3.6.0



/**
 * Operacion generated by hbm2java
 */
public class Operacion  implements java.io.Serializable {


     private Integer idOperacion;
     private Catalogocuenta catalogocuenta;
     private String valOperacion;

    public Operacion() {
    }

    public Operacion(Catalogocuenta catalogocuenta, String valOperacion) {
       this.catalogocuenta = catalogocuenta;
       this.valOperacion = valOperacion;
    }
   
    public Integer getIdOperacion() {
        return this.idOperacion;
    }
    
    public void setIdOperacion(Integer idOperacion) {
        this.idOperacion = idOperacion;
    }
    public Catalogocuenta getCatalogocuenta() {
        return this.catalogocuenta;
    }
    
    public void setCatalogocuenta(Catalogocuenta catalogocuenta) {
        this.catalogocuenta = catalogocuenta;
    }
    public String getValOperacion() {
        return this.valOperacion;
    }
    
    public void setValOperacion(String valOperacion) {
        this.valOperacion = valOperacion;
    }




}


