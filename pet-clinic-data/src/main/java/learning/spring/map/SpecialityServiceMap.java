package learning.spring.map;

import learning.spring.model.Specialty;
import learning.spring.services.SpecialityService;
import org.springframework.stereotype.Service;

@Service
public class SpecialityServiceMap extends AbstractMapService<Specialty, Long> implements SpecialityService {
}
