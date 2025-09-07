package com.bytes_colaborativos.api.service;

import com.bytes_colaborativos.api.commons.dto.LoginRequest;
import com.bytes_colaborativos.api.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.commons.dto.UserEntityRequest;

public interface AuthService {
    TokenResponse createUser (UserEntityRequest userEntityRequest);
    TokenResponse login (LoginRequest loginRequest);
}
