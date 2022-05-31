function initEnumMenuDataTable(element, enumData, descriptionRender) {
    var data = [];
    if (enumData && enumData instanceof Array) {
        for (var i = 0; i < enumData.length; i++) {
            data.push({
                'value': enumData[i]
                , 'description': descriptionRender(enumData[i])
            });
        }
    }

    return element.DataTable({
        data: data,
        destroy: true,
        select: true,
        searching: false,
        paging: false,
        info: false,
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'value', data: 'value'},
            {name: 'description', data: 'description'}
        ]
    });
}

function initOptionMenuDataTable(element, enumData) {
    return element.DataTable({
        data: enumData,
        destroy: true,
        select: true,
        searching: false,
        paging: false,
        info: false,
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'id', defaultContent: ''},
            {name: 'name', data: 'name'}
        ]
    });
}

function initDatePicker(inputId) {
    return $('#' + inputId).datepicker({
        language: 'zh-TW',
        enableOnReadonly: false,
        format: 'yyyy/mm/dd'
    });
}

function initYearMonthPicker(inputId) {
    return $('#' + inputId).datepicker({
        language: 'zh-TW',
        enableOnReadonly: false,
        viewMode: "months",
        minViewMode: "months",
        format: 'yyyymm'
    });
}

function initBillingPreviewDataTable(element, data) {
    return element.DataTable({
        data: data,
        destroy: true,
        select: true,
        paging: false,
        ordering: true,
        info: false,
        searching: false,
        order: [[0, 'asc']],
        columnDefs: [
            {width: '45px', targets: 1},
            {width: '45px', targets: 2},
            {width: '45px', targets: 3},
        ],
        columns: [
            {name: 'id', data: 'billingItemId'},
            {
                name: 'productCategory',
                data: 'packageRef',
                render: billingSrcCategoryRender,
                defaultContent: ''
            },
            {name: 'paidPlan', data: 'paidPlan', render: paidPlanNameRender},
            {name: 'chargePlan', data: 'chargePlan', render: chargePlanNameRender},
            {name: 'productName', data: 'packageRef', render: sourceNameRender, defaultContent: ''},
            {name: 'expectedOutDate', data: 'expectedOutDate', render: localDateTimeRender},
            {name: 'calculateFromDate', data: 'calculateFromDate', render: dateTimeRender},
            {name: 'calculateToDate', data: 'calculateToDate', render: dateTimeRender},
            {name: 'count', data: 'count'},
            {name: 'taxExcludedAmount', data: 'taxExcludedAmount'}
        ]
    });
}

function initDeductHistoryPreviewDataTable(element, data) {
    return element.DataTable({
        data: data,
        destroy: true,
        select: true,
        paging: false,
        ordering: true,
        info: false,
        searching: false,
        order: [[0, 'asc']],
        columns: [
            {name: 'id', data: 'deductHistoryId', defaultContent: ''},
            {name: 'deductBillingItemId', data: 'deduct.deductBillingItemId'},
            {name: 'deductAmount', data: 'amount'},
            {name: 'createDate', data: 'createDate', render: dateTimeRender}
        ]
    });
}

function initServerSideProcessingContractDataTable(
    element
    , serverSideAjaxCall
    , condition
    , uniqueId
    , onSelectCallback) {
    var option = {
        columnDefs: [
            {"width": "75px", "targets": 2},
            {"width": "50px", "targets": 5},
            {"width": "60px", "targets": 6},
            {"width": "60px", "targets": 7},
            {"width": "60px", "targets": 8},
            {
                "targets": [0, 1],
                "visible": false
            }
        ],
        order: [[7, "desc"]],
        searching: false,
        uid: uniqueId,
        columns: [
            {name: 'contractId', data: 'contractId'},
            {name: 'companyId', data: 'company.companyId'},
            {name: 'companyBusinessNo', data: 'company.businessNo'},
            {name: 'companyName', data: 'company.name'},
            {name: 'name', data: 'name'},
            {name: 'status', data: 'status', render: contractStatusNameRender},
            {name: 'installationDate', data: 'installationDate', render: localDateTimeRender},
            {name: 'effectiveDate', data: 'effectiveDate', render: localDateTimeRender},
            {name: 'expirationDate', data: 'expirationDate', render: localDateTimeRender},
        ]
    };
    return initMultiSelectServerSideProcessingDataTable(element, condition, option, serverSideAjaxCall, onSelectCallback);
}

function initRootGradeMenuDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0, 3],
                "visible": false
            }
        ],
        columns: [
            {name: 'rootId', data: 'rootId', defaultContent: ''},
            {name: 'name', data: 'name'},
            {name: 'enabled', data: 'enabled', render: booleanDescriptionRender},
            {name: 'gradeDetail', data: 'children'}
        ]
    };

    if (data) {
        option.data = data;
    } else {
        option.ajax = {
            url: '/backendAdmin/newGradeManagementServlet/api/list',
            dataSrc: 'data'
        };
    }
    return element.DataTable(option);
}

function initRootGradeDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0, 3],
                "visible": false
            },
            {"width": "32px", "targets": 2},
        ],
        columns: [
            {name: 'rootId', data: 'rootId', defaultContent: ''},
            {name: 'name', data: 'name'},
            {name: 'enabled', data: 'enabled', render: booleanDescriptionRender},
            {name: 'gradeList', data: 'children'}
        ]
    }
    if (data) {
        option.data = data;
    } else {
        option.ajax = {
            url: '/backendAdmin/newGradeManagementServlet/api/list',
            dataSrc: 'data'
        }
    }
    return element.DataTable(option);
}

function initServiceInvoiceReliableDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'businessNo', data: 'businessNo', defaultContent: ''},
            {name: 'hasCompanyInChargeSystem', data: 'hasCompanyInChargeSystem', defaultContent: ''},
            {name: 'hasEnabledPackage', data: 'hasEnabledPackage', defaultContent: ''},
            {name: 'hasDisabledPackage', data: 'hasDisabledPackage', defaultContent: ''},
            {name: 'pscsCounter', data: 'pscsCounter', defaultContent: ''},
            {name: 'cscsCounter', data: 'cscsCounter', defaultContent: ''},
            {name: 'iasrCounter', data: 'iasrCounter', defaultContent: ''}
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initServerSideProcessingBillDataTable(
    element
    , serverSideAjaxCall
    , condition
    , uniqueId
    , onSelectCallback) {
    var option = {
        columnDefs: [
            {"width": "32px", "targets": 0},
            {"width": "65px", "targets": 5},
            {"width": "80px", "targets": 6},
            {"width": "65px", "targets": 7},
            {"width": "60px", "targets": 8},
            {"width": "60px", "targets": 9},
            {"width": "65px", "targets": 10},
            {"width": "65px", "targets": 11},
            {
                "targets": [1, 2],
                "visible": false
            }, {
                orderable: false,
                className: 'select-checkbox',
                targets: 0
            }, {
                orderable: false,
                targets: [2, 3]
            },
        ],
        lengthMenu: [[25, 50, 100, 300], [25, 50, 100, 300]],
        searching: false,
        autoWidth: false,
        uid: uniqueId,
        columns: [
            {name: 'checked', data: '', defaultContent: ''},
            {name: 'billId', data: 'billId'},
            {name: 'companyId', data: 'company.companyId'},
            {name: 'companyName', data: 'company.name'},
            {name: 'businessNo', data: 'company.businessNo'},
            {name: 'billStatus', data: 'billStatus', render: billStatusNameRender},
            {name: 'paymentRequestEmail', data: 'noticeList', render: paymentRequestEmailDescriptionRender},
            {name: 'paidDate', data: 'paidDate', render: dateTimeRender},
            {name: 'taxExcludedAmount', data: 'taxExcludedAmount'},
            {name: 'taxIncludedAmount', data: 'taxIncludedAmount'},
            {name: 'createDate', data: 'createDate', render: dateTimeRender},
            {name: 'billYm', data: 'billYm', defaultContent: ''}
        ]
    };
    var billDataTable = initMultiSelectServerSideProcessingDataTable(element, condition, option, serverSideAjaxCall, onSelectCallback);
    //checkbox for select all
    billDataTable.on("click", "th.select-checkbox", function () {
        if ($("th.select-checkbox").hasClass("selected")) {
            billDataTable.rows().deselect();
            $("th.select-checkbox").removeClass("selected");
        } else {
            billDataTable.rows().select();
            $("th.select-checkbox").addClass("selected");
        }
    }).on("select deselect", function () {
        ("Some selection or deselection going on")
        if (billDataTable.rows({
            selected: true
        }).count() !== billDataTable.rows().count()) {
            $("th.select-checkbox").removeClass("selected");
        } else {
            $("th.select-checkbox").addClass("selected");
        }
    }).on('page.dt', function () {
        $("tr.selected").removeClass("selected");
        $("th.select-checkbox").removeClass("selected");
    }).on('order.dt', function () {
        $("tr.selected").removeClass("selected");
        $("th.select-checkbox").removeClass("selected");
    });
    return billDataTable;
}

function initChargePackageMenuDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0, 1],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'packageId', defaultContent: ''},
            {name: 'packageRefList', data: 'packageRefList', defaultContent: ''},
            {name: 'name', data: 'name'}
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initChargePackageDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0, 2],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'packageId', defaultContent: ''},
            {name: 'name', data: 'name'},
            {name: 'enabled', data: 'enabled'},
            {name: 'enabledDescription', data: 'enabled', render: booleanDescriptionRender}
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.ajax = {};
    }
    return element.DataTable(option);
}

function initChargeRuleDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'chargeRuleId', defaultContent: ''},
            {name: 'name', data: 'chargeRuleName'},
            {name: 'gradeName', data: 'rootGradeName'},
            {name: 'enabled', data: 'enabled', render: chargeRuleStatusRender},
            {name: 'paidPlan', data: 'paidPlanDesc'},
            {name: 'chargeBaseType', data: 'chargeBaseTypeDesc'},
            {name: 'calculateCycleType', data: 'calculateCycleTypeDesc'},
            {name: 'chargeCycleType', data: 'chargeCycleTypeDesc'}
        ]
    };

    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initCycleMenuDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'id'},
            {name: 'name', data: 'name'}
        ]
    };

    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initChildrenGradeDataTable(element, data) {
    var option = {
        "destroy": true,
        "select": true,
        "paging": false,
        "ordering": true,
        "info": false,
        "searching": false,
        "data": data,
        "order": [[1, 'asc']],
        "columnDefs": [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'gradeId', defaultContent: ''},
            {name: 'level', data: 'level'},
            {name: 'cntStart', data: 'cntStart'},
            {name: 'cntEnd', data: 'cntEnd'},
            {name: 'fixPrice', data: 'fixPrice', defaultContent: ''},
            {name: 'unitPrice', data: 'unitPrice', defaultContent: ''}
        ]
    };

    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initServerSideProcessingBillingItemDataTable(
    element,
    serverSideAjaxCall,
    condition,
    uniqueId,
    onSelectCallback) {
    var option = {
        columnDefs: [
            {
                "targets": [0, 1, 4],
                "visible": false
            },
            {orderData: [2, 3], targets: 1},
            {orderData: [5], targets: 4},
            {width: '45px', targets: 3},
            {width: '45px', targets: 5},
            {width: '45px', targets: 6},
            {width: '45px', targets: 7},
            {width: '75px', targets: 9},
            {width: '60px', targets: 10},
            {width: '48px', targets: 11},
            {width: '48px', targets: 12},
            {width: '48px', targets: 13}
        ],
        fixedColumns: true,
        destroy: true,
        searching: false,
        autoWidth: false,
        processing: true,
        serverSide: true,
        deferRender: true,
        ajax: serverSideAjaxCall,
        uid: uniqueId,
        columns: [
            {name: 'billingItemId', data: 'billingItemId'},
            {name: 'companyId', data: 'company.companyId'},
            {name: 'companyName', data: 'company.name'},
            {name: 'companyBusinessNo', data: 'company.businessNo'},
            {name: 'productId', data: 'product.productId', defaultContent: ''},
            {
                name: 'productCategory',
                data: 'packageRef',
                defaultContent: '',
                render: billingSrcCategoryRender
            },
            {name: 'chargePlan', data: 'chargePlan', render: chargePlanNameRender},
            {name: 'paidPlain', data: 'paidPlan', render: paidPlanNameRender, defaultContent: ''},
            {name: 'count', data: 'count'},
            {name: 'taxExcludedAmount', data: 'taxExcludedAmount'},
            {name: 'expectedOutDate', data: 'expectedOutDate', defaultContent: '', render: localDateTimeRender},
            {name: 'calculateFromDate', data: 'calculateFromDate', render: dateTimeRender},
            {name: 'calculateToDate', data: 'calculateToDate', render: dateTimeRender},
            {name: 'createDate', data: 'createDate', defaultContent: '', render: dateTimeRender}
        ]
    };
    return initMultiSelectServerSideProcessingDataTable(element, condition, option, serverSideAjaxCall, onSelectCallback);
}

function initServerSideProcessingProductDataTable(element, serverSideAjaxCall, rowCallback) {
    var option = {
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        fixedColumns: true,
        destroy: true,
        searching: false,
        autoWidth: false,
        processing: true,
        serverSide: true,
        deferRender: true,
        ajax: serverSideAjaxCall,
        rowCallback: rowCallback,
        columns: [
            {name: 'productId', data: 'productId'},
            {name: 'productName', data: 'productName'},
            {name: 'productCategory', data: 'productCategory.categoryName', defaultContent: ''},
            {name: 'productType', data: 'productType', render: productTypeNameRender},
            {name: 'productSource', data: 'productSource', defaultContent: ''},
            {name: 'productStatus', data: 'productStatus', render: productStatusRender},
            {name: 'productPrice', data: 'productPricing.basePrice'},
            {name: 'productDiscountAmount', data: 'productDiscount.discountValue'},
            {name: 'productDiscountUnit', data: 'productDiscount.discountUnit'},
            {name: 'productDiscountMaximumAmount', data: 'productDiscount.maximumDiscountAmount'}
        ]
    };
    return element.DataTable(option);
}

function initConfirmBillingItemDataTable(element, data) {
    var option = {
        columnDefs: [
            {
                "targets": [],
                "visible": false
            },
            {width: "10%", "targets": 2}
        ],
        destroy: true,
        paging: false,
        ordering: true,
        info: false,
        searching: false,
        select: false,
        columns: [
            {name: 'companyName', data: 'company.name'},
            {
                name: 'productCategory',
                data: 'packageRef',
                defaultContent: '',
                render: billingSrcCategoryRender
            },
            {name: 'chargePlan', data: 'chargePlan', render: chargePlanNameRender},
            {name: 'paidPlain', data: 'paidPlan', render: paidPlanNameRender, defaultContent: ''},
            {name: 'packageRef', data: 'packageRef', render: sourceNameRender},
            {name: 'count', data: 'count'},
            {name: 'taxExcludedAmount', data: 'taxExcludedAmount'},
            {name: 'calculateFromDateDescription', data: 'calculateFromDate', render: dateTimeRender},
            {name: 'calculateToDateDescription', data: 'calculateToDate', render: dateTimeRender}
        ]
    };

    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initServerSideProcessingDeductDataTable(
    element,
    serverSideAjaxCall,
    condition,
    uniqueId,
    onSelectCallback) {
    var option = {
        columnDefs: [
            {
                "targets": [0, 1],
                "visible": false
            }
            , {width: '75px', targets: 3}
            , {orderData: [2, 3], targets: 1}
        ],
        fixedColumns: true,
        destroy: true,
        searching: false,
        autoWidth: false,
        processing: true,
        serverSide: true,
        deferRender: true,
        select: true,
        ajax: serverSideAjaxCall,
        uid: uniqueId,
        columns: [
            {name: 'deductId', data: 'deductId'},
            {name: 'companyId', data: 'company.companyId'},
            {name: 'companyName', data: 'company.name',},
            {name: 'companyBusinessNo', data: 'company.businessNo'},
            {name: 'status', data: 'deductStatus', render: deductStatusRender},
            {name: 'targetProductCategoryName', data: 'targetProductCategory.categoryName'},
            {name: 'quota', data: 'quota'},
            {name: 'deductibleAmount', data: 'deductHistoryList', render: deductAmountRender},
            {name: 'salesPrice', data: 'salesPrice'},
            {name: 'effectiveDate', data: 'effectiveDate', render: localDateTimeRender},
            {name: 'expirationDate', data: 'expirationDate', render: localDateTimeRender},
            {name: 'createDate', data: 'createDate', render: localDateTimeRender},
            {name: 'modifyDate', data: 'modifyDate', render: localDateTimeRender}
        ]
    };
    return initMultiSelectServerSideProcessingDataTable(element, condition, option, serverSideAjaxCall, onSelectCallback);
}

function initDeductHistoryDataTable(element, data) {
    var option = {
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        destroy: true,
        select: true,
        columns: [
            {name: 'deductHistoryId', data: 'deductHistoryId'},
            {name: 'itemName', data: 'billingItem.packageRef.chargeRule.name', defaultContent: ''},
            {name: 'amount', data: 'amount'},
            {name: 'createDate', data: 'createDate'}
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initChargeRuleMenuDataTable(element, data) {
    var option = {
        destroy: true,
        select: true,
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'id', data: 'chargeRuleId', defaultContent: ''},
            {name: 'name', data: 'chargeRuleName'},
            {name: 'gradeName', data: 'rootGradeName'},
            {name: 'paidPlan', data: 'paidPlanDesc'},
            {name: 'productCategory', data: 'productCategoryName'},
            {name: 'chargeBaseName', data: 'chargeBaseType', render: chargeBaseNameRender}
        ]
    };

    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initPackageRefChargeRulePreviewDataTable(element, data) {
    var option = {
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        destroy: true,
        select: true,
        paging: false,
        searching: false,
        info: false,
        autoWidth: false,
        fixedColumns: true,
        columns: [
            {name: 'packageRefId', data: 'packageRefId'},
            {name: 'productCategoryName', data: 'productCategoryName'},
            {name: 'chargeRuleName', data: 'chargeRuleName'},
            {name: 'gradeName', data: 'rootGradeName'},
            {name: 'paidPlan', data: 'paidPlanDesc'},
            {
                name: 'chargeBaseType',
                data: 'chargeBaseTypeDesc'
            },
            {name: 'calculateCycleType', data: 'calculateCycleTypeDesc'},
            {name: 'chargeCycleType', data: 'chargeCycleTypeDesc'}
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initInvoiceRemainingListViewDataTable(element, data) {
    let option = {
        destroy: true,
        paging: true,
        info: true,
        autoWidth: false,
        searching: false,
        fixedColumns: true,
        ordering: true,
        order: [[0, 'asc']],
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            }
        ],
        columns: [
            {name: 'invoiceRemainingId', data: 'invoiceRemainingId'},
            {name: 'chargePackageName', data: 'chargePackageName'},
            {name: 'businessNo', data: 'businessNo'},
            {name: 'usage', data: 'usage', defaultContent: ''},
            {name: 'remaining', data: 'remaining'},
            {name: 'calculateStartDate', data: 'calculateStartDate'},
            {name: 'calculateEndDate', data: 'calculateEndDate'},
            {name: 'updateTimestampDesc', data: 'updateTimestampDesc'}
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}

function initChargeSourceDataTable(element, data) {
    var option = {
        destroy: true,
        paging: true,
        info: true,
        autoWidth: false,
        fixedColumns: true,
        columns: [
            {name: 'seller', data: 'seller'},
            {name: 'invoiceDate', data: 'invoiceDate'},
            {name: 'originalCount', data: 'originalCount'},
            {name: 'mediumCount', data: 'mediumCount', "defaultContent": 0},
            {
                name: 'diff', data: 'diff', "defaultContent": "", render: function (data, type, row) {
                    var rowMediumCount = row.mediumCount;
                    if (!rowMediumCount) {
                        rowMediumCount = 0;
                    }
                    return row.originalCount - rowMediumCount;
                }
            }
        ]
    };
    if (data) {
        option.data = data;
    } else {
        option.data = {};
    }
    return element.DataTable(option);
}
