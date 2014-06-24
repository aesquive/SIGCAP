package com.view.page;

import db.controller.DAO;
import db.pojos.User;
import java.util.List;
import manager.session.SessionController;
import util.UserManager;

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
        SessionController controller = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext);
        User user = (User) controller.getVariable("user").getValue();
        List<User> createQuery = DAO.createQuery(User.class, null);
        User userQ=null;
        for(User u:createQuery){
            if(u.getUser().toUpperCase().equals(user.getUser().toUpperCase())){
                userQ=u;
            }
        }
        userQ.setActivo(0);
        DAO.update(userQ);
//        DAO.saveRecordt(userQ, userQ.getUser()+" salió del sistema");
        setRedirect("redirect.html");
    }

}
