package project.pbo.repository.interfaces;

public interface WritableRepository<T> {
    void save(T entity);
}
