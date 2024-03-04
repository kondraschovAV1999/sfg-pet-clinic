package learning.spring.map;

import learning.spring.model.Vet;
import learning.spring.services.VetService;
import org.springframework.stereotype.Service;

@Service

public class VetServiceMap extends AbstractMapService<Vet, Long> implements VetService {
}
