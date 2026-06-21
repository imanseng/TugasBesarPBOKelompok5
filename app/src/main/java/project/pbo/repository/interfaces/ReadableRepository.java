package project.pbo.repository.interfaces;

import java.util.List;

public interface ReadableRepository<T> {
    List<T> getAll();
}
