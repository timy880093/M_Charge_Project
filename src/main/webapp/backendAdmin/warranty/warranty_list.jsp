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
                <td align="left"><span class="textstyle_06">機器保固</span></td>
                <td align="right">
                    <span class="select_div">
                        <form action="<%=request.getContextPath()%>/backendAdmin/warrantySearchServlet" method="post" id="warrantyForm" name="warrantyForm">
                            經銷商名稱
                            <input type="text" id="userDealerCompany" name="userDealerCompany" /><input type="button" value="..." id="allUserDealerCompany">
                            <input type="hidden" id="userDealerCompanyId" name="userDealerCompanyId" />
                            用戶名稱
                            <input type="text" id="userCompany" name="userCompany" /><input type="button" value="..." id="allUserCompany">
                            <input type="hidden" id="userCompanyId" name="userCompanyId" />
                            &nbsp;出貨狀態
                            <select name="onlyShipForSearch" id="onlyShipForSearch">
                                <option value="">全部</option>
                                <option value="1">僅出貨</option>
                            </select>
                            &nbsp;狀態
                            <select name="statusForSearch" id="statusForSearch">
                                <option value="">全部</option>
                                <option value="1">使用中</option>
                                <option value="2">不使用</option>
                                <option value="3">退回</option>
                            </select>
                            <a href="#" id="send" title="執行" class="butbox"><span>查詢</span></a>
                             <a href="#" id="exportWar" title="匯出" class="butbox"><span>匯出資料</span></a>
                            <a href="#" id="create" title="新增" class="butbox"><span>新&nbsp;&nbsp;增</span></a></span></td>
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
            url: '<%=request.getContextPath()%>/backendAdmin/warrantySearchServlet?method=search',
            datatype: "json",
            postData: sdata,
            colNames: ['warranty_id', 'company_id', '發票機序號', '用戶名稱','經銷商名稱', '出貨狀態', '保固起日','保固迄日','是否延長保固','型號','備註', '狀態', '設定'],
            colModel: [
                {name: 'warranty_id', index: 'warranty_id', hidden: true, key: true, hidedlg: true},
                {name: 'company_id', index: 'company_id', hidden: true, hidedlg: true},
                {name: 'warranty_no', index: 'warranty_no'},
                {name: 'name', index: 'name'},
                {name: 'dealer_company_name', index: 'dealer_company_name'},
                {name: 'only_ship', index: 'only_ship', formatter:onlyShipFormat},
                {name: 'start_date', index: 'start_date', formatter: dateFormat},
                {name: 'end_date', index: 'end_date', formatter: dateFormat },
                {name: 'extend', index: 'extend', formatter: extendFormat},
                {name: 'model', index: 'model'},
                {name: 'note', index: 'note'},
                {name: 'status', index: 'status', formatter: statusFormat},
                {name: 'warranty_id', index: 'warranty_id', formatter: settingFormat}
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
                $(".butbox2").click(function () {
                    var selectOption = $(this).prev().val();
                    var warrantyId = $(this).parent('td').parent('tr').attr('id');
                    var url = '';
                    if (selectOption == 'edit') {
                        url = '<%=request.getContextPath()%>/backendAdmin/warrantyEditServlet?method=edit&warrantyId=' + warrantyId;
                    }
                    MM_openBrWindow(url,800,800);
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

    $("#search").click(function () {
        var sdata = searchCriteria();
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1, search: true
        }).trigger("reloadGrid");
    });

    function searchCriteria(){
        var userCompanyId = $("#userCompanyId").val();
        var userDealerCompanyId = $("#userDealerCompanyId").val();
        var onlyShipForSearch = $("#onlyShipForSearch").val();
        var statusForSearch = $("#statusForSearch").val();
        var sdata = setSearchValue("userCompanyId", encodeURIComponent(userCompanyId), "userDealerCompanyId", encodeURIComponent(userDealerCompanyId), "onlyShipForSearch", encodeURIComponent(onlyShipForSearch),"statusForSearch", encodeURIComponent(statusForSearch));
        return sdata;
    }

    //用戶清單
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
                    $("#userDealerCompanyId").val(cpDealerList[memberIndex].dealer_company_id);
                }
            }
        }
    });

    $("#userDealerCompany").blur(function(){
        if($("#userDealerCompany").val()==""){
            $("#userDealerCompanyId").val("");
        }
    });

    $("#allUserDealerCompany").on("click",function(){
        $("#userDealerCompany").autocomplete("search", "");
    });


    $("#create").click(function () {
        var url = "<%=request.getContextPath()%>/backendAdmin/warrantyEditServlet?method=create";

        MM_openBrWindow(url,800,800);
    });

    function settingFormat(cellvalue, options, rowObject){
        var value = cellvalue;
        var str='<select name="fieldName" class="forms_Dropdown">'+
             '<option value="edit">編輯</option>'+
             '</select>';
        str+='<a href="#"  title="確定" class="butbox2"><span>確&nbsp;&nbsp;定</span></a>';
        return str;
    }

    //出貨狀態
    function onlyShipFormat(cellvalue, options, rowObject){
        var value = cellvalue;
        if(value=="1"){
            return "僅出貨";
        }else{
            return "";
        }
    }

    //日期起迄
    function dateFormat(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = value.substring(0, 10);
        return str;
    }

    //是否延長保固
    function extendFormat(cellvalue, options, rowObject){
        var value = cellvalue;
        //var id = rowObject['company_id'];
        var str = value;
        if (value == true) {
            str = '是';
        } else if (value == false) {
            str = '否';
        }
        return str;
    }

    //狀態: 1.使用中 2.不使用 3.退回
    function statusFormat(cellvalue, options, rowObject){
        var value = cellvalue;
        //var id = rowObject['company_id'];
        var str = value;
        if (value == '1') {
            str = '使用中';
        } else if (value == '2') {
            str = '不使用';
        } else if (value == '3') {
            str = '退回';
        }
        return str;
    }

    $("#send").click(function (){
        var sdata = searchCriteria();
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1, search: true
        }).trigger("reloadGrid");
    });

    $("#exportWar").click(function(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var warrantyAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var warranty = new Object();
                warranty.warrantyid = row.warranty_id;
                warrantyAry[i] = warranty;
            }
            var warrantyJson = JSON.stringify(warrantyAry);
            //alert(commissionLogJson);
            var url = path + "/backendAdmin/warrantySearchServlet?method=exportWar&warranty=" + warrantyJson;

            location.href = url;
        } else {
            alert("請先勾選資料列。");
        }
    });








</script>