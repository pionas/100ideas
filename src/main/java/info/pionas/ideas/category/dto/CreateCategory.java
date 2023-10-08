package info.pionas.ideas.category.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CreateCategory {

    @NotEmpty(message = "{ideas.validation.name.NotBlank.message}")
    @Size(min = 3, max = 255)
    private String name;

}
