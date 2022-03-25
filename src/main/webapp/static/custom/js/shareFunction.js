function cleanWithValue(obj, val) {
    for (const [key, value] of Object.entries(obj)) {
        obj[key] = val;
    }
}

function isEmptyObject(obj) {
    var result = false;
    if (!obj) {
        return true;
    }
    if (Object.keys(obj).length === 0) {
        return true;
    }
    return result;
}

/**
 * jquery 的1.12版與tooltips衝突喔!
 */
function draggableModal() {
    $(".modal-header").on("mousedown", function (mousedownEvt) {
        var $draggable = $(this);
        var x = mousedownEvt.pageX - $draggable.offset().left,
            y = mousedownEvt.pageY - $draggable.offset().top;
        $("body").on("mousemove.draggable", function (mousemoveEvt) {
            $draggable.closest(".modal-dialog").offset({
                "left": mousemoveEvt.pageX - x,
                "top": mousemoveEvt.pageY - y
            });
        });
        $("body").one("mouseup", function () {
            $("body").off("mousemove.draggable");
        });
        $draggable.closest(".modal").one("bs.modal.hide", function () {
            $("body").off("mousemove.draggable");
        });
    });
}

function initTooltip() {
    //初始化所有非modal類型的tooltip
    //謹以此註解紀念這位大大的無私奉獻:https://stackoverflow.com/questions/39960446/bootstrap-tooltip-doesnt-work-in-modal
    $('[data-tooltip="tooltip"]').tooltip();
    //在建立tooltip的時候modal還沒有建立，因此要在modal觸發的時候建立tooltip
    $('[data-toggle="modal"]').on('show.bs.modal', function (event) {
        $('[data-tooltip="tooltip"]').tooltip();
    });
}

function localDateTimeToJsDate(localDateTime) {
    var yearStr = localDateTime.date.year;
    var dayStr = localDateTime.date.day;
    var monthStr = localDateTime.date.month;
    return Date.parse(yearStr + "-" + monthStr + "-" + dayStr);
}

function promiseWrapper(func) {
    return new Promise(function (resolve, reject) {
        try {
            func();
            resolve();
        } catch (ex) {
            console.log(ex.stack);
            reject();
        }
    });
}

function genOperationObj(condition, idList, custom) {
    return {
        condition: condition,
        idList: idList,
        custom: custom
    }
}

function selectedArrayToIdList(selectedArray, key) {
    let idListStr = '';
    if (selectedArray instanceof Array) {
        for (let i = 0; i < selectedArray.length; i++) {
            idListStr += selectedArray[i][key];
            if (i != selectedArray.length - 1) {
                idListStr += ",";
            }
        }
    }
    return idListStr;
}