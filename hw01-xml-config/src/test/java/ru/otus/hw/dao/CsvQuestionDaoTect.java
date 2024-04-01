package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvQuestionDaoTect {

    @DisplayName("Ошибка файл не найден")
    @Test
    void checkNotFoundFileTest() {
        QuestionDao dao = new CsvQuestionDao(
                new AppProperties("file_name.csv"));
        assertThrows(QuestionReadException.class, dao::findAll);
    }

    @DisplayName("Выполнение предоставленного файла не вызывает исключения")
    @Test
    void checkFilExists() {
        QuestionDao dao = new CsvQuestionDao(new AppProperties("questions.csv"));
        assertDoesNotThrow(() -> dao.findAll());
    }

}
