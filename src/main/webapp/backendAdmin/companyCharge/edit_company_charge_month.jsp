<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/company_charge.js"></script>
<script type="text/javascript">

	Date.prototype.defaultView = function(){
		var dd=this.getDate();
		if(dd<10)dd='0'+dd;
		var mm=this.getMonth()+1;
		if(mm<10)mm='0'+mm;
		var yyyy=this.getFullYear();
		return String(yyyy+"\/"+mm+"\/"+dd)
	}
</script>	
<form id="companyChargeCycleForm" name="companyChargeCycleForm" method="post"
      action="<%=request.getContextPath()%>/backendAdmin/companyChargeEditServlet?method=insert&type=1">
    <table cellspacing="0" cellpadding="0" border="0" class="imglist">
        <input type="hidden" id="companyId" name="companyId">
        <input type="hidden" id="type" name="type" value="1">

        <tr>
            <td colspan="4">
                <p style="color: brown">設定公司收費方式 月租型</p>
            </td>
        </tr>
        <tr>
            <td>目前公司名稱：</td>
            <td>
                <label style="color: darkred"><span id="companyName"></span></label>
                <input type="button" value="檢視公司基本資料" id="viewCompanyInfo"/>
            </td>
            <td>目前公司統編：</td>
            <td><label style="color: darkred"><span id="businessNo"></span></label></td>
        </tr>
        <tr>
            <td>收費方式：</td>
            <td colspan="1">
                <input type="radio" name="packageType" id="packageType1" value="1"/>月租型
                <input type="radio" name="packageType" id="packageType2" value="2"/>級距型
            </td>
            <td>是否為首次綁合約：</td>
            <td colspan="1">
                <input type="radio" name="isFirst" id="isFirst1" value="1"/>是
                <input type="radio" name="isFirst" id="isFirst2" value="2"/>否
            </td>
        </tr>
        <tr>
            <td>方案選擇：</td>
            <td colspan="3">
                <select id="chargeId" name="chargeId">
                </select>
                <input type="button" id="viewPackage" value="檢視方案">

                <div>綁約<span id="contractLimit"></span>月,免費<span id="freeMonth2"></span>月,<input type="hidden" id="chargeCycle"/>
                </div>
            </td>
        </tr>
        <tr>
            <td>加贈免費月份</td>
            <td><input type="number" id="freeMonth" name="freeMonth" value="0" min="0" onblur="countDayForEndDate()"/></td>
            <td>贈送金額</td>
            <td><input type="number" id="giftPrice" name="giftPrice" value="0" min="0"/></td>
        </tr>
        <input type="hidden" id="additionQuantity" name="additionQuantity" value="0" />
        <%--<tr>--%>
            <%--<td>加贈每月使用張數</td>--%>
            <%--<td colspan="3"><input type="number" id="additionQuantity" name="additionQuantity" value="0" min="0"/></td>--%>

        <%--</tr>--%>
        <tr>
            <td>實際起始日</td>
            <td>
                <input type="text" id="realStartDate" name="realStartDate" readonly/>
                <input type="hidden" id="oldRealEndDate" name="oldRealEndDate" readonly/>
            </td>
            <td>實際結束日</td>
            <td><input type="text" id="realEndDate" name="realEndDate" readonly/></td>
        </tr>
        <tr>
            <td>經銷公司</td>
            <td>
                <select id="dealerCompanyId" name="dealerCompanyId">
                    <option value="" selected>請選擇經銷公司</option>
                </select>
            </td>
            <td>經銷公司業務員</td>
            <td>
                <select id="dealerId" name="dealerId">
                    <option value="" selected>請選擇經銷公司業務</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>介紹公司</td>
            <td>
                <input type="text" id="brokerCp2" name="brokerCp2"/>
            </td>
            <td>介紹公司的介紹人</td>
            <td>
                <input type="text" id="broker2" name="broker2"/>
            </td>
        </tr>
        <tr>
            <td>裝機公司</td>
            <td>
                <input type="text" id="brokerCp3" name="brokerCp3"/>
            </td>
            <td>裝機公司的裝機人</td>
            <td>
                <input type="text" id="broker3" name="broker3"/>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <table width="100%" border="1" id="chargeHistory">
                    <tr>
                        <td colspan="13">歷史紀錄：</td>
                    </tr>
                    <tr>
                        <td>方案名稱</td>
                        <td>方案狀態</td>
                        <td>計算起始日</td>
                        <td>計算結束日</td>
                        <td>實際起始日</td>
                        <td>實際結束日</td>
                        <td>合約明細</td>
                        <td>終止合約(結清)</td>
                    </tr>

                </table>
            </td>
        </tr>
        <tr>
            <td colspan="4" align="center">
                <input type="button" value="確認新增" id="send">
                <input type="button" value="關閉" onclick="window.close()">
            </td>
        </tr>
        <tr>
            <td colspan="4">&nbsp;</td>
        </tr>

    </table>
