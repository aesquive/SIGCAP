package db.pojos;
// Generated 4/06/2014 10:53:47 PM by Hibernate Tools 3.6.0

import java.text.NumberFormat;
import java.util.Locale;
import org.apache.click.control.ActionLink;


/**
 * Cuenta generated by hbm2java
 */
public class Cuenta  implements java.io.Serializable,Cloneable {


     private Long idCuenta;
     private Moneda moneda;
     private Regcuenta regcuenta;
     private Catalogocuenta catalogocuenta;
     private Double valor;
     private String ref;
     private Integer status;

         private ActionLink actionLink;
    private ActionLink editLink;

     
    public Cuenta() {
    }

	
    public Cuenta(Regcuenta regcuenta, Catalogocuenta catalogocuenta) {
        this.regcuenta = regcuenta;
        this.catalogocuenta = catalogocuenta;
    }
    public Cuenta(Moneda moneda, Regcuenta regcuenta, Catalogocuenta catalogocuenta, Double valor, String ref, Integer status) {
       this.moneda = moneda;
       this.regcuenta = regcuenta;
       this.catalogocuenta = catalogocuenta;
       this.valor = valor;
       this.ref = ref;
       this.status = status;
    }
   
    public Long getIdCuenta() {
        return this.idCuenta;
    }
    
    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }
    public Moneda getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    public Regcuenta getRegcuenta() {
        return this.regcuenta;
    }
    
    public void setRegcuenta(Regcuenta regcuenta) {
        this.regcuenta = regcuenta;
    }
    public Catalogocuenta getCatalogocuenta() {
        return this.catalogocuenta;
    }
    
    public void setCatalogocuenta(Catalogocuenta catalogocuenta) {
        this.catalogocuenta = catalogocuenta;
    }
    public Double getValor() {
        return this.valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public String getRef() {
        return this.ref;
    }
    
    public void setRef(String ref) {
        this.ref = ref;
    }
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getEditStatus() {
        if (ref == null || ref.equals("")) {
            return "Editar";
        }
        return "";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getDetalle() {
        return this.catalogocuenta.getDesCatalogoCuenta();
    }

    public String getResultado() {
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        instance.setMaximumFractionDigits(2);
        return instance.format(getValor());

    }

    public static void main(String[] args) {
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        System.out.println(instance.format(534234.123));
    }

    public String getEjercicio() {
        return this.getRegcuenta().getDesRegCuenta();
    }


    
    public static String[] getColumns() {
        return new String[]{"Ejercicio", "Detalle", "Resultado*"};
    }

    public static String[] getSimulationColumns() {
        return new String[]{"Ejercicio", "Detalle", "Resultado*", "Accion?"};
    }

    /**
     * @return the actionLink
     */
    public ActionLink getActionLink() {
        return actionLink;
    }

    /**
     * @param actionLink the actionLink to set
     */
    public void setActionLink(ActionLink actionLink) {
        this.actionLink = actionLink;
    }

    public ActionLink getEditLink() {
        return editLink;
    }

    public void setEditLink(ActionLink editLink) {
        this.editLink = editLink;
    }


}


