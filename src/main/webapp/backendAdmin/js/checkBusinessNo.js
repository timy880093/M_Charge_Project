/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/16
 * Time: 下午 6:39
 * To change this template use File | Settings | File Templates.
 */

function isValidGUI(taxId) {
    var invalidList = "00000000,11111111";
    if (/^\d{8}$/.test(taxId) == false || invalidList.indexOf(taxId) != -1) {
        return false;
    }

    var validateOperator = [1, 2, 1, 2, 1, 2, 4, 1],
        sum = 0,
        calculate = function(product) { // 個位數 + 十位數
            var ones = product % 10,
                tens = (product - ones) / 10;
            return ones + tens;
        };
    for (var i = 0; i < validateOperator.length; i++) {
        sum += calculate(taxId[i] * validateOperator[i]);
    }

    return sum % 10 == 0 || (taxId[6] == "7" && (sum + 1) % 10 == 0);
};