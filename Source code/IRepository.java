import java.util.List;

public interface IRepository<T> {
    List<T> loadAll();
    void saveAll(List<T> items);
}
