package com.fabuleux.wuntu.billstore.Pojos;

public class SelectableLanguage extends Language
{
    public boolean isSelected = false;
    public SelectableLanguage(Language language,boolean isSelected)
    {
        super(language.getLanguage(),language.getLanguageCode());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
