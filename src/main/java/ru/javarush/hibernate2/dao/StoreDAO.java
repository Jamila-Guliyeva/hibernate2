package ru.javarush.hibernate2.dao;

import org.hibernate.SessionFactory;
import ru.javarush.hibernate2.domain.Store;

public class StoreDAO extends GenericDAO<Store>{
    public StoreDAO(SessionFactory sessionFactory) {
        super(Store.class, sessionFactory);
    }
}
