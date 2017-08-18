<form action="<%=request.getContextPath()%>/backendAdmin/commissionLogEditServlet?method=edit" method="post" id="commissionLogEditForm">
    <table cellspacing="0" cellpadding="0" border="0" class="imglist">
        <input type="hidden" id="commissionLogId" name="commissionLogId" value="">
        <tr>
            <td colspan="13">
                <p style="color: brown">佣金名細</p>
            </td>
        </tr>
        <tr>
            <td colspan="30">
                <table cellspacing="0" cellpadding="0" border="0" class="imglist" id="commissionLogList">
                    <tr>
                        <th>用戶名稱</th>
                        <th>繳費類型</th>
                        <th>方案名稱</th>
                        <th>計算年月</th>
                        <th>出帳年月</th>
                        <th>入帳時間</th>
                        <th>入帳金額</th>
                        <th>出入帳金額是否相同</th>
                        <th>佣金類型</th>
                        <th>佣金比例</th>
                        <th>佣金金額</th>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td colspan="13" align="center">
                <input type="button" id="send" value="關閉">
            </td>
        </tr>
    </table>
</form>
<script type="text/javascript">
    var path = '<%=request.getContextPath()%>';

    $(function () {

        if (${!empty REQUEST_SEND_OBJECT_0}) {
            var comLogDetailList = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            $.each(comLogDetailList, function (i, element) {

                $("#commissionLogList").append('<tr><td>' + element.name + '</td>' +
                        '<td>'+ formatCashType(element.cash_type) +'</td>'+
                        '<td>'+ element.package_name +'</td>'+
                        '<td>'+ element.cal_ym +'</td>'+
                        '<td>'+ element.out_ym +'</td>'+
                        '<td>'+ element.in_date +'</td>'+
                        '<td>'+ element.tax_inclusive_price +'</td>'+
                        '<td>'+ formatIsUnMatch(element.is_inout_money_unmatch) +'</td>'+
                        '<td>'+ formatType(element.commission_type) +'</td>'+
                        '<td>'+ element.main_percent +'%</td>'+
                        '<td>'+ element.commission_amount +'</td></td>');

            })
        };

        function formatIsUnMatch(cellvalue){
            var value = cellvalue;
            switch(value){
                case "1": str='不相同'; break;
                default : str='相同';
            }
            return str;
        }

        function formatType(cellvalue) {
            var value = cellvalue;
            switch(value){
                case "0":str='抽傭金額'; break;
                case "1":str='抽成%數'; break;
                case "2":str='每筆代收'; break;
            }
            return str;
        }

        //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值
        function formatCashType(cashType){
            var str = "";
            switch (cashType) {
                case 1:
                    str = "月租預繳";
                    break;
                case 2:
                    str = "超額";
                    break;
                case 3:
                    str = "代印代計";
                    break;
                case 4:
                    str = "加值型服務";
                    break;
            }
            return str;
        }


    });


    $("#send").click(function () {
        window.close();
    });

</script>