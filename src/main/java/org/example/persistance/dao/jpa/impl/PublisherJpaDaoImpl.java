package org.example.persistance.dao.jpa.impl;

import es.cesguiro.domain.exception.BusinessException;
import es.cesguiro.domain.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.persistance.dao.jpa.PublisherJpaDao;
import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;
import org.example.persistance.dao.jpa.entity.BookJpaEntity;
import org.example.persistance.dao.jpa.entity.PublisherJpaEntity;

import java.util.List;
import java.util.Optional;

public class PublisherJpaDaoImpl implements PublisherJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PublisherJpaEntity> findBySlug(String slug) {
        return Optional.ofNullable(entityManager.find(PublisherJpaEntity.class, slug));
    }

    @Override
    public List<PublisherJpaEntity> findAll(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);

        String sql = "select a from PublisherJpaEntity a";
        TypedQuery<PublisherJpaEntity> query = entityManager.
                createQuery(sql, PublisherJpaEntity.class)
                .setFirstResult(pageIndex*size)
                .setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Optional<PublisherJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(PublisherJpaEntity.class, id));
    }

    @Override
    public PublisherJpaEntity insert(PublisherJpaEntity entity) {
        try {
            entityManager.persist(entity);
            return entity;
        }catch(Exception e) {
            return null;
        }
    }

    @Override
    public PublisherJpaEntity update(PublisherJpaEntity entity) {
        if(entity !=null) {
            PublisherJpaEntity managed = entityManager.find(PublisherJpaEntity.class, entity.getId());
            if(managed == null) {
                throw new ResourceNotFoundException("Book not found with id " + entity.getId());
            }
            return entityManager.merge(entity);
        }
        throw new BusinessException("Invalid AuthorJpaEntity");
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(PublisherJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(p) from PublisherJpaEntity p", Long.class).getSingleResult();
    }
}
