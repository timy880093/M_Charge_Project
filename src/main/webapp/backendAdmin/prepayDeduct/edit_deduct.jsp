<form action="<%=request.getContextPath()%>/backendAdmin/dealerCompanyEditServlet?method=insert" method="post"
      id="dealerCompanyForm">
    <input type="hidden" id="dealerCompanyId" name="dealerCompanyId"/>
    <table border="1" >
        <tr>
            <td>
                <p style="color: brown">扣抵表</p>
            </td>
        </tr>

        <tr>
            <td>
                <table cellspacing="0" cellpadding="0" border="0" class="imglist">
                    <tr>
                        <td>
                            <table cellspacing="0" cellpadding="0" border="0" class="imglist" id="deductHistory">
                                <tr>
                                    <td>繳款(計算)年月</td>
                                    <td>預繳/扣款金額</td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>
            </td>
        </tr>
    </table>


</form>

<script type="text/javascript">
    var path = '<%=request.getContextPath()%>';

    $(function () {


        var status;
        if (${!empty REQUEST_SEND_OBJECT_1}) {
            var histVOList = $.parseJSON('${REQUEST_SEND_OBJECT_1}');
            console.log("histVOList="+histVOList);
            $("#cal_ym").val(histVOList.cal_ym);
            $("#money").val(histVOList.money);
            $.each(histVOList, function (i, element) {
                var status = "";
                switch(element.status) {
                    case 1:
                        status = "未繳"; break; //status = "生效";原本的生效是指合約建立，但這裡是預繳資料建立。
                    case 2:
                        status = "作廢"; break;
                    case 3:
                        status = "出帳"; break;
                    case 4:
                        status = "入帳"; break;
                    case 5:
                        status = "佣金"; break;
                    default:
                        break;
                }
                $("#deductHistory").append(
                    "<tr><td>"+element.cal_ym+"</td><td>"+element.money+"</td></tr>");
            });

        } else{
            $("#title").text('新增經銷商方案');
            $("#send").val('建立');
        }
    });

</script>