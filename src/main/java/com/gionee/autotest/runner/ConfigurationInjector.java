package com.gionee.autotest.runner;

public class ConfigurationInjector {
    private static Configuration configuration;

    public static void setConfiguration(Configuration configuration) {
        ConfigurationInjector.configuration = configuration;
    }

    public static Configuration configuration() {
        return configuration;
    }

}