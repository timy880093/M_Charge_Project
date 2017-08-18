<form action="<%=request.getContextPath()%>/backendAdmin/prepayDeductEditServlet?method=insertPrepay" method="post"
      id="prepayForm">
    <input type="hidden" id="prepayDeductMasterId" name="prepayDeductMasterId"/>
    <input type="hidden" id="companyId" name="companyId"/>
    <table border="1">
        <tr>
            <td>
                <p style="color: brown">預繳表</p>
            </td>
        </tr>
        <tr>
            <td>
                <table cellspacing="0" cellpadding="0" border="0" class="imglist">
                    <tr>
                        <th colspan="2">用戶是否啟用超額預繳扣抵</th>
                        <td colspan="2">
                            <div id="status" style="display: inline-block"></div>
                            <input type="button" value="暫停" id="stop" style="display: none">
                            <input type="button" value="開啟" id="open" style="display: none">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <p style="color: brown">新增預繳資料</p>
            </td>
        </tr>
        <tr>
            <td>
                <table cellspacing="0" cellpadding="0" border="0" class="imglist">
                    <tr>
                        <th colspan="2">
                            <label style="color: red;vertical-align: middle">*</label>繳款年月(計算年月)(YYYYMM)：
                        </th>
                        <td colspan="2">
                            <input type="text" id="calYm" name="calYm" maxlength="6">
                        </td>
                    </tr>
                    <tr>
                        <th colspan="2">
                            <label style="color: red;vertical-align: middle">*</label>預繳金額：
                        </th>
                        <td colspan="2">
                            <input type="number" id="money" value="0" name="money" min="0" max="99999">
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4">
                            <input type="button" value="建立" id="send">
                            <input type="button" value="關閉視窗" id="close" onclick="window.close()">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <p style="color: brown">預繳歷史資料</p>
            </td>
        </tr>
        <tr>
            <td>
                <table cellspacing="0" cellpadding="0" border="0" class="imglist" id="prepayHistory">
                    <tr>
                        <td>繳款(計算)年月</td>
                        <td>預繳金額</td>
                        <td>狀態</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>

<script type="text/javascript">
    var path = '<%=request.getContextPath()%>';

    $(function () {
        var status;
        if (${!empty REQUEST_SEND_OBJECT_0}) {
            var prepayDeductMaster = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            console.log("prepayDeductMaster=" + prepayDeductMaster);

            $("#prepayDeductMasterId").val(prepayDeductMaster.prepayDeductMasterId);
            $("#companyId").val(prepayDeductMaster.companyId);

            status = prepayDeductMaster.isEnableOver;
            console.log("prepayDeductMaster.isEnableOver=" + prepayDeductMaster.isEnableOver);
            var statusValue = '';
            switch(status){
                case 'Y':
                    statusValue = '生效中';
                    $("#stop").show();
                    break;
                case 'N':
                    statusValue = '暫停';
                    $("#open").show();
                    break;
            }
            $("#status").text(statusValue);
        }

        if (${!empty REQUEST_SEND_OBJECT_1}) {
            var histVOList = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
            $.each(histVOList, function (i, element) {
                var status = "";
                switch(element.status) {
                    case 1:
                        status = "未繳"; break; //status = "生效";原本的生效是指合約建立，但這裡是預繳資料建立。
                    case 2:
                        status = "作廢"; break;
                    case 3:
                        status = "出帳"; break;
                    case 4:
                        status = "入帳"; break;
                    case 5:
                        status = "佣金"; break;
                    default:
                        if(element.cash_detail_id==undefined){
                            //「取消預繳」時，會沒有對應的cash_detail和cash_master
                            status = "已取消預繳";
                        }
                        break;
                }
                $("#prepayHistory").append(
                    "<tr><td>"+element.cal_ym+"</td><td>"+element.money+"</td><td>"+status+"</td></tr>");
            });
        }

        $('tr').addClass('even');
        $('th').addClass('even');

        $("#prepayForm").validate({
            onkeyup: function (element) {
                $(element).valid()
            },
            errorClass: "my-error-class",
            rules: {
                calYm: {required: true},
                mainAmount: {required: true}
            },
            ignore: ":hidden",
            messages: {
                calYm: {required: "*請填寫公司!"},
                mainAmount: {required: "*請填寫金額!"}
            }
        });
    });

    $("#send").click(function () {
        if($("#prepayForm").valid()==true){
            $("select").prop('disabled',false);
            $("input[type=text] ").prop('disabled',false);
            $("input[type=radio]").prop('disabled',false);
            $("input[type=number]").prop('disabled',false);

            $("#send").prop('disabled', true);
            $("#prepayForm").ajaxSubmit(
                {
                    beforeSubmit: function(){},
                    success: function(resp,st,xhr,$form) {
                        alert('成功!');
                        window.location.reload();
                        //window.close();
                    },
                    error:function(xhr, ajaxOptions, thrownError){
                        alert('失敗!');
                        alert(xhr.status+'/'+thrownError);
                        $("#send").prop('disabled', false);
                    }
                });
        }
    });

    //停用超額扣抵機制
    $("#stop").click(function(){
        $.ajax({
            url: "<%=request.getContextPath()%>/backendAdmin/prepayDeductAjaxServlet?method=stop",
            dataType: 'json',
            data: {prepayDeductMasterId:$("#prepayDeductMasterId").val()},
            async: false,
            success: function (data) {
                alert("已暫停!");
                location.reload();
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert('doSubmit()-'+xhr.status+'/'+thrownError);
            }
        });
    });

    //啟用超額扣抵機制
    $("#open").click(function(){
        $.ajax({
            url: "<%=request.getContextPath()%>/backendAdmin/prepayDeductAjaxServlet?method=open",
            dataType: 'json',
            data: {prepayDeductMasterId:$("#prepayDeductMasterId").val()},
            async: false,
            success: function (data) {
                alert("已生效!");
                location.reload();
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert('doSubmit()-'+xhr.status+'/'+thrownError);
            }
        });
    });


</script>