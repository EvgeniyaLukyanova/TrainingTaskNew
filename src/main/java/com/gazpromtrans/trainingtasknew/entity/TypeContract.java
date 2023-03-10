package com.gazpromtrans.trainingtasknew.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum TypeContract implements EnumClass<Integer> {

    FIXPRICE(10),
    TIMEANDMATERIAL(20),
    OUTSTAFF(30);

    private Integer id;

    TypeContract(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static TypeContract fromId(Integer id) {
        for (TypeContract at : TypeContract.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}