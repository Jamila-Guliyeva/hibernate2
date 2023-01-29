package ru.javarush.hibernate2.dao;

import org.hibernate.SessionFactory;
import ru.javarush.hibernate2.domain.Customer;

public class CustomerDAO extends GenericDAO<Customer> {
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }
}
