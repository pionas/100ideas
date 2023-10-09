package info.pionas.ideas.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class CategoryPage {

    private List<CategoryDto> content;
    private long number;
    private long size;
    private long totalElements;
    private long totalPages;
}
