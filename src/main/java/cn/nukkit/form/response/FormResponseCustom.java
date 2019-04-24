package cn.nukkit.form.response;

import java.util.HashMap;

public class FormResponseCustom extends FormResponse {

    private final HashMap<Integer, Object> responses;
    private final HashMap<Integer, FormResponseData> dropdownResponses;
    private final HashMap<Integer, String> inputResponses;
    private final HashMap<Integer, Float> sliderResponses;
    private final HashMap<Integer, FormResponseData> stepSliderResponses;
    private final HashMap<Integer, Boolean> toggleResponses;
    private final HashMap<Integer, String> labelResponses;

    public FormResponseCustom(HashMap<Integer, Object> responses, HashMap<Integer, FormResponseData> dropdownResponses,
                              HashMap<Integer, String> inputResponses, HashMap<Integer, Float> sliderResponses,
                              HashMap<Integer, FormResponseData> stepSliderResponses,
                              HashMap<Integer, Boolean> toggleResponses,
                              HashMap<Integer, String> labelResponses) {
        this.responses = responses;
        this.dropdownResponses = dropdownResponses;
        this.inputResponses = inputResponses;
        this.sliderResponses = sliderResponses;
        this.stepSliderResponses = stepSliderResponses;
        this.toggleResponses = toggleResponses;
        this.labelResponses = labelResponses;
    }

    public HashMap<Integer, Object> getResponses() {
        return this.responses;
    }

    public Object getResponse(int id) {
        return this.responses.get(id);
    }

    public FormResponseData getDropdownResponse(int id) {
        return this.dropdownResponses.get(id);
    }

    public String getInputResponse(int id) {
        return this.inputResponses.get(id);
    }

    public float getSliderResponse(int id) {
        return this.sliderResponses.get(id);
    }

    public FormResponseData getStepSliderResponse(int id) {
        return this.stepSliderResponses.get(id);
    }

    public boolean getToggleResponse(int id) {
        return this.toggleResponses.get(id);
    }

    public String getLabelResponse(int id) {
        return this.labelResponses.get(id);
    }
}
