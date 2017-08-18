<style>
    #block {
        height: 200px;
        width: 400px;
        position: absolute; /*絕對位置*/
        top: 50%;/*從上面開始算，下推 50% (一半) 的位置*/
        left: 50%;/*從左邊開始算，右推 50% (一半) 的位置*/
        margin-top: -100px; /*高度的一半*/
        margin-left: -200px;/*寬度的一半*/
    }
</style>

<div id="block">
    <table  style="text-align: center"  width="100%" height="100%" class="imglist">
        <tr>
            <td>選擇方案類型</td>
        </tr>
        <tr>
            <td>
                月租制：<input type="radio" id="charge_type_0" name="charge_type" checked>
                級距制：<input type="radio" id="charge_type_1" name="charge_type">
                <%--預繳制：<input type="radio" id="charge_type_1" name="charge_type">--%>
            </td>
        </tr>
        <tr>
            <td align="center">
                <input type="button" id="submit" name="submit" value="確認" onclick="chosePackage()">
                <input type="button" id="close" name="close" onclick="window.close()" value="關閉">
            </td>
        </tr>
    </table>
</div>


<script>
    function chosePackage(){

        if($("#charge_type_0").prop('checked')==true){
            location.href = "<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=edit&type=1";

        }
        else if($("#charge_type_1").prop('checked')==true){
            location.href = "<%=request.getContextPath()%>/backendAdmin/chargeEditServlet?method=edit&type=2";

        }
        else{
            alert('請選擇方案。');
        }
    }
</script>
