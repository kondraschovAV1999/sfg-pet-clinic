package learning.spring.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "owners")
public class Owner extends Person {
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "telephone")
    private String telephone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Pet> pets = new HashSet<>();

    @Builder
    public Owner(String firstName, String lastName, String address, String city,
                 String telephone, Set<Pet> pets) {
        super(firstName, lastName);
        this.address = address;
        this.city = city;
        this.telephone = telephone;
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setOwner(this);
    }

    public void removePet(Pet pet) {
        pets.removeIf(p -> p.getId().equals(pet.getId()));
        pet.setOwner(null);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return a pet if pet name is already in use
     */
    public Pet getPet(String name, boolean ignoreNew) {
        return pets.stream()
                .filter(p -> p.getName() != null && p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return a pet if pet name is already in use
     */
    public Pet getPet(String name) {
        return getPet(name, false);
    }

    /**
     * Return the Pet with the given id, or null if none found for this Owner.
     *
     * @param id to test
     * @return a pet if pet id is already in use
     */
    public Pet getPet(Long id) {
        return pets.stream()
                .filter(p -> !p.isNew() && p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds the given {@link Visit} to the {@link Pet} with the given identifier.
     * @param petId the identifier of the {@link Pet}, must not be {@literal null}.
     * @param visit the visit to add, must not be {@literal null}.
     */
    public void addVisit(Long petId, Visit visit) {

        Assert.notNull(petId, "Pet identifier must not be null!");
        Assert.notNull(visit, "Visit must not be null!");

        Pet pet = getPet(petId);

        Assert.notNull(pet, "Invalid Pet identifier!");

        pet.addVisit(visit);
    }
}
