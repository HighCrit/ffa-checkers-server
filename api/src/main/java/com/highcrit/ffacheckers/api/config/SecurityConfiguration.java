package com.highcrit.ffacheckers.api.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final String socketIp = InetAddress.getByName("socket").getHostAddress();

  public SecurityConfiguration() throws UnknownHostException {
    // Empty constructor because the initialization of socketIp might throw something
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    String replayAntPattern = "/replays/**";

    http.csrf()
        .disable()
        .authorizeRequests()
        // All GET requests are okay
        .antMatchers(HttpMethod.GET, replayAntPattern, "/moves/**")
        .permitAll()
        // For any non-get request to replay you'll need to have the ip resolved from 'socket'
        .antMatchers(HttpMethod.POST, replayAntPattern)
        .hasIpAddress(socketIp)
        .antMatchers(HttpMethod.DELETE, replayAntPattern)
        .hasIpAddress(socketIp)
        // We'll permit routes on a route by route basis
        .anyRequest()
        .denyAll();
  }
}
