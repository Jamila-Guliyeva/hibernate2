package ru.javarush.hibernate2.dao;

import org.hibernate.SessionFactory;
import ru.javarush.hibernate2.domain.Category;

public class CategoryDAO extends GenericDAO<Category> {
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
