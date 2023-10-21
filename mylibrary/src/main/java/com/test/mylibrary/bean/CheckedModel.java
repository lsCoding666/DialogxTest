package com.test.mylibrary.bean;

public class CheckedModel {
    private boolean isChecked = false;
    private String content;

    public CheckedModel() {}

    public CheckedModel(boolean isChecked, String content) {
        this.isChecked = isChecked;
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
