package dev.foxxie911.services;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class AssetProvider {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final Logger LOGGER = Logger.getLogger("AssetProvider.class");

    public AssetProvider() {
    }

    public void copyStyleSheet() {
        Path sitePath = getProcessedSitePath();

        Path styleSheetPath = Path.of("src/main/resources/styles").toAbsolutePath();

        try {
            Files.createDirectory(sitePath.resolve("styles"));
        } catch (IOException e) {
            LOGGER.warning("Could not create styles folder");
        }


        if (Files.notExists(styleSheetPath)) LOGGER.warning("No style file exists");

        try {
            Files.copy(styleSheetPath.resolve("style.css"),
                    sitePath.resolve("styles/style.css"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.severe("Couldn't copy style file");
        }
    }

    public void copyFontsFiles() {
        Path sitePath = getProcessedSitePath();

        Path fontFilesPath = Path.of("src/main/resources/fonts");

        try {
            Files.createDirectory(sitePath.resolve("fonts"));
            if (Files.notExists(fontFilesPath)) LOGGER.warning("No font file exists");

        } catch (IOException e) {
            LOGGER.warning("Couldn't create fonts folder");
        }

        try {
            Files.walk(fontFilesPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.copy(file, sitePath.resolve("fonts/" + file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            LOGGER.severe("Couldn't copy: " + file.getFileName());
                        }
                    });
        } catch (IOException e) {
            LOGGER.severe("Can't walk: " + fontFilesPath.getFileName());
        }
    }

    private Path getProcessedSitePath() {
        String sitePath = DOTENV.get("SITE_PATH");
        if (sitePath.startsWith("~")) {
            sitePath = System.getProperty("user.home") + sitePath.substring(1);
        }
        return Paths.get(sitePath).toAbsolutePath();
    }
}