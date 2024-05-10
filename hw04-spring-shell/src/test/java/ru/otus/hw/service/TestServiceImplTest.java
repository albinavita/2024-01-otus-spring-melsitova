package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@SpringBootTest
class TestServiceImplTest {

    @Mock
    private QuestionDao dao;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, dao);
    }

    @DisplayName("Тестирование вариантов вопросов и ответов")
    @Test
    void testReadQuestionAndAnswer() {
        Student student = new Student("Name", "Surname");
        TestResult testResult = testService.executeTestFor(student);

        assertThatList(prepareQuestions()).contains(new Question("Question 1", prepareAnswers()));
        assertThatList(prepareAnswers()).contains(new Answer("Answer 1", false));

        assertThat(student.firstName()).isEqualTo("Name");
        assertThat(student.lastName()).isEqualTo("Surname");
        assertThat(testResult.getStudent()).isNotNull().isEqualTo(student);

        assertThat(testResult).isNotNull();


    }

    private List<Question> prepareQuestions() {
        return List.of(
                new Question("Question 1", prepareAnswers()),
                new Question("Question 2", prepareAnswers()),
                new Question("Question 3", prepareAnswers())
        );
    }

    private List<Answer> prepareAnswers() {
        return List.of(
                new Answer("Answer 1", false),
                new Answer("Answer 2", true),
                new Answer("Answer 2", false)
        );
    }

}