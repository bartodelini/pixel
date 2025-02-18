package com.bartodelini.pixel.modules.terminal.bbtext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A <i>SimpleBBParser</i> is used to parse a text into a {@linkplain List} of {@linkplain BBChunk BBChunks}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class SimpleBBParser implements BBParser {

    private final BBTokenizer tokenizer;

    /**
     * Allocates a new {@code SimpleBBParser} object by passing in a {@linkplain BBTokenizer}.
     *
     * @param tokenizer the {@code BBTokenizer} which will be used during the parsing.
     * @throws NullPointerException if the specified {@code BBTokenizer} is {@code null}.
     */
    public SimpleBBParser(BBTokenizer tokenizer) {
        this.tokenizer = Objects.requireNonNull(tokenizer, "tokenizer must not be null");
    }

    /**
     * Returns the {@linkplain BBParseResult} from the passed in text.
     *
     * @param text the text to parse.
     * @return the {@code BBParseResult} from the passed in text.
     * @throws NullPointerException if the specified text is {@code null}.
     */
    @Override
    public BBParseResult parse(String text) {
        List<BBToken> tokens = tokenizer.tokenize(text);
        
        List<BBToken> noParseTokens = new LinkedList<>();
        LinkedList<BBChunk> chunks = new LinkedList<>();
        StringBuilder clearText = new StringBuilder();
        Map<String, List<String>> currentProperties = new HashMap<>();

        for (BBToken token : tokens) {
            String noParseMarker = "noparse";
            String[] split;
            switch (token.type()) {
                case LEFT_DELIMITER -> {
                    if (currentProperties.containsKey(noParseMarker)) {
                        noParseTokens.add(token);
                        break;
                    }
                    split = token.content().split("=", 2);
                    if (split.length > 1) {
                        currentProperties.computeIfAbsent(split[0], k -> new LinkedList<>()).add(split[1]);
                    } else {
                        currentProperties.put(split[0], new LinkedList<>());
                    }
                }
                case TEXT -> {
                    if (currentProperties.containsKey(noParseMarker)) {
                        noParseTokens.add(token);
                        break;
                    }
                    chunks.add(new BBChunk(currentProperties.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> (e.getValue().isEmpty() ? "" : e.getValue().get(e.getValue().size() - 1))
                            )),
                            token.content()));
                    clearText.append(chunks.getLast().content());
                }
                case RIGHT_DELIMITER -> {
                    if (token.content().equals(noParseMarker)) {
                        currentProperties.remove(token.content());
                        if (!noParseTokens.isEmpty()) {
                            chunks.add(new BBChunk(currentProperties.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            e -> (e.getValue().isEmpty() ? "" : e.getValue().get(e.getValue().size() - 1))
                                    )),
                                    noParseTokens.stream().map(BBToken::rawContent).collect(Collectors.joining())));
                            clearText.append(chunks.getLast().content());
                            noParseTokens = new LinkedList<>();
                        }
                    }
                    if (currentProperties.containsKey(noParseMarker)) {
                        noParseTokens.add(token);
                        break;
                    }
                    split = token.content().split("=", 2);
                    List<String> value = currentProperties.get(split[0]);
                    if (value == null) {
                        break;
                    }
                    if (split.length > 1) {
                        value.remove(split[1]);
                        if (value.isEmpty()) {
                            currentProperties.remove(split[0]);
                        }
                    } else {
                        if (value.size() <= 1) {
                            currentProperties.remove(split[0]);
                        } else {
                            value.remove(value.size() - 1);
                        }
                    }
                }
            }
        }
        if (!noParseTokens.isEmpty()) {
            chunks.add(new BBChunk(currentProperties.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> (e.getValue().isEmpty() ? "" : e.getValue().get(e.getValue().size() - 1))
                    )),
                    noParseTokens.stream().map(BBToken::rawContent).collect(Collectors.joining())));
            clearText.append(chunks.getLast().content());
        }

        return new BBParseResult(chunks, clearText.toString(), text);
    }
}