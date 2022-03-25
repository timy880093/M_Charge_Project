function chargePackageMenuComponentInitializer(callback) {
    var date = new Date();
    var componentId = 'chargePackageMenuViewComponent_' + date.getTime();
    var tableId = 'chargePackageMenuDataTable_' + date.getTime();
    var modalId = 'chargePackageMenuModal_' + date.getTime();
    var generateDom = createBootstrapDataTableMenuModalDom({
        modalId: modalId,
        title: '清單',
        tableId: tableId,
        confirmId: 'chargePackageMenuModalChoose_' + date.getTime(),
        th: ['chargePackageId', 'packageRefList', '方案名稱']
    });
    return domTemplateInitializer(
        generateDom.outerHTML,
        componentId,
        function () {
            var chargePackageMenuViewModel = {
                chosenPackage: ko.observable({}),
                show: function () {
                    $('#' + modalId).modal('show');
                }, setById: function (value) {
                    var selectedObj = chargePackageMenuViewModel.chargePackageMenuDataTable.rows().data().toArray().filter(
                        chargePackage => chargePackage.packageId == value
                    );
                    if (selectedObj.length == 1) {
                        chargePackageMenuViewModel.chosenPackage(selectedObj[0]);
                    }
                }, clear: function () {
                    chargePackageMenuViewModel.chosenPackage({});
                }
            };
            //初始化knockout
            chargePackageMenuViewModel.chargePackageMenuDataTable = initChargePackageMenuDataTable(
                $('#' + tableId), dataSourceModule.getAvailablePackageList()
            );
            //binding onSelectEvent
            chargePackageMenuViewModel.chargePackageMenuDataTable.on('select', function (e, dt, type, indexes) {
                var chargePackageMenuSelected = chargePackageMenuViewModel.chargePackageMenuDataTable.rows(indexes).data()[0];
                if (chargePackageMenuSelected) {
                    chargePackageMenuViewModel.chosenPackage(chargePackageMenuSelected);
                } else {
                    chargePackageMenuViewModel.chosenPackage({});
                }
                $('#' + modalId).modal('hide');
            });
            ko.applyBindings(chargePackageMenuViewModel, document.getElementById(componentId));
            return callback(chargePackageMenuViewModel);
        }
    )
}