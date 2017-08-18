<%--
  Created by IntelliJ IDEA.
  User: simon
  Date: 2014/7/4
  Time: 下午 02:10
  To change this template use File | Settings | File Templates.
--%>

<html>

<head>
<title>關網資訊-會員中心</title>

<link href="<%=request.getContextPath()%>/css/index_layout.css"
	rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	FONT-FAMILY: "微軟正黑體", "新細明體";
	FONT-SIZE: 10px;
	color: #CCCCCC;
	padding-left: 10px;
	padding-top: 15px;
	line-height: 20px;
}

.style2 {
	FONT-FAMILY: "微軟正黑體", "新細明體";
	FONT-SIZE: 14px;
	color: #FF0000;
	line-height: 30px;
}
-->
</style>
</head>

<body>
	<%--<%--%>
	<%--session.invalidate();--%>
	<%--%>--%>
	<form action='j_security_check' method="POST" id="loginForm">
		<div align="center">
			<br />
            <table width="975" border="0" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/images/bg2.jpg">
				<tr>
					<td colspan="3"><img
						src="<%=request.getContextPath()%>/images/003.jpg" width="975"   border="0"
						height="243" usemap="#Map2" /></td>
				</tr>
				<tr>
					<td valign="top"><img src="<%=request.getContextPath()%>/images/004.jpg"
						width="364" height="150" /></td>
					<td width="247"	background="<%=request.getContextPath()%>/images/007.jpg">
						<table width="100%" border="0" cellspacing="0" cellpadding="3">
							<tr>
                                系統維護時間:早上8:30~9:00<br>
								系統時間:
								<div id="dt">自動顯示時間</div>
							</tr>
							<tr>
								<td width="65" align="left"><img
									src="<%=request.getContextPath()%>/images/006.gif" width="32"
									height="18" /></td>
								<td width="173" align="left"><INPUT type='text'
									name='j_username' value=''></td>
							</tr>
							<tr>
								<td align="left"><img
									src="<%=request.getContextPath()%>/images/007.gif" width="32"
									height="18" /></td>
								<td align="left"><INPUT type='password' name='j_password' autocomplete="off"
									value='' onKeyPress="return submitenter(this,event)"></td>
							</tr>
							<%--<tr>--%>
							<%--<td align="left">&nbsp;</td>--%>
							<%--<td align="left"><span class="textstyle_03">${ERROR_MESSAGE}</span></td>--%>
							<%--</tr>--%>
							<tr>
								<td align="left">
									<%--<a href="http://gauth.apps.gbraad.nl/" target="_blank" class="link">忘記密碼</a>--%>
								</td>
								<td align="left" class="style2" valign="top">密碼+動態密碼 <%--<INPUT type='submit' name='submit' value='登入'>--%>
									<a href="javascript:submit()"><img
										src="<%=request.getContextPath()%>/images/008.jpg" alt="登入"
										width="75" height="26" hspace="5" hspace="5" border="0" /></a> <%--<a href="#"><img src="<%=request.getContextPath()%>/images/009.jpg" alt="申請帳號" width="75" height="26" border="0" /></a>--%>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top"><img src="<%=request.getContextPath()%>/images/005.jpg"
						width="364"  height="150" border="0" usemap="#Map" /></td>
				</tr>
				<tr>
					<td colspan="3"><img
						src="<%=request.getContextPath()%>/images/006.jpg" width="975" height="192" /></td>
				</tr>
			</table>
			<table width="800" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" class="style1">作業系統請使用微軟系統Windows 系列 │
						建議瀏覽器： Google Chrome │ 解析度1024*768瀏覽，可獲得最佳的效果。&#13;<br />
						使用上若有問題請E-Mall&nbsp;<u><a href="mailto:scm@bankpro.com.tw"
							class="link">service01@gateweb.com.tw</a></u>&nbsp;
						或電洽0986-577577洽客服人員詢問&#13;
					</td>
				</tr>
			</table>

			<br />
			<p>&nbsp;</p>
		</div>

		<map name="Map" id="Map">
            <area shape="rect" coords="153,27,290,48" href="https://drive.google.com/file/d/0B4d69aaoa3EuVW9HWENMd1lxSEk/view?usp=sharing" target="_blank" />
            <area shape="rect" coords="155,60,280,76" href="https://drive.google.com/file/d/0B4d69aaoa3EudnZhRmFWTVNTaFE/view?usp=sharing" target="_blank" />
            <area shape="rect" coords="138,89,280,115" href="http://www.stdtime.gov.tw/chinese/home.aspx" target="_blank" />
            <area shape="rect" coords="138,121,301,141" href="http://www.google.com/intl/zh-TW/chrome/browser/" target="_blank" />
		</map>
		<map name="Map2" id="Map2">
			<area shape="rect" coords="750,211,835,232"
				href="http://gauth.apps.gbraad.nl/" target="_blank" />
		</map>
	</form>


</body>
</html>


<script language="javascript">
	function submitenter(myfield, e) {
		var keycode;
		if (window.event)
			keycode = window.event.keyCode;
		else if (e)
			keycode = e.which;
		else
			return true;

		if (keycode == 13) {
			myfield.form.submit();
			return false;
		} else
			return true;
	}

	function submit() {
		document.getElementById("loginForm").submit();
	}

	var currentDate = new Date(
<%=new java.util.Date().getTime()%>
	);
	function run() {
		currentDate.setSeconds(currentDate.getSeconds() + 1);
		var time = "";
		var year = currentDate.getFullYear();
		var month = currentDate.getMonth() + 1;
		var day = currentDate.getDate();
		var hour = currentDate.getHours();
		var minute = currentDate.getMinutes();
		var second = currentDate.getSeconds();
		if (hour < 10) {
			time += "0" + hour;
		} else {
			time += hour;
		}
		time += ":";
		if (minute < 10) {
			time += "0" + minute;
		} else {
			time += minute;
		}
		time += ":";
		if (second < 10) {
			time += "0" + second;
		} else {
			time += second;
		}
		document.getElementById("dt").innerHTML = year + "年" + month + "月"
				+ day + "日" + time;
	}
	window.setInterval("run();", 1000);
//    setTimeout ("self.location.reload ();" ,1000*60*15);
</script>