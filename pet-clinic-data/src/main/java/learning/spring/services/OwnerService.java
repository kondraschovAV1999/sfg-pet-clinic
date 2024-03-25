package learning.spring.services;

import learning.spring.model.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OwnerService extends CrudService<Owner, Long> {

    Owner findByLastName(String lastName);

    Page<Owner> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}
