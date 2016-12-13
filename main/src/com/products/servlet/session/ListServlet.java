package com.products.servlet.session;

import com.products.ProductManager;
import com.products.model.Product;
import com.products.model.data.ProductDatabaseManagerImpl;
import com.products.model.data.ProductMemoryManagerImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.products.APP_CONSTANTS.JSP;

/**
 * Created by Paweł Ziemiańczyk on 02.12.16.
 */
@WebServlet(name = "Products",
            urlPatterns = "/list")
public class ListServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(ListServlet.class);

    private ProductManager productManager;

    public static final String JSP_LIST = JSP + "list.jsp";

    private Integer counter = 0;

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing ListServlet");
        productManager = new ProductMemoryManagerImpl();// new ProductDatabaseManagerImpl();
        getServletContext().setAttribute("servletContextCounter",0);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.counter++;
            Integer servletContextCounter = (Integer)getServletContext().getAttribute("servletContextCounter");
            if(servletContextCounter != null){
                getServletContext().setAttribute("servletContextCounter", ++servletContextCounter);
            } else {
                logger.warn("Servlet context counter is null");
            }
            request.getSession().setAttribute("listCounter", this.counter);

            updateSessionCounter(request);

            logger.debug("GET List");
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            if(request.getSession().getAttribute("productManager") == null) {
                request.getSession().setAttribute("productManager", this.productManager);
            }

            String url = JSP_LIST;

            if(request.getParameter("action") != null &&
                    "remove".equals(request.getParameter("action")) &&
                    request.getParameter("productId") != null){

                ProductManager productManager = this.productManager;
                if(productManager != null) {
                    Product product = this.productManager.getProductByPK(Integer.valueOf(request.getParameter("productId")));
                    if (product != null) {
                        this.productManager.deleteProduct(product);
                    }
                }
            } else if (request.getParameter("productId") != null) {
                url = EditServlet.JSP_EDIT;
                logger.info("Edit product by productId " + request.getParameter("productId"));
            }
            else {
                request.setAttribute("products", productManager.getProductsList());
                logger.info("No productId.");
            }

            request.getSession().setAttribute("products", productManager.getProductsList());

            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
            requestDispatcher.forward(request, response);

        } catch (IOException exception) {
            logger.error("IOException", exception);
        } catch (ServletException e) {
            logger.error("ServletException", e);
        }
    }

    private void updateSessionCounter(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Integer counter = (Integer) session.getAttribute("counter");

        if (counter == null) {
            counter = new Integer(1);
        } else {
            counter = new Integer(counter.intValue() + 1);
        }

        session.setAttribute("counter", counter);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.counter++;
        req.getSession().setAttribute("listCounter", this.counter);
    }
}
