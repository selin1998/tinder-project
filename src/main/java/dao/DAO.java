package dao;

import java.util.List;

public interface DAO<T> {
    T get(int id);
    List<T> getAll();
    boolean add(T object);
    boolean remove(int id);
}
