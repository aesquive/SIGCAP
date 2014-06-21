package com.view.page;

import db.controller.DAO;
import db.pojos.Permisos;
import db.pojos.Permisosuser;
import db.pojos.User;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import manager.session.SessionController;
import manager.session.Variable;
import org.apache.click.Page;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;
import util.ContextManager;
import util.UserManager;
import util.Util;

public abstract class BorderPage extends Page {

    public String title;
    public String message;
    private Menu rootMenu;
    public ActionLink goBack;
    public ActionLink goForward;
    public Form forwardForm;
    public Form backwardForm;
    public ContextManager context;
    public boolean showPage;
    public static List<String> lic = Util.readFile(manager.configuration.Configuration.getValue("license"));
    public List<String> per;
    public Map<Integer, Boolean> dte;

    public BorderPage() {
        showPage = false;
        checkLic();
        addCommonControls();
        checkSessionVars();
        init();
    }

    /**
     * casa clase que herede debe implementar como iniciar la pantalla
     */
    public abstract void init();

    /**
     * toma el estilo que se usara en comun
     *
     */
    public String getTemplate() {
        return "/border-template.htm";
    }

    /**
     * pone el titulo de la pagina
     */
    public void setTitle(String title) {
        UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).addVariable("title", new Variable("title", title, String.class), true);
    }

    public String getTitle() {
        return UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).getVariable("title").toString();

    }

    private Menu createMenu(String label, String path) {
        Menu menu = new Menu();
        menu.setAccessController(new RoleAccessController());
        menu.setLabel(label);
        menu.setPath(path);
        menu.setTitle(label);
        return menu;
    }

    /**
     * agrega el Menu de inicio y los botones de atras y adelante
     */
    private void addCommonControls() {
        //cargamos el menu
        rootMenu = new Menu("rootMenu");
        SessionController controller = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext);
        User user = (User) controller.getVariable("user").getValue();
        List<Permisosuser> createQuery = DAO.createQuery(Permisosuser.class, null);
        List<Permisosuser> listPermisos = new LinkedList<Permisosuser>();
        for (Permisosuser ps : createQuery) {
            if (ps.getUser().getIduser() == user.getIduser()) {
                DAO.refresh(ps);
                listPermisos.add(ps);
            }
        }
        Collections.sort(listPermisos);
        String[] label = new String[]{"DataWarehouse", "Administrador de Modelos", "Gestion de Capital", "Simulador de Capital",
            "Generador de RC´s", "Auditor", "Administrador de Usuarios"};
        String[] path = new String[]{"warehouse.htm", "administradormodelos.htm", "icap.htm", "whatif.htm", "reportes.htm",
            "auditor.htm", "controlusuarios.htm"};
        for (int t = 0; t < label.length; t++) {
            if (listPermisos.size() > t && listPermisos.get(t).getValor() == 1) {
                //if (isActive(listPermisos.get(t).getPermisos(),fileInfo)) {
                rootMenu.add(createMenu(label[t], path[t]));
                //}
            }
        }
        rootMenu.add(createMenu("Salir", "salir.htm"));
        addControl(rootMenu);

        try {
            if (UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext + 1) != null) {
                forwardForm = new Form("forwardForm");
                addControl(forwardForm);
                goForward = new ActionLink("forwardLink", "", this, "forwardClicked");
                goForward.setName("forwardLink");
                goForward.setId("forwardLink");
                goForward.setImageSrc("/img/forward.png");
                forwardForm.add(goForward);
            }
            if (UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext - 1) != null
                    && UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext != 1) {
                backwardForm = new Form("backwardForm");
                addControl(backwardForm);
                goBack = new ActionLink("backLink", "", this, "backClicked");
                goBack.setImageSrc("/img/back.png");
                goBack.setName("backLink");
                goBack.setId("backLink");
                backwardForm.add(goBack);
            }
        } catch (Exception e) {

        }
    }

    /**
     * evento que se lanza cuando el boton de forward es apretado
     *
     * @return
     */
    public boolean forwardClicked() {
        UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext++;
        Variable variable = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).getVariable("page");
        Class value = (Class) variable.getValue();
        setRedirect(value);
        return true;
    }

    /**
     * evento que se lanza cuando el boton de back es apretado
     *
     * @return
     */
    public boolean backClicked() {
        UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext--;
        Variable variable = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).getVariable("page");
        Class value = (Class) variable.getValue();
        setRedirect(value);
        return true;
    }

    /**
     * evento que se lanza cuando se aprieta salir
     *
     * @return
     */
    public boolean onLogOut() {
        UserManager.clearContext(Integer.parseInt(getContext().getSessionAttribute("user").toString()));
        if (context != null) {
            context.cleanMap();
        }
        getContext().removeSessionAttribute("user");
        return true;
    }

    /**
     * Verifica las variables de sesion y trae los respectivos valores
     */
    private void checkSessionVars() {
        try {

            if (UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).getVariable("title") != null) {
                Variable titlevar = UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).getVariable("title");
                if (titlevar != null) {
                    title = (String) titlevar.getValue();
                }
            }
        } catch (NullPointerException m) {

        }

    }

    public void newContext() {
        SessionController newSessionController = new SessionController();
        UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).getSessionController(UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).actualContext).copySession(newSessionController);
        UserManager.getContextManager(Integer.parseInt(getContext().getSessionAttribute("user").toString())).addSessionController(newSessionController);
    }

    public void javaScriptProcess(Submit submit) {
        submit.setAttribute("onclick", "waitPage();");
    }

    private boolean isActive(Permisos permisos, List<String> values) {
        String text = Util.getAsciiText(permisos.getCodigo(), 2);
        return values.get(permisos.getIdPermiso()).equals(text);
    }

    private void checkLic() {
        List<Permisos> createQuery = DAO.createQuery(Permisos.class, null);
        dte = new HashMap<Integer, Boolean>();
        per = new LinkedList<String>();
        for (Permisos p : createQuery) {
            if (p.getCodigo() != null) {
                try {
                    SimpleDateFormat form = new SimpleDateFormat("yyyyddMM");
                    String asciiText = Util.getAsciiText(p.getCodigo().substring(p.getCodigo().length() - 16, p.getCodigo().length()), 2);
                    System.out.println("lo que se pretende parsear " + asciiText);
                    Date parse = form.parse(asciiText);
                    System.out.println("la date " + parse);
                    if (parse.compareTo(Calendar.getInstance().getTime()) < 0) {
                        dte.put(p.getIdPermiso(), false);
                        per.add(null);
                    } else {
                        per.add(p.getCodigo().substring(0, p.getCodigo().length() - 18));
                        dte.put(p.getIdPermiso(), true);
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                    dte.put(p.getIdPermiso(), true);
                    per.add(p.getCodigo());
                }
            } else {
                dte.put(p.getIdPermiso(), false);
                per.add("");
            }
            System.out.println("la licencia de " + p.getDesPermiso() + " " + per.get(p.getIdPermiso()) + " " + dte.get(p.getIdPermiso()));
        }
    }

}
