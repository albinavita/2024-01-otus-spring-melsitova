package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcGenreRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcOperations.getJdbcOperations().query("select id, name from genres",
                new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        List<Genre> genres = jdbcOperations.query(
                "select id, name from genres where id = :id",
                Map.of("id", id), new GenreRowMapper());
        if (genres.size() == 1) {
            return Optional.of(genres.get(0));
        }
        return Optional.empty();
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return null;
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            final long id = rs.getLong("id");
            final String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
