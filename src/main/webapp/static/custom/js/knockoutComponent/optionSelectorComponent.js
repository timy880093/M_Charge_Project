function optionSelectorInitializer(id, dataArr, callback) {
    var optionSelectorViewModel;
    if (dataArr instanceof Array) {
        //增加一個什麼也不是的選項
        var emptyOption = new Option(
            '未選擇',
            ''
        );
        $('#' + id).append(emptyOption);
        dataArr.forEach(element => {
            var newOption = new Option(
                element.name, element.id
            );
            $('#' + id).append(newOption);
        });
        var optionSelectorObj = $('#' + id).select2();
        optionSelectorObj.on('select2:select', function (e) {
            if (e.params.data.id) {
                var targetOption = optionSelectorViewModel.getOptionById(e.params.data.id);
                optionSelectorViewModel.chosenOption(targetOption);
            } else {
                optionSelectorViewModel.chosenOption({});
            }
        });
        optionSelectorViewModel = {
            availableOption: dataArr,
            chosenOption: ko.observable({}),
            getOptionById: function (id) {
                var targetOptionArr = optionSelectorViewModel.availableOption.filter(option => {
                    if (option.id == id) {
                        return true;
                    } else {
                        return false;
                    }
                });
                if (targetOptionArr.length == 1) {
                    return targetOptionArr[0];
                }
            },
            setById: function (id) {
                var currentId = optionSelectorViewModel.chosenOption().id;
                if (currentId != id) {
                    optionSelectorObj.val(id).trigger("change");
                    optionSelectorViewModel.chosenOption(
                        optionSelectorViewModel.getOptionById(id)
                    );
                } else {
                    return;
                }
            }
        };
    }
    return callback(optionSelectorViewModel);
}
