package com.fcn.fanscoin.api.v1.auth;

import com.fcn.fanscoin.bloc.v1.AuthBloc;
import com.fcn.fanscoin.dto.v1.request.LoginReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthBloc authBloc;

    public AuthController(final AuthBloc authBloc) {
        this.authBloc = authBloc;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final LoginReq loginReq,
                                   final HttpServletResponse response) {

        Cookie cookie = authBloc.login(loginReq);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request,
                                    final HttpServletResponse response) {
        authBloc.logout(request, response);

        return ResponseEntity.noContent().build();
    }
}
