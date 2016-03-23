<%--
  Created by IntelliJ IDEA.
  User: Arvin
  Date: 2016/2/22
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add product</title>
</head>
<body>

<form action="/product/save" method="post">
    <div>
        ProductName: <input name="product.name">
    </div>
    <div>
        ProductPrice: <input name="product.price">
    </div>

    <div>
        <input type="submit" value="添加">
    </div>
</form>

</body>
</html>
