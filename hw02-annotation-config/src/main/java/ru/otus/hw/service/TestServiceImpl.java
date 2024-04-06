package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            var maxAnswers = question.answers().size();

            int numberAnswer = ioService.readIntForRangeWithPrompt(1, maxAnswers, printAnswers(question),
                    "You can enter numbers from 1 to " + maxAnswers);
            Answer answer = question.answers().get(numberAnswer - 1);
            var isAnswerValid = answer.isCorrect(); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    public String printAnswers(Question question) {
        StringBuilder builder = new StringBuilder();
        int index = 1;
        for (Answer answer : question.answers()) {
            if (answer == null) {
                return null;
            }
            builder.append(index)
                    .append(".Вариант: ")
                    .append(" ")
                    .append(answer.text())
                    .append("\n");
            index++;
        }
        return builder.toString();
    }
}
