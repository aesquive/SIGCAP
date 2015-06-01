/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communications;

import db.controller.DAO;
import db.pojos.Permisos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import manager.configuration.Configuration;

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

    public String execute(int action, String passwordClient) throws Exception {
        System.out.println("Socket abierto");
        String readLine = in.readLine();
        if (readLine.equals("ok")) {
            out.println("act=" + action + "&pass=" + passwordClient + "&client=" + Configuration.getValue("clientId"));
            out.flush();
            String answer = executeAnswer(in.readLine());
            //out.println(answer);
            //out.flush();
            //String byeMessage = in.readLine();
            return answer;
        }
        return "No se pudo comunicar con el Servidor";
    }

    private String executeAnswer(String answer) {
        System.out.println("recibio del server " + answer);
        String[] split = answer.split("&");
        Map<String, String> receivedValues = new HashMap<String, String>();
        for (String variable : split) {
            if (variable != null && !variable.equals("")) {
                receivedValues.put(variable.split("=")[0], variable.split("=")[1]);
            }
        }
        if (receivedValues.get("status").equals("0")) {
            return "Error";
        }
        return executeOnDatabase(receivedValues);
    }

    public static void main(String[] args) throws Exception {
        ProviderServerConnection pr = new ProviderServerConnection();
    }

    private String executeOnDatabase(Map<String, String> map) {
        if (map.get("act").equals("2")) {
            List<Permisos> createQuery = DAO.createQuery(Permisos.class, null);
            for (Permisos permiso : createQuery) {
                String licencia = map.get("mod" + permiso.getIdPermiso());
                permiso.setCodigo(licencia);
            }
            return "Modulos actualizados";
        }
        return "no implementado aun";
    }

}
