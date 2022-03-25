function promiseSyncAjaxCall(ajaxParam, resolveCallBack, rejectCallBack) {
    if (!ajaxParam.requestType) {
        ajaxParam.requestType = 'GET';
    }
    return new Promise((resolve, reject) => {
        $.ajax({
            url: ajaxParam.url,
            dataType: ajaxParam.dataType,
            type: ajaxParam.requestType,
            data: ajaxParam.data,
            processData: ajaxParam.processData,
            contentType: ajaxParam.contentType,
            async: false,
            success: function (data, textStatus, jqXHR) {
                try {
                    resolve();
                    if (resolveCallBack instanceof Function) {
                        resolveCallBack(data, textStatus, jqXHR);
                    }
                } catch (e) {
                    console.log(e);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                try {
                    reject();
                    if (rejectCallBack instanceof Function) {
                        rejectCallBack(xhr, ajaxOptions, thrownError);
                    }
                } catch (e) {
                    console.log(e)
                }
            }
        });
        return;
    });
}

function asyncAjaxCall(ajaxParam, resolveCallBack, rejectCallBack) {
    if (!ajaxParam.requestType) {
        ajaxParam.requestType = 'GET';
    }
    $.ajax({
        url: ajaxParam.url,
        dataType: ajaxParam.dataType,
        type: ajaxParam.requestType,
        data: ajaxParam.data,
        processData: ajaxParam.processData,
        contentType: ajaxParam.contentType,
        async: true,
        success: function (data, textStatus, jqXHR) {
            try {
                if (resolveCallBack instanceof Function) {
                    resolveCallBack(data, textStatus, jqXHR);
                }
            } catch (e) {
                console.log(e);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            try {
                if (rejectCallBack instanceof Function) {
                    rejectCallBack(xhr, ajaxOptions, thrownError);
                }
            } catch (e) {
                console.log(e)
            }
        }
    });
}

function syncAjaxCall(ajaxParam) {
    if (!ajaxParam.dataType) {
        ajaxParam.dataType = 'json'
    }
    if (!ajaxParam.requestType) {
        ajaxParam.requestType = 'GET';
    }
    if (ajaxParam.processData == 'false') {
        ajaxParam.processData = false;
    } else {
        ajaxParam.processData = true;
    }
    if (!ajaxParam.contentType) {
        ajaxParam.contentType = 'application/x-www-form-urlencoded';
    }
    $.ajax({
        url: ajaxParam.url,
        dataType: ajaxParam.dataType,
        type: ajaxParam.requestType,
        data: ajaxParam.data,
        processData: ajaxParam.processData,
        contentType: ajaxParam.contentType,
        async: false,
        success: function (data) {
            if (ajaxParam.successHandler) {
                ajaxParam.successHandler(data);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (ajaxParam.errorHandler) {
                ajaxParam.errorHandler(xhr, ajaxOptions, thrownError);
            }
        }
    });
}

function messageHandler(data) {
    let status = "info";
    if (data.status) {
        status = data.status;
    }
    if (data.sweetAlertStatus) {
        status = data.sweetAlertStatus;
    }
    Swal.fire({
        icon: status.toLowerCase(),
        text: data.message,
        title: data.title
    });
}

function isValidObj(obj) {
    return Object.keys(obj).length !== 0 && obj.constructor === Object;
}

/**swal的loading與modal的連動有問題，如果loading的發動與modal的dismiss有關，他會等到dismiss結束後才會觸發
 * 但這並不是我們想要的順序，這會造成即便我們整個event內容都用promise也沒有用，他還是被卡在event中，他一定要出event才會觸發
 * 網路上說的把table-index=-1拿掉我也試過了，並沒有用處
 * 這裡就用workaround，既然他一定要出來再觸發，那我就用async的request，他一定會出來，然後在結束的callback中隱藏loading訊息
 *
 */
function loadingAjaxProcessor(ajaxFunc, title) {
    Swal.fire({
        title: title,
        allowEscapeKey: false,
        allowOutsideClick: false,
        didOpen: () => {
            ajaxFunc();
            Swal.showLoading();
        }
    })
}

function confirmAjaxProcessor(ajaxFunc, title, text, icon) {
    Swal.fire({
        title: title,
        text: text,
        icon: icon,
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    }).then((result) => {
        if (result.isConfirmed) {
            ajaxFunc();
        }
    });
}

