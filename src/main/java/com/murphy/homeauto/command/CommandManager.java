package com.murphy.homeauto.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class CommandManager {

    private static ExecutorService fixedPool;

    public CommandManager(int poolSize) {
        fixedPool = Executors.newFixedThreadPool(poolSize);
    }

    public CommandManager() {
        fixedPool = Executors.newFixedThreadPool(2);
    }

    public String readWaterSample() {
        String command = "python temperature.py";
        String directory = "/samba/scripts";

        InvokeCommand inCommand = new InvokeCommand(directory, command);
        inCommand.run();
        return inCommand.getCommandOutput();
    }

    public boolean createGif(String imageDirectory, List<String> imageNames, String gifName,
                             String destinationDirectory) {
        // FIXME: Move this to a static file
        StringBuilder command = new StringBuilder("convert -delay 100 ");
        for (String imageName : imageNames) {
            command.append(imageName).append(" ");
        }

        String gifFilepath = destinationDirectory + "/" + gifName;
        command.append(" -loop 0 ").append(gifFilepath);

        // Invoke this within the current thread
        InvokeCommand inCommand = new InvokeCommand(imageDirectory, command.toString());
        inCommand.run();
        return inCommand.wasSuccessful();
    }


    public boolean createAnimatedWebP(String imageDirectory, List<String> imageNames, String webPName,
                                      String destinationDirectory) {
        // FIXME: Move this to a static file
        StringBuilder command = new StringBuilder("img2webp -d 1000 -lossy -q 75 ");
        for (String imageName : imageNames) {
            command.append(imageName).append(" ");
        }

        String webPFilePath = destinationDirectory + "/" + webPName;
        command.append(" -o ").append(webPFilePath);

        // Invoke this within the current thread
        InvokeCommand inCommand = new InvokeCommand(imageDirectory, command.toString());
        inCommand.run();
        return inCommand.wasSuccessful();
    }

    public void send433Code(long code, int retry) {
        // FIXME: Move this to a static file
        String command = "./_433D -t9 -x" + retry + " " + code;
        String directory = "/home/pi/433_sniffer";
        fixedPool.execute(new InvokeCommand(directory, command));
    }

    public void send433Code(long code) {
        send433Code(code, 4);
    }

    public String toggleGate() {
        String command = "python gate_toggle.py";
        String directory = "/samba/scripts";

        InvokeCommand inCommand = new InvokeCommand(directory, command);
        inCommand.run();
        return inCommand.getCommandOutput();
    }

    public static class InvokeCommand implements Runnable {

        private final String command;
        private final String directory;
        private boolean success;
        private String output;

        public InvokeCommand(String directory, String command) {
            this.command = command;
            this.directory = directory;
        }

        public boolean wasSuccessful() {
            return success;
        }

        public String getCommandOutput() {
            return output;
        }

        @Override
        public void run() {
            Process process = null;

            log.info("Invoking Command " + command + " @ directory: " + directory);
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
            pb.directory(new File(directory));
            byte[] error = null;
            try {
                process = pb.start();
                process.waitFor();
                error = IOUtils.toByteArray(process.getErrorStream());
                output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("Failed to run the command", e);
            } catch (InterruptedException e) {
                log.error("Failed to run the command, interrupted before the command completed", e);
            } finally {
                if (process != null && process.exitValue() == 0) {
                    log.info("Command completed");
                } else {
                    String errorString = new String(Objects.requireNonNull(error));
                    log.info("Command failed, " + errorString);
                }

            }

            success = process.exitValue() == 0;
        }

    }


}

