   <table cellspacing="0" cellpadding="0" border="0" class="imglist">
    <input type="hidden" id="cashDetailId" name="cashDetailId" value="">
    <tr>
      <td colspan="13">
        <p style="color: brown">月租型超額名細</p>
      </td>
    </tr>
    <tr>
      <td colspan="30">
        <table cellspacing="0" cellpadding="0" border="0" class="imglist" id="overList">
          <tr>
            <th>計算年月</th>
            <th>月租金額</th>
            <th>當月使用張數</th>
            <th>基張</th>
            <th>贈送張數</th>
            <th>超限(張)</th>
            <th>發票扣點單位</th>
            <th>超限應繳</th>
            <th>繳款上限</th>
            <th>超額金額</th>
          </tr>
        </table>
      </td>
    </tr>

    <tr>
      <td colspan="13" align="center">
        <input type="button" id="send" value="關閉" onclick="window.close()">
      </td>
    </tr>
  </table>
<script type="text/javascript">
  var path = '<%=request.getContextPath()%>';

  $(function () {

    if (${!empty REQUEST_SEND_OBJECT_0}) {
      var billCycleList = $.parseJSON('${REQUEST_SEND_OBJECT_0}');
      $.each(billCycleList, function (i, element) {
          $("#overList").append('<tr><td>' + element.yearMonth + '</td>' +
                  '<td>'+ element.price +'</td>' +
                  '<td>'+ element.cnt +'</td>' +
                  '<td>'+ element.cntLimit +'</td>' +
                  '<td>'+ element.cntGift +'</td>' +
                  '<td>'+ element.cntOver +'</td>' +
                  '<td>'+ element.singlePrice +'</td>' +
                  '<td>'+ element.priceOver +'</td>' +
                  '<td>'+ element.priceMax +'</td>' +
                  '<td>'+ element.payOver +'</td><tr>'
          );
      });
    };
  });

</script>