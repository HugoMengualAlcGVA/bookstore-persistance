package org.example.persistance.repository.mapper;

import es.cesguiro.domain.exception.BusinessException;
import es.cesguiro.domain.repository.entity.AuthorEntity;
import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;

public class AuthorMapper {
    private static AuthorMapper INSTANCE;

    private AuthorMapper() {}

    public static AuthorMapper getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AuthorMapper();
        }
        return INSTANCE;
    }

    public AuthorJpaEntity fromAuthorEntityToAuthorJpaEntity(AuthorEntity author) {
        if(author == null) {
            throw new BusinessException("Author cannot be null");
        }
        return new AuthorJpaEntity(
            null,
            author.name(),
            author.nationality(),
            author.biographyEs(),
            author.biographyEn(),
            author.birthYear(),
            author.deathYear(),
            author.slug(),
            null
        );
    }

    public AuthorEntity fromAuthorJpaEntityToAuthorEntity(AuthorJpaEntity authorJpaEntity) {
        if(authorJpaEntity == null) {
            throw new BusinessException("AuthorJpaEntity cannot be null");
        }
        return new AuthorEntity(
            authorJpaEntity.getId(),
            authorJpaEntity.getName(),
            authorJpaEntity.getNationality(),
            authorJpaEntity.getBiographyEs(),
            authorJpaEntity.getBiographyEn(),
            authorJpaEntity.getBirthYear(),
            authorJpaEntity.getDeathYear(),
            authorJpaEntity.getSlug()
        );
    }
}
