package rocks.minestom;

import io.github.togar2.pvp.MinestomPvP;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;
import rocks.minestom.listener.*;
import rocks.minestom.worldgen.WorldGenerators;

import java.nio.file.Path;

import static rocks.minestom.ConfigLoader.loadConfig;

public final class Server {
    public static Config config;
    public static InstanceContainer overworld;
    public static InstanceContainer theNether;
    public static InstanceContainer theEnd;
    public static WorldGenerators worldGenerators;

    static void main() {
        config = loadConfig();

        var server = MinecraftServer.init(switch (config.authentication()) {
            case OFFLINE -> new Auth.Offline();
            case ONLINE -> new Auth.Online();
            case VELOCITY -> new Auth.Velocity(System.getenv("VELOCITY_FORWARDING_SECRET"));
            case BUNGEE -> new Auth.Bungee();
        });

        worldGenerators = new WorldGenerators(Path.of("."), config.seed());
        overworld = createInstance(DimensionType.OVERWORLD, worldGenerators.overworld());
        theNether = createInstance(DimensionType.THE_NETHER, worldGenerators.nether());
        theEnd = createInstance(DimensionType.THE_END, worldGenerators.end());

        MinestomPvP.init();
        registerEventListeners();

        Logger.info("Started the server.");
        server.start(config.address(), config.port());
    }

    private static InstanceContainer createInstance(RegistryKey<DimensionType> dimension, @Nullable Generator generator) {
        var instanceManager = MinecraftServer.getInstanceManager();
        var instance = instanceManager.createInstanceContainer(dimension, ChunkLoader.noop());
        instance.eventNode().addChild(MinestomPvP.events());
        instance.setGenerator(generator);
        return instance;
    }

    private static void registerEventListeners() {
        MinecraftServer.getGlobalEventHandler()
                .addListener(new AsyncPlayerConfigurationListener())
                .addListener(new PlayerDisconnectListener())
                .addListener(new PlayerChunkUnloadListener())
                .addListener(new PlayerGameModeRequestListener())
                .addListener(new PickupItemListener())
                .addListener(new ItemDropListener())
                ;
    }
}
