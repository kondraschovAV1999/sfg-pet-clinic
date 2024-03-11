package learning.spring.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {

    @Column(name = "pet_type")
    @JoinColumn(name = "type_id")
    private PetType petType;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner; // owning side
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
