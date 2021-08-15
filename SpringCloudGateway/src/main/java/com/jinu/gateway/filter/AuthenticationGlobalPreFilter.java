package com.jinu.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import com.jinu.gateway.dto.AuthenticatorReponse;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import reactor.core.publisher.Mono;

//@Component
public class AuthenticationGlobalPreFilter implements GlobalFilter {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	private LoadBalancerClient loadBalancer;
	
	//@Autowired
	//WebClient webClient;

	final Logger LOGGER = LoggerFactory.getLogger(AuthenticationGlobalPreFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			LOGGER.info("Global Pre Filter executed");
			AuthenticatorReponse authenticatorReponse = null;
			ServerHttpRequest request = exchange.getRequest();
			String jwtToken = request.getHeaders().getOrEmpty("jwt").get(0);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			headers.set("jwt", jwtToken);
			//ServiceInstance instance = loadBalancer.choose("authenticator-service");
			InstanceInfo service = eurekaClient.getApplication("authenticator-service").getInstances().get(0);
			String hostName = service.getHostName();
			int port = service.getPort();
			/*
			 * UriComponentsBuilder builder =
			 * UriComponentsBuilder.fromHttpUrl("http://"+service.getIPAddr()+":"+service.
			 * getPort()+"/user/getUserfromJwt") .queryParam("jwt", jwtToken);
			 */
			
			/*UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl("http://AUTHENTICATOR-SERVICE/user/getUserfromJwt")
					.queryParam("jwt", jwtToken);
			HttpEntity entity = new HttpEntity(headers);
			ResponseEntity<AuthenticatorReponse> responseEntity = restTemplate.exchange(builder.toUriString(),
					HttpMethod.GET, entity, AuthenticatorReponse.class);
			authenticatorReponse = responseEntity.getBody();*/
			
			Mono<AuthenticatorReponse> response = WebClient.create()
			.get()
			.uri(uriBuilder -> uriBuilder.path("http://AUTHENTICATOR-SERVICE/user/getUserfromJwt").queryParam("jwt", jwtToken).build())
			.retrieve()
			.bodyToMono(AuthenticatorReponse.class);
			
			response.subscribe(authenticatorresponse->{
				if (authenticatorresponse != null && authenticatorresponse.getStatus().equals("success")
						&& authenticatorReponse.getMessage().equals("User authentication sucessfull")) {
					
				} else {
					onError(exchange, "authentication failed");
				}
			});
			
			/*if (authenticatorReponse != null && authenticatorReponse.getStatus().equals("success")
					&& authenticatorReponse.getMessage().equals("User authentication sucessfull")) {
				exchange.getRequest().mutate().header("userId", response.getUser().getUsername())
						.build();
			} else {
				throw new Exception();
			}*/

		} catch (Exception e) {
			return this.onError(exchange, e.getMessage());
		}

		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
	}

}