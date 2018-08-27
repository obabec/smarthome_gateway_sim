package com.redhat.patriot.smarthome_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Main.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.
            getLogger(com.redhat.patriot.network_simulator.example.Main.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        GwController gwController = new GwController();
        LOGGER.info("Gateway build started");
        gwController.startGateway();
        }


    }




