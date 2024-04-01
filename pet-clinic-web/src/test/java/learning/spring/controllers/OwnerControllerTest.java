package learning.spring.controllers;

import learning.spring.model.Owner;
import learning.spring.model.Pet;
import learning.spring.model.PetType;
import learning.spring.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @InjectMocks
    private OwnerController controller;
    @Mock
    private OwnerService service;

    private Set<Owner> owners;

    private MockMvc mockMvc;

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
        owners = new HashSet<>();
        owners.add(createOwner(1L));
        owners.add(createOwner(2L));

        mockMvc = MockMvcBuilders.
                standaloneSetup(controller)
                .build();
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attributeExists("owner"));
        verifyNoInteractions(service);
    }

    @Test
    void processFindFromReturnMany() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Owner> paginated = new PageImpl<>(new ArrayList<>(owners), pageable, 2);
        when(service.findByLastNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(paginated);
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attribute("listOwners", hasSize(2)));
    }

    @Test
    void processFindFromReturnOne() throws Exception {
        Owner owner = new Owner();
        owner.setId(1L);
        Pageable pageable = PageRequest.of(0, 5);
        Page<Owner> paginated = new PageImpl<>(List.of(owner), pageable, 1);
        when(service.findByLastNameContainingIgnoreCase(
                anyString(), any(Pageable.class))).thenReturn(paginated);
        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
    }

    @Test
    void displayOwner() throws Exception {
        Long id = 1L;
        Owner owner1 = new Owner();
        owner1.setId(id);
        when(service.findById(anyLong())).thenReturn(owner1);

        mockMvc.perform(get("/owners/%d".formatted(id)))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void initCreationForm() throws Exception {

        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(service);
    }

    @Test
    void processCreationForm() throws Exception {
        Long id = 1L;
        Owner owner = createOwner(id);
        when(service.save(any(Owner.class))).thenReturn(owner);

        mockMvc.perform(post("/owners/new")
                        .flashAttr("owner", owner))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/%d".formatted(id)));
    }

    @Test
    void initUpdateForm() throws Exception {
        Long id = 1L;
        Owner owner = createOwner(id);
        when(service.findById(anyLong())).thenReturn(owner);

        mockMvc.perform(get("/owners/%d/edit".formatted(id)))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void processUpdateOwnerForm() throws Exception {
        Long id = 1L;
        Owner owner = createOwner(id);
        when(service.save(any(Owner.class))).thenReturn(owner);

        mockMvc.perform(post("/owners/%d/edit".formatted(id))
                        .flashAttr("owner", owner))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        verify(service).save(any(Owner.class));
    }
}