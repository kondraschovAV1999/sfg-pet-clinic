package learning.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType petType;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner; // owning side
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
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
