package learning.spring.map;

import learning.spring.model.Pet;
import learning.spring.services.PetService;
import org.springframework.stereotype.Service;

@Service

public class PetServiceMap extends AbstractMapService<Pet,Long> implements PetService {
}
