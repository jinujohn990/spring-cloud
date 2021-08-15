package com.jinu.interview.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.jinu.interview.dto.response.AuthenticatorReponse;
import com.jinu.interview.security.MyUserDetails;
import com.jinu.interview.security.PrincipalUser;
import com.jinu.interview.dto.response.User;
import com.jinu.interview.serviceimpl.JwtUtil;


@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	DiscoveryClient client;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		List<ServiceInstance> intsnces = client.getInstances("authenticator-service");
		UserDetails userDetails = null;
		AuthenticatorReponse authenticatorReponse = null;
		String jwtToken = request.getHeader("jwt");
		if (jwtToken != null || SecurityContextHolder.getContext().getAuthentication() == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			headers.set("jwt", jwtToken);
			try {
				HttpEntity<HttpHeaders> entity = new HttpEntity(headers);
				ResponseEntity<AuthenticatorReponse> responseEntity = restTemplate.exchange("http://authenticator-service/user/getUserfromJwt",
						HttpMethod.GET, entity, AuthenticatorReponse.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			HttpEntity<HttpHeaders> entity = new HttpEntity(headers);
				ResponseEntity<AuthenticatorReponse> responseEntity = restTemplate.exchange("http://authenticator-service/user/getUserfromJwt",
						HttpMethod.GET, entity, AuthenticatorReponse.class);
			/*ResponseEntity<AuthenticatorReponse> responseEntity = restTemplate.exchange(builder.toUriString(),
					HttpMethod.GET, entity, AuthenticatorReponse.class);*/
			authenticatorReponse = responseEntity.getBody();
			PrincipalUser principalUser = createPrincipalUserFromUserResponse(authenticatorReponse.getUser());
			userDetails = new MyUserDetails(principalUser);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

		}
		filterChain.doFilter(request, response);
	}

	private PrincipalUser createPrincipalUserFromUserResponse(User user) {
		try {
			PrincipalUser principalUser = new PrincipalUser();
			principalUser.setUsername(user.getUsername());
			principalUser.setEmail(user.getEmail());
			principalUser.setEnabled(user.getEnabled());
			principalUser.setAccountNonLocked(user.getAccountNonLocked());
			principalUser.setAccountNonExpired(user.getAccountNonExpired());
			if(user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
				principalUser.setAuthorities(user.getAuthorities().stream().map(e->new SimpleGrantedAuthority(e.getAuthority())).collect(Collectors.toList()));
			}
			return principalUser;
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return null;
		
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request)
	  throws ServletException {
	    String path = request.getRequestURI();
	    return path.startsWith("/inteviewscheduler/swagger-ui.html");

	}
}
