package com.chharkoi.app.domain.util;

import com.chharkoi.app.domain.Setting;
import com.chharkoi.app.enums.AppSettingType;
import com.chharkoi.app.service.SettingService;

public class AppSetting {
    public static final AppSetting DM_VERSION = AppSetting.builder()
            .setName("datamodel.version")
            .setDefaultValue(0)
            .build();

    private String name;
    private AppSettingType type;
    private Object defaultValue;

    AppSetting(Builder builder) {
        setName(builder.getName());
        setType(builder.getType());
        setDefaultValue(builder.getDefaultValue());
    }

    public static Builder builder() {
        return new Builder();
    }

    public AppSettingType getType() {
        return type;
    }

    public void setType(AppSettingType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getValue(SettingService service) {
        Object r = null;
        Setting setting = service.findOneByName(getName());
        if (setting != null) {
            String json = setting.getValue();
            r = Utils.fromJson(json, getDefaultValue().getClass());
        }
        if (r == null) {
            r = getDefaultValue();
        }
        return r;
        //this is to commit
    }

    public void saveValue(SettingService service, Object value) {
        String json = Utils.toJson(value);
        service.save(getName(), json);
    }

    public static class Builder {
        private String name = "";
        private AppSettingType type = AppSettingType.DB;
        private Object defaultValue = null;

        public AppSetting build() {
            return new AppSetting(this);
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public AppSettingType getType() {
            return type;
        }

        public Builder setType(AppSettingType type) {
            this.type = type;
            return this;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public Builder setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }
}
