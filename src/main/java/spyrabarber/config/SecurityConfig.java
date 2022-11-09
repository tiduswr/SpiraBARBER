package spyrabarber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager configureAuth(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
            .antMatchers("/css/**", "/js/**","/images/**").permitAll()
            .antMatchers("/", "/home").permitAll()
            .antMatchers("/users/**").hasAuthority("ADMIN")
            //.antMatchers("/api/barbers").hasAuthority("BARBEIRO")
            //Mapeando Login
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login-error")
                .permitAll()
            //Mapeando logout
            .and()
                .logout()
                .logoutSuccessUrl("/")
            //Mapeando erros
            .and()
                .exceptionHandling();
        return http.build();
    }

}
