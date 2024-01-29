package ch.hearc.nde.pulsaryapi.filter;

import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String[] IGNORED_PATHS = new String[]{
            "/login",
            "/register"
    };

    private TokenService service;

    public void injectService(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        assert webApplicationContext != null;
        service = webApplicationContext.getBean(TokenService.class);
    }


    private boolean isIgnoredPath(String path){
        for (String ignoredPath : IGNORED_PATHS) {
            if (path.equals(ignoredPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if(isIgnoredPath(request.getRequestURI())){
            filterChain.doFilter(request,response);
            return;
        }
        injectService(request);

        String token = request.getHeader("Authorization");

        if(token == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Optional<UserEntity> user = service.getUserFromToken(token);
        if(user.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        request.setAttribute("user", user.get());
        filterChain.doFilter(request,response);
    }
}
