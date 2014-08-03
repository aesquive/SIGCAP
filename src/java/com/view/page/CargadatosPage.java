package com.view.page;

import db.controller.DAO;
import db.pojos.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SizeRequirements;
import manager.configuration.Configuration;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.commons.fileupload.FileItem;
import util.Util;
import util.Vector;

/**
 *
 * @author Admin
 */
public class CargadatosPage extends BorderPage {

    Form form;
    FileField fileTenencia;
    FileField fileCaptacion;
    FileField filePrestamosPersonales;
    FileField fileReservas;
    FileField fileDisponibilidades;
    FileField fileTarjetaCredito;
    FileField fileCatalogoMinimo;
    FileField fileIngresos;
    FileField vector;
    TextField name;
    DateField dateField;
    private static int numPer = 0;
    private List<Catalogocuenta> catalogoCuenta;

    @Override
    public void init() {
        catalogoCuenta=DAO.createQuery(Catalogocuenta.class, null);
        this.form = new Form("form");
//        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) || !dte.get(numPer)) {
//            setRedirect(NocontratadoPage.class);
//            return;
//        }
        name = new TextField("name", "Nombre del Ejercicio", true);
        dateField = new DateField("dateField", "Fecha de Ejercicio (dd/mm/aaaa)", true);
        dateField.setFormatPattern("dd/MM/yyyy");
        fileTenencia = new FileField("fileTenencia", "Tenencia (csv|txt)", true);
        fileCaptacion = new FileField("fileCaptacion", "Captación (csv|txt)", true);
        filePrestamosPersonales = new FileField("filePrestamos", "Prestamos Personales (csv|txt)", true);
        fileDisponibilidades = new FileField("fileDisponibilidades", "Disponibilidades (csv|txt)", true);
        fileTarjetaCredito = new FileField("fileTarjeta", "Tarjeta de Crédito  (csv|txt)", true);
        fileReservas = new FileField("fileConsumo", "Reservas(csv|txt)", true);
        fileIngresos = new FileField("fileIngreso", "Ingresos Netos (csv|txt)", true);
        vector = new FileField("vector", "Vector Analitico (csv|txt)", true);
        form.add(name);
        form.add(dateField);
        form.add(fileTenencia);
        form.add(fileCaptacion);
        form.add(filePrestamosPersonales);
        form.add(fileDisponibilidades);
        form.add(fileReservas);
        form.add(fileTarjetaCredito);
        form.add(fileIngresos);
        fileCatalogoMinimo = new FileField("fileCatalogo", "Catálogo Mínimo", true);
        form.add(fileCatalogoMinimo);
        form.add(vector);
        Submit sub = new Submit("sub", "Procesar", this, "procesarClicked");
        javaScriptProcess(sub);
        form.add(sub);

        addControl(form);

    }

