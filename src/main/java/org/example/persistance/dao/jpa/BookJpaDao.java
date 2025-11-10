package org.example.persistance.dao.jpa;

import org.example.persistance.dao.jpa.entity.BookJpaEntity;

import java.util.Optional;

public interface BookJpaDao extends GenericJpaDao<BookJpaEntity> {
    Optional<BookJpaEntity> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
