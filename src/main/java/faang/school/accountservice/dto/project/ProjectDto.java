package faang.school.accountservice.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectDto {
    private long id;
    @NotBlank
    private String title;
}