    public boolean procesarClicked() {
        if (form.isValid()) {
            try {
                List<Regcuenta> createQueryRg = DAO.createQuery(Regcuenta.class, null);
                for(Regcuenta r:createQueryRg){
                    if(r.getDesRegCuenta().trim().toUpperCase().equals(name.getValue().trim().toUpperCase())){
                        message="Favor de utilizar otro nombre de ejercicio (Repetido)";
                        return false;
                    }
                }
                message = "Reportes Invalidos (Número de Campos) : ";
                FileField[] camposArchivos = new FileField[]{fileCaptacion, fileCatalogoMinimo, fileDisponibilidades, fileIngresos, filePrestamosPersonales, fileReservas, fileTarjetaCredito, fileTenencia};
                String[] nombres = new String[]{"Captación", "Catálogo Mínimo", "Disponibilidades", "Ingresos Netos", "Prestamos Personales", "Reservas", "Tarjeta de Crédito", "Tenencia"};
                //vALIDA EL NUMERO DE CAMPOS QUE TRAE EL ARCHIVO ... SI ESTE NO ES CORRECTO SACA AL USUARIO DE LA CARGA
                Integer[] numCampos = new Integer[]{6, 4, 5, 3, 8, 3, 8, 9};
                boolean pass = true;
                for (int t = 0; t < nombres.length; t++) {
                    List<String> dataCatalogoMinimo = getFileData(camposArchivos[t]);
                    boolean validarNumeroCampos = validNumberFields(dataCatalogoMinimo, numCampos[t]);
                    if (!validarNumeroCampos) {
                        message = message + nombres[t] + ", ";
                        pass = false;
                    }
                }
                if (!pass) {
                    return false;
                }
                //salvamos el proyecto que registra nombre y fecha
                Regcuenta regCuenta = saveProject();
                User user = (User) getSessionVar("user");
                saveUserRelation(regCuenta, user);
                //Generamos una nueva consistencia , para poner los resultados de los reportes leidos
                Consistencia cons = new Consistencia();
                cons.setRegcuenta(regCuenta);
                List<Object> saveCaptacion = saveCaptacion(cons, regCuenta);
                List<Object> saveCatalogoMinimo = saveCatalogoMinimo(cons, regCuenta);
                List<Object> saveDisponibilidades = saveDisponibilidades(cons, regCuenta);
                List<Object> saveIngresos = saveIngresos(cons, regCuenta);
                List<Object> savePrestamos = savePrestamos(cons, regCuenta);
                List<Object> saveReservas = saveReservas(cons, regCuenta);
                List<Object> saveTarjeta = saveTarjeta(cons, regCuenta);
                List<Object> saveTenencia = saveTenencia(cons, regCuenta);
                Map<String, Vector> mapVector = mapVector();
                saveAll(saveCaptacion, saveCatalogoMinimo, saveDisponibilidades, saveIngresos, savePrestamos, saveReservas, saveTarjeta, saveTenencia);
                DAO.saveRecordt(user, user.getUser() + "generó alta del ejercicio " + regCuenta.getDesRegCuenta());
                DAO.save(cons);
                addSessionVar("mapeoConsistencia", cons);
                addSessionVar("mapeoRegCuenta", regCuenta);
                addSessionVar("mapeoCaptación", saveCaptacion);
                addSessionVar("mapeoCatalogo", saveCatalogoMinimo);
                addSessionVar("mapeoDisponibilidades", saveDisponibilidades);
                addSessionVar("mapeoIngresos", saveIngresos);
                addSessionVar("mapeoPrestamos", savePrestamos);
                addSessionVar("mapeoReservas", saveReservas);
                addSessionVar("mapeoTarjeta", saveTarjeta);
                addSessionVar("mapeoTenencia", saveTenencia);
                addSessionVar("mapeoVector", mapVector);
                setRedirect(MapeoPage.class);
                message = "Carga Completa";
                return true;
            } catch (Exception ex) {
                System.out.println(ex);
                message = "Algún error ha ocurrido";
                return false;
            }
        }
        return false;
    }

    public List<String> getFileData(FileField fileField) throws IOException {
        FileItem fileItem = fileField.getFileItem();
        List<String> lines = new LinkedList<String>();
        InputStream inp = fileItem.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
        String linea = reader.readLine();
        while (linea != null) {
            lines.add(linea);
            linea = reader.readLine();
        }
        reader.close();
        inp.close();
        return lines;
    }

    private boolean validNumberFields(List<String> dataCatalogoMinimo, int i) {
        try {

            if (dataCatalogoMinimo.get(0).split(";").length == i) {

                return true;
            }
            return false;
        } catch (Exception e) {
            message = "Alguno de los archivos esta vacio";
            return false;
        }
    }

    private Regcuenta saveProject() {
        Regcuenta regcuenta = new Regcuenta(name.getValue());
        regcuenta.setFecha(dateField.getDate());
        DAO.save(regcuenta);
        return regcuenta;
    }

