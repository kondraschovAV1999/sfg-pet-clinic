package learning.spring.map;

import learning.spring.model.Speciality;
import learning.spring.services.SpecialityService;
import org.springframework.stereotype.Service;

@Service
public class SpecialityServiceMap extends AbstractMapService<Speciality, Long> implements SpecialityService {
}
