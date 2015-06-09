package util;

import db.pojos.Valores;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Label;

/**
 *
 * @author Admin
 */
public class Util {

    /**
     * ordena valores double
     * @param keySet
     * @return 
     */
    public static List<Double> sortDoubleValues(Set<Double> keySet) {
        List<Double> list = new LinkedList<Double>();
        for (Double d : keySet) {
            list.add(Math.abs(d));
        }
        Collections.sort(list);
        return list;
    }

    /**
     * lee un archivo y regresa su contenido en una lista de cadenas
     * @param fileName
     * @return 
     */
    public static List<String> readFile(String fileName) {
        try {
            List<String> values = new LinkedList<String>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String readLine = reader.readLine();
            while (readLine != null && !readLine.equals("")) {
                values.add(readLine);
                readLine = reader.readLine();
            }
            reader.close();
            return values;
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    /**
     * genera el texto ascii, nos da los numeros que queramos por letra encriptada
     * @param valor
     * @param numbersPerLetter
     * @return 
     */
    public static String getAsciiText(String valor, int numbersPerLetter) {
        String cad = "";
        if (valor == null) {
            return "";
        }
        for (int t = 0; t < valor.length(); t = t + 2) {
            if (t + 2 > valor.length()) {
                return "";
            }
            String subs = valor.substring(t, t + 2);
            cad = cad + String.valueOf(Character.toChars(Integer.parseInt(subs)));
        }
        return cad;
    }

    public static void main(String[] args) throws ParseException {
        List<String> l = new LinkedList<String>();
        l.add("BAINVEX");
        l.add("BONDESD");
        l.add("VWLEASE");
        l.add("CFEHCB");

        Collections.sort(l);
        for (String s : l) {
            System.out.println(s);
        }
    }

    /**
     * Saca la diferencia en dias de dos fechas
     * @param init
     * @param last
     * @return 
     */
    public static int daysBetweenDates(Date init, Date last) {
        Calendar calLast = Calendar.getInstance();
        calLast.setTime(last);
        Calendar calInit = Calendar.getInstance();
        calInit.setTime(init);
        int rangoAnyos = calLast.get(Calendar.YEAR) - calInit.get(Calendar.YEAR);

        return (rangoAnyos * 365) + calLast.get(Calendar.DAY_OF_YEAR) - calInit.get(Calendar.DAY_OF_YEAR);
    }

    public static List<Valores> sortValoresBy(List<Valores> valoreses, String sortedColumn, boolean sortedAscending) {
        Map<String, String> map = new HashMap<String, String>();
        Map<Double, String> mapDouble = new HashMap<Double, String>();
        Map<String, Valores> mapValores = new HashMap<String, Valores>();
        for (Valores v : valoreses) {
            Object value = Reflector.callMethod(v, null, "get" + sortedColumn);
            String cadena = value.toString();
            String replaceAll = cadena.replaceAll(",", "");
            try {
                Double valDouble = Double.parseDouble(replaceAll);
                if (mapDouble.get(valDouble) == null) {
                    mapDouble.put(valDouble, v.getIdTenencia() + ",");
                } else {
                    mapDouble.put(valDouble, mapDouble.get(valDouble) + v.getIdTenencia() + ",");
                }
            } catch (Exception e) {

                if (map.get(replaceAll) == null) {
                    map.put(replaceAll, v.getIdTenencia() + ",");
                } else {
                    map.put(replaceAll, map.get(replaceAll) + v.getIdTenencia() + ",");
                }

            }
            mapValores.put(v.getIdTenencia().toString(), v);
        }
        if (map.size() > 0) {

            List<String> example = new LinkedList<String>(map.keySet());
            Collections.sort(example);
            List<Valores> vals = new LinkedList<Valores>();
            for (int t = 0; t < example.size(); t++) {
                String key = null;
                if (sortedAscending) {
                    key = example.get(t);
                } else {
                    key = example.get(example.size() - t - 1);
                }
                String tenencias = map.get(key);
                String[] split = tenencias.split(",");
                for (String s : split) {
                    if (s != null && !s.equals("")) {
                        vals.add(mapValores.get(s));
                    }
                }
            }
            return vals;
        }

        List<Double> example = new LinkedList<Double>(mapDouble.keySet());
        Collections.sort(example);
        List<Valores> vals = new LinkedList<Valores>();
        for (int t = 0; t < example.size(); t++) {
            Double key = null;
            if (sortedAscending) {
                key = example.get(t);
            } else {
                key = example.get(example.size() - t - 1);
            }
            String tenencias = mapDouble.get(key);
            String[] split = tenencias.split(",");
            for (String s : split) {
                if (s != null && !s.equals("")) {
                    vals.add(mapValores.get(s));
                }
            }
        }
        return vals;
    }

    /**
     * metodo con reflexion sobre el objeto para ejecutar un metodo y regresarlo como cadena de texto
     * @param object
     * @param idmethodName
     * @return 
     */
    public static String reflectionString(Object object, String idmethodName) {
        try {
            Class classObject = object.getClass();
            Method method = classObject.getMethod(idmethodName, null);
            Object invoke = method.invoke(object, null);
            return invoke.toString();
        } catch (Exception ex) {
            System.out.println("Error "+ex);
        }
        return null;
    }
    
    /**
     * metodo con reflexion sobre el objeto para ejecutar un metodo
     * @param object
     * @param nmethod
     * @return 
     */
    public static Object reflectionInvoke(Object object, String nmethod){
        try {
            Class classObject = object.getClass();
            Method method = classObject.getMethod(nmethod, null);
            Object invoke = method.invoke(object, null);
            return invoke;
        } catch (Exception ex) {
            System.out.println("Error reflection invoke"+ex);
        }
        return null;
    }
    
    public static boolean reflectionInvokeSet(Object target,Method method,Object ... args){
        try {
            method.invoke(target, args);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.INFO, null, ex);
            return false;
        }
    }

    
    public static String formatNumber(Number num){
        Locale loc = new Locale("us");
        NumberFormat instance = NumberFormat.getInstance(loc);
        instance.setMaximumFractionDigits(2);
        return instance.format(num);
    }
    
    
    public static String formatDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    /**
     * saca un metodo por reflexión dado su nombre y la clase en la cual se busca
     * @param target
     * @param name
     * @param params
     * @return 
     */
    public static Method getMethod(Class target,String name,Class... params){
        try {
            return target.getMethod(name,params);
        } catch (Exception ex) {
            System.out.println("Error Util.getMethod :"+target.toString()+" Method: "+name);
            return null;
        }  
    }

    /**
     * Saca el tipo de objeto que regresa un metodo
     * @param aClass
     * @param method
     * @return 
     */
    public static Class getReturnType( Method method) {
        return method.getReturnType();
    }

    public static String parseDateString(String pattern, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }



}
