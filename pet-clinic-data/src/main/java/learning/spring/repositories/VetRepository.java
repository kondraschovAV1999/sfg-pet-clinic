package learning.spring.repositories;

import learning.spring.model.Vet;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface VetRepository extends CrudRepository<Vet, Long> {
    Page<Vet> findAll(Pageable pageable) throws DataAccessException;
}
