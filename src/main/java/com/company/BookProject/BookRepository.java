package com.company.BookProject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByTitle(String title);
    List<Book> findByAuthorLastName(String lastName);
    List<Book> findByAuthorFirstNameAndAuthorLastName(String firstName, String lastName);
    List<Book> findByAuthorLastNameAndYearRange(String lastName, String startYear, String endYear);

}
