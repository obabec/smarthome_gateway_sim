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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The type Docker controller.
 */
public class GwManager {
    private DockerManager dockerManager = new DockerManager();
    private HashMap<String, DockerContainer> gwContainers = new HashMap<>();
    /**
     * Genererate enviroment.
     */
    public AppConfig deployGateway(DockerFileBuilder dockerFileBuilder,
                                   String iotHost, String wsHost, String mobileHost) {
        AppConfig appConfig = null;
        try {
            Path tmpDir = Files.createTempDirectory(Paths.get("/tmp"), "dockerfiles");
            Path dockerfile = Files.createTempFile(tmpDir, "tempDockerfile", "");
            dockerFileBuilder.write(dockerfile);
            DockerManager dockerManager = new DockerManager();
            String tagApp = "smart_home_gateway:01";
            DockerImage dockerImage = new DockerImage(dockerManager);
            dockerImage.buildImage(new HashSet<String>(Arrays.asList(tagApp)), dockerfile.toAbsolutePath().toString());

            Container smartGateway = dockerManager.createContainer("smart_gateway", tagApp);
            gwContainers.put(smartGateway.getId(), (DockerContainer) smartGateway);
            dockerManager.startContainer(smartGateway);
            dockerManager.runCommand(smartGateway, "java -Diot.host=" + iotHost +
                    " -Diot.ws.host=" + wsHost + " -Dmobile.host=" + mobileHost +
                    " -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager " +
                    "-jar app/target/smart-home-gateway-app-1.0-SNAPSHOT.jar");
            appConfig = new AppConfig(dockerManager.findIpAddress(smartGateway), "running", smartGateway.getId());
            return appConfig;

        } catch (Exception e ) {
            e.printStackTrace();
            destroyGw(appConfig);
            return null;
        }

    }
    public void destroyGw(AppConfig appConfig) {
        dockerManager.destroyContainer(gwContainers.get(appConfig.getContainerId()));
    }


}
