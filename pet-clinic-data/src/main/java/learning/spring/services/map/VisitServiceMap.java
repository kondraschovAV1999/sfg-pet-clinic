package learning.spring.services.map;

import learning.spring.model.Visit;
import learning.spring.services.VisitService;

public class VisitServiceMap extends AbstractMapService<Visit, Long> implements VisitService {

    private final OwnerServiceMap ownerServiceMap;

    public VisitServiceMap(OwnerServiceMap ownerServiceMap) {
        this.ownerServiceMap = ownerServiceMap;
    }

    @Override

    public Visit save(Visit visit) {

        if (visit.getPet().getOwner().getId() == null)
            ownerServiceMap.save(visit.getPet().getOwner());

        return super.save(visit);
    }
}
