package payment.src.service.storage;

import java.util.List;
import java.util.Optional;

public interface StorageService<T> {
    void save(T entity);

    Optional<T> findFirst();

    List<T> findAll();
}
