package learning.spring.controllers;

import learning.spring.model.Owner;
import learning.spring.model.Pet;
import learning.spring.model.PetType;
import learning.spring.services.OwnerService;
import learning.spring.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private OwnerService ownerService;
    @Mock
    private PetTypeService petTypeService;
    @InjectMocks
    private PetController petController;

    private MockMvc mockMvc;
    private Owner owner;
    private Set<PetType> petTypes;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1L);
        petTypes = new HashSet<>();
        PetType dog = new PetType("dog");
        dog.setId(1L);
        PetType cat = new PetType("cat");
        cat.setId(2L);
        petTypes.add(dog);
        petTypes.add(cat);
        Pet pet = new Pet();
        pet.setId(1L);
        owner.addPet(pet);

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    void initCreationForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(get("/owners/%d/pets/new".formatted(owner.getId())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name("pets/createOrUpdatePetForm"));
    }

    @Test
    void processCreationFrom() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(ownerService.save(any(Owner.class))).thenReturn(owner);

        mockMvc.perform(post("/owners/%d/pets/new".formatted(owner.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        verify(ownerService).save(any(Owner.class));
    }

    @Test
    void initUpdateForm() throws Exception {

        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(get("/owners/%d/pets/%d/edit"
                        .formatted(owner.getId(), 1L)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name("pets/createOrUpdatePetForm"));
    }

    @Test
    void processUpdateFrom() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(ownerService.save(any(Owner.class))).thenReturn(owner);

        mockMvc.perform(post("/owners/%d/pets/%d/edit"
                        .formatted(owner.getId(), 1L)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        verify(ownerService).save(any(Owner.class));
    }


}