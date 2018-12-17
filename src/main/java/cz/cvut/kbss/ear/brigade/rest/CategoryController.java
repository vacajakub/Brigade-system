package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;


    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getWorkers() {
        final List<Category> categories = categoryService.findAll();
        if (categories == null) {
            throw NotFoundException.create("Categories", "findAll()");
        }
        return categories;
    }
}
