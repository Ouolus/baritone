/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.builder;

import baritone.api.utils.BetterBlockPos;

/**
 * Bounding box of a cuboid
 * <p>
 * Basically just a lot of helper util methods lol
 */
public class CuboidBounds {

    public final int sizeX;
    public final int sizeY;
    public final int sizeZ;
    public final int size;

    public CuboidBounds(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.size = sizeX * sizeY * sizeZ;
        if (Main.DEBUG) {
            sanityCheck();
        }
    }

    public int toIndex(long pos) {
        return toIndex(BetterBlockPos.XfromLong(pos), BetterBlockPos.YfromLong(pos), BetterBlockPos.ZfromLong(pos));
    }

    public int toIndex(int x, int y, int z) {
        if (Main.DEBUG && !inRange(x, y, z)) {
            throw new IllegalStateException();
        }
        return (x * sizeY + y) * sizeZ + z;
    }

    public boolean inRange(int x, int y, int z) {
        return (x >= 0) & (x < sizeX) & (y >= 0) & (y < sizeY) & (z >= 0) & (z < sizeZ);
    }

    public boolean inRangeIndex(int index) {
        return (index >= 0) & (index < size);
    }

    public boolean inRangePos(long pos) {
        return inRange(BetterBlockPos.XfromLong(pos), BetterBlockPos.YfromLong(pos), BetterBlockPos.ZfromLong(pos));
    }

    @FunctionalInterface
    public interface CuboidCoordConsumer {

        void consume(int x, int y, int z);
    }

    @FunctionalInterface
    public interface CuboidIndexConsumer {

        void consume(long index);
    }

    @FunctionalInterface
    public interface CuboidCoordAndIndexConsumer {

        void consume(int x, int y, int z, long index);
    }

    public void forEach(CuboidCoordConsumer consumer) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    consumer.consume(x, y, z);
                }
            }
        }
    }

    public void forEach(CuboidIndexConsumer consumer) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    consumer.consume(BetterBlockPos.toLong(x, y, z));
                }
            }
        }
    }

    public void forEach(CuboidCoordAndIndexConsumer consumer) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    consumer.consume(x, y, z, BetterBlockPos.toLong(x, y, z));
                }
            }
        }
    }

    private void sanityCheck() {
        if (sizeY > 256) {
            throw new IllegalStateException();
        }
        long chk = ((long) sizeX) * ((long) sizeY) * ((long) sizeZ);
        if (chk != (long) size) {
            throw new IllegalStateException();
        }
        int index = 0;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    if (toIndex(x, y, z) != index) {
                        throw new IllegalStateException();
                    }
                    index++;
                }
            }
        }
        if (index != size) {
            throw new IllegalStateException();
        }
    }
}
