package com.redhat.patriot.network_simulator_test;

import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.container.config.AppConfig;
import com.redhat.patriot.network_simulator.example.image.docker.DockerImage;
import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The type Docker controller.
 */
public class GwManager {
    private List<String> args;
    private String appPath;
    private String tag;
    private DockerManager dockerManager = new DockerManager();
    private HashMap<String, Application> apps = new HashMap<>();

    /**
     * Genererate enviroment.
     */
    public AppConfig deploy(String name) {
            buildImage(name);
            apps.get(name).setDockerContainer(createAndStart(name, tag));
            dockerManager.runCommand(apps.get(name).getDockerContainer(), prepareCommand());
            AppConfig appConfig =
                    new AppConfig(dockerManager.findIpAddress(apps.get(name).getDockerContainer()), "running");
            return appConfig;
    }

    private String prepareCommand() {
        String command = "java";
        for (String arg : args) {
            command += " -D" + arg;
        }
        return command + " -jar " + appPath;
    }

    public void destroy(String name) {
        dockerManager.destroyContainer(apps.get(name).getDockerContainer());
    }

    private void buildImage(String name) {

        try {
            Path tmpDir = Files.createTempDirectory(Paths.get("/tmp"), "dockerfiles");
            Path dockerfile = Files.createTempFile(tmpDir, "tempDockerfile", "");
            apps.get(name).getDockerFileBuilder().write(dockerfile);
            buildImage(new HashSet<>(Arrays.asList(tag)), dockerfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildImage(Set<String> tags, Path dockerfile) {
        DockerImage dockerImage = new DockerImage(dockerManager);
        dockerImage.buildImage(tags, dockerfile.toAbsolutePath().toString());
    }

    private DockerContainer createAndStart(String name, String tag) {
        DockerContainer appCont = (DockerContainer) dockerManager.createContainer(name, tag);
        dockerManager.startContainer(appCont);
        return appCont;
    }

    public DockerFileBuilder newApp(String name) {
        DockerFileBuilder dockerFileBuilder = new DockerFileBuilder();
        apps.put(name, new Application(dockerFileBuilder));
        return dockerFileBuilder;
    }

    public void setProps(List<String> args, String appPath, String tag) {
        this.args = args;
        this.appPath = appPath;
        this.tag = tag;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
