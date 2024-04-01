package learning.spring.controllers;

import learning.spring.model.Owner;
import learning.spring.model.Pet;
import learning.spring.model.Visit;
import learning.spring.services.OwnerService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private VisitController visitController;
    private MockMvc mockMvc;
    private Owner owner;
    private Pet pet;
    private Visit visit;

    @BeforeEach
    void setUp() {
        owner = Owner.builder().id(1L).pets(new HashSet<>()).build();
        pet = Pet.builder().id(1L).visits(new HashSet<>()).build();

        visit = Visit.builder()
                .id(1L)
                .date(LocalDate.now())
                .description("visit")
                .build();
        pet.addVisit(visit);

        owner.addPet(pet);
        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();

    }

    @Test
    void initNewVisitForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);

        mockMvc.perform(get("/owners/%d/pets/%d/visits/new"
                        .formatted(owner.getId(), pet.getId())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("visit"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    void processNewVisitForm() throws Exception {
        when(ownerService.save(any(Owner.class))).thenReturn(owner);

        mockMvc.perform(post("/owners/%d/pets/%d/visits/new"
                        .formatted(owner.getId(), pet.getId()))
                        .flashAttr("owner", owner)
                        .flashAttr("visit", visit))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/%d"
                        .formatted(owner.getId())));
    }
}