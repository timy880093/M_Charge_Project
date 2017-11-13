<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
<tr>
    <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td align="left"><span class="textstyle_06">用戶費率管理</span></td>
                <td align="right">
                <span class="select_div">
                  <select name="searchField" id="searchField" class="forms_Dropdown" onchange="changeQtype(this.value)">
                      <option value="name">用戶名稱</option>
                      <option value="business_no">統編</option>
                      <option value="almost_out">查詢快過期合約(YYYYMM)</option>
                      <option value="pkg_status">合約狀態</option>
                  </select>
                  <input type="text" name="searchString" id="searchString"/>
                  <select name="sel_pkg_status" id="sel_pkg_status" class="forms_Dropdown"  style="display: none">
                      <option value="0">未生效</option>
                      <option value="1">生效中</option>
                      <option value="2">作廢</option>
                      <option value="3">過期</option>
                      <option value="4">未綁約</option>
                  </select>
                    <a href="#" id="send" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
                    <a href="#" id="continuePackage" title="快過期合約出帳(為了續約)" class="butbox"><span>快過期合約續約(請再出帳並通知他們)</span></a>
                    <div style="display: block; color:red; ">當您作了${REQUEST_SEND_OBJECT_0}的超額計算後，再作${REQUEST_SEND_OBJECT_1}前的快到期合約續約，會無法匯出上銀Excel。(因為續約前的超額已先被計算了)</div>
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
    var grid = $("#jqgrid"), i;
    $(document).ready(function () {
        var myGrid = jQuery("#jqgrid").jqGrid({
            url: '<%=request.getContextPath()%>/backendAdmin/companySearchServlet?method=search',
            datatype: "json",

            colNames: ['company_id', 'parent_id', '用戶名稱', '統編','收費型態','實際起始日','實際結束日','狀態','設定','company_id','package_id'],
            colModel: [
                {name: 'company_id', index: 'company_id', hidden: true, key: true, hidedlg: true},
                {name: 'parent_id', index: 'parent_id', hidden: true, hidedlg: true},
                {name: 'name', index: 'name'},
                {name: 'business_no', index: 'business_no'} ,
                {name: 'package_type', index: 'package_type',formatter:formatType} ,
                {name:  'real_start_date', index: 'real_start_date', formatter:formatDateType} ,
                {name:  'real_end_date', index: 'real_end_date', formatter:formatDateType} ,
                {name:  'pkg_status', index: 'pkg_status', formatter:formatStatus} ,
                {name: 'company_id', index: 'company_id', formatter: formatSetting},
                {name: 'company_id', index: 'company_id', hidden: true, key: true, hidedlg: true},
                {name: 'package_id', index: 'package_id', hidden: true, key: true, hidedlg: true}
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
            onSortCol:function (index, columnIndex, sortOrder)
            {
                $("#jqgrid").jqGrid("setGridParam", {
                    search: true
                });
            },
            beforeSelectRow: function(rowid, e) {
                var row = $("#jqgrid").jqGrid('getRowData', rowid);
                if(row.pkg_status == '未生效'){
                    return false;
                }else{
                    return true;
                }
            },
            onSelectAll: function(aRowids,status) {
                if (status) {
                    // uncheck "protected" rows
                    var cbs = $("tr.jqgrow > td > input.cbox:disabled", grid[0]);
                    cbs.removeAttr("checked");

                    //modify the selarrrow parameter
                    grid[0].p.selarrrow = grid.find("tr.jqgrow:has(td > input.cbox:checked)")
                            .map(function() { return this.id; }) // convert to set of ids
                            .get(); // convert to instance of Array
                }
            },
            loadComplete: function () {
                var ids =jQuery("#jqgrid").jqGrid('getDataIDs');
                for(var i=0;i < ids.length;i++){
                    var rowId = ids[i];
                    var row = $("#jqgrid").jqGrid('getRowData', rowId);
                    if(row.pkg_status == '未生效'){
                        $("#jqg_jqgrid_"+rowId, "#jqgrid").attr("disabled",true);
                    }
                }

                //查詢後如無資料，請show"查無資料"的訊息。
                if(ids.length == 0){
                    alert("查無資料");
                }


                $(".butbox2").click(function () {
                    var companyId = $(this).parent('td').parent('tr').attr('id');
                    var type = $(this).prev().val();
                    if(type==''){
                        return;
                    }
                    var url = '';
                    url = '<%=request.getContextPath()%>/backendAdmin/companyChargeEditServlet?method=edit&type='+type+'&companyId=' + companyId;
                    MM_openBrWindow(url,1200,800);
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


    function formatSetting(cellvalue, options, rowObject) {
        var value = cellvalue;
        var package_type = rowObject['package_type'];
        var str = '';
        str += '<select name="fieldName" class="forms_Dropdown">' ;
        str += '<option value="">請選擇</option>';
        str += '<option value="1" selected>設定費率-月租型</option>';
//        str += '<option value="2">設定費率-預繳型</option>';
        str += '</select>';
        str += '<a href="#"  title="確定" class="butbox2"><span>確&nbsp;&nbsp;定</span></a>';

        return str;


    }

    function formatType(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';
        if(value==1){
            str = '月租型';
        }else if(value==2){
            str = '級距型';
        } else{
            str = '無';
        }

        return str;
    }

    function formatDateType(cellvalue, options, rowObject){
        if(cellvalue != null){
            var value = cellvalue;
            var str = value.substring(0,10);
            return str;
        }
        return "";
    }
    //續約快過期合約在查詢時若勾選包含作廢的無法執行
    //反正作廢的本就不需續約,可否直接剃除,或在查詢時就不須帶出(本來作廢的就不該在快過期合約中,因為已作廢)
    function formatStatus(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';
        if(value=='0'){
            str = '未生效';
        }else if(value=='1'){
            str = '生效';
        }
//        else if(value=='2'){
//            str = '作廢';
//        } else if(value=='3'){
//            str = '過期';
//        }

        return str;
    }

    function changeQtype(qtype) {
        if (qtype == 'pkg_status') {
            $('#searchString').hide();
            $('#sel_pkg_status').show();
        }  else{
            $('#searchString').show();
            $('#sel_pkg_status').hide();
        }
    }


    $("#send").click(function () {
        var pv = $("#searchField").val();
        var pn = $("#searchString").val();
        var sel_pkg_status = $("#sel_pkg_status").val();
        var sdata = setSearchValue(pv, encodeURIComponent(pn));
        if(pv=="pkg_status"){
            sdata = setSearchValue(pv, encodeURIComponent(sel_pkg_status));
        }
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1, search: true
        }).trigger("reloadGrid");
    });

    //continuePackage
    $("#continuePackage").click(function () {
        continuePackage();
    });

    function continuePackage(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var almostOutAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var almostOut = new Object();
                almostOut.companyId = row.company_id;
                almostOut.packageId = row.package_id;
                almostOutAry[i] = almostOut;
            }
            var almostOutJson = JSON.stringify(almostOutAry);
            //alert(almostOutJson);
            var url = path + "/backendAdmin/companySearchServlet?method=continuePackage&almostOut=" + almostOutJson;
            $.getJSON(url, {almost_out: almostOutAry}, function (data) {
                alert(data);
                //window.location.reload();
                $("#send").click();
            })
        } else {
            alert("請先勾選資料列。");
        }
    }

</script>