package vinted;

import java.util.Map;

public class ApplicationContext {

    private Map<Class, Object> beans;

    public ApplicationContext(Map<Class, Object> beans) {
        this.beans = beans;
    }
}
