package learning.spring.services.map;

import learning.spring.model.Owner;
import learning.spring.services.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class OwnerServiceMapTest extends AbstractMapServiceTest<Owner, Long> {

    private final String lastName = "Kondrashov";
    @Mock
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        crudService = new OwnerServiceMap(petService);
        id = 1L;
        entity = new Owner();
        
        Owner owner = Owner.builder()
                .lastName(lastName)
                .pets(new HashSet<>())
                .build();
        owner.setId(id);

        crudService.save(owner);
    }

    @Test
    void findByLastName() {
        Owner owner = ((OwnerServiceMap) crudService).findByLastName(lastName);

        assertNotNull(owner);

        assertEquals(id, owner.getId());
    }

    @Test
    void findByLastNameNotFound() {
        assertNull(((OwnerServiceMap) crudService).findByLastName("foo").getLastName());
    }
}