</form>

<script type="text/javascript">

var path = '<%=request.getContextPath()%>';

$(function () {
    bindEvent();

    if (${!empty REQUEST_SEND_OBJECT_3}) {
        var historyList = $.parseJSON('${REQUEST_SEND_OBJECT_3}');
        $.each(historyList, function (i, element) {
        	alert(element.status+"    "+i)
            if(i == 0 ){
                $("#oldRealEndDate").val(element.real_end_date);
                //alert(element.real_end_date);
                //alert($("#oldRealEndDate").val());
            }
            if(element.status == '1' && i==0){
                $("#chargeHistory").append('<tr><td>' + element.package_name + '</td>' +
                        '<td>生效</td>'+
                        '<td>' + element.start_date + '</td><td>' + element.end_date + '</td>' +
                        '<td>' + element.real_start_date + '</td><td>' + element.real_end_date+
                        '<td><input type="button" value="檢視合約明細" onclick="viewChargeDetail(' + element.company_id + ',' + element.package_id + ','+ element.package_type +')"></td>'+
                        '<td><input type="button" value="終止合約(結清)" onclick="viewChargeDetail2(' + element.company_id + ','  + element.package_id + ','+ element.package_type +')"></td></tr>');
            } else if ( element.status == '0'  && i == 0){
                $("#chargeHistory").append('<tr><td>' + element.package_name + '</td>' +
                        '<td>未生效</td>'+
                        '<td>' + element.start_date + '</td><td>' + element.end_date + '</td>' +
                        '<td>' + element.real_start_date + '</td><td>' + element.real_end_date+
                        '<td><input type="button" value="檢視合約明細" onclick="viewChargeDetail(' + element.company_id + ',' + element.package_id + ','+ element.package_type +')"></td>'+
                        '<td><input type="button" value="終止合約(結清)" onclick="viewChargeDetail2(' + element.company_id + ','  + element.package_id + ','+ element.package_type +')"></td></tr>');
            } else if (element.status == '2' ){
                $("#chargeHistory").append('<tr><td>' + element.package_name + '</td>' +
                        '<td>作廢</td>'+
                        '<td>' + element.start_date + '</td><td>' + element.end_date + '</td>' +
                        '<td>' + element.real_start_date + '</td><td>' + element.real_end_date+
                        '<td><input type="button" value="檢視合約明細" onclick="viewChargeDetail(' + element.company_id + ',' + element.package_id + ','+ element.package_type +')"></td>'+
                        '<td></td></tr>');
            } else if (element.status == '0' ){
                $("#chargeHistory").append('<tr><td>' + element.package_name + '</td>' +
                        '<td>未生效</td>'+
                        '<td>' + element.start_date + '</td><td>' + element.end_date + '</td>' +
                        '<td>' + element.real_start_date + '</td><td>' + element.real_end_date+
                        '<td><input type="button" value="檢視合約明細" onclick="viewChargeDetail(' + element.company_id + ',' + element.package_id + ','+ element.package_type +')"></td>'+
                        '<td></td></tr>');
            } else if(element.status == '1'){
                $("#chargeHistory").append('<tr><td>' + element.package_name + '</td>' +
                        '<td>生效</td>'+
                        '<td>' + element.start_date + '</td><td>' + element.end_date + '</td>' +
                        '<td>' + element.real_start_date + '</td><td>' + element.real_end_date+
                        '<td><input type="button" value="檢視合約明細" onclick="viewChargeDetail(' + element.company_id + ',' + element.package_id + ','+ element.package_type +')"></td>'+
                        '<td></td></tr>');
            } else{
                $("#chargeHistory").append('<tr><td>' + element.package_name + '</td>' +
                        '<td></td>'+
                        '<td>' + element.start_date + '</td><td>' + element.end_date + '</td>' +
                        '<td>' + element.real_start_date + '</td><td>' + element.real_end_date+
                        '<td><input type="button" value="檢視合約明細" onclick="viewChargeDetail(' + element.company_id + ',' + element.package_id + ','+ element.package_type +')"></td>'+
                        '<td></td></tr>');
            }
        })
    }

    if (${!empty REQUEST_SEND_OBJECT_0}) {
        var companyMap = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
        $("#companyId").val(companyMap.company_id);
        $("#companyName").text(companyMap.company_name);
        $("#businessNo").text(companyMap.business_no);

    }

    //月租型方案
    var chargeCycleList;
    if (${!empty REQUEST_SEND_OBJECT_1}) {
        var chargeCycleList = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
        var mySelect = $('#chargeId');
        mySelect.append($('<option></option>').val("").html("請選擇"));
        $.each(chargeCycleList, function () {
            mySelect.append(
                    $('<option></option>').val(this.charge_id).html(this.package_name)
            );
        })
    }


    //級距型方案
    var chargeGradeList;
    if (${!empty REQUEST_SEND_OBJECT_2}) {
        var chargeGradeList = $.parseJSON('${REQUEST_SEND_OBJECT_2}');
//        var mySelect = $('#chargeId');
//        $.each(chargeGradeList, function () {
//            mySelect.append(
//                $('<option></option>').val(this.charge_id).html(this.package_name)
//            );
//        })
    }

    if (${!empty REQUEST_SEND_OBJECT_4}) {
        var company = $.parseJSON('${REQUEST_SEND_OBJECT_4}');
        var select = $('#dealerCompanyId');
        var options = select.prop('options');
        $.each(company, function () {
            select.append('<option value=' + this.dealerCompanyId + '>' + this.dealerCompanyName + '</option>');
        });
    }

    $('#viewPackage').prop( "disabled", true ); //畫面一開始時，不會選擇方案，所以"檢視方案"的按鈕應該disable。
    $("#chargeId").change(function () {
        var selChargeValue = $('#chargeId').val();
        if(selChargeValue == ""){
            $('#viewPackage').prop( "disabled", true );
        }else{
            $('#viewPackage').prop( "disabled", false );
        }
        var packageType =  $('input[name=packageType]:checked').val();
        var url = "<%=request.getContextPath()%>/backendAdmin/companyChargeAjaxServlet?method=getChargeInfo&packageType="+packageType;
        $.getJSON(url, {chargeId: $("#chargeId").val()}, function (data) {
            $("#contractLimit").text(data.contractLimit);
            $("#freeMonth2").text(data.freeMonth);
            $("#chargeCycle").val(data.chargeCycle);
            countDayForEndDate();
        })
    });

    $("#viewPackage").click(function () {
        var chargeId = $(this).parent('td').find('select').val();
        var packageType =  $('input[name=packageType]:checked').val();
        var url = path + '/backendAdmin/chargeEditServlet?method=read&chargeId=' + chargeId + '&type='+packageType;
        window.open(url, '', config = 'height=1000,width=800');
    });

    $("#realStartDate").datepicker({
        dateFormat: 'yy-mm-dd',
        changeMonth: true,
        changeYear: true,
        showOn: "button",
        yearRange: '2014:2034',
        minDate: new Date(2014, 10 - 1, 25),
        maxDate: '+20Y',
        onClose: function (selectedDate) {
            //$("#realEndDate").datepicker("option", "minDate", selectedDate);
            countDayForEndDate();
        }
    });
    if($("#oldRealEndDate").val()!=""){
        $("#realStartDate").datepicker("option", "minDate",addOneDay($("#oldRealEndDate").val()));
    }

    $("#realEndDate").datepicker({
        dateFormat: 'yy-mm-dd',
        changeMonth: true,
        changeYear: true,
        showOn: "button",
        buttonImage: path+"/backendAdmin/images/icon/icon_Cal.gif",
        buttonImageOnly: true,
        onClose: function (selectedDate) {
            //$("#realStartDate").datepicker("option", "maxDate", selectedDate);
        }
    });
    $("#realEndDate").prop("readonly", true).next("img").hide();

    $("#dealerCompanyId").change(function () {
        var url = "<%=request.getContextPath()%>/backendAdmin/companyChargeAjaxServlet?method=getDealerList";
        $.getJSON(url, {dealerCompanyId: $("#dealerCompanyId").val()}, function (data) {
            var select = $("#dealerId");
            select.empty();
            select.append($('<option></option>').val('').html('請選擇經銷公司業務'));
//            $.each(data,function(index,value){
//                select.append('<option value=' + value.dealer_id + '>' + value.dealer_name + '</option>');
//            })
            $.each(data, function () {
                select.append('<option value=' + this.dealer_id + '>' + this.dealer_name + '</option>');
            })

        })
    });


    $.validator.addMethod("countDay", function (value, element) {
        return countDay();
    }, "");


    $("#companyChargeCycleForm").validate({
        onkeyup: function (element) {
            $(element).valid()
        },
        errorClass: "my-error-class",
        rules: {
//            additionQuantity: {required: true},
            chargeId: {required: true},
            freeMonth: {required: true},
            realStartDate: {required: true},
            realEndDate: {required: true, countDay: true},
            isFirst: {required:true}

        },
        ignore: ":hidden",
        messages: {
//            additionQuantity: {required: "*請填寫!"},
            chargeId: {required: "*請選擇方案!"},
            freeMonth: {required: "*請填寫!"},
            realStartDate: {required: "*請選擇日期" },
            realEndDate: {required: "*請選擇日期", countDay: "*日期必須等於於綁約+免費月份"},
            isFirst: {required:"*請選擇是否為首次!"}
        }
    });

    $("#send").click(function () {
        if ($("#companyChargeCycleForm").valid() == true) {
            $("#send").prop('disabled', true);
            $("#companyChargeCycleForm").ajaxSubmit(
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
                            $("#send").prop('disabled', false);
                        }
                    });
        }
    });

    $("#freeMonth").click(function(){
        countDayForEndDate();
    });
    
    $("#packageType1").click(function () {
        //選擇「月租型」
        console.log("月租型");
        var mySelect = $('#chargeId');
        mySelect.find('option').remove();
        mySelect.append($('<option></option>').val("").html("請選擇"));
        $.each(chargeCycleList, function () {
            mySelect.append(
                $('<option></option>').val(this.charge_id).html(this.package_name)
            );
        })
    });

    $("#packageType2").click(function () {
        //選擇「級距型」
        console.log("級距型");
        var mySelect = $('#chargeId');
        mySelect.find('option').remove(); //.end();
        mySelect.append($('<option></option>').val("").html("請選擇"));
        $.each(chargeGradeList, function () {
            mySelect.append(
                $('<option></option>').val(this.charge_id).html(this.package_name)
            );
        })
    });

    //畫面預設先選擇「月租型」
    $("#packageType1").click();

});  //end of $(function () {


