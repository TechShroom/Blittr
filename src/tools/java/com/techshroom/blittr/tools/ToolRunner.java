package com.techshroom.blittr.tools;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ServiceLoader;

/**
 * Runs all tools that are discovered.
 */
public final class ToolRunner {

    public static void main(String[] args) {
        Path srcJava = Paths.get(args[0]);
        Path srcRes = Paths.get(args[1]);
        for (Tool tool : ServiceLoader.load(Tool.class)) {
            tool.run(srcJava, srcRes);
        }
    }

    private ToolRunner() {
    }

}
