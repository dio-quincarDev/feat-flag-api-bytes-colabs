package com.bytes_colaborativos.api.auth.service;

import com.bytes_colaborativos.api.auth.commons.dto.request.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.request.UserEntityRequest;

public interface AuthService {
    TokenResponse createUser (UserEntityRequest userEntityRequest);
    TokenResponse createSuperAdmin (UserEntityRequest userEntityRequest);
    TokenResponse login (LoginRequest loginRequest);
}
