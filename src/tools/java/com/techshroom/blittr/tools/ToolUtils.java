package com.techshroom.blittr.tools;

import static com.google.common.base.Preconditions.checkState;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

public final class ToolUtils {

    private static final Logger logger = LogManager.getLogger();

    public static List<FormatData> parse(InputStream stream) {
        ImmutableList.Builder<FormatData> fmtData = ImmutableList.builder();
        String name = null;
        Map<String, String> data = new HashMap<>();
        String keyTmp = null;
        String lineTmp = "";
        try (
                Scanner s = new Scanner(stream)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                logger.trace("line '{}'", line);
                if (line.startsWith("name")) {
                    int tildex = line.lastIndexOf('~');
                    checkState(tildex == line.length() - 1,
                            "Name key may only be on one line");
                    line = line.substring(0, tildex);
                    if (name != null) {
                        logger.trace("Adding FmtData name='{}',data='{}'", name,
                                data);
                        fmtData.add(FormatData.create(name, data));
                    }
                    data.clear();
                    name = line.substring("name=".length());
                    logger.trace("Starting FmtData name='{}'", name);
                    continue;
                }
                if (keyTmp == null) {
                    int eqIndex = line.indexOf('=');
                    keyTmp = line.substring(0, eqIndex).trim();
                    line = line.substring(eqIndex + 1);
                    logger.trace("Collecting key '{}'", keyTmp);
                }
                if (lineTmp.isEmpty()) {
                    lineTmp = line;
                } else {
                    lineTmp += "\n" + line;
                }
                if (line.length() > 0
                        && line.charAt(line.length() - 1) == '~') {
                    logger.trace("Collected key '{}'", keyTmp);
                    data.put(keyTmp,
                            lineTmp.substring(0, lineTmp.length() - 1));
                    keyTmp = null;
                    lineTmp = "";
                }
            }
            if (name != null) {
                logger.trace("Adding FmtData name='{}',data='{}'", name, data);
                fmtData.add(FormatData.create(name, data));
            }
        }
        return fmtData.build();
    }

    private ToolUtils() {
    }

}
