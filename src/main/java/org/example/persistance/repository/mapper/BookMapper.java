package org.example.persistance.repository.mapper;

import es.cesguiro.domain.repository.entity.AuthorEntity;
import es.cesguiro.domain.repository.entity.BookEntity;
import org.example.persistance.dao.jpa.entity.BookJpaEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BookMapper {
    private static BookMapper INSTANCE;

    private BookMapper(){}

    public static BookMapper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BookMapper();
        }
        return INSTANCE;
    }

    public BookEntity fromBookJpaEntityToBookEntity(BookJpaEntity bookJpaEntity){
        if(bookJpaEntity == null){
            throw new IllegalArgumentException("BookJpaEntity cannot be null");
        }
        List<AuthorEntity> authors = null;
        if(bookJpaEntity.getAuthors() != null && !bookJpaEntity.getAuthors().isEmpty()){
            authors = bookJpaEntity.getAuthors().stream()
                    .map(AuthorMapper.getInstance()::fromAuthorJpaEntityToAuthorEntity)
                    .toList();

        }
        return new BookEntity(
                bookJpaEntity.getId(),
                bookJpaEntity.getIsbn(),
                bookJpaEntity.getTitleEs(),
                bookJpaEntity.getTitleEn(),
                bookJpaEntity.getSynopsisEs(),
                bookJpaEntity.getSynopsisEn(),
                bookJpaEntity.getBasePrice(),
                bookJpaEntity.getDiscountPercentage(),
                bookJpaEntity.getCover(),
                LocalDate.parse(bookJpaEntity.getPublicationDate()),
                PublisherMapper.getInstance().fromPublisherJpaEntityToPublisherEntity(bookJpaEntity.getPublisher()),
                authors
        );

    }

    public BookJpaEntity fromBookEntityToBookJpaEntity(BookEntity bookEntity){
        if(bookEntity == null){
            throw new IllegalArgumentException("BookEntity cannot be null");
        }
        List<org.example.persistance.dao.jpa.entity.AuthorJpaEntity> authors = null;
        if(bookEntity.authors() != null && !bookEntity.authors().isEmpty()){
            authors = bookEntity.authors().stream()
                    .map(AuthorMapper.getInstance()::fromAuthorEntityToAuthorJpaEntity)
                    .toList();
        }
        return new BookJpaEntity(
                bookEntity.id(),
                bookEntity.isbn(),
                bookEntity.titleEs(),
                bookEntity.titleEn(),
                bookEntity.synopsisEs(),
                bookEntity.synopsisEn(),
                bookEntity.basePrice(),
                bookEntity.discountPercentage(),
                bookEntity.cover(),
                Date.valueOf(bookEntity.publicationDate()).toString(),
                PublisherMapper.getInstance().fromPublisherEntityToPublisherJpaEntity(bookEntity.publisher()),
                authors
        );
    }
}
