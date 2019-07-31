package com.company.BookProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookStoreControllerTests {

    private MockMvc mockMvc;

    @Mock
    BookStoreServiceImplementation mockBookStoreServiceImpl;

    @InjectMocks
    BookStoreController bookStoreController;

    Book book1;
    Book book2;
    Book book3;


    Author author1;
    Author author2;

    List<Book> bookList;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookStoreController).build();

        author1 = new Author();
        author1.setFirstName("Stephen");
        author1.setLastName("King");

        author2 = new Author();
        author2.setFirstName("JK");
        author2.setLastName("Rowling");

        book1 = new Book();
        book1.setTitle("The Gunslinger");
        book1.setPublishYear("1982");
        book1.setAuthor(author1);

        book2 = new Book();
        book2.setTitle("The Stand");
        book2.setPublishYear("1978");
        book2.setAuthor(author1);


        book3 = new Book();
        book3.setTitle("Harry Potter and the Philosopher's Stone");
        book3.setPublishYear("1997");
        book3.setAuthor(author2);

        bookList = Arrays.asList(book1, book2, book3);
    }

    @Test
    public void rootContext_ShouldRespondWith404() throws Exception {


        mockMvc.perform(get("/"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void ShouldReturnAllBooks() throws Exception {
        when(mockBookStoreServiceImpl.getAllBooks()).thenReturn(bookList);


        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", is(bookList.get(0).getTitle())));

        verify(mockBookStoreServiceImpl).getAllBooks();

    }


    @Test
    public void ShouldReturnBookByTitle() throws Exception {
        bookList = Arrays.asList(book2);
        when(mockBookStoreServiceImpl.getBooksByTitle(bookList.get(0).getTitle())).thenReturn(bookList);


        mockMvc.perform(get("/books/title/" + bookList.get(0).getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(bookList.get(0).getTitle())));

        verify(mockBookStoreServiceImpl).getBooksByTitle(bookList.get(0).getTitle());

    }

    @Test
    public void ShouldReturnBookByAuthorLastName() throws Exception {
        bookList = Arrays.asList(book1, book2);
        when(mockBookStoreServiceImpl.getBooksByAuthorLastName(bookList.get(0).getAuthor().getLastName())).thenReturn(bookList);


        mockMvc.perform(get("/books/author/" + bookList.get(0).getAuthor().getLastName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(bookList.get(0).getTitle())));

        verify(mockBookStoreServiceImpl).getBooksByAuthorLastName(bookList.get(0).getAuthor().getLastName());

    }

    @Test
    public void ShouldReturnBookByAuthorFullName() throws Exception {
        bookList = Arrays.asList(book1, book2);
        when(mockBookStoreServiceImpl
                .getBooksByAuthorFullName(bookList.get(0).getAuthor().getLastName(), bookList.get(0).getAuthor().getFirstName()))
                .thenReturn(bookList);


        mockMvc.perform(get("/books/author/" + bookList.get(0).getAuthor().getLastName() + "/" + bookList.get(0).getAuthor().getFirstName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].title", is(bookList.get(1).getTitle())));

        verify(mockBookStoreServiceImpl).getBooksByAuthorFullName(bookList.get(0).getAuthor().getLastName(), bookList.get(0).getAuthor().getFirstName());

    }


    @Test
    public void ShouldReturnBookByAuthorAndPublishYear() throws Exception {
        bookList = Arrays.asList(book2);
        String author = bookList.get(0).getAuthor().getLastName();
        String startYear = bookList.get(0).getPublishYear();
        String endYear = Integer.toString(Integer.parseInt(startYear) + 2);

        when(mockBookStoreServiceImpl
                .getBooksByAuthorLastNameAndYearRange(author, startYear, endYear))
                .thenReturn(bookList);


        mockMvc.perform(get("/books/author/" + author + "/" + startYear + "/" + endYear))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(bookList.get(0).getTitle())));

        verify(mockBookStoreServiceImpl).getBooksByAuthorLastNameAndYearRange(author, startYear, endYear);

    }

    @Test(expected = NestedServletException.class)
    public void ShouldErrorWhenYearIsNotFourDigit() throws Exception {

        String author = bookList.get(0).getAuthor().getLastName();
        String startYear = bookList.get(0).getPublishYear();
        String endYear = "2";

        mockMvc.perform(get("/books/author/" + author + "/" + startYear + "/" + endYear));

    }
}
