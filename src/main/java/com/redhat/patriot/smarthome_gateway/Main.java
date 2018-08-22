package com.redhat.patriot.smarthome_gateway;

import com.redhat.patriot.network_simulator.example.args.CommandLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
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
        CommandLineParser cmdArgs = new CommandLineParser();
        CmdLineParser parser = new CmdLineParser(cmdArgs);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            e.printStackTrace();
        }

        if (cmdArgs.isClean()) {
            LOGGER.info("Cleaning docker.");
        } else {
            LOGGER.info("Gateway build started");
            gwController.startGateway();
        }


    }



}
