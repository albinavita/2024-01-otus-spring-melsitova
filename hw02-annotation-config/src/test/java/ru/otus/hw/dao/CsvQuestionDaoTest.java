package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private AppProperties appProperties;

    private QuestionDao dao;

    @BeforeEach
    void setUp() {
        dao = new CsvQuestionDao(appProperties);
    }

    @DisplayName("Ошибка файл не найден")
    @Test
    void checkNotFoundFileTest() {

        assertThrows(QuestionReadException.class, () -> dao.findAll());
    }

    @DisplayName("Выполнение предоставленного файла не вызывает исключения")
    @Test
    void checkFilExists() {
        given(appProperties.getTestFileName()).willReturn("question_test.csv");

        assertDoesNotThrow(() -> dao.findAll());
        assertThat(dao.findAll().size()).isEqualTo(2);
        assertThat(dao.findAll().get(0)).isNotNull();
        assertThat(dao.findAll().get(0).answers().size()).isEqualTo(2);

    }

}