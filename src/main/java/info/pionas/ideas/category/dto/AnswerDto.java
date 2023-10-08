package info.pionas.ideas.category.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AnswerDto {

    private UUID id;
    private String name;
}
