package learning.spring.services.map;

import learning.spring.model.Vet;
import learning.spring.services.SpecialityService;
import learning.spring.services.VetService;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Profile({"default", "map"})
public class VetServiceMap extends AbstractMapService<Vet, Long> implements VetService {

    private final SpecialityService specialityService;

    public VetServiceMap(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @Override
    public Vet save(Vet object) {

        if (object == null) throw new RuntimeException("Illegal argument for save method!");

        if (object.getSpecialties() != null)
            object.getSpecialties().stream()
                    .filter(sp -> sp.getId() == null)
                    .forEach(specialityService::save);

        return super.save(object);
    }

    @Override
    public Page<Vet> findAll(Pageable pageable) throws DataAccessException {
        throw new UnsupportedOperationException();
    }
}
