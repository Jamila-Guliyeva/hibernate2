package ru.javarush.hibernate2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ru.javarush.hibernate2.dao.*;
import ru.javarush.hibernate2.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Main {

    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public Main() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "rootpassword");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {

        Main main = new Main();
        Customer customer = main.createCustomer();
        
        main.customerReturnInventoryToStore();
        main.customerRentInventory(customer);
        main.newFilmHasMade();

    }

    private void newFilmHasMade() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Language language = languageDAO.getItems(0, 20).stream().unordered().findAny().get();
            List<Category> categories = categoryDAO.getItems(1, 5);
            List<Actor> actors = actorDAO.getItems(0, 20);

            Film film = new Film();
            film.setActors(new HashSet<>(actors));
            film.setRating(Rating.NC);
            film.setSpecialFeature(Set.of(Feature.COMMENTARIES, Feature.TRAILERS));
            film.setLength((short)123);
            film.setReplacementCost(BigDecimal.TEN);
            film.setLanguageId(language);
            film.setDescription("Some movie");
            film.setTitle("Some movie");
            film.setOriginalLanguageId(language);
            film.setCategories(new HashSet<>(categories));
            film.setReleaseYear(Year.now());
            film.setRentalDuration((byte)123);
            film.setRentalRate(BigDecimal.TEN);
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setId((short)film.getId());
            filmText.setFilm(film);
            filmText.setDescription("Some movie");
            filmText.setTitle("Some movie");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        }
    }

    private void customerRentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Store store = storeDAO.getItems(0, 1).get(0);

            Film film = filmDAO.getFirstAvailableFilmForRent();

            Staff staff = store.getStaff();

            Inventory inventory = new Inventory();
            inventory.setFilms(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setCustomer(customer);
            rental.setStaff(staff);
            rental.setInventory(inventory);
            rentalDAO.save(rental);

            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setAmount(BigDecimal.valueOf(55.77));
            payment.setCustomer(customer);
            payment.setStaff(staff);
            paymentDAO.save(payment);

            session.getTransaction().commit();
        }
    }

    private void customerReturnInventoryToStore() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Rental rental = rentalDAO.getAnyUnreturnedRental();

            rental.setReturnDate(LocalDateTime.now());

            rentalDAO.save(rental);
            session.getTransaction().commit();
        }
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()){
            session.beginTransaction();

            Store store = storeDAO.getItems(0, 1).get(0);

            City city = cityDAO.getByName("Kragujevac");

            Address address = new Address();
            address.setAddress("Street ave, 62/57");
            address.setCity(city);
            address.setPhone("777-33-99");
            address.setDistrict("Some District");

            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setAddress(address);
            customer.setActive(true);
            customer.setEmail("someMail@gmail.com");
            customer.setFirstName("Jama");
            customer.setLastName("Guliyeva");
            customer.setStore(store);

            customerDAO.save(customer);

            session.getTransaction().commit();
            return customer;
        }
    }
}