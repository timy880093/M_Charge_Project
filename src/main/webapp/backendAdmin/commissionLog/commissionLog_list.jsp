<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
<form action="<%=request.getContextPath()%>/backendAdmin/commissionLogSearchServlet" method="post" id="commisionLogForm" name="commisionLogForm">
<tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="left"><span class="textstyle_06">佣金計算管理列表</span></td>
            <td align="right">
                經銷商
                <select id="dealerCompany" name="dealerCompany">
                </select>
                入帳時間起
                <input type="text" name="inDateS" id="inDateS" size="10"/>
                ~
                入帳時間迄
                <input type="text" name="inDateE" id="inDateE" size="10"/>
                <a href="#" id="search" title="查詢" class="butbox"><span>查詢</span></a>
                <a href="#" id="calCommission" title="計算" class="butbox"><span>計算佣金</span></a>
                <a href="#" id="payCommission" title="付款" class="butbox"><span>佣金付款</span></a>
                <a href="#" id="exportCom" title="匯出" class="butbox"><span>匯出佣金資料</span></a>
               </td>
        </tr>
    </table></td>
</tr>
</form>

<tr>
    <td><div class="content" title="主要區域" style=" width:100%; ">
        <div class="spacer"></div>
        <table id="jqgrid" width="95%" border="0" cellspacing="0" cellpadding="0">
        </table>
        <div id="gridPager"></div>
    </div></td>
</tr>


