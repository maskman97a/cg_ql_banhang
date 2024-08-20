package vn.codegym.qlbanhang.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class GlobalExceptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(req, resp);
        } catch (Exception e) {
            req.getRequestDispatcher("/views/error.jsp").forward(req, resp);
            req.setAttribute("message", e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }
}
