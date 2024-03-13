package learning.spring.services.map;

import learning.spring.model.BaseEntity;
import learning.spring.services.CrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class AbstractMapServiceTest<T extends BaseEntity, ID extends Number> {

    protected CrudService<T, ID> crudService;
    protected T entity;
    protected ID id;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findAll() {
        Set<T> objects = crudService.findAll();
        assertEquals(1, objects.size());
    }

    @Test
    void findById() {
        T object = crudService.findById(id);
        assertEquals(1, object.getId());
    }

    @Test
    void saveWithProvidedId() {
        Long entityId = 2L;
        entity.setId(entityId);

        crudService.save(entity);

        assertEquals(entityId, entity.getId());
    }

    @Test
    void saveNoId() {

        crudService.save(entity);

        assertNotNull(entity.getId());
    }

    @Test
    void saveWithExistingId() {
        entity.setId(id.longValue());

        crudService.save(entity);

        assertEquals(id, entity.getId());
    }

    @Test
    void deleteById() {
        crudService.deleteById(id);
        assertEquals(0, crudService.findAll().size());
    }

    @Test
    void delete() {
        crudService.delete(crudService.findById(id));
        assertEquals(0, crudService.findAll().size());
    }
}