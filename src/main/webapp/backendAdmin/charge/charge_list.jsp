<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
<tr>
    <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td align="left"><span class="textstyle_06">計費方案管理</span></td>
                <td align="right">
                <span class="select_div">
                    方案類型
                    <select id="charge_type">
                          <option value="">請選擇</option>
                          <option value="1">月租</option>
                          <option value="2">級距</option>
                    </select>
                    <select name="searchField" id="searchField" class="forms_Dropdown">
                        <option value="package_name">方案名稱</option>
                    </select>
                    <input type="text" name="searchString" id="searchString"/>
                    方案狀態
                    <select id="status">
                          <option value="">請選擇</option>
                          <option value="1">生效中</option>
                          <option value="4">暫停</option>
                    </select>
                </span>
                    <a href="#" id="send" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
                    <a href="#" id="create" title="新增" class="butbox"><span>新&nbsp;&nbsp;增</span></a>
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
    $(document).ready(function () {
        var myGrid = jQuery("#jqgrid").jqGrid({
            url: '<%=request.getContextPath()%>/backendAdmin/chargeSearchServlet?method=search',
            datatype: "json",
            colNames: ['charge_id','charge_type', '方案名稱', '方案類型', '銷售價格', '狀態', '功能'],
            colModel: [
                {name: 'charge_id', index: 'charge_id', hidden: true, hidedlg: true},
                {name: 'charge_type', index: 'charge_type', hidden: true, hidedlg: true},
                {name: 'package_name', index: 'package_name'},
                {name: 'charge_type', index: 'charge_type',formatter:setType},
                {name: 'sales_price', index: 'sales_price'},
                {name: 'status', index: 'status',formatter:setStatus},
                {name: 'charge_id', index: 'charge_id', formatter: setting}
            ],
            rowNum: 100,
            rowList: [100, 200, 300],
            pager: '#gridPager',
            sortname: 'status',
            sortorder: "asc",
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
            loadComplete: function () {

                //查詢後如無資料，請show"查無資料"的訊息。`
                var ids =jQuery("#jqgrid").jqGrid('getDataIDs');
                if(ids.length == 0){
                    alert("查無資料");
                }

                $(".butbox2").click(function () {
                    var selectOption = $(this).prev().val();
                    var chargeId = $(this).parent('td').parent('tr').find('td:eq(2)').text();
                    var type = $(this).parent('td').parent('tr').find('td:eq(3)').text();
                    var url = '';
                    console.log("selectOption="+selectOption+", chargeId="+chargeId+", type="+type);
                    if (selectOption == 'editCharge') {
                        url = '<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=edit&chargeId=' + chargeId+'&type='+type;
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

    function setting(cellvalue, options, rowObject) {
        var value = cellvalue;
        var str = '';
        str = '<select name="fieldName" class="forms_Dropdown"><option value="editCharge">修改</option>';
        str += '</select>';
        str += '<a href="#"  title="確定" class="butbox2"><span>確&nbsp;&nbsp;定</span></a>';
        return str;

    }


    function setStatus(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';
        if(value=='1'){
            str ='生效中';
        } else if(value=='2'){
            str = '未生效';
        } else if(value=='3'){
            str = '已結束';
        } else if(value=='4'){
            str = '暫停';
        }
        return str;
    }

    function setType(cellvalue, options, rowObject){
        var value = cellvalue;
        var str = '';
        if(value=='1'){
            str = '月租型';
        } else if(value=='2'){
            str = '級距型';
        }
        //str = '月租型';
        return str;
    }



    $("#create").click(function(){
        //var url = "<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=edit&type=1";
        var url = "<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=add";
        MM_openBrWindow(url, 800,800);
    });

    $("#send").click(function () {
        var pv = $("#searchField").val();
        var pn = $("#searchString").val();
        var charge_type = $("#charge_type").val();
        var status = $("#status").val();
        var sdata = setSearchValue(pv, encodeURIComponent(pn), 'charge_type', charge_type,'status',status);
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1, search: true
        }).trigger("reloadGrid");
    });

</script>