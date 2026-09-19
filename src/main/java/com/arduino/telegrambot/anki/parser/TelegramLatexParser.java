package com.arduino.telegrambot.anki.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсит текст сообщения и оборачивает найденные LaTeX-формулы
 * в тег <tg-math-block> для использования в Telegram RichMessage.
 *
 * Поддерживаемые форматы формул:
 *   $$ ... $$   — display-формула (двойной доллар)
 *   $ ... $     — inline-формула (одинарный доллар)
 *   \[ ... \]   — display-формула (LaTeX-скобки)
 *   \( ... \)   — inline-формула (LaTeX-скобки)
 *
 * Порядок проверки важен: сначала $$ и \[...\], иначе одинарный $
 * или \( съедят их как обычный текст.
 */
public class TelegramLatexParser {

    // $$...$$  (не жадный, поддержка переноса строк)
    private static final Pattern DOUBLE_DOLLAR =
            Pattern.compile("\\$\\$(.+?)\\$\\$", Pattern.DOTALL);

    // \[ ... \]
    private static final Pattern DISPLAY_BRACKETS =
            Pattern.compile("\\\\\\[(.+?)\\\\\\]", Pattern.DOTALL);

    // \( ... \)
    private static final Pattern INLINE_BRACKETS =
            Pattern.compile("\\\\\\((.+?)\\\\\\)", Pattern.DOTALL);

    // $...$  — одинарный доллар, но не должен путать с $$
    // (?<!\$) и (?!\$) исключают соседство с другим $
    private static final Pattern SINGLE_DOLLAR =
            Pattern.compile("(?<!\\$)\\$(?!\\$)(.+?)(?<!\\$)\\$(?!\\$)", Pattern.DOTALL);

    // Плейсхолдеры, чтобы уже обработанные формулы не парсились повторно
    private static final String PLACEHOLDER_PREFIX = "\u0000LATEX_BLOCK_";
    private static final String PLACEHOLDER_SUFFIX = "\u0000";

    /**
     * Основной метод: принимает исходный текст, возвращает текст
     * с формулами, обёрнутыми в <tg-math-block>.
     */
    public static String parse(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        java.util.List<String> extracted = new java.util.ArrayList<>();

        String text = input;
        text = extractAndReplace(text, DOUBLE_DOLLAR, extracted);
        text = extractAndReplace(text, DISPLAY_BRACKETS, extracted);
        text = extractAndReplace(text, INLINE_BRACKETS, extracted);
        text = extractAndReplace(text, SINGLE_DOLLAR, extracted);

        // Остальной текст (включая уже существующую HTML-разметку,
        // если она там есть) не трогаем и не экранируем.

        // Возвращаем формулы на место, уже обёрнутыми в тег.
        // Экранируем спецсимволы только ВНУТРИ формулы, чтобы, например,
        // "a < b" в LaTeX не сломало сам тег <tg-math-block>.
        for (int i = 0; i < extracted.size(); i++) {
            String placeholder = PLACEHOLDER_PREFIX + i + PLACEHOLDER_SUFFIX;
            String formula = extracted.get(i);
            String wrapped = "<tg-math-block>" + escapeHtml(formula) + "</tg-math-block>";
            text = text.replace(placeholder, wrapped);
        }

        return text;
    }

    private static String extractAndReplace(String text, Pattern pattern, java.util.List<String> extracted) {
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            result.append(text, lastEnd, matcher.start());
            String formulaContent = matcher.group(1).trim();
            int index = extracted.size();
            extracted.add(formulaContent);
            result.append(PLACEHOLDER_PREFIX).append(index).append(PLACEHOLDER_SUFFIX);
            lastEnd = matcher.end();
        }
        result.append(text.substring(lastEnd));
        return result.toString();
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
