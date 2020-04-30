package fakeArduino;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.io.*;

public class App {
    private static Path createDirectoryUnlessExists(Path p) throws IOException {
        if(p.toFile().exists())
            return p;
        else
            return Files.createDirectory(p);
    }
    
    public static int compile(File file) throws IOException {
        Path ino = file.toPath();
        // Path compileDir = Files.createTempDirectory(Paths.get("."), "compile");
        Path compileDir = createDirectoryUnlessExists(Paths.get(".", "compile"));
        Path sourceDir = createDirectoryUnlessExists(Paths.get(compileDir.toString(), "source"));
        Path buildDir = createDirectoryUnlessExists(Paths.get(compileDir.toString(), "build"));
        String baseName = FilenameUtils.removeExtension(file.getName());
        // Create code.c every time
        {
            FileWriter codeWriter = new FileWriter(Paths.get(sourceDir.toString(), baseName + ".c").toFile());
            codeWriter.write("#include \"Arduino.h\"\n\n");
            FileReader inoReader = new FileReader(ino.toFile());
            int read;
            char[] buf = new char[1024];
            while((read = inoReader.read(buf)) != -1)
                codeWriter.write(buf, 0, read);
            codeWriter.close();
            inoReader.close();
        }
        // copy resources every time (for now)
        for(String res : new String[]{"CMakeLists.txt", "source/Arduino.h", "source/arduino.c", "source/fakeArduino_JNIArduino.h"}) {
            InputStream cmakelists = App.class.getResourceAsStream("/" + res);
            Files.copy(cmakelists, compileDir.resolve(Paths.get(res)), StandardCopyOption.REPLACE_EXISTING);
        }
        // Run cmake - S . -B build -DCODEFILE:STRING=code.c
        try {
            ProcessBuilder cmakeBuilder = null;
            if(SystemUtils.IS_OS_WINDOWS) {
                cmakeBuilder = new ProcessBuilder("cmake", "-S", ".", "-B", "build", "-DJAVA_OS_DIR:STRING=win32", "-DCMAKE_GENERATOR_PLATFORM=x64", "-DCODEFILE:STRING=" + baseName + ".c");
            } else if(SystemUtils.IS_OS_LINUX) {
                cmakeBuilder = new ProcessBuilder("cmake", "-S", ".", "-B", "build", "-DJAVA_OS_DIR:STRING=linux", "-DCODEFILE:STRING=" + baseName + ".c");
                System.out.println("Linux");
            }
            cmakeBuilder.directory(compileDir.toFile());
            cmakeBuilder.inheritIO();
            Process cmake = cmakeBuilder.start();
            int returnCode = cmake.waitFor();
            if(returnCode != 0)
                return returnCode;
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        // Run cmake --build build
        try {
            ProcessBuilder cmakeBuilder = new ProcessBuilder("cmake", "--build", "build");
            cmakeBuilder.directory(compileDir.toFile());
            cmakeBuilder.inheritIO();
            Process cmake = cmakeBuilder.start();
            int returnCode = cmake.waitFor();
            if(returnCode != 0)
                return returnCode;
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        // copy codelib.dll and DONT delete temporary compilation steps
        if(SystemUtils.IS_OS_WINDOWS) {
            Files.copy(buildDir.resolve(Paths.get("Debug", "codelib.dll")), Paths.get(".", "codelib.dll"), StandardCopyOption.REPLACE_EXISTING);
        } else if(SystemUtils.IS_OS_LINUX) {
            Files.copy(buildDir.resolve(Paths.get("libcodelib.so")), Paths.get(".", "libcodelib.so"), StandardCopyOption.REPLACE_EXISTING);
        }
        return 0;
    }

    public static void clean() throws IOException {
        // File compileDir = Paths.get(".", "compile").toFile();
        // if(compileDir.exists())
        //     FileUtils.deleteDirectory(compileDir);
        if(SystemUtils.IS_OS_WINDOWS) {
            Path lib = Paths.get(".", "codelib.dll");
            if(lib.toFile().exists())
                Files.delete(lib);
        } else if(SystemUtils.IS_OS_LINUX) {
            Path lib = Paths.get(".", "libcodelib.so");
            if(lib.toFile().exists())
                Files.delete(lib);
        }
    }

    public static void main(String[] args) {
        try {
            if(compile(new File("main.ino")) == 0) {
                JNIArduino a = new JNIArduino();
                a.init();

                a.setup();
                while(a.getRunning()) {
                    a.loop();
                    System.out.println(Arrays.toString(a.getPins()));
                }
                a.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
