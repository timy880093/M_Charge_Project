<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/charge_edit.js" ></script>
<form action="<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=insert" method="post" id="chargeForm">
    <table cellspacing="0" cellpadding="0" border="0" class="imglist">
        <input type="hidden" id="type" name="type" value="1">
        <input type="hidden" id="chargeId" name="chargeId" value="">

        <tr>
            <td colspan="4">
                <p style="color: brown">建立收費模組 步驟二 月租型</p>
            </td>
        </tr>
        <tr>
            <th colspan="2">方案名稱:</th>
            <td colspan="2"><input type="text" id="packageName" name="packageName"></td>
        </tr>
        <tr>
            <th colspan="2">計費周期：</th>
            <td colspan="2"><select id="chargeCycle" name="chargeCycle">
                <option value="1">足月</option>
                <%--<option value="2">破月</option>--%>
            </select>
            </td>
        </tr>
        <tr>
            <th colspan="2">收費區間：</th>
            <td colspan="2">
                <select id="feePeriod" name="feePeriod">
                    <option value="1">年繳</option>
                    <option value="2">季繳</option>
                    <%--<option value="3">月繳</option>--%>
                </select>
            </td>
        </tr>
        <tr>
            <th colspan="2">基本使用量(每月)：</th>
            <td colspan="2"><input type="number" id="baseQuantity" value="0" name="baseQuantity" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">超過使用量後單一張發票收費價格：</th>
            <td colspan="2"><input type="number" id="singlePrice" value="0" name="singlePrice" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">最大收費價格(每月)：</th>
            <td colspan="2"><input type="number" id="maxPrice" value="0" name="maxPrice" min="0" max="99999"></td>
        </tr>
        <input type="hidden" id="freeQuantity" value="0" name="freeQuantity" />
        <%--<tr>--%>
            <%--<th colspan="2">免費張數(每月)：</th>--%>
            <%--<td colspan="2"><input type="number" id="freeQuantity" value="0" name="freeQuantity" min="0" max="99999"></td>--%>
        <%--</tr>--%>
        <tr>
            <th colspan="2">免費月份：</th>
            <td colspan="2"><input type="number" id="freeMonth" value="0" name="freeMonth" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">銷售價格：</th>
            <td colspan="2"><input type="number" id="salesPrice" value="0" name="salesPrice" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">綁約月份：</th>
            <td colspan="2"><input type="number" id="contractLimit" name="contractLimit" value="12" min="0" max="99999" readonly></td>
        </tr>
        <tr>
            <th colspan="2">預付金額：</th>
            <td colspan="2"><input type="number" id="prePayment" value="0" name="prePayment" min="0" max="99999" readonly></td>
        </tr>
        <tr>
            <th colspan="2">目前方案狀態：</th>
            <td colspan="2"><p style="color: red"><span id="status"></span>

                <p></td>
        </tr>
        <%--<tr>--%>
            <%--<th colspan="2">預付可扣抵超用：</th>--%>
            <%--<td colspan="2"><select id="prepaidAdditionEnable" name="prepaidAdditionEnable"><option value="0">不可扣抵</option></select></td>--%>
        <%--</tr>--%>
        <tr>
            <td colspan="4" align="center">
                <input type="button" id="stopCharge" value="暫停方案" style="display: none">
                <input type="button" id="openCharge" value="打開方案" style="display: none">
                <input type="button" id="send" value="確認">
                <input type="button" id="close" value="關閉">
            </td>
        </tr>
        <tr>
            <td colspan="4">&nbsp;</td>
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
    </table>
 </form>
