package com.view.page;

import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/**
 *
 * @author desarrollo
 */
public class ConfiguracionponderadoresPage extends BorderPage{

    Form form;
    
    @Override
    public void init() {
        form=new Form("form");
        title="Configuración de Ponderadores";
        form.add(new Submit("hist", "Ponderadores Historicos", this, "historicos"));
        form.add(new Submit("nuevo", "Alta de Ponderadores", this,"altaponderadores"));
        form.add(new Submit("eliminar", "Baja de Ponderadores", this, "bajaponderadores"));
        addControl(form);
    }
    
    /**
     * redirecciona a los historicos de los ponderadores
     * @return 
     */
    public boolean historicos(){
        return true;
    }
    
    /**
     * redirecciona a el alta de ponderadores
     * @return 
     */
    public boolean altaponderadores(){
        setRedirect(AltaponderadoresPage.class);
        return true;
    }
    
    /**
     * redirecciona a la baja de ponderadores
     * @return 
     */
    public boolean bajaponderadores(){
        return true;
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }
    
}
