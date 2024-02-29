package learning.spring.map;

import learning.spring.model.Owner;
import learning.spring.services.OwnerService;

public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {
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
                .filter(o->o.getLastName().equals(lastName))
                .findFirst();
        return owner.orElse(new Owner());

    }
}
