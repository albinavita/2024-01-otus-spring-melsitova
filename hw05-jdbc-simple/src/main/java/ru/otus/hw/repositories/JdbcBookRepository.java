package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotUpdateExceptions;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        List<Book> books = jdbcOperations.query("""
                select b.id, b.title, b.author_id, b.genre_id,
                        g.id, g.name,
                        a.id, a.full_name
                 from books b 
                 inner join genres g 
                     on b.genre_id = g.id
                 inner join authors a
                     on b.author_id = a.id
                 where b.id = :id
                """, Map.of("id", id), new BookRowMapper());

        if (books.size() == 1) {
            return Optional.of(books.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        return jdbcOperations.getJdbcOperations().query(
                """
                        select b.id, b.title, b.author_id, b.genre_id,
                                g.id, g.name,
                                a.id, a.full_name
                         from books b 
                         inner join genres g 
                             on b.genre_id = g.id
                         inner join authors a
                             on b.author_id = a.id
                        """, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update(
                "delete from books where id = :id",
                Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("author_id", book.getAuthor().getId());
        parameterSource.addValue("genre_id", book.getGenre().getId());

        jdbcOperations.update("""
                insert into books (title, author_id, genre_id)
                values (:title, :author_id, :genre_id)
                """, parameterSource, keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int res = jdbcOperations.update(
                """
                        update books 
                        set title = :title, author_id = :author_id, genre_id = :genre_id
                        where id = :id
                        """, Map.of("id", book.getId(),
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId()));
        if (res <= 0) {
            throw new EntityNotUpdateExceptions(book);
        }
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("authors.id"));
            author.setFullName(rs.getString("authors.full_name"));

            Genre genre = new Genre();
            genre.setId(rs.getLong("genres.id"));
            genre.setName(rs.getString("genres.name"));

            Book book = new Book();
            book.setId(rs.getLong("books.id"));
            book.setTitle(rs.getString("books.title"));
            book.setAuthor(author);
            book.setGenre(genre);
            return book;
        }
    }
}
