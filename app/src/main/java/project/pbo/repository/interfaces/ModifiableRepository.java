package project.pbo.repository.interfaces;

public interface ModifiableRepository<T, ID> {
    void update(T entity);
    void delete(ID id);
}
