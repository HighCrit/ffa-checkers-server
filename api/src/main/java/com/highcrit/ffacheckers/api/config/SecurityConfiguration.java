package com.highcrit.ffacheckers.api.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final String socketHostAddress = InetAddress.getByName("socket").getHostAddress();

  public SecurityConfiguration() throws UnknownHostException {
    System.out.println(socketHostAddress);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        // All GET requests are okay
        .antMatchers(HttpMethod.GET)
        .permitAll()
        // For any non-get request to replay you'll need to have the ip resolved from 'socket'
        .antMatchers("/replay/**")
        .hasIpAddress(socketHostAddress)
        // We'll permit routes on a route by route basis
        .anyRequest()
        .denyAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }
}
