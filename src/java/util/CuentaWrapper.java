package util;

import db.pojos.Catalogocuenta;
import db.pojos.Cuenta;
import org.apache.click.control.ActionLink;

/**
 *
 * @author Admin
 */
public class CuentaWrapper {

        private Catalogocuenta catalogoCuenta;
        private Cuenta primerCuenta;
        private Cuenta segundaCuenta;
        private ActionLink actionLinkUno;
        private ActionLink actionLinkDos;
        
        
        public CuentaWrapper(Catalogocuenta catalogocuenta,Cuenta primerCuenta,Cuenta segundaCuenta){
            this.catalogoCuenta=catalogocuenta;
            this.primerCuenta=primerCuenta;
            this.segundaCuenta=segundaCuenta;
        }

    /**
     * @return the catalogoCuenta
     */
    public Catalogocuenta getCatalogoCuenta() {
        return catalogoCuenta;
    }

    /**
     * @param catalogoCuenta the catalogoCuenta to set
     */
    public void setCatalogoCuenta(Catalogocuenta catalogoCuenta) {
        this.catalogoCuenta = catalogoCuenta;
    }

    /**
     * @return the primerCuenta
     */
    public Cuenta getPrimerCuenta() {
        return primerCuenta;
    }

    /**
     * @param primerCuenta the primerCuenta to set
     */
    public void setPrimerCuenta(Cuenta primerCuenta) {
        this.primerCuenta = primerCuenta;
    }

    /**
     * @return the segundaCuenta
     */
    public Cuenta getSegundaCuenta() {
        return segundaCuenta;
    }

    /**
     * @param segundaCuenta the segundaCuenta to set
     */
    public void setSegundaCuenta(Cuenta segundaCuenta) {
        this.segundaCuenta = segundaCuenta;
    }

    /**
     * @return the actionLinkCuenta
     */
    public ActionLink getActionLinkUno() {
        return actionLinkUno;
    }

    /**
     * @param actionLinkCuenta the actionLinkCuenta to set
     */
    public void setActionLinkUno(ActionLink actionLinkCuenta) {
        this.actionLinkUno = actionLinkCuenta;
    }

    /**
     * @return the actionLinkDos
     */
    public ActionLink getActionLinkDos() {
        return actionLinkDos;
    }

    /**
     * @param actionLinkDos the actionLinkDos to set
     */
    public void setActionLinkDos(ActionLink actionLinkDos) {
        this.actionLinkDos = actionLinkDos;
    }
    
    public String getCuenta(){
        return this.primerCuenta.getCatalogocuenta().getDesCatalogoCuenta();
    }
    
    public String getPrimerValor(){
        return actionLinkUno.toString();
    }
    
    public String getSegundoValor(){
        return actionLinkDos.toString();
    }
}
