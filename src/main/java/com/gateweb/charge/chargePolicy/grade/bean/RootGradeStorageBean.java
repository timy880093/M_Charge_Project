package com.gateweb.charge.chargePolicy.grade.bean;

import com.gateweb.orm.charge.entity.NewGrade;

import java.util.ArrayList;
import java.util.List;

public class RootGradeStorageBean {
    NewGrade root;
    List<NewGrade> childList = new ArrayList<>();
    List<NewGrade> removeList = new ArrayList<>();

    public RootGradeStorageBean() {
    }

    public RootGradeStorageBean(NewGrade root, List<NewGrade> childList, List<NewGrade> removeList) {
        this.root = root;
        this.childList = childList;
        this.removeList = removeList;
    }

    public NewGrade getRoot() {
        return root;
    }

    public void setRoot(NewGrade root) {
        this.root = root;
    }

    public List<NewGrade> getChildList() {
        return childList;
    }

    public void setChildList(List<NewGrade> childList) {
        this.childList = childList;
    }

    public List<NewGrade> getRemoveList() {
        return removeList;
    }

    public void setRemoveList(List<NewGrade> removeList) {
        this.removeList = removeList;
    }
}
