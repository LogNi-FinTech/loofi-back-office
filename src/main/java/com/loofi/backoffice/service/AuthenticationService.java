package com.loofi.backoffice.service;

import com.loofi.backoffice.constants.MfsProductErrors;
import com.loofi.backoffice.entity.Role;
import com.loofi.backoffice.entity.User;
import com.loofi.backoffice.exceptions.CommonException;
import com.loofi.backoffice.jwt.JwtUtils;
import com.loofi.backoffice.repository.RoleRepository;
import com.loofi.backoffice.repository.UserRepositoty;
import com.loofi.backoffice.request.LoginRequest;
import com.loofi.backoffice.request.ResponseToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class AuthenticationService {

    @Autowired
    private UserRepositoty userRepositoty;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public ResponseToken login(LoginRequest loginRequest) {

        User userInfo = userRepositoty.findByUsername(loginRequest.getUsername());
        ResponseToken responseToken = new ResponseToken();
        if (userInfo != null) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateToken(loginRequest.getUsername());
            responseToken.setAccessToken(jwtToken);
            responseToken.setUserId(userInfo.getId());
            responseToken.setRole(userInfo.getRoles().get(0).getName());
            return responseToken;
        }
        throw new CommonException(
                MfsProductErrors.getErrorCode(MfsProductErrors.MFSPRODUCT_MANAGEMENT, MfsProductErrors.ACCOUNT_NOT_FOUND),
                MfsProductErrors.ERROR_MAP.get(MfsProductErrors.ACCOUNT_NOT_FOUND));
    }

    public int userSignUp(User user) {
        if (userRepositoty.existsByUsername(user.getUsername())) {
            throw new CommonException(
                    MfsProductErrors.getErrorCode(MfsProductErrors.MFSPRODUCT_MANAGEMENT, MfsProductErrors.USERNAME_ALREADY_EXIST),
                    MfsProductErrors.ERROR_MAP.get(MfsProductErrors.USERNAME_ALREADY_EXIST));
        }
        if(isValid(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepositoty.save(user);
            if (user.getRoles() != null)
                for (Role role : user.getRoles()) {
                    role.setUser(savedUser);
                    roleRepository.save(role);
                }
            return 1;
        }
        throw new CommonException(
                MfsProductErrors.getErrorCode(MfsProductErrors.MFSPRODUCT_MANAGEMENT, MfsProductErrors.PASSWORD_VALIDATION_FAILED),
                MfsProductErrors.ERROR_MAP.get(MfsProductErrors.PASSWORD_VALIDATION_FAILED));
    }

    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
