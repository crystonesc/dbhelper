package com.xkx.config.mysql;

import com.xkx.config.Meta;

public class MysqlMeta extends Meta {

    private boolean notNull;
    private boolean key;
    private String comment;

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }








}
