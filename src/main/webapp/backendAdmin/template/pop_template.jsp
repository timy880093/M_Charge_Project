<!DOCTYPE html>
<html>
<%
    String root = request.getContextPath() + "/backendAdmin";
%>


<head>


    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>:::關網資訊股份有限公司:::</title>
    <link href="<%=root %>/css/index_layout.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=root %>/css/jquery-ui.css"/>
    <%--<link rel="stylesheet" href="<%=root %>/css/jquery-ui-1.9.2.custom.css"/>--%>
    <script type="text/javascript" src="<%=root %>/js/jquery-1.9.0.js"></script>
    <script type="text/javascript" src="<%=root %>/js/jquery-ui.js"></script>
<%--<script type="text/javascript" src="<%=root %>/js/jquery-ui-1.9.2.custom.js"></script>--%>
    <script type="text/javascript" src="<%=root %>/js/jquery.validate.js"></script>
    <script type="text/javascript" src="<%=root %>/js/jquery.form.min.js" ></script>
    <script type="text/javascript" src="<%=root %>/js/jquery.ui.datepicker-zh-TW.js"></script>


</head>

<body>


<div id="chmis">


    <%--<div class="contant">--%>
            <div align="center">
                <!-- 中間數據table區域 start -->
                    <jsp:include page="content.jsp"/>

            </div>

        <%--</div>--%>
    </div>






</body>
</html>

<script type="text/javascript">
    function MM_openBrWindow(theURL,win_width,win_height) {
        var PosX = (screen.width-win_width)/2;
        var PosY = (screen.Height-win_height)/2;
        var features = "width="+win_width+",height="+win_height+",top="+PosY+",left="+PosX;
       window.open(theURL, '', features);
    }
</script>