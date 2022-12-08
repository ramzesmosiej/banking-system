package com.bankingapp.bankingapp.security.jwt;

import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.service.PropertiesCashMachineIdsConnector;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final PropertiesCashMachineIdsConnector propertiesCashMachineIdsConnector;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (request.getHeader("cash-machine") != null &&
                cashMachineIds(propertiesCashMachineIdsConnector).contains(request.getHeader("cash-machine"))) {
            var authenticationToken = new UsernamePasswordAuthenticationToken("cash-machine", null,
                    Set.of(new SimpleGrantedAuthority(Authority.USER_AUTHORITY.toString())));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.split(" ")[1].trim();
        if (!jwtUtil.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getLogin(token);

        var authorities = Arrays.stream(jwtUtil.parseClaims(token).get("auth").asString()
                .split(",")).map(SimpleGrantedAuthority::new).toList();
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private List<String> cashMachineIds(PropertiesCashMachineIdsConnector propertiesCashMachineIdsConnector) {
        return List.of(
                propertiesCashMachineIdsConnector.getDominikanski(),
                propertiesCashMachineIdsConnector.getDworzec_glowny(),
                propertiesCashMachineIdsConnector.getGrunwaldzki()
        );
    }

}
