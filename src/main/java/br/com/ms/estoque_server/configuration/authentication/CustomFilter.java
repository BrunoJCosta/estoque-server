package br.com.ms.estoque_server.configuration.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class CustomFilter extends OncePerRequestFilter {

    private final static String USER = "bruno_estoque";
    private final static String PASSWORD = "bruno_estoque_123";
    public static final UsernameNotFoundException USERNAME_OR_PASSWORD_INVALID = new UsernameNotFoundException("username or password invalid");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String basic = "Basic ";
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith(basic)) {
            String auth = authorization.replaceFirst(basic, "");
            byte[] decode = new Base64().decode(auth);
            UsernamePasswordAuthenticationToken userAuth = getUsernamePasswordAuthenticationToken(decode);
            SecurityContextHolder.getContext().setAuthentication(userAuth);
        }
        filterChain.doFilter(request,response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(byte[] decode) {
        String usuarioSenha = new String(decode);
        String[] split = usuarioSenha.strip().split(":");
        if (split.length != 2)
            throw USERNAME_OR_PASSWORD_INVALID;
        String usuario = split[0];
        String senha = split[1];
        if (!Objects.equals(usuario, USER) || !Objects.equals(senha, PASSWORD))
            throw USERNAME_OR_PASSWORD_INVALID;

        return new UsernamePasswordAuthenticationToken(usuario, null, null);
    }
}
