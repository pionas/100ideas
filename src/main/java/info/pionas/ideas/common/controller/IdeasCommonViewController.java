package info.pionas.ideas.common.controller;

import info.pionas.ideas.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

public abstract class IdeasCommonViewController {

    @Autowired
    protected CategoryService categoryService;

    protected void addGlobalAttributes(Model model) {
        model.addAttribute("categoriesTop", categoryService.getCategories(
                PageRequest.of(0, 10, Sort.by("name").ascending())
        ));
    }
}