    private void saveUserRelation(Regcuenta regCuenta, User user) {
        Regcuentauser regcuentauser = new Regcuentauser(regCuenta, user);
        DAO.save(regcuentauser);
    }

    private List<Object> saveCaptacion(Consistencia cons, Regcuenta reg) {
        try {
            System.out.println("---COMIENZA CAPTACION");
            List<String> captaciones = getFileData(fileCaptacion);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    System.out.println(s);
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
                    String des = split[2];
                    Long idCapt = parseLong(split[3]);
                    Double valor = parseDouble(split[4]);
                    Date venc = parseDate(split[5]);
                    Captacion cap = new Captacion(reg, cuenta, date, des, idCapt, valor, venc);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("registro de captación no guardado");
                }
            }
            System.out.println("-----TERMINO CAPTACION---");
            cons.setCaptacionLeidos(new Double(String.valueOf(captaciones.size())));
            return items;
        } catch (Exception ex) {
            removeData(reg.getCaptacions());
        }
        return null;
    }

    private List<Object> saveCatalogoMinimo(Consistencia cons, Regcuenta reg) {
        try {
            System.out.println("--COMIENTZA CAT MINIMO---");
            List<String> captaciones = getFileData(fileCatalogoMinimo);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
                    Double value = parseDouble(split[2]);
                    int moneda = Integer.parseInt(split[3]);
                    Catalogominimo cap = new Catalogominimo(reg, cuenta, date, value, moneda);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println("registro no guardado");
                }
            }
            cons.setCatalogoMinimoLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("---TERMINA CAT MINIMO---");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private List<Object> saveDisponibilidades(Consistencia cons, Regcuenta regCuenta) {
        try {
            System.out.println("---COMIENZA DISPONIBILIDADES--");
            List<String> captaciones = getFileData(fileDisponibilidades);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
                    String des = split[2];
                    Double value = parseDouble(split[3]);
                    Date venc = parseDate(split[4]);
                    Disponibilidad cap = new Disponibilidad(regCuenta, cuenta, date, des, value, venc);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println("registro no guardado");
                }
            }
            cons.setDisponibilidadesLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("--TERMINA DISPONIBILIDADES---");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private List<Object> saveIngresos(Consistencia cons, Regcuenta regCuenta) {
        try {
            System.out.println("---COMIENZA INGRESOS---");
            List<String> captaciones = getFileData(fileIngresos);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    int cuenta = Integer.parseInt(split[1]);
                    Double value = parseDouble(split[2]);
                    Ingresosnetos cap = new Ingresosnetos(regCuenta, date, cuenta, value);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println("registro no guardado");
                }
            }
            cons.setIngresosLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("---TERMINA INGRESOS---");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private List<Object> savePrestamos(Consistencia cons, Regcuenta regCuenta) {
        try {
            System.out.println("--COMIENZA PRESTAMOS--");
            List<String> captaciones = getFileData(filePrestamosPersonales);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
                    String des = split[2];
                    Long cta = parseLong(split[3]);
                    Double value = parseDouble(split[4]);
                    Date fecCorte = parseDate(split[5]);
                    String tipo = split[6];
                    int relev = Integer.parseInt(split[7]);
                    Prestamo cap = new Prestamo(regCuenta, cuenta, date, des, cta, value, fecCorte, tipo, relev);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println("registro no guardado");
                }
            }
            cons.setPrestamosLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("--TERMINA PRESTAMOS--");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private List<Object> saveReservas(Consistencia cons, Regcuenta regCuenta) {
        try {
            System.out.println("--COMIENZA RESERVAS--");
            List<String> captaciones = getFileData(fileReservas);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    String sta = split[1];
                    Double value = parseDouble(split[2]);
                    Reservas cap = new Reservas(regCuenta, date, sta, value);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println("registro no guardado");
                }
            }
            cons.setReservasLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("--TERMINA RESERVAS--");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private List<Object> saveTarjeta(Consistencia cons, Regcuenta regCuenta) {
        try {
            System.out.println("--COMIEZA TARJETA--");
            List<String> captaciones = getFileData(fileTarjetaCredito);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
                    String des = split[2];
                    String cta = split[3];
                    Double value = parseDouble(split[4]);
                    Date fecCorte = parseDate(split[5]);
                    String tipo = split[6];
                    int relev = Integer.parseInt(split[7]);
                    Tarjetacredito cap = new Tarjetacredito(regCuenta, cuenta, date, des, cta, value, fecCorte, tipo, relev);
                    items.add(cap);
                } catch (Exception e) {
                    System.out.println("registro no guardado");
                }
            }
            cons.setTarjetaCreditoLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("--TERMINA TARJETA--");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private List<Object> saveTenencia(Consistencia cons, Regcuenta regCuenta) {
        try {
            System.out.println("--COMIEZA TENENCIA--");
            List<String> captaciones = getFileData(fileTenencia);
            List<Object> items = new LinkedList<Object>();
            for (String s : captaciones) {
                try {
                    String[] split = s.split(";");
                    Date date = parseDate(split[0]);
                    String cta = (split[1]);
                    String des = split[2];
                    Integer tits = Integer.parseInt(split[3]);
                    String tipoValor = split[4];
                    String emision = split[5];
                    String serie = split[6];
                    Date fecCpn = parseDate(split[7]);
                    String rc10 = split[8];
                    
                    Valores valores = new Valores(regCuenta, date, cta, des, tits, tipoValor, emision, serie, fecCpn, rc10);
                    valores.setMapeado(2);
                    items.add(valores);
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("registro no guardado");
                }
            }
            cons.setTenenciaLeidos(new Double(String.valueOf(captaciones.size())));
            System.out.println("--TERMINA TENENCIA--");
            return items;
        } catch (Exception ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private Catalogocuenta parseCatalogoCuenta(String string) {
        for (Catalogocuenta c : catalogoCuenta) {
            if (c.getIdCatalogoCuenta().toString().equals(string)) {
                return c;
            }
        }
        return null;
    }

    private Date parseDate(String string) {
        try {
            SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
            return sp.parse(string);
        } catch (ParseException ex) {
            return null;
        }
    }

    private void removeData(Set captacions) {
        for (Object o : captacions) {
            DAO.delete(o);
        }
    }

    public static void main(String[] args) {
        File f = new File("C:\\Users\\Admin\\Documents\\NetBeansProjects\\SIGCAP\\web\\modelo\\baseModelo.xlsx");
        String toPath = f.toPath().toString();
        System.out.println(toPath);

    }

    private Map<String, Vector> mapVector() {
        try {
            System.out.println("--MAPEANDO VECTOR--");
            Map<String, Vector> mapping = new HashMap<String, Vector>();
            List<String> fileData = getFileData(vector);
            for (String row : fileData) {
                try {
                    String[] split = row.split(";");
                    String concat = split[1] + split[2] + split[3];
                    Double precio = parseDouble(split[5]);
                    Date fecVenc = parseDate(split[15]);
                    int moneda = split[17].toUpperCase().contains("UDI") ? 1 : split[17].toUpperCase().contains("USD") ? 4 : 14;
                    String mdy = split[36];
                    String fitch = split[53];
                    String sp = split[37];
                    String hr = split[59];
                    String stasa = split[20] == null || split[20].equals("") || split[20].equals("0")|| split[20].equals("-") ? "No" : "Si";
                    Vector vec = new Vector(concat, precio, fecVenc, moneda, mdy, fitch, sp, hr, stasa);
                    mapping.put(concat, vec);
                } catch (Exception e) {
                    System.out.println("registro de vector no guardado");
                }
            }
            System.out.println("--VECTOR MAPEADO--");
            return mapping;
        } catch (IOException ex) {
            Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    private void saveAll(List<Object>... objects) {
        DAO.saveCargaDatos(objects);
    }

}
