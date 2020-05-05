package cz.vse.java;

import cz.vse.java.services.serverSide.Server;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {


        String confPath = null;
        String schemaPath = null;

        if(args.length == 0) {


            ClassLoader classLoader = App.class.getClassLoader();
            File conf = new File(classLoader.getResource("config.xml").getFile());
            File schema = new File(classLoader.getResource("configSchema.xsd").getFile());

            if (conf.exists() && !conf.isDirectory() && schema.exists() && !conf.isDirectory()) {

                confPath = conf.getAbsolutePath();
                schemaPath = schema.getAbsolutePath();

                Server.getInstance().load(confPath, schemaPath);

            } else {

                throw new NullPointerException("Configuration file or schema are not correct or are not present!");
            }

        } else if(args.length != 1) {

            throw new IllegalArgumentException("Not suitable parameters! " +
                    "The only parameter stands for absolute file path to config file! " +
                    "When you want to start the server using default settings, do not pass any parameter!");
        } else {

            ClassLoader classLoader = App.class.getClassLoader();
            File schema = new File(classLoader.getResource("configSchema.xsd").getFile());

            File conf = new File(args[0]);

            if (conf.exists() && !conf.isDirectory() && schema.exists() && !conf.isDirectory()) {

                confPath = conf.getAbsolutePath();
                schemaPath = schema.getAbsolutePath();

                Server.getInstance().load(confPath, schemaPath);

            } else {

                throw new NullPointerException("Given configuration file is wrong, " +
                        "does not exist or schema is not present!");
            }
        }
    }
}
