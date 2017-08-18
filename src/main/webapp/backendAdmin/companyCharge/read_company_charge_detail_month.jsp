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
            var packageMap = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            $("#business_no").val(packageMap.business_no);
            $("#company_name").val(packageMap.company_name);
            $("#package_name").val(packageMap.package_name);
            $("#charge_cycle").val(packageMap.charge_cycle);
            $("#base_quantity").val(packageMap.base_quantity);
            $("#single_price").val(packageMap.single_price);
            $("#max_price").val(packageMap.max_price);
//            $("#free_quantity").val(packageMap.free_quantity);
            $("#free_month_base").val(packageMap.free_month_base);
            $("#pre_payment").val(packageMap.pre_payment);
            $("#sales_price").val(packageMap.sales_price);
            $("#contract_limit").val(packageMap.contract_limit);
            $("#addition_quantity").val(packageMap.addition_quantity);
            $("#free_month").val(packageMap.free_month);
            $("#real_start_date").val(packageMap.real_start_date);
            $("#real_end_date").val(packageMap.real_end_date);
            $("#start_date").val(packageMap.start_date);
            $("#end_date").val(packageMap.end_date);
            $("#gift_price").val(packageMap.gift_price);
            $("#dealer_company_name").val(packageMap.dealer_company_name);
            $("#dealer_name").val(packageMap.dealer_name);
            $("#broker_cp2").val(packageMap.broker_cp2);
            $("#broker2").val(packageMap.broker2);
            $("#broker_cp3").val(packageMap.broker_cp3);
            $("#broker3").val(packageMap.broker3);
            $("#createDate").text(packageMap.create_date);
            $("#modifyDate").text(packageMap.modify_date);
            $("#creator").text(packageMap.creator);
            $("#modifier").text(packageMap.modifier);
        }
    });

</script>