

<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    </head>
    <body>
        <h2>Products list</h2>

        <c:forEach items="${products}" var="item">
            <a href="/products/edit?productId=${item.pk}">${item}</a>
            <a href="/products/list?action=remove&productId=${item.pk}">Remove</a>
            <br>
        </c:forEach>

        //test komentarz do commitu
        <a href="/products/edit?action=add_product">Add product</a>
    </body>
</html>
