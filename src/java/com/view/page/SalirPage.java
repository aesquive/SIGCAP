package com.view.page;

import db.controller.DAO;
import db.pojos.User;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class SalirPage extends BorderPage {

    @Override
    public void init() {
        User user = (User) getSessionVar("user");
        List<User> createQuery = DAO.createQuery(User.class, null);
        User userQ=null;
        for(User u:createQuery){
            if(u.getUser().toUpperCase().equals(user.getUser().toUpperCase())){
                userQ=u;
            }
        }
        userQ.setActivo(0);
        DAO.saveRecordt(user, "Salio del Sistema");
        DAO.update(userQ);
//        DAO.saveRecordt(userQ, userQ.getUser()+" salió del sistema");
        cleanSession();
        setRedirect("redirect.html");
    }

    @Override
    public Integer getPermisoNumber() {
        return -1;
    }

}
