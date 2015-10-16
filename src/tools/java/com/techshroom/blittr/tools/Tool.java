package com.techshroom.blittr.tools;

import java.nio.file.Path;

public interface Tool {

    void run(Path outputJava, Path outputResources);

}
