package db.pojos;
// Generated 24/05/2014 05:59:47 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Catalogocuenta generated by hbm2java
 */
public class Catalogocuenta  implements java.io.Serializable {


     private Long idCatalogoCuenta;
     private String desCatalogoCuenta;
     private Set operacions = new HashSet(0);
     private Set cuentas = new HashSet(0);

    public Catalogocuenta() {
    }

	
    public Catalogocuenta(String desCatalogoCuenta) {
        this.desCatalogoCuenta = desCatalogoCuenta;
    }
    public Catalogocuenta(String desCatalogoCuenta, Set operacions, Set cuentas) {
       this.desCatalogoCuenta = desCatalogoCuenta;
       this.operacions = operacions;
       this.cuentas = cuentas;
    }
   
    public Long getIdCatalogoCuenta() {
        return this.idCatalogoCuenta;
    }
    
    public void setIdCatalogoCuenta(Long idCatalogoCuenta) {
        this.idCatalogoCuenta = idCatalogoCuenta;
    }
    public String getDesCatalogoCuenta() {
        return this.desCatalogoCuenta;
    }
    
    public void setDesCatalogoCuenta(String desCatalogoCuenta) {
        this.desCatalogoCuenta = desCatalogoCuenta;
    }
    public Set getOperacions() {
        return this.operacions;
    }
    
    public void setOperacions(Set operacions) {
        this.operacions = operacions;
    }
    public Set getCuentas() {
        return this.cuentas;
    }
    
    public void setCuentas(Set cuentas) {
        this.cuentas = cuentas;
    }




}


