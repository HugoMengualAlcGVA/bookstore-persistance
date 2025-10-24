package org.example.persistance.dao.jpa.impl;

import es.cesguiro.domain.exception.BusinessException;
import es.cesguiro.domain.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.persistance.dao.jpa.AuthorJpaDao;
import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;
import org.example.persistance.dao.jpa.entity.BookJpaEntity;

import java.util.List;
import java.util.Optional;

public class AuthorJpaDaoImpl implements AuthorJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AuthorJpaEntity> findBySlug(String slug) {
        return Optional.ofNullable(entityManager.find(AuthorJpaEntity.class, slug));
    }

    @Override
    public List<AuthorJpaEntity> findAll(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);

        String sql = "select a from AuthorJpaEntity a";
        TypedQuery<AuthorJpaEntity> query = entityManager.
                createQuery(sql, AuthorJpaEntity.class)
                .setFirstResult(pageIndex*size)
                .setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Optional<AuthorJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(AuthorJpaEntity.class, id));
    }

    @Override
    public AuthorJpaEntity insert(AuthorJpaEntity entity) {
        try {
            entityManager.persist(entity);
            return entity;
        }catch(Exception e) {
            return null;
        }
    }

    @Override
    public AuthorJpaEntity update(AuthorJpaEntity entity) {
        if(entity !=null) {
            AuthorJpaEntity managed = entityManager.find(AuthorJpaEntity.class, entity.getId());
            if(managed == null) {
                throw new ResourceNotFoundException("Author not found with id " + entity.getId());
            }

            managed.getBookAuthors().clear();
            entityManager.flush();
            return entityManager.merge(entity);
        }
        throw new BusinessException("Invalid AuthorJpaEntity");
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(AuthorJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(a) from AuthorJpaEntity a", Long.class).getSingleResult();
    }
}
