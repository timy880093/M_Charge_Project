function dataTableOptionSelectorComponentInitializer(dataSet, callback) {
    if (dataSet && dataSet instanceof Array) {
        let id = new Date().getTime();
        let componentId = id + 'MenuViewComponent';
        let modalId = id + 'Modal';
        let tableId = id + 'DataTable';
        let generateDom = createBootstrapDataTableMenuModalDom({
            modalId: modalId,
            title: '清單',
            tableId: tableId,
            th: ['編號', '名稱']
        });
        //加入未選擇
        let emptyObj = {
            id: undefined,
            name: '未選擇'
        };
        dataSet.push(emptyObj);
        return domTemplateInitializer(
            generateDom.outerHTML,
            componentId,
            function () {
                let optionMenuViewModel = {
                    chosenOption: ko.observable({}),
                    show: function () {
                        $('#' + modalId).modal('show');
                    }, setById: function (id) {
                        let selectedObj = optionMenuViewModel.dataTable.rows().data().toArray().filter(
                            element => element.id == id
                        );
                        if (selectedObj.length == 1) {
                            optionMenuViewModel.chosenOption(selectedObj[0]);
                        }
                    }, clean: function () {
                        optionMenuViewModel.chosenOption(emptyObj);
                    }
                };
                //初始化knockout
                optionMenuViewModel.dataTable = initOptionMenuDataTable(
                    $('#' + tableId), dataSet
                );
                //binding onSelectEvent
                optionMenuViewModel.dataTable.on('select', function (e, dt, type, indexes) {
                    let optionSelected = optionMenuViewModel.dataTable.rows(indexes).data()[0];
                    if (optionSelected) {
                        optionMenuViewModel.chosenOption(optionSelected);
                    } else {
                        optionMenuViewModel.chosenOption({});
                    }
                    $('#' + modalId).modal('hide');
                });
                ko.applyBindings(optionMenuViewModel, document.getElementById(componentId));
                return callback(optionMenuViewModel);
            }
        )
    }
}