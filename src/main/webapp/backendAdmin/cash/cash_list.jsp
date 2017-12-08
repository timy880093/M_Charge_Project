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
                <td align="left"><span class="textstyle_06">出/入帳管理</span></td>
                <td align="left">
                <span class="select_div">
                    <form action="<%=request.getContextPath()%>/backendAdmin/cashSearchServlet" method="post" id="cashMasterForm" name="cashMasterForm">
                        出帳單年月
                        <select name="outYM" id="outYM" class="forms_Dropdown">
                        </select>
                        用戶名稱
                        <input type="text" id="userCompany" name="userCompany" /><input type="button" value="..." id="allUserCompany">
                        <input type="hidden" id="userCompanyId" name="userCompanyId" />
                        公司名稱<input type="text" id="companyName" name="companyName" />
                        付款狀態
                        <select name="payStatus" id="payStatus" class="forms_Dropdown">
                            <option value="all">全部</option>
                            <option value="paid">已付款</option>
                            <option value="unpay">未付款</option>
                        </select>
                        <a href="#" id="search" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
                        <select name="runItem" id="runItem" class="forms_Dropdown">
                            <option value="out">出帳-多筆(請勾選欲執行的資料)</option>
                            <option value="outYM">出帳-批次(請選擇出帳單年月)</option>
                            <option value="outExcel">匯出Excel帳單-多筆(請勾選欲執行的資料)</option>
                            <option value="outExcelym">匯出Excel帳單-批次(請選擇出帳單年月)</option>
                            <option value="email">寄email明細通知-多筆(請勾選欲執行的資料)</option>
                            <option value="emailYM">寄email明細通知-批次(請選擇出帳單年月)</option>
                            <option value="email1">未繳費客戶email通知-多筆(請勾選欲執行的資料)</option>
                            <option value="cancelOut">取消出帳-多筆(請勾選欲執行的資料)</option>
                            <option value="cancelOutYM">取消出帳-批次(請選擇出帳單年月)</option>
                            <option value="inExcel">入帳-匯入入帳資料(Excel)</option>
                            <option value="invoice">匯出發票資料-多筆(請勾選欲執行的資料)</option>
                            <option value="invoiceYM">匯出發票資料-批次(請選擇出帳單年月)</option>
                        </select>
                        <a href="#" id="run" title="執行" class="butbox"><span>執行</span></a>
                    </form>
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
            url: '<%=request.getContextPath()%>/backendAdmin/cashSearchServlet?method=search',
            datatype: "json",
            postData: sdata,
            colNames: ['cash_master_id', 'company_id', '公司名稱','出帳年月','出帳上銀年月','總未稅價','總含稅價','出帳日期','入帳金額','入帳日期','備註','寄e-mail日期','首次','功能','cash_master_id'
            ],
            colModel: [
                {name: 'cash_master_id', index: 'cash_master_id', hidden: true, key: true, hidedlg: true},
                {name: 'company_id', index: 'company_id', hidden: true, hidedlg: true},
                {name: 'name', index: 'name', width: '400px'},
                {name: 'out_ym', index: 'out_ym'},
                {name: 'bank_ym', index: 'bank_ym'},
                {name: 'no_tax_inclusive_amount', index: 'no_tax_inclusive_amount'},
                {name: 'tax_inclusive_amount', index: 'tax_inclusive_amount'},
                {name: 'out_date', index: 'out_date'},
                {name: 'in_amount', index: 'in_amount', formatter: formatImAmount},
                {name: 'in_date', index: 'in_date', formatter: formatImDate, width: '200px'},
                {name: 'in_note', index: 'in_note', formatter: formatImNote},
                {name: 'email_sent_date', index: 'email_sent_date'},
                {name: 'is_first', index: 'is_first', formatter: formatIsFirst},
                {name: 'cash_master_id', index: 'cash_master_id', formatter: formatSetting, width: '250px', align: 'center'},
                {name: 'cash_master_id', index: 'cash_master_id', hidden: true, key: true, hidedlg: true}
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

                //設定入帳日期的日曆
                var ids =jQuery("#jqgrid").jqGrid('getDataIDs');
                for(var i=0;i < ids.length;i++){
                    var rowId = ids[i];
                    var row = $("#jqgrid").jqGrid('getRowData', rowId);
                    $("#in_date"+rowId).datepicker({
                        dateFormat: 'yy-mm-dd',
                        changeMonth: true,
                        changeYear: true,
                        showOn: "button",
                    });
                }

                $(".butbox2").click(function () {
                    var selectOption = $(this).prev().val();
                    var cashMasterId = $(this).parent('td').parent('tr').find('td:eq(2)').text();
                    var in_amount = $(this).parent('td').parent('tr').find('td:eq(10)').children("input").val();
                    var in_date = $(this).parent('td').parent('tr').find('td:eq(11)').children("input").val();
                    var in_note = $(this).parent('td').parent('tr').find('td:eq(12)').children("input").val();
                    var email = $(this).parent().find('[name=email]').val();
                    var email1 = $(this).parent().find('[name=email]').val();
                    //alert("selectOption="+selectOption+"cashMasterId="+cashMasterId+",in_amount="+in_amount+",in_date="+in_date+",in_note="+in_note);

                    var url = '';
                    if(selectOption == 'viewCashDetail') {
                        url = path + '/backendAdmin/cashEditServlet?method=viewCashDetail&cashMasterId=' + cashMasterId;
                        MM_openBrWindow(url,1600,600);
                    }else if(selectOption == 'in'){
                        url = path + '/backendAdmin/cashAjaxServlet?method=in&cashMasterId=' + cashMasterId +"&inAmount="+in_amount+"&inDate="+in_date+"&inNote="+encodeURIComponent(in_note);
                        $.getJSON( url, function( data ) {
                            alert(data);
                            if(data == "success!!"){
                                $("#search").click();
                            }
                        });
                    }else if(selectOption == 'reSendEmail') {
                        url = path + '/backendAdmin/cashAjaxServlet?method=reSendEmail&cashMasterId=' + cashMasterId + "&email=" + email;
                        $.getJSON(url, function (data) {
                            alert(data);
                        });
                    }else if(selectOption =='reSendEmail1'){
                        url = path + '/backendAdmin/cashAjaxServlet?method=reSendEmail1&cashMasterId=' + cashMasterId + "&email=" + email;
                        $.getJSON(url, function (data) {
                            alert(data);
                        });


                    }else if(selectOption == 'cancelIn'){
                        url = path + '/backendAdmin/cashAjaxServlet?method=cancelIn&cashMasterId=' + cashMasterId ;
                        $.getJSON( url, function( data ) {
                            alert(data);
                        });
                    }
                });

                $('[name=fieldName]').bind('change', function () {
                    var value = $(this).val();
                    if(value == 'reSendEmail'){
                        $(this).parent().find('[name=email]').show();
                    } else {
                        $(this).parent().find('[name=email]').hide();
                    }

                });
                $('[name=fieldName]').bind('change', function () {
                    var value = $(this).val();
                    if(value == 'reSendEmail1'){
                        $(this).parent().find('[name=email]').show();
                    } else {
                        $(this).parent().find('[name=email]').hide();
                    }

                });




            }
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
        var select =   $("#outYM");
        var data = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
            select.show();
            select.empty();
            select.append(($('<option></option>').val("").html("請選擇")));
            $.each(data,function(index,value){
                select.append($('<option></option>').val(value.calYear + value.calMonth).html(value.calYear + value.calMonth));

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
        $("#outYM").val(defaultYm);
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

    function test(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
                //alert("勾選[" + (i+1) + "]=" + row.price + "," + row.company_id);
            }
        } else {
            alert("請先勾選資料列。");
        }
    }


    $("#allUserCompany").on("click",function(){
        $("#userCompany").autocomplete("search", "");
    });


    function formatSetting(cellvalue, options, rowObject) {
        var value = cellvalue;
        var is_validated = rowObject['is_validated'];
        var str = '';

        str = '<select name="fieldName" class="forms_Dropdown">' +
                '<option value="viewCashDetail">帳單明細</option>' +
                '<option value="reSendEmail">明細email寄送</option>' +
            '<option value="reSendEmail1">未繳費客戶通知</option>' +
                '<option value="in">確認入帳</option>' +
                '<option value="cancelIn">取消入帳</option>';
        str += '</select>';
        str += '<a href="#"  title="確定" class="butbox2"><span>確定</span></a>';
        str += '<input type="text" name="email" maxlength="40" style="display: none"/>';

        return str;
    }

    function formatImAmount(cellvalue, options, rowObject) {
        var out_date = rowObject['out_date'];
        var in_date = rowObject['in_date'];
        var inAmount = '';
        if(cellvalue != undefined){ //出帳金額
            inAmount = cellvalue;
        }
        if(out_date != undefined && in_date == undefined){ //已出帳且未入帳的CashMaster才可以修改入帳
            return "<input type='text' name=inAmount id=inAmount value='"+inAmount+"' size=10 />";
        }else{
            return inAmount;
        }
    }

    function formatImDate(cellvalue, options, rowObject) {
        var cash_master_id = rowObject['cash_master_id'];
        var out_date = rowObject['out_date'];
        var in_date = rowObject['in_date'];
        var inDate = '';
        if(cellvalue != undefined){ //出帳金額
            inDate = cellvalue;
        }
        if(out_date != undefined && in_date == undefined){ //已出帳且未入帳的CashMaster才可以修改入帳
            $("#in_date"+cash_master_id).datepicker({
                dateFormat: 'yy-mm-dd',
                changeMonth: true,
                changeYear: true,
                showOn: "button",
            });
            return "<input type=text id=in_date"+cash_master_id+" name=in_date"+cash_master_id+" size=10 readonly/>";

        }else{
            return inDate;
        }
    }

    function formatImNote(cellvalue, options, rowObject) {
        var out_date = rowObject['out_date'];
        var in_date = rowObject['in_date'];
        var imNote = '';
        if(cellvalue != undefined){ //出帳金額
            imNote = cellvalue;
        }
        if(out_date != undefined && in_date == undefined){ //已出帳且未入帳的CashMaster才可以修改入帳
            return "<input type='text' name=inAmount id=inAmount value='"+imNote+"' size=10 />";
        }else{
            return imNote;
        }
    }

    function formatIsFirst(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';

        switch(value) {
            case "1":
                str = '是';
                break;
            default:
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
        var outYM = $("#outYM").val();
        var userCompanyId = $("#userCompanyId").val();
        var payStatus = $("#payStatus").val();
        var companyName = $("#companyName").val();
        var sdata = setSearchValue("outYM", encodeURIComponent(outYM), "userCompanyId", encodeURIComponent(userCompanyId), "payStatus", encodeURIComponent(payStatus),"companyName", encodeURIComponent(companyName));
        return sdata;
    }


    $("#run").click(function(){
        var outYM = $("#outYM").val();
        var runItem = $("#runItem").val();
        switch(runItem) {
            case "out": //出帳-多筆(請勾選欲執行的資料)
                multiMethod("out");
                break;
            case "outYM": //出帳-批次(請選擇出帳單年月)
                runMethod("outYM");
                break;
            case "inExcel": //入帳-匯入入帳資料(Excel)
                var url = path + '/backendAdmin/cashImportServlet?method=inExcel';
                openNewPage(url);
                break;
            case "outExcelym": //匯出Excel帳單-批次(請選擇出帳單年月)
                var outYm = $("#outYM").val();
                if(outYm == ''){
                    alert('請選擇帳單年月');
                    break;
                }
                var url = path + '/backendAdmin/cashSearchServlet?method=outExcelym&outYM='+outYm;
                location.href = url;
                break;
            case "outExcel":  //匯出上銀Excel -多筆  (匯出Excel帳單-多筆(請勾選欲執行的資料))
                var outYm = $("#outYM").val();
                if(outYm == ''){
                    alert('請選擇帳單年月');
                    break;
                }
                var destJson  = multiSelected();
                if(destJson == false){
                    alert("請先勾選資料列。");
                    break;
                }
                var url = path + '/backendAdmin/cashSearchServlet?method=outExcel&outYM='+outYm+"&destJson="+destJson;
                location.href = url;
                break;
            case "cancelOutYM": //取消出帳-By年月
                runMethod("cancelOutYM");
                break;
            case "cancelOut": //取消出帳-多筆
                multiMethod("cancelOut");
                break;
            case "email": //寄email明細通知-多筆(請勾選欲執行的資料)
                multiMethod("email");
                break;
            case "email1"://寄email未繳費客戶email通知
               runMethod("email1");
               break;
            case "emailYM": //寄email明細通知-批次(請選擇出帳單年月)
                runMethod("emailYM");
                break;

                case "invoiceYM": //匯出發票資料-批次(by年月)
                var outYm = $("#outYM").val();
                if(outYm == ''){
                    alert('請選擇帳單年月');
                    break;
                }
                var url = path + '/backendAdmin/cashSearchServlet?method=invoiceExcelYM&outYM='+outYm;
                location.href = url;
                break;
                break;
            case "invoice": //匯出發票資料-多筆
                var destJson  = multiSelected();
                if(destJson == false){
                    alert("請先勾選資料列。");
                    break;
                }
                var url = path + '/backendAdmin/cashSearchServlet?method=invoiceExcel&outYM='+outYm+"&destJson="+destJson;
                location.href = url;
                break;
                break;
            default:
                alert("有事嗎?");
        }
    })

    function runMethod(methodName){
        //callback handler for form submit
        $("#cashMasterForm").submit(function(e)
        {
            var postData = $(this).serializeArray();
            var formURL = $(this).attr("action") + "?method="+methodName ;
            $.ajax(
                    {
                        url : formURL,
                        type: "POST",
                        data : postData,
                        success:function(data, textStatus, jqXHR)
                        {
                            //data: return data from server
                            alert(data);
                            $("#search").click();
                        },
                        error: function(jqXHR, textStatus, errorThrown)
                        {
                            //if fails
                        }
                    });
            e.preventDefault(); //STOP default action
            e.unbind(); //unbind. to stop multiple form submit.
        });

        $("#cashMasterForm").submit(); //Submit  the FORM
    }

    function multiMethod(methodName){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var objAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var obj = new Object();
                obj.cashMasterId = row.cash_master_id;
                objAry[i] = obj;
            }
            var destJson = JSON.stringify(objAry);
            //alert(calOverJson);
            var url = path + "/backendAdmin/cashSearchServlet?method="+methodName+"&destJson=" + destJson;
            $.getJSON(url, {destJson: destJson}, function (data) {
                alert(data);
                $("#search").click();
            })
        } else {
            alert("請先勾選資料列。");
        }
    }

    function multiSelected(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var objAry = new Array(_aIDs.length);
            for (var i = 0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var obj = new Object();
                obj.cashMasterId = row.cash_master_id;
                objAry[i] = obj;
            }
            var destJson = JSON.stringify(objAry);
            return destJson;
        }
        return false;
    }

    function openNewPage(url){
        MM_openBrWindow(url,1400,600);
    }

    function inMethod(){
        var outYM = $("#outYM").val();
        var userCompanyId = $("#userCompanyId").val();
        var url = path+"/backendAdmin/cashSearchServlet?method=In&outYM="+outYM + "&userCompanyId="+userCompanyId
        $.getJSON(url,{outYM:outYM, userCompanyId:userCompanyId}, function( data ){
            alert(data);
            $("#search").click();
        })
    }

    $("#download").click(function(){
        var url = path + '/backendAdmin/invoiceJustSearchServlet?method=download&_search=false';
        location.href = url;
    });
</script>