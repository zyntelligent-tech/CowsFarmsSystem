package com.jozzz.records;

import com.jozzz.util.Element;

import java.util.ArrayList;

public record DataTab(String title, ArrayList<String[]> data) {
    public DataTab(String title, ArrayList<String[]> data) {
        this.title = title + " (" + Element.decimalFormat(data.size()) + " รายการ)";
        this.data = data;
    }
}
