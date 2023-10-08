package info.pionas.ideas.question.service;

import info.pionas.ideas.question.domain.model.Question;
import info.pionas.ideas.question.dto.QuestionDto;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionDto map(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setName(question.getName());
        questionDto.setCategoryId(question.getCategory().getId());
        questionDto.setCategoryName(question.getCategory().getName());
        questionDto.setAnswers(question.getAnswers() == null ? 0 : question.getAnswers().size());
        questionDto.setCreated(question.getCreated());

        return questionDto;
    }
}