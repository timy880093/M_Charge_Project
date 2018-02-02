<style type="text/css">
    .ui-autocomplete {
        max-height: 200px;
        overflow-y: auto;
        /* prevent horizontal scrollbar */
        overflow-x: hidden;
    }
</style>

<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.form.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/linq.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
<tr>
    <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td align="left"><span class="textstyle_06">月租超額計算</span></td>
                <td align="left">
                <span class="select_div">
                    <form action="<%=request.getContextPath()%>/backendAdmin/calCycleSearchServlet?method=" method="post" id="calCycleForm" name="calCycleForm">
                        計算年月
                        <select name="calYM" id="calYM" class="forms_Dropdown"></select>
                           <%--付款狀態--%>
                        <%--<select name="payStatus" id="payStatus" class="forms_Dropdown">--%>
                            <%--<option value="all">全部</option>--%>
                            <%--<option value="unpay">未繳費</option>--%>
                            <%--<option value="paid">已繳費</option>--%>
                        <%--</select>--%>
                        <%--生效狀態--%>
                        <%--<select name="Status" id="Status" class="forms_Dropdown">--%>
                            <%--<option value="all">全部</option>--%>
                            <%--<option value="effective">生效</option>--%>
                            <%--<option value="void">作廢</option>--%>
                        <%--</select>--%>
                        用戶名稱
                        <input type="text" id="userCompany" name="userCompany" /><input type="button" value="..." id="allUserCompany">
                        <input type="hidden" id="userCompanyId" name="userCompanyId" />
                        <a href="#" id="search" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
                        <select name="runItem" id="runItem" class="forms_Dropdown">
                            <option value="calOverYM">計算超額(批次,請選擇計算年月)</option>
                            <option value="calOver">計算超額(請選擇要計算哪幾筆)</option>
                             <%--<option value="calOverPeriod">計算超額上期(請選擇月份)</option>--%>
                            <option value="calOverToCash">計算超額且出帳(請選擇年月、用戶、要計算哪幾筆)</option>
                            <option value="emailYM">寄E-mail(批次,請選擇計算年月)</option>
                            <option value="email">寄E-Mail(請選擇哪幾筆要寄E-Mail)</option>
                        </select>
                        <a href="#" id="run" title="執行" class="butbox"><span>執行</span></a>
                    </form>
                    <div style="display: block; color:red; ">請先作完${REQUEST_SEND_OBJECT_3}前的快到期合約續約後，再作${REQUEST_SEND_OBJECT_2}的超額計算，不然出帳後會無法匯出上銀Excel。(因為續約前的超額已先被計算了)</div>
                </span>

                </td>
            </tr>
        </table>
    </td>
</tr>


<tr>
    <td>
        <div class="content" title="主要區域" style=" width:100%; ">
            <div class="spacer"></div>
            <table id="jqgrid" width="95%" border="0" cellspacing="0" cellpadding="0">
            </table>
            <div id="gridPager"></div>
        </div>
    </td>
</tr>


