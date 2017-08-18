<form action="<%=request.getContextPath()%>/backendAdmin/dealerCompanyEditServlet?method=insert" method="post"
      id="dealerCompanyForm">
    <input type="hidden" id="dealerCompanyId" name="dealerCompanyId"/>
    <table border="1" >
        <tr>
            <td>
                <p style="color: brown"><span id="title"></span></p>
            </td>
        </tr>

        <tr>
            <td>
                <table cellspacing="0" cellpadding="0" border="0" class="imglist">
                    <tr>
                        <th><label style="color: red;vertical-align: middle">*</label>經銷商公司名稱：</th>
                        <td>
                            <input type="text" name="dealerCompanyName" id="dealerCompanyName" maxlength="100"/>
                        </td>
                        <th>統一編號：</th>
                        <td>
                            <input type="text" name="businessNo" id="businessNo" maxlength="8"/>
                        </td>
                    </tr>
                    <tr>
                        <th>電話：</th>
                        <td>
                            <input type="text" name="phone" id="phone" maxlength="50"/>
                        </td>
                        <th>傳真：</th>
                        <td>
                            <input type="text" name="fax" id="fax" maxlength="50"/>
                        </td>
                    </tr>
                    <tr>
                        <th>通訊地址：</th>
                        <td>
                            <input type="text" name="companyAddress" id="companyAddress" maxlength="100"/>
                        </td>
                        <th>email地址：</th>
                        <td>
                            <input type="text" name="email" id="email" maxlength="100"/>
                        </td>
                    </tr>
                    <tr>
                        <th>方案類型</th>
                        <td>
                            <%--<input type="radio" name="packageType" id="type0" value="0" checked>固定金額--%>
                            <input type="radio" name="commissionType" id="type1" value="1" checked>固定比例
                            <%--<input type="radio" name="packageType" id="type2" value="2">總經銷--%>
                        </td>
                        <th><label style="color: red;vertical-align: middle">*</label>固定佣金比例</th>
                        <td><input type="number" name="mainPercent" id="mainPercent" min="0" max="99999999">%</td>
                    </tr>
                    <%--<tr>--%>
                    <%--<th>固定佣金金額</th>--%>
                    <%--<td><input type="number" name="mainAmount" id="mainAmount" min="0" max="99999999"></td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<th>代收金額</th>--%>
                    <%--<td><input type="number" name="collectMoney" id="collectMoney" min="0" max="99999999"></td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<th>超過基本使用量抽成%數</th>--%>
                    <%--<td><input type="number" name="additionPercent" id="additionPercent" min="0" max="99999999" ></td>--%>
                    <%--</tr>--%>
                    <tr>
                        <th>狀態</th>
                        <td><p style="color: red"><span id="status"></span><p></td>
                    </tr>
                    <tr>
                        <table id="table-item" cellspacing="0" cellpadding="0" border="0" class="imglist">
                            <tr>
                                <th width="100px" style="vertical-align: middle; ">No</th>
                                <th width="180px">業務員姓名</th>
                                <th width="180px"  style="vertical-align: middle; ">業務員電話</th>
                                <th width="100px"  style="vertical-align: middle; ">業務員E-mail</th>
                                <th></th>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>
                                    <input type="hidden" name="itemNo" style="width: 100px;" maxlength="13" />
                                    <input type="hidden" id="dealerId" name="dealerId" />
                                    <input type="text" id="dealerName" name="dealerName" maxlength="100" style="width: 100px;"/>
                                </td>
                                <td>
                                    <input type="text" id="dealerPhone" name="dealerPhone" maxlength="50" style="width: 150px;"/>
                                </td>
                                <td>
                                    <input type="text" id="dealerEmail" name="dealerEmail" maxlength="100" style="width: 100px;"/>
                                </td>
                                <td>
                                    <input type="button" id="btnAddItem" class="addButton" value="+"/>
                                </td>
                            </tr>

                        </table>
                    </tr>
                    <tr>
                        <td align="center" colspan="4">
                            <input type="button" value="建立" id="send">
                            <input type="button" value="暫停" id="stop" style="display: none">
                            <input type="button" value="開啟" id="open" style="display: none">
                            <input type="button" value="關閉視窗" id="close" onclick="window.close()">
                            <input type="hidden" id="exist" name="exist">
                        </td>
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
            var dealerCompanyVO = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
