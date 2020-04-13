package cn.nukkit.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class LibraryAgent {

    private static Instrumentation inst;

    public static void agentmain(String args, Instrumentation instrumentation) {
        if (inst != null) return;
        inst = instrumentation;
    }

    protected static void load(File file) throws IOException {
        inst.appendToSystemClassLoaderSearch(new JarFile(file));
    }
}
