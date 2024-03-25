package learning.spring.repositories;

import learning.spring.model.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, Long> {
    Owner findByLastName(String lastName);

    /**
     * This method is equal to
     * SELECT * FROM owner WHERE owner.lastName ILIKE %lastName%
     * @param lastName Value to search for
     * @return a Collection of matching {@link Owner}s (or an empty Collection if none
     * found)
     */
    Page<Owner> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}
