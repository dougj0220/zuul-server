package io.doug.zuulserver.filter;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class ZuulAuthFilter extends ZuulFilter {
	
	protected Logger logger = Logger.getLogger(ZuulAuthFilter.class
			.getName());
	
	private ProxyRouteLocator routeLocator;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private boolean addProxyHeaders;
	
	public ZuulAuthFilter(ProxyRouteLocator routeLocator, boolean addProxyHeaders) {
		this.routeLocator = routeLocator;
		this.addProxyHeaders = addProxyHeaders;
		
	}

	@Override
	public boolean shouldFilter() {

		return true;
	}

	@Override
	public Object run() {

		RequestContext ctx = RequestContext.getCurrentContext();
		String auth = ctx.getRequest().getHeader("X-Auth-Doug");
		if (auth != null && "foo".equals(auth)) {
			logger.info("Ok, found X-Auth-Doug request header");
			 ctx.setSendZuulResponse(false);
			 ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
		}
		
		return null;
		/*final String requestURI = this.urlPathHelper
				.getPathWithinApplication(ctx.getRequest());
		ProxyRouteSpec route = this.routeLocator.getMatchingRoute(requestURI);
		if (route != null) {
			String location = route.getLocation();
			if (location != null) {
				ctx.put("requestURI", route.getPath());
				ctx.put("proxy", route.getId());

				if (route.getRetryable() != null) {
					ctx.put("retryable", route.getRetryable());
				}

				if (location.startsWith("http:") || location.startsWith("https:")) {
					ctx.setRouteHost(getUrl(location));
					ctx.addOriginResponseHeader("X-Zuul-Service-Doug", location);
				}
				else if (location.startsWith("forward:")) {
					ctx.set("forward.to", StringUtils.cleanPath(
							location.substring("forward:".length()) + route.getPath()));
					ctx.setRouteHost(null);
					return null;
				}
				else {
					// set serviceId for use in filters.route.RibbonRequest
					ctx.set("serviceId", location);
					ctx.setRouteHost(null);
					ctx.addOriginResponseHeader("X-Zuul-ServiceId", location);
					ctx.addZuulResponseHeader("X-Zuul-ServiceId-doug-no-location", "Doug is here");
				}
				if (this.addProxyHeaders) {
					ctx.addZuulRequestHeader("X-Forwarded-Host",
							ctx.getRequest().getServerName() + ":"
									+ String.valueOf(ctx.getRequest().getServerPort()));
					ctx.addZuulRequestHeader(ZuulHeaders.X_FORWARDED_PROTO,
							ctx.getRequest().getScheme());
					if (StringUtils.hasText(route.getPrefix())) {
						ctx.addZuulRequestHeader("X-Forwarded-Prefix", route.getPrefix());
					}
				}
			}
		}
		else {
			//log.warn("No route found for uri: " + requestURI);
			ctx.set("forward.to", requestURI);
		}
		return null;*/
	}

	@Override
	public String filterType() {

		return "pre";
	}

	@Override
	public int filterOrder() {

		return 1;
	}
	
	private URL getUrl(String target) {
		try {
			return new URL(target);
		}
		catch (MalformedURLException ex) {
			throw new IllegalStateException("Target URL is malformed", ex);
		}
	}

}
