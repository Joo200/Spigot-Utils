package de.derfrzocker.spigot.utils;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum Version {

    v1_14_R1(10), v1_13_R2(9), v1_13_R1(8), v1_12_R1(7), v1_11_R1(6), v1_10_R1(5), v1_9_R2(4), v1_9_R1(3), v1_8_R3(2), v1_8_R2(1), v1_8_R1(0);

    @Getter
    private static Version current;

    private final static boolean testcase;

    @Getter
    private final static boolean paper;


    static {
        paper = Bukkit.getName().equalsIgnoreCase("Paper");

        String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

        if (version.equalsIgnoreCase("codegen")) {
            testcase = true;
        } else {
            testcase = false;

            try {
                current = valueOf(version.trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("unknown server version: " + version);
            }
        }
    }

    public static void clear() {
        Stream.of(values()).forEach(value -> value.list.clear());
    }

    @VisibleForTesting
    public static void setCurrentVersion(Version version) {
        if (!testcase)
            throw new IllegalStateException("Only for Testing!");

        current = version;
    }

    private final List<Runnable> list = new ArrayList<>();

    private final int value;

    public void run() {
        list.forEach(Runnable::run);
    }

    public void add(Runnable runnable) {
        list.add(runnable);
    }

    public boolean isNewerOrSameVersion(Version version) {
        return version.value >= value;
    }

    public boolean isNewerVersion(Version version) {
        return version.value > value;
    }

    public boolean isOlderOrSameVersion(Version version) {
        return version.value <= value;
    }

    public boolean isOlderVersion(Version version) {
        return version.value < value;
    }

}
