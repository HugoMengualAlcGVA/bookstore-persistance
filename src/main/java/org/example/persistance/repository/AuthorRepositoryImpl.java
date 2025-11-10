package org.example.persistance.repository;

import es.cesguiro.domain.repository.AuthorRepository;
import es.cesguiro.domain.repository.entity.AuthorEntity;
import org.example.persistance.dao.jpa.AuthorJpaDao;
import org.example.persistance.repository.mapper.AuthorMapper;

import java.util.Optional;

public class AuthorRepositoryImpl implements AuthorRepository {
    private final AuthorJpaDao authorJpaDao;

    public AuthorRepositoryImpl(AuthorJpaDao authorJpaDao) {
        this.authorJpaDao = authorJpaDao;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return null;
    }

    @Override
    public Optional<AuthorEntity> findById(Long id) {
        return authorJpaDao.findById(id)
                .map(AuthorMapper.INSTANCE::fromAuthorJpaEntityToAuthorEntity);
    }
}
