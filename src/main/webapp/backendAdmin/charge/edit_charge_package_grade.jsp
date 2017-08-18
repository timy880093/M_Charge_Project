<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/charge_edit.js" ></script>
<form action="<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=insert" method="post" id="chargeForm">
    <table cellspacing="0" cellpadding="0" border="0" class="imglist">
        <input type="hidden" id="type" name="type" value="2">
        <input type="hidden" id="chargeId" name="chargeId" value="">

        <tr>
            <td colspan="4">
                <p style="color: brown">建立收費模組 步驟二 級距型</p>
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
            <th colspan="2">免費月份：</th>
            <td colspan="2"><input type="number" id="freeMonth" value="0" name="freeMonth" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">綁約月份：</th>
            <td colspan="2"><input type="number" id="contractLimit" name="contractLimit" value="12" min="0" max="99999" readonly></td>
        </tr>
        <tr>
            <th colspan="2">銷售價格：</th>
            <td colspan="2"><input type="number" id="salesPrice" value="0" name="salesPrice" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">預付金額：</th>
            <td colspan="2"><input type="number" id="prePayment" value="0" name="prePayment" min="0" max="99999" readonly></td>
        </tr>
        <tr>
            <th colspan="2">基本使用量(每月)：</th>
            <td colspan="2"><input type="number" id="baseQuantity" value="0" name="baseQuantity" min="0" max="99999"></td>
        </tr>
        <tr>
            <th colspan="2">是否有級距表：</th>
            <td colspan="2">
                <input type="radio" name="hasGrade" id="gradeTableY"  value="Y">有
                <input type="radio" name="hasGrade" id="gradeTableN"  value="N">無
            </td>
        </tr>

            <tr>
                <td colspan="4">
                    <div id="gradeY">
                        <table id="table-item" cellspacing="0" cellpadding="0" border="0" class="imglist">
                            <tr>
                                <th width="100px" style="vertical-align: middle; ">No</th>
                                <th width="150px">張數起</th>
                                <th width="150px">張數迄</th>
                                <th width="150px">價格</th>
                                <th></th>
                                <th></th>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>
                                    <input type="hidden" name="itemNo" style="width: 100px;" maxlength="13" />
                                    <input type="hidden" id="gradeId" name="gradeId" />
                                    <input type="number" id="cntStart" name="cntStart" min="0" max="999999999"/>
                                </td>
                                <td>
                                    <input type="number" id="cntEnd" name="cntEnd" min="0" max="999999999"/>
                                </td>
                                <td>
                                    <input type="number" id="price" name="price" min="0" max="999999999"/>
                                </td>
                                <td>
                                    <input type="button" id="btnAddItem" class="addButton" value="+"/>
                                </td>
                                <td>
                                    <input type="button"  class="removeButton" value="-"/>
                                </td>
                            </tr>

                        </table>
                    </div>

                    <div id="gradeN">
                        <table cellspacing="0" cellpadding="0" border="0" class="imglist">
                            <tr>
                                <th>級距區間費用</th>
                                <td>
                                    <input type="text" id="gradePrice" name="gradePrice" value="0" maxlength="100" style="width: 100px;"/>
                                </td>
                            </tr>
                            <tr>
                                <th>級距區間張數</th>
                                <td>
                                    <input type="text" id="gradeCnt" name="gradeCnt" value="0"  maxlength="100" style="width: 100px;"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
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
                freeMonth : {required: true},
                prePayment : {required: true},
                contractLimit : {required: true},
                gradePrice : {required: true},
                gradeCnt : {required: true}
            },
            ignore: ":hidden",
            messages: {
                packageName: {required: "*請填寫方案名稱!"},
                baseQuantity: {required: "*請填寫基本使用量!"},
                salesPrice: {required: "*請填寫銷售價格!"},
                freeMonth : {required: "*請填寫免費月份!"},
                prePayment : {required: "*請填寫預付金額!"},
                contractLimit : {required: "*請填寫綁約月份!"},
                gradePrice : {required: "*級距區間費用!"},
                gradeCnt : {required: "*級距區間張數!"}
            }
        });


        $("#gradeN").hide();
        $("#gradeTableN").click(function () {
            $("#gradeY").hide();
            $("#gradeN").show();
        });
        $("#gradeTableY").click(function () {
            $("#gradeY").show();
            $("#gradeN").hide();
        });

        //選擇要有級距表
        var count = 2;
        $('#btnAddItem').on('click', function () {
            console.log("btnAddItem click");
            var $tr = $(this).closest('tr');
            btnAddItemMethod($tr);
        });
        function btnAddItemMethod($tr){
            var $clone = $tr.clone();
            $clone.find('[name=itemNo]').attr('id','clone_'+count);
            $clone.find('[name=gradeId]').val('');
            $clone.find('[name=cntStart]').val('');
            $clone.find('[name=cntEnd]').val('');
            $clone.find('[name=price]').val('');
            $("#btnAddItem").remove();
            $tr.after($clone);
            bindItemClone('#clone_'+count);
            refreshNo();
            count++;
        }
        function bindItemClone(field){
            var $tr = $(field).closest('tr');
            $tr.find('#btnAddItem').on('click', function () {
                var $tr = $(this).closest('tr');
                btnAddItemMethod($tr);
            });
            $tr.find('.removeButton').on('click', function () {
                if ($('#table-item tr').length > 2) {  //header + first row = 2
                    $(this).closest('tr').remove();
                    refreshNo();
                }
            });
        }
        function refreshNo() {//更新商品序號
            $('#table-item tr').each(function (i, item) {
                if (i == 0)return;  //header row
                var $td = $(this).find('td').eq(0).text(i);
                var $focus = $(this).find('td').eq(1);
                $("input[type='text']:first", $focus).focus();
            });
        }
        //移除商品
        $('.removeButton').on('click', function () {
            if ($('#table-item tr').length > 2) {  //header + first row = 2
                $(this).closest('tr').remove();
                refreshNo();
            }
        });

        if (${!empty REQUEST_SEND_OBJECT_1}) {
            var chargeVO = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
            $("#chargeId").val(chargeVO.chargeId);
            $("#packageName").val(chargeVO.packageName);
            $("#chargeCycle").val(chargeVO.chargeCycle); //計費周期：足月
            $("#feePeriod").val(chargeVO.feePeriod); //收費區間：年繳/季繳
            $("#baseQuantity").val(chargeVO.baseQuantity); //基本使用量(每月)
            $("#freeMonth").val(chargeVO.freeMonth); //免費月份：
            $("#contractLimit").val(chargeVO.contractLimit); //綁約月份：
            $("#prePayment").val(chargeVO.prePayment); //月租的預付金額
            $("#salesPrice").val(chargeVO.salesPrice); //銷售價格：
            $("#gradePrice").val(chargeVO.gradePrice); //級距區間費用
            $("#gradeCnt").val(chargeVO.gradeCnt); //級距區間張數
            if(chargeVO.hasGrade == "Y"){
                $(':radio[id="gradeTableY"]').prop("checked", true);
                $(':radio[id="gradeTableY"]').click();

            }else{
                $(':radio[id="gradeTableN"]').prop("checked", true);
                $(':radio[id="gradeTableN"]').click();
            }

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

        if (${!empty REQUEST_SEND_OBJECT_2}) {
            var gradeVOList = $.parseJSON('${REQUEST_SEND_OBJECT_2}');
            var $tr =  $("#gradeId").closest('tr');
            $.each(gradeVOList, function (i, element) {
                console.log("i="+i);
                if(i != 0){
                    console.log("i != 0");
                    $tr.find("#btnAddItem").click();
                    $tr = $tr.next();
                }
                console.log("element.gradeId="+element.gradeId);
                $tr.find("[name=gradeId]").val(element.gradeId);
                $tr.find("[name=cntStart]").val(element.cntStart);
                $tr.find("[name=cntEnd]").val(element.cntEnd);
                $tr.find("[name=price]").val(element.price);

            });
        }

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