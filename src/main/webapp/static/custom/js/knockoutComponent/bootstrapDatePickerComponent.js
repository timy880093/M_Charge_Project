function datePickerComponentInitializer(id, callback) {
    let param= {
        language: 'zh-TW',
        enableOnReadonly: false,
        format: 'yyyy/mm/dd'
    };
    return bootstrapDatePickerComponentInitializer(id,param, callback);
}

function yearMonthPickerComponentInitializer(id, callback) {
    let param= {
        language: 'zh-TW',
        enableOnReadonly: false,
        format: 'yyyy/mm',
        viewMode: "months",
        minViewMode: "months"
    };
    return bootstrapDatePickerComponentInitializer(id,param, callback);
}

function bootstrapDatePickerComponentInitializer(id,param, callback) {
    var datePicker = $('#' + id).datepicker(param);
    datePicker.on("change", function (event) {
        var newDate = new Date(event.target.value);
        if (newDate.getTime()) {
            datePickerViewModel.chosenDate(newDate);
        } else {
            datePickerViewModel.chosenDate(undefined);
        }
    });

    var datePickerViewModel = {
        chosenDate: ko.observable(),
        setByValue: function (newValue) {
            try {
                var newDate = new Date(newValue);
                if (newDate instanceof Date) {
                    if (datePickerViewModel.chosenDate() instanceof Date) {
                        if (datePickerViewModel.chosenDate().getTime() != newDate.getTime()) {
                            datePickerViewModel.chosenDate(newDate);
                            datePicker.datepicker("setDate", newDate);
                        }
                    } else {
                        datePickerViewModel.chosenDate(newDate);
                        datePicker.datepicker("setDate", newDate);
                    }
                } else {
                    datePickerViewModel.clean();
                }
            } catch (e) {
                console.log(e);
                return;
            }
        }, clean: function () {
            datePicker.datepicker("setDate", '');
        }, enable: function () {
            $('#' + id).prop('disabled', false);
        }, disable: function () {
            $('#' + id).prop('disabled', true);
        }
    };
    return callback(datePickerViewModel);
}
