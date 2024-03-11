package learning.spring.services.map;

import learning.spring.model.PetType;
import learning.spring.services.PetTypeService;
import org.springframework.stereotype.Service;

@Service
public class PetTypeServiceMap extends AbstractMapService<PetType, Long> implements PetTypeService {
}
