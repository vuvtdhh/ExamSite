<%-- 
    Document   : index
    Created on : May 9, 2018, 7:05:14 PM
    Author     : dangh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/style.css">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PTUDCSDL2 - 1560685</title>
    </head>
    <body>
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">PTUDCSDL2</a>
                </div>
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">Home</a></li>
                    <li><a href="#">Đăng Ký</a></li>
                </ul>
            </div>
        </nav>
        <div class="login">
            <div class="login-triangle"></div>
  
            <h2 class="login-header">Log in</h2>

            <form class="login-container" action="DangNhap" method="post">
                <p><input name="username" type="text" placeholder="User Name"></p>
                <p><input name="password" type="password" placeholder="Password"></p>
                <p><input type="submit" value="Log in"></p>
            </form>
        </div>
    </body>
</html>
