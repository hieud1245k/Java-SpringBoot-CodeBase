package com.fcn.fanscoin;

import com.fcn.fanscoin.config.ApplicationProperties;
import com.fcn.fanscoin.exception.ExceptionTranslator;
import com.fcn.fanscoin.repository.AccountRoleRepository;
import com.fcn.fanscoin.security.DomainUserDetails;
import com.github.javafaker.Faker;
import com.tobedevoured.modelcitizen.CreateModelException;
import com.tobedevoured.modelcitizen.ModelFactory;
import com.tobedevoured.modelcitizen.RegisterBlueprintException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.assertj.core.api.AbstractComparableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Component
@Configuration
public class AbstractTest {

    public static final long CURRENT_USER_ID = 1L;
    public static final String CURRENT_USER_LOGIN = "current_user@email.com";
    public static final String CURRENT_USER_NO = "currentUserNo";
    public static final String CURRENT_USER_NICKNAME = "nickname";
    public static final String PASSWORD = "password";
    private static final String DEFAULT_TIME_ZONE = "Asia/Tokyo";

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Mock
    private ApplicationProperties mockApplicationProperties;

    @Mock
    private HttpServletResponse mockHttpServletResponse;

    private ModelFactory modelFactory;
    private Faker faker;
    private ZoneId zoneId;

    @BeforeEach
    public void before()
            throws IOException {
        this.modelFactory = new ModelFactory();
        this.faker = new Faker();

        initMockSecurityContext(null);

        Mockito.when(mockApplicationProperties.getTimezone()).thenReturn(DEFAULT_TIME_ZONE);

        zoneId = ZoneId.of(mockApplicationProperties.getTimezone());

        Mockito.doReturn(Mockito.mock(ServletOutputStream.class)).when(mockHttpServletResponse).getOutputStream();
        mockHttpServletResponse.getOutputStream();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @SuppressWarnings("rawtypes")
    public void registerBlueprints(final Class... classes)
            throws RegisterBlueprintException {
        modelFactory.setRegisterBlueprints(Arrays.asList(classes));
    }

    public <T> T createFakeModel(final Class<T> clazz)
            throws CreateModelException {
        return modelFactory.createModel(clazz, true);
    }

    public <T> T createFakeModel(final Class<T> clazz, final boolean forceFakeId)
            throws CreateModelException, IllegalAccessException {
        T fakeModel = createFakeModel(clazz);

        if (forceFakeId) {
            Field field = FieldUtils.getField(clazz, "id", true);
            if (field != null) {
                Long id = faker.number().randomNumber();
                FieldUtils.writeField(field, fakeModel, id);
            }
        }

        return fakeModel;
    }

    public JsonPathResultMatchers jsonPath(final String expression) {
        return MockMvcResultMatchers.jsonPath(expression);
    }

    public ResultMatcher matchJsonPath(final String expression, final Object expectedValue) {
        return jsonPath(expression).value(expectedValue);
    }

    public void initMockSecurityContext(final Long userId) {
        initMockSecurityContext(userId, CURRENT_USER_LOGIN, CURRENT_USER_NICKNAME);
    }

    public void initMockSecurityContext(final Long userId, final String currentUserLogin) {
        initMockSecurityContext(userId, currentUserLogin, CURRENT_USER_NICKNAME);
    }

    public void initMockSecurityContext(final Long userId,
                                        final String currentUserLogin,
                                        final String currentUserNickname) {
        DomainUserDetails domainUserDetails =
                DomainUserDetails.builder()
                                 .userId(userId)
                                 .username(currentUserLogin)
                                 .nickname(currentUserNickname)
                                 .userNo(CURRENT_USER_NO)
                                 .password(PASSWORD)
                                 .authorities(Collections.singletonList(new SimpleGrantedAuthority("ADMIN")))
                                 .id(userId)
                                 .build();

        Collection<? extends GrantedAuthority> authorities = Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .orElseGet(Collections::emptyList);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(domainUserDetails,
                                                                                                     null,
                                                                                                     authorities);
        authentication.setDetails(domainUserDetails);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    public <T> T randomValueFromList(final List<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }

        int index = faker.number().numberBetween(0, values.size() - 1);
        return values.get(index);
    }

    public String toStringWithDecimalFormat(final Number value, final String pattern) {
        if (value == null) {
            return "";
        }

        return new DecimalFormat(pattern).format(value);
    }

    public static class BigDecimalAssert
            extends AbstractComparableAssert<BigDecimalAssert, BigDecimal> {

        public BigDecimalAssert(final BigDecimal actual) {
            super(actual, BigDecimalAssert.class);
        }

        public BigDecimalAssert isEqualByComparingTo(final BigDecimal expected) {
            if (actual == null && expected == null) {
                return this;
            }

            return super.isEqualByComparingTo(expected);
        }

        public static BigDecimalAssert assertThat(final BigDecimal actual) {
            return new BigDecimalAssert(actual);
        }
    }
}
