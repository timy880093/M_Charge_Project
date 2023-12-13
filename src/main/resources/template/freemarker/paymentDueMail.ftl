<html>
<body>
<p>敬愛的用戶您好：</p>

<p> 貴公司所使用的關網資訊雲端電子發票服務繳款單，將於<span lang="EN-US" style="color:red;font-weight:bold;">${paymentRequestMailFreemarkerData.paymentExpirationDate}</span>到期，
敬請儘速繳納。</p>

<p>
    <span style="font-size:14.0pt">貴公司專屬帳號</span><br>
    <span style="color:black">銀行：中國信託仁愛分行 (822-0152)</span><br>
    <span style="color:black">帳號：</span>
    <b>
        <span lang="EN-US" style="font-size:16.0pt">
            <span style="color:red">${paymentRequestMailFreemarkerData.ctbcVirtualAccount}</span>
        </span>
    </b><span style="font-size:14.0pt">(請勿扣匯費或手續費)</span><br>
    <span style="color:black;">戶名：關網資訊股份有限公司</span><br>
    <span>敬請撥冗繳納，以利您處理電子發票相關業務。</span>
</p>

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

<p style="color:red">若已繳款，請提供匯款收據，以利會計人員對帳。</p>

<p>關網特別提醒您：<br>
    <span>關網已採用系統自動對帳服務，敬請於繳款截止日前完成繳納！</span><br>
    <span>若逾期未繳，系統將可能強制停用，</span><br>
    <span style="color: red; font-weight: bold;">後續發票資訊無法上傳財政部，可能對貴司造成極大困擾！</span><br>
    <span>為避免此情形發生，請務必留意，謝謝！</span><br>
</p>

<p>
    <span>以上說明，</span><br>
    <span>若有任何疑問請與我們聯繫，感謝您。</span><br>
</p>
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
<p>105台北市松山區復興北路167號12樓之一</p>
<p>Tel. 02-7750-5070</p>
<p>Fax. 02-8773-5339 </p>
<p>『客戶服務時間』</p>
<p>平常上班日09:30~12:00,13:30~18:00</p>
</body>

</html>
