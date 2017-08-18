

<%
    String root = request.getContextPath() + "/backendAdmin";
%>

<script type="text/javascript" src="<%=root %>/js/checkBusinessNo.js"></script>



<form action="<%=request.getContextPath()%>/backendAdmin/companyEditServlet?method=insert" method="post" id="companyForm">
    <input type="hidden" id="companyId" name="companyId"/>
    <input type="hidden" id="parentId" name="parentId"/>


    <table cellspacing="0" cellpadding="0" border="0" class="imglist">
        <tr>
            <td colspan="4"><p style="color: brown">修改公司基本資料</p></td>
        </tr>
        <tr>
            <th style="width: 130px"><label style="color: red;vertical-align: middle">*</label>公司統編：</th>
            <td style="width:300px"><input type="text" id="businessNo" name="businessNo" /></td>
            <th style="width: 130px"><label style="color: red;vertical-align: middle">*</label>公司名稱：</th>
            <td style="width:300px"><input type="text" id="name" name="name"/></td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>公司狀態：</th>
            <td>
                <select name="verifyStatus" id="verifyStatus">
                    <option value="1">註冊</option>
                    <option value="2">已審核</option>
                    <option value="3">使用中</option>
                    <option value="9">停權</option>
                </select>
            </td>
            <th><label style="color: red;vertical-align: middle">*</label>所在縣市別：</th>
            <td>
                <select name="cityId" id="cityId">

                </select>
            </td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>公司類型：</th>
            <td>
                <select name="companyType" id="companyType">
                    <option value="1">一般公司</option>
                    <option value="2">會計公司</option>
                    <option value="3">POS公司</option>
                </select>
            </td>
            <th>&nbsp;&nbsp;公司代號：</th>
            <td><input type="text" name="codeName" id="codeName"/></td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>公司地址：</th>
            <td colspan="3" ><input type="text" name="companyAddress" id="companyAddress" style="width:350px;"/></td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>公司(帳單)通訊地址：</th>
            <td colspan="3">
                <input type="text" name="mailingAddress" id="mailingAddress"  style="width:350px;"/>&nbsp;
                <input name="checkAddress" id="checkAddress" type="checkbox"/>同上
            </td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>公司電話：</th>
            <td><input type="text" name="phone" id="phone"/></td>
            <th>&nbsp;&nbsp;公司傳真：</th>
            <td><input type="text" name="fax" id="fax"/></td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>聯絡人(帳單聯絡人)一：</th>
            <td><input type="text" name="contact1" id="contact1"/></td>
            <th><label style="color: red;vertical-align: middle">*</label>連絡人(帳單聯絡人)一電話：</th>
            <td><input type="text" name="contactPhone1" id="contactPhone1"/></td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>聯絡人(帳單聯絡人)一Email：</th>
            <td colspan="3"><input type="text" id="email1" name="email1"/></td>
        </tr>
        <tr>
            <th>&nbsp;&nbsp;聯絡人二：</th>
            <td><input type="text" name="contact2" id="contact2"/></td>
            <th>&nbsp;&nbsp;連絡人二電話：</th>
            <td><input type="text" name="contactPhone2" id="contactPhone2"/></td>
        </tr>
        <tr>
            <th>&nbsp;&nbsp;聯絡人二Email：</th>
            <td colspan="3"><input type="text" id="email2" name="email2"/></td>
        </tr>
        <tr class="normalCompany">
            <th><label style="color: red;vertical-align: middle">*</label>稅籍編號：</th>
            <td><input type="text" name="taxNo" id="taxNo" maxlength="9"/></td>
            <th>&nbsp;&nbsp;稽徵機關：</th>
            <td><input type="text" name="taxOffice" id="taxOffice"/></td>
        </tr>
        <tr>
            <th>&nbsp;&nbsp;發票廣告：</th>
            <td><input type="text" name="topBanner" id="topBanner" maxlength="15"/></td>
            <th>&nbsp;&nbsp;明細廣告：</th>
            <td><input type="text" name="bottomBanner" id="bottomBanner" maxlength="15"/></td>
        </tr>
        <tr>
            <th><label style="color: red;vertical-align: middle">*</label>傳輸方式：</th>
            <td colspan="3">
                <select name="transferType" id="transferType">
                    <option value="1">逐筆</option>
                    <option value="2">批次</option>
                    <option value="3">Trunkey格式</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>&nbsp;&nbsp;注意事項：</td>
            <td colspan="3"><label style="color: red;vertical-align: middle">有*者，則為必填欄位。</label></td>
        </tr>



        <tr>
            <td colspan="4" align="center">
                <input type="button" id="send" value="確認修改">
                <button id="close">關閉</button>
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

    $(function () {

        $('tr').addClass('even');
        $('th').addClass('even');

        var cities = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
        var select = $('#cityId');
        var options = select.prop('options');
        $.each(cities, function () {
            options[options.length] = new Option(this.name, this.zip);
        });

        if (${!empty REQUEST_SEND_OBJECT_2}) {
            var read = $.parseJSON('${REQUEST_SEND_OBJECT_2}');
            if(read=='read'){
                var $allInput = $(".imglist input,select");
                $allInput.prop('disabled',true);
                $("#send").hide();
            }
        }


        if (${!empty REQUEST_SEND_OBJECT_1}) {
            var companyVO = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
//            var companyVO = $.parseJSON(str.replace(/\\\"/, '\\"'));
            $("#parentId").val(companyVO.parentId);
            $("#companyId").val(companyVO.companyId);
            if(companyVO.businessNo!=null){
                $("#businessNo").val(companyVO.businessNo);
                $('#businessNo').prop('readonly', true);
            }

            $("#verifyStatus").val(companyVO.verifyStatus);
            $("#name").val(companyVO.name);
            $("#companyType").val(companyVO.companyType);
            $("#cityId").val(companyVO.cityId);
            $("#codeName").val(companyVO.codeName);
            $("#companyAddress").val(companyVO.companyAddress);
            $("#mailingAddress").val(companyVO.mailingAddress);
            $("#phone").val(companyVO.phone);
            $("#fax").val(companyVO.fax);
            $("#contact1").val(companyVO.contact1);
            $("#contactPhone1").val(companyVO.contactPhone1);
            $("#contact2").val(companyVO.contact2);
            $("#contactPhone2").val(companyVO.contactPhone2);
            $("#taxNo").val(companyVO.taxNo);
            $("#taxOffice").val(companyVO.taxOffice);
            $("#transferType").val(companyVO.transferType);
            $("#email1").val(companyVO.email1);
            $("#email2").val(companyVO.email2);
            $("#createDate").text(companyVO.createDate);
            $("#modifyDate").text(companyVO.modifyDate);
            $("#creator").text(companyVO.creator);
            $("#modifier").text(companyVO.modifier);
            $("#topBanner").val(companyVO.topBanner);
            $("#bottomBanner").val(companyVO.bottomBanner);
        }

        $.validator.addMethod("checkBusinessNo", function (value, element) {
            return isValidGUI(value);
        }, "");

        $.validator.addMethod("taxNo", function (value, element) {
            return $("#taxNo").val().length==9;
        }, "");

        $.validator.addMethod("repeatBusinessNo", function (value, element) {
                var flag = 1;
            if($("#businessNo").val().length>0){
                $.ajax({
                    url: "<%=request.getContextPath()%>/backendAdmin/otherAjaxServlet?method=companyCheckBusinessNo",
                    dataType: 'json',
                    data: {businessNo: $("#businessNo").val(),companyId:$("#companyId").val()},
                    async: false,
                    success: function (data) {
                        if (data != '0') {
                            flag = 0;
                        }
                    }
                });
            }

                if (flag == 0) {
                    return false;
                } else {
                    return true;
                }

        }, "");


        $("#companyForm").validate({
            onkeyup: function(element) {$(element).valid()},
            errorClass: "my-error-class",
            rules: {
                businessNo: {
                    required: true,
                    checkBusinessNo: true,
                    repeatBusinessNo: true
                },
                name: {required: true},
                phone: {required: true},
                contact1: {required: true},
                contactPhone1 : {required: true},
                email1 : {required: true,email:true},
                companyAddress: {required: true},
                mailingAddress: {required: true},
                taxNo:{taxNo:true}

            },
            ignore: ":hidden",
            messages: {
                businessNo: {required: "*請填寫統一編號!", checkBusinessNo: "*請輸入正確的統一編號!", repeatBusinessNo: "*統一編號重複!"},
                name: {required: "*請填寫公司名稱!"},
                phone: {required: "*請填寫公司電話!"},
                contact1: {required: "*請填寫公司聯絡人!"},
                contactPhone1: {required: "*請填寫公司聯絡人電話!"},
                email1: {required: "*請填寫公司聯絡人email!",email:"*請填寫正確格式!"},
                companyAddress: {required: "*請填寫公司地址!"},
                mailingAddress: {required: "*請填寫通訊地址!"},
                taxNo:{taxNo:"*長度需為9位數"}
            }


    });

        $("#send").click(function () {
            if($("#companyForm").valid()==true){
                $("#companyForm").ajaxSubmit(
                        {
                            beforeSubmit: function(){},
                            success: function(resp,st,xhr,$form) {

                                alert('修改成功!');
                                window.close();
                            },
                            error:function(xhr, ajaxOptions, thrownError){
                                alert('修改失敗!');
                                alert(xhr.status+'/'+thrownError);
                            }
                        });
            }

        });

        $("#close").click(function () {
            window.close();

        });



        $("#checkAddress").click(function () {
            $("#mailingAddress").val($("#companyAddress").val());
        });
    });
</script>