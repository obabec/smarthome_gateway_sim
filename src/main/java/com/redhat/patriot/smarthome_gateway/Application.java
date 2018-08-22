package com.redhat.patriot.smarthome_gateway;

import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.container.config.AppConfig;
import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;

public class Application {
    private DockerFileBuilder dockerFileBuilder;
    private AppConfig appConfig;
    private DockerContainer dockerContainer;

    public Application(DockerFileBuilder dockerFileBuilder) {
        this.dockerFileBuilder = dockerFileBuilder;
    }

    public Application(DockerFileBuilder dockerFileBuilder, AppConfig appConfig) {
        this.dockerFileBuilder = dockerFileBuilder;
        this.appConfig = appConfig;
    }

    public DockerFileBuilder getDockerFileBuilder() {
        return dockerFileBuilder;
    }

    public void setDockerFileBuilder(DockerFileBuilder dockerFileBuilder) {
        this.dockerFileBuilder = dockerFileBuilder;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public DockerContainer getDockerContainer() {
        return dockerContainer;
    }

    public void setDockerContainer(DockerContainer dockerContainer) {
        this.dockerContainer = dockerContainer;
    }
}
