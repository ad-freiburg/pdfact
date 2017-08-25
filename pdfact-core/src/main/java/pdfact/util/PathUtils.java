package pdfact.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A collection of utility methods that deal with file / paths.
 * 
 * @author Claudius Korzen
 */
public class PathUtils {
  /**
   * Returns the base name of the given file, that is the filename without the
   * file-extension.
   * 
   * @param path
   *        The path to process.
   * @return The base name of the path.
   */
  public static String getBasename(String path) {
    return getBasename(Paths.get(path));
  }

  /**
   * Returns the basename of the given file, that is the filename without the
   * file-extension.
   * 
   * @param file
   *        The file to process.
   * @return The base name of the path.
   */
  public static String getBasename(Path file) {
    if (file != null) {
      Path filename = file.getFileName();
      if (filename != null) {
        String filenameStr = filename.toString();
        String[] tokens = filenameStr.split("\\.(?=[^\\.]+$)");
        return tokens[0];
      }
    }
    return null;
  }

  /**
   * Returns the file extension of the given file.
   * 
   * @param file
   *        The file to process.
   * @return The file extension of the file.
   */
  public static String getExtension(Path file) {
    if (file != null) {
      Path filename = file.getFileName();
      if (filename != null) {
        String filenameStr = filename.toString();
        String[] tokens = filenameStr.split("\\.(?=[^\\.]+$)");
        return tokens.length > 1 ? tokens[1] : "";
      }
    }
    return null;
  }

  /**
   * Reads the given directory non-recursively and returns a map with the names
   * and the streams of all files in this directory. Works also in jar files.
   * 
   * @param path
   *        The path to the directory to read.
   * @return A map with the names and the streams of all files in this directory
   * @throws IOException
   *         If reading the directory failed.
   */
  public static Map<String, InputStream> readDirectory(String path)
      throws IOException {
    Map<String, InputStream> streams = new HashMap<>();
    ClassLoader classLoader = PathUtils.class.getClassLoader();
    ProtectionDomain domain = PathUtils.class.getProtectionDomain();

    // We need to distinguish, if we are in a jar file or not.
    CodeSource codeSource = domain.getCodeSource();
    Path jarFile = Paths.get(codeSource.getLocation().getPath());

    // Check, if we are in jar file.
    if (Files.isRegularFile(jarFile)) {
      try (JarFile jar = new JarFile(jarFile.toFile())) {
        // Fetch all files in the jar.
        final Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
          final String name = entries.nextElement().getName();
          // filter according to the path

          if (name.startsWith(path) && !name.equals(path)) {
            streams.put(name, classLoader.getResourceAsStream(name));
          }
        }
      }
    } else {
      // We are not in a jar file.
      File directory = null;
      try {
        // Read the directory.
        directory = new File(classLoader.getResource(path).toURI());
      } catch (Exception e) {
        // Nothing to do.
      }

      if (directory != null) {
        File[] files = directory.listFiles();
        if (files != null) {
          for (File file : files) {
            try {
              streams.put(file.getName(), new FileInputStream(file));
            } catch (Exception e) {
              continue;
            }
          }
        }
      }
    }
    return streams;
  }
}
