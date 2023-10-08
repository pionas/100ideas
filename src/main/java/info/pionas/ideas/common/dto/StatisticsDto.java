package info.pionas.ideas.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsDto {
    private long questions;
    private long answers;
}
