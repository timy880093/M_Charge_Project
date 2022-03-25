function bindingDomToDataModel(paramId, dataModel) {
    var modelTag = "[" + paramId + "]";
    const modelListeners = document.querySelectorAll(modelTag);
    modelListeners.forEach((listener) => {
        var name = listener.getAttribute(paramId);
        listener.addEventListener('keyup', (event) => {
            dataModel[name] = listener.value;
        });
        listener.addEventListener('change', (event) => {
            dataModel[name] = listener.value;
        });
    });
}

function bindingDataModelToDom(dataRender) {
    //one way binding for gradeEditMaster's gradeList preview
    const bindDataModel = (dataModel) => {
        return new Proxy(dataModel, {
            set(target, property, value) {
                target[property] = value; // default set behaviour
                //不同屬性不能觸發到其它屬性的set方法，不然會有問題。
                dataRender(property); // updates the view every time the state changes
                return true;
            }
        });
    };
    return bindDataModel;
}

function bindingDataModelToDom(setDataRender) {
    const bindDataModel = (dataModel) => {
        return new Proxy(dataModel, {
            set(target, property, value) {
                target[property] = value; // default set behaviour
                //不同屬性不能觸發到其它屬性的set方法，不然會有問題。
                setDataRender(property); // updates the view every time the state changes
                return true;
            }
        });
    };
    return bindDataModel;
}

function bindingDataModelGetToDom(getDataRender) {
    const bindDataModel = (dataModel) => {
        return new Proxy(dataModel, {
            get(target, property, receiver) {
                // if (!target[property]) {
                //     getDataRender(target, property);
                // }
                getDataRender(target, property);
                return target[property];
            }
        });
    };
    return bindDataModel;
}

function createDataRender(dataRender) {
    const render = property => {
        dataRender(property);
    };
    return render;
}

function createParamDataRenderWithTarget(dataRender) {
    const render = (target, property) => {
        dataRender(target, property);
    };
    return render;
}

function defaultModelDataRender(dataModel, attr, property) {
    var queryStr = '[' + attr + '="' + property + '"]';
    var selectedElement = $(queryStr);
    if (selectedElement.length > 0) {
        selectedElement.val(dataModel[property]);
    }
}

function getDataModel(attr, render) {
    const dataRenderConst = createDataRender(render);
    const createDataModel = bindingDataModelToDom(dataRenderConst);
    const dataModel = createDataModel({});
    bindingDomToDataModel(attr, dataModel);
    return dataModel;
}

function getDataCenter(attr, getRender) {
    const dataGetRenderConst = createParamDataRenderWithTarget(getRender);
    const createDataModel = bindingDataModelGetToDom(dataGetRenderConst);
    const dataModel = createDataModel({});
    bindingDomToDataModel(attr, dataModel);
    return dataModel;
}

/**
 * modal event fire 後，回覆false做preventDefault及stopPropagation就不能再被fire不符合需求，只能binding每個使用到modal的open event來解決這個問題。
 */
function bindingFunctionTargetEventHandler(targetId, handler) {
    $('[function-target="' + targetId + '"]').on('click', handler);
}

/**
 * deprecated
 * @param object
 */
function cleanDataModel(object) {
    var paramArray = Object.keys(object);
    for (var i = 0; i < paramArray.length; i++) {
        switch (typeof object[paramArray[i]]) {
            case "string":
                object[paramArray[i]] = '';
                break;
            case "boolean":
                object[paramArray[i]] = false;
                break;
            case "number":
                object[paramArray[i]] = 0;
                break;
            case "object":
                if (object[paramArray[i]] instanceof Array) {
                    object[paramArray[i]] = [];
                }
                break;
            default:
                object[paramArray[i]] = undefined;
                break;
        }
    }
}