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
        <td align="left"><span class="textstyle_06">介紹裝機公司匯出</span></td>
        <td align="left">
                <span class="select_div">
                    <form action="<%=request.getContextPath()%>/backendAdmin/brokerCompanySearchServlet?method=" method="post" id="brokerCompanyForm" name="brokerCompanyForm">
                      <select name="brokerType" id="brokerType" class="forms_Dropdown">
                        <option value="">請選擇</option>
                        <option value="2">介紹公司</option>
                        <option value="3">裝機公司</option>
                      </select>
                      <input type="text" id="brokerCompany" name="brokerCompany" /><input type="button" value="..." id="allBrokerCompany">
                      <a href="#" id="send" title="查詢" class="butbox"><span>查&nbsp;&nbsp;詢</span></a>
                      <a href="#" id="excel" title="匯出" class="butbox"><span>匯出</span></a>
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
    //var sdata = searchCriteria();

    var myGrid = jQuery("#jqgrid").jqGrid({
      url: '<%=request.getContextPath()%>/backendAdmin/brokerCompanySearchServlet?method=search',
      datatype: "json",
      colNames: ['package_id', '公司名稱', '統編', '實際起始日','實際結束日','方案名稱','經銷商','經銷商業務','介紹公司','介紹人','裝機公司','裝機人'
      ],
      colModel: [
        {name: 'package_id', index: 'package_id', hidden: true, key: true, hidedlg: true},
        {name: 'name', index: 'name'},
        {name: 'business_no', index: 'business_no'},
        {name: 'real_start_date', index: 'real_start_date'},
        {name: 'real_end_date', index: 'real_end_date'},
        {name: 'package_name', index: 'package_name'},
        {name: 'dealer_company_name', index: 'dealer_company_name'},
        {name: 'dealer_name', index: 'dealer_name'},
        {name: 'broker_cp2', index: 'broker_cp2'},
        {name: 'broker2', index: 'broker2'},
        {name: 'broker_cp3', index: 'broker_cp3'},
        {name: 'broker3', index: 'broker3'}
      ],
      rowNum: 100,
      rowList: [100, 200, 300],
      pager: '#gridPager',
      sortname: 'broker_cp2',
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
      },
    });

    var broker2Data = [];
    var broker2List;
    if (${!empty REQUEST_SEND_OBJECT_0}) {
      broker2List = $.parseJSON('${REQUEST_SEND_OBJECT_0}');

      broker2Data =
              Enumerable.From(broker2List)
                      .Select(function(o) { return {
                        label: o.broker_cp2,
                        value: o.broker_cp2
                      };}).ToArray();
    };

    var broker3Data = [];
    var broker3List;
    if (${!empty REQUEST_SEND_OBJECT_1}) {
      broker3List = $.parseJSON('${REQUEST_SEND_OBJECT_1}');

      broker3Data =
              Enumerable.From(broker3List)
                      .Select(function(o) { return {
                        label: o.broker_cp3,
                        value: o.broker_cp3
                      };}).ToArray();
    };



    $("#brokerCompany").blur(function(){
      if($("#brokerCompany").val()==""){
        $("#brokerCompanyId").val("");
      }
    });

    $("#allBrokerCompany").on("click",function(){
      $("#brokerCompany").autocomplete("search", "");
    });

    $("#brokerType").change(function(){
      var brokerType = $("#brokerType").val();
      if(brokerType != ""){
        if(brokerType == "2"){
          $("#brokerCompany").autocomplete({
            source: broker2Data,
            minLength:0,
            select: function(event, ui) {

            }
          });
        }else if(brokerType == "3"){
          $("#brokerCompany").autocomplete({
            source: broker3Data,
            minLength:0,
            select: function(event, ui) {

            }
          });
        }
      }else{
        $("#brokerCompany").autocomplete({
          source: null,
          minLength:0,
          select: function(event, ui) {
          }
        });
      }
    });

    //查詢
    $( "#send" ).click(function() {
      var pv = $("#brokerType").val();
      var pn = $("#brokerCompany").val();
      var sdata = setSearchValue(pv, encodeURIComponent(pn));
      $("#jqgrid").jqGrid("setGridParam", {
        datatype: "json", postData: sdata, page: 1,search:true
      }).trigger("reloadGrid");
    });

    //匯出excel
    $("#excel").click(function(){
      var brokerType = $("#brokerType").val();
      var brokerCompany = $("#brokerCompany").val();
      var url = path + '/backendAdmin/brokerCompanySearchServlet?method=excel&brokerType='+brokerType+"&brokerCompany="+encodeURIComponent(brokerCompany);
      location.href = url;
    });

    $(document).ajaxStart(function () {
      $.blockUI();
    });

    $(document).ajaxStop(function () {
      $.unblockUI();
    });

  });


  </script>