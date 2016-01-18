package io.doug.zuulserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import io.doug.zuulserver.filter.ZuulAuthFilter;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
@EnableZuulProxy
public class ZuulServerApplication {
	
	@Autowired
	private ZuulProperties zuulProperties;
	
    public static void main(String[] args) {
    	System.setProperty("spring.application.name", "zuulserver");
        //new SpringApplicationBuilder(ZuulServerApplication.class).web(true).run(args);
    	SpringApplication.run(ZuulServerApplication.class, args);
    }
    
    @Bean
    public ZuulAuthFilter myZuulFilter(ProxyRouteLocator routeLocator) {
        return new ZuulAuthFilter(routeLocator, this.zuulProperties.isAddProxyHeaders());
    }

}
