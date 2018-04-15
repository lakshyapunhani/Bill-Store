package com.fabuleux.wuntu.billstore.Pojos;

public class Language
{
    private String language;

    public Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        Language itemCompare = (Language) obj;
        if(itemCompare.getLanguage().equals(this.getLanguage()))
            return true;

        return false;
    }
}
