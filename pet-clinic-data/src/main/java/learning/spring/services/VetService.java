package learning.spring.services;

import learning.spring.model.Vet;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VetService extends CrudService<Vet, Long> {
    Page<Vet> findAll(Pageable pageable) throws DataAccessException;
}