<script type="text/javascript">
    var path = '<%=request.getContextPath()%>';

    $(document).ready(function () {
        $(window).bind('resize', function () {
            $("#jqgrid").setGridWidth($(window).width() - 300);
        }).trigger('resize');

        //頁面預設載入時，撈的資料條件如下
        var sdata = searchCriteria();

        var myGrid = jQuery("#jqgrid").jqGrid({
            url: '<%=request.getContextPath()%>/backendAdmin/calCycleSearchServlet?method=search',
            datatype: "json",
            postData: sdata,
            colNames: ['bill_id', 'company_id', '公司名稱', '計算年月', '月租金額','當張','基張','贈送張數','超限(張)','發票扣點單位','超限應繳','繳款上限','超額應繳金額','超額是否計費','超額是否繳費','月租應繳金額','月租是否繳費','狀態','功能','bill_id','package_id','bill_type'
            ],
            colModel: [
                {name: 'bill_id', index: 'bill_id', hidden: true, key: true, hidedlg: true},
                {name: 'company_id', index: 'company_id', hidden: true, hidedlg: true},
                {name: 'name', index: 'name'},
                {name: 'year_month', index: 'year_month'},
                {name: 'price', index: 'price'},
                {name: 'cnt', index: 'cnt'},
                {name: 'cnt_limit', index: 'cnt_limit'},
                {name: 'cnt_gift', index: 'cnt_gift'},
                {name: 'cnt_over', index: 'cnt_over'},
                {name: 'single_price', index: 'single_price'},
                {name: 'price_over', index: 'price_over'},
                {name: 'price_max', index: 'price_max'},
                {name: 'pay_over', index: 'pay_over'},
                {name: 'cash_out_over_id', index: 'cash_out_over_id', formatter: formatCashOutOverId},
                {name: 'cash_in_over_id', index: 'cash_in_over_id', formatter: formatCashInOverId},
                {name: 'pay_month', index: 'pay_month'},
                {name: 'cash_in_month_id', index: 'cash_in_month_id', formatter: formatCashInOverId},
                {name: 'bc_status', index: 'bc_status', formatter: formatStatus},
                {name: 'bill_id', index: 'bill_id', formatter: formatSetting, width: '350px', align: 'center'},
                {name: 'bill_id', index: 'bill_id', hidden: true, key: true, hidedlg: true},
                {name: 'package_id', index: 'package_id', hidden: true, key: true, hidedlg: true},
                {name: 'bill_type', index: 'bill_type', hidden: true, key: true, hidedlg: true}
            ],
            rowNum: 100,
            rowList: [100, 200, 300],
            pager: '#gridPager',
            sortname: 'company_id',
            sortorder: "desc",
            onPaging: function (id) {
                $("#jqgrid").jqGrid("setGridParam", {
                    search: true
                });
            },
            onSortCol: function (index, columnIndex, sortOrder) {
                $("#jqgrid").jqGrid("setGridParam", {
                    search: true
                });
            },
            loadComplete: function () {

                //查詢後如無資料，請show"查無資料"的訊息。
                var ids =jQuery("#jqgrid").jqGrid('getDataIDs');
                if(ids.length == 0){
                    alert("查無資料");
                }

                $(".butbox2").click(function () {
                    var selectOption = $(this).prev().val();
                    var billId = $(this).parent('td').parent('tr').find('td:eq(2)').text();
                    var companyId = $(this).parent('td').parent('tr').find('td:eq(3)').text();
                    var packageId = $(this).parent('td').parent('tr').find('td:eq(22)').text();
                    var billType =  $(this).parent('td').parent('tr').find('td:eq(23)').text();
                    //alert("billId = " + billId + " selectOption= "+selectOption + "  packageId = "+ packageId);
                    var url = '';
                    if (selectOption == 'editGift') {
                        url = path +'/backendAdmin/calCycleEditServlet?method=editGift&billId=' + billId;
                        MM_openBrWindow(url,500,500);
                    }else if(selectOption == 'viewPackage') {
                        var url = '';
                        url = '<%=request.getContextPath()%>/backendAdmin/companyChargeEditServlet?method=read&companyId='+companyId+'&packageId='+packageId+'&type='+billType;
                        MM_openBrWindow(url,1200,800);
                    }
                });
            },
        });

        $(document).ajaxStart(function () {
            $.blockUI();
        });

        $(document).ajaxStop(function () {
            $.unblockUI();
        });
    });

    if (${!empty REQUEST_SEND_OBJECT_0}) {
        //畫面的年月下拉選單
        var select =   $("#calYM");
        var data = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            select.show();
            select.empty();
            select.append(($('<option></option>').val("").html("請選擇")));
            $.each(data,function(index,value){
                select.append($('<option></option>').val(value.calYear + value.calMonth).html(value.calYear + value.calMonth));
            })
    }


    if (${!empty REQUEST_SEND_OBJECT_0}) {
        //畫面的年月下拉選單
        var select =   $("#cash_in_month_id");
        var data = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
        select.show();
        select.empty();
        select.append(($('<option>').val("").html("請選擇")));
        $.each(data,function(index,value){
            select.append($('<option>').val(value.cash_in_month_id).html(value.cash_in_month_id));
        })
    }

    var cpData = [];
    var cpList;
    if (${!empty REQUEST_SEND_OBJECT_1}) {
        cpList = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
        cpData =
                Enumerable.From(cpList)
                        .Select(function(o) { return {
                            label: o.business_no +"("+o.name+")",
                            value: o.business_no +"("+o.name+")",
                        };}).ToArray();

    };

    if (${!empty REQUEST_SEND_OBJECT_2}) {
        defaultYm = $.parseJSON('${REQUEST_SEND_OBJECT_2}');
        $("#calYM").val(defaultYm);
    };

    $("#userCompany").autocomplete({
        source: cpData,
        minLength:0,
        select: function(event, ui) {
            for(var memberIndex in cpList){
                if((cpList[memberIndex].business_no+"("+cpList[memberIndex].name+")") == ui.item.value){
                    $("#userCompanyId").val(cpList[memberIndex].company_id);
                }
            }
        }
    });
    $("#userCompany").blur(function(){
        if($("#userCompany").val()==""){
            $("#userCompanyId").val("");
        }
    });

    $("#allUserCompany").on("click",function(){
        $("#userCompany").autocomplete("search", "");
    });



    function formatSetting(cellvalue, options, rowObject) {
        var value = cellvalue;
//        var is_validated = rowObject['is_validated'];
        var str = '';

        str = '<select name="fieldName" class="forms_Dropdown">' +
                '<option value="editGift">修改贈送點數</option>' +
                '<option value="viewPackage">檢視合約明細</option>' ;
        str += '</select>';
        str += '<a href="#"  title="確定" class="butbox2"><span>確定</span></a>';

        return str;

    }

    function formatCashOutOverId(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';

        if(value == undefined){
            str = '未計費';
        }else{
            str = '已計費';
        }

        return str;
    }

    function formatCashInOverId(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';
        if(value == undefined){
            str = '未繳費';
        }else{
            str = '已繳費';
        }
        return str;
    }

    function formatStatus(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';

        switch(value) {
            case '1':
                str = '生效';
                break;
            case '2':
                str = '作廢';
                break;
            default:
               str = '';
        }

        return str;
    }

    function formatSetting2(cellvalue, options, rowObject) {
        var value = cellvalue;
        var gift_date = rowObject['gift_date'];
        var str = '';
        str += gift_date.substr(0,7);
        return str;
    }


    function billType(cellvalue, options, rowObject) {
        
        var value = cellvalue;
        //var str = value.toString();
        if(typeof value == "undefined"){
        		value = "";
        } else {

	        if (value == 1) {
	        		value = '月租';
	        } else if(value == 2) {
	        		value = '預付';
	        } 
        }
        return value;
    }

    function verify(cellvalue, options, rowObject) {
        var value = cellvalue;
        var str = value;
        if (value == '1') {
            str = '是';
        } else {
            str = '否';
        }
        return str;
    }

    $("#search").click(function () {
        var sdata = searchCriteria();
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1, search: true
        }).trigger("reloadGrid");
    });

    function searchCriteria(){
        var calYM = $("#calYM").val();
        var userCompanyId = $("#userCompanyId").val();
//        var payStatus = $("#payStatus").val();
//        var status = $("#status").val();
        var sdata = setSearchValue("calYM", encodeURIComponent(calYM), "userCompanyId", encodeURIComponent(userCompanyId));
        return sdata;
    }

    function calOverYM(){
        var calYM = $("#calYM").val();
        if(calYM == ""){
            alert("請先選擇計算年月");
            return false;
        }


//        //callback handler for form submit
//        $("#calCycleForm").submit(function(e)
//        {
//            var postData = $(this).serializeArray();
//            var formURL = $(this).attr("action") + "calOverYM";
//            $.ajax(
//                    {
//                        url : formURL,
//                        type: "POST",
//                        data : postData,
//                        success:function(data, textStatus, jqXHR)
//                        {
//                            //data: return data from server
//                            alert("ok");
//                        },
//                        error: function(jqXHR, textStatus, errorThrown)
//                        {
//                            //if fails
//                        }
//                    });
//            e.preventDefault(); //STOP default action
//            e.unbind(); //unbind. to stop multiple form submit.
//        });
//        $.blockUI();
//        $("#calCycleForm").submit(); //Submit  the FORM
//        $.unblockUI();

        var url = path + "/backendAdmin/calCycleSearchServlet?method=calOverYM&calYM=" + calYM;
        $.getJSON(url, {calYM: calYM}, function (data) {
            //alert(data);
            alert(data);
        });
    }

    function calOver(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var calOverAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var calOver = new Object();
                calOver.billId = row.bill_id;
                calOverAry[i] = calOver;
            }
            var calOverJson = JSON.stringify(calOverAry);
            //alert(calOverJson);
            var url = path + "/backendAdmin/calCycleSearchServlet?method=calOver&calOverAry=" + calOverJson;
            $.getJSON(url, {calOverAry: calOverAry}, function (data) {
                //alert(data);
                alert("ok!");
            })
        } else {
            alert("請先勾選資料列。");
        }
    }

    function calOverToCash(){
        var calYM = $("#calYM").val();
        if(calYM == ""){
            alert("請先選擇計算年月");
            return false;
        }
        var userCompanyId = $("#userCompanyId").val();
        if(userCompanyId == ""){
            alert("請先選擇用戶名稱");
            return false;
        }
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var calOverAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var calOver = new Object();
                calOver.billId = row.bill_id;
                calOverAry[i] = calOver;
            }
            var calOverJson = JSON.stringify(calOverAry);
            //alert(calOverJson);
            var url = path + "/backendAdmin/calCycleSearchServlet?method=calOverToCash&calYM="+calYM+"&userCompanyId="+userCompanyId+"&calOverAry=" + calOverJson;
            $.getJSON(url, {calOverAry: calOverAry}, function (data) {
                //alert(data);
                alert(data);
            })
        } else {
            alert("請先勾選資料列。");
        }
    }

    function emailYM(){
        var calYM = $("#calYM").val();
        if(calYM == ""){
            alert("請先選擇計算年月");
            return false;
        }
        var url = path + "/backendAdmin/calCycleSearchServlet?method=emailYM&calYM=" + calYM;
        $.getJSON(url, {calYM: calYM}, function (data) {
            alert(data);
        })
    }

    function email(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var calOverAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var calOver = new Object();
                calOver.billId = row.bill_id;
                calOverAry[i] = calOver;
            }
            var calOverJson = JSON.stringify(calOverAry);
            //alert(calOverJson);
            var url = path + "/backendAdmin/calCycleSearchServlet?method=email&calOverAry=" + calOverJson;
            $.getJSON(url, {calOverAry: calOverAry}, function (data) {
                alert(data);
            })
        } else {
            alert("請先勾選資料列。");
        }
    }

    $("#run").click(function(){
        var runItem = $("#runItem").val();
        if(runItem == "calOverYM"){ //計算-by年月
            calOverYM();
        }else if(runItem == "calOver"){ //計算-多筆
            calOver();
        }else if(runItem == "calOverToCash"){ //計算超額且出帳(請選擇年月、用戶、要計算哪幾筆)
            calOverToCash();
        }else if(runItem == "emailYM"){ //寄email-by年月
            emailYM();
        }else if(runItem == "email"){ //寄email-多筆
            email();
        }
    })


</script>