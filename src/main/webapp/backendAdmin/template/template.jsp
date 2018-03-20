
<!DOCTYPE html>
<html>
<%
    String root = request.getContextPath() + "/backendAdmin";
%>


<head>

    <meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>:::關網資訊股份有限公司:::</title>
    <link href="<%=root %>/css/index_layout.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=root %>/css/jquery-ui-1.9.2.custom.css"/>
    <link rel="stylesheet" href="<%=root %>/css/ui.jqgrid.css"/>
    <script type="text/javascript" src="<%=root %>/js/jquery-1.9.0.js"></script>
    <script type="text/javascript" src="<%=root %>/js/jquery-ui.js"></script>
    <script type="text/javascript" src="<%=root %>/js/jquery-ui-1.9.2.custom.js"></script>
    <script type="text/javascript" src="<%=root %>/js/grid.locale-en.js"></script>
    <script type="text/javascript" src="<%=root %>/js/gridSearch.js"></script>
    <script type="text/javascript" src="<%=root %>/js/tablednd.js"></script>
    <script type="text/javascript" src="<%=root %>/js/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="<%=root %>/js/jquery.ui.datepicker-zh-TW.js"></script>
    <script type="text/javascript" src="<%=root %>/js/epilgrim.sessionTimeoutHandler.js"></script>
    <script type="text/javascript" src="<%=root %>/template/lib/jquery.marquee.js"></script>
    <%-- lock --%>
    <script type="text/javascript" src="<%=root %>/js/lockUtils.js"></script>

    <link type="text/css" href="<%=root %>/template/css/docs.css" rel="stylesheet" media="all" />
    <link type="text/css" href="<%=root %>/template/css/jquery.marquee.css" rel="stylesheet" title="default" media="all" />


    <style type="text/css">
        h4, h5 {
            margin-bottom: 0;
        }

        .examples pre {
            margin-top: 0;


        }

        .marquee .author {
            display: none;
        }

        .marquee-author {
            float: left;
            width: 90px;
            text-align: right;
            padding: 4px 5px 1px 0;
        }
    </style>
    <!---// load jQuery from the GoogleAPIs CDN //--->
    <script>
        //共用的script請在這裡編輯
        //you can override this method in last javascript area
        function reloadPage(){
            window.location.reload();
        }
    </script>
</head>

<body >


<div id="chmis">
    <div style="width:100%; margin:0 auto; overflow:auto; _display:inline-block;">
        <div class="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="left" style="white-space:nowrap;"><span
                            class="textstyle_01">${login_name}-${company_name}</span></td>

                </tr>
            </table>
        </div>
        <div class="mes">
            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="left">
                        <div class="examples">

                            <ul id="marquee1" class="marquee">
                                <%----%>
                                <%--<li>現行整合服務平台已提供所建議功能，以稅務代理人身分登入，至「媒體申報檔--%>
                                <%--下載」功能，即可下載兩種格式(僅年月、含年月日)之電子發票媒體檔案供申報及其他運用。</li>--%>
                                <%--<li>買受人為非營業人者，得彙加申報計算連續發票號碼之銷項資料。</li>--%>
                                <%--<li>為避免營業人103年11-12期申報誤用測試版本，將於103年11-12月申報期結束後儘速將正式版本上版(暫定104年1月20日) ，以便利營業人更新版本並進行轉檔測試。</li>--%>
                                <%--<li>國稅局考量電子發票開立及申報特性與三聯式收銀機統一發票同，爰選用同申報格式代號35，避免新增格式代號影響大部分營業人系統修改。</li>--%>
                                <%--<li>以發票字軌期別判斷：103年以前開立之發票，可傳送發票類別1~6，104年1月1日起開立之發票，必須傳送發票類別7~8，若類別錯誤則發送錯誤提醒通知，請營業人進行修正</li>--%>
                                <%--<li>「本系統擬於今日(9/3) 20時至21時暫停服務1小時，進行系統維護, 若造成您的不便, 敬請見諒, 謝謝!」</li>--%>
                                <%--<li>本次系統升級已順利結束, 電子發票證明聯總計部分已加入千分號,</li>--%>
                                <%--<li>請您將電腦重新開關機一次, 以利印表機驅動程式更新. 謝謝!</li>--%>
                                <%--<li>若您已經重開機過, 請不用理會此訊息.</li>--%>

                            </ul>
                        </div>

                        <!--最新訊息跑馬燈文字露出區塊可以輸入隨便多少字-->

                    </td>
                    <td align="right">
                        <a href="javascript:editSelf()" title="修改"><span class="textstyle_01">修改個人資料</span></a>
                        <a href="javascript:logout()" title="登出系統"> <span class="textstyle_01">登出　</span>
                            <%--<img src="<%=root %>/images/icon_logout.gif" alt="登出系統" width="28" height="28" hspace="10"--%>
                            <%--border="0"/>--%>
                        </a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="spacer"></div>


    <div style="width:100%; margin:0 auto;  background-image: url(<%=root %>/images/bg.jpg); background-position: left;">
        <div class="menu">
            <a href="<%=request.getContextPath()%>/backendAdmin/loginServlet"><img src="<%=root %>/images/logo.jpg"
                                                                                   width="200" height="127"/></a>

            <div id="testMenu">

                <jsp:include page="menu.jsp"/>

            </div>
        </div>


        <div class="contant">
            <div align="center">
                <!-- 中間數據table區域 start -->
                <%--<table width="90%"  border="0" cellspacing="0" cellpadding="0">--%>
                <jsp:include page="content.jsp"/>

                <%--</table>--%>
            </div>
            <div class="spacer"></div>
        </div>
    </div>


    <div style="width:100%; margin:0 auto; overflow:auto; _display:inline-block;">
        <div class="top"></div>
        <div class="mes">
            <div style="text-align:center; width:100%">
                <span class="footerstyle_01">All rights reserved © 關網資訊股份有限公司‧本系統支援：Chrome 瀏覽器 ．螢幕解析度 1024x768 ．目前版本V.01 </span>
            </div>
        </div>
    </div>
</div>


</body>
</html>

<script>

    $(document).ready(function (){
        $("#marquee1").marquee();
    });

    var children = Array();

    $(function () {
        var logoutTime = parseInt('${logoutTime}');
        $('body').sessionTimeoutHandler({
            logoutUrl: '<%=request.getContextPath()%>/backendAdmin/logOutServlet',
            keepAliveUrl: '<%=request.getContextPath()%>/backendAdmin/loginServlet',
            redirUrl:'<%=request.getContextPath()%>/backendAdmin/logOutServlet',
            warnWhenLeft: 1 * 1000 * 60, // logoutTime前一分鐘跳出警示
            defaultSessionTime: logoutTime * 1000 * 60,
            checkTimeBeforeRedirect: false,

            getSessionTimeLeft: function ( event, data ){
                data.callback.call(data.plugin, data.plugin.options.defaultSessionTime);
            }
        });

    });

    function logout() {
        closeAllChildren();
    }

    function editSelf() {
        var url = "<%=request.getContextPath()%>/backendAdmin/userEditSelfServlet?method=edit";

        MM_openBrWindow(url,800,800);
    }

    function openPopWin(theURL, winName, win_width, win_height) {
        var PosX = (screen.width - win_width) / 2;
        var PosY = (screen.Height - win_height) / 2;
        var features = "width=" + win_width + ",height=" + win_height + ",top=" + PosY + ",left=" + PosX;
        children[children.length] = window.open(theURL, winName, features);
    }

    function closeAllChildren() {
        for (var n = 0; n < children.length; n++) {
            children[n].close();
        }
        location.href = "<%=request.getContextPath()%>/backendAdmin/logOutServlet";
//        window.close();
    }


    function MM_openBrWindow(theURL,win_width,win_height) {
        var PosX = (screen.width-win_width)/2;
        var PosY = (screen.Height-win_height)/2;
        var features = "width="+win_width+",height="+win_height+",top="+PosY+",left="+PosX;
        children[children.length]=window.open(theURL, '', features);
    }

</script>