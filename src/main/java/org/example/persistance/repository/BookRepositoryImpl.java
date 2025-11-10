package org.example.persistance.repository;

import es.cesguiro.domain.model.Page;
import es.cesguiro.domain.repository.BookRepository;
import es.cesguiro.domain.repository.entity.BookEntity;
import org.example.persistance.dao.jpa.BookJpaDao;
import org.example.persistance.dao.jpa.entity.BookJpaEntity;
import org.example.persistance.repository.mapper.BookMapper;

import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository{

    private final BookJpaDao bookJpaDao;

    public BookRepositoryImpl(BookJpaDao bookJpaDao) {
        this.bookJpaDao = bookJpaDao;
    }

    @Override
    public Page<BookEntity> findAll(int page, int size) {
        List<BookEntity> content = bookJpaDao.findAll(page, size).stream()
                .map(BookMapper.INSTANCE::fromBookJpaEntityToBookEntity)
                .toList();
        long totalElements = bookJpaDao.count();
        return new Page<>(content, page, size, totalElements);
    }


    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookJpaDao.findByIsbn(isbn)
                .map(BookMapper.INSTANCE::fromBookJpaEntityToBookEntity);
    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        BookJpaEntity bookJpaEntity = BookMapper.INSTANCE.fromBookEntityToBookJpaEntity(bookEntity);
        if(bookEntity.id() == null) {
            return BookMapper.INSTANCE.fromBookJpaEntityToBookEntity(bookJpaDao.insert(bookJpaEntity));
        }
        return BookMapper.INSTANCE.fromBookJpaEntityToBookEntity(bookJpaDao.update(bookJpaEntity));
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return bookJpaDao.findById(id)
                .map(BookMapper.INSTANCE::fromBookJpaEntityToBookEntity);
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookJpaDao.deleteByIsbn(isbn);
    }
}
