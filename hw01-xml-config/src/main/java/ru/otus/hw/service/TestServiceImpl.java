package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        for (Question question : questionDao.findAll()) {
            ioService.printFormattedLine(question.text());
            ioService.printLine("");
            ioService.printLine(printAnswers(question));
        }
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
