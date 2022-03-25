function packageRefChargeRulePreviewComponentInitializer(id) {
    let date = new Date();
    let tableId = 'packageRefChargeRulePreviewDataTable_' + date.getTime();
    let tableDom = createBootstrapDataTableDom({
        tableId: tableId,
        th: [
            'packageRefId',
            '自訂分類',
            '項目名稱',
            '級距名稱',
            '繳費類別',
            '收費類別',
            '計費來源',
            '計算週期',
            '帳期'
        ]
    });
    let targetDiv = document.getElementById(id);
    targetDiv.appendChild(tableDom);
    var option = {
        columnDefs: [
            {
                "targets": [0],
                "visible": false
            },
            {width: '45px', targets: 4},
            {width: '45px', targets: 5},
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
            {name: 'gradeName', data: 'rootGradeName',},
            {name: 'paidPlan', data: 'paidPlanDesc'},
            {name: 'chargePlan', data: 'chargePlanDesc'},
            {
                name: 'chargeBaseType',
                data: 'chargeBaseTypeDesc'
            },
            {name: 'calculateCycleType', data: 'calculateCycleTypeDesc'},
            {name: 'chargeCycleType', data: 'chargeCycleTypeDesc'}
        ]
    };
    let obj = {};
    obj.dataTable = $("#" + tableId).DataTable(option);
    obj.setData = function (data) {
        if (data) {
            this.dataTable.clear();
            this.dataTable.rows.add(data);
            this.dataTable.draw();
        }
    }
    obj.getData = function () {
        return this.dataTable.rows().data().toArray();
    }
    obj.getSelected = function () {
        return this.dataTable.rows('.selected').data().toArray();
    }
    return obj;
}