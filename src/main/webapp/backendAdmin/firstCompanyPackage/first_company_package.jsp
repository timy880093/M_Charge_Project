<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.form.min.js" ></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/backendAdmin/js/jquery.blockUI.js" ></script>
已選擇檔案:<br />
<div style="width: 1000px; height: 400px; overflow: auto; border: 2px inset #000000;" data-id="fileContainer">
    <ul style="list-style: none; padding: 0px;">
    </ul>
</div>

<form name="Form1" id="Form1" method="POST" action="<%=request.getContextPath()%>/backendAdmin/uploadServlet"
      enctype="multipart/form-data">
    <table>
        <tr>
            <td>
                <label>1:瀏覽/上傳 入帳excel格式(客戶資料template的excel).xls</label>
            </td>
            <td>
                <input name="FILE" type="file" id="FILE" style="width: 300px;"  multiple="multiple" data-id="fileUpload">
                <input type="hidden" id="fileName" />
                <input type="hidden" id="oriFilename" />
            </td>
        </tr>
        <tr>
            <td>
                <label>2:匯入入帳資料</label>
            </td>
            <td>
                <input type="button" id="importBtn" value="匯入入帳資料" onclick="firstCompPkgImport()">
            </td>
        </tr>

    </table>
</form>
<div id="loadingIMG" style="display:none">資料處理中，請稍後。</div>
<table id="table-batchItem" cellspacing="0" cellpadding="0" border="0"  class="imglist" style="display: none;width: 500px" >

    <th>公司統編</th>
    <th>發票類型</th>
    <th>年月</th>
    <th>起始號碼</th>
    <th>結束號碼</th>
    <tr id="batch-base-tr">
        <td>
            <label name="businessNo"></label>
        </td>
        <td>
            <label name="invoiceType"></label>
        </td>
        <td>
            <label name="yearMonth"></label>
        </td>
        <td>
            <label name="startNo"></label>
         </td>
        <td>
            <label name="endNo"></label>
        </td>
    </tr>
</table>


<script type="text/javascript">

    $(function(){

        $('input[type=file]').on('change', function(){
            if (event.target.files.length > 0) {
                $("div[data-id='fileContainer'] > ul").empty();

                for (var i = 0; i < event.target.files.length; i++) {

                    var file = event.target.files[i];

                    $("div[data-id='fileContainer'] > ul").append(
                            $("<li style='padding: 3px' />").html("檔名: " + file.name + " 大小: " + file.size + " 類型: " + file.type)
                    );

                }
            }
            else {
                $("div[data-id='fileContainer'] > ul").empty();
            }


            $("#Form1").ajaxSubmit(
                    {
                        beforeSubmit: function(){},
                        success: function(resp,st,xhr,$form) {
                            var respJson = JSON.parse(resp);
                            $('#fileName').val(respJson.fileName);
                            $("#oriFilename").val(respJson.oriFilename);
                        },
                        error:function(xhr, ajaxOptions, thrownError){
                            alert(xhr.status+'/'+thrownError);
                        }
                    });
        });

        $(document).ajaxStart(function () {
            $.blockUI();
        });

        $(document).ajaxStop(function () {
            $.unblockUI();
        });
    })


    //匯入客戶資料
    function firstCompPkgImport(){
        var fileName = $('#fileName').val();
        var oriFilename = $("#oriFilename").val();
        var extension = fileName.split('.').pop();
//        if(extension!='csv'){
//            alert("只允許上傳csv檔");
//            return;
//        }

        $.ajax({
            url: '<%=request.getContextPath()%>/backendAdmin/firstCompanyPackageAjaxServlet',
            data: {method:'import1',fileName:fileName,oriFilename:oriFilename},
            type:"POST",
            dataType:'json',

            beforeSend:function(){

            },
            complete:function(){

            },
            success: function(datalist){
                $("div[data-id='fileContainer'] > ul").empty();
                $.each(datalist.importList, function(i, message) {
                    $("div[data-id='fileContainer'] > ul").append(message);
                });
                alert('匯入客戶資料成功!');
                $("#FILE").replaceWith($("#FILE").val('').clone(true));
                $("#fileName").val('');
                $("#oriFilename").val('');
                //$("div[data-id='fileContainer'] > ul").empty();
                $('tr').addClass('even');
                $('th').addClass('even');

            },

            error:function(xhr, ajaxOptions, thrownError){

                alert(xhr.status);
                alert(thrownError);
            }
        });
    }


</script>
