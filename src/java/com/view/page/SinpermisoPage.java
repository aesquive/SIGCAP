
package com.view.page;

/**
 *
 * @author Admin
 */
public class SinpermisoPage extends BorderPage{
    
    @Override
    public void init() {
        title="Usuario no autorizado";
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }
    
}
