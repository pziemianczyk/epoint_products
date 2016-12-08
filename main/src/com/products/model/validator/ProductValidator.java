package com.products.model.validator;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by pziemianczyk on 06.12.16.
 */
public class ProductValidator {

    private Boolean nameValid = true;
    private Boolean priceValid = true;

    public boolean isProductNameValid(String name){
        if(name != null && !"".equals(name)) {
            this.nameValid = true;
            return this.nameValid;
        }
        this.nameValid = false;
        return this.nameValid;
    }

    public boolean isProductPriceValid(String price){
        if(price == null || "".equals(price)){
            this.priceValid = false;
            return this.priceValid;
        }
        try {
            Double.valueOf(price);
            this.priceValid = true;
            return this.priceValid;
        } catch (NumberFormatException e){
            this.priceValid = false;
            return this.priceValid;
        }
    }

    public boolean validate(HttpServletRequest req, String name, String price) {
        Boolean res = true;
        if(!isProductNameValid(name)){
            req.setAttribute("validName", false);
            res = false;
        }
        if(!isProductPriceValid(price)){
            req.setAttribute("validPrice", false);
            res = false;
        }

        return res;
    }

    public String getValidationErrorMessage() {
        StringBuilder sb = new StringBuilder("");
        if(!this.priceValid){
            sb.append("Price is not valid");
        }
        if(!this.nameValid){
            if(!"".equals(sb.toString())){
                sb.append(". ");
            }
            sb.append("Name is not valid");
        }
        return sb.toString();
    }

}
