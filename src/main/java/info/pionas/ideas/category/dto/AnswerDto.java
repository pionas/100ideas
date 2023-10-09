package info.pionas.ideas.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class AnswerDto {

    private UUID id;
    private String name;
}
