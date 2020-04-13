package cn.nukkit.plugin;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

/**
 * Created on 15-12-13.
 */
@Log4j2
public class LibraryLoader {

    private static final File BASE_FOLDER = new File("./libraries");
    private static final String SUFFIX = ".jar";

    static {
        if (BASE_FOLDER.mkdir()) {
            log.info("Created libraries folder.");
        }
    }

    public static void load(String library) {
        String[] split = library.split(":");
        if (split.length != 3) {
            throw new IllegalArgumentException(library);
        }
        load(new Library() {
            public String getGroupId() {
                return split[0];
            }

            public String getArtifactId() {
                return split[1];
            }

            public String getVersion() {
                return split[2];
            }
        });
    }

    public static void load(Library library) {
        String filePath = library.getGroupId().replace('.', '/') + '/' + library.getArtifactId() + '/' + library.getVersion();
        String fileName = library.getArtifactId() + '-' + library.getVersion() + SUFFIX;

        File folder = new File(BASE_FOLDER, filePath);
        if (folder.mkdirs()) {
            log.info("Created " + folder.getPath() + '.');
        }

        File file = new File(folder, fileName);
        if (!file.isFile()) try {
            URL url = new URL("https://repo1.maven.org/maven2/" + filePath + '/' + fileName);
            log.info("Get library from " + url + '.');
            Files.copy(url.openStream(), file.toPath());
            log.info("Get library " + fileName + " done!");
        } catch (IOException e) {
            throw new LibraryLoadException(library);
        }

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader instanceof URLClassLoader) {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                boolean accessible = method.isAccessible();
                if (!accessible) {
                    method.setAccessible(true);
                }
                URL url = file.toURI().toURL();
                method.invoke(classLoader, url);
                method.setAccessible(accessible);
            } else { // Java9+
                LibraryAgent.load(file);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            throw new LibraryLoadException(library);
        }

        log.info("Load library " + fileName + " done!");
    }

    public static File getBaseFolder() {
        return BASE_FOLDER;
    }

}
