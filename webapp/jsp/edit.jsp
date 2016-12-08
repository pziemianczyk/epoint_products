<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    </head>
    <body>
        <h2>${labelAction} product</h2>

        <label>${errorMessage}</label>

        <form name="editProduct" method="post" action="edit">
            Name:<input type="text" name="productName" value="${product.name}"></input><br />
            Price:<input type="text" name="productPrice" value="${product.price}"></input><br />
            <input type="submit" name="cancel" value="Cancel" />
            <input type="submit" name="save" value="Save" onclick='this.form.action="edit?action=save_product";' />

        </form>
    </body>
</html>
