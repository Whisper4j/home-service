package com.homeservice.handler;

import com.homeservice.common.constant.MessageConstant;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;

/**
 * 请求参数绑定增强类
 * 统一配置请求参数的严格绑定规则
 */
@ControllerAdvice
public class RequestBindingAdvice {
    /**
     * 创建严格请求参数绑定器
     */
    @InitBinder
    public void strictBinding(WebDataBinder binder) {
        binder.setIgnoreUnknownFields(false);
        binder.setAutoGrowCollectionLimit(100);
        // Spring 可能在 Converter 失败后回退到默认 PropertyEditor，需封住相同的格式边界。
        binder.registerCustomEditor(
                Long.class,
                new PropertyEditorSupport() {
                    /**
                     * 解析并设置请求参数文本
                     */
                    public void setAsText(String text) {
                        setValue(com.homeservice.handler.json.IdDeserializer.parse(text));
                    }
                });
        binder.registerCustomEditor(
                Boolean.class,
                new PropertyEditorSupport() {
                    /**
                     * 解析并设置请求参数文本
                     */
                    public void setAsText(String text) {
                        String value = text.strip();
                        if (!value.equals("true") && !value.equals("false"))
                            throw new IllegalArgumentException(MessageConstant.BOOLEAN_REQUIRED);
                        setValue(Boolean.valueOf(value));
                    }
                });
        binder.registerCustomEditor(
                String.class,
                new PropertyEditorSupport() {
                    /**
                     * 解析并设置请求参数文本
                     */
                    public void setAsText(String text) {
                        setValue(text == null ? null : text.strip());
                    }
                });
        binder.registerCustomEditor(
                String.class,
                "password",
                new PropertyEditorSupport() {
                    /**
                     * 解析并设置请求参数文本
                     */
                    public void setAsText(String text) {
                        setValue(text);
                    }
                });
    }
}
