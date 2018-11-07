package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.model.User;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.brigade.exception.InsufficientAmountException;
import cz.cvut.kbss.ear.eshop.model.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;


public class CartServiceTest extends BaseServiceTestRunner {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CartService sut;

    private Cart cart;

    @Before
    public void setUp() {
        final User owner = Generator.generateUser();
        this.cart = new Cart();
        owner.setCart(cart);
        em.persist(owner);
    }

    @Test
    public void addItemAddsItemIntoCart() {
        final Product product = new Product();
        product.setName("test product");
        product.setAmount(5);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(1);
        sut.addItem(cart, toAdd);

        final Cart result = em.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(toAdd.getAmount(), result.getItems().get(0).getAmount());
        assertEquals(toAdd.getProduct(), result.getItems().get(0).getProduct());
    }

    //no
    @Test
    public void addItemUpdatesProductAmountBySubtractingAmountSpecifiedInItem() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(1);
        sut.addItem(cart, toAdd);

        final Product result = em.find(Product.class, product.getId());
        assertEquals(productAmount - toAdd.getAmount(), result.getAmount().intValue());
    }

    //no
    @Test
    public void addItemUpdatesExistingItemIfItHasSameProduct() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        item.setAmount(1);
        sut.addItem(cart, item);
        System.out.println("item.getAmount()" + item.getAmount());
        final CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setAmount(2);
        System.out.println("item.getAmount()" + item.getAmount());

        sut.addItem(cart, newItem);
        System.out.println("item.getAmount()" + item.getAmount());

        final Cart result = em.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        System.out.println("item.getAmount()" + item.getAmount());
        System.out.println("newItem.getAmount()" + newItem.getAmount());
        assertEquals(item.getAmount() + newItem.getAmount(), result.getItems().get(0).getAmount().intValue());
    }


//no
    @Test
    public void addItemThrowsInsufficientAmountExceptionWhenNotEnoughProductExistsForItem() {
        thrown.expect(InsufficientAmountException.class);
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(productAmount + 1);
        sut.addItem(cart, toAdd);
    }

    @Test
    public void removeItemRemovesItemFromCart() {
        final Product product = new Product();
        product.setName("test product");
        product.setAmount(5);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        item.setAmount(1);
        cart.addItem(item);
        em.persist(item);
        em.merge(cart);
        sut.removeItem(cart, item);

        final Cart result = em.find(Cart.class, cart.getId());
        assertTrue(result.getItems().isEmpty());
    }

    //no
    @Test
    public void removeItemUpdatesProductAmount() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        item.setAmount(1);
        cart.addItem(item);
        em.persist(item);
        em.merge(cart);
        sut.removeItem(cart, item);

        final Product result = em.find(Product.class, product.getId());
        assertEquals(productAmount + item.getAmount(), result.getAmount().intValue());
    }

    @Test
    public void removeItemUpdatesExistingItemAmountWhenToRemoveAmountIsSmallerThanAmountInCart() {
        final Product product = new Product();
        product.setName("test product");
        product.setAmount(5);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        int origAmount = 4;
        item.setAmount(origAmount);
        cart.addItem(item);
        em.persist(item);
        em.merge(cart);

        final CartItem toRemove = new CartItem();
        toRemove.setProduct(product);
        toRemove.setAmount(item.getAmount() - 1);
        sut.removeItem(cart, toRemove);

        final Cart result = em.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(origAmount - toRemove.getAmount(), result.getItems().get(0).getAmount().intValue());
    }
}