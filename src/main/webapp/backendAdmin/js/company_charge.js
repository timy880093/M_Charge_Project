/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/10/6
 * Time: 下午 5:52
 * To change this template use File | Settings | File Templates.
 */
function bindEvent(){




    $("#viewCompanyInfo").click(function(){
        var companyId = $("#companyId").val();
        var url = path+'/backendAdmin/companyEditServlet?method=edit&companyId=' + companyId+"&type=1";
        window.open(url, '', config='height=1000,width=800');
    });

    $("#brokeCompanyType").change(function(){

        var type = $(this).parent('td').find('select').val();
        var select =   $("#brokeCompany");
        var selectId =   $("#brokerId");
        var url = path+"/backendAdmin/chargeAjaxServlet?method=getCompanyListByType";
        $.getJSON(url,{type:type}, function( data ){
            if(type=='1'){

                selectId.empty();
                selectId.append($('<option></option>').val('-1').html('請選擇'));
                $.each(data,function(index,value){
                    selectId.append($('<option></option>').val(value.user_id).html(value.name));

                })
                select.hide();
            }else{
                selectId.empty();
                selectId.append($('<option></option>').val('-1').html('請選擇'));
                select.show();
                select.empty();
                select.append($('<option></option>').val('-1').html('請選擇'));
                $.each(data,function(index,value){
                    select.append($('<option></option>').val(value.company_id).html(value.company_name));

                })
            }

        })
    });

    $("#brokeCompany").change(function(){
        var type = $("#type").val();
        var select = $("#brokerId");
        select.empty();
        select.append($('<option></option>').val('-1').html('請選擇'));
        var companyId = $(this).parent('td').find('select:eq(1)').val();
        var url = path+"/backendAdmin/chargeAjaxServlet?method=getBrokerListByCompany&type="+type;
        $.getJSON(url,{companyId:companyId}, function( data ){
            $.each(data,function(index,value){
                select.append($('<option></option>').val(value.broker_id).html(value.user_name));

            })
        })
    });



}

function viewChargeDetail(company_id,package_id, package_type){
    var type = $("#type").val();
    var url = path+"/backendAdmin/companyChargeEditServlet?method=read&companyId="+company_id+"&packageId="+package_id+"&type="+package_type;
    window.open(url, '', config = 'width=800');
}