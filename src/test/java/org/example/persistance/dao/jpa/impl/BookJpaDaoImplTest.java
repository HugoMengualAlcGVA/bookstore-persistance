package org.example.persistance.dao.jpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.persistance.TestConfig;
import org.example.persistance.dao.jpa.AuthorJpaDao;
import org.example.persistance.dao.jpa.BookJpaDao;
import org.example.persistance.dao.jpa.PublisherJpaDao;
import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;
import org.example.persistance.dao.jpa.entity.BookJpaEntity;
import org.example.persistance.dao.jpa.entity.PublisherJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookJpaDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BookJpaDao bookJpaDao;

    @Autowired
    private PublisherJpaDao publisherJpaDao;

    @Autowired
    private AuthorJpaDao authorJpaDao;


    @Test
    @DisplayName("Test insert method persists BookJpaEntity")
    void testInsert() {
        PublisherJpaEntity publisherJpaEntity = new PublisherJpaEntity();
        publisherJpaEntity.setName("Editorial X");
        publisherJpaEntity.setSlug("editorial-x");
        entityManager.persist(publisherJpaEntity);

        AuthorJpaEntity authorJpaEntity1 = new AuthorJpaEntity();
        authorJpaEntity1.setName("Author One");
        entityManager.persist(authorJpaEntity1);
        AuthorJpaEntity authorJpaEntity2 = new AuthorJpaEntity();
        authorJpaEntity2.setName("Author Two");
        entityManager.persist(authorJpaEntity2);

        /*BookJpaEntity bookEntity1 = new BookJpaEntity(
                null,
                "9780316073797",
                "La espada del destino",
                "Sword of Destiny",
                "La espada del destino es una colección de relatos que sigue las aventuras de Geralt de Rivia mientras enfrenta a monstruos y se cruza con personajes importantes en su vida.",
                "Sword of Destiny is a collection of stories following Geralt of Rivia's adventures as he faces monsters and encounters important characters in his life.",
                BigDecimal.valueOf(18.99),
                5.00,
                "http://images.cesguiro.es/books/9780316073797.jpeg",
                "1992-11-01",
                publisherJpaEntity,
                List.of(authorJpaEntity1, authorJpaEntity2)
        );*/

        BookJpaEntity newBook = new BookJpaEntity(
                null,
                "666666666666",
                "New Book Title ES",
                "New Book Title EN",
                "New Book Synopsis ES",
                "New Book Synopsis EN",
                BigDecimal.valueOf(29.99),
                10.0,
                "new_book_cover.jpg",
                LocalDate.of(2024, 1, 1).toString(),
                publisherJpaEntity, // Assuming the first publisher exists
                List.of(authorJpaEntity1, authorJpaEntity2) // Assuming the first two authors exist
        );

        String sql = "SELECT COUNT(b) FROM BookJpaEntity b";
        long countBefore = entityManager.createQuery(sql, Long.class)
                .getSingleResult();

        BookJpaEntity result = bookJpaDao.insert(newBook);

        long countAfter = entityManager.createQuery(sql, Long.class)
                .getSingleResult();

        long lastId = entityManager.createQuery("SELECT MAX(b.id) FROM BookJpaEntity b", Long.class)
                .getSingleResult();

        Set<Long> expectedAuthorIds = newBook.getAuthors().stream()
                .map(AuthorJpaEntity::getId)
                .collect(Collectors.toSet());
        Set<Long> resultAuthorIds = result.getAuthors().stream()
                .map(AuthorJpaEntity::getId)
                .collect(Collectors.toSet());

        Optional<BookJpaEntity> insertedBook = bookJpaDao.findByIsbn(newBook.getIsbn());

        Set<Long> insertedAuthorIds = result.getAuthors().stream()
                .map(AuthorJpaEntity::getId)
                .collect(Collectors.toSet());

        assertAll(
                () -> assertNotNull(result, "Insert didn't return any book"),
                () -> assertEquals(lastId, result.getId()),
                () -> assertEquals(newBook.getIsbn(), result.getIsbn()),
                () -> assertEquals(newBook.getTitleEs(), result.getTitleEs()),
                () -> assertEquals(newBook.getTitleEn(), result.getTitleEn()),
                () -> assertEquals(newBook.getSynopsisEs(), result.getSynopsisEs()),
                () -> assertEquals(newBook.getSynopsisEn(), result.getSynopsisEn()),
                () -> assertEquals(newBook.getBasePrice(), result.getBasePrice()),
                () -> assertEquals(newBook.getDiscountPercentage(), result.getDiscountPercentage()),
                () -> assertEquals(newBook.getCover(), result.getCover()),
                () -> assertEquals(newBook.getPublicationDate(), result.getPublicationDate()),
                () -> assertEquals(newBook.getPublisher().getId(), result.getPublisher().getId()),
                () -> assertEquals(newBook.getAuthors().size(), result.getAuthors().size()),
                () -> assertEquals(expectedAuthorIds, resultAuthorIds)
        );

        assertNotNull(insertedBook, "No book was inserted");
        insertedBook.ifPresent(iBook -> assertAll(
                () -> assertEquals(newBook.getIsbn(), iBook.getIsbn()),
                () -> assertEquals(newBook.getTitleEs(), iBook.getTitleEs()),
                () -> assertEquals(newBook.getTitleEn(), iBook.getTitleEn()),
                () -> assertEquals(newBook.getSynopsisEs(), iBook.getSynopsisEs()),
                () -> assertEquals(newBook.getSynopsisEn(), iBook.getSynopsisEn()),
                () -> assertEquals(newBook.getBasePrice(), iBook.getBasePrice()),
                () -> assertEquals(newBook.getDiscountPercentage(), iBook.getDiscountPercentage()),
                () -> assertEquals(newBook.getCover(), iBook.getCover()),
                () -> assertEquals(newBook.getPublicationDate(), iBook.getPublicationDate()),
                () -> assertEquals(newBook.getPublisher().getId(), iBook.getPublisher().getId()),
                () -> assertEquals(newBook.getAuthors().size(), iBook.getAuthors().size()),
                () -> assertEquals(expectedAuthorIds, insertedAuthorIds),
                () -> assertEquals(countBefore + 1, countAfter)
        ));
    }

    @Test
    @DisplayName("Test count() does in fact count properly")
    void countTest(){
        String sql = "SELECT COUNT(b) FROM BookJpaEntity b";
        long expectedCount = entityManager.createQuery(sql, Long.class)
                .getSingleResult();
        long actualCount = bookJpaDao.count();

        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("Test DeleteByIsbn does delete the book")
    void testDeleteByIsbn() {
        long countBefore = bookJpaDao.count();

        String existingIsbn = bookJpaDao.findAll(1,1).getFirst().getIsbn();
        bookJpaDao.deleteByIsbn(existingIsbn);

        Optional<BookJpaEntity> resultAfter = bookJpaDao.findByIsbn(existingIsbn);
        long countAfter = bookJpaDao.count();

        assertAll(
                () -> assertEquals(resultAfter, Optional.empty()),
                () -> assertEquals(countBefore, countAfter + 1)
        );
    }

    @Test
    @DisplayName("Test FindByIsbn when isbn exists")
    void testFindByIsbn() {
        String existingIsbn = bookJpaDao.findAll(1,1).getFirst().getIsbn();
        BookJpaEntity result = bookJpaDao.findByIsbn(existingIsbn).get();

        assertEquals(existingIsbn, result.getIsbn());
    }

    @Test
    @DisplayName("Test FindByIsbn when isbn doesn't exist")
    void testFindByIsbnNonExistent() {
        String nonExistingIsbn = "Albacete";
        Optional<BookJpaEntity> result = bookJpaDao.findByIsbn(nonExistingIsbn);

        assertEquals(result, Optional.empty());
    }

    @Test
    @DisplayName("Test FindAll returns all books")
    void findAllTest(){
        List<BookJpaEntity> allBooks = bookJpaDao.findAll(1, 1000);
        long count = bookJpaDao.count();

        assertEquals(count, allBooks.size());
    }

    @Test
    @DisplayName("Test FindAll with pagination returns expected books")
    void findAllWithPaginationTest(){
        int page = 2;
        int size = 3;
        List<BookJpaEntity> pagedBooks = bookJpaDao.findAll(page, size);
        List<BookJpaEntity> nonPagedBooks = bookJpaDao.findAll(1, 6);

        assertAll(
                () -> assertEquals(size, pagedBooks.size()),
                () -> assertEquals(nonPagedBooks.get(3).getId(), pagedBooks.getFirst().getId()),
                () -> assertEquals(nonPagedBooks.get(4).getId(), pagedBooks.get(1).getId()),
                () -> assertEquals(nonPagedBooks.get(5).getId(), pagedBooks.get(2).getId())
        );
    }

    @Test
    @DisplayName("Test findById when id exists")
    void testFindById() {
        Long existingId = bookJpaDao.findAll(1, 1).getFirst().getId();
        String existingIsbn = bookJpaDao.findAll(1, 1).getFirst().getIsbn();
        BookJpaEntity result = bookJpaDao.findById(existingId).get();
        assertAll(
                () -> assertEquals(existingId, result.getId()),
                () -> assertEquals(existingIsbn, result.getIsbn()),
                () -> assertNotNull(result.getAuthors()),
                () -> assertNotNull(result.getBookAuthors()),
                () -> assertNotNull(result.getPublisher()),
                () -> assertNotNull(result.getCover()),
                () -> assertNotNull(result.getBasePrice()),
                () -> assertNotNull(result.getPublicationDate()),
                () -> assertNotNull(result.getPublicationDate()),
                () -> assertNotNull(result.getPublicationDate()),
                () -> assertNotNull(result.getSynopsisEn()),
                () -> assertNotNull(result.getSynopsisEn()),
                () -> assertNotNull(result.getTitleEs()),
                () -> assertNotNull(result.getTitleEs())
        );

    }

    @Test
    @DisplayName("Test findById when id exists")
    void testFindByIdNonExistent() {
        Long nonExistingId = 1231441L;
        Optional<BookJpaEntity> result = bookJpaDao.findById(nonExistingId);
        assertEquals(result, Optional.empty());
    }

    @Test
    @DisplayName("Test deleteById deletes expected book")
    void testDeleteById(){
        long countBefore = bookJpaDao.count();

        Long existingId = bookJpaDao.findAll(1,1).getFirst().getId();
        bookJpaDao.deleteById(existingId);

        Optional<BookJpaEntity> resultAfter = bookJpaDao.findById(existingId);
        long countAfter = bookJpaDao.count();

        assertAll(
                () -> assertEquals(resultAfter, Optional.empty()),
                () -> assertEquals(countBefore, countAfter + 1)
        );
    }

    @Test
    @DisplayName("Test update modifies expected book")
    void testUpdate(){
        BookJpaEntity existingBook = bookJpaDao.findAll(1, 1).getFirst();

        BookJpaEntity newBook = new BookJpaEntity(
                existingBook.getId(),
                existingBook.getIsbn(),
                "Updated Book Title ES",
                "Updated Book Title EN",
                existingBook.getSynopsisEs(),
                existingBook.getSynopsisEn(),
                BigDecimal.valueOf(39.99),
                15.0,
                "new_book_cover.jpg",
                LocalDate.of(2024, 1, 1).toString(),
                existingBook.getPublisher(), // Assuming the first publisher exists
                existingBook.getAuthors() // Assuming the first two authors exist
        );

        BookJpaEntity result = bookJpaDao.update(newBook);
        BookJpaEntity expectedBookToBeChanged = bookJpaDao.findById(existingBook.getId()).get();

        assertAll(
                () -> assertEquals(newBook.getId(), result.getId()),
                () -> assertEquals(newBook.getIsbn(), result.getIsbn()),
                () -> assertEquals(newBook.getTitleEs(), result.getTitleEs()),
                () -> assertEquals(newBook.getTitleEn(), result.getTitleEn()),
                () -> assertEquals(newBook.getSynopsisEs(), result.getSynopsisEs()),
                () -> assertEquals(newBook.getSynopsisEn(), result.getSynopsisEn()),
                () -> assertEquals(newBook.getBasePrice(), result.getBasePrice()),
                () -> assertEquals(newBook.getDiscountPercentage(), result.getDiscountPercentage()),
                () -> assertEquals(newBook.getCover(), result.getCover()),
                () -> assertEquals(newBook.getPublicationDate(), result.getPublicationDate()),
                () -> assertEquals(newBook.getPublisher().getId(), result.getPublisher().getId()),
                () -> assertEquals(newBook.getAuthors().size(), result.getAuthors().size()),
                () -> assertEquals(expectedBookToBeChanged.getId(), result.getId()),
                () -> assertEquals(expectedBookToBeChanged.getIsbn(), result.getIsbn()),
                () -> assertEquals(expectedBookToBeChanged.getTitleEs(), result.getTitleEs()),
                () -> assertEquals(expectedBookToBeChanged.getTitleEn(), result.getTitleEn()),
                () -> assertEquals(expectedBookToBeChanged.getSynopsisEs(), result.getSynopsisEs()),
                () -> assertEquals(expectedBookToBeChanged.getSynopsisEn(), result.getSynopsisEn()),
                () -> assertEquals(expectedBookToBeChanged.getBasePrice(), result.getBasePrice()),
                () -> assertEquals(expectedBookToBeChanged.getDiscountPercentage(), result.getDiscountPercentage()),
                () -> assertEquals(expectedBookToBeChanged.getCover(), result.getCover()),
                () -> assertEquals(expectedBookToBeChanged.getPublicationDate(), result.getPublicationDate()),
                () -> assertEquals(expectedBookToBeChanged.getPublisher().getId(), result.getPublisher().getId()),
                () -> assertEquals(expectedBookToBeChanged.getAuthors().size(), result.getAuthors().size())
        );

    }
}


    /*@ParameterizedTest
    @DisplayName("Test update method modifies existing BookJpaEntity")
    @Transactional
    @CsvSource({
        "9777777777777, El principito, 15.99, 1, 1",
        "9780142424179, Nuevo título, 15.99, 1, 1",
        "9780142424179, El principito, 19.99, 1, 1",
        "9780142424179, El principito, 15.99, 2, 1",
        "9780142424179, El principito, 15.99, 1, 1;3",
        "9780142424179, El principito, 15.99, 2, 3"
    })
    void testUpdate(String newIsbn,
                    String newTitleEs,
                    double newBasePrice,
                    long newPublisherId,
                    String authorIdsCsv) {
        // Parseamos los IDs de autores separados por ";"
        List<Long> authorIds = Arrays.stream(authorIdsCsv.split(";"))
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .toList();

        // Seleccionamos un libro existente
        BookJpaEntity bookToUpdate = bookJpaDao.findById(bookEntities.getFirst().id())
                .orElseThrow(() -> new IllegalStateException("No book found to update"));

        // Construimos la lista de AuthorJpaEntity correspondientes
        List<AuthorJpaEntity> updatedAuthors = authorEntities.stream()
                .filter(a -> authorIds.contains(a.id()))
                .map(AuthorMapper.INSTANCE::authorEntityToAuthorJpaEntity)
                .toList();

        // Creamos la entidad modificada
        BookEntity updatedBook = new BookEntity(
                bookToUpdate.id(),
                newIsbn,
                newTitleEs,
                bookToUpdate.titleEn(),
                bookToUpdate.synopsisEs(),
                bookToUpdate.synopsisEn(),
                BigDecimal.valueOf(newBasePrice),
                bookToUpdate.discountPercentage(),
                bookToUpdate.cover(),
                bookToUpdate.publicationDate(),
                publisherEntities.stream()
                        .filter(p -> p.id() == newPublisherId)
                        .findFirst()
                        .orElseThrow(),
                updatedAuthors
        );

        // Ejecutamos la actualización
        BookEntity result = bookJpaDao.update(updatedBook);

        // Asserts
        assertAll(
                () -> assertEquals(updatedBook.id(), result.id()),
                () -> assertEquals(newIsbn, result.isbn()),
                () -> assertEquals(newTitleEs, result.titleEs()),
                () -> assertEquals(BigDecimal.valueOf(newBasePrice), result.basePrice()),
                () -> assertEquals(newPublisherId, result.publisher().id()),
                () -> assertEquals(updatedAuthors.size(), result.authors().size()),
                () -> assertTrue(updatedAuthors.stream()
                        .allMatch(author -> result.authors().stream()
                                .anyMatch(a -> a.id().equals(author.id()))))
        );
    }*/

