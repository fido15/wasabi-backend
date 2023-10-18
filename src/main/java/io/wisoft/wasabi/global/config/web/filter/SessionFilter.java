package io.wisoft.wasabi.global.config.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String token = this.extractToken(request);

        final boolean isAuthenticated = StringUtils.hasText(token);
        request.setAttribute("isAuthenticated", isAuthenticated);

        if (!isAuthenticated) {
            request.getSession();

        }

        doFilter(request, response, filterChain);
    }

    private String extractToken(final HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization)) {
            return authorization.substring(7).trim();
        }

        return Strings.EMPTY;
    }

}
