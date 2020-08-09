package com.javatutoriales.todo.zuulserver.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@RefreshScope
@EnableResourceServer
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Value("${cors.allowed-methods}")
    private String[] corsAllowedMethods;

    @Value("${cors.allowed-credentials}")
    private Boolean corsAllowedCredentials;

    @Value("${cors.allowed-headers}")
    private String[] corsAllowedHeaders;

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        @formatter:off
        http.authorizeRequests()
                .antMatchers("/api/security/v1/oauth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/v1/users").permitAll()
                .antMatchers(HttpMethod.POST, "/api/account/v1/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/account/v1/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/account/verify/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/todolists/v1/todolist").hasRole("User")
                .antMatchers("/v1/todolist/**").hasAnyRole("Administrator", "User")
                .antMatchers("/api/todolists/**").hasAnyRole("Administrator", "User")
                .antMatchers("/api/**").hasAnyRole("Administrator")
                .anyRequest().authenticated()
                .and().cors().configurationSource(corsConfigurationSource());
//            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        @formatter:on
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        log.debug("Allowed Origins: {}", corsAllowedOrigins);
        log.debug("Allowed Methods: {}", corsAllowedMethods);
        log.debug("Allowed Headers: {}", corsAllowedHeaders);
        log.debug("Allow Credentials: {}", corsAllowedCredentials);

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins));
        corsConfiguration.setAllowedMethods(Arrays.asList(corsAllowedMethods));
        corsConfiguration.setAllowCredentials(corsAllowedCredentials);
        corsConfiguration.setAllowedHeaders(Arrays.asList(corsAllowedHeaders));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    protected FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }


    @Bean
    protected TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    protected JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("replace_with_secret_password"); //TODO: Replace with password stored on vault
        return tokenConverter;
    }
}
