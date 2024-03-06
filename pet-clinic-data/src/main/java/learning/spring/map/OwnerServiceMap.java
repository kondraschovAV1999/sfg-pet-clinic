package learning.spring.map;

import learning.spring.model.Owner;
import learning.spring.services.OwnerService;
import learning.spring.services.PetService;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetService petService;

    public OwnerServiceMap(PetService petService) {
        this.petService = petService;
    }

    /***
     *
     * @param lastName
     * @return Owner class, if there is more than one object with the same last name,
     * there is no guarantee which one would be returned
     * If there is not such person method will return default value
     */
    @Override
    public Owner findByLastName(String lastName) {

        var owner = map.values()
                .stream()
                .filter(o -> o.getLastName().equals(lastName))
                .findFirst();
        return owner.orElse(new Owner());

    }

    @Override
    public Owner save(Owner object) {

        if (object.getPets() != null)
            object.getPets().stream()
                    .filter(pet -> pet.getId() == null)
                    .forEach(petService::save);

        return super.save(object);
    }
}
