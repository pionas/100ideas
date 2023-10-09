package info.pionas.ideas.category.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class QuestionDto {

    private UUID id;
    private String name;
    private Set<AnswerDto> answers;
    private LocalDateTime created;
    private LocalDateTime modified;
}
