package ru.otus.hw.dao;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = (Objects.requireNonNull
                (classLoader.getResourceAsStream(fileNameProvider.getTestFileName())))) {
            MappingStrategy<QuestionDto> csvMappingToBean = new ColumnPositionMappingStrategy<>();
            csvMappingToBean.setType(QuestionDto.class);

            List<QuestionDto> parseList = getParseBean(inputStream, csvMappingToBean);

            List<Question> questions = new ArrayList<>();
            for (QuestionDto questionDto : parseList) {
                if (questionDto != null) {
                    questions.add(questionDto.toDomainObject());
                }
            }
            return questions;
        } catch (Exception e) {
            throw new QuestionReadException(String.format("По данному пути такого файла нет %s", e.getMessage()),
                                                                                                        e.getCause());
        }
    }

    private List<QuestionDto> getParseBean(InputStream inputStream, MappingStrategy<QuestionDto> csvMappingToBean) {
        List<QuestionDto> parseList = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(inputStream))
                .withExceptionHandler(ex -> {
                    throw new QuestionReadException(
                            String.format("Ошибка чтения csv файла %s", ex.getMessage()),
                            ex.getCause()
                    );
                }).withMappingStrategy(csvMappingToBean)
                .withSeparator(';')
                .withSkipLines(1)
                .build()
                .parse();

        return parseList;
    }

}
