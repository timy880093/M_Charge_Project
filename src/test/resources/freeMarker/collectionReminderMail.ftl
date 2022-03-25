<html>

<body>
<p> 請注意：本信件是由「關網資訊雲端電子發票系統」自動產生與發送，請勿直接回覆。 </p>
<p> 敬愛的 <font color=\ "#FF0000\">${user.name}</font>您好：</p>
<p>以下資訊為貴公司${billYearMonth}月份繳款帳單之明細，請查收。</p>
<p>1.請點選以下連結<font color=" FF0000 ">「下載帳單」</font>，並惠予繳費</p>
<p>「<a title=" 上海銀行 繳費平台 - 關網資訊股份有限公司 " href=" https: //payment.fesc.com.tw/fespay/s1_011245492103711.jsp">上海銀行
    繳費平台- 關網資訊股份有限公司</a>」</p>
<p> 2.請輸入 <b> 公司統編 </b> →登入 </p>
<p> 3.確認月份帳單→確定 </p>
<p> 4.勾選繳費月份→帳單下載 </p>
<p> 5.再次點選帳單下載，即可產出繳費帳單 </p>
<p> < font color = "FF0000" > ★【詳細操作，請詳附件】★ < / font > </p>

<p>繳費明細</p>
<table style="border:1px black solid; border-collapse: collapse" cellpadding="5" border='2'>
    <tr>
        <th> 繳費類型</th>
        <th> 方案名稱</th>
        <th> 開立發票張數</th>
        <th> 含稅費用</th>
    </tr>
     <#list billingItemList as billingItem>
         <tr>
             <td> ${billingItem.type}</td>
             <td> ${billingItem.name} </td>
             <td> ${billingItem.taxExcludedAmount} </td>
             <td> ${billingItem.taxIncludedAmount} </td>
         </tr>
     </#list>
    <tr>
        <td colspan=2></td>
        <td>合計</td>
        <td>${bill.taxInclusiveAmount}</td>
    </tr>
</table>

<p>如對帳單資訊有疑問，請儘速致電關網資訊詢問，謝謝！</p>
<p><a title="關網資訊股份有限公司" href="http://www.gateweb.com.tw">www.gateweb.com.tw</a></p>
<p>客服時間：一般上班日09:30～12:00 , 13:30～18:00</p>
<p>客服專線：02-77505070</p>

</body>

</html>
