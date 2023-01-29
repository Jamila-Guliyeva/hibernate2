package ru.javarush.hibernate2.dao;

import org.hibernate.SessionFactory;
import ru.javarush.hibernate2.domain.Inventory;

public class InventoryDAO extends GenericDAO<Inventory> {
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }
}
