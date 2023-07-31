package mega.naemeal.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    Enumeration em = request.getHeaderNames();
    while(em.hasMoreElements()) {
      String name = (String)em.nextElement();
      String value = request.getHeader(name);
      System.out.println("name = " + name);
      System.out.println("value = " + value);
    }
    System.out.println("request.getContentType() = " + request.getContentType());


    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods","*");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    response.addHeader("Access-Control-Expose-Headers","*");

    // 이렇게 해서 빌드 해가지고 진행해보세요 !
    if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    }else {
      chain.doFilter(req, res);
    }
  }
  @Override
  public void destroy() {
  }
}