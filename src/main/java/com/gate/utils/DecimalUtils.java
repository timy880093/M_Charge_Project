package com.gate.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 協助作小數點轉型，需用於計算可直接用BigDecimal要顯示則可以用這個轉。
 * 這個方法做的事情只是把小數點後方的無意義零捨去取得原來實際的值。
 * todo:想法：使用正規表達式找出小數點及非小數點，接著再根據小數點及非小數點做decimalFormat來移除無意義的零。
 * 盡可能限制使用者只能傳入數字，可以慮重寫成吃BigDecimal類型，會比較有效果，畢竟只有BigDeciaml會需要移除無意義的零。
 * 用BigDecimal的理由是因為他同時可以處理Double的資料及Long的資料，就不用害怕其它程式轉過來的值是什麼。
 * Created by Eason on 9/2/2015.
 */
public class DecimalUtils {
    public static String getStrippingValue(Object value){
        try{
            String pattern;
            BigDecimal decimalValue = new BigDecimal(String.valueOf(value));
            String strValue = decimalValue.toPlainString();
            //過瀘零開頭的數字
            if((!(strValue.startsWith("0") && strValue.length()>1))
                    && (strValue.matches("[0-9]+\\.[0]+$") || value instanceof Long)){
                //為了匹配BigDecimal轉出來的多零整數。
                pattern = "############;-############";
            }else if(strValue.matches("[0-9]+\\.[0-9]+") || value instanceof Double){
                pattern = "##############.#####;-##############.#####";
            }else{
                throw new Exception("Not Numeric Value");
            }

            return new DecimalFormat(pattern).format(decimalValue);
        }catch(Exception e){
            try{
                return new BigDecimal(String.valueOf(value)).stripTrailingZeros().toPlainString();
            }catch (Exception ex){
                return String.valueOf(value);
            }
        }
    }
}
