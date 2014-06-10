package com.view.page;

import db.controller.DAO;
import db.pojos.User;
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
        user.setActivo(0);
        DAO.update(user);
        DAO.saveRecordt(user, user.getUser()+" salio del sistema");
        setRedirect("redirect.html");
    }

}
