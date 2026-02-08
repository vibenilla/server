package rocks.minestom;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;

public record FlatGenerator(Block block) implements Generator {
    @Override
    public void generate(GenerationUnit unit) {
        unit.modifier().fillHeight(-64, 0, block);
    }
}
