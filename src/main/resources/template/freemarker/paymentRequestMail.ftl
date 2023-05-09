<html>

<body>
<p> 敬愛的<span style="color:#2F5496">${paymentRequestMailFreemarkerData.companyName}</span>您好：</p>

<#if paymentRequestMailFreemarkerData.correction == true>
    <p>
        <span lang="EN-US" style="font-size:18.0pt;color:red">***</span>
        <span style="font-size:18.0pt;color:red">帳務更正<span lang="EN-US">***</span>
        </span>
    </p>
    <p>
        <span style="font-size:18.0pt;color:red">因本公司人員疏失，先前發送的帳務通知金額計算有誤，特此發信更正，舊金額之費用發票因已開立寄送，會將差額部分另外開立發票予 &nbsp;&nbsp;貴公司，造成不便，敬請見諒。
        </span>
    </p>
</#if>

<p>以下資訊為貴公司${paymentRequestMailFreemarkerData.ymString}月份繳款帳單之明細，請查收。</p>

<p><span style="font-size:16.0pt;color:red">***提醒您***</span></p>
<#if paymentRequestMailFreemarkerData.extraNotice == true>
    <p>
        ${paymentRequestMailFreemarkerData.extraNoticeMessage}
    </p>
</#if>
<p>
    <span style="font-size:16.0pt">繳款期限為
        <span lang="EN-US" style="color:red">${paymentRequestMailFreemarkerData.paymentExpirationDate}</span>
    </span>
</p>
<p>
    <span style="font-size:14.0pt">貴公司專屬帳號</span><br>
    <span style="color:black">銀行：中國信託仁愛分行 (822-0152)</span><br>
    <span style="color:black">帳號：</span>
    <b>
        <span lang="EN-US" style="font-size:16.0pt">
            <span style="color:red">${paymentRequestMailFreemarkerData.ctbcVirtualAccount}</span>
        </span>
    </b><span style="font-size:14.0pt">(請勿扣匯費或手續費)</span><br>
    <span style="color:black;">戶名：關網資訊股份有限公司</span>
</p>
<p>敬請撥冗繳納，以利您處理電子發票相關業務。</p>

<table style="border:1px black solid; border-collapse: collapse" cellpadding="5" border='2'>
    <tr>
        <th> 方案名稱</th>
        <th> 繳費類型</th>
        <th> 發票額度</th>
        <th> 含稅費用</th>
    </tr>
    <#list paymentRequestMailFreemarkerData.paymentRequestMailBillingItemSet as billingItem>
        <tr>
            <td> ${billingItem.itemName} </td>
            <td> ${billingItem.chargeType} </td>
            <td> ${billingItem.quota} </td>
            <td> ${billingItem.taxIncludedAmount} </td>
        </tr>
    </#list>
    <tr>
        <td colspan=2></td>
        <td>合計</td>
        <td>${paymentRequestMailFreemarkerData.paymentRequestTotalAmount}</td>
    </tr>
</table>
<p></p>
<p></p>
<#if paymentRequestMailFreemarkerData.haveOverage>
    <p>超額明細如下：</p>
    <table style="border:1px black solid; border-collapse: collapse" cellpadding="5" border='2'>
        <tr>
            <th> 計算月份</th>
            <th> 發票額度</th>
            <th> 使用張數</th>
            <th> 超出額度</th>
            <th> 單張超額費用</th>
            <th> 計算金額(未稅)</th>
            <th> 含稅金額</th>
        </tr>
        <#list paymentRequestMailFreemarkerData.paymentRequestMailOverageItemList as overageItem>
            <tr>
                <td> ${overageItem.calYm}</td>
                <td> ${overageItem.quota}</td>
                <td> ${overageItem.usage}</td>
                <td> ${overageItem.overageCount}</td>
                <td> ${overageItem.unitPrice}</td>
                <td> ${overageItem.taxExcludedAmount}</td>
                <td> ${overageItem.taxIncludedAmount}</td>
            </tr>
        </#list>
        <tr>
            <td colspan=5></td>
            <td>合計</td>
            <td>${paymentRequestMailFreemarkerData.paymentRequestOverageTotalAmount}</td>
        </tr>
    </table>

    <p> 以下為此次超額費用的級距表 </p>
    <#list paymentRequestMailFreemarkerData.paymentRequestMailGradeTableList as gradeTable>
        <p>${gradeTable.name}</p>
        <p>最大收費：${gradeTable.maximumCharge}</p>
        <table style="border:1px black solid; border-collapse: collapse" cellpadding="5" border='2'>
            <tr>
                <th> 張數起</th>
                <th> 張數迄</th>
                <th> 固定費用(未稅)</th>
                <th> 單張費用</th>
            </tr>
            <#list gradeTable.paymentRequestMailGradeTableItemList as gradeTableItem>
                <tr>
                    <td> ${gradeTableItem.from}</td>
                    <td> ${gradeTableItem.to}</td>
                    <td> ${gradeTableItem.fixPrice}</td>
                    <td> ${gradeTableItem.unitPrice}</td>
                </tr>
            </#list>
        </table>
        <p><span style="color:red">${gradeTable.accumulateAnnouncement}</span></p>
    </#list>

</#if>
<p>以上說明， 若有任何疑問請與我們聯繫，感謝您。</p>
<#if paymentRequestMailFreemarkerData.oBank.oBankAdvert>
    <br/>
    <#if paymentRequestMailFreemarkerData.oBank.oBankAdvertUrl?has_content>
        <a href="${paymentRequestMailFreemarkerData.oBank.oBankAdvertUrl}">
            <img src='cid:O_Bank_Advert01' width="360" height="335">
        </a>
    <#else>
        <img src='cid:O_Bank_Advert01' width="360" height="335">
    </#if>
</#if>
<p>--------------------------------------</p>
<p><a title="關網資訊股份有限公司" href="http://www.gateweb.com.tw">www.gateweb.com.tw</a></p>
<p>106663台北市大安區忠孝東路四段285號2樓之2</p>
<p>Tel. 02-7750-5070</p>
<p>Fax. 02-8773-5339 </p>
<p>『客戶服務時間』</p>
<p>平常上班日09:30~12:00,13:30~18:00</p>
</body>

</html>
