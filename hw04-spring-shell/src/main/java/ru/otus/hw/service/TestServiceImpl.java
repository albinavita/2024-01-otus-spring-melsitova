package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
           ioService.printLine(question.text());
            var maxAnswers = question.answers().size();

            int numberAnswer = ioService.readIntForRangeWithPrompt(1, maxAnswers, printAnswers(question),
                    ioService.getMessage("TestService.excecute.number.format.read.error", maxAnswers));
            Answer answer = question.answers().get(numberAnswer - 1);
            var isAnswerValid = answer.isCorrect();
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
