package cn.nukkit.form.element;

import java.util.ArrayList;
import java.util.List;

public class ElementDropdown extends Element {

    private final String type = "dropdown"; //This variable is used for JSON import operations. Do NOT delete :) -- @Snake1999
    private String text = "";
    private List<String> options;
    private int defaultOptionIndex = 0;

    public ElementDropdown(String text) {
        this(text, new ArrayList<>());
    }

    public ElementDropdown(String text, List<String> options) {
        this(text, options, 0);
    }

    public ElementDropdown(String text, List<String> options, int defaultOption) {
        this.text = text;
        this.options = options;
        this.defaultOptionIndex = defaultOption;
    }

    public int getDefaultOptionIndex() {
        return this.defaultOptionIndex;
    }

    public void setDefaultOptionIndex(int index) {
        if (index >= this.options.size()) {
            return;
        }
        this.defaultOptionIndex = index;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addOption(String option) {
        this.addOption(option, false);
    }

    public void addOption(String option, boolean isDefault) {
        this.options.add(option);
        if (isDefault) {
            this.defaultOptionIndex = this.options.size() - 1;
        }
    }
}
