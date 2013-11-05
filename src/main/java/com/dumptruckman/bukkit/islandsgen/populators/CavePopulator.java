package com.dumptruckman.bukkit.islandsgen.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CavePopulator extends BlockPopulator {

    /**
     * @see org.bukkit.generator.BlockPopulator#populate(org.bukkit.World,
     *      java.util.Random, org.bukkit.Chunk)
     */
    @Override
    public void populate(final World world, final Random random, Chunk source) {

        if (random.nextInt(100) < 80) {
            final int x = 4 + random.nextInt(8) + source.getX() * 16;
            final int z = 4 + random.nextInt(8) + source.getZ() * 16;
            int maxY = world.getHighestBlockYAt(x, z);
            if (maxY < 16) {
                maxY = 32;
            }

            final int y = random.nextInt(maxY);
            Set<Vector> snake = selectBlocksForCave(world, random, x, y, z);
            buildCave(world, snake.toArray(new Vector[0]));
            for (Vector block : snake) {
                world.unloadChunkRequest(block.getBlockX() / 16, block.getBlockZ() / 16);
            }
        }
    }

    static Set<Vector> selectBlocksForCave(World world, Random random, int blockX, int blockY, int blockZ) {
        Set<Vector> snakeBlocks = new HashSet<Vector>();

        int airHits = 0;
        Vector block = new Vector();
        while (true) {
            if (airHits > 1200) {
                break;
            }

            if (random.nextInt(20) == 0) {
                blockY++;
            } else if (world.getBlockTypeIdAt(blockX, blockY + 2, blockZ) == 0) {
                blockY += 2;
            } else if (world.getBlockTypeIdAt(blockX + 2, blockY, blockZ) == 0) {
                blockX++;
            } else if (world.getBlockTypeIdAt(blockX - 2, blockY, blockZ) == 0) {
                blockX--;
            } else if (world.getBlockTypeIdAt(blockX, blockY, blockZ + 2) == 0) {
                blockZ++;
            } else if (world.getBlockTypeIdAt(blockX, blockY, blockZ - 2) == 0) {
                blockZ--;
            } else if (world.getBlockTypeIdAt(blockX + 1, blockY, blockZ) == 0) {
                blockX++;
            } else if (world.getBlockTypeIdAt(blockX - 1, blockY, blockZ) == 0) {
                blockX--;
            } else if (world.getBlockTypeIdAt(blockX, blockY, blockZ + 1) == 0) {
                blockZ++;
            } else if (world.getBlockTypeIdAt(blockX, blockY, blockZ - 1) == 0) {
                blockZ--;
            } else if (random.nextBoolean()) {
                if (random.nextBoolean()) {
                    blockX++;
                } else {
                    blockZ++;
                }
            } else {
                if (random.nextBoolean()) {
                    blockX--;
                } else {
                    blockZ--;
                }
            }

            if (world.getBlockTypeIdAt(blockX, blockY, blockZ) != 0) {
                int radius = 1 + random.nextInt(2);
                int radius2 = radius * radius + 1;
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + y * y + z * z <= radius2 && y >= 0 && y < 128) {
                                if (world.getBlockTypeIdAt(blockX + x, blockY + y, blockZ + z) == 0) {
                                    airHits++;
                                } else {
                                    block.setX(blockX + x);
                                    block.setY(blockY + y);
                                    block.setZ(blockZ + z);
                                    if (snakeBlocks.add(block)) {
                                        block = new Vector();
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                airHits++;
            }
        }

        return snakeBlocks;
    }

    static void buildCave(World world, Vector[] snakeBlocks) {
        for (Vector loc : snakeBlocks) {
            Block block = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            if (!block.isEmpty() && !block.isLiquid() && block.getType() != Material.BEDROCK) {
                block.setType(Material.AIR);
            }
        }
    }
}
