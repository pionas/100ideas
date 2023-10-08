package info.pionas.ideas.category.mapper;

import info.pionas.ideas.category.domain.model.Category;
import info.pionas.ideas.category.dto.*;
import info.pionas.ideas.question.domain.model.Answer;
import info.pionas.ideas.question.domain.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper
public interface CategoryMapper {

    CategoryPage map(Page<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questions", ignore = true)
    Category map(CreateCategory createCategory);

    CategoryDto map(Category category);

    QuestionDto map(Question question);

    AnswerDto map(Answer answer);
}
