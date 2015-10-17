package com.techshroom.blittr.tools;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.auto.service.AutoService;
import com.google.auto.value.AutoValue;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

@AutoService(Tool.class)
public class ColorClassGeneratorTool implements Tool {

    private static final Logger logger = LogManager.getLogger();
    private static final String PACKAGE = "com.techshroom.blittr.color";
    // generate all color names via first char
    private static final Map<Character, String> COLOR_NAMES;

    static {
        Map<Character, String> base = Stream
                .of("Red", "Green", "Blue", "Hue", "Saturation", "Lightness",
                        "Cyan", "Yellow", "Magenta")
                .collect(Collectors.toMap(color -> color.charAt(0),
                        Function.identity()));
        // special: black is K in CYMK
        base.put('K', "Black");
        COLOR_NAMES = ImmutableMap.copyOf(base);
    }

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
        Stream.of(colors)
                .flatMap(color -> Stream.of(color + "ColorI", color + "ColorD"))
                .map(this::generateClass).forEach(file -> {
                    try {
                        file.writeTo(outputJava);
                    } catch (Exception e) {
                        logger.error("Whoops.", e);
                    }
                });

    }

    private JavaFile generateClass(String className) {
        logger.debug("Generating class data for {}", className);
        FormatData data = FORMAT_DATA.get(className);
        checkState(data != null, "Need data on " + className);
        logger.trace("Using Format {}", data);
        List<String> colors =
                className.chars().limit(className.indexOf("Color"))
                        .mapToObj(c -> COLOR_NAMES.get((char) c))
                        .collect(Collectors.toList());
        logger.trace("Colors: {} -> {}",
                className.substring(0, className.indexOf("Color")), colors);
        Class<?> dataType =
                ToolUtils.classForNamePSupport(data.getData().get("type"));
        TypeName dataTypeName = TypeName.get(dataType);
        TypeSpec.Builder type =
                TypeSpec.classBuilder(className).addAnnotation(AutoValue.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        type.addMethod(generateCreationMethod(className, colors, dataTypeName));
        type.addMethods(generateColorMethods(colors, dataTypeName));
        type.addMethods(generateConversionMethods(data));
        return JavaFile.builder(PACKAGE, type.build())
                .skipJavaLangImports(false).indent("    ").build();
    }

    private Iterable<MethodSpec> generateConversionMethods(FormatData data) {
        logger.debug(data.getData().keySet());
        return FluentIterable.from(data.getData().keySet())
                .filter(x -> x.startsWith("converterCode"))
                .transform(key -> convertCodeToMSpec(key,
                        Stream.of(data.getData().get(key).split("\n"))));
    }

    private MethodSpec convertCodeToMSpec(String key, Stream<String> lines) {
        String typeData = key.substring("converterCode".length());
        // type data (e.x. HSLD) -> First chars (e.x. HSL) and data type (e.x. D
        // -> double)
        String convertReturnType = typeData.substring(0, typeData.length() - 1)
                + "Color" + typeData.charAt(typeData.length() - 1);
        ClassName convertReturnName = ClassName.get(PACKAGE, convertReturnType);
        String giantBlock = lines.map(line -> "$[" + line + "\n$]")
                .collect(Collectors.joining());
        logger.trace("Big code block:");
        logger.trace(giantBlock);
        return MethodSpec.methodBuilder("convertTo" + typeData)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(convertReturnName)
                .addCode(giantBlock, convertReturnName).build();
    }

    private MethodSpec generateCreationMethod(String className,
            List<String> colors, TypeName dataTypeName) {
        CodeBlock.Builder code = CodeBlock.builder();
        code.add("$[return new AutoValue_$L(", className);

        for (String color : colors) {
            code.add("$L", color.toLowerCase());
            if (colors.indexOf(color) != (colors.size() - 1)) {
                code.add(", ");
            }
        }
        code.add(");\n$]");
        return MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(ClassName.get(PACKAGE, className))
                .addParameters(generateColorParameters(colors, dataTypeName))
                .addCode(code.build()).build();
    }

    private Iterable<ParameterSpec> generateColorParameters(List<String> colors,
            TypeName dataTypeName) {
        return FluentIterable.from(colors)
                .transform(color -> colorToPSpec(color, dataTypeName));
    }

    private ParameterSpec colorToPSpec(String color, TypeName dataTypeName) {
        return ParameterSpec.builder(dataTypeName, color.toLowerCase()).build();
    }

    private Iterable<MethodSpec> generateColorMethods(List<String> colors,
            TypeName dataTypeName) {
        return FluentIterable.from(colors)
                .transform(color -> colorToMSpec(color, dataTypeName));
    }

    private MethodSpec colorToMSpec(String color, TypeName dataTypeName) {
        return MethodSpec.methodBuilder("get" + color)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(dataTypeName).build();
    }

}