<script type="text/javascript">
    var path = '<%=request.getContextPath()%>';

    $(function () {
        bindEvent();

        if (${!empty REQUEST_SEND_OBJECT_1}) {
            var chargeVO = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
            $("#chargeId").val(chargeVO.chargeId);
            $("#packageName").val(chargeVO.packageName);
            $("#chargeCycle").val(chargeVO.chargeCycle);
            $("#feePeriod").val(chargeVO.feePeriod);
            $("#baseQuantity").val(chargeVO.baseQuantity);
            $("#singlePrice").val(chargeVO.singlePrice);
            $("#maxPrice").val(chargeVO.maxPrice);
            $("#freeQuantity").val(chargeVO.freeQuantity);
            $("#freeMonth").val(chargeVO.freeMonth);
            $("#prePayment").val(chargeVO.prePayment);
            $("#contractLimit").val(chargeVO.contractLimit);
            $("#salesPrice").val(chargeVO.salesPrice);
            var status = chargeVO.status;
            var statusValue = '';
            var $stopCharge = $("#stopCharge");
            switch(status){
                case 1:
                    statusValue = '生效中';
                    invalidEdit();
                    $stopCharge.show();
                    $stopCharge.prop('disabled',false);
                    break;
                case 2:
                    statusValue = '未生效';
                    $stopCharge.show();
                    $stopCharge.prop('disabled',false);
                    break;
                case 3:
                    statusValue = '結束';
                    invalidEdit();
                    $stopCharge.show();
                    $stopCharge.prop('disabled',false);
                    break;
                case 4:
                    statusValue = '暫停';
                    invalidEdit();
                    $("#openCharge").show();
                    $("#openCharge").prop('disabled',false);
                    break;
            }
            $("#status").text(statusValue);
            $("#createDate").text(chargeVO.createDate);
            $("#modifyDate").text(chargeVO.modifyDate);
            $("#creator").text(chargeVO.creator);
            $("#modifier").text(chargeVO.modifier);
        }


        $("#salesPrice").blur(function(){
            var intSalesPrice = parseInt($("#salesPrice").val());
            var intContractLimit = parseInt($("#contractLimit").val());
            var prePayment = intSalesPrice * intContractLimit;
            $("#prePayment").val(prePayment);
        });

        $("#contractLimit").blur(function(){
            var intSalesPrice = parseInt($("#salesPrice").val());
            var intContractLimit = parseInt($("#contractLimit").val());
            var prePayment = intSalesPrice * intContractLimit;
            $("#prePayment").val(prePayment);
        });

        if (${empty REQUEST_SEND_OBJECT_1}) {
            var now = new Date();
            now.setDate(now.getDate());
            var month = (now.getMonth() + 1);
            var day = now.getDate();
            if (month < 10)
                month = "0" + month;
            if (day < 10)
                day = "0" + day;
            var nowDate = now.getFullYear() + '-' + month + '-' + day;

        }



        $("#send").click(function () {
            if ($("#chargeForm").valid() == true) {
                $("#chargeForm").ajaxSubmit(
                        {
                            beforeSubmit: function () {
                            },
                            success: function (resp, st, xhr, $form) {
                                alert('成功!');
                                window.close();
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert('失敗!');
                                alert(xhr.status + '/' + thrownError);
                            }
                        });
            }

        });


        $("#chargeForm").validate({
            onkeyup: function(element) {$(element).valid()},
            errorClass: "my-error-class",
            rules: {
                packageName: {required: true},
                baseQuantity: {required: true},
                salesPrice : {required: true},
                singlePrice : {required: true},
                maxPrice : {required: true},
//                freeQuantity : {required: true},
                freeMonth : {required: true},
                prePayment : {required: true},
                contract : {required: true}
            },
            ignore: ":hidden",
            messages: {
                packageName: {required: "*請填寫方案名稱!"},
                baseQuantity: {required: "*請填寫基本使用量!"},
                salesPrice: {required: "*請填寫銷售價格!"},
                singlePrice : {required: "*請填寫單一張發票收費價格!"},
                maxPrice : {required: "*請填寫最大收費價格!"},
//                freeQuantity : {required: "*請填寫免費張數!"},
                freeMonth : {required: "*請填寫免費月份!"},
                prePayment : {required: "*請填寫預付金額!"},
                contract : {required: "*請填寫綁約月份!"}

            }


        });

        if (${!empty REQUEST_SEND_OBJECT_0}) {
            var method = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            if(method=='read'){
                $("input[type=button]").hide();
                $("#close").show();
                $(".ui-datepicker-trigger").hide();
            }

        }


    });


</script>