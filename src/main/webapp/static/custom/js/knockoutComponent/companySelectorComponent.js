function billableCompanySelectorInitializer(id, callback) {
    let availableCompanyList = dataSourceModule.getBillableCompanyList();
    let viewModel = select2RegisterAndViewModelWrapper(id, availableCompanyList);
    return callback(viewModel);
}

function generalCompanySelectorInitializer(id, callback) {
    let availableCompanyList = dataSourceModule.getCompanyList();
    let viewModel = select2RegisterAndViewModelWrapper(id, availableCompanyList);
    return callback(viewModel);
}


function companySelectorDomUpdate(id, companyList) {
    //增加一個什麼也不是的選項
    let emptyOption = new Option(
        '未選擇',
        ''
    );
    let childHtml = emptyOption.outerHTML;
    companyList.forEach(company => {
        let newOption = new Option(
            company.name + '(' + company.businessNo + ')',
            company.companyId
        );
        childHtml += newOption.outerHTML;
    });
    let element = document.getElementById(id);
    element.innerHTML = childHtml;
}

/**
 *  用嚴格型別判定會過不了，在修正嚴格比較===之前，先確認資料不需要轉型
 * @param id
 * @param availableCompanyList
 * @returns {{chosenCompany: *, getCompanyById: ((function(*): (*|undefined))|*), setById: companySelectorViewModel.setById}}
 */
function select2RegisterAndViewModelWrapper(id, availableCompanyList) {
    companySelectorDomUpdate(id, availableCompanyList);
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
        getCompanyById: function (companyId) {
            let targetCompanyArr = availableCompanyList.filter(company => {
                return company.companyId == companyId;
            });
            if (targetCompanyArr.length === 1) {
                return targetCompanyArr[0];
            }
        },
        setById: function (companyId) {
            let currentId = companySelectorViewModel.chosenCompany().companyId;
            if (currentId != companyId) {
                companySelectorObj.val(companyId).trigger("change");
                companySelectorViewModel.chosenCompany(
                    companySelectorViewModel.getCompanyById(companyId)
                );
            } else {
                return;
            }
        }
    };
    return companySelectorViewModel;
}
