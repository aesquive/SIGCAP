/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import db.controller.DAO;
import db.pojos.Regcuenta;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    
    
}
