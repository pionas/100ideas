package info.pionas.ideas.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.pionas.ideas.category.domain.model.Category;
import info.pionas.ideas.category.dto.CategoryDto;
import info.pionas.ideas.category.dto.CategoryPage;
import info.pionas.ideas.category.dto.CreateCategory;
import info.pionas.ideas.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<Category> page;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(UUID.fromString("cbb9f3e8-22bf-4e80-8ada-3cef67911ad4"), "Cat1");
        Category category2 = new Category(UUID.fromString("f675468d-8cf9-48d6-8a15-27efa1359407"), "Cat2");
        Category category3 = new Category(UUID.fromString("ed3335df-5754-4038-a026-d39ce0cf2ebd"), "Cat3");
        page = new PageImpl<>(
                List.of(category, category2, category3)
        );
        when(categoryService.getCategories(any())).thenReturn(page);
        when(categoryService.getCategory(category.getId())).thenReturn(category);

        when(categoryService.createCategory(any())).thenAnswer(invocation -> category);
        when(categoryService.updateCategory(any(), any())).thenAnswer(
                (InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);
    }

    @Test
    void shouldGetCategories() throws Exception {
        //given
        List<CategoryDto> categoryDtos = page.map(CategoryApiControllerTest::getCategoryDto)
                .stream()
                .toList();
        CategoryPage categoryPage = new CategoryPage(categoryDtos, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        //when
        ResultActions resultActions = mockMvc.perform(get("http://localhost:8080/api/v1/categories"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(
                        content().json(objectMapper.writeValueAsString(categoryPage))
                );
    }

    @Test
    void shouldGetCategory() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/v1/categories/{categoryId}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(
                        content().json(objectMapper.writeValueAsString(category))
                );
    }

    @Test
    void shouldCreateCategory() throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                )
                .andExpect(status().isCreated())
                .andExpect(
                        content().json(objectMapper.writeValueAsString(category))
                );
    }

    @Test
    void shouldReturnBadRequestWhenTryCreateEmptyCategory() throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(CategoryApiController.class))
                .andExpect(
                        content().json("{\"errors\":\"Required request body is missing\"}")
                );
    }

    @Test
    void shouldReturnBadRequestWhenTryCreateCategoryWithShortName() throws Exception {
        CreateCategory createCategory = CreateCategory.builder().name("Ab").build();
        mockMvc.perform(post("http://localhost:8080/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategory))
                )
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(CategoryApiController.class))
                .andExpect(
                        content().json("{\"errors\":[\"name: size must be between 3 and 255\"]}")
                );
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        mockMvc.perform(put("http://localhost:8080/api/v1/categories/{categoryId}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                )
                .andExpect(status().isAccepted())
                .andExpect(
                        content().json(objectMapper.writeValueAsString(category))
                );
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("http://localhost:8080/api/v1/categories/{categoryId}", category.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldThrowExceptionWhenTryDeleteCategory() throws Exception {
        willThrow(new IllegalStateException("Some error message")).given(categoryService).deleteCategory(any());

        mockMvc.perform(delete("http://localhost:8080/api/v1/categories/{categoryId}", category.getId()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"errors\":\"Error: 500 INTERNAL_SERVER_ERROR\"}"));
    }

    private static CategoryDto getCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}