package com.company.BookProject;


import com.company.BookProject.Book;
import com.company.BookProject.Author;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.Before;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorDaoTests {

    @Autowired
    BookRepository bookRepo;
    @Autowired
    AuthorRepository authorRepo;

    // test objects
    Book book1;
    Book book2;
    Book book3;

    Author author1;
    Author author2;
    Author author3;

    @Before
    public void setUp() {

        bookRepo.deleteAll();
        authorRepo.deleteAll();

        book1 = new Book();
        book1.setTitle("The Gunslinger");
        book1.setPublishYear("1982");

        book2 = new Book();
        book2.setTitle("The Stand");
        book2.setPublishYear("1978");

        book3 = new Book();
        book3.setTitle("Harry Potter and the Philosopher's Stone");
        book3.setPublishYear("1997");

        author1 = new Author();
        author1.setFirstName("Stephen");
        author1.setLastName("King");

        author2 = new Author();
        author2.setFirstName("JK");
        author2.setLastName("Rowling");

        author3 = new Author();
        author3.setFirstName("Tabitha");
        author3.setLastName("King");
    }

    @Test
    @Transactional
    public void shouldAddAuthors() {
        authorRepo.save(author1);
        authorRepo.save(author2);


        assertNotNull(author1.getId());
        assertNotNull(author2.getId());
    }

    @Test
    @Transactional
    public void shouldGetAuthors() {

        authorRepo.save(author1);

        book1.setAuthor(author1);
        book1.getAuthor().setId(author1.getId());
        bookRepo.save(book1);

        book2.setAuthor(author1);

        book2.getAuthor().setId(author1.getId());
        bookRepo.save(book2);


        Set<Book> bookSet = new HashSet<>();
        bookSet.add(book1);
        bookSet.add(book2);

        author1.setBooks(bookSet);

        List<Author> fromRepo = authorRepo.findAll();

        assertEquals(author1, fromRepo.get(0));
    }

    @Test
    @Transactional
    public void shouldDeleteAuthors() {

        authorRepo.save(author1);

        authorRepo.deleteById(author1.getId());

        Optional<Author> fromRepo = authorRepo.findById(author1.getId());

        assertFalse(fromRepo.isPresent());
    }

    @Test
    @Transactional
    public void shouldGetAuthorsByLastName() {
        authorRepo.save(author1);

        authorRepo.save(author2);

        authorRepo.save(author3);

        List<Author> author1List = authorRepo.findByLastName(author1.getLastName());

        assertEquals(2, author1List.size());

        List<Author> notPresentList = authorRepo.findByLastName("Hawkins");
        assertEquals(0, notPresentList.size());
    }

    @Test
    @Transactional
    public void shouldGetAuthorsByFirstAndLastName() {
        authorRepo.save(author1);

        authorRepo.save(author2);

        authorRepo.save(author3);

        List<Author> author1List = authorRepo.findByFirstNameAndLastName(author1.getFirstName(),author1.getLastName());

        assertEquals(1, author1List.size());

        List<Author> notPresentList = authorRepo.findByFirstNameAndLastName("Stephen", "Hawkins");
        assertEquals(0, notPresentList.size());
    }

    @After
    public void tearDown() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();
    }
}
