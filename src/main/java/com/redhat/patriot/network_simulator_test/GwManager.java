package com.redhat.patriot.network_simulator_test;

import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.container.config.AppConfig;
import com.redhat.patriot.network_simulator.example.image.docker.DockerImage;
import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The type Docker controller.
 */
public class GwManager {
    private DockerFileBuilder dockerFileBuilder = new DockerFileBuilder();
    private String appName;
    private List<String> args;
    private String appPath;
    private String tag;
    private DockerManager dockerManager = new DockerManager();
    private HashMap<String, DockerContainer> gwContainers = new HashMap<>();

    /**
     * Genererate enviroment.
     */
    public AppConfig deploy() {
        try {
            Path tmpDir = Files.createTempDirectory(Paths.get("/tmp"), "dockerfiles");
            Path dockerfile = Files.createTempFile(tmpDir, "tempDockerfile", "");
            dockerFileBuilder.write(dockerfile);
            buildImage(new HashSet<>(Arrays.asList(tag)), dockerfile);
            Container appCont = createAndStart(appName, tag);

            String command = "java";
            for (String arg : args) {
                command += " -D" + arg;
            }
            command = command + " -jar " + appPath;
            dockerManager.runCommand(appCont, command);
            AppConfig appConfig = new AppConfig(dockerManager.findIpAddress(appCont), "running", appCont.getId());

            return appConfig;

        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        }

    }
    public void destroy(AppConfig appConfig) {
        dockerManager.destroyContainer(gwContainers.get(appConfig.getContainerId()));
    }

    private void buildImage(Set<String> tags, Path dockerfile) {
        DockerImage dockerImage = new DockerImage(dockerManager);
        dockerImage.buildImage(tags, dockerfile.toAbsolutePath().toString());
    }

    private DockerContainer createAndStart(String name, String tag) {
        DockerContainer appCont = (DockerContainer) dockerManager.createContainer(name, tag);
        gwContainers.put(appCont.getId(), appCont);
        dockerManager.startContainer(appCont);
        return appCont;
    }

    public DockerFileBuilder newApp(String name) {
        this.appName = name;
        return dockerFileBuilder;
    }

    public void setProps(List<String> args, String appPath, String tag) {
        this.args = args;
        this.appPath = appPath;
        this.tag = tag;
    }


}
