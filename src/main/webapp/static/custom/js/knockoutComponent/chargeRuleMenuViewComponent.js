function chargeRuleMenuViewComponentInitializer(callback) {
    let date = new Date();
    let componentId = 'chargeRuleMenuViewComponent_' + date.getTime();
    let tableId = 'chargeRuleMenuDataTable_' + date.getTime();
    let modalId = 'chargeRuleMenuModal_' + date.getTime();
    let generateDom = createBootstrapDataTableMenuModalDom({
        modalId: modalId,
        title: '清單',
        tableId: tableId,
        size: 'xl',
        confirmId: 'chargeRuleMenuModalChoose_' + date.getTime(),
        th: ['id', '項目名稱', '級距名稱', '繳費類別', '自訂類別', '計費來源']
    });
    domTemplateInitializer(
        generateDom.outerHTML,
        componentId,
        function () {
            let chargeRuleMenuViewModel = {
                chosenChargeRule: ko.observable({}),
                onConfirm: function () {
                    let chargeRuleMenuSelected = chargeRuleMenuViewModel.chargeRuleMenuDataTable.rows('.selected').data()[0];
                    if (chargeRuleMenuSelected) {
                        chargeRuleMenuViewModel.chosenChargeRule(chargeRuleMenuSelected);
                    } else {
                        chargeRuleMenuViewModel.chosenChargeRule({});
                    }
                }, show: function () {
                    $('#' + modalId).modal();
                }, setById: function (id) {
                    let selectedObj = chargeRuleMenuViewModel.chargeRuleMenuDataTable.rows().data().toArray().filter(
                        chargeRule => chargeRule.ruleId == id
                    );
                    if (selectedObj.length == 1) {
                        chargeRuleMenuViewModel.chosenChargeRule(selectedObj[0]);
                    }
                }
            };
            //初始化knockout
            chargeRuleMenuViewModel.chargeRuleMenuDataTable = initChargeRuleMenuDataTable(
                $('#' + tableId), dataSourceModule.getChargeRuleList()
            );
            //binding onSelectEvent
            chargeRuleMenuViewModel.chargeRuleMenuDataTable.on('select', function (e, dt, type, indexes) {
                let chargeRuleSelected = chargeRuleMenuViewModel.chargeRuleMenuDataTable.rows(indexes).data()[0];
                if (chargeRuleSelected) {
                    chargeRuleMenuViewModel.chosenChargeRule(chargeRuleSelected);
                } else {
                    chargeRuleMenuViewModel.chosenChargeRule({});
                }
                $('#' + modalId).modal('hide');
            });
            ko.applyBindings(chargeRuleMenuViewModel, document.getElementById(componentId));
            callback(chargeRuleMenuViewModel);
        }
    );
}