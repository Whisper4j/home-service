package com.homeservice.handler;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.beans.PropertyEditorSupport;
@ControllerAdvice
public class RequestBindingAdvice {
    @InitBinder public void strictBinding(WebDataBinder binder) {
        binder.setIgnoreUnknownFields(false);
        binder.setAutoGrowCollectionLimit(100);
        // Spring 可能在 Converter 失败后回退到默认 PropertyEditor，需封住相同的格式边界。
        binder.registerCustomEditor(Long.class, new PropertyEditorSupport() {
            public void setAsText(String text) { setValue(com.homeservice.handler.json.IdDeserializer.parse(text)); }
        });
        binder.registerCustomEditor(Boolean.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                String value = text.strip();
                if (!value.equals("true") && !value.equals("false")) throw new IllegalArgumentException("需要 true/false");
                setValue(Boolean.valueOf(value));
            }
        });
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            public void setAsText(String text) { setValue(text == null ? null : text.strip()); }
        });
        binder.registerCustomEditor(String.class, "password", new PropertyEditorSupport() {
            public void setAsText(String text) { setValue(text); }
        });
    }
}
