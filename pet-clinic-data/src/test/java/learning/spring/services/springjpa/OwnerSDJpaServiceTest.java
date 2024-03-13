package learning.spring.services.springjpa;

import learning.spring.model.Owner;
import learning.spring.repositories.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {
    public static final String LAST_NAME = "Smith";
    @Mock
    private OwnerRepository ownerRepository;
    @InjectMocks
    private OwnerSDJpaService service;
    private Owner returnOwner;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().lastName(LAST_NAME).build();
        returnOwner.setId(id);
    }

    @Test
    void findAll() {
        Set<Owner> returnOwners = new HashSet<>();
        returnOwners.add(returnOwner);

        when(ownerRepository.findAll()).thenReturn(returnOwners);
        Set<Owner> owners = service.findAll();

        assertNotNull(owners);
        assertEquals(1, owners.size());

    }

    @Test
    void findById() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(returnOwner));

        Owner owner = service.findById(id);
        assertEquals(id, owner.getId());
    }

    @Test
    void findByIdNotFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Owner owner = service.findById(2L);
        assertNull(owner.getId());
    }

    @Test
    void save() {
        when(ownerRepository.save(any())).thenReturn(returnOwner);

        Owner savedOwner = service.save(new Owner());
        assertEquals(1, savedOwner.getId());
        verify(ownerRepository).save(any());
    }

    @Test
    void delete() {
        service.delete(returnOwner);
        verify(ownerRepository).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(returnOwner.getId());
        verify(ownerRepository).deleteById(anyLong());
    }

    @Test
    void findByLastName() {

        when(ownerRepository.findByLastName(any())).thenReturn(returnOwner);

        Owner smith = service.findByLastName(LAST_NAME);
        assertEquals(returnOwner.getLastName(), smith.getLastName());
    }
}