/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.view.page;

import org.apache.click.control.Form;

/**
 *
 * @author Admin
 */
public class AuditorPage extends BorderPage{

    Form form;
    
    @Override
    public void init() {
        form=new Form("form");
        addControl(form);
    }
    
}
