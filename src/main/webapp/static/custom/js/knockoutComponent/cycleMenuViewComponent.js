function chargeCycleMenuComponentInitializer(callback) {
    return cycleMenuComponentInitializer(callback, dataSourceModule.getChargeCycleList());
}

function cycleMenuComponentInitializer(callback) {
    return cycleMenuComponentInitializer(callback, dataSourceModule.getCycleList());
}

function calculateCycleMenuComponentInitializer(callback) {
    return cycleMenuComponentInitializer(callback, dataSourceModule.getCalculateCycleList());
}

function cycleMenuComponentInitializer(callback, dataSet) {
    var date = new Date();
    var componentId = 'cycleMenuViewComponent_' + date.getTime();
    var tableId = 'cycleMenuDataTable_' + date.getTime();
    var modalId = 'cycleMenuModal_' + date.getTime();
    var generateDom = createBootstrapDataTableMenuModalDom({
        modalId: modalId,
        title: '清單',
        tableId: tableId,
        confirmId: 'cycleMenuModalChoose_' + date.getTime(),
        th: ['區間名稱', '編碼']
    });
    return domTemplateInitializer(
        generateDom.outerHTML,
        componentId,
        function () {
            var cycleMenuViewModel = {
                chosenCycle: ko.observable({}),
                show: function () {
                    $('#' + modalId).modal('show');
                }, setById: function (value) {
                    var selectedObj = cycleMenuViewModel.cycleMenuDataTable.rows().data().toArray().filter(
                        cycleType => cycleType.id == value
                    );
                    if (selectedObj.length == 1) {
                        cycleMenuViewModel.chosenCycle(selectedObj[0]);
                    }
                }, clear: function () {
                    cycleMenuViewModel.chosenCycle({});
                }
            };
            //初始化knockout
            cycleMenuViewModel.cycleMenuDataTable = initCycleMenuDataTable(
                $('#' + tableId), dataSet
            );
            //binding onSelectEvent
            cycleMenuViewModel.cycleMenuDataTable.on('select', function (e, dt, type, indexes) {
                var cycleMenuSelected = cycleMenuViewModel.cycleMenuDataTable.rows(indexes).data()[0];
                if (cycleMenuSelected) {
                    cycleMenuViewModel.chosenCycle(cycleMenuSelected);
                } else {
                    cycleMenuViewModel.chosenCycle({});
                }
                $('#' + modalId).modal('hide');
            });
            ko.applyBindings(cycleMenuViewModel, document.getElementById(componentId));
            return callback(cycleMenuViewModel);
        }
    )
}