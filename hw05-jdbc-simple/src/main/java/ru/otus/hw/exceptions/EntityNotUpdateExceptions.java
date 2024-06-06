package ru.otus.hw.exceptions;

import ru.otus.hw.models.Book;

public class EntityNotUpdateExceptions extends RuntimeException {
    public EntityNotUpdateExceptions(Book book) {
        super(String.format("Запись книги %s не обновлена!", book == null ? "" : book.getTitle()));
    }
}
