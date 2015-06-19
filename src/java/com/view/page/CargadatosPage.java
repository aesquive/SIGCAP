package com.view.page;

import db.controller.DAO;
import db.pojos.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private List<Catalogocuenta> catalogoCuenta;
    private int registroLeido;
    private Regcuenta regCuenta;

    @Override
    
    public void init() {
        message = null;
        title = "DataWarehouse - Alta Ejercicio";
        catalogoCuenta = DAO.createQuery(Catalogocuenta.class, null);
        this.form = new Form("form");
//        if (!Util.getAsciiText(per.get(numPer), 2).equals(lic.get(numPer)) || !dte.get(numPer)) {
//            setRedirect(NocontratadoPage.class);
//           return;
//        }
        name = new TextField("name", "Nombre del Ejercicio", true);
        dateField = new DateField("dateField", "Fecha de Ejercicio (dd/mm/aaaa)", true);
        dateField.setFormatPattern("dd/MM/yyyy");
        fileTenencia = new FileField("fileTenencia", "Tenencia (csv|txt)", true);
        fileCaptacion = new FileField("fileCaptacion", "Captación (csv|txt)", true);
        filePrestamosPersonales = new FileField("filePrestamos", "Cartera de Consumo (csv|txt)", true);
        fileDisponibilidades = new FileField("fileDisponibilidades", "Disponibilidades (csv|txt)", true);
        fileTarjetaCredito = new FileField("fileTarjeta", "Tarjeta de Crédito  (csv|txt)", true);
        fileReservas = new FileField("fileConsumo", "Reservas(csv|txt)", true);
        fileIngresos = new FileField("fileIngreso", "Ingresos Netos (csv|txt)", true);
        vector = new FileField("vector", "Vector Analítico (csv|txt)", true);
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

    /**
     * procesa el guardado de cada uno de los archivos dentro de la base de datos
     * @return 
     */
    public boolean procesarClicked() {
        if (form.isValid()) {
            List<Regcuenta> createQueryRg = DAO.createQuery(Regcuenta.class, null);
            for (Regcuenta r : createQueryRg) {
                if (r.getDesRegCuenta().trim().toUpperCase().equals(name.getValue().trim().toUpperCase())) {
                    message = "Favor de utilizar otro nombre de ejercicio (Repetido)";
                    return false;
                }
            }
            message = "Reportes Invalidos (El número de campos no corresponde) : ";
            FileField[] camposArchivos = new FileField[]{fileCaptacion, fileCatalogoMinimo, fileDisponibilidades, fileIngresos, filePrestamosPersonales, fileReservas, fileTarjetaCredito, fileTenencia};
            String[] nombres = new String[]{"Captación", "Catálogo Mínimo", "Disponibilidades", "Ingresos Netos", "Prestamos Personales", "Reservas", "Tarjeta de Crédito", "Tenencia"};
            //vALIDA EL NUMERO DE CAMPOS QUE TRAE EL ARCHIVO ... SI ESTE NO ES CORRECTO SACA AL USUARIO DE LA CARGA
            Integer[] numCampos = new Integer[]{6, 4, 5, 4, 8, 3, 8, 9};
            boolean pass = true;
            for (int t = 0; t < nombres.length; t++) {
                List<String> dataCatalogoMinimo;
                //esta exception pasa si no se leyo correctamente el archivo , mandamos un mensaje de error
                try {
                    dataCatalogoMinimo = getFileData(camposArchivos[t]);
                } catch (IOException ex) {
                    message = "No se pudo leer el archivo de " + nombres[t];
                    Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                    return false;
                }
                boolean validarNumeroCampos = false;
                //esta excepcion pasa si el archivo que se leyo esta vacio, mandamos un mensaje de error
                try {
                    validarNumeroCampos = validNumberFields(dataCatalogoMinimo, numCampos[t]);
                } catch (Exception ex) {
                    message = "El archivo " + nombres[t] + " esta mal formado";
                    Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                    return false;
                }
                if (!validarNumeroCampos) {
                    message = message + nombres[t] + ", ";
                    pass = false;
                }
            }
            //el sistema no deja pasar porque alguno de los archivos no esta bien formado
            if (!pass) {
                return false;
            }
            message = null;
            //salvamos el proyecto que registra nombre y fecha
            regCuenta = saveProject();
            User user = (User) getSessionVar("user");
            saveUserRelation(regCuenta, user);
            //Generamos una nueva consistencia , para poner los resultados de los reportes leidos
            Consistencia cons = new Consistencia();
            cons.setRegcuenta(regCuenta);
            registroLeido=0;
            List<Object> saveCaptacion;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de captacion, la variable que guarda el registro error es registroLeido
            try {
                saveCaptacion = saveCaptacion(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Captación";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> saveCatalogoMinimo;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de catalogominimo, la variable que guarda el registro error es registroLeido
            try {
                saveCatalogoMinimo = saveCatalogoMinimo(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Catalogo Minimo";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> saveDisponibilidades;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de disponibilidades, la variable que guarda el registro error es registroLeido
            try {
                saveDisponibilidades = saveDisponibilidades(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Disponibilidades";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> saveIngresos;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de ingresos, la variable que guarda el registro error es registroLeido
            try {
                saveIngresos = saveIngresos(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Ingresos";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> savePrestamos;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de prestamos, la variable que guarda el registro error es registroLeido
            try {
                savePrestamos = savePrestamos(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Prestamos";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> saveReservas;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de reservas, la variable que guarda el registro error es registroLeido
            try {
                saveReservas = saveReservas(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Reservas";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> saveTarjeta;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de tarjeta, la variable que guarda el registro error es registroLeido
            try {
                saveTarjeta = saveTarjeta(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Tarjeta";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            registroLeido=0;
            List<Object> saveTenencia;
            //esta exception pasa si hubo error al guardar alguno de los registros en la base de datos de tenencia, la variable que guarda el registro error es registroLeido
            try {
                saveTenencia = saveTenencia(cons, regCuenta);
            } catch (Exception ex) {
                message="Error en el registro "+registroLeido+" de Tenencia";
                borrarRegistroEjercicio(regCuenta);
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
            try {
                saveVector();
            } catch (Exception ex) {
                message="Error al guardar el vector";
                Logger.getLogger(CargadatosPage.class.getName()).log(Level.INFO, null, ex);
                
            }
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
            setRedirect(MapeoPage.class);
            message = "Carga Completa";
            return true;

        }
        return false;
    }

    /**
     * obtiene la informacion del archivo que se pasa en el campo fileField
     * @param fileField
     * @return
     * @throws IOException 
     */
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

    /**
     * checa si la primera linea de la informacion tiene en el numero de columnas correcta
     * @param dataCatalogoMinimo
     * @param i
     * @return 
     */
    private boolean validNumberFields(List<String> dataCatalogoMinimo, int i) {
        if (dataCatalogoMinimo.get(0).split(";").length == i) {
            return true;
        }
        return false;
    }

    /**
     * guarda el regcuenta del proyecto
     * @return 
     */
    private Regcuenta saveProject() {
        Regcuenta regcuenta = new Regcuenta(name.getValue());
        regcuenta.setFecha(dateField.getDate());
        DAO.save(regcuenta);
        return regcuenta;
    }

    /**
     * guarda la relacion del proyecto con el usuario
     * @param regCuenta
     * @param user 
     */
    private void saveUserRelation(Regcuenta regCuenta, User user) {
        Regcuentauser regcuentauser = new Regcuentauser(regCuenta, user);
        DAO.save(regcuentauser);
    }

    /**
     * guarda la captacion dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveCaptacion(Consistencia cons, Regcuenta reg) throws Exception {
        System.out.println("---COMIENZA CAPTACION");
        List<String> captaciones = getFileData(fileCaptacion);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
            String[] split = s.split(";");
            Date date = parseDate(split[0]);
            Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
            String des = split[2];
            Long idCapt = parseLong(split[3]);
            Double valor = parseDouble(split[4]);
            Date venc = parseDate(split[5]);
            Captacion cap = new Captacion(reg, cuenta, date, des, idCapt, valor, venc);
            items.add(cap);
        }
        System.out.println("-----TERMINO CAPTACION---");
        cons.setCaptacionLeidos(new Double(String.valueOf(captaciones.size())));
        return items;
    }

    
    /**
     * guarda el catalogo minimo dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveCatalogoMinimo(Consistencia cons, Regcuenta reg) throws Exception {
        System.out.println("--COMIENTZA CAT MINIMO---");
        List<String> captaciones = getFileData(fileCatalogoMinimo);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
            String[] split = s.split(";");
            Date date = parseDate(split[0]);
            Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
            Double value = parseDouble(split[2]);
            int moneda = Integer.parseInt(split[3]);
            Catalogominimo cap = new Catalogominimo(reg, cuenta, date, value, moneda);
            items.add(cap);
        }
        cons.setCatalogoMinimoLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("---TERMINA CAT MINIMO---");
        return items;
    }

    
    /**
     * guarda las disponibilidades dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveDisponibilidades(Consistencia cons, Regcuenta regCuenta) throws Exception {
        System.out.println("---COMIENZA DISPONIBILIDADES--");
        List<String> captaciones = getFileData(fileDisponibilidades);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
            String[] split = s.split(";");
            Date date = parseDate(split[0]);
            Catalogocuenta cuenta = parseCatalogoCuenta(split[1]);
            String des = split[2];
            Double value = parseDouble(split[3]);
            Date venc = parseDate(split[4]);
            Disponibilidad cap = new Disponibilidad(regCuenta, cuenta, date, des, value, venc);
            items.add(cap);
        }
        cons.setDisponibilidadesLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("--TERMINA DISPONIBILIDADES---");
        return items;
    }

    
    /**
     * guarda los ingresos dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveIngresos(Consistencia cons, Regcuenta regCuenta) throws Exception {
        System.out.println("---COMIENZA INGRESOS---");
        List<String> captaciones = getFileData(fileIngresos);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
            String[] split = s.split(";");
            Date date = parseDate(split[0]);
            int cuenta = Integer.parseInt(split[1]);
            Double value = parseDouble(split[2]);
            Double reqMerc=parseDouble(split[3]);
            Ingresosnetos cap = new Ingresosnetos(regCuenta, date, cuenta, value,reqMerc);
            items.add(cap);
        }
        cons.setIngresosLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("---TERMINA INGRESOS---");
        return items;
    }

    
    /**
     * guarda los prestamos dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> savePrestamos(Consistencia cons, Regcuenta regCuenta) throws Exception {
        System.out.println("--COMIENZA PRESTAMOS--");
        List<String> captaciones = getFileData(filePrestamosPersonales);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
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
        }
        cons.setPrestamosLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("--TERMINA PRESTAMOS--");
        return items;
    }

    
    /**
     * guarda las reservas dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveReservas(Consistencia cons, Regcuenta regCuenta) throws Exception {
        System.out.println("--COMIENZA RESERVAS--");
        List<String> captaciones = getFileData(fileReservas);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
            String[] split = s.split(";");
            Date date = parseDate(split[0]);
            String sta = split[1];
            Double value = parseDouble(split[2]);
            Reservas cap = new Reservas(regCuenta, date, sta, value);
            items.add(cap);
        }
        cons.setReservasLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("--TERMINA RESERVAS--");
        return items;
    }

    
    /**
     * guarda la tarjeta dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveTarjeta(Consistencia cons, Regcuenta regCuenta) throws Exception {
        System.out.println("--COMIEZA TARJETA--");
        List<String> captaciones = getFileData(fileTarjetaCredito);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
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
        }
        cons.setTarjetaCreditoLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("--TERMINA TARJETA--");
        return items;
    }

    
    /**
     * guarda la tenencia dentro de una lista de objetos
     * @param cons
     * @param reg
     * @return
     * @throws Exception 
     */
    private List<Object> saveTenencia(Consistencia cons, Regcuenta regCuenta) throws Exception {
        System.out.println("--COMIEZA TENENCIA--");
        List<String> captaciones = getFileData(fileTenencia);
        List<Object> items = new LinkedList<Object>();
        for (String s : captaciones) {
            registroLeido++;
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
        }
        cons.setTenenciaLeidos(new Double(String.valueOf(captaciones.size())));
        System.out.println("--TERMINA TENENCIA--");
        return items;
    }

    
    private Catalogocuenta parseCatalogoCuenta(String string) {
        for (Catalogocuenta c : catalogoCuenta) {
            if (c.getIdCatalogoCuenta().toString().equals(string)) {
                return c;
            }
        }
        return null;
    }

    
    
    private Date parseDate(String string)  {
        try{
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
        return sp.parse(string);
             
        }catch(Exception ex){
            return null;
        }
    }

    public static void main(String[] args) {
        File f = new File("C:\\Users\\Admin\\Documents\\NetBeansProjects\\SIGCAP\\web\\modelo\\baseModelo.xlsx");
        String toPath = f.toPath().toString();
        System.out.println(toPath);

        
    }

    /**
     * guarda los objetos dentro de la base de datos
     * @param objects 
     */
    private void saveAll(List<Object>... objects) {
        for (List l : objects) {
            DAO.saveMultiple(l);
        }
    }

    @Override
    public Integer getPermisoNumber() {
        return 1;
    }


    /**
     * borra el ejercicio de la base de datos
     * 
     * @param regCuenta 
     */
    private void borrarRegistroEjercicio(Regcuenta regCuenta) {
        DAO.delete(regCuenta);
    }

    private void saveVector() throws Exception {
        List<String> fileData = getFileData(vector);
        String date = Util.parseDateString("ddMMyyyy",dateField.getDate())+"-"+regCuenta.getIdRegCuenta();
        BufferedWriter writer=new BufferedWriter(new FileWriter(Configuration.getValue("RutaVectores")+date+".txt"));
        for(String s:fileData){
            writer.write(s);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        
    }

}
