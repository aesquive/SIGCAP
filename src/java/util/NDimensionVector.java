package util;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NDimensionVector<T extends Comparable> {

    private List<Comparable> values;

    public NDimensionVector() {
        values = new LinkedList<Comparable>();
    }

    public NDimensionVector(Comparable... value) {
        values = new LinkedList<Comparable>();
        for(Comparable v:value){
            addValue(v);
        }
    }

    public void addValue(Comparable newValue) {
        values.add(newValue);
    }
    
    
    public void addValues(Comparable ... newValue) {
        for(Comparable c:newValue){
            values.add(c);
        }
    }

    public List<Comparable> getValues(){
        return values;
    }

    public void addValues(NDimensionVector get) {
        List<Comparable> values1 = get.getValues();
        for(Comparable nv:values1){
            addValue(nv);
        }
    }

    public String toString(){
        for(Object o:values){
            System.out.print(o+",");
        }
        System.out.println("");
        return "";
    }
}
