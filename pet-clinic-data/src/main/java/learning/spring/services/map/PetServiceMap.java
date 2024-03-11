package learning.spring.services.map;

import learning.spring.model.Pet;
import learning.spring.services.PetService;
import learning.spring.services.PetTypeService;
import org.springframework.stereotype.Service;

@Service
public class PetServiceMap extends AbstractMapService<Pet, Long> implements PetService {
    private final PetTypeService petTypeService;

    public PetServiceMap(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public Pet save(Pet object) {

        if (object.getPetType() == null) throw new RuntimeException("Pet Type is required");
        if (object.getPetType().getId() == null)
            petTypeService.save(object.getPetType());

        return super.save(object);
    }
}
