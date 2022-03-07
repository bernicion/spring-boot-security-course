package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RestHeaderAuthFilter extends AbstractRestAuthFilter {

    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Api-Secret");
    }
    @Override
    public String getUsername(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Api-Key");
    }


}