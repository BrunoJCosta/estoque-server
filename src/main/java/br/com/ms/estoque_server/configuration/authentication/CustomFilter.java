package br.com.ms.estoque_server.configuration.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class CustomFilter extends OncePerRequestFilter {

    private final AssinaturaValidacao assinaturaValidacao;
    private final JwtDecoder jwtDecoder;

    CustomFilter(AssinaturaValidacao assinaturaValidacao, JwtDecoder jwtDecoder) {
        this.assinaturaValidacao = assinaturaValidacao;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        assinaturaValida(request);

        String token = request.getHeader("Authorization-security");
        if (StringUtils.isNotBlank(token)) {
            Jwt decode = jwtDecoder.decode(token);
            List<GrantedAuthority> scope = decode.getClaimAsStringList("scope").stream()
                    .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(decode.getSubject(), "", scope);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }

    private void assinaturaValida(HttpServletRequest request) {
        RuntimeException credentialsException = new RuntimeException("Invalid Credentials!");
        try {
            String assinatura = request.getHeader("X-Signature");
            boolean assinaturaValida = assinaturaValidacao.valido(assinatura);
            if (!assinaturaValida) {
                throw credentialsException;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            throw credentialsException;
        }
    }

}
