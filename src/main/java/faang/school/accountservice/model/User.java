package faang.school.accountservice.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Data
@Table(name = "users")
public class User {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private OwnerType ownerType;
}
