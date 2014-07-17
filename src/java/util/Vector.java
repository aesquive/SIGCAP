
package util;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class Vector {

    private String tvEmiSerie;
    private Double precioSucio;
    private Date fechaVencimiento;
    private int moneda; //14 peso ; 4 usd ; 1 udi
    private String moodys;
    private String fitch;
    private String sp;
    private String hr;
    private String sobretasa;
    private String gradoRiesgo;
    private String grupoRiesgo;
    private Double ponderador;
    
    public Vector(String tvEmiSerie, Double precioSucio, Date fechaVencimiento, int moneda, String moodys, String fitch, String sp, String hr,String stasa) {
        this.tvEmiSerie = tvEmiSerie;
        this.precioSucio = precioSucio;
        this.fechaVencimiento = fechaVencimiento;
        this.moneda = moneda;
        this.moodys = moodys;
        this.fitch = fitch;
        this.sp = sp;
        this.hr = hr;
        this.sobretasa=stasa;
    }

    /**
     * @return the tvEmiSerie
     */
    public String getTvEmiSerie() {
        return tvEmiSerie;
    }

    /**
     * @param tvEmiSerie the tvEmiSerie to set
     */
    public void setTvEmiSerie(String tvEmiSerie) {
        this.tvEmiSerie = tvEmiSerie;
    }

    /**
     * @return the precioSucio
     */
    public Double getPrecioSucio() {
        return precioSucio;
    }

    /**
     * @param precioSucio the precioSucio to set
     */
    public void setPrecioSucio(Double precioSucio) {
        this.precioSucio = precioSucio;
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
     * @return the moneda
     */
    public int getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the moodys
     */
    public String getMoodys() {
        return moodys;
    }

    /**
     * @param moodys the moodys to set
     */
    public void setMoodys(String moodys) {
        this.moodys = moodys;
    }

    /**
     * @return the fitch
     */
    public String getFitch() {
        return fitch;
    }

    /**
     * @param fitch the fitch to set
     */
    public void setFitch(String fitch) {
        this.fitch = fitch;
    }

    /**
     * @return the sp
     */
    public String getSp() {
        return sp;
    }

    /**
     * @param sp the sp to set
     */
    public void setSp(String sp) {
        this.sp = sp;
    }

    /**
     * @return the hr
     */
    public String getHr() {
        return hr;
    }

    /**
     * @param hr the hr to set
     */
    public void setHr(String hr) {
        this.hr = hr;
    }

    public void setGradoRiesgo(String value) {
        this.gradoRiesgo=value;
    }

    public void setGrupoRiesgo(String value) {
        this.grupoRiesgo=value;
    }

    public void setPonderador(Double aDouble) {
        this.ponderador=aDouble;
    }

    /**
     * @return the sobretasa
     */
    public String getSobretasa() {
        return sobretasa;
    }

    /**
     * @param sobretasa the sobretasa to set
     */
    public void setSobretasa(String sobretasa) {
        this.sobretasa = sobretasa;
    }

    /**
     * @return the gradoRiesgo
     */
    public String getGradoRiesgo() {
        return gradoRiesgo;
    }

    /**
     * @return the grupoRiesgo
     */
    public String getGrupoRiesgo() {
        return grupoRiesgo;
    }

    /**
     * @return the ponderador
     */
    public Double getPonderador() {
        return ponderador;
    }

    public String getCalificacion() {
        if(sp!=null && !sp.equals("NA") && !sp.equals("-")){
            return sp;
        }
        if(fitch!=null && !fitch.equals("NA") && !fitch.equals("-")){
            return fitch;
        }
        
        if(moodys!=null && !moodys.equals("NA") && !moodys.equals("-")){
            return moodys;
        }
        
        else{
            return "MxAA";
        }
    }
    
    
    
}
