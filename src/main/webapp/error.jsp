<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--
  Created by IntelliJ IDEA.
  User: simon
  Date: 2014/7/4
  Time: 下午 02:10
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="description" content="關網資訊電子發票系統">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="Keywords" content=">關網資訊,電子發票系統,會員中心">
	<title>關網資訊-Exception</title>

	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
	<link href="/css/index_layout.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        body {
            padding: 10px; /* 60px to make the container go all the way to the bottom of the topbar */
            background:#fff;
        }
    </style>
        <link href="<%=request.getContextPath()%>/css/index_layout.css" rel="stylesheet" type="text/css" />
    
</head>
<body> 

<% Exception ex = (Exception)request.getAttribute("ex"); %>
	<div class="content">
		<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
		<div id="main" class="col-xs-10 col-sm-10 col-md-10 col-lg-10 alert alert-success fade in jumbotron">
<%-- 			<span style="color: #3366ff;"><%=request.getAttribute("javax.servlet.error.status_code") %></span>--%>
			<%-- <H2>Exception:<%= ex.getMessage()%></H2> --%>
			<h2 class="text-center alert alert-danger">不預期的錯誤發生，請稍後再試一次或通知系統管理員</h2>	
			<a class="btn btn-info" href="<%=request.getContextPath()%>/backendAdmin/loginServlet">按此回到首頁</a>
			
			<h3>
				<ul>
					<li>Exception: <c:out value="${requestScope['javax.servlet.error.exception']}" /></li>
					<li>Exception type: <c:out value="${requestScope['javax.servlet.error.exception_type']}" /></li>
					<li>Exception message: <c:out value="${requestScope['javax.servlet.error.message']}" /></li>
					<li>Request URI: <c:out value="${requestScope['javax.servlet.error.request_uri']}" /></li>
					<%--     <li>Servlet name: <c:out value="${requestScope['javax.servlet.error.servlet_name']}" /></li>--%>
					<li>Status code: <c:out value="${requestScope['javax.servlet.error.status_code']}" /></li>
				</ul>
			</h3>
			Stack trace: <pre class="alert alert-danger">${pageContext.out.flush()}${ex.printStackTrace(pageContext.response.writer)}</pre>
			
			
		</div>
		<div class="row"></div>


	</div>



<%-- 
<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>

	<c:if test="${exception != null}">
  <h4>${exception}</h4>
  <c:forEach var="stackTraceElem" items="${ex.stackTrace}">
    		<c:out value="${stackTraceElem}"/><br/>
  			</c:forEach>
</c:if> --%>

</body> 
</html> 