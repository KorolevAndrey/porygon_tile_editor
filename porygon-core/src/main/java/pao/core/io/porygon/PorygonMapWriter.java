package pao.core.io.porygon;

import pao.core.exceptions.PorygonIOException;
import pao.core.io.MapWriter;
import pao.core.models.Map;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Mark van der Wal
 * @since 15/02/18
 */
public class PorygonMapWriter implements MapWriter {

    @Override
    public void write(Path path, Map map) throws PorygonIOException {
        if (map == null) {
            throw new PorygonIOException("Passed in map is null");
        }

        if (path == null) {
            throw new PorygonIOException("Specified path is null!");
        }

        try {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path.toFile())))) {
                writer.println(map.getName());
                writer.println(map.getVersion());
                writer.println(map.getWidth());
                writer.println(map.getHeight());
                writer.println(map.getTileSize());

                byte[] collisionData = map.getCollisionData();
                IntStream.range(0, collisionData.length).map(idx -> collisionData[idx])
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(","));

                writer.println(collisionData);
            }
        } catch (IOException e) {
            throw new PorygonIOException(String.format("Could not write map file: %s", path));
        }
    }
}
