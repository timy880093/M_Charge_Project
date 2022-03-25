function initMultiSelectServerSideProcessingDataTable(
    element
    , condition
    , option
    , serverSideAjaxCall
    , onSelectCallback) {
    var multiSelectArray = new Array();
    var result;
    option['destroy'] = true;
    option['processing'] = true;
    option['serverSide'] = true;
    option['ajax'] = function serverSideProcessingAjaxCall(data, callback, settings) {
        var page = 0;
        if (typeof condition === 'function') {
            data['condition'] = prepareSearchConditionObj(condition());
        } else {
            data['condition'] = prepareSearchConditionObj(condition);
        }
        if (result) {
            page = result.page.info().page;
        }
        if (multiSelectArray.length > 0) {
            multiSelectArray = new Array();
        }
        addHiddenColumnSortSupplyData(data, settings);
        data['page'] = page;
        serverSideAjaxCall(data, function (responseMap) {
            if (responseMap.data) {
                callback({
                    draw: data.draw,
                    data: responseMap.data,
                    recordsTotal: responseMap.totalCount,
                    recordsFiltered: responseMap.totalCount
                });
            } else {
                callback({
                    draw: data.draw,
                    data: new Array(),
                    recordsTotal: 0,
                    recordsFiltered: 0
                });
            }
        });
    };
    option['rowCallback'] = function serverSideProcessRowCallback(row, data) {
        var existList = multiSelectArray.filter(exist => exist[option.uid] == data[option.uid]);
        if (existList.length == 1) {
            $(row).addClass('selected');
        }
    };
    result = element.DataTable(option);
    result.on('click', 'tr', function (event) {
        //避免標題欄吃到互動script
        if ($(this).hasClass("even") || $(this).hasClass("odd")) {
            $(this).toggleClass('selected');
        }
        multiSelectArray = result.rows('.selected').data().toArray();
        if (onSelectCallback) {
            onSelectCallback(multiSelectArray);
        }
    });
    return result;
}

function prepareSearchConditionObj(obj) {
    const result = Object.assign({}, obj);
    for (const [key, value] of Object.entries(result)) {
        if (value == undefined || value == '') {
            delete result[key];
        }
    }
    return result;
}