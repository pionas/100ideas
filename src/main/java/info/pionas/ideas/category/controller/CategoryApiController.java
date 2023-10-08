package info.pionas.ideas.category.controller;

import info.pionas.ideas.category.domain.model.Category;
import info.pionas.ideas.category.dto.CategoryDto;
import info.pionas.ideas.category.dto.CategoryPage;
import info.pionas.ideas.category.dto.CreateCategory;
import info.pionas.ideas.category.mapper.CategoryMapper;
import info.pionas.ideas.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @GetMapping
    CategoryPage getCategories(Pageable pageable) {
        return mapper.map(categoryService.getCategories(pageable));
    }

    @GetMapping("{id}")
    CategoryDto getCategory(@PathVariable UUID id) {
        return mapper.map(categoryService.getCategory(id));
    }

    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategory category) throws URISyntaxException {
        CategoryDto categoryDto = mapper.map(categoryService.createCategory(mapper.map(category)));
        return ResponseEntity
                .created(new URI("/api/v1/categories/" + categoryDto.getId()))
                .body(categoryDto);
    }

    @PutMapping("{id}")
    ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        Category categoryUpdated = categoryService.updateCategory(id, category);
        return ResponseEntity
                .accepted()
                .body(mapper.map(categoryUpdated));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }
}
