package com.techshroom.blittr.tools;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;

@AutoService(Tool.class)
public class ColorClassGeneratorTool implements Tool {

    private static final Logger logger = LogManager.getLogger();
    private static final Map<String, FormatData> FORMAT_DATA;

    static {
        ImmutableMap.Builder<String, FormatData> b = ImmutableMap.builder();
        try (
                InputStream stream = Files.newInputStream(Paths.get(
                        "src/tools/resources/ColorClassConversions.fmt"))) {
            ToolUtils.parse(stream).forEach(x -> b.put(x.getName(), x));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FORMAT_DATA = b.build();
    }

    @Override
    public void run(Path outputJava, Path outputResources) {
        String[] colors = { "RGB", "HSL", "CYMK" };
        // map colors -> ColorI/D variants
        Stream.of(colors).flatMap(color -> {
            return Stream.of(color + "ColorI", color + "ColorD");
        }).forEach(this::generateClass);

    }

    private void generateClass(String s) {
        logger.debug("Generating class data for {}", s);
    }

}
