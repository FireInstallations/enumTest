package de.fireinstallations;

import java.util.EnumSet;
import java.util.random.RandomGenerator;

public class Main {
    private static final RandomGenerator generator = RandomGenerator.of("Xoshiro256PlusPlus");
    private static final int SIZE = 1000_000;
    private static final String[] generated = new String[SIZE];
    private static final Class<MyEnum> myClass = MyEnum.class;

    public static void main(String[] args) {
        testClass();
        testSET();
        testNative();
    }

    private static void generateNewRandoms() {
        MyEnum[] values = MyEnum.values();

        for (int i = 0; i < SIZE; i++) {
            generated[i] = values[generator.nextInt(values.length)].name();
        }
    }

    private static void testClass() {
        generateNewRandoms();

        long startTime = System.nanoTime();

        for (String name : generated) {
            MyEnum a = getEnumClass(myClass, name);

            if (a == null) {
                System.out.println("Error!");
            }
        }

        System.out.println("Time class: " + (System.nanoTime() - startTime));
    }

    private static void testSET() {
        generateNewRandoms();

        long startTime = System.nanoTime();

        for (String name : generated) {
            MyEnum a = getEnumSET(myClass, name);

            if (a == null) {
                System.out.println("Error!");
            }
        }

        System.out.println("Time set: " + (System.nanoTime() - startTime));
    }

    private static void testNative() {
        generateNewRandoms();

        long startTime = System.nanoTime();

        for (String name : generated) {
            MyEnum a = Enum.valueOf(myClass, name);

            if (a == null) { // I hope the compiler does not optimize this out for fair testing
                System.out.println("Error!");
            }
        }

        System.out.println("Time native: " + (System.nanoTime() - startTime));
    }

    public static <E extends Enum<E>> E getEnumClass(final Class<E> enumClass, final String enumName) {
        for (E enumVal : enumClass.getEnumConstants()) {
            if (enumVal.name().equalsIgnoreCase(enumName)) {
                return enumVal;
            }
        }

        return null;
    }

    public static <E extends Enum<E>> E getEnumSET(final Class<E> enumClass, final String enumName) {
        for (E enumVal : EnumSet.allOf(enumClass)) {
            if (enumVal.name().equalsIgnoreCase(enumName)) {
                return enumVal;
            }
        }

        return null;
    }

    private enum MyEnum {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX
    }
}