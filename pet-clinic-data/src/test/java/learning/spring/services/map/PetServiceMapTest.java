package learning.spring.services.map;

import learning.spring.model.Pet;
import learning.spring.model.PetType;
import learning.spring.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PetServiceMapTest extends AbstractMapServiceTest<Pet, Long>{

    @Mock
    private PetTypeService petTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        crudService = new PetServiceMap(petTypeService);
        id = 1L;
        entity = Pet.builder().petType(PetType.builder().name("dog").build()).build();

        Pet pet = Pet.builder().petType(PetType.builder().name("cat").build()).build();
        pet.setId(id);
        crudService.save(pet);
    }
}