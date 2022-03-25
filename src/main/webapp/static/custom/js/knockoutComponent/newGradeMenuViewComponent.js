function gradeMenuViewComponentInitializer(callback) {
    let date = new Date();
    let componentId = 'gradeMenuViewComponent_' + date.getTime();
    let tableId = 'gradeMenuDataTable_' + date.getTime();
    let modalId = 'gradeMenuModal_' + date.getTime();
    let generateDom = createBootstrapDataTableMenuModalDom({
        modalId: modalId,
        title: '清單',
        tableId: tableId,
        size: '',
        confirmId: 'gradeMenuModalChoose_' + date.getTime(),
        th: ['rootId', '級距名稱', '狀態', 'gradeList']
    });
    return domTemplateInitializer(
        generateDom.outerHTML,
        componentId,
        function () {
            let gradeMenuViewModel = {
                chosenGrade: ko.observable({}),
                onConfirm: function () {
                    var newGradeMenuSelected = gradeMenuViewModel.gradeMenuDataTable.rows('.selected').data()[0];
                    if (newGradeMenuSelected) {
                        gradeMenuViewModel.chosenGrade(newGradeMenuSelected);
                    } else {
                        gradeMenuViewModel.chosenGrade({});
                    }
                }, show: function () {
                    $('#' + modalId).modal();
                }, setById: function (id) {
                    var selectedObj = gradeMenuViewModel.gradeMenuDataTable.rows().data().toArray().filter(
                        grade => grade.gradeId == id
                    );
                    if (selectedObj.length == 1) {
                        gradeMenuViewModel.chosenGrade(selectedObj[0]);
                    }
                }
            };
            //初始化knockout
            gradeMenuViewModel.gradeMenuDataTable = initRootGradeMenuDataTable(
                $('#' + tableId), dataSourceModule.getRootGradeList()
            );
            //binding onSelectEvent
            gradeMenuViewModel.gradeMenuDataTable.on('select', function (e, dt, type, indexes) {
                let gradeSelected = gradeMenuViewModel.gradeMenuDataTable.rows(indexes).data()[0];
                if (gradeSelected) {
                    gradeMenuViewModel.chosenGrade(gradeSelected);
                } else {
                    gradeMenuViewModel.chosenGrade({});
                }
                $('#' + modalId).modal('hide');
            });
            ko.applyBindings(gradeMenuViewModel, document.getElementById(componentId));
            return callback(gradeMenuViewModel);
        }
    );
}