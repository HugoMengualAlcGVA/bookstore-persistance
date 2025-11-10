package org.example.persistance.repository;

import es.cesguiro.domain.repository.PublisherRepository;
import es.cesguiro.domain.repository.entity.PublisherEntity;
import org.example.persistance.dao.jpa.PublisherJpaDao;
import org.example.persistance.repository.mapper.PublisherMapper;

import java.util.List;
import java.util.Optional;

public class PublisherRepositoryImpl implements PublisherRepository {
    private final PublisherJpaDao publisherDao;

    public PublisherRepositoryImpl(PublisherJpaDao publisherDao) {
        this.publisherDao = publisherDao;
    }

    @Override
    public Optional<PublisherEntity> findById(Long id) {
        return publisherDao.findById(id)
                .map(PublisherMapper.INSTANCE::fromPublisherJpaEntityToPublisherEntity);
    }

    @Override
    public Optional<PublisherEntity> findBySlug(String slug) {
        return publisherDao.findBySlug(slug)
                .map(PublisherMapper.INSTANCE::fromPublisherJpaEntityToPublisherEntity);
    }

    @Override
    public PublisherEntity save(PublisherEntity publisherEntity) {
        return null;
    }

    public List<PublisherEntity> findAll(int page, int size) {
        return publisherDao.findAll(page, size)
                .stream()
                .map(PublisherMapper.INSTANCE::fromPublisherJpaEntityToPublisherEntity)
                .toList();
    }
}

