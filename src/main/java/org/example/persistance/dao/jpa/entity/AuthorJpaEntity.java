package org.example.persistance.dao.jpa.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "authors")
public class AuthorJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nationality;
    @Column(name = "biography_es")
    private String biographyEs;
    @Column(name = "biography_en")
    private String biographyEn;
    private Integer bithYear;
    private Integer deathYear;
    private String slug;
    @OneToMany(mappedBy = "author")
    private List<BookAuthorJpaEntity> bookAuthors;

    public AuthorJpaEntity (Long id, String name, String nationality, String biographyEs, String biographyEn, Integer bithYear, Integer deathYear, String slug, List<BookAuthorJpaEntity> bookAuthors) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.biographyEs = biographyEs;
        this.biographyEn = biographyEn;
        this.bithYear = bithYear;
        this.deathYear = deathYear;
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBiographyEs() {
        return biographyEs;
    }

    public void setBiographyEs(String biographyEs) {
        this.biographyEs = biographyEs;
    }

    public String getBiographyEn() {
        return biographyEn;
    }

    public void setBiographyEn(String biographyEn) {
        this.biographyEn = biographyEn;
    }

    public Integer getBithYear() {
        return bithYear;
    }

    public void setBithYear(Integer bithYear) {
        this.bithYear = bithYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
