package cz.cvut.kbss.ear.brigade.dao;

import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ProductDaoTest extends BaseDaoTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProductDao sut;

    @Test
    public void findAllByCategoryReturnsProductsInSpecifiedCategory() {
        final Category cat = generateCategory("testCategory");

        final List<Product> products = generateProducts(cat);
        final List<Product> result = sut.findAll(cat);
        assertEquals(products.size(), result.size());
        products.sort(Comparator.comparing(Product::getName));
        result.sort(Comparator.comparing(Product::getName));
        for (int i = 0; i < products.size(); i++) {
            assertEquals(products.get(i).getId(), result.get(i).getId());
        }
    }

    private Category generateCategory(String name) {
        final Category cat = new Category();
        cat.setName(name);
        em.persist(cat);
        return cat;
    }

    private List<Product> generateProducts(Category category) {
        final List<Product> inCategory = new ArrayList<>();
        final Category other = generateCategory("otherCategory");
        for (int i = 0; i < 10; i++) {
            final Product p = Generator.generateProduct();
            p.addCategory(other);
            if (Generator.randomBoolean()) {
                p.addCategory(category);
                inCategory.add(p);
            }
            em.persist(p);
        }
        return inCategory;
    }

    @Test
    public void findAllReturnsOnlyNonRemovedProducts() {
        final Category cat = generateCategory("testCategory");
        final List<Product> products = IntStream.range(0, 10).mapToObj(i -> {
            final Product p = Generator.generateProduct();
            p.addCategory(cat);
            p.setRemoved(Generator.randomBoolean());
            return p;
        }).collect(Collectors.toList());
        products.forEach(em::persist);

        final List<Product> result = sut.findAll();
        assertEquals(products.stream().filter(p -> !p.isRemoved()).count(), result.size());
        result.forEach(p -> assertFalse(p.isRemoved()));
    }

    @Test
    public void findAllByCategoryReturnsOnlyNonRemovedProducts() {
        final Category cat = generateCategory("testCategory");

        final List<Product> products = generateProducts(cat);
        products.forEach(p -> {
            p.setRemoved(Generator.randomBoolean());
            em.merge(p);
        });
        final List<Product> result = sut.findAll(cat);
        result.forEach(p -> {
            assertTrue(p.getCategories().stream().anyMatch(c -> c.getId().equals(cat.getId())));
            assertFalse(p.isRemoved());
        });
    }
}