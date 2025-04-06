package DB.DAO;

import java.util.List;

public interface DAO <T> {
//    void save(T obj);
//    void update(T obj);
//    void delete(T obj);
//    T findById(int id);
    List<T> getAll();
}
