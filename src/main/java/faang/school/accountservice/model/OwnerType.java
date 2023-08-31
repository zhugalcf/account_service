package faang.school.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
@Table(name = "owner_type")
public class OwnerType {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToOne
    @JoinColumn(name = "project")
    private Project project;
}
