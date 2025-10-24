package org.example.persistance.dao.jpa;

import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;

import java.util.Optional;

public interface AuthorJpaDao extends GenericJpaDao<AuthorJpaEntity> {
     Optional<AuthorJpaEntity> findBySlug(String slug);
}
