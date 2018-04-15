package com.fabuleux.wuntu.billstore.Pojos;

public class Language
{
    private String language;

    private String languageCode;

    public Language(String language,String languageCode) {
        this.language = language;
        this.languageCode = languageCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;

        Language itemCompare = (Language) obj;
        if(itemCompare.getLanguage().equals(this.getLanguage()))
            return true;

        return false;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
