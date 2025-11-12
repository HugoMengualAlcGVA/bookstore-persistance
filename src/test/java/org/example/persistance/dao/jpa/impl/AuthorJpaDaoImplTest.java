package org.example.persistance.dao.jpa.impl;

import org.example.persistance.dao.jpa.AuthorJpaDao;
import org.example.persistance.dao.jpa.entity.AuthorJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorJpaDaoImplTest {

    private final AuthorJpaDao authorJpaDao = new AuthorJpaDaoImpl();

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
    @DisplayName("")
    void findBySlugTest(){
        
    }

}
