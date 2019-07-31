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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookDaoTests {

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
    }

    @Test
    @Transactional
    public void shouldAddBooks() {
        authorRepo.save(author1);

        bookRepo.save(book1);

        bookRepo.save(book2);

        assertNotNull(book1.getId());
        assertNotNull(book2.getId());
    }

    @Test
    @Transactional
    public void shouldGetBooks() {

        authorRepo.save(author1);

        book1.setAuthor(author1);
        bookRepo.save(book1);

        book2.setAuthor(author1);
        bookRepo.save(book2);

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        List<Book> fromRepo = bookRepo.findAll();

        assertEquals(bookList, fromRepo);
    }

    @Test
    @Transactional
    public void shouldDeleteBooks() {

        authorRepo.save(author1);

        book1.setAuthor(author1);
        bookRepo.save(book1);

        bookRepo.deleteById(book1.getId());

        Optional<Book> fromRepo = bookRepo.findById(book1.getId());

        assertFalse(fromRepo.isPresent());
    }

    @Test
    @Transactional
    public void shouldGetBooksByTitle() {

        authorRepo.save(author1);

        book1.setAuthor(author1);
        bookRepo.save(book1);

        book2.setAuthor(author1);
        bookRepo.save(book2);

        authorRepo.save(author2);

        book3.setAuthor(author2);
        bookRepo.save(book3);

        List<Book> titleList = bookRepo.findByTitle(book3.getTitle());

        assertEquals(book3, titleList.get(0));
    }

    @Test
    @Transactional
    public void shouldGetBooksByAuthorLastName() {

        authorRepo.save(author1);

        book1.setAuthor(author1);
        bookRepo.save(book1);

        book2.setAuthor(author1);
        bookRepo.save(book2);

        authorRepo.save(author2);

        book3.setAuthor(author2);
        bookRepo.save(book3);

        List<Book> fromRepo = bookRepo.findByAuthorLastName(book2.getAuthor().getLastName());

        assertEquals(2, fromRepo.size());

        fromRepo = bookRepo.findByAuthorLastName(book3.getAuthor().getLastName());

        assertEquals(1, fromRepo.size());
    }

    @Test
    @Transactional
    public void shouldGetBooksByAuthorFullName() {

        authorRepo.save(author1);

        book1.setAuthor(author1);
        bookRepo.save(book1);

        book2.setAuthor(author1);
        bookRepo.save(book2);

        authorRepo.save(author2);

        book3.setAuthor(author2);
        bookRepo.save(book3);

        List<Book> fromRepo = bookRepo.findByAuthorFirstNameAndAuthorLastName(book2.getAuthor().getFirstName(), book2.getAuthor().getLastName());

        assertEquals(2, fromRepo.size());

        fromRepo = bookRepo.findByAuthorFirstNameAndAuthorLastName(book3.getAuthor().getFirstName(), book3.getAuthor().getLastName());

        assertEquals(1, fromRepo.size());
    }

    @After
    public void tearDown() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();
    }
}
