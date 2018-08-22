package com.redhat.patriot.smarthome_gateway;

import com.redhat.patriot.network_simulator.example.container.config.AppConfig;

import java.util.Arrays;

public class GwController {

    public void startGateway() throws InterruptedException {
            GwManager gwManager = new GwManager();
            String name = "gateway";
            gwManager.newApp("gateway")
                    .from("nimmis/java:openjdk-8-jdk")
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
            gwManager.setTag("app_gateway");
            gwManager.setStartCommand(name, "java -Diot.host=10.40.1.23:8282 -Diot.mqtt.host=10.40.1.23:1883 " +
                    "-Dmqtt.host=127.0.0.1:1883 -Dmobile.host=0.0.0.0:8283 " +
                    "-jar app/target/smart-home-gateway-app-1.0-SNAPSHOT.jar");
            AppConfig appConfig = gwManager.deploy(name);
            Thread.sleep(5000);
            gwManager.destroy(name);

    }
}
