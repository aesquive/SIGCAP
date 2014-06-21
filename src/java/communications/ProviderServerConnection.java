/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communications;

import db.controller.DAO;
import db.pojos.Permisos;
import db.pojos.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import manager.configuration.Configuration;
import org.apache.click.Control;
import org.apache.click.control.TextField;

/**
 *
 * @author Admin
 */
public class ProviderServerConnection {

    private Socket socket;
    public static int UNLOCK_USER = 1;
    public static int UNLOCK_MODULE = 2;
    
    private PrintWriter out;
    private BufferedReader in;

    public ProviderServerConnection() {
        try {
            socket = new Socket(Configuration.getValue("urlProveedor"), Integer.parseInt(Configuration.getValue("puertoProveedor")));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            socket = null;
        }
    }

    public String execute(int action, int idModuleUser, String passwordClient) throws Exception {
        System.out.println("Socket abierto");
        String readLine = in.readLine();
        if (readLine.equals("ok")) {
            out.println("act=" + action + "&moduse=" + idModuleUser + "&pass=" + passwordClient + "&client=" + Configuration.getValue("clientId"));
            out.flush();
            String answer = executeAnswer(in.readLine());
            out.println(answer);
            out.flush();
            String byeMessage = in.readLine();
            return answer;
        }
        return "No se pudo comunicar con el Servidor";
    }

    private String executeAnswer(String answer) {
        System.out.println("recibio del server " + answer);
        String[] split = answer.split("&");
        if (split[0].split("%")[1].equals("1")) {
            ejecuteOnDataBase(split[2].split("%")[1]);
        }
        return split[1].split("%")[1];
    }

    public static void main(String[] args) throws Exception {
        ProviderServerConnection pr = new ProviderServerConnection();
        pr.execute(UNLOCK_MODULE,0, "bancoppel");
    }

    private void ejecuteOnDataBase(String received) {
        System.out.println("el sql que se ejecutaria recibido del servidor"+received);
        String[] split = received.split("#");
        if(split[0].equals("user")){
            List<User> createQuery = DAO.createQuery(User.class, null);
            for(User u:createQuery){
                if(u.getUser().toUpperCase().equals(split[2].toUpperCase())){
                    u.setActivo(0);
                    DAO.update(u);
                    DAO.refresh(u);
                    return;
                }
            }
        }else if(split[0].equals("permisos")){
            List<Permisos> createQuery=DAO.createQuery(Permisos.class, null);
            for(Permisos p:createQuery){
                if(p.getIdPermiso()==Integer.parseInt(split[2])){
                    p.setCodigo(split[1]);
                    DAO.update(p);
                    DAO.refresh(p);
                    return;
                }
            }
        }
    }

}
