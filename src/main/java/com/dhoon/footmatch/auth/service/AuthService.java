package com.dhoon.footmatch.auth.service;

import com.dhoon.footmatch.auth.dto.request.MemberLoginRequest;
import com.dhoon.footmatch.auth.dto.response.MemberLoginResponse;

public interface AuthService {

    MemberLoginResponse login(MemberLoginRequest request);
}
