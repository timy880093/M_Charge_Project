function bootstrapSwitchViewComponentInitializer(id, callback) {
    let bootstrapSwitchObj = $('#' + id).bootstrapSwitch();
    bootstrapSwitchObj.bootstrapSwitch('onSwitchChange',
        function (event, state) {
            bootstrapSwitchViewModel.chosenValue(state);
        }
    );

    let bootstrapSwitchViewModel = {
        switch: bootstrapSwitchObj,
        chosenValue: ko.observable(),
        setByValue: function (value) {
            var currentValue = bootstrapSwitchViewModel.chosenValue();
            if (currentValue != value) {
                bootstrapSwitchViewModel.chosenValue(value);
                bootstrapSwitchViewModel.switch.bootstrapSwitch('state', value);
            }
        }, disable: function () {
            bootstrapSwitchViewModel.switch.bootstrapSwitch('disabled', true);
        }, enable: function () {
            bootstrapSwitchViewModel.switch.bootstrapSwitch('disabled', false);
        }
    };
    return callback(bootstrapSwitchViewModel);
}