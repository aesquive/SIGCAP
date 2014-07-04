package com.view.page;

import db.controller.DAO;
import db.pojos.Cuenta;
import db.pojos.Regcuenta;
import db.pojos.Regcuentauser;
import db.pojos.User;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class IcapPage extends BorderPage {

    public IcapPage() {
        super();
    }

    @Override
    public void init() {
        User varUser = (User) getSessionVar("user");
        List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
        List<Cuenta> data = new LinkedList<Cuenta>();
        for (Cuenta c : createQuery) {
            Regcuenta regcuenta = c.getRegcuenta();
            if (c.getCatalogocuenta().getIdCatalogoCuenta() == 1) {
                data.add(c);
            }
        }
        addSessionVar("icapCounter", 0);
        addSessionVar("icapMaxCounter", 0);
        addSessionVar("icapData-0", data);
        addSessionVar("title-0", "");
        addSessionVar("regCta-0", "Datos");
        setRedirect(TablePage.class);
    }
}
