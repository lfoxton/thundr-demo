package servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WrappingServletFilter implements Filter {

	public void destroy() {}

	public void init(FilterConfig arg0) throws ServletException {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
		chain.doFilter(wrappedRequest, response);
	}
	
}