package com.view.page;

import db.controller.DAO;
import db.pojos.Permisosuser;
import db.pojos.User;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import manager.configuration.Configuration;
import org.apache.click.Page;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.RoleAccessController;
import util.Util;

public abstract class BorderPage extends Page {

    //titulo mostrado en la aplicacion (muestra en que ventana estas parado actualmente)
    public String title;
    //almacena la direccion de reportes que tomaran los javascripts
    public static String direccionReportes=Configuration.getValue("direccionReportes");
    //es el mensaje que mostrara el sistema a traves de los javascripts
    public String message;
    //menu principal dentro del sistema
    private Menu rootMenu;
    //flechas para ir atras o adelante
    public ActionLink goBack;
    public ActionLink goForward;
    public Form forwardForm;
    public Form backwardForm;
    //almacena la licencia de usuario
    public static List<String> lic = Util.readFile(manager.configuration.Configuration.getValue("license"));
    public String connecteduser;
    public User user;
    
    public BorderPage() {
        title="Título default";
        
        connectedUser();
        addCommonControls();
        checkPermiso();
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
      
        //creamos el menu principal de toda la aplicacion
        //indica los menus principales a mostrar
        String[] menuPrincipal = new String[]{"DataWarehouse","Generador de Reportes","Gestión de Capital","Auditor","Simulación de Capital", "Administrador de Usuarios"};
        //indica los submenus a mostrar, en caso de que no existan submenus debera ponerse ""
        String[] subMenus=new String[]{"Alta Ejericio|Baja Ejercicio","Generador RC's|Reporte de Tenencia|Reporte de Consistencia|Reporte de Congruencia","","Análisis Comparativo|Tracking Log","","Alta Usuarios|Editar Usuario"};
        //indica la ruta a la que apunta ya sea el menu principal o los submenus si es que los tiene
        String[] path = new String[]{"cargadatos.htm|bajadatos.htm","reportes.htm|menutenencia.htm|reportecons.htm|reportecongruencia.htm", "icap.htm","reportecambios.htm|trackinglog.htm","whatif.htm","altausuarios.htm|editarusuarios.htm"};
        //identificador de los submenus dentro del sistema
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

    
    /**
     * agregar un evento de javascript al hacer submit sobre un boton
     * @param submit 
     */
    public void javaScriptProcess(Submit submit) {
        submit.setAttribute("onclick", "waitPage();");
    }

    /**
     * agrega una variable a la sesion de trabajo
     * @param nameVar
     * @param value 
     */
    public void addSessionVar(String nameVar,Object value){
        getContext().setSessionAttribute(nameVar, value);
    }
    
    /**
     * obtiene una variable de la sesion de trabajo
     * @param name
     * @return 
     */
    public Object getSessionVar(String name){
        return getContext().getSessionAttribute(name);
    }
    
    /**
     * elimina una variable de la sesion de trabajo
     * @param nameVar 
     */
    public void removeSessionVar(String nameVar){
        getContext().removeSessionAttribute(nameVar);
    }
    
    /**
     * limpia todas las variables de la sesion de trabajo
     */
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

    /**
     * obtiene al usuario que esta actualmente loggeado en el sistema
     */
    private void connectedUser() {
        Object sessionVar = getSessionVar("user");
        if(sessionVar!=null){
            user=(User)sessionVar;
            connecteduser=user.getUser();
        }
    }

    public abstract Integer getPermisoNumber();
    
    private void checkPermiso() {
        if(getPermisoNumber()==-1){
            return;
        }
        List<Permisosuser> createQuery = DAO.createQuery(Permisosuser.class, null);
        for(Permisosuser per:createQuery){
            if(per.getUser().getIduser()==user.getIduser() && getPermisoNumber()==per.getPermisos().getIdPermiso() && per.getValor()==1){
                return;
            }
        }
        setRedirect(SinpermisoPage.class);
    }
}
