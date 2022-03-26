package mobile.security.config;

import mobile.security.JWT.AuthEntryPointJwt;
import mobile.security.Service.AppUserDetailService;
import mobile.security.filter.AuthTokenFilter;
import mobile.common.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final PasswordEncoder passwordEncoder = new PasswordConfig().passwordEncoder();

    @Autowired
    AppUserDetailService userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/admin/**").hasAnyAuthority(UserPermission.ADMIN_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/admin/**").hasAnyAuthority(UserPermission.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.GET,"/api/user/**").hasAnyAuthority(UserPermission.USER_READ.getPermission(), UserPermission.ADMIN_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/user/**").hasAnyAuthority(UserPermission.USER_WRITE.getPermission(), UserPermission.ADMIN_WRITE.getPermission())
                .anyRequest().permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }



}
