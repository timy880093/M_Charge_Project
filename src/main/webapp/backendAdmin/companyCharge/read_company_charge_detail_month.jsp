<table border="1">
    <tr>
        <td colspan="4">
            <p style="color: brown">檢視合約明細</p>
        </td>
    </tr>
    <tr>
        <td>公司統編：</td>
        <td><input type="text" id="business_no" disabled></td>
        <td>公司名稱：</td>
        <td><input type="text" id="company_name" disabled></td>
    </tr>
    <tr>
        <td colspan="2">方案名稱:</td>
        <td colspan="2"><input type="text" id="package_name"  disabled></td>
    </tr>
    <tr>
        <td colspan="2">計費周期：</td>
        <td colspan="2"><select id="charge_cycle" disabled>
            <option value="1" >足月</option>
            <option value="2">破月</option>
        </select>
        </td>
    </tr>
    <tr>
        <td colspan="2">基本使用量(每月)：</td>
        <td colspan="2"><input type="number" id="base_quantity" disabled></td>
    </tr>
    <tr>
        <td colspan="2">超過使用量後單一張發票收費價格：</td>
        <td colspan="2"><input type="number" id="single_price" disabled></td>
    </tr>
    <tr>
        <td colspan="2">最大收費價格(每月)：</td>
        <td colspan="2"><input type="number" id="max_price" disabled></td>
    </tr>
    <%--<tr>--%>
        <%--<td colspan="2">免費張數(每月)：</td>--%>
        <%--<td colspan="2"><input type="number" id="free_quantity" disabled></td>--%>
    <%--</tr>--%>
    <tr>
        <td colspan="2">免費月份：</td>
        <td colspan="2"><input type="number" id="free_month_base" disabled></td>
    </tr>
    <tr>
        <td colspan="2">預付金額：</td>
        <td colspan="2"><input type="number" id="pre_payment" disabled></td>
    </tr>
    <tr>
        <td colspan="2">銷售價格：</td>
        <td colspan="2"><input type="number" id="sales_price" disabled></td>
    </tr>
    <tr>
        <td colspan="2">綁約月份：</td>
        <td colspan="2"><input type="number" id="contract_limit" disabled></td>
    </tr>
    <tr>
        <td colspan="4">
            <table border="1" style="background-color: pink">
                <tr>
                    <td colspan="4" style="color: red">客製化設定部分：</td>
                </tr>
                <tr>
                    <td>贈送金額：</td>
                    <td><input type="text" id="gift_price"  disabled/></td>
                    <td>加贈免費月份:</td>
                    <td><input type="number" id="free_month" disabled/></td>
                </tr>
                <%--<tr>--%>
                    <%--<td>加贈每月使用張數：</td>--%>
                    <%--<td><input type="number" id="addition_quantity" disabled/></td>--%>
                    <%--<td></td>--%>
                    <%--<td></td>--%>
                <%--</tr>--%>
                <tr>
                    <td>實際起始日：</td>
                    <td><input type="text" id="real_start_date"  disabled/></td>
                    <td>實際結束日：</td>
                    <td><input type="text" id="real_end_date"  disabled/></td>
                </tr>
                <tr>
                    <td>計算起始日：</td>
                    <td><input type="text" id="start_date"  disabled/></td>
                    <td>計算結束日：</td>
                    <td><input type="text" id="end_date" disabled/></td>
                </tr>
                <tr>
                    <td>經銷公司：</td>
                    <td>
                        <input type="text" id="dealer_company_name" disabled>
                    </td>
                    <td>經銷公司業務員：</td>
                    <td>
                        <input type="text" id="dealer_name" disabled>
                    </td>
                </tr>
                <tr>
                    <td>介紹公司：</td>
                    <td>
                        <input type="text" id="broker_cp2" disabled>
                    </td>
                    <td>介紹公司的介紹人：</td>
                    <td>
                        <input type="text" id="broker2" disabled>
                    </td>
                </tr>
                <tr>
                    <td>裝機公司：</td>
                    <td>
                        <input type="text" id="broker_cp3" disabled>
                    </td>
                    <td>裝機公司的裝機人：</td>
                    <td>
                        <input type="text" id="broker3" disabled>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <th>建立時間：</th>
        <td><label><span id="createDate"></span></label></td>
        <th>建立人員：</th>
        <td><label><span id="creator"></span></label></td>
    </tr>
    <tr>
        <th>修改時間：</th>
        <td><label><span id="modifyDate"></span></label></td>
        <th>修改人員：</th>
        <td><label><span id="modifier"></span></label></td>
    </tr>

    <tr>
        <td colspan="4" align="center">

            <input type="button" id="close" onclick="window.close()" value="關閉">
        </td>
    </tr>
    <tr>
        <td colspan="4">&nbsp;</td>
    </tr>

</table>

<script type="text/javascript">
    $(function(){
        if (${!empty REQUEST_SEND_OBJECT_0}) {
            var chargeDetailViewObject = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            $("#business_no").val(chargeDetailViewObject.businessNo);
            $("#company_name").val(chargeDetailViewObject.companyName);
            $("#package_name").val(chargeDetailViewObject.packageName);
            $("#charge_cycle").val(chargeDetailViewObject.chargeCycle);
            $("#base_quantity").val(chargeDetailViewObject.baseQuantity);
            $("#single_price").val(chargeDetailViewObject.singlePrice);
            $("#max_price").val(chargeDetailViewObject.maxPrice);
            $("#free_month_base").val(chargeDetailViewObject.freeMonthBase);
            $("#pre_payment").val(chargeDetailViewObject.prePayment);
            $("#sales_price").val(chargeDetailViewObject.salesPrice);
            $("#contract_limit").val(chargeDetailViewObject.contractLimit);
            $("#addition_quantity").val(chargeDetailViewObject.additionQuantity);
            $("#free_month").val(chargeDetailViewObject.freeMonth);
            $("#real_start_date").val(chargeDetailViewObject.realStartDate);
            $("#real_end_date").val(chargeDetailViewObject.realEndDate);
            $("#start_date").val(chargeDetailViewObject.startDate);
            $("#end_date").val(chargeDetailViewObject.endDate);
            $("#gift_price").val(chargeDetailViewObject.giftPrice);
            $("#dealer_company_name").val(chargeDetailViewObject.dealerCompanyName);
            $("#dealer_name").val(chargeDetailViewObject.dealerName);
            $("#broker_cp2").val(chargeDetailViewObject.brokerCp2);
            $("#broker2").val(chargeDetailViewObject.broker2);
            $("#broker_cp3").val(chargeDetailViewObject.brokerCp3);
            $("#broker3").val(chargeDetailViewObject.broker3);
            $("#createDate").text(chargeDetailViewObject.createDate);
            $("#modifyDate").text(chargeDetailViewObject.modifyDate);
            $("#creator").text(chargeDetailViewObject.creator);
            $("#modifier").text(chargeDetailViewObject.modifier);
        }
    });

</script>