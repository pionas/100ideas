package info.pionas.ideas.category.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CategoryDto {

    private UUID id;

    private String name;

    private List<QuestionDto> questions;
}
