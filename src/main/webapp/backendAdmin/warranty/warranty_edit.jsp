
<%
    String root = request.getContextPath() + "/backendAdmin";
%>
<style type="text/css">
    .ui-autocomplete {
        max-height: 200px;
        overflow-y: auto;
        /* prevent horizontal scrollbar */
        overflow-x: hidden;
    }
</style>

<form action="<%=request.getContextPath()%>/backendAdmin/warrantyEditServlet?method=update" method="post" id="warrantyForm">
    <input type="hidden" id="warrantyId" name="warrantyId"/>
    <table cellspacing="0" cellpadding="0" border="0"  class="imglist">
        <tr>
            <td colspan="2"><p style="color: brown">修改發票機保固資料</p></td>
        </tr>
        <tr>
            <th>發票機序號</th>
            <td><input type="text" name="warrantyNo" id="warrantyNo"/></td>
        </tr>
        <tr>
            <th>用戶名稱</th>
            <td>
                <input type="text" id="userCompany" name="userCompany" size="50"/><input type="button" value="..." id="allUserCompany">
                <input type="hidden" id="companyId" name="companyId" />
            </td>
        </tr>
        <tr>
            <th>經銷商名稱</th>
            <td>
                <input type="text" id="userDealerCompany" name="userDealerCompany" size="50"/><input type="button" value="..." id="allUserDealerCompany">
                <input type="hidden" id="dealerCompanyId" name="dealerCompanyId" />
            </td>
        </tr>
        <tr>
            <th>出貨狀態</th>
            <td>
                <input type="checkbox" name="onlyShip" id="onlyShip" value="1" >僅出貨
            </td>
        </tr>
        <tr>
            <th>是否延長保固</th>
            <td>
                <input type="radio" id="extendF" name="extend" value="false" onclick="countDayForEndDate()" checked/>不延長保固 &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" id="extendY" name="extend" value="true" onclick="countDayForEndDate()"/>延長保固
            </td>
        </tr>
        <tr>
            <th>保固起日</th>
            <td>
                <input type="text" name="startDate" id="startDate" readonly/>
                <input type="checkbox" onclick="getNowDate()">取得現在日期
            </td>
        </tr>
        <tr>
            <th>保固迄日</th>
            <td><input type="text" name="endDate" id="endDate" readonly/></td>
        </tr>
        <tr>
            <th>型號</th>
            <td><input type="text" name="model" id="model"/></td>
        </tr>
        <tr>
            <th>備註</th>
            <td><input type="text" name="note" id="note"/></td>
        </tr>
        <tr>
            <th>狀態</th>
            <td>
                <select name="status" id="status">
                    <option value="1">使用中</option>
                    <option value="2">不使用</option>
                    <option value="3">退回</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>&nbsp;&nbsp;注意事項：</td>
            <td><label style="color: red;vertical-align: middle">有*者，則為必填欄位。</label></td>
        </tr>
        <tr>
            <td colspan="4" align="center">
                <input type="button" id="send" value="確認">
                <button id="close" onclick="window.close();">關閉</button>
            </td>
        </tr>
    </table>
    <table cellspacing="0" cellpadding="0" border="0"  class="imglist">
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

