package io.openapiparser;

public class Keyword {
    private String keyword;
    private boolean required;

    public Keyword (String keyword) {
        this.keyword = keyword;
        this.required = false;
    }

    public Keyword (String keyword, boolean required) {
        this.keyword = keyword;
        this.required = required;
    }

    public String getKeyword () {
        return keyword;
    }

    public boolean isRequired () {
        return required;
    }
}
