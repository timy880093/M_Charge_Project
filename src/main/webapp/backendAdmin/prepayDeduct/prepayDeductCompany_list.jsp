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
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="left"><span class="textstyle_06">預用金管理</span></td>
            <td align="right">
                <input type="text" id="userCompany" name="userCompany" size="50"/><input type="button" value="..." id="allUserCompany">
                <input type="hidden" id="userCompanyId" name="userCompanyId" />
                <a href="#" id="send" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
                <a href="#" id="create" title="新增用戶" class="butbox"><span>新增用戶</span></a>
            </td>
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
            url: '<%=request.getContextPath()%>/backendAdmin/prepayDeductSearchServlet?method=search',
            datatype: "json",
            colNames: ['prepay_deduct_master_id','用戶名稱', '總預用金額', '是否啟用超額預繳扣抵','設定'],
            colModel: [
                {name: 'prepay_deduct_master_id', index: 'prepay_deduct_master_id', hidden: true, key: true, hidedlg: true},
                {name: 'name', index: 'name'},
                {name: 'amount', index: 'amount'},
                {name: 'is_enable_over', index: 'is_enable_over'},
                {name: 'prepay_deduct_master_id', index: 'prepay_deduct_master_id',formatter:setting}
            ],
            rowNum: 100,
            rowList: [100, 200, 300],
            pager: '#gridPager',
            sortname: 'prepay_deduct_master_id',
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
                    var masterId =$(this).parent('td').parent('tr').attr('id');
                    var url = '';
                    if(selectOption=='editPrepay'){ //修改預繳清單
                        url=  '<%=request.getContextPath()%>/backendAdmin/prepayDeductEditServlet?method=editPrepay&masterId=' + masterId ;
                    } else if(selectOption=='viewDeduct'){ //查看扣抵清單
                        url=  '<%=request.getContextPath()%>/backendAdmin/prepayDeductEditServlet?method=viewDeduct&masterId=' + masterId ;
                    }
                    MM_openBrWindow(url,1000,600);
                });
            }

        });

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
    });


    function formatType(cellvalue, options, rowObject) {
        
        var value = cellvalue;
        //var str = value.toString();
        if(typeof value == "undefined"){
        		value = "";
        } else {

	        if (value == 0) {
	        		value = '抽傭金額';
	        } else if (value == 1) {
	        		value = '抽成%數';
	        } else if (value == 2) {
        		value = '每筆代收';

	        }
        }
        return value;

    }

    function setting(cellvalue, options, rowObject) {
        var value = cellvalue;
        var id = rowObject['company_id'];
        var str ='';
        str+= '<select name="fieldName" class="forms_Dropdown">' ;
        str+= '<option value="editPrepay">修改預繳清單</option>' ;
        str+= '<option value="viewDeduct">查看扣抵清單</option>' ;
        str+= '</select>';
        str+='<a href="#"  title="確定" class="butbox2"><span>確&nbsp;&nbsp;定</span></a>';
        return str;

    }


    $( "#send" ).click(function() {
        var userCompanyId = $("#userCompanyId").val();
        var sdata = setSearchValue("companyId", encodeURIComponent(userCompanyId));
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

    //新增使用預用金的用戶
    $("#create").click(function () {
        //新增一筆prepay_deduct_master
        var companyId = $("#userCompanyId").val();
        var url = "<%=request.getContextPath()%>/backendAdmin/prepayDeductSearchServlet?method=createPdm";
        $.getJSON(url, {companyId: companyId}, function (data) {
            alert(data);
        });
    });



</script>