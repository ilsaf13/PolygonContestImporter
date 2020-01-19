package pcms2;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static int runDoAll(File probDir, boolean quiet) throws IOException {
        ProcessBuilder processBuilder = System.getProperty("os.name").toLowerCase().startsWith("win") ?
                new ProcessBuilder("cmd", "/c", "doall.bat") :
                new ProcessBuilder("/bin/bash", "-c", "find -name '*.sh' | xargs chmod +x && ./doall.sh");
        processBuilder.directory(probDir);
        if (!quiet) {
            processBuilder.inheritIO();
        }
        Process exec = processBuilder.start();
        try {
            return exec.waitFor();
        } catch (InterruptedException e) {
            System.err.println("The process was interrupted");
            return 130;
        }
    }

    public static void archiveToDirectory(File zipFile, File probDir, boolean runDoAll) throws IOException, ZipException {

        System.out.println("Unzipping " + zipFile.getAbsolutePath());
        unzip(zipFile, probDir);

        System.out.println("Problem downloaded to " + probDir.getAbsolutePath());
        if (runDoAll) {
            System.out.println("Standard package, initiating test generation");
            int exitCode = Utils.runDoAll(probDir, false);
            if (exitCode != 0) {
                throw new AssertionError("doall failed with exit code " + exitCode);
            } else {
                System.out.println("Tests generated successfully in " + probDir.getAbsolutePath());
            }
        }
    }

    static public void unzip(File zipFile, File probDir) throws ZipException {
        new ZipFile(zipFile).extractAll(probDir.getAbsolutePath());
    }

    static public void copyToVFS(Challenge challenge, File vfs, Asker asker) throws IOException {
        String[] files = {"challenge.xml", "submit.lst"};
        File vfsEtcDirectory = new File(vfs, "etc/" + challenge.id.replace(".", "/"));
        for (String f : files) {
            File src = new File(challenge.path, f);
            File dest = new File(vfsEtcDirectory, f);
            System.out.println("Preparing to copy " + f + " to " + dest.getAbsolutePath());
            deployFile(src, dest, asker);
        }
    }

    static public void forceCopyFileOrDirectory(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            FileUtils.copyDirectory(src, dest);
        } else {
            FileUtils.copyFile(src, dest);
        }
    }

    static public void deployFile(File src, File dest, Asker asker) throws IOException {
        if (dest.exists()) {
            System.out.println(src.getName() + " '" + dest.getAbsolutePath() + "' exists.");
            if (asker.askForUpdate("Do you want to update it?")) {
                System.out.println("Updating...");
                forceCopyFileOrDirectory(src, dest);
            } else {
                System.out.println("Skipping...");
            }
        } else {
            System.out.println("Copying " + src.getName() + " to '" + dest.getAbsolutePath() + "'.");
            forceCopyFileOrDirectory(src, dest);
        }
    }

    static public void publishFile(File src, File dest, Asker asker) throws IOException {
        if (asker.askForUpdate("Do you want to publish it?")) {
            System.out.println("Publishing...");
            forceCopyFileOrDirectory(src, dest);
        } else {
            System.out.println("Skipping...");
        }
    }

    static public void copyToWEB(Challenge challenge, File webroot, Asker asker) throws IOException {
        File src = new File(challenge.path, "statements/" + challenge.language + "/statements.pdf");
        if (!src.exists()) {
            return;
        }
        File dest = new File(webroot, "statements/" + challenge.id.replace(".", "/") + "/statements.pdf");
        System.out.println("Preparing to copy " + challenge.language + " statement to " + dest.getAbsolutePath());
        publishFile(src, dest, asker);
    }

    static public void copyToVFS(Problem problem, File vfs, Asker asker) throws IOException {
        File src = problem.getDirectory();
        File dest = new File(vfs, "problems/" + problem.id.replace(".", "/"));
        System.out.println("Preparing to copy problem " + problem.shortName + " to " + dest.getAbsolutePath());
        deployFile(src, dest, asker);
    }
}
