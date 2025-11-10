package org.example.persistance;

import jakarta.persistence.EntityManager;
import org.example.persistance.dao.jpa.AuthorJpaDao;
import org.example.persistance.dao.jpa.BookJpaDao;
import org.example.persistance.dao.jpa.PublisherJpaDao;
import org.example.persistance.dao.jpa.impl.AuthorJpaDaoImpl;
import org.example.persistance.dao.jpa.impl.BookJpaDaoImpl;
import org.example.persistance.dao.jpa.impl.PublisherJpaDaoImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
public class TestConfig {
    @Bean
    public PublisherJpaDao publisherJpaDao(EntityManager entityManager) {
        return new PublisherJpaDaoImpl();
    }

    @Bean
    public BookJpaDao bookJpaDao(EntityManager entityManager) {
        return new BookJpaDaoImpl();
    }

    @Bean
    public AuthorJpaDao authorJpaDao(EntityManager entityManager) {
        return new AuthorJpaDaoImpl();
    }

}
