package interfaces;

import java.util.List;

/** Generic search contract used by service. */
public interface Searchable<T> {
    T findById(String id);
    T binarySearchById(String id);
    List<T> linearSearchByName(String token);
}