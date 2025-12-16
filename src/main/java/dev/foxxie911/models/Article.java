package dev.foxxie911.models;

import java.time.LocalDate;

public record Article(String title, LocalDate createOn, String body) {
}
