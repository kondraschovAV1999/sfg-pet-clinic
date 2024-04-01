package learning.spring.controllers;

import learning.spring.model.Speciality;
import learning.spring.model.Vet;
import learning.spring.services.VetService;
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

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {
    @Mock
    private VetService vetService;

    @InjectMocks
    private VetController vetController;

    private MockMvc mockMvc;

    private Vet vet;

    @BeforeEach
    void setUp() {
        vet = Vet.builder()
                .id(1L)
                .firstName("James")
                .lastName("Nod")
                .specialties(new HashSet<>())
                .build();
        Speciality surgery = new Speciality(1L, "surgery");
        vet.addSpeciality(surgery);
        mockMvc = MockMvcBuilders.
                standaloneSetup(vetController)
                .build();
    }

    @Test
    void listVets() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Vet> paginated = new PageImpl<>(List.of(vet), pageable, 1);

        when(vetService.findAll(any(Pageable.class))).thenReturn(paginated);
        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("listVets"))
                .andExpect(view().name("vets/index"));
    }
}