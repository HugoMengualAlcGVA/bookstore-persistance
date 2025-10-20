package org.example.persistance.dao.jpa;

import org.example.persistance.dao.jpa.entity.PublisherJpaEntity;

import java.util.Optional;

public interface PublisherJpaDao extends BookJpaDao {
    Optional<PublisherJpaEntity> findBySlug(String slug);
}