//            $("#packageId").val(dealerCompanyVO.packageId);
//            $("#packageName").val(dealerCompanyVO.packageName);
//            $("#commissionPackageId").val(dealerCompanyVO.commissionPackageId);
            $('input[name="commissionType"][value="' + dealerCompanyVO.commissionType + '"]').prop('checked', true);
            $("#mainAmount").val(dealerCompanyVO.mainAmount);
            $("#mainPercent").val(dealerCompanyVO.mainPercent);
            $("#collectMoney").val(dealerCompanyVO.collectMoney);
            $("#additionPercent").val(dealerCompanyVO.additionPercent);
            if(dealerCompanyVO.commissionType != null){
                $("#commissionType").val(dealerCompanyVO.commissionType);
            }
            $("#dealerCompanyId").val(dealerCompanyVO.dealerCompanyId);
            $("#dealerCompanyName").val(dealerCompanyVO.dealerCompanyName);
            $("#businessNo").val(dealerCompanyVO.businessNo);
            $("#phone").val(dealerCompanyVO.phone);
            $("#fax").val(dealerCompanyVO.fax);
            $("#companyAddress").val(dealerCompanyVO.companyAddress);
            $("#email").val(dealerCompanyVO.email);

            status = dealerCompanyVO.status;
            var statusValue = '';
            switch(status){
                case 1:
                    statusValue = '生效中';
                    $("#stop").show();
                    break;
                case 2:
                    statusValue = '暫停';
                    $("#open").show();
                    break;
            }
            $("#status").text(statusValue);

//
//            if(dealerCompanyVO.companyId!=null){
//                var select =   $("#companyId");
//                select.show();
//                select.empty();
//                select.append($('<option></option>').val(dealerCompanyVO.companyId).html(dealerCompanyVO.companyName));
//                $("#companyId").val(dealerCompanyVO.companyId);
//            }



//            $("input[type=text] ").prop('disabled',true);
//            $("input[type=radio]").prop('disabled',true);
//            $("input[type=number]").prop('disabled',true);
            $("select").prop('disabled',true);
