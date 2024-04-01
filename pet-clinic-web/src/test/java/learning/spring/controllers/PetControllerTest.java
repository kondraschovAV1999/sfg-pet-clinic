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

import java.time.LocalDate;
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

    private Owner createOwner(Long id) {

        Pet snowball = Pet.builder()
                .id(1L)
                .birthDate(LocalDate.now())
                .name("snowball")
                .visits(new HashSet<>())
                .petType(PetType.builder()
                        .id(1L)
                        .name("dog")
                        .build()).build();

        Owner mike = Owner.builder()
                .id(id)
                .firstName("Mike")
                .lastName("Smith")
                .city("New York")
                .address("Time Square")
                .telephone("1234567891")
                .pets(new HashSet<>())
                .build();

        mike.addPet(snowball);

        return mike;
    }

    @BeforeEach
    void setUp() {
        owner = createOwner(1L);
        petTypes = new HashSet<>();
        petTypes.add(PetType.builder().name("dog").id(1L).build());
        petTypes.add(PetType.builder().name("cat").id(2L).build());

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

        mockMvc.perform(post("/owners/%d/pets/new".formatted(owner.getId()))
                        .flashAttr("pet", owner.getPet(1L)))
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
                        .formatted(owner.getId(), 1L))
                        .flashAttr("pet", owner.getPet(1L)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        verify(ownerService).save(any(Owner.class));
    }


}