package info.pionas.ideas.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.pionas.ideas.category.domain.model.Category;
import info.pionas.ideas.category.dto.CategoryDto;
import info.pionas.ideas.category.dto.CategoryPage;
import info.pionas.ideas.category.dto.CreateCategory;
import info.pionas.ideas.category.service.CategoryService;
import info.pionas.ideas.question.domain.model.Answer;
import info.pionas.ideas.question.domain.model.Question;
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
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryApiControllerTest {

    private static final UUID CATEGORY_UUID = UUID.fromString("cbb9f3e8-22bf-4e80-8ada-3cef67911ad4");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<Category> page;

    @BeforeEach
    void setUp() {
        Category category1 = prepareCategoryWithQuestionAndAnswer();
        Category category2 = new Category(UUID.fromString("f675468d-8cf9-48d6-8a15-27efa1359407"), "Cat2");
        Category category3 = new Category(UUID.fromString("ed3335df-5754-4038-a026-d39ce0cf2ebd"), "Cat3");
        page = new PageImpl<>(
                List.of(category1, category2, category3)
        );
        when(categoryService.getCategories(any())).thenReturn(page);
        when(categoryService.getCategory(any())).thenReturn(category1);

        when(categoryService.createCategory(any())).thenAnswer(invocation -> category1);
        when(categoryService.updateCategory(any(), any())).thenAnswer((InvocationOnMock invocationOnMock) -> category1);
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
        mockMvc.perform(get("http://localhost:8080/api/v1/categories/{categoryId}", CATEGORY_UUID))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("{\"id\":\"cbb9f3e8-22bf-4e80-8ada-3cef67911ad4\",\"name\":\"Cat1\",\"questions\":[{\"id\":\"a07208ec-69c1-42dc-ab62-ba0aaf85c516\",\"name\":\"Question 1\",\"answers\":[{\"id\":\"16541f93-8ede-476b-9ce5-730a22c103ea\",\"name\":\"Answer 1\"}]}]}")
                );
    }

    @Test
    void shouldCreateCategory() throws Exception {
        Category categoryToCreate = new Category(CATEGORY_UUID, "new category name");
        mockMvc.perform(post("http://localhost:8080/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryToCreate))
                )
                .andExpect(status().isCreated())
                .andExpect(
                        content().json("{\"id\":\"cbb9f3e8-22bf-4e80-8ada-3cef67911ad4\",\"name\":\"Cat1\",\"questions\":[{\"id\":\"a07208ec-69c1-42dc-ab62-ba0aaf85c516\",\"name\":\"Question 1\",\"answers\":[{\"id\":\"16541f93-8ede-476b-9ce5-730a22c103ea\",\"name\":\"Answer 1\"}]}]}")
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
        Category categoryToUpdate = new Category(CATEGORY_UUID, "new category name");
        mockMvc.perform(put("http://localhost:8080/api/v1/categories/{categoryId}", CATEGORY_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryToUpdate))
                )
                .andExpect(status().isAccepted())
                .andExpect(
                        content().json("{\"id\":\"cbb9f3e8-22bf-4e80-8ada-3cef67911ad4\",\"name\":\"Cat1\",\"questions\":[{\"id\":\"a07208ec-69c1-42dc-ab62-ba0aaf85c516\",\"name\":\"Question 1\",\"answers\":[{\"id\":\"16541f93-8ede-476b-9ce5-730a22c103ea\",\"name\":\"Answer 1\"}]}]}")
                );
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("http://localhost:8080/api/v1/categories/{categoryId}", CATEGORY_UUID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldThrowExceptionWhenTryDeleteCategory() throws Exception {
        willThrow(new IllegalStateException("Some error message")).given(categoryService).deleteCategory(any());

        mockMvc.perform(delete("http://localhost:8080/api/v1/categories/{categoryId}", CATEGORY_UUID))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"errors\":\"Error: 500 INTERNAL_SERVER_ERROR\"}"));
    }

    private static CategoryDto getCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private static Category prepareCategoryWithQuestionAndAnswer() {
        Category category1 = new Category(CATEGORY_UUID, "Cat1");
        Question question1 = new Question(UUID.fromString("a07208ec-69c1-42dc-ab62-ba0aaf85c516"), "Question 1");
        question1.setCategory(category1);
        question1.setAnswers(Set.of(new Answer(UUID.fromString("16541f93-8ede-476b-9ce5-730a22c103ea"), "Answer 1")));
        category1.setQuestions(List.of(question1));
        return category1;
    }
}