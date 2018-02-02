package com.gate.core.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by simon on 2014/7/7.
 */
public abstract class BaseFormBean implements Serializable {

    public abstract List<String> validateData();
}