<script type="text/javascript">
    var path = '<%=request.getContextPath()%>';
    $(document).ready(function () {
        var myGrid = jQuery("#jqgrid").jqGrid({
            url: path + '/backendAdmin/commissionLogSearchServlet?method=search',
            datatype: "json",
            colNames: ['commission_log_id','所屬經銷商','入帳時間起','入帳時間迄','入帳金額(含稅)', '佣金類型', '抽佣%數','佣金金額', '佣金付款狀態','備註','設定','commission_log_id'],
            colModel: [
                {name: 'commission_log_id', index: 'commission_log_id', hidden: true, key: true, hidedlg: true},
                {name: 'dealer_company_name', index: 'dealer_company_name'},
                {name: 'in_date_start', index: 'in_date_start', formatter:formatDate},
                {name: 'in_date_end', index: 'in_date_end', formatter:formatDate},
                {name: 'in_amount', index: 'in_amount'},
                {name: 'commission_type', index: 'commission_type',formatter:formatType},
                {name: 'main_percent', index: 'main_percent'},
                {name: 'commission_amount', index: 'commission_amount'},
                {name: 'is_paid', index: 'is_paid',formatter:formatStatus},
                {name: 'note', index: 'note', formatter:formatNote},
                {name: 'commission_log_id', index: 'commission_log_id',formatter:setting, width: '250px', align: 'center' },
                {name: 'commission_log_id', index: 'commission_log_id',hidden: true}
            ],
            rowNum: 100,
            rowList: [100, 200, 300],
            pager: '#gridPager',
            sortname: 'commission_log_id',
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
                    var id =$(this).parent('td').parent('tr').find('td:eq(2)').text();
                    var note = $(this).parent('td').parent('tr').find('td:eq(11)').find("#note").val();

                    var url = '';
                    if(selectOption=='editCommission'){
                        url=  path + '/backendAdmin/commissionLogEditServlet?method=updateNote&commission_log_id=' + id +"&note="+note ;
                        $.getJSON( url, function( data ) {
                            alert(data);
                        });
                        window.location.reload();
                    }else if(selectOption=='viewLogDetail'){
                        url=  path + '/backendAdmin/commissionLogEditServlet?method=edit&commission_log_id=' + id ;
                        MM_openBrWindow(url,1200,800);
                    }else if(selectOption=='delCommission')
                    {
                        $.ajax({
                            url: "<c:url value='/backendAdmin/CommissionLogEditServlet?method=delete&commission_log_id=' />",
                            datatype: "json",
                            data: {
                                download_id: commissionlogid
                            },
                            success: function (data, textStatus, jqXHR) {
                                var response = jQuery.parseJSON(data);
                                if(response['status']){
                                    alert("刪除成功");
                                    $('#CUdialog').dialog("close");
                                    location.href="<c:url value='/backendAdmin/CommissionLogEditServlet'/>";
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                alert("無法刪除檔案");
                            }
                        });
                    }


                });
            }

        });

        $("#inDateS").datepicker({
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            changeYear: true,
            showOn: "button"
        });
        $("#inDateE").datepicker({
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            changeYear: true,
            showOn: "button"
        });


        $(document).ajaxStart(function () {
            $.blockUI();
        });

        $(document).ajaxStop(function () {
            $.unblockUI();
        });

    });

    if (${!empty REQUEST_SEND_OBJECT_0}) {
        //經銷商下拉選單
        var select =   $("#dealerCompany");
        var data = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
        select.show();
        select.empty();
        select.append($('<option></option>').val('').html('請選擇'));
        $.each(data,function(index,value){
            select.append($('<option></option>').val(value.dealer_company_id).html(value.dealer_company_name));

        })
    }

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

    function formatDate(cellvalue, options, rowObject) {
        var value = cellvalue;
        var str = value.toString().substring(0,10);
        return str;
    }

    function formatStatus(cellvalue, options, rowObject) {
        var value = cellvalue;
        var str = "";
        switch(value){
            case '1':str='已付款'; break;
            default :str='未付款'; break;
        }
        return str;
    }

    function formatNote(cellvalue, options, rowObject){
//        alert("cellvalue="+cellvalue+",options="+optins+",rowObject="+rowObject);
        var str = "";
        if(cellvalue == undefined){
            str="<input type='text' id=note name=note size='15'/>";
        }else{
            str="<input type='text' id=note name=note value='"+cellvalue+"' size='15'/>";
        }
        return str;
    }

    function setting(cellvalue, options, rowObject) {
        var value = cellvalue;
        var id = rowObject['commission_log_id'];
        var str ='';
        str = '<select name="fieldName" class="forms_Dropdown">' ;
        str+= '<option value="viewLogDetail">檢視佣金明細</option>' ;
        str+= '<option value="editCommission">修改備註</option>';
        str+= '<option value="delCommission">刪除</option>';
        str+= '</select>';
        str+='<a href="#"  title="確定" class="butbox2"><span>確&nbsp;&nbsp;定</span></a>';

        return str;

    }

    $("#search").click(function () {
        var dealerCompany = $("#dealerCompany").val();
        var inDateS = $("#inDateS").val();
        var inDateE = $("#inDateE").val();
        var sdata = setSearchValue("dealerCompany", encodeURIComponent(dealerCompany), "inDateS", encodeURIComponent(inDateS), "inDateE", encodeURIComponent(inDateE));
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1, search: true
        }).trigger("reloadGrid");
    });

    $( "#send" ).click(function() {
        var pv = $("#searchField").val();
        var pn = $("#searchString").val();
        var package_type = $("#package_type").val();
        var status = $("#status").val();
        var searchValue ;
        if(pv=='package_name'){
            searchValue = encodeURIComponent(pn);
        } else if(pv=='package_type'){
            searchValue = package_type;
        } else{
            searchValue = status;
        }
        var sdata = setSearchValue(pv, searchValue);
        $("#jqgrid").jqGrid("setGridParam", {
            datatype: "json", postData: sdata, page: 1,search:true
        }).trigger("reloadGrid");
    });

    function changeQtype(qtype) {
        if (qtype == 'package_name') {
            $('#searchString').show();
            $('#package_type').hide();
            $('#status').hide();
        } else if (qtype == 'package_type'){
            $('#searchString').hide();
            $('#package_type').show();
            $('#status').hide();
        }  else{
            $('#searchString').hide();
            $('#package_type').hide();
            $('#status').show();
        }
    }

    $("#calCommission").click(function(){
        var dealerCompany = $("#dealerCompany").val();
        if(dealerCompany == ""){
            alert("請選擇經銷商!!");
            return false;
        }

        //alert($("#inDateS").val());
        //alert($("#inDateE").val());
        var inDateS = $("#inDateS").val();
        var inDateE = $("#inDateE").val();

        if(inDateS=="" || inDateE==""){
            alert("請填寫時間起迄!!");
            return false;
        }

        var url = path + "/backendAdmin/commissionLogSearchServlet?method=calCommission&dealerCompany=" + dealerCompany+"&inDateS="+inDateS+"&inDateE="+inDateE;
        alert("url:  "+url);
        $.getJSON(url, function (data) {
            alert(data);
            window.location.reload();
        })
    });

    $("#payCommission").click(function(){
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        if (_aIDs.length > 0) {
            var commissionLogAry = new Array(_aIDs.length);
            for (var i=0; i < _aIDs.length; i++) {
                var id = _aIDs[i];
                var row = $("#jqgrid").jqGrid('getRowData', id);
//                alert("勾選[" + (i+1) + "]=" + row.company_id + "," + row.package_id);
                var commissionLog = new Object();
                commissionLog.commissionLogId = row.commission_log_id;
                commissionLogAry[i] = commissionLog;
            }
            var commissionLogJson = JSON.stringify(commissionLogAry);
          //  alert(commissionLogJson);
            var url = path + "/backendAdmin/commissionLogSearchServlet?method=payCommission&commissionLog=" + commissionLogJson;
            $.getJSON(url, {commissionLog: commissionLogAry}, function (data) {
                alert(data);
                window.location.reload();
            })
        } else {
            alert("請先勾選資料列。");
        }
    });


    $("#exportCom").click(function(){
        //取出被選擇的列
        var _aIDs = $("#jqgrid").jqGrid('getGridParam','selarrrow');
        var ids = [];
        for (var i = 0; i < _aIDs.length; i++) {
            ids.push($("#jqgrid").jqGrid('getRowData', _aIDs[i]).commission_log_id);
        }

        if (_aIDs.length > 0) {
            var url = path + "/backendAdmin/commissionLogSearchServlet?method=exportCom&commissionLogIdArray="+_aIDs;
            location.href = url;
        } else {
            alert("請先勾選資料列。");
        }
    });




</script>