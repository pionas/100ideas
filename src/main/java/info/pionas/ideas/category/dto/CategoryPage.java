package info.pionas.ideas.category.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CategoryPage {

    private List<CategoryDto> content;
    private long number;
    private long size;
    private long totalElements;
    private long totalPages;
}
