package net.resultadofinal.micomercial.util;

import javax.servlet.*;
import java.io.IOException;

public class UTF8Filter implements Filter{
	private String encoding;
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
			throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		fc.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		encoding=filterConfig.getInitParameter("rewuestEncoding");
		if(encoding==null)
			encoding="UTF-8";
	}

}
