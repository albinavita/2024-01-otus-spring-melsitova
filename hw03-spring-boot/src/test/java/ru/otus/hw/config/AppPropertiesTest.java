package ru.otus.hw.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AppProperties.class)
class AppPropertiesTest {

    @Mock
    private AppProperties appProperties;

    @Test
    void checkLocal() {
        given(appProperties.getLocale()).willReturn(Locale.forLanguageTag("en-US"));

        assertEquals(Locale.forLanguageTag("en-US"), appProperties.getLocale());
    }
}