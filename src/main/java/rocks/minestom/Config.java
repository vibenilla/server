package rocks.minestom;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import static net.minestom.server.codec.Codec.*;

public record Config(String address, int port, Authentication authentication) {
    public static final Codec<Config> CODEC = StructCodec.struct(
            "address", STRING, Config::address,
            "port", INT, Config::port,
            "authentication", Authentication.CODEC, Config::authentication,
            Config::new);

    public enum Authentication {
        OFFLINE,
        ONLINE,
        VELOCITY,
        BUNGEE;

        public static final Codec<Authentication> CODEC = Enum(Authentication.class);
    }
}
