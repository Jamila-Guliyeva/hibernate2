package ru.javarush.hibernate2.dao;

import org.hibernate.SessionFactory;
import ru.javarush.hibernate2.domain.Country;

public class CountryDAO extends GenericDAO<Country> {
    public CountryDAO(SessionFactory sessionFactory) {
        super(Country.class, sessionFactory);
    }
}
