package com.fcn.fanscoin.api.v1.auth;

import com.fcn.fanscoin.AbstractTest;
import com.fcn.fanscoin.FansCoinApp;
import com.fcn.fanscoin.blueprint.AccountBlueprint;
import com.fcn.fanscoin.blueprint.UserBlueprint;
import com.fcn.fanscoin.domain.Account;
import com.fcn.fanscoin.domain.AccountRole;
import com.fcn.fanscoin.domain.Role;
import com.fcn.fanscoin.domain.User;
import com.fcn.fanscoin.dto.v1.request.LoginReq;
import com.fcn.fanscoin.helper.TestHelper;
import com.fcn.fanscoin.repository.AccountRepository;
import com.fcn.fanscoin.repository.AccountRoleRepository;
import com.fcn.fanscoin.repository.RoleRepository;
import com.fcn.fanscoin.repository.UserRepository;
import com.tobedevoured.modelcitizen.CreateModelException;
import com.tobedevoured.modelcitizen.RegisterBlueprintException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.fcn.fanscoin.constant.Constant.ACCESS_TOKEN_COOKIE_NAME;
import static com.fcn.fanscoin.enums.RoleType.ADMIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FansCoinApp.class)
public class AuthControllerIntTest
        extends AbstractTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private String baseUrl;

    private User user;
    private Account account;
    private String rawPassword;
    private LoginReq loginReq;

    @BeforeEach
    public void setup()
            throws CreateModelException, RegisterBlueprintException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController)
                                      .setCustomArgumentResolvers(getPageableArgumentResolver())
                                      .setControllerAdvice(getExceptionTranslator())
                                      .setMessageConverters(getJacksonMessageConverter())
                                      .build();
        this.baseUrl = "/api/v1/auth";
        initData();
    }

    @Test
    @Transactional
    public void test_login_shouldBeOk()
            throws Exception {
        mockMvc.perform(post(this.baseUrl + "/login")
                                .content(TestHelper.convertObjectToJsonBytes(loginReq))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
               .andExpect(cookie().httpOnly(ACCESS_TOKEN_COOKIE_NAME, true));
    }

    @Test
    @Transactional
    public void test_logout_shouldBeOk()
            throws Exception {
        mockMvc.perform(post(this.baseUrl + "/logout"))
               .andExpect(status().isNoContent())
               .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
               .andExpect(cookie().value(ACCESS_TOKEN_COOKIE_NAME, ""))
               .andExpect(cookie().maxAge(ACCESS_TOKEN_COOKIE_NAME, 0))
               .andExpect(cookie().httpOnly(ACCESS_TOKEN_COOKIE_NAME, true));
    }

    private void initData()
            throws RegisterBlueprintException, CreateModelException {
        registerBlueprints(AccountBlueprint.class,
                           UserBlueprint.class);
        user = createFakeModel(User.class);
        userRepository.save(user);

        account = createFakeModel(Account.class);
        rawPassword = account.getPassword();
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setUserId(user.getId());
        accountRepository.save(account);

        Role role = Role.builder()
                        .name(ADMIN)
                        .build();
        roleRepository.save(role);

        AccountRole accountRole = AccountRole.builder()
                                             .accountId(account.getId())
                                             .roleId(role.getId())
                                             .build();
        accountRoleRepository.save(accountRole);

        loginReq = LoginReq.builder()
                           .email(account.getEmail())
                           .password(rawPassword)
                           .build();
    }
}
