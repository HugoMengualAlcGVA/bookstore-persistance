package org.example.persistance.repository.mapper;

import es.cesguiro.domain.exception.BusinessException;
import es.cesguiro.domain.repository.entity.PublisherEntity;
import org.example.persistance.dao.jpa.entity.PublisherJpaEntity;

public class PublisherMapper {
    private static PublisherMapper INSTANCE;

    private  PublisherMapper() {
    }

    public static PublisherMapper getInstance() {
        if(INSTANCE == null){
            INSTANCE = new PublisherMapper();
        }
        return INSTANCE;
    }

    public PublisherEntity fromPublisherJpaEntityToPublisherEntity(
            org.example.persistance.dao.jpa.entity.PublisherJpaEntity publisherJpaEntity) {
        if(publisherJpaEntity == null){
            throw new BusinessException("PublisherJpaEntity cannot be null");
        }
        return new PublisherEntity(
                publisherJpaEntity.getId(),
                publisherJpaEntity.getName(),
                publisherJpaEntity.getSlug()
        );
    }

    public PublisherJpaEntity fromPublisherEntityToPublisherJpaEntity(
            PublisherEntity publisher) {
        if(publisher == null){
            throw new BusinessException("Publisher cannot be null");
        }
        return new PublisherJpaEntity(
                publisher.id(),
                publisher.name(),
                publisher.slug()
        );
    }
}