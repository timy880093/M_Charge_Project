function setSearchValue(field0,value0,field1,value1,field2,value2,field3,value3,field4,value4){
    var sdata ={
        searchField: [field0,field1,field2,field3,field4],
        searchString:[value0,value1,value2,value3,value4]
    }
    return sdata;
}

/**
 * 之前的setSearchValue不好用，直接讓他傳進兩個陣列做json的格式就可以了。
 * @param searchField
 * @param searchString
 * @returns {{searchField: *, searchString: *}}
 */
function setSearchValueByArray(searchField,searchString){
    var sdata = {
        searchField: checkIfNotArray(searchField),
        searchString: checkIfNotArray(searchString)
    }
    return sdata;
}

/**
 * 如果進來的資料不是陣列的話就什麼都不回傳，至少不能讓JS資料錯誤，不然頁面會出現沒有辦法預期的情況。
 * @param array
 * @returns {*}
 */
function checkIfNotArray(array){
    if(!array.constructor === Array){
        return
    }else{
        return array;
    }
}