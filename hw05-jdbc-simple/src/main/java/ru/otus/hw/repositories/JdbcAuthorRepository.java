package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcAuthorRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Author> findAll() {
        return jdbcOperations.query("select id, full_name from authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var authors = jdbcOperations.query("select id, full_name from authors where id = :id",
                Map.of("id", id), new AuthorRowMapper());
        if (authors.size() == 1) {
            return Optional.of(authors.get(0));
        }
        return Optional.empty();
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            final long id = rs.getLong("id");
            final String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
