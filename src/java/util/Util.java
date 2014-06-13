/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Util {

    public static List<Double> sortDoubleValues(Set<Double> keySet) {
        List<Double> list=new LinkedList<Double>();
        for(Double d:keySet){
            list.add(d);
        }
        Collections.sort(list);
        return list;
    }

    public static List<String> readFile(String fileName) {
        try {
            List<String> values=new LinkedList<String>();
            BufferedReader reader=new BufferedReader(new FileReader(fileName));
            String readLine = reader.readLine();
            while(readLine!=null && !readLine.equals("")){
                values.add(readLine);
                readLine=reader.readLine();
            }
            reader.close();
            return values;
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.INFO, null, ex);
        }
        return null;
    }

    public static String getAsciiText(String valor, int numbersPerLetter) {
        String cad="";
        if(valor==null){
            return "";
        }
        for(int t=0;t<valor.length();t=t+2){
            if(t+2>valor.length()){
                return "";
            }
            String subs=valor.substring(t, t+2);
            cad=cad+ String.valueOf(Character.toChars(Integer.parseInt(subs)));
        }
        return cad;
    }
    
    public static void main(String[] args) {
        String asciiText = Util.getAsciiText("677980806976876582", 2);
        System.out.println(asciiText);
    }
    
    
}
