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
	<meta name="description" content="關網資訊計費系統">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="Keywords" content=">關網資訊,計費系統,會員中心">
	<title>關網資訊-計費系統</title>

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
            padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
            background:#fff;
        }
    </style>
</head>

<body>

		 <div class="content">
		 	<div id="leftMain" class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
		 	<ui>
		 		<li class="alert alert-danger fade in">系統更新公告：系統將於9月15號晚上23:00到隔日9月16號03：00進行更新，歷時系統將無法使用，請需要開立發票的公司行號，避開該時段開立或傳送發票。</li>
		 		<li class="alert alert-warning">news 2</li>
		 		<li class="alert alert-warning">news 3</li>
		 	</ui>
		 	</div>
			<div id="main" class="col-xs-12 col-sm-12 col-md-6 col-lg-4 alert alert-success">
                    <div class="login-form">
                        <c:url var="loginUrl" value="/j_spring_security_check" />
                        <form action="${loginUrl}" method="post" class="form-horizontal">
                        	<div class="text-center">
                        	<img src="<%=request.getContextPath()%>/images/gateweb.png" class="img-responsive" alt="關網資訊"/>
                        	</div>
                            <c:if test="${param.error != null}">
                                <div class="alert alert-danger">
                                    <p>使用者帳號和密碼不合！請檢查您的帳號和密碼，或洽客服。</p>
                                </div>
                            </c:if>
                            <c:if test="${param.logout != null}">
                                <div class="alert alert-success">
                                    <p>成功登出</p>
                                </div>
                            </c:if>
                            <div class="input-group input-sm">
                                <label class="input-group-addon" for="username"><i class="fa fa-user"></i></label>
                                <input type="text" class="form-control" id="username" name="username" placeholder="Enter Username" required>
                            </div>
                            <div class="input-group input-sm">
                                <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label> 
                                <input type="password" class="form-control" id="password" name="password" placeholder="Enter Password" required>
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
                                 
                            <div class="form-group text-center">
                                <input type="submit" class="btn btn-info" value="登入"><p></p>
                            </div>
                        </form>
                </div>
            </div>
		 	<div id="rightMain" class="col-xs-12 col-sm-12 col-md-6 col-lg-4 alert alert-info">
            <ui>
		 		<li >取消動態密碼登入設定</li>
		 		<li >修正下入帳時間條件後,抓出來的入帳資料excel卻不符合</li>
		 		<li >修正帶出資料請包含統編</li>
		 		<li >匯出EXCEL帳單有問題 公司應該改為姓名 客戶識別碼須帶入統編</li>
		 		<li >修正佣金計算可修改備註</li>
		 		<li >出入帳發送email, 20170910</li>
		 		<li >出入帳管理，公司名稱可模糊查詢 20170919</li>
                <li >入帳備註</li>
                <li >首次申請?</li>
		 		
		 	</ui>
		 	</div>
            
        </div>
</body>
</html>

