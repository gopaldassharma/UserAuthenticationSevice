package com.example.userauthenticationsevice.services;

import com.example.userauthenticationsevice.dtos.ValidateTokenDto;
import com.example.userauthenticationsevice.exception.InvalidCredentialsException;
import com.example.userauthenticationsevice.exception.UserAlreadyExistsException;
import com.example.userauthenticationsevice.models.Session;
import com.example.userauthenticationsevice.models.State;
import com.example.userauthenticationsevice.models.User;
import com.example.userauthenticationsevice.repos.SessionRepo;
import com.example.userauthenticationsevice.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private SecretKey secretKey;

    @Override
    public User signUp(String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = userRepo.findUserByEmail(email);
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("Email Aleady registered");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);
        return user;
    }

    @Override
    public Pair<User, MultiValueMap<String, String>> login(String email, String password) throws InvalidCredentialsException {
        Optional<User> userOptional = userRepo.findUserByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
                throw new InvalidCredentialsException("Invalid Password, please provide correct password");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("user_id__", user.getId());
            claims.put("role__", user.getRole());
            claims.put("email__", user.getEmail());
            long timeinmillis = System.currentTimeMillis();
            claims.put("iat", timeinmillis);
            claims.put("exp", timeinmillis+86400000);
            claims.put("iss", "gopaldassharma");

//            MacAlgorithm algorithm = Jwts.SIG.HS256;  --- this will come from Bean
//            SecretKey secretKey = algorithm.key().build();
            String token = Jwts.builder().claims(claims).signWith(secretKey).compact();
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, token);

            Pair<User, MultiValueMap<String, String>> pair = new Pair<>(user, headers);
            Session session = new Session();
            session.setToken(token);
            session.setUser(user);
            session.setSessionState(State.ACTIVE);
            sessionRepo.save(session);
            return pair;
        }
        return null;
    }

    @Override
    public Boolean validateToken(ValidateTokenDto validateTokenDto){
        Optional<Session> optionalSession = sessionRepo.findByTokenAndUser_Id(validateTokenDto.getToken(), validateTokenDto.getUserId());
        if(optionalSession.isEmpty()){
            return false;
        }
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(validateTokenDto.getToken()).getPayload();
        Long expiry = (Long)claims.get("exp");
        long currentTimeInMiMillis = System.currentTimeMillis();
        return currentTimeInMiMillis <= expiry;
    }

    @Override
    public User logout(String email) {
        return null;
    }
}
