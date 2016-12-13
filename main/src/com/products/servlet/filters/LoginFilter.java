package com.products.servlet.filters;

import com.products.servlet.session.ListServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static com.products.APP_CONSTANTS.JSP;

/**
 * Created by pziemianczyk on 09.12.16.
 */
@WebServlet(name = "Login",
        urlPatterns = {"/login", "/logout"})
@WebFilter(filterName = "LoginFilter",
            urlPatterns = "/*")
public class LoginFilter extends HttpServlet implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    public static final String JSP_LOGIN = JSP + "login.jsp";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Login doGet");

        String url = JSP_LOGIN;

        String user = req.getParameter("user");
        logger.info("user: " + user);

        String pwd = req.getParameter("pwd");
        logger.info("pwd: " + pwd);

        if("user".equals(req.getParameter("user")) && "123456".equals(req.getParameter("pwd"))) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(ListServlet.JSP_LIST);
            requestDispatcher.forward(req, resp);
            return;
        }

        if("logout".equals(req.getParameter("action"))){
            req.getSession().setAttribute("userId", null);
            req.getSession().invalidate();
        }
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
        requestDispatcher.forward(req, resp);

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if("login".equals(req.getParameter("action"))){

            if("user".equals(req.getParameter("user")) && "123456".equals(req.getParameter("pwd"))) {
                req.getSession().setAttribute("userId", "1");

                resp.sendRedirect(req.getContextPath() + "/list");
            } else {
                resp.sendRedirect(req.getContextPath() + "/logout");
            }

        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("doFilter");

        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String name = params.nextElement();
            String value = request.getParameter(name);
            logger.info(request.getRemoteAddr() + "::Request Params::{"+name+"="+value+"}");
        }

        System.out.println("Schema: " + request.getScheme());

        HttpServletRequest req = (HttpServletRequest) request;
        logger.info("Ctx path: " + req.getContextPath());
        logger.info("Path info: " + req.getPathInfo());
        logger.info("Servlet path: " + req.getServletPath());
        logger.info("Request URI: " + req.getRequestURI());


        HttpServletResponse res = (HttpServletResponse) response;


        if(((HttpServletRequest) request).getSession().getAttribute("userId") == null){
            if(!"/login".equals(req.getServletPath())) {
                logger.info("Redirecting to servlet: " + req.getContextPath() + "/login");
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                logger.info("2 user: " + req.getParameter("user"));
                logger.info("2 pwd: " + req.getParameter("pwd"));
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }



    }

    @Override
    public void destroy() {

    }
}
