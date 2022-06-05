package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("*.do")
public class OpenSessionInViewFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            TransacsionManager.beginTransacsion();
            filterChain.doFilter(servletRequest,servletResponse);
            TransacsionManager.commit();
        } catch (Exception e) {
            TransacsionManager.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
