package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private final TestServiceImpl testService = mock(TestServiceImpl.class);

    @DisplayName("Тестирование вариантов вопросов и ответов")
    @Test
    void testReadQuestionAndAnswer() {
        testService.executeTest();
        when(testService.printAnswers(any())).thenReturn(String.valueOf(prepareQuestions()));
        verify(testService, times(1)).executeTest();
        verify(testService, times(0)).printAnswers(any());

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
                new Answer("Answer 1", true),
                new Answer("Answer 2", false),
                new Answer("Answer 2", false)
        );
    }


}