
package com.view.page;

/**
 *
 * @author Admin
 */
public class BienvenidaPage extends BorderPage{

    @Override
    public void init() {       
        title="SIGCAP";
        System.out.println("el mensaje es "+message );
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }
    
}
