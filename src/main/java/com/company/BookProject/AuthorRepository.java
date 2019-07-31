package com.company.BookProject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    List<Author> findByLastName(String lastName);
    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

}
