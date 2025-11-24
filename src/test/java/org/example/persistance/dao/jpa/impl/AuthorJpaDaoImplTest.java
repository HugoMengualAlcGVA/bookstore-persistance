package org.example.persistance.dao.jpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.persistance.TestConfig;
import org.example.persistance.dao.jpa.AuthorJpaDao;
import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorJpaDaoImplTest {

    //private final AuthorJpaDao authorJpaDao = new AuthorJpaDaoImpl();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuthorJpaDao authorJpaDao;

    @Test
    @DisplayName("Insert a new author and verify it is saved correctly")
    void testInsertAuthor() {
        AuthorJpaEntity newAuthor = new AuthorJpaEntity(
                null,
                "Test Author",
                "test-nationality",
                "test-biography-es",
                "test-biography-en",
                1970,
                null,
                "test-author"
        );

        long countBefore = authorJpaDao.count();

        AuthorJpaEntity result = authorJpaDao.insert(newAuthor);
        Optional<AuthorJpaEntity> insertedAuthor = authorJpaDao.findBySlug(newAuthor.getSlug());

        long countAfter = authorJpaDao.count();

        assertAll(
                () -> assertNotNull(result.getId(), "Returned Author ID should not be null after insertion"),
                () -> assertEquals(countBefore + 1, countAfter)

        );
        insertedAuthor.ifPresent(authorJpaEntity -> assertAll(
                () -> assertNotNull(authorJpaEntity.getId(), "Inserted Author ID should not be null after insertion"),
                () -> assertEquals(newAuthor.getId(), insertedAuthor.get().getId()),
                () -> assertEquals(newAuthor.getName(), insertedAuthor.get().getName()),
                () -> assertEquals(newAuthor.getSlug(), insertedAuthor.get().getSlug()),
                () -> assertEquals(newAuthor.getBookAuthors(), insertedAuthor.get().getBookAuthors()),
                () -> assertEquals(newAuthor.getBiographyEn(), insertedAuthor.get().getBiographyEn()),
                () -> assertEquals(newAuthor.getBiographyEs(), insertedAuthor.get().getBiographyEs()),
                () -> assertEquals(newAuthor.getBirthYear(), insertedAuthor.get().getBirthYear()),
                () -> assertEquals(newAuthor.getDeathYear(), insertedAuthor.get().getDeathYear()),
                () -> assertEquals(newAuthor.getNationality(), insertedAuthor.get().getNationality())
        ));

    }

    @Test
    @DisplayName("Test count() does in fact count properly")
    void countTest(){
        String sql = "SELECT COUNT(b) FROM AuthorJpaEntity b";
        long expectedCount = entityManager.createQuery(sql, Long.class)
                .getSingleResult();
        long actualCount = authorJpaDao.count();

        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("Test findBySlug returns whats expected")
    void findBySlugTest(){
        String sql = "SELECT TOP 1 a FROM AuthorJpaEntity a";
        AuthorJpaEntity existingAuthor = entityManager.find(AuthorJpaEntity.class, sql);

        Optional<AuthorJpaEntity> response = authorJpaDao.findBySlug(existingAuthor.getSlug());

        assertNotNull(response, "Returned Author entity should not be null");
        response.ifPresent(authorFound -> assertAll(
                () -> assertEquals(existingAuthor.getId(), authorFound.getId()),
                () -> assertEquals(existingAuthor.getName(), authorFound.getName()),
                () -> assertEquals(existingAuthor.getSlug(), authorFound.getSlug()),
                () -> assertEquals(existingAuthor.getNationality(), authorFound.getNationality()),
                () -> assertEquals(existingAuthor.getBiographyEs(), authorFound.getBiographyEs()),
                () -> assertEquals(existingAuthor.getBiographyEn(), authorFound.getBiographyEn()),
                () -> assertEquals(existingAuthor.getBirthYear(), authorFound.getBirthYear()),
                () -> assertEquals(existingAuthor.getDeathYear(), authorFound.getDeathYear())
        ));
    }

}
