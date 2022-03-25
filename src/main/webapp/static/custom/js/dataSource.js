var dataSourceModule = {
    getBankCodeList: function () {
        var resultSet = undefined;
        var ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/bankCode';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getPaymentMethod: function () {
        var resultSet = undefined;
        var ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/paymentMethod';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getCompanyList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/companySearchServlet/api/list';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap.data;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getBillableCompanyList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/billableCompanyMenuItem';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getCompanyMenuItemList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/companyMenuItem';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getContractList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/contractManagementServlet/api/list';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap.data;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getContractStatus: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/contractStatus';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargeRuleList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/chargeRuleManagementServlet/api/list';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap.data;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getPackageList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/chargePackageManagementServlet/api/list';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getAvailablePackageList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/chargePackageManagementServlet/api/availableList';
        ajaxParam.requestType = 'GET';
        ajaxParam.dataType = 'json';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getBillStatusList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/billStatus';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getPaidPlanList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/paidPlan';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getProductList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/productManagementServlet/api/list';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap.data;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargePlanList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/chargePlan';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargeBaseTypeList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/chargeBaseType';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargeBaseList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/chargeBase';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap.data;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargePackageStatusList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/chargePackageStatus';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getTerminationClauseTypeList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/terminationClauseType';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseMap) {
            resultSet = responseMap;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargeModeStatusList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/chargeRuleStatus';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getRootGradeList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/newGradeManagementServlet/api/list';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData.data;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getCycleList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/cycleType';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getChargeCycleList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/chargeCycleType';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getCalculateCycleList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/calculateCycleType';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getProductCategoryList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/productCategory';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getDeductTypeList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/deductType';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    getDeductStatusList: function () {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/enumerationServlet/api/deductStatus';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = function (responseData) {
            resultSet = responseData;
        };
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    serverSideProcessingBillSearchAjaxCall: function (obj, handler) {
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/billManagementServlet/api/serverSideProcessingSearch';
        ajaxParam.requestType = 'POST';
        ajaxParam.contentType = 'application/json';
        ajaxParam.dataType = 'json';
        ajaxParam.processData = 'false';
        ajaxParam.data = JSON.stringify(obj);
        return promiseSyncAjaxCall(ajaxParam, handler, handler);
    },
    findInvoiceDataSourceReport: function (obj, handler) {
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/chargeSourceManagementServlet/api/list/condition';
        ajaxParam.requestType = 'POST';
        ajaxParam.contentType = 'application/json';
        ajaxParam.dataType = 'json';
        ajaxParam.processData = 'false';
        ajaxParam.data = JSON.stringify(obj);
        asyncAjaxCall(ajaxParam, handler);
    },
    recalculateIasrByCondition: function (obj, handler) {
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/chargeSourceManagementServlet/api/recalculateByCondition';
        ajaxParam.requestType = 'POST';
        ajaxParam.contentType = 'application/json';
        ajaxParam.dataType = 'json';
        ajaxParam.processData = 'false';
        ajaxParam.data = JSON.stringify(obj);
        asyncAjaxCall(ajaxParam, handler, handler);
    },
    getRegenTaskMap: function (handler) {
        let resultSet = undefined;
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/chargeSourceManagementServlet/api/getRegenTaskMap';
        ajaxParam.requestType = 'GET';
        ajaxParam.successHandler = handler;
        syncAjaxCall(ajaxParam);
        return resultSet;
    },
    genInvoiceImportReportWithIdList: function (handler) {
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/billManagementServlet/api/downloadInvoiceImportReportWithIdList';
        ajaxParam.requestType = 'GET';
        ajaxParam.contentType = 'application/text;charset=utf-8';
        ajaxParam.successHandler = handler;
        syncAjaxCall(ajaxParam);
    },
    serverSideProcessingDeductSearchAjaxCall: function (obj, handler) {
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/deductManagementServlet/api/serverSideProcessingSearch';
        ajaxParam.requestType = 'POST';
        ajaxParam.contentType = 'application/json';
        ajaxParam.dataType = 'json';
        ajaxParam.processData = 'false';
        ajaxParam.data = JSON.stringify(obj);
        return promiseSyncAjaxCall(ajaxParam, handler, handler);
    },
    invoiceRemainingViewListAjaxCall: function (companyId, handler) {
        let ajaxParam = {};
        ajaxParam.url = '/backendAdmin/invoiceRemainingViewServlet/find/list/' + companyId;
        ajaxParam.requestType = 'GET';
        ajaxParam.contentType = 'application/json';
        ajaxParam.dataType = 'json';
        ajaxParam.processData = 'false';
        return promiseSyncAjaxCall(ajaxParam, handler, handler);
    }
};

function getIasrYearMonthList(handler) {
    let ajaxParam = {};
    ajaxParam.url = '/backendAdmin/reliableManagementServlet/api/iasr/yearMonth/list';
    ajaxParam.requestType = 'GET';
    ajaxParam.successHandler = handler;
    syncAjaxCall(ajaxParam);
}

function serverSideProcessingBillingItemSearchAjaxCall(obj, handler) {
    let ajaxParam = {};
    ajaxParam.url = '/backendAdmin/billingItemManagementServlet/api/serverSideProcessingSearch';
    ajaxParam.requestType = 'POST';
    ajaxParam.contentType = 'application/json';
    ajaxParam.dataType = 'json';
    ajaxParam.processData = 'false';
    ajaxParam.data = JSON.stringify(obj);
    return promiseSyncAjaxCall(ajaxParam, handler, handler);
}

