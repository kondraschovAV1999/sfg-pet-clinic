package learning.spring.services;

import learning.spring.model.Owner;
import learning.spring.model.Pet;

import java.util.Set;

public interface PetService {
    Pet findById(Long id);

    Pet save(Pet pet);

    Set<Pet> findAll();
}
