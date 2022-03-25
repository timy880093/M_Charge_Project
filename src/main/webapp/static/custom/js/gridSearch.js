function setSearchValue(field0, value0, field1, value1, field2, value2, field3, value3, field4, value4) {
    var sdata = {
        searchField: [field0, field1, field2, field3, field4],
        searchString: [value0, value1, value2, value3, value4]
    }
    return sdata;
}

/**
 * 之前的setSearchValue不好用，直接讓他傳進兩個陣列做json的格式就可以了。
 * @param searchField
 * @param searchString
 * @returns {{searchField: *, searchString: *}}
 */
function setSearchValueByArray(searchField, searchString) {
    var sdata = {
        searchField: checkIfNotArray(searchField),
        searchString: checkIfNotArray(searchString)
    }
    return sdata;
}

/**
 * 如果進來的資料不是陣列的話就什麼都不回傳，至少不能讓JS資料錯誤，不然頁面會出現沒有辦法預期的情況。
 * @param array
 * @returns {*}
 */
function checkIfNotArray(array) {
    if (!array.constructor === Array) {
        return
    } else {
        return array;
    }
}

function initJqGrid(gridParam) {
    if (!gridParam.dataType) {
        gridParam.dataType = 'json';
    }
    if (!gridParam.sort) {
        gridParam.sort = gridParam.columnModel[0];
    }
    if (!gridParam.order) {
        gridParam.order = 'desc';
    }
    jQuery("#" + gridParam.gridTableId).jqGrid({
        url: gridParam.url,
        datatype: gridParam.dataType,
        colNames: gridParam.columnNames,
        colModel: gridParam.columnModel,
        rowNum: 100,
        rowList: [100, 200, 300],
        pager: "#" + gridParam.pagerDivId,
        sortname: gridParam.sort,
        sortorder: gridParam.order,
        onPaging: function (id) {
            gridParam.onPagingHandler(id);
        },
        onSortCol: function (index, columnIndex, sortOrder) {
            gridParam.onSortColHandler(index, columnIndex, sortOrder);
        },
        beforeSelectRow: function (rowid, e) {
            gridParam.beforeSelectRowHandler(rowid, e);
        },
        onSelectAll: function (aRowids, status) {
            gridParam.onSelectAllHandler(aRowids, status);
        },
        loadComplete: function () {
            gridParam.loadCompleteHandler();
        }
    });
}

function wrapDataObj(settings, data) {
    var mappedObj = {};
    if (settings.aoColumns) {
        settings.aoColumns.forEach(
            function (element) {
                mappedObj[element.name] = data[element.idx];
            }
        );
    }
    return mappedObj;
}

function arrayMove(arr, old_index, new_index) {
    var oldObj;
    for (var i = 0; i < arr.length; i++) {
        if (i == old_index) {
            oldObj = arr[i];
            arr.splice(old_index, 1);
            break;
        }
    }
    arrayInsertElement(arr, oldObj, new_index);
}

function arrayInsertElement(arr, element, index) {
    var result = [];
    if (arr[index]) {
        var before = arr.splice(0, index);
        var after = arr.splice(index, arr.length - index);
        before.forEach(beforeEle => {
            result.push(beforeEle);
        });
        result.push(element);
        after.forEach(afterEle => {
            result.push(afterEle);
        });
        result.forEach(resultEle => {
            arr.push(resultEle);
        });
        arr.push();
    } else {
        arr[index] = element;
    }
}

function addHiddenColumnSortSupplyData(data, settings) {
    var aoColumns = settings.aoColumns;
    for (var i = 0; i < data.length; i++) {
        if (aoColumns[i] && aoColumns[i].aDataSort) {
            for (var j = 0; j < aoColumns[i].aDataSort.length; j++) {
                if (i != aoColumns[i].aDataSort[j]) {
                    data.columns[aoColumns[i].aDataSort[j]].sortBy = i;
                }
            }
        }
    }
}

function isFunction(functionToCheck) {
    return functionToCheck && {}.toString.call(functionToCheck) === '[object Function]';
}