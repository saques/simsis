package tp1;

import java.util.*;
import java.util.stream.Stream;

public class Cell<T> implements Iterable<T>{

    public Set<T> units = new HashSet<>();

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
