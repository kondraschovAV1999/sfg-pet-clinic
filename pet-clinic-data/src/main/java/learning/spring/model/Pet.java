package learning.spring.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {
    @Builder
    public Pet(Long id, PetType petType, Owner owner, LocalDate birthDate, String name, Set<Visit> visits) {
        super(id);
        this.petType = petType;
        this.owner = owner;
        this.birthDate = birthDate;
        this.name = name;
        this.visits = visits;
    }

    @ManyToOne
    @JoinColumn(name = "type_id")
    @Valid
    @NotNull
    private PetType petType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner; // owning side

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate birthDate;

    @Column(name = "name")
    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    @Valid
    private Set<Visit> visits = new HashSet<>();

    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setPet(this);
    }

    public void removeVisit(Visit visit) {
        visits.removeIf(v -> v.getId().equals(visit.getId()));
        visit.setPet(null);
    }

    @Override
    public String toString() {
        return name;
    }
}
