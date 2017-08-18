<form id="settleForm" name="settleForm" method="post"
      action="<%=request.getContextPath()%>/backendAdmin/companyChargeEditServlet?method=settleUpdate&type=1">
  <table cellspacing="0" cellpadding="0" border="0" class="imglist">
    <input type="hidden" id="package_id">
    <tr>
      <td colspan="4">
        <p style="color: brown">合約結清設定</p>
      </td>
    </tr>
    <tr>
      <td colspan="1">方案名稱:</td>
      <td colspan="3"><input type="text" id="package_name" value="" disabled></td>
    </tr>
    <tr>
      <td>實際起始日：</td>
      <td><input type="text" id="real_start_date" value="" disabled/></td>
      <td>實際結束日：</td>
      <td>
        <input type="text" id="real_end_date"  value="" readonly/>
        <div style="color: red; font-size: 8px;" >結清的實際結束日會影響，該公司下一份合約可選擇的實際起始日，請三思!!</div>
      </td>
    </tr>
    <tr>
      <td>計算起始日：</td>
      <td><input type="text" id="start_date" value="" disabled/></td>
      <td>計算結束日：</td>
      <td>
        <input type="text" id="end_date"  value=""/>
        <a href="#" id="tryCal" title="結清計算" class="butbox"><span>結清計算</span></a>
      </td>
    </tr>
    <tr>
      <td colspan="4">
        結清資料：
        <table width="100%" border="1" id="tabTryCalResult">
          <tr>
            <td>已繳月租費</td>
            <td>已使用月租費</td>
            <td>剩餘月租費</td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="4" align="center">
        <input type="button" id="doSettle" value="確定結清">
        <input type="button" id="close" onclick="window.close()" value="關閉">
      </td>
    </tr>
    <tr>
      <td colspan="4">&nbsp;</td>
    </tr>
  </table>
 </form>

<script type="text/javascript">
  $(function(){
    if (${!empty REQUEST_SEND_OBJECT_0}) {
      var settleInfo = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
      $("#package_id").val(settleInfo.package_id);
      $("#package_name").val(settleInfo.package_name);
      $("#real_start_date").val(settleInfo.real_start_date);
      $("#real_end_date").val(settleInfo.real_end_date);
      $("#start_date").val(settleInfo.start_date);
      $("#end_date").val(settleInfo.end_date);
      $("#end_date").datepicker("option", "maxDate", ""+settleInfo.end_date+"");
    }
  });

  $("#end_date").datepicker({
    dateFormat: 'yy-mm-dd',
    changeMonth: true,
    changeYear: true,
    showOn: "button",
    //minDate: 0,
    onClose: function (selectedDate) {
      var real_start_date = $("#real_start_date").val();
      var day = real_start_date.substring(8);
      var newEndDate = selectedDate.substring(0,8) + day;
      var endDate = new Date(newEndDate);
      if("01" == day){
        endDate.setMonth(endDate.getMonth()+1);
      }else{
        endDate.setMonth(endDate.getMonth()+1);
      }
      endDate.setDate(endDate.getDate()-1);
      $("#real_end_date").val(dateParseToStr(endDate));
    }
  });

  $("#tryCal").click(function(){
    $.ajax({
      url: "<%=request.getContextPath()%>/backendAdmin/companyChargeAjaxServlet?method=getCycleTryCalSettle&packageId=" + $("#package_id").val() + "&endDate=" + $("#end_date").val(),
      type: "POST",
      dataType: 'json',
      success: function (data) {
        $("#tabTryCalResult").empty();
        $("#tabTryCalResult").append("<tr> <td>已繳月租費</td> <td>已使用月租費</td> <td>剩餘月租費</td> </tr>");
        $("#tabTryCalResult").append('<tr>' +
                '<td>' + data.paidMonth +'</td>'+
                '<td>' + data.usedMonth + '</td>' +
                '<td>' + data.remainderMonth +'</td></tr>');
      },
      error: function (xhr, ajaxOptions, thrownError) {
        alert(xhr.status);
        alert(thrownError);
      }
    });
  });

  //結清:更新real_end_date和end_date，並且把bill_cylce裡end_date後的bill全部作廢，cash_detail裡的end_date後的cash也要全部作廢
  $("#doSettle").click(function(){
    if(isMaxDate()){
      if ( confirm ("結清後就無法回愎，是否確定結清? ") ){
        $.ajax({
          url: "<%=request.getContextPath()%>/backendAdmin/companyChargeAjaxServlet?method=doSettle&type=1&packageId=" + $("#package_id").val() + "&endDate=" + $("#end_date").val() + "&realEndDate=" + $("#real_end_date").val(),
          type: "POST",
          dataType: 'json',
          success: function (data) {
            alert('結清成功!!');
//          alert(data);
            window.opener.location.reload();
            window.close();
          },
          error: function (xhr, ajaxOptions, thrownError) {
            alert(xhr.status);
            alert(thrownError);
          }
        });
      }
    }
  });

  function isMaxDate(){
    var endDate= $("#end_date").val();
    var maxDate = getLastDay(endDate.substring(0,4), endDate.substring(5,7));
    var maxDateOfEndDate = endDate.substring(8,10);
    if(maxDateOfEndDate == maxDate){
      return true;
    }
    alert('計算結束日期請選擇當月最後一日!!');
    return false;
  }

  function getLastDay(year,month)
  {
    var new_year = year;  //取當前的年份
    var new_month = month++;//取下一個月的第一天，方便計算（最後一天不固定）
    if(month>12)      //如果當前大於12月，則年份轉到下一年
    {
      new_month -=12;    //月份減
      new_year++;      //年份增
    }
    var new_date = new Date(new_year,new_month,1);        //取當年當月中的第一天
    return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//獲取當月最後一天日期
  }

  function dateParseToStr(endDate){
    var result = "";
    result += endDate.getFullYear()+"-";
    if((endDate.getMonth()+1)<10){
      result += "0" + (endDate.getMonth()+1)+"-";
    }else{
      result += (endDate.getMonth()+1)+"-";
    }
    if((endDate.getDate()+1)<10){
      result += "0" + endDate.getDate();
    }else{
      result += endDate.getDate();
    }

    return result;
  }

</script>