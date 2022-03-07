package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JPAUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.UrlHeaderAuthFilter;
import guru.sfg.brewery.security.google.Google2FaFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JPAUserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final Google2FaFilter google2FaFilter;

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public UrlHeaderAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        UrlHeaderAuthFilter filter = new UrlHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        httpSecurity
//                .addFilterBefore(restUrlAuthFilter(authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class)
//                .csrf().disable();
        http
                .addFilterBefore(google2FaFilter, SessionManagementFilter.class);

        http.cors().and()
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() //do not use in production!
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(loginConfigurer -> {
                    loginConfigurer.loginProcessingUrl("/login")
                            .loginPage("/").permitAll()
                            .successForwardUrl("/")
                            .defaultSuccessUrl("/")
                            .failureForwardUrl("/?error");
                })
                .logout(logoutConfigurer -> {
                    logoutConfigurer
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/")
                            .logoutSuccessUrl("/?logout")
                            .permitAll();
                })
                .httpBasic()
                .and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
                .and().rememberMe()
                    .tokenRepository(persistentTokenRepository)
                    .userDetailsService(userDetailsService);

        //.rememberMe()
        //.key("sfg-key")
        //.userDetailsService(userDetailsService);

        //h2 console config
        http.headers().frameOptions().sameOrigin();
    }



//
//    @Autowired
//    JPAUserDetailsService jpaUserDetailsService;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(jpaUserDetailsService).passwordEncoder(passwordEncoder()); //makes sense when many UserDetailServices are defined
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin")
//                .password("{bcrypt}$2a$12$lLMMf3As1NkCgR5CbL2pPOqMLisGpOX0UFaIDOSfEEmSQbp14xRzC")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}73f9d552cca8cec567458cf4dc9bf34058e6a0b0daae00ef116e17199e11c6e3416ba9d7cbe828dc")
//                .roles("USER");
//    }

//    @Override
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }


}
