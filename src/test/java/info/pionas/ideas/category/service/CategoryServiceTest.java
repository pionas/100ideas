package info.pionas.ideas.category.service;

import info.pionas.ideas.category.domain.model.Category;
import info.pionas.ideas.category.domain.repository.CategoryRepository;
import info.pionas.ideas.category.exception.CategoryNotFoundException;
import info.pionas.ideas.common.uuid.UuidUtils;
import info.pionas.ideas.question.domain.model.Question;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final UuidUtils uuidUtils = Mockito.mock(UuidUtils.class);

    private final CategoryService categoryService = new CategoryService(categoryRepository, uuidUtils);

    @Test
    void getCategories_should_return_empty_list() {
        //given
        Page<Category> page = new PageImpl<>(Collections.emptyList());
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        //when
        Page<Category> pageResults = categoryService.getCategories(Pageable.ofSize(100));
        //then
        assertThat(pageResults).isEmpty();
    }

    @Test
    void getCategories_should_return_three_categories() {
        //given
        Category cat1 = new Category(UUID.fromString("cbb9f3e8-22bf-4e80-8ada-3cef67911ad4"), "Cat1");
        Category cat2 = new Category(UUID.fromString("f675468d-8cf9-48d6-8a15-27efa1359407"), "Cat2");
        Category cat3 = new Category(UUID.fromString("ed3335df-5754-4038-a026-d39ce0cf2ebd"), "Cat3");
        Page<Category> page = new PageImpl<>(
                List.of(cat1, cat2, cat3)
        );
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        //when
        Page<Category> pageResults = categoryService.getCategories(Pageable.ofSize(100));
        //then
        assertThat(pageResults)
                .hasSize(3)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Cat1", "Cat2", "Cat3");
    }

    @Test
    void getCategories_should_return_categories_when_contains_c_in_name() {
        //given
        Category cat1 = new Category(UUID.fromString("cbb9f3e8-22bf-4e80-8ada-3cef67911ad4"), "Cat1");
        Category cat2 = new Category(UUID.fromString("f675468d-8cf9-48d6-8a15-27efa1359407"), "Cat2");
        Category cat3 = new Category(UUID.fromString("ed3335df-5754-4038-a026-d39ce0cf2ebd"), "Cat3");

        Page<Category> page = new PageImpl<>(
                List.of(cat1, cat2, cat3)
        );
        when(categoryRepository.findByNameContainingIgnoreCase(any(String.class), any(Pageable.class))).thenReturn(page);
        //when
        Page<Category> pageResults = categoryService.getCategories("c", Pageable.ofSize(100));
        //then
        assertThat(pageResults)
                .hasSize(3)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Cat1", "Cat2", "Cat3");
    }

    @Test
    void getCategory_should_return_null_when_category_by_uuid_not_exists() {
        //given
        UUID uuid = UUID.randomUUID();
        when(categoryRepository.getReferenceById(uuid)).thenReturn(null);
        //when
        Category category = categoryService.getCategory(uuid);
        //then
        assertThat(category).isNull();
    }

    @Test
    void getCategory_should_return_category_details() {
        //given
        UUID uuid = UUID.randomUUID();
        Category category = new Category();
        category.setId(uuid);
        category.setQuestions(List.of(new Question(UUID.randomUUID(), "Question 1")));
        when(categoryRepository.getReferenceById(uuid)).thenReturn(category);
        //when
        Category categoryExpect = categoryService.getCategory(uuid);
        //then
        assertThat(categoryExpect).isNotNull();
        assertThat(categoryExpect.getId()).isEqualTo(category.getId());
        assertThat(categoryExpect.getName()).isEqualTo(category.getName());
        assertThat(categoryExpect.getQuestions()).isEqualTo(category.getQuestions());
    }

    @Test
    void createCategory_should_create_category() {
        //given
        UUID uuid = UUID.randomUUID();
        Category category = new Category();
        category.setName("Category 1");
        category.setQuestions(List.of(new Question(UUID.randomUUID(), "Question 1")));
        when(uuidUtils.generate()).thenReturn(uuid);
        when(categoryRepository.save(any())).thenAnswer(
                (InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        //when
        Category categorySaved = categoryService.createCategory(category);
        //then
        assertThat(categorySaved).isNotNull();
        assertThat(categorySaved.getId()).isEqualTo(uuid);
        assertThat(categorySaved.getName()).isEqualTo(category.getName());
        assertThat(categorySaved.getQuestions()).isEqualTo(category.getQuestions());
    }

    @Test
    void updateCategory_should_update_category() {
        //given
        UUID uuid = UUID.randomUUID();
        Category currentCategory = new Category();
        currentCategory.setId(uuid);
        currentCategory.setName("Category 1");
        currentCategory.setQuestions(List.of(new Question(UUID.randomUUID(), "Question 1")));
        Category categoryToUpdate = new Category();
        categoryToUpdate.setName("New Category Name");
        when(categoryRepository.getReferenceById(uuid)).thenReturn(currentCategory);
        when(categoryRepository.save(any())).thenAnswer(
                (InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        //when
        Category categorySaved = categoryService.updateCategory(uuid, categoryToUpdate);
        //then
        assertThat(categorySaved).isNotNull();
        assertThat(categorySaved.getId()).isEqualTo(uuid);
        assertThat(categorySaved.getName()).isEqualTo(categoryToUpdate.getName());
        assertThat(categorySaved.getQuestions()).isEqualTo(currentCategory.getQuestions());
    }

    @Test
    void updateCategory_should_throw_exception_when_category_by_uuid_not_exists() {
        //given
        UUID uuid = UUID.randomUUID();
        Category category = new Category();
        category.setQuestions(List.of(new Question(UUID.randomUUID(), "Question 1")));
        when(categoryRepository.getReferenceById(uuid)).thenReturn(null);
        when(categoryRepository.save(any())).thenAnswer(
                (InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        //when
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(uuid, category));
        //then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage())
                .isEqualTo(String.format("Category by ID: %s not exist", uuid));
    }
}