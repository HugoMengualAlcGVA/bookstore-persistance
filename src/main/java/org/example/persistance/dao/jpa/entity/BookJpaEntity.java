package org.example.persistance.dao.jpa.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="books")
public class BookJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;
    @Column(name = "title_es")
    private String titleEs;
    @Column(name = "title_en")
    private String titleEn;
    @Column(name = "synopsis_es", length = 2000)
    private String synopsisEs;
    @Column(name = "synopsis_en", length = 2000)
    private String synopsisEn;
    @Column(name = "base_price")
    private BigDecimal basePrice;
    @Column(name = "discount_percentage")
    private Double discountPercentage;
    private String cover;
    @Column(name = "publication_date")
    private String publicationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private PublisherJpaEntity publisher;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookAuthorJpaEntity> bookAuthors = new ArrayList<>();

    public BookJpaEntity(Long id, String isbn, String titleEs, String titleEn, String synopsisEs, String synopsisEn, BigDecimal basePrice, Double discountPercentage, String cover, String publicationDate, PublisherJpaEntity publisher, List<AuthorJpaEntity> authors) {
        this.id = id;
        this.isbn = isbn;
        this.titleEs = titleEs;
        this.titleEn = titleEn;
        this.synopsisEs = synopsisEs;
        this.synopsisEn = synopsisEn;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.cover = cover;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        setAuthors(authors);
    }

    public void setAuthors(List<AuthorJpaEntity> authors) {
        //this.bookAuthors = authors;
        this.bookAuthors.clear();
        for (AuthorJpaEntity author : authors) {
            BookAuthorJpaEntity bookAuthor = new BookAuthorJpaEntity(this, author);
            this.bookAuthors.add(bookAuthor);
        }
    }
}
