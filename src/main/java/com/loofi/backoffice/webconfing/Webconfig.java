package com.loofi.backoffice.webconfing;

import com.loofi.backoffice.jwt.AuthEntryPointJwt;
import com.loofi.backoffice.jwt.TokenFilter;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@EnableWebMvc
public class Webconfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/login", "/signup")
				.permitAll()
				.antMatchers(HttpMethod.POST,"/api/ledger-transaction").hasAnyRole("MAKER")
				.antMatchers(HttpMethod.POST,"/api/registration").hasAnyRole("MAKER")
				.antMatchers(HttpMethod.GET, "/api/ledger-transaction").hasAnyRole("CHECKER", "MAKER")
				.antMatchers(HttpMethod.GET, "/api/registration").hasAnyRole("CHECKER", "MAKER")
				//.antMatchers(HttpMethod.GET, "/api/mfs-ledger-transaction/maker").hasAnyRole("")
				.antMatchers(HttpMethod.POST, "/api/ledger-transaction/change-status").hasAnyRole("CHECKER")
				.antMatchers(HttpMethod.PUT, "/api/registration/update/status").hasAnyRole("CHECKER")
				.antMatchers(HttpMethod.POST, "/api/registration//multiple-registration").hasAnyRole("CHECKER")
				// .and().httpBasic();
				.anyRequest().authenticated();
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encodedPassword());
	}
	@Bean
	public PasswordEncoder encodedPassword() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Bean
	public TokenFilter authenticationJwtTokenFilter() {
		return new TokenFilter();
	}
}
