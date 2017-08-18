
<%--
  Created by IntelliJ IDEA.
  User: simon
  Date: 2014/7/8
  Time: 下午 04:44
  To change this template use File | Settings | File Templates.
--%>

<html>
<head>
    <link href="<%=request.getContextPath()%>/css/index_layout.css" rel="stylesheet" type="text/css" />
    <title></title>
</head>
<body>
<%
    session.invalidate();
%>
登入失敗:
1.密碼輸入錯誤
2.時間不正確
     error!!
<a href="<%=request.getContextPath()%>/backendAdmin/loginServlet">按此回到登入頁</a>
</body>
</html>
