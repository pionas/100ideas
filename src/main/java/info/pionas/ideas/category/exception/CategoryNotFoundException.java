package info.pionas.ideas.category.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(UUID id) {
        super(String.format("Category by ID: %s not exist", id));
    }
}
