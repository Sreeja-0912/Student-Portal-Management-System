package com.portal.studentportal.service;

import com.portal.studentportal.service.impl.MarksServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarksServiceTest {
    private final MarksServiceImpl service = new MarksServiceImpl(null, null, null, null);

    @ParameterizedTest
    @CsvSource({"95,A+", "82,A", "75,B+", "65,B", "55,C", "41,D", "35,F"})
    void calculatesGrade(BigDecimal score, String expected) {
        assertEquals(expected, service.grade(score));
    }
}
