package faang.school.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
@Table(name = "projects")
public class Project {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private OwnerType ownerType;
}

