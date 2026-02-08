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

import static rocks.minestom.ConfigLoader.loadConfig;

public final class Server {
    public static Config config;
    public static InstanceContainer overworld;
    public static InstanceContainer theNether;
    public static InstanceContainer theEnd;

    static void main() {
        config = loadConfig();

        var server = MinecraftServer.init(switch (config.authentication()) {
            case OFFLINE -> new Auth.Offline();
            case ONLINE -> new Auth.Online();
            case VELOCITY -> new Auth.Velocity(System.getenv("VELOCITY_FORWARDING_SECRET"));
            case BUNGEE -> new Auth.Bungee();
        });

        overworld = createInstance(DimensionType.OVERWORLD, new FlatGenerator(Block.GRASS_BLOCK));
        theNether = createInstance(DimensionType.THE_NETHER, new FlatGenerator(Block.NETHERRACK));
        theEnd = createInstance(DimensionType.THE_END, new FlatGenerator(Block.END_STONE));

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
                .addListener(new ConnectListener())
                .addListener(new DisconnectListener())
                .addListener(new ChunkUnloadListener())
                .addListener(new ItemCollectListener())
                .addListener(new ItemDropListener());
    }
}
