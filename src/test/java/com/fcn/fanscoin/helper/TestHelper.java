package com.fcn.fanscoin.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public final class TestHelper {

    private static final ObjectMapper MAPPER = createObjectMapper();

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON
            .getSubtype(), UTF_8);

    private TestHelper() {
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static byte[] convertObjectToJsonBytes(final Object object)
            throws IOException {
        return MAPPER.writeValueAsBytes(object);
    }

    public static byte[] createByteArray(final int size, final String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    public static class ZonedDateTimeMatcher
            extends TypeSafeDiagnosingMatcher<String> {

        private final ZonedDateTime date;

        public ZonedDateTimeMatcher(final ZonedDateTime date) {
            this.date = date;
        }

        @Override
        protected boolean matchesSafely(final String item, final Description mismatchDescription) {
            try {
                if (!date.isEqual(ZonedDateTime.parse(item))) {
                    mismatchDescription.appendText("was ").appendValue(item);
                    return false;
                }
                return true;
            } catch (DateTimeParseException e) {
                mismatchDescription.appendText("was ").appendValue(item)
                                   .appendText(", which could not be parsed as a ZonedDateTime");
                return false;
            }
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a String representing the same Instant as ").appendValue(date);
        }
    }

    public static ZonedDateTimeMatcher sameInstant(final ZonedDateTime date) {
        return new ZonedDateTimeMatcher(date);
    }

    public static <T> void equalsVerifier(final Class<T> clazz)
            throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        Assertions.assertThat(domainObject1.toString()).isNotNull();
        Assertions.assertThat(domainObject1).isEqualTo(domainObject1);
        Assertions.assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
        // Test with an instance of another class
        Object testOtherObject = new Object();
        Assertions.assertThat(domainObject1).isNotEqualTo(testOtherObject);
        Assertions.assertThat(domainObject1).isNotEqualTo(null);
        // Test with an instance of the same class
        T domainObject2 = clazz.getConstructor().newInstance();
        Assertions.assertThat(domainObject1).isNotEqualTo(domainObject2);
        // HashCodes are equals because the objects are not persisted yet
        Assertions.assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode());
    }

    public static FormattingConversionService createFormattingConversionService() {
        DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(dfcs);
        return dfcs;
    }
}
