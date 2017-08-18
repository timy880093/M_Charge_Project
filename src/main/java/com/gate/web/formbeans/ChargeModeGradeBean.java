package com.gate.web.formbeans;

import dao.ChargeModeGradeEntity;

/**
 * Created by emily on 2017/5/11.
 */
public class ChargeModeGradeBean extends ChargeModeGradeEntity {
    private Integer gradeId[];
    private Integer cntStart[];
    private Integer cntEnd[];
    private Integer price[];

    public Integer[] getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer[] gradeId) {
        this.gradeId = gradeId;
    }

    public Integer[] getCntStart() {
        return cntStart;
    }

    public void setCntStart(Integer[] cntStart) {
        this.cntStart = cntStart;
    }

    public Integer[] getCntEnd() {
        return cntEnd;
    }

    public void setCntEnd(Integer[] cntEnd) {
        this.cntEnd = cntEnd;
    }

    public Integer[] getPrice() {
        return price;
    }

    public void setPrice(Integer[] price) {
        this.price = price;
    }
}
