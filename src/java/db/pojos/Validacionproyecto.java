package db.pojos;
// Generated 24/05/2014 05:59:47 PM by Hibernate Tools 3.6.0



/**
 * Validacionproyecto generated by hbm2java
 */
public class Validacionproyecto  implements java.io.Serializable {


     private Integer idValidacionProyecto;
     private Regcuenta regcuenta;
     private String des;

    public Validacionproyecto() {
    }

    public Validacionproyecto(Regcuenta regcuenta, String des) {
       this.regcuenta = regcuenta;
       this.des = des;
    }
   
    public Integer getIdValidacionProyecto() {
        return this.idValidacionProyecto;
    }
    
    public void setIdValidacionProyecto(Integer idValidacionProyecto) {
        this.idValidacionProyecto = idValidacionProyecto;
    }
    public Regcuenta getRegcuenta() {
        return this.regcuenta;
    }
    
    public void setRegcuenta(Regcuenta regcuenta) {
        this.regcuenta = regcuenta;
    }
    public String getDes() {
        return this.des;
    }
    
    public void setDes(String des) {
        this.des = des;
    }




}