//            $("#send").hide();
            $("#exist").val("update");
            if(${!empty REQUEST_SEND_OBJECT_2}){
                $("#title").text('修改經銷商方案');
                $("#send").val('修改');

            }else{
                $("#title").text('瀏覽經銷商方案');
            }

        } else{
            $("#title").text('新增經銷商方案');
            $("#send").val('建立');
        }

        if (${!empty REQUEST_SEND_OBJECT_1}) {
            var dealerVOList = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
            var $tr =  $("#dealerId").closest('tr');
            $.each(dealerVOList, function (i, element) {
//                alert(element.dealerId+","+element.dealerName+","+element.dealerPhone+","+element.dealerEmail);
                if(i != 0){
                    $tr.find("#btnAddItem").click();
                    $tr = $tr.next();
                }
                $tr.find("[name=dealerId]").val(element.dealerId);
                $tr.find("[name=dealerName]").val(element.dealerName);
                $tr.find("[name=dealerPhone]").val(element.dealerPhone);
                $tr.find("[name=dealerEmail]").val(element.dealerEmail);

            });
        }


        $('tr').addClass('even');
        $('th').addClass('even');

        $(':radio[name="packageType"]').change(function () {
            var option = $(this).filter(':checked').val();

            if (option == '0') {
                $("#mainAmount").prop('disabled', false);
                $("#mainPercent").prop('disabled', true).val('');
                $("#collectMoney").prop('disabled', true).val('');
                $("#additionPercent").prop('disabled', false);
            } else if (option == '1') {
                $("#mainAmount").prop('disabled', true).val('');
                $("#mainPercent").prop('disabled', false);
                $("#collectMoney").prop('disabled', true).val('');
                $("#additionPercent").prop('disabled', false);
            } else if (option == '2') {
                $("#mainAmount").prop('disabled', true).val('');
                $("#mainPercent").prop('disabled', true).val('');
                $("#collectMoney").prop('disabled', false);
                $("#additionPercent").prop('disabled', true).val('');
            }
        }).change();




        $.validator.addMethod("typeValue0", function (value, element) {
            var flag = true;
            if ($("#type0").prop('checked')) {
                if ($("#mainAmount").val() == '') {
                    flag = false;
                }
            }
            return flag;
        }, "");

        $.validator.addMethod("typeValue1", function (value, element) {
            var flag = true;
            if ($("#type1").prop('checked')) {
                if ($("#mainPercent").val() == '') {
                    flag = false;
                }
            }
            return flag;
        }, "");

        $.validator.addMethod("typeValue2", function (value, element) {
            var flag = true;
            if ($("#type2").prop('checked')) {
                if ($("#collectMoney").val() == '') {
                    flag = false;
                }
            }
            return flag;
        }, "");


        $.validator.addMethod("typeValue3", function (value, element) {
            var flag = true;
            if ($("#type0").prop('checked') || $("#type1").prop('checked')) {
                if ($("#additionPercent").val() == '') {
                    flag = false;
                }
            }
            return flag;
        }, "");

        $("#dealerCompanyForm").validate({
            onkeyup: function (element) {
                $(element).valid()
            },
            errorClass: "my-error-class",
            rules: {
                dealerCompanyName: {required: true},
                mainAmount: {typeValue0: true},
                mainPercent: {typeValue1: true},
                collectMoney: {typeValue2: true},
                additionPercent:  {typeValue3: true}

            },
            ignore: ":hidden",
            messages: {
                dealerCompanyName: {required: "*請填寫公司!"},
                mainAmount: {typeValue0: "*請填寫金額!"},
                mainPercent: {typeValue1: "*請填寫比例!"},
                collectMoney: {typeValue2: "*請填寫金額!"},
                additionPercent:  {typeValue3: "*請填寫比例!(可填0)"}
            }
        });

    });

    $("#send").click(function () {
        if($("#dealerCompanyForm").valid()==true){
            $("select").prop('disabled',false);
            $("input[type=text] ").prop('disabled',false);
            $("input[type=radio]").prop('disabled',false);
            $("input[type=number]").prop('disabled',false);
            
            if($("#mainAmount").val()==""){
                $("#mainAmount").val("0");
            }
            if($("#mainPercent").val()==""){
                $("#mainPercent").val("0");
            }
            if($("#collectMoney").val()==""){
                $("#collectMoney").val("0");
            }
            if($("#additionPercent").val()==""){
                $("#additionPercent").val("0");
            }
            $("#dealerCompanyForm").ajaxSubmit(
                    {
                        beforeSubmit: function(){},
                        success: function(resp,st,xhr,$form) {
                            alert('成功!');
                            window.close();
                        },
                        error:function(xhr, ajaxOptions, thrownError){
                            alert('失敗!');
                            alert(xhr.status+'/'+thrownError);
                        }
                    });
        }
    });

    $("#stop").click(function(){
        $.ajax({
            url: "<%=request.getContextPath()%>/backendAdmin/commissionAjaxServlet?method=stop",
            dataType: 'json',
            data: {dealerCompanyId:$("#dealerCompanyId").val()},
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

    $("#open").click(function(){
        $.ajax({
            url: "<%=request.getContextPath()%>/backendAdmin/commissionAjaxServlet?method=open",
            dataType: 'json',
            data: {dealerCompanyId:$("#dealerCompanyId").val()},
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

//    $("#companyType").change(function(){
//        var companyType = $(this).val();
//        var select =   $("#companyId");
//        var url = path+"/backendAdmin/chargeAjaxServlet?method=getCompanyListByCpType";
//        $.getJSON(url,{companyType:companyType}, function( data ){
//            select.show();
//            select.empty();
//            select.append($('<option></option>').val('-1').html('請選擇'));
//            $.each(data,function(index,value){
//                select.append($('<option></option>').val(value.company_id).html(value.company_name));
//
//            })
//
//        })
//    })
    var count = 2;

    $('#btnAddItem').on('click', function () {
        var $tr = $(this).closest('tr');
        btnAddItemMethod($tr);
    });

    //更新商品序號
    function refreshNo() {
        $('#table-item tr').each(function (i, item) {
            if (i == 0)return;  //header row
            var $td = $(this).find('td').eq(0).text(i);
            var $focus = $(this).find('td').eq(1);
            $("input[type='text']:first", $focus).focus();
        });
    }

    function bindItemClone(field){
        var $tr = $(field).closest('tr');
        $tr.find('#btnAddItem').on('click', function () {
            var $tr = $(this).closest('tr');
            btnAddItemMethod($tr);
        });
    }

    function btnAddItemMethod($tr){
        var $clone = $tr.clone();
        $clone.find('[name=itemNo]').attr('id','clone_'+count);
        $clone.find('[name=dealerId]').val('');
        $clone.find('[name=dealerName]').val('');
        $clone.find('[name=dealerPhone]').val('');
        $clone.find('[name=dealerEmail]').val('');
        $("#btnAddItem").remove();
        $tr.after($clone);
        bindItemClone('#clone_'+count);
        refreshNo();
        count++;
    }

</script>