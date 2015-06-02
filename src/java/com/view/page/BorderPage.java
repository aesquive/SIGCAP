package com.view.page;

import db.controller.DAO;
import db.pojos.Permisos;
import db.pojos.Permisosuser;
import db.pojos.User;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.click.Page;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;
import util.Util;

public abstract class BorderPage extends Page {

    public String title;
    public String message;
    private Menu rootMenu;
    public ActionLink goBack;
    public ActionLink goForward;
    public Form forwardForm;
    public Form backwardForm;
    public boolean showPage;
    public static List<String> lic = Util.readFile(manager.configuration.Configuration.getValue("license"));
    public List<String> per;
    public Map<Integer, Boolean> dte;
    public String connecteduser;
    
    public BorderPage() {
        showPage = false;
        title="Título default";
        checkLic();
        connectedUser();
        addCommonControls();
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
        User user = (User) getSessionVar("user");
        List<Permisosuser> createQuery = DAO.createQuery(Permisosuser.class, null);
        List<Permisosuser> listPermisos = new LinkedList<Permisosuser>();
        for (Permisosuser ps : createQuery) {
            if (ps.getUser().getIduser() == user.getIduser()) {
 //               DAO.refresh(ps);
                listPermisos.add(ps);
            }
        }
        Collections.sort(listPermisos);

        //creamos el menu
        String[] menuPrincipal = new String[]{"DataWarehouse","Generador de Reportes","Gestión de Capital","Auditor","Simulación de Capital", "Administrador de Usuarios"};
        String[] subMenus=new String[]{"Alta Ejericio|Baja Ejercicio","Generador RC's|Reporte de Tenencia|Reporte de Consistencia|Reporte de Congruencia","","Análisis Comparativo|Tracking Log","","Alta Usuarios|Editar Usuario"};
        String[] path = new String[]{"cargadatos.htm|bajadatos.htm","reportes.htm|menutenencia.htm|reportecons.htm|reportecongruencia.htm", "icap.htm","reportecambios.htm|trackinglog.htm","whatif.htm","altausuarios.htm|editarusuarios.htm"};
        String[] identSUbs=new String[]{"1|2","3|4|5|6","","7|8","","9|10"};
        for (int t = 0; t < menuPrincipal.length; t++) {
                Menu pestania=null;
                if(subMenus[t].equals("")){
                    pestania=createMenu(menuPrincipal[t], path[t]);
                }else{
                   pestania=createMenu(menuPrincipal[t], "#");
                    String[] splitSubmenus = subMenus[t].split("\\|");
                    String[] splitSubsIds=identSUbs[t].split("\\|");
                    String[] splitPaths=path[t].split("\\|");
                    for(int x=0;x<splitSubmenus.length;x++){
                       Menu createMenu = createMenu(splitSubmenus[x], splitPaths[x]);
                       createMenu.setId("sub"+splitSubsIds[x]);
                       pestania.add(createMenu);
                    }
                }
                pestania.setAttribute("onmouseup", "mostrarSubmenu('"+identSUbs[t]+"')");
                rootMenu.add(pestania);
        }
        
        
        addControl(rootMenu);

    }

    
    
    public void javaScriptProcess(Submit submit) {
        submit.setAttribute("onclick", "waitPage();");
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
                    Date parse = form.parse(asciiText);
                    if (parse.compareTo(Calendar.getInstance().getTime()) < 0) {
                        dte.put(p.getIdPermiso()-1, false);
                        per.add(null);
                    } else {
                        per.add(p.getCodigo().substring(0, p.getCodigo().length() - 18));
                        dte.put(p.getIdPermiso()-1, true);
                    }
                } catch (Exception ex) {
                    dte.put(p.getIdPermiso()-1, true);
                    per.add(p.getCodigo());
                }
            } else {
                dte.put(p.getIdPermiso()-1, false);
                per.add("");
            }
        }
    }

    public void addSessionVar(String nameVar,Object value){
        getContext().setSessionAttribute(nameVar, value);
    }
    
    public Object getSessionVar(String name){
        return getContext().getSessionAttribute(name);
    }
    
    public void removeSessionVar(String nameVar){
        getContext().removeSessionAttribute(nameVar);
    }
    
    public void cleanSession(){
        Enumeration<String> attributeNames = getContext().getSession().getAttributeNames();
        List<String> elms=new LinkedList<String>();
        while(attributeNames.hasMoreElements()){
            elms.add(attributeNames.nextElement());
        }
        for(String s:elms){
            removeSessionVar(s);
        }
    }

    private void connectedUser() {
        Object sessionVar = getSessionVar("user");
        if(sessionVar!=null){
            User u=(User)sessionVar;
            connecteduser=u.getUser();
        }
    }
}
