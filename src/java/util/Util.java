package util;

import db.pojos.Valores;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    public static List<Double> sortDoubleValues(Set<Double> keySet) {
        List<Double> list = new LinkedList<Double>();
        for (Double d : keySet) {
            list.add(Math.abs(d));
        }
        Collections.sort(list);
        return list;
    }

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

}
