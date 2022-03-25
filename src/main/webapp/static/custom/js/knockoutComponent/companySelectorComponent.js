function companySelectorInitializer(id, callback) {
    //增加一個什麼也不是的選項
    let emptyOption = new Option(
        '未選擇',
        ''
    );
    $('#' + id).append(emptyOption);
    let availableCompanyList = dataSourceModule.getCompanyList();
    availableCompanyList.forEach(company => {
        let newOption = new Option(
            company.name + '(' + company.businessNo + ')',
            company.companyId
        );
        $('#' + id).append(newOption);
    });
    let companySelectorObj = $('#' + id).select2();
    companySelectorObj.on('select2:select', function (e) {
        if (e.params.data.id) {
            let targetCompany = companySelectorViewModel.getCompanyById(e.params.data.id);
            companySelectorViewModel.chosenCompany(targetCompany);
        } else {
            companySelectorViewModel.chosenCompany({});
        }
    });
    let companySelectorViewModel = {
        chosenCompany: ko.observable({}),
        getCompanyById: function (id) {
            var targetCompanyArr = availableCompanyList.filter(company => {
                if (company.companyId == id) {
                    return true;
                } else {
                    return false;
                }
            });
            if (targetCompanyArr.length == 1) {
                return targetCompanyArr[0];
            }
        },
        setById: function (id) {
            var currentId = companySelectorViewModel.chosenCompany().companyId;
            if (currentId != id) {
                companySelectorObj.val(id).trigger("change");
                companySelectorViewModel.chosenCompany(
                    companySelectorViewModel.getCompanyById(id)
                );
            } else {
                return;
            }
        }
    };
    return callback(companySelectorViewModel);
}
