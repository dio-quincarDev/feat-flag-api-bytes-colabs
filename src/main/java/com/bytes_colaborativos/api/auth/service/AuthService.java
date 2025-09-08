package com.bytes_colaborativos.api.auth.service;

import com.bytes_colaborativos.api.auth.commons.dto.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.UserEntityRequest;

public interface AuthService {
    TokenResponse createUser (UserEntityRequest userEntityRequest);
    TokenResponse login (LoginRequest loginRequest);
}
