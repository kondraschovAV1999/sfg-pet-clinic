package learning.spring.map;

import learning.spring.model.BaseEntity;
import learning.spring.services.CrudService;

import java.util.*;

public abstract class AbstractMapService<T extends BaseEntity, ID extends Number>
        implements CrudService<T, ID> {

    protected Map<Number, T> map = new HashMap<>();

    @Override
    public Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    @Override
    public T findById(ID id) {
        return map.get(id);
    }

    @Override
    public T save(T object) {
        map.put(object.getId(), object);
        return object;
    }

    @Override
    public void deleteById(ID id) {
        map.remove(id);
    }

    @Override
    public void delete(T object) {
        map.entrySet().removeIf(e -> e.getValue().equals(object));
    }
}
