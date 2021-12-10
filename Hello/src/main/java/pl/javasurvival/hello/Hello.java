package pl.javasurvival.hello;

import io.vavr.collection.List;

import java.util.stream.IntStream;

public class Hello {
    public static void main(String ... args) {
        // Biblioteka Vavr
        List.range(1,6)
                .map(iNumber -> ("Witaj vavr po raz: " + iNumber))
                .forEach(System.out::println);
        // Funkcyjnie
        IntStream.range(1, 6)
                .mapToObj(iNumber -> ("Witaj po raz: " + iNumber))
                //.forEach(iText -> System.out.println(iText));
                // Referencja do metody - za[pis skrótowy
                .forEach(System.out::println);
//        for (int i =0; i < 5; i = i + 1 ) {
//        System.out.println("Witaj świecie!");
//        }
    }
}
