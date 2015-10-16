package com.techshroom.blittr.tools;

import java.util.Map;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

@AutoValue
public abstract class FormatData {

    public static FormatData create(String name, Map<String, String> data) {
        return new AutoValue_FormatData(name, ImmutableMap.copyOf(data));
    }

    FormatData() {
    }

    public abstract String getName();

    public abstract ImmutableMap<String, String> getData();

}