function countDay() {
    var start = new Date($("#realStartDate").val());
    var end = new Date($("#realEndDate").val());
    var contractLimit = parseInt($("#contractLimit").text());
    var freeMonth2 = parseInt($("#freeMonth2").text());
    var freeMonth = parseInt($("#freeMonth").val());
    var chargeCycle = $("#chargeCycle").val();

    var allAddMonth = contractLimit + freeMonth2 + freeMonth;
    if (chargeCycle == '2') {   //破月
        var startDate = start.getDate();
        if (parseInt(startDate) > 15) {
            allAddMonth += 1;
        }
    }
    start.setMonth(start.getMonth() + allAddMonth);
    start.setDate(start.getDate()-1);

    if(start.getTime() == end.getTime()){
        return true;
    }else{
        return false;
    }
//    if (start > end) {
//        return false;
//    } else {
//        return true;
//    }
}

    function countDayForEndDate(){
        var start = new Date($("#realStartDate").val());
        var end = new Date($("#realEndDate").val());
        var contractLimit = parseInt($("#contractLimit").text());
        var freeMonth2 = parseInt($("#freeMonth2").text());
        var freeMonth = parseInt($("#freeMonth").val());
        var chargeCycle = $("#chargeCycle").val();

        var allAddMonth = contractLimit + freeMonth2 + freeMonth;
        if (chargeCycle == '2') {   //破月
            var startDate = start.getDate();
            if (parseInt(startDate) > 15) {
                allAddMonth += 1;
            }
        }
        start.setMonth(start.getMonth() + allAddMonth);
        start.setDate(start.getDate()-1);
        $('#realEndDate').datepicker('setDate', start);
    }

    function addOneDay(strDate){
        var date = new Date(strDate);
        date.setDate(date.getDate() + 1);
        var dd = date.getDate();
        if(dd<10) dd= '0'+dd;
        var mm = date.getMonth() + 1;
        if(mm<10) dd = '0'+mm;
        var yyyy = date.getFullYear();
        var someFormattedDate = yyyy + '-'+ mm + '-'+ dd;
        return someFormattedDate;
    }



function viewChargeDetail2(company_id, package_id, type){
    //var type = $("#type").val();
    var url = path+"/backendAdmin/companyChargeEditServlet?method=settleView&&companyId="+company_id+"&packageId="+package_id+"&type="+type;
    window.open(url, '', config = 'width=800');
}

</script>
