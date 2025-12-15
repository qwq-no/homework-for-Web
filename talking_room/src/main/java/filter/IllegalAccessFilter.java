package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "IllegalAccessFilter", urlPatterns = "/*")
public class IllegalAccessFilter implements Filter {

    private static final String[] WHITE_LIST = {"/index.jsp", "/register.jsp", "/mm_error.jsp","/same.jsp","/no_same.jsp","/captcha_error.jsp","/captcha","/talking_room"};
    @Override
    public void init(FilterConfig filterConfig) {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        for (String whitePath : WHITE_LIST) {
            if (whitePath.equals(servletPath)) {
                chain.doFilter(request, response);
                return;
            }
        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}