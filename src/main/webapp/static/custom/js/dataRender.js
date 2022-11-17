let inCacheMilliSeconds = {};
let cycleMenuOptionList = {};

/**
 * 為了解決重覆render的問題，約定defaultDataModel為Rendering用的資料集。
 * @param milliseconds
 * @returns {*}
 */

function checkInUsedMilliSecond(milliseconds) {
    if (milliseconds && !inCacheMilliSeconds[milliseconds]) {
        inCacheMilliSeconds[milliseconds] = new Date(milliseconds);
    }
    return inCacheMilliSeconds[milliseconds];
}

function dateTimeRender(data, type, row) {
    var date = checkInUsedMilliSecond(data);
    var formatString;
    if (date) {
        formatString = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate();
    } else {
        formatString = '';
    }
    return formatString;
}

function localDateTimeRender(data, type, row) {
    var formatString;
    if (data) {
        formatString = data.date.year + '/' + data.date.month + '/' + data.date.day;
    } else {
        formatString = '';
    }
    return formatString;
}

function sourceNameRender(data, type, row) {
    var result = ' ';
    if (data) {
        if (data.chargeRule) {
            result = data.chargeRule.name;
        }
    }
    return result;
}

function deductTypeRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'PREPAYMENT':
            result = '預繳';
            break;
        case 'DEDUCT':
            result = '扣抵';
            break;
    }
    return result;
}

function deductStatusRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'C':
            result = '新建';
            break;
        case 'B':
            result = '等待結帳中';
            break;
        case 'E':
            result = '啟用';
            break;
        case 'D':
            result = '停用';
            break;
    }
    return result;
}

function productStatusRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'E':
            result = '啟用';
            break;
        case 'D':
            result = '停用';
            break;
    }
    return result;
}

function productTypeNameRender(data, type, row) {
    var result = '';
    if (row.chargeRule) {
        result = '服務';
    }
    if (row.product) {
        result = '商品';
    }
    return result;
}

function chargePlanNameRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'PERIODIC':
            result = '週期';
            break;
        case 'INITIATION':
            result = '啟用';
            break;
        case 'TERMINATION':
            result = '終止';
            break;
        case 'SUSPENSION':
            result = '暫停';
            break;
        case 'SUSPENSION_PERIODIC':
            result = '停止區間週期';
            break;
        case 'RE_ACTIVATION':
            result = '重啟';
            break;
        case 'USAGE':
            result = '單次使用服務';
            break;
    }
    return result;
}

function paidPlanNameRender(data, type, row) {
    var result = '';
    if (data == 'PRE_PAID') {
        result = '預繳';
    }
    if (data == 'POST_PAID') {
        result = '後繳';
    }
    return result;
}

function chargeBaseNameRender(data, type, row) {
    var result = '';
    if (data == 'INVOICE_AMOUNT_SUM') {
        result = '發票數量合計';
    }
    if (data == 'EMAIL_AMOUNT_SUM') {
        result = 'EMAIL數量合計';
    }
    return result;
}

function contractStatusNameRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'C':
            result = '新建';
            break;
        case 'B':
            result = '預繳結帳中';
            break;
        case 'E':
            result = '啟用';
            break;
        case 'T':
            result = '終止';
            break;
        case 'PT':
            result = '部份結清';
            break;
        case 'S':
            result = '暫停';
            break;
        case 'D':
            result = '停用';
            break;
    }
    return result;
}

function billStatusNameRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'C':
            result = '未付款';
            break;
        case 'P':
            result = '已付款';
            break;
    }
    return result;
}

function booleanDescriptionRender(data, type, row) {
    var result = '';
    if (data) {
        result = '是';
    } else {
        result = '否';
    }
    return result;
}

function paymentRequestEmailDescriptionRender(noticeList, type, row) {
    var result = '';
    if (noticeList.length > 0) {
        result = '已寄送';
    } else {
        result = '未寄送';
    }
    return result;
}

function billingSrcCategoryRender(data, type, row) {
    var productCategoryName = '';
    if (data) {
        if (data.chargeRule) {
            productCategoryName = data.chargeRule.productCategory.categoryName;
        }
        if (row.billingItemType == 'MIGRATION') {
            productCategoryName += '(舊)';
        }
    }
    return productCategoryName;
}

function chargeRuleStatusRender(data, type, row) {
    var result = '';
    if (data) {
        result = '啟用';
    } else {
        result = '停用';
    }
    return result;
}

function deductStatusRender(data, type, row) {
    var result = '';
    switch (data) {
        case 'C':
            result = '新建';
            break;
        case 'B':
            result = '等待結帳';
            break;
        case 'E':
            result = '啟用';
            break;
        case 'D':
            result = '停用';
            break;
    }
    return result;
}

function deductAmountRender(data, type, row) {
    var deductibleAmount = 0;
    if (data && data.length > 0) {
        data.forEach(deductHistory => {
            deductibleAmount += deductHistory.amount;
        });
    }
    return deductibleAmount;
}

function cycleMenuOptionNameRender(data, type, row) {
    if (cycleMenuOptionList instanceof Array) {
        return cycleMenuOptionNameMatch(data);
    } else {
        cycleMenuOptionList = dataSourceModule.getCycleList();
        return cycleMenuOptionNameMatch(data);
    }
    return name;
}

function cycleMenuOptionNameMatch(data) {
    var name = '';
    cycleMenuOptionList.forEach(element => {
        if (element.id == data) {
            name = element.name;
        }
    });
    return name;
}