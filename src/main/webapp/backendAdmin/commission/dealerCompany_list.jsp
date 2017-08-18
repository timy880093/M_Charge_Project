<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
<tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="left"><span class="textstyle_06">經銷商方案列表</span></td>
            <td align="right">
                <select name="searchField" id="searchField" class="forms_Dropdown" onchange="changeQtype(this.value)">
                    <option value="dealer_company_name">經銷商公司名稱</option>
                    <option value="commission_type">經銷公司的佣金類型</option>
                    <option value="status">經銷公司狀態</option>
                </select>
                <input type="text" name="searchString" id="searchString"/>
                <%--方案類型--%>
                <select id="commission_type" style="display: none">
                    <option value="">請選擇</option>
                    <option value="0">固定金額</option>
                    <option value="1">固定比例</option>
                    <option value="2">經銷商</option>
                </select>
                <%--方案狀態--%>
                <select id="status" style="display: none">
                    <option value="">請選擇</option>
                    <option value="1">生效中</option>
                    <option value="2">暫停</option>
                </select>


                <a href="#" id="send" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
            <a href="#" id="create" title="新增" class="butbox"><span>新&nbsp;&nbsp;增</span></a></td>
        </tr>
    </table></td>
</tr>




<tr>
    <td><div class="content" title="主要區域" style=" width:100%; ">
        <div class="spacer"></div>
        <table id="jqgrid" width="95%" border="0" cellspacing="0" cellpadding="0">
        </table>
        <div id="gridPager"></div>
    </div></td>
</tr>


<script type="text/javascript">
    $(document).ready(function () {
        var myGrid = jQuery("#jqgrid").jqGrid({
            url: '<%=request.getContextPath()%>/backendAdmin/dealerCompanySearchServlet?method=search',
            datatype: "json",
            colNames: ['dealer_company_id','經銷商名稱', '佣金類型', '抽佣%數','狀態','設定'],
            colModel: [
                {name: 'dealer_company_id', index: 'dealer_company_id', hidden: true, key: true, hidedlg: true},
                {name: 'dealer_company_name', index: 'dealer_company_name'},
                {name: 'commission_type', index: 'commission_type',formatter:formatType},
                {name: 'main_percent', index: 'main_percent'},
                {name: 'status', index: 'status',formatter:formatStatus},
                {name: 'dealer_company_id', index: 'dealer_company_id',formatter:setting}
            ],
            rowNum: 100,
            rowList: [100, 200, 300],
            pager: '#gridPager',
            sortname: 'dealer_company_id',
            sortorder: "asc",
            onPaging: function(id){
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
            loadComplete:function(){

                //查詢後如無資料，請show"查無資料"的訊息。
                var ids =jQuery("#jqgrid").jqGrid('getDataIDs');
                if(ids.length == 0){
                    alert("查無資料");
                }

                $(".butbox2").click(function(){
                    var selectOption = $(this).prev().val();
                    var id =$(this).parent('td').parent('tr').attr('id');
                    var url = '';
                    if(selectOption=='editCommission'){
                        url=  '<%=request.getContextPath()%>/backendAdmin/dealerCompanyEditServlet?method=edit&dealerCompanyId=' + id ;
                    }
                    MM_openBrWindow(url,800,800);
                });
            }

        });

    });


    function formatType(cellvalue, options, rowObject) {
        var value = cellvalue;
        var str = value.toString();
        switch(value){
            case '0':str='抽傭金額'; break;
            case '1':str='抽成%數'; break;
            case '2':str='每筆代收'; break;
        }

        return str;

    }

    function formatStatus(cellvalue, options, rowObject) {
        var value = cellvalue;
        var str = value.toString();
        switch(value){
            case 1:str='生效中'; break;
            case 2:str='暫停'; break;
        }
        return str;

    }

    function setting(cellvalue, options, rowObject) {
        var value = cellvalue;
        var id = rowObject['company_id'];
        var str ='';
        str = '<select name="fieldName" class="forms_Dropdown"><option value="editCommission">修改</option>';
        str+= '</select>';
        str+='<a href="#"  title="確定" class="butbox2"><span>確&nbsp;&nbsp;定</span></a>';
        return str;

    }


    $( "#send" ).click(function() {
        var pv = $("#searchField").val();
        var pn = $("#searchString").val();

        var commission_type = $("#commission_type").val();
        var status = $("#status").val();
        var searchValue ;
        if(pv=='dealer_company_name'){
            searchValue = encodeURIComponent(pn);
        } else if(pv=='commission_type'){
            searchValue = commission_type;
        } else{
            searchValue = status;
        }
        var sdata = setSearchValue(pv, searchValue);
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1,search:true
        }).trigger("reloadGrid");
    });

    function changeQtype(qtype) {
        if (qtype == 'dealer_company_name') {
            $('#searchString').show();
            $('#commission_type').hide();
            $('#status').hide();
        } else if (qtype == 'commission_type'){
            $('#searchString').hide();
            $('#commission_type').show();
            $('#status').hide();
        }  else{
            $('#searchString').hide();
            $('#commission_type').hide();
            $('#status').show();
        }
    }

    $("#create").click(function () {
        var url = "<%=request.getContextPath()%>/backendAdmin/dealerCompanyEditServlet?method=create";
        MM_openBrWindow(url,800,800);
    });



</script>