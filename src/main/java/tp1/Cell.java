package tp1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Cell<T> implements Iterable<T>{

    private List<T> units = new LinkedList<T>();

    public void add(T t){
        units.add(t);
    }

    public Iterator<T> iterator() {
        return units.iterator();
    }

}
