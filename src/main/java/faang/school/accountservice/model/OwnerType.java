package faang.school.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "owner_type")
public class OwnerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;
}
