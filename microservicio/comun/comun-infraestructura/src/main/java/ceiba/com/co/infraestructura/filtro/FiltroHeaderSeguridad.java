package ceiba.com.co.infraestructura.filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = { "/*" })
public class FiltroHeaderSeguridad implements Filter {

	private static final String X_FRAME_OPTIONS = "X-Frame-Options";
	private static final String PRAGMA = "Pragma";
	private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	private static final String X_XSS_PROTECTION = "X-XSS-Protection";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader(X_XSS_PROTECTION, "1; mode=block");
		httpServletResponse.setHeader(X_CONTENT_TYPE_OPTIONS, "nosniff");
		httpServletResponse.setHeader(PRAGMA, "no-cache");
		httpServletResponse.setHeader(X_FRAME_OPTIONS, "SAMEORIGIN");
		chain.doFilter(request, response);
	}
}