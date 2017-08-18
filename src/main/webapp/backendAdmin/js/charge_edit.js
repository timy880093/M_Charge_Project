function bindEvent(){
    $.extend($.validator.messages, {
        number: '請填寫數字!',
        date: '請填寫日期!'
    });

    $('tr').addClass('even');
    $('th').addClass('even');

    $("#close").click(function () {
        window.close();

    });

    $("#stopCharge").click(function(){
        $.ajax({
            url: path+"/backendAdmin/chargeAjaxServlet?method=closeCharge",
            dataType: 'json',
            data: {type: $("#type").val(),chargeId:$("#chargeId").val()},
            async: false,
            success: function (data) {
                alert("已暫停!");
                location.reload();
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert('doSubmit()-'+xhr.status+'/'+thrownError);
            }
        });
    });

    $("#openCharge").click(function(){
        $.ajax({
            url: path+"/backendAdmin/chargeAjaxServlet?method=openCharge",
            dataType: 'json',
            data: {type: $("#type").val(),chargeId:$("#chargeId").val()},
            async: false,
            success: function (data) {
                alert("已開啟!");
                location.reload();
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert('doSubmit()-'+xhr.status+'/'+thrownError);
            }
        });
    });
}


function invalidEdit(){
    var $allInput = $(".imglist input,select");
    $allInput.prop('disabled',true);
    $("#close").prop('disabled',false);
    $("#send").hide();
    $(".ui-datepicker-trigger").hide();
}