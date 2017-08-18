/**
 * Created by Eason on 1/27/2016.
 */
function lockElement(buttonName,processingMessage,originalMessage,lockState){

    $(buttonName).attr("disabled", lockState);
    if(lockState==true){
        $(buttonName).attr('value', processingMessage);
    }
    if(lockState==false){
        $(buttonName).attr('value', originalMessage);
    }
}

function lockFactory(buttonName,processingMessage,originalMessage,lockState){
    if($(buttonName).size()==1) {
        lockElement(buttonName,processingMessage,originalMessage,lockState);
    }else if($(buttonName).size()>1){
        $(buttonName).each(
            function(){
                lockElement(buttonName,processingMessage,originalMessage,lockState);
            }
        );
    }
}