<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.form.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/linq.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
<script type="text/javascript">
    $("#startDate").datepicker({
        dateFormat: 'yy-mm-dd',
        changeMonth: true,
        changeYear: true,
        showOn: "button",
        onClose: function (selectedDate) {
            countDayForEndDate();
        }
    });
    $("#endDate").datepicker({
        dateFormat: 'yy-mm-dd',
        changeMonth: true,
        changeYear: true,
        showOn: "button",
        onClose: function (selectedDate) {
        }
    });

    //------------------------------------------------------------------------------------------------------------------

    //0.用戶清單
    var cpData = [];
    var cpList;
    if (${!empty REQUEST_SEND_OBJECT_0}) {
        cpList = $.parseJSON('${REQUEST_SEND_OBJECT_0}');

        cpData =
                Enumerable.From(cpList)
                        .Select(function(o) { return {
                            label: o.business_no +"("+o.name+")",
                            value: o.business_no +"("+o.name+")",
                        };}).ToArray();
    };

    $("#userCompany").autocomplete({
        source: cpData,
        minLength:0,
        select: function(event, ui) {
            for(var memberIndex in cpList){
                if((cpList[memberIndex].business_no+"("+cpList[memberIndex].name+")") == ui.item.value){
                    $("#companyId").val(cpList[memberIndex].company_id);
                }
            }
        }
    });

    $("#userCompany").blur(function(){
        if($("#userCompany").val()==""){
            $("#companyId").val("");
        }
    });

    $("#allUserCompany").on("click",function(){
        $("#userCompany").autocomplete("search", "");
    });

    //1.經銷商清單
    var cpDealerData = [];
    var cpDealerList;
    if (${!empty REQUEST_SEND_OBJECT_1}) {
        cpDealerList = $.parseJSON('${REQUEST_SEND_OBJECT_1}');

        cpDealerData =
            Enumerable.From(cpDealerList)
                .Select(function(o) { return {
                    label: o.business_no +"("+o.dealer_company_name+")",
                    value: o.business_no +"("+o.dealer_company_name+")",
                };}).ToArray();
    };

    $("#userDealerCompany").autocomplete({
        source: cpDealerData,
        minLength:0,
        select: function(event, ui) {
            for(var memberIndex in cpDealerList){
                if((cpDealerList[memberIndex].business_no+"("+cpDealerList[memberIndex].dealer_company_name+")") == ui.item.value){
                    $("#dealerCompanyId").val(cpDealerList[memberIndex].dealer_company_id);
                }
            }
        }
    });

    $("#userDealerCompany").blur(function(){
        if($("#userDealerCompany").val()==""){
            $("#dealerCompanyId").val("");
        }
    });

    $("#allUserDealerCompany").on("click",function(){
        $("#userDealerCompany").autocomplete("search", "");
    });

    //------------------------------------------------------------------------------------------------------------------

    function countDayForEndDate(){
        var start = new Date($("#startDate").val());
        var allAddMonth = 0;
        var isExtend = $('input[name=extend]:checked').val();
        if(isExtend == "false" ){
            allAddMonth = 12;
        }else{
            allAddMonth = 36;
        }
        start.setMonth(start.getMonth() + allAddMonth);
        start.setDate(start.getDate()-1);
        $('#endDate').datepicker('setDate', start);
    }

    function getNowDate(){
        var today = new Date();
        var dd = today.getDate();

        var mm = today.getMonth()+1;
        var yyyy = today.getFullYear();
        if(dd<10)
        {
            dd='0'+dd;
        }

        if(mm<10)
        {
            mm='0'+mm;
        }
        $("#startDate").val(yyyy+"-"+mm+"-"+dd);
        countDayForEndDate();
    }

    /**
     *  完成，送出表單
     */
    $("#send").click(function () {
//        if ($("#warrantyForm").valid() == true) {
            $("#warrantyForm").ajaxSubmit(
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
//        }
    });


    if (${!empty REQUEST_SEND_OBJECT_2}) {
        var warrantyVO = $.parseJSON('${REQUEST_SEND_OBJECT_2}');
        $("#warrantyId").val(warrantyVO.warrantyId);
        $("#warrantyNo").val(warrantyVO.warrantyNo);
        $("#companyId").val(warrantyVO.companyId);
        for(var memberIndex in cpList){
            if(warrantyVO.companyId == cpList[memberIndex].company_id){
                $("#userCompany").val(cpList[memberIndex].business_no+"("+cpList[memberIndex].name+")");
            }
        }
        $("#dealerCompanyId").val(warrantyVO.dealerCompanyId);
        console.log("warrantyVO.dealerCompanyId="+warrantyVO.dealerCompanyId)
        for(var memberIndex in cpDealerList){
            if(warrantyVO.dealerCompanyId == cpDealerList[memberIndex].dealer_company_id){
                $("#userDealerCompany").val(cpDealerList[memberIndex].business_no+"("+cpDealerList[memberIndex].dealer_company_name+")");
            }
        }
        if(warrantyVO.onlyShip == "1"){
            console.log("warrantyVO.onlyShip ="+warrantyVO.onlyShip);
            $("#onlyShip").prop('checked','true');
        }

        if(warrantyVO.extend == true){
            $("#extendY").prop('checked','true');
            $("#extendN").prop('checked','false');
        }

        $("#startDate").val(warrantyVO.startDate.substring(0, 10).replace(/\//g,"-"));
        $("#endDate").val(warrantyVO.endDate.substring(0, 10).replace(/\//g,"-"));
        $("#model").val(warrantyVO.model);
        $("#note").val(warrantyVO.note);
        $("#status").val(warrantyVO.status);
        $("#createDate").text(warrantyVO.createDate);
        $("#modifyDate").text(warrantyVO.modifyDate);
        $("#creator").text(warrantyVO.creator);
        $("#modifier").text(warrantyVO.modifier);
    }

</script>

