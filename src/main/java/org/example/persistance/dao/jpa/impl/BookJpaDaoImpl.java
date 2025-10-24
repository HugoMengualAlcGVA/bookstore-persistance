package org.example.persistance.dao.jpa.impl;

import org.example.persistance.dao.jpa.BookJpaDao;
import org.example.persistance.dao.jpa.entity.BookJpaEntity;

import java.util.List;
import java.util.Optional;

public class BookJpaDaoImpl implements BookJpaDao {
    @Override
    public Optional<BookJpaEntity> findByIsbn(String isbn) {
        return Optional.empty();
    }

    @Override
    public boolean deleteByIsbn(String isbn) {
        return false;
    }

    @Override
    public List<BookJpaEntity> findAll(int page, int size) {
        return List.of();
    }

    @Override
    public Optional<BookJpaEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public BookJpaEntity insert(BookJpaEntity entity) {
        return null;
    }

    @Override
    public BookJpaEntity update(BookJpaEntity entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public long count() {
        return 0;
    }
}
