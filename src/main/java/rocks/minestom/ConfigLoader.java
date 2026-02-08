package rocks.minestom;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class ConfigLoader {
    private static final Gson GSON = new Gson();
    private static final Path PATH = Path.of("config.json");
    private static final Config DEFAULT_CONFIG = new Config(
            "0.0.0.0",
            25565,
            Config.Authentication.ONLINE);

    static Config loadConfig() {
        Config config;

        try {
            var rawConfig = GSON.fromJson(Files.readString(PATH), JsonObject.class);

            switch (Config.CODEC.decode(Transcoder.JSON, rawConfig)) {
                case Result.Ok<Config> ok -> config = ok.value();

                case Result.Error<Config> error -> {
                    Logger.error("Failed to load config!", error.message());
                    config = DEFAULT_CONFIG;
                }
            }
        } catch (IOException e) {
            Logger.error("Failed to read config file!", e);
            config = DEFAULT_CONFIG;
        }

        return config;
    }

    static void saveConfig(Config config) {
        JsonElement element;

        switch (Config.CODEC.encode(Transcoder.JSON, config)) {
            case Result.Ok<JsonElement> ok -> element = ok.value();

            case Result.Error<JsonElement> error -> {
                Logger.error("Failed to save config!", error.message());
                return;
            }
        }

        try {
            Files.writeString(PATH, GSON.toJson(element));
        } catch (IOException e) {
            Logger.error("Failed to write to config file!", e);
        }
    }
}
