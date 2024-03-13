package learning.spring.services.map;

import learning.spring.model.Vet;
import learning.spring.services.SpecialityService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VetServiceMapTest extends AbstractMapServiceTest<Vet, Long> {

    @Mock
    private SpecialityService specialityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        crudService = new VetServiceMap(specialityService);
        id = 1L;
        entity = new Vet();

        Vet vet = new Vet();
        vet.setId(id);
        crudService.save(vet);
    }

}