package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.IUserRepository;

@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultAuthenticationProvider(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getPrincipal().toString();
        String dbPassword = userRepository.selectPassword(login).orElseThrow(() -> new BadCredentialsException("Invalid user " + login));
        String requestPassword = (String) authentication.getCredentials();
        if (requestPassword == null || !passwordEncoder.matches(requestPassword, dbPassword)) {
            throw new BadCredentialsException("Invalid password for user " + login);
        }

        Authentication token = new UsernamePasswordAuthenticationToken(login, dbPassword);
        SecurityContextHolder.setContext(new SecurityContextImpl(token));

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
