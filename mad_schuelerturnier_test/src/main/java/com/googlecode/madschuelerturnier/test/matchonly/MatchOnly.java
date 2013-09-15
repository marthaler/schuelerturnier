package com.googlecode.madschuelerturnier.test.matchonly;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

public class MatchOnly {
    private static final Logger LOG = Logger.getLogger(MatchOnly.class);

    public static boolean restart = true;

    // create the command line parser
    public static void main(String[] args) {

        final CommandLineParser parser = new PosixParser();

        // create the Options
        final Options options = new Options();

        try {

            while (1 == 1) {


                restart = false;
                SeleniumEintragerThread eintrager = new SeleniumEintragerThread();
                eintrager.start();

                SeleniumKontrolliererThread kontrollierer = new SeleniumKontrolliererThread();
                kontrollierer.start();

                SeleniumSpeakerThread speaker = new SeleniumSpeakerThread();
                speaker.start();


                speaker.join();
                restart = true;
                kontrollierer.join();
                restart = true;
                eintrager.join();
                restart = true;


            }


        } catch (final Exception exp) {

            LOG.error(exp.getMessage(), exp);

            MatchOnly.printHelp(options);

            System.exit(1);
        }
    }

    private static void printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar schuelerturnier-X.X.X.jar", options);
    }
}
