package com.products.servlet.session;

import com.products.ProductManager;
import com.products.model.Product;
import com.products.model.validator.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.products.APP_CONSTANTS.EDITABLE_PRODUCT;
import static com.products.APP_CONSTANTS.JSP;
import static com.products.servlet.session.ListServlet.JSP_LIST;

/**
 * Created by pziemianczyk on 05.12.16.
 */
@WebServlet(name = "EditProduct",
        urlPatterns = "/edit/*")
public class EditServlet extends HttpServlet {

    public static final String JSP_EDIT = JSP + "edit.jsp";

    private final Logger logger = LoggerFactory.getLogger(EditServlet.class);

    private String labelAction;
    private Product product;
    private Boolean addingNew = false;

    private Integer editCounter = 0;

    public Integer getEditCounter() {
        return editCounter;
    }

    public void setEditCounter(Integer editCounter) {
        this.editCounter = editCounter;
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        logger.info("EditServlet doGet start, productId is: " + productId);

        this.editCounter++;
        req.setAttribute("editCounter", this.editCounter);

        if(productId != null) {
            ProductManager productManager = (ProductManager) req.getSession().getAttribute("productManager");
            if(productManager != null) {
                this.product = productManager.getProductByPK(Integer.valueOf(productId));
                req.getSession().setAttribute("product", product);
            }
            this.labelAction = "Edit";
            this.addingNew = false;
            req.setAttribute("labelAction",this.labelAction);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(JSP_EDIT);
            requestDispatcher.forward(req, resp);
        } else {
            String action = req.getParameter("action");
            logger.info("Action: " + action);
            if("add_product".equals(action)){
                this.addingNew = true;
                this.labelAction = "Add new";
                req.setAttribute("labelAction",this.labelAction);

                System.out.println("Adding new product");
                this.product = new Product();
                req.getSession().setAttribute("product", this.product);

                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(JSP_EDIT);
                requestDispatcher.forward(req, resp);
            } else {
                this.addingNew = false;
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{

            String action = req.getParameter("action");
            logger.info("Action: " + action);

            this.editCounter++;
            req.setAttribute("editCounter", this.editCounter);

            if(action != null && "save_product".equals(action)) {
                String name = req.getParameter("productName");
                String price = req.getParameter("productPrice");
                ProductValidator productValidator = new ProductValidator();
                if(productValidator.validate(req, name, price)) {
                    saveProduct(req, name, price);
                } else {
                    if(this.addingNew){
                        if(productValidator.isProductNameValid(name)) {
                            this.product.setName(name);
                        }
                        if(productValidator.isProductPriceValid(price)){
                            this.product.setPrice(Double.valueOf(price));
                        }
                        req.setAttribute("product", this.product);
                    }
                    req.setAttribute("errorMessage", productValidator.getValidationErrorMessage());
                    req.setAttribute("labelAction", this.labelAction);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher(JSP_EDIT);
                    rd.forward(req, resp);
                    logger.info("Forward to JSP_EDIT");
                    return;
                }
            }

            resp.sendRedirect(req.getContextPath() + "/list");
        } catch (ServletException e){
            e.printStackTrace();
        }
    }

    private void saveProduct(HttpServletRequest req, String name, String price) {
        Product product = (Product) req.getSession().getAttribute(EDITABLE_PRODUCT);
        if (product != null) {
            if (product.getPk() != null && !"".equals(product.getPk())) {
                updateProduct(req, name, price, product);
            } else {
                addNewProduct(req, name, price);
            }
        }
    }

    private void addNewProduct(HttpServletRequest req, String name, String price) {
        Product product = new Product();

        product.setName(name);
        try {
            product.setPrice(Double.valueOf(price));
        } catch (NumberFormatException e) {
            logger.error("Add new product, wrong price format", e);
        }

        ProductManager productManager = (ProductManager) req.getSession().getAttribute("productManager");
        if(productManager != null) {
            productManager.insertProduct(product);
        }
    }

    private void updateProduct(HttpServletRequest req, String name, String price, Product product) {
        ProductManager productManager = (ProductManager) req.getSession().getAttribute("productManager");
        product.setName(name);
        try {
            product.setPrice(Double.valueOf(price));
        } catch (NumberFormatException e) {
            logger.error("Update product, wrong price format", e);
        }
        if(productManager != null) {
            productManager.updateProduct(product);
        }
    }
}
