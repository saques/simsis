package tp1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Cell<T> implements Iterable<T>{

    public List<T> units = new LinkedList<T>();

    public void add(T t){
        units.add(t);
    }

    public Iterator<T> iterator() {
        return units.iterator();
    }

    public Stream<T> stream(){
        return units.stream();
    }

}
