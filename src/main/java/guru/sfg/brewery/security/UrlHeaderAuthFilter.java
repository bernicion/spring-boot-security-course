package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UrlHeaderAuthFilter extends AbstractRestAuthFilter {

    public UrlHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }


    @Override
    public String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("apiSecret");
    }

    @Override
    public String getUsername(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("apiKey");
    }

}