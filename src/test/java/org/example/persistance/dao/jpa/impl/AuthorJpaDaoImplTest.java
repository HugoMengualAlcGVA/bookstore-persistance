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
        AuthorJpaEntity result = authorJpaDao.insert(newAuthor);
        Optional<AuthorJpaEntity> insertedAuthor = authorJpaDao.findBySlug(newAuthor.getSlug());

        assertAll(
                () -> assertNotNull(result.getId(), "Returned Author ID should not be null after insertion"),
                () -> assertNotNull(result.getId(), "Inserted Author ID should not be null after insertion")
        );

    }

}
