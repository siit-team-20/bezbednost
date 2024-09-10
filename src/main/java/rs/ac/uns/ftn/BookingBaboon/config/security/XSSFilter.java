// package rs.ac.uns.ftn.BookingBaboon.config.security;

// import jakarta.servlet.*;
// import jakarta.servlet.http.HttpServletRequest;

// import java.io.IOException;

// public class XSSFilter implements Filter {
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//         Filter.super.init(filterConfig);
//     }

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//         chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
//     }

//     @Override
//     public void destroy() {
//         Filter.super.destroy();
//     }
// }