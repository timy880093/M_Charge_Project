<form action="<%=request.getContextPath()%>/backendAdmin/calCycleEditServlet?method=updateGift" method="post" id="giftForm">
  <table cellspacing="0" cellpadding="0" border="0" class="imglist">
    <input type="hidden" id="billId" name="billId" value="">
    <input type="hidden" id="companyId" name="companyId" value="">
    <input type="hidden" id="isCalculated" name="isCalculated" value="">
    <tr>
      <td colspan="5">
        <p style="color: brown">修改贈送張數</p>
      </td>
    </tr>
    <tr>
        <th>公司名稱</th>
        <td><input type="text" id="companyName" name="companyName" disabled/></td>
    </tr>
    <tr>
        <th>計算年月</th>
        <td><input type="text" id="calYM" name="calYM" disabled/></td>
    </tr>
    <tr>
        <th>贈送張數</th>
        <td><input type="text" id="cntGift" name="cntGift"/></td>
    </tr>
    <tr>
      <td colspan="5" align="center">
          <input type="button" id="send" value="儲存" />
          <input type="button" id="close" value="關閉" onclick="window.close()" />
      </td>
    </tr>
  </table>
</form>
<script type="text/javascript">
  var path = '<%=request.getContextPath()%>';

  $(function () {

    if (${!empty REQUEST_SEND_OBJECT_0}) {
      var giftVO = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
        $("#billId").val(giftVO.billId);
        $("#companyId").val(giftVO.companyId);
        $("#companyName").val(giftVO.companyName);
        $("#calYM").val(giftVO.calYM);
        $("#cntGift").val(giftVO.cntGift);
        $("#isCalculated").val(giftVO.isCalculated);
        if($("#isCalculated").val() == "true"){
            document.getElementById( 'send' ).style.display = 'none';

        }
    }

    $("#send").click(function () {
        var billId = $("#billId").val();
        var cntGift = $("#cntGift").val();
        $.ajax({
            url: path+"/backendAdmin/calCycleEditServlet?method=updateGift&billId="+billId+"&cntGift="+cntGift,
            type: "POST",
            dataType: 'text',
            success: function (data) {
                alert('success');
                window.opener.location.reload();
                window.close();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.status);
                alert(thrownError);
            }
        });
    });
  });


</script>