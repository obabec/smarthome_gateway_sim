package com.redhat.patriot.network_simulator_test;

import com.redhat.patriot.network_simulator.example.container.config.AppConfig;
import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;

import java.util.Arrays;

public class GwController {

    public void startGateway() {
            GwManager gwManager = new GwManager();
            DockerFileBuilder dockerFileBuilder = new DockerFileBuilder();
            dockerFileBuilder.from("nimmis/java:openjdk-8-jdk")
                    .run("echo \"deb http://archive.ubuntu.com/ubuntu trusty main universe\" " +
                            "> /etc/apt/sources.list")
                    .run("apt-get -y update")
                    .run("echo 'deb http://us.archive.ubuntu.com/ubuntu/ artful main restricted universe multiverse' " +
                            ">> /etc/apt/sources.list")
                    .run("echo 'deb-src http://us.archive.ubuntu.com/ubuntu/ artful main restricted universe " +
                            "multiverse' >> /etc/apt/sources.list")
                    .run(Arrays.asList("apt-get -y update", "apt-get -y install git maven"))
                    .workdir("/")
                    .run("git clone https://github.com/ficap/smart-home-gateway.git")
                    .run(Arrays.asList("cd smart-home-gateway", "git checkout dummy-test", "mvn package"))
                    .entrypoint("tail -f /dev/null")
                    .workdir("/smart-home-gateway/");
            AppConfig appConfig = gwManager.deployGateway(dockerFileBuilder,"10.40.1.23:8282",
                    "10.40.1.23:9292", "0.0.0.0:8283");
            gwManager.destroyGw(appConfig);

    }
}
