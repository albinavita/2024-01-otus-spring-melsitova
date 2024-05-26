package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

        for (var question : questions) {
            ioService.printLine(question.text());
            var maxAnswers = question.answers().size();

            printAnswers(question.answers());
            ioService.printLine("");

            int numberAnswer = ioService.readIntForRangeWithPrompt(1, maxAnswers,
                    ioService.getMessage("TestService.write.number.prompt"),
                    ioService.getMessage("TestService.excecute.number.format.read.error", maxAnswers));
            Answer answer = question.answers().get(numberAnswer - 1);
            var isAnswerValid = answer.isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printAnswers(List<Answer> answers) {
        AtomicInteger questionNumber = new AtomicInteger(1);
        answers.forEach(answer ->
                ioService.printFormattedLine("%s %s", questionNumber.getAndIncrement(), answer.text()));
    }

}
