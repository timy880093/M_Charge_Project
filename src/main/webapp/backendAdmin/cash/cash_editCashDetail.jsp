<form action="<%=request.getContextPath()%>/backendAdmin/cashEditServlet?method=updateCashDetail" method="post" id="cashEditForm">
  <table cellspacing="0" cellpadding="0" border="0" class="imglist">
    <input type="hidden" id="cashMasterId" name="cashMasterId" value="">
    <input type="hidden" id="updateCashDetailId" name="updateCashDetailId" value="">
    <input type="hidden" id="updateDiffPrice" name="updateDiffPrice" value="">
    <input type="hidden" id="updateDiffPriceNote" name="updateDiffPriceNote" value="">
    <tr>
      <td colspan="13">
        <p style="color: brown">帳單名細</p>
      </td>
    </tr>
    <tr>
      <td colspan="30">
        <table cellspacing="0" cellpadding="0" border="0" class="imglist" id="cashDetailList">
          <tr>
            <th>公司名稱</th>
            <th>狀態</th>
            <th>繳費類型</th>
            <th>方案名稱</th>
            <th>計算年月</th>
            <th>出帳年月</th>
            <th>計算金額</th>
            <th>差額</th>
            <th>差額備註</th>
            <th>未稅金額</th>
            <th>稅別</th>
            <th>稅率</th>
            <th>稅額</th>
            <th>含稅金額</th>
            <th>功能</th>
          </tr>
        </table>
      </td>
    </tr>

    <tr>
      <td colspan="13" align="center">
        <input type="button" id="send" value="關閉">
        <input type="button" id="delCashMaster" value="刪除帳單(註:帳單為空才可刪除)">
      </td>
    </tr>
    <tr>
      <td colspan="13">&nbsp;</td>
    </tr>
    <tr>
      <th >建立時間：</th>
      <td ><label><span id="createDate"></span></label></td>
      <th >建立人員：</th>
      <td ><label><span id="creator"></span></label></td>
    </tr>
    <tr>
      <th >修改時間：</th>
      <td ><label><span id="modifyDate"></span></label></td>
      <th >修改人員：</th>
      <td ><label><span id="modifier"></span></label></td>
    </tr>
  </table>
</form>
<script type="text/javascript">
  var path = '<%=request.getContextPath()%>';

  $(function () {

    if (${!empty REQUEST_SEND_OBJECT_0}) {
      var cashDetailList = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
      $.each(cashDetailList, function (i, element) {
        if(element.diffPriceNote == null){
          element.diffPriceNote = "";
        }

        if(element.cashType == 1){ //月租
            if(element.detailStatus == '作廢' || element.masterStatus >= 3){
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                        '<td>'+ element.detailStatus +'</td>'+
                        '<td>'+ element.cashTypeName +'</td>'+
                        '<td>'+ element.packageName +'<br>' + element.period +'</td>'+
                        '<td>'+ element.calYm +'</td>'+
                        '<td>'+ element.outYm +'</td>'+
                        '<td>'+ element.orgPrice +'</td>'+
                        '<td>'+ element.diffPrice +'</td>'+
                        '<td>'+ element.diffPriceNote +'</td>'+
                        '<td>'+ element.noTaxInclusivePrice +'</td>'+
                        '<td>'+ element.taxTypeName +'</td>'+
                        '<td>'+ element.taxRate +'</td>'+
                        '<td>'+ element.taxPrice +'</td>'+
                        '<td>'+ element.taxInclusivePrice +'</td>'+
                        "<td><input type='hidden' id='cashDetailId' value='"+element.cashDetailId+"'/></td>");
            }else{
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                        '<td>'+ element.detailStatus +'</td>'+
                        '<td>'+ element.cashTypeName +'</td>'+
                        '<td>'+ element.packageName +'<br>' + element.period +'</td>'+
                        '<td>'+ element.calYm +'</td>'+
                        '<td>'+ element.outYm +'</td>'+
                        '<td>'+ element.orgPrice +'</td>'+
                        "<td><input type='number' id='diffPrice' name='diffPrice' value='" + element.diffPrice +"' /></td>"+
                        "<td><input type='text' id='diffPriceNote' name='diffPriceNote' value='" + element.diffPriceNote + "'/></td>"+
                        '<td>'+ element.noTaxInclusivePrice +'</td>'+
                        '<td>'+ element.taxTypeName +'</td>'+
                        '<td>'+ element.taxRate +'</td>'+
                        '<td>'+ element.taxPrice +'</td>'+
                        '<td>'+ element.taxInclusivePrice +'</td>'+
                        "<td><input type='hidden' id='cashDetailId' value='"+element.cashDetailId+"'/>" +
                        "<a href=#  title=修改 class=butbox2 id='update"+ element.cashDetailId +"'><span>修改</span></a></td>");
            }

        }else if(element.cashType == 2){ //超額
            if(element.detailStatus == '作廢' || element.masterStatus >= 3){
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                        '<td>'+ element.detailStatus +'</td>'+
                        '<td>'+ element.cashTypeName +'</td>'+
                        '<td>'+ element.packageName +'<br>' + element.period +'</td>'+
                        '<td>'+ element.calYm +'</td>'+
                        '<td>'+ element.outYm +'</td>'+
                        '<td>'+ element.orgPrice +'</td>'+
                        '<td>'+ element.diffPrice +'</td>'+
                        '<td>'+ element.diffPriceNote +'</td>'+
                        '<td>'+ element.noTaxInclusivePrice +'</td>'+
                        '<td>'+ element.taxTypeName +'</td>'+
                        '<td>'+ element.taxRate +'</td>'+
                        '<td>'+ element.taxPrice +'</td>'+
                        '<td>'+ element.taxInclusivePrice +'</td>'+
                        "<td><input type='hidden' id='cashDetailId' value='"+element.cashDetailId+"'/><br>"+
                        "<a href=#  title=檢視超額明細 class=butbox2 id='overList"+ element.cashDetailId +"'><span>檢視超額明細</span></a></td>");
            }else{
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                        '<td>'+ element.detailStatus +'</td>'+
                        '<td>'+ element.cashTypeName +'</td>'+
                        '<td>'+ element.packageName +'<br>' + element.period +'</td>'+
                        '<td>'+ element.calYm +'</td>'+
                        '<td>'+ element.outYm +'</td>'+
                        '<td>'+ element.orgPrice +'</td>'+
                        "<td><input type='number' id='diffPrice' name='diffPrice' value='" + element.diffPrice +"' /></td>"+
                        "<td><input type='text' id='diffPriceNote' name='diffPriceNote' value='" + element.diffPriceNote + "'/></td>"+
                        '<td>'+ element.noTaxInclusivePrice +'</td>'+
                        '<td>'+ element.taxTypeName +'</td>'+
                        '<td>'+ element.taxRate +'</td>'+
                        '<td>'+ element.taxPrice +'</td>'+
                        '<td>'+ element.taxInclusivePrice +'</td>'+
                        "<td><input type='hidden' id='cashDetailId' value='"+element.cashDetailId+"'/>" +
                        "<a href=#  title=修改 class=butbox2 id='update"+ element.cashDetailId +"'><span>修改</span></a><br>"+
                        "<a href=#  title=取消計算 class=butbox2 id='cancel"+ element.cashDetailId +"'><span>取消計算</span></a><br>"+
                        "<a href=#  title=檢視超額明細 class=butbox2 id='overList"+ element.cashDetailId +"'><span>檢視超額明細</span></a></td>");
            }


          $("#overList"+element.cashDetailId).click(function(){
//          alert($(this).prev().prev().val() + "," + $(this).parent('td').parent('tr').find('td:eq(6)').children("#diffPrice").val() + "," + $(this).parent('td').parent('tr').find('td:eq(7)').children("#diffPriceNote").val());
            url = path + '/backendAdmin/cashEditServlet?method=viewOrverList&cashDetailId=' + element.cashDetailId + '&billType='+element.billType;
            MM_openBrWindow(url,1600,600);
          });

            $("#cancel"+element.cashDetailId).click(function(){
                $("#cashEditForm").attr("action",path+'/backendAdmin/cashEditServlet?method=cancelOver&cashDetailId=' + element.cashDetailId + "&cashMasterId=" +$("#cashMasterId").val());
                $("#cashEditForm").submit();
            });
        } else if(element.cashType == 6) { //預繳
            if(element.detailStatus == '作廢' || element.masterStatus >= 3){
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                    '<td>'+ element.detailStatus +'</td>'+
                    '<td>預繳</td>'+
                    '<td>無</td>'+
                    '<td>'+ element.calYm +'</td>'+
                    '<td>'+ element.outYm +'</td>'+
                    '<td>'+ element.orgPrice +'</td>'+
                    '<td>'+ element.diffPrice +'</td>'+
                    '<td>'+ element.diffPriceNote +'</td>'+
                    '<td>'+ element.noTaxInclusivePrice +'</td>'+
                    '<td>營業稅</td>'+
                    '<td>'+ element.taxRate +'</td>'+
                    '<td>'+ element.taxPrice +'</td>'+
                    '<td>'+ element.taxInclusivePrice +'</td>'+
                    "<td></td>");
            }else{
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                    '<td>'+ element.detailStatus +'</td>'+
                    '<td>預繳</td>'+
                    '<td>無</td>'+
                    '<td>'+ element.calYm +'</td>'+
                    '<td>'+ element.outYm +'</td>'+
                    '<td>'+ element.orgPrice +'</td>'+
                    "<td><input type='number' id='diffPrice' name='diffPrice' value='" + element.diffPrice +"' /></td>"+
                    "<td><input type='text' id='diffPriceNote' name='diffPriceNote' value='" + element.diffPriceNote + "'/></td>"+
                    '<td>'+ element.noTaxInclusivePrice +'</td>'+
                    '<td>營業稅</td>'+
                    '<td>'+ element.taxRate +'</td>'+
                    '<td>'+ element.taxPrice +'</td>'+
                    '<td>'+ element.taxInclusivePrice +'</td>'+
                    "<td><input type='hidden' id='cashDetailId' value='"+element.cashDetailId+"'/>" +
                    "<a href=#  title=修改 class=butbox2 id='update"+ element.cashDetailId +"'><span>修改</span></a><br>" +
                    "<a href=#  title=取消 class=butbox2 id='cancel"+ element.cashDetailId +"'><span>取消</span></a></td>");

                $("#cancel"+element.cashDetailId).click(function(){
                    $("#cashEditForm").attr("action",path+'/backendAdmin/cashEditServlet?method=cancelPrepay&cashDetailId=' + element.cashDetailId + "&cashMasterId=" +$("#cashMasterId").val()) + '&billType='+element.billType;
                    $("#cashEditForm").submit();
                });

            }
        } else if(element.cashType == 7) { //扣抵
            if(element.detailStatus == '作廢' || element.masterStatus >= 3){
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                    '<td>'+ element.detailStatus +'</td>'+
                    '<td>扣抵</td>'+
                    '<td>無</td>'+
                    '<td>'+ element.calYm +'</td>'+
                    '<td>'+ element.outYm +'</td>'+
                    '<td>'+ element.orgPrice +'</td>'+
                    '<td>'+ element.diffPrice +'</td>'+
                    '<td>'+ element.diffPriceNote +'</td>'+
                    '<td>'+ element.noTaxInclusivePrice +'</td>'+
                    '<td>營業稅</td>'+
                    '<td>'+ element.taxRate +'</td>'+
                    '<td>'+ element.taxPrice +'</td>'+
                    '<td>'+ element.taxInclusivePrice +'</td>'+
                    "<td></td>");
            }else{
                $("#cashDetailList").append('<tr><td>' + element.companyName + '</td>' +
                    '<td>'+ element.detailStatus +'</td>'+
                    '<td>扣抵</td>'+
                    '<td>無</td>'+
                    '<td>'+ element.calYm +'</td>'+
                    '<td>'+ element.outYm +'</td>'+
                    '<td>'+ element.orgPrice +'</td>'+
                    "<td><input type='number' id='diffPrice' name='diffPrice' value='" + element.diffPrice +"' /></td>"+
                    "<td><input type='text' id='diffPriceNote' name='diffPriceNote' value='" + element.diffPriceNote + "'/></td>"+
                    '<td>'+ element.noTaxInclusivePrice +'</td>'+
                    '<td>營業稅</td>'+
                    '<td>'+ element.taxRate +'</td>'+
                    '<td>'+ element.taxPrice +'</td>'+
                    '<td>'+ element.taxInclusivePrice +'</td>'+
                    "<td><input type='hidden' id='cashDetailId' value='"+element.cashDetailId+"'/></td>");
            }
        }


        $("#update"+element.cashDetailId).click(function(){
          //alert($(this).prev().val() + "," + $(this).parent('td').parent('tr').find('td:eq(7)').children("#diffPrice").val() + "," + $(this).parent('td').parent('tr').find('td:eq(8)').children("#diffPriceNote").val());

          var cashDetailId = $(this).prev().val();
          //alert("cashDetailId="+cashDetailId);
          var diffPrice = $(this).parent('td').parent('tr').find('td:eq(7)').children("#diffPrice").val();
          var diffPriceNote = $(this).parent('td').parent('tr').find('td:eq(8)').children("#diffPriceNote").val();
          var cashMasterId = $("#cashMasterId").val();

          $("#updateCashDetailId").val(cashDetailId);
          $("#updateDiffPrice").val(diffPrice);
          $("#updateDiffPriceNote").val(diffPriceNote);

          $("#cashEditForm").submit();
        });
      });
      if(cashDetailList == ''){
          $("#delCashMaster").show();
      }else{
          $("#delCashMaster").hide();
      }
    };

    if (${!empty REQUEST_SEND_OBJECT_1}) {
      var cashMaster = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
      $("#cashDetailList").append("<tr> <td colspan='8' align='right'>總未稅價" + cashMaster.noTaxInclusiveAmount +
              "</td><td colspan='4' align='right'>總含稅價" + cashMaster.taxInclusiveAmount + "</td><td></td></tr>");
//      alert(cashMaster.createDate +","+cashMaster.creatorId+","+cashMaster.modifyDate+","+cashMaster.modifierId);
      $("#createDate").text(cashMaster.createDate);
      $("#creator").text(cashMaster.creator);
      $("#modifyDate").text(cashMaster.modifyDate);
      $("#modifier").text(cashMaster.modifier);
      $("#cashMasterId").val(cashMaster.cashMasterId);
    };
  });

  $("#send").click(function () {
    window.close();
  });

  //刪除帳單
  $("#delCashMaster").click(function(){
      $("#cashEditForm").attr("action",path+'/backendAdmin/cashEditServlet?method=delCashMaster&cashMasterId=' +$("#cashMasterId").val());
      $("#cashEditForm").ajaxSubmit(
          {
              beforeSubmit: function () {
              },
              success: function (resp, st, xhr, $form) {
                  alert('成功!');
                  //opener.location.reload();
                  window.close();
              },
              error: function (xhr, ajaxOptions, thrownError) {
                  alert('失敗!');
                  alert(xhr.status + '/' + thrownError);
              }
          }
      );
  });

</script>