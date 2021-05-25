package com.xxxx.localism.config.security;



import com.xxxx.localism.pojo.Admin;
import com.xxxx.localism.service.IAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private IAdminService adminService;
    @Resource
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

//    @Resource
//    private CustomFilter customFilter;
//    @Resource
//    private CustomUrlDecisionManager customUrlDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //放行静态资源
        web.ignoring().antMatchers(
                "/api/v1/login",
                "/api/v1/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/api/v1/captcha",
                "/api/v1/hello",
                "/ws/**"
        );

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
//                //动态权限配置
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setAccessDecisionManager(customUrlDecisionManager);
//                        object.setSecurityMetadataSource(customFilter);
//                        return object;
//                    }
//                })
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .headers()
                .cacheControl();
        //添加jwt登录授权过滤器
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Admin admin=adminService.getAdminByUserName(username);
            if(null!=admin)
            {

                return  admin;
            }
            throw  new UsernameNotFoundException("用户名或密码不正确");

        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }


}
