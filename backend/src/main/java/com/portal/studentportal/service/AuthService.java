package com.portal.studentportal.service;

import com.portal.studentportal.dto.auth.LoginRequest;
import com.portal.studentportal.dto.auth.LoginResponse;
import com.portal.studentportal.dto.auth.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
}
