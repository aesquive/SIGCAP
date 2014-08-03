package util;

import db.controller.DAO;
import db.pojos.Calificacion;
import db.pojos.Valores;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Integer mapeada;

    public Vector(String tvEmiSerie, Double precioSucio, Date fechaVencimiento, int moneda, String moodys, String fitch, String sp, String hr, String stasa) {
        mapeada = 0;
        this.tvEmiSerie = tvEmiSerie;
        this.precioSucio = precioSucio;
        this.fechaVencimiento = fechaVencimiento;
        this.moneda = moneda;
        this.moodys = moodys;
        this.fitch = fitch;
        this.sp = sp;
        this.hr = hr;
        this.sobretasa = stasa;
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
        this.gradoRiesgo = value;
    }

    public void setGrupoRiesgo(String value) {
        this.grupoRiesgo = value;
    }

    public void setPonderador(Double aDouble) {
        this.ponderador = aDouble;
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

    public String getCalificacion(Map<String, Calificacion> calificaciones, Valores valor) {
        Calificacion calSp = null;
        Calificacion calMoodys = null;
        Calificacion calFitch = null;
        Calificacion calHr = null;
        if (sp != null && !sp.equals("NA") && !sp.equals("-")) {
            calSp = calificaciones.get(sp.toUpperCase().trim());
        }
        if (moodys != null && !moodys.equals("NA") && !moodys.equals("-")) {
            calMoodys = calificaciones.get(moodys.toUpperCase().trim());
        }
        if (fitch != null && !fitch.equals("NA") && !fitch.equals("-")) {
            calFitch = calificaciones.get(fitch.toUpperCase().trim());
        }
        if (hr != null && !hr.equals("NA") && !hr.equals("-")) {
            calHr = calificaciones.get(hr.toUpperCase().trim());
        }
        Integer plazoRC02 = valor.getPlazoRC02();
        if (plazoRC02 <= 365) {
            if (calSp != null && calSp.getPlazo().toUpperCase().equals("LARGO")) {
                calSp = null;
            }
            if (calMoodys != null && calMoodys.getPlazo().toUpperCase().equals("LARGO")) {
                calMoodys = null;
            }
            if (calFitch != null && calFitch.getPlazo().toUpperCase().equals("LARGO")) {
                calFitch = null;
            }

            if (calHr != null && calHr.getPlazo().toUpperCase().equals("LARGO")) {
                calHr = null;
            }
        } else {
            if (calSp != null && calSp.getPlazo().toUpperCase().equals("CORTO")) {
                calSp = null;
            }
            if (calMoodys != null && calMoodys.getPlazo().toUpperCase().equals("CORTO")) {
                calMoodys = null;
            }
            if (calFitch != null && calFitch.getPlazo().toUpperCase().equals("CORTO")) {
                calFitch = null;
            }

            if (calHr != null && calHr.getPlazo().toUpperCase().equals("CORTO")) {
                calHr = null;
            }
        }

        int idSp = calSp == null ? 999 : calificaciones.get(calSp.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idMd = calMoodys == null ? 999 : calificaciones.get(calMoodys.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idFi = calFitch == null ? 999 : calificaciones.get(calFitch.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idHR = calHr == null ? 999 : calificaciones.get(calHr.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int firstMin = Math.min(idSp, idMd);
        int secondMin = Math.min(idFi, idHR);
        int min = Math.min(firstMin, secondMin);

        if (min == 999) {

            return getCalificacionSinPlazo(calificaciones, valor);
        }
        if (min == idSp) {
            return calSp == null ? null : calSp.getCalificadoraReferencia();
        } else if (min == idMd) {
            return calMoodys == null ? null : calMoodys.getCalificadoraReferencia();
        } else if (min == idFi) {
            return calFitch == null ? null : calFitch.getCalificadoraReferencia();
        } else if (min == idHR) {
            return calHr == null ? null : calHr.getCalificadoraReferencia();
        }
            return "";
    }

    public static void main(String[] args) {
        Map<String, Calificacion> calificaciones = new HashMap<String, Calificacion>();

        List<Calificacion> createQueryCals = DAO.createQuery(Calificacion.class, null);
        for (Calificacion c : createQueryCals) {
            calificaciones.put(c.getCalificacion(), c);
            calificaciones.put(c.getCalificacion().toUpperCase(), c);
            calificaciones.put(c.getCalificacion().toUpperCase().trim(), c);
        }

        Integer plazoRC02 = 700;
        String sp = "mxAAA";
        String moodys = "";
        String fitch = "AA(mex)";
        String hr = "";
        Calificacion calSp = null;
        Calificacion calMoodys = null;
        Calificacion calFitch = null;
        Calificacion calHr = null;
        if (sp != null && !sp.equals("NA") && !sp.equals("-")) {
            calSp = calificaciones.get(sp.toUpperCase().trim());
        }
        if (moodys != null && !moodys.equals("NA") && !moodys.equals("-")) {
            calMoodys = calificaciones.get(moodys.toUpperCase().trim());
        }
        if (fitch != null && !fitch.equals("NA") && !fitch.equals("-")) {
            calFitch = calificaciones.get(fitch.toUpperCase().trim());
        }
        if (hr != null && !hr.equals("NA") && !hr.equals("-")) {
            calHr = calificaciones.get(hr.toUpperCase().trim());
        }

        if (plazoRC02 <= 365) {
            if (calSp != null && calSp.getPlazo().toUpperCase().equals("LARGO")) {
                calSp = null;
            }
            if (calMoodys != null && calMoodys.getPlazo().toUpperCase().equals("LARGO")) {
                calMoodys = null;
            }
            if (calFitch != null && calFitch.getPlazo().toUpperCase().equals("LARGO")) {
                calFitch = null;
            }

            if (calHr != null && calHr.getPlazo().toUpperCase().equals("LARGO")) {
                calHr = null;
            }
        } else {
            if (calSp != null && calSp.getPlazo().toUpperCase().equals("CORTO")) {
                calSp = null;
            }
            if (calMoodys != null && calMoodys.getPlazo().toUpperCase().equals("CORTO")) {
                calMoodys = null;
            }
            if (calFitch != null && calFitch.getPlazo().toUpperCase().equals("CORTO")) {
                calFitch = null;
            }

            if (calHr != null && calHr.getPlazo().toUpperCase().equals("CORTO")) {
                calHr = null;
            }
        }

        int idSp = calSp == null ? 999 : calificaciones.get(calSp.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idMd = calMoodys == null ? 999 : calificaciones.get(calMoodys.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idFi = calFitch == null ? 999 : calificaciones.get(calFitch.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idHR = calHr == null ? 999 : calificaciones.get(calHr.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int firstMin = Math.min(idSp, idMd);
        int secondMin = Math.min(idFi, idHR);
        int min = Math.min(firstMin, secondMin);

        System.out.println("el min " + min);
        if (min == 999) {

        }
        if (min == idSp) {
            System.out.println(calSp.getCalificadoraReferencia());
        } else if (min == idMd) {

            System.out.println(calMoodys.getCalificadoraReferencia());
        } else if (min == idFi) {

            System.out.println(calFitch.getCalificadoraReferencia());
        } else if (min == idHR) {

            System.out.println(calHr.getCalificadoraReferencia());
        }

    }

    /**
     * @return the mapeada
     */
    public Integer getMapeada() {
        return mapeada;
    }

    /**
     * @param mapeada the mapeada to set
     */
    public void setMapeada(Integer mapeada) {
        this.mapeada = mapeada;
    }

    private String getCalificacionSinPlazo(Map<String, Calificacion> calificaciones, Valores valor) {
        Calificacion calSp = null;
        Calificacion calMoodys = null;
        Calificacion calFitch = null;
        Calificacion calHr = null;
        if (sp != null && !sp.equals("NA") && !sp.equals("-")) {
            calSp = calificaciones.get(sp.toUpperCase().trim());
        }
        if (moodys != null && !moodys.equals("NA") && !moodys.equals("-")) {
            calMoodys = calificaciones.get(moodys.toUpperCase().trim());
        }
        if (fitch != null && !fitch.equals("NA") && !fitch.equals("-")) {
            calFitch = calificaciones.get(fitch.toUpperCase().trim());
        }
        if (hr != null && !hr.equals("NA") && !hr.equals("-")) {
            calHr = calificaciones.get(hr.toUpperCase().trim());
        }
        int idSp = calSp == null ? 999 : calificaciones.get(calSp.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idMd = calMoodys == null ? 999 : calificaciones.get(calMoodys.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idFi = calFitch == null ? 999 : calificaciones.get(calFitch.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int idHR = calHr == null ? 999 : calificaciones.get(calHr.getCalificadoraReferencia().toUpperCase().trim()).getPonderador();
        int firstMin = Math.min(idSp, idMd);
        int secondMin = Math.min(idFi, idHR);
        int min = Math.min(firstMin, secondMin);

        if (min == idSp) {
            return calSp == null ? null : calSp.getCalificadoraReferencia();
        } else if (min == idMd) {
            return calMoodys == null ? null : calMoodys.getCalificadoraReferencia();
        } else if (min == idFi) {
            return calFitch == null ? null : calFitch.getCalificadoraReferencia();
        } else if (min == idHR) {
            return calHr == null ? null : calHr.getCalificadoraReferencia();
        }
        return "";
    }

}
