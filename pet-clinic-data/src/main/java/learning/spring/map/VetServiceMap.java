package learning.spring.map;

import learning.spring.model.Vet;
import learning.spring.services.SpecialityService;
import learning.spring.services.VetService;
import org.springframework.stereotype.Service;

@Service
public class VetServiceMap extends AbstractMapService<Vet, Long> implements VetService {

    private final SpecialityService specialityService;

    public VetServiceMap(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @Override
    public Vet save(Vet object) {

        if (object.getSpecialties() != null)
            object.getSpecialties().stream()
                    .filter(sp -> sp.getId() == null)
                    .forEach(specialityService::save);

        return super.save(object);
    }
}
