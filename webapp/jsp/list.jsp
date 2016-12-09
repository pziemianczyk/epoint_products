

<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    </head>
    <body>
        <h2>Products list</h2>

        <c:forEach items="${sessionScope.products}" var="item">
            <a href="/products/edit?productId=${item.pk}">${item}</a>
            <a href="/products/list?action=remove&productId=${item.pk}">Remove</a>
            <br>
        </c:forEach>

        <a href="/products/edit?action=add_product">Add product</a>

        <br />
        <br />
        <label>Servlet counter: "${sessionScope.listCounter}"</label>
        <label>ServletContext counter: "${applicationScope.servletContextCounter}"</label>
        <label>Servlet SessionScope counter: "${sessionScope.counter}"</label>

        <br />
        <%@include file="/jsp/logout.jsp" %>

    </body>
</html>
