package learning.spring.map;

import learning.spring.model.Pet;
import learning.spring.services.PetService;
import org.springframework.stereotype.Service;

@Service
public class PetServiceMap extends AbstractMapService<Pet, Long> implements PetService {
    public PetServiceMap(PetTypeServiceMap petTypeServiceMap) {
        this.petTypeServiceMap = petTypeServiceMap;
    }

    private final PetTypeServiceMap petTypeServiceMap;

    @Override
    public Pet save(Pet object) {

        if (object.getPetType() == null) throw new RuntimeException("Pet Type is required");
        if (object.getPetType().getId() == null)
            petTypeServiceMap.save(object.getPetType());

        return super.save(object);
    }
}
