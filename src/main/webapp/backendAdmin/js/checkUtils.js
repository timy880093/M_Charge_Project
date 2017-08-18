
//檢查手機條碼 : 1.第一碼務必為/ 2.後面為7碼(英文，數字及+-.三個符號) 3.共8碼
function checkPhoneCode(value){
    return /[\/]([^\W_]|\+|\.|-){7}$/.test(value);
}

//檢查自然人憑證 : 1.第1,2碼務必為英文 2.後面為14碼(數字) 3.共16碼
function checkNaturalPerson(value){
    return /[a-zA-Z]{2}[\d]{14}/.test(value);
}

//檢查愛心碼 : 1.全部為數字 2.最少3碼 3.最多7碼
function checkNpoban(value){
    return /^[0-9]{3,7}$/.test(value);
}

//檢查會員類別 : 共6碼(英文，數字)
function checkMemberType(value){
    return /[a-zA-Z0-9]{6}/.test(value);
}

//檢查會員號碼：共20碼(英文，數字)
function checkMemberNumber(value){
    return /[a-zA-Z0-9]{1,20}/.test(value);
}