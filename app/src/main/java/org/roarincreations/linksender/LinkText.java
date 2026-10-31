package org.roarincreations.linksender;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Pure Java, no Android/SMS/network access. Bare hostnames are displayed with an explicit HTTPS prefix. */
public final class LinkText {
    public static final int MAX_URL_CHARS = 4096;
    // Reject matches embedded in emails or paths; accept ordinary domains with alphabetic TLDs.
    // Preserve #fragments on bare hostnames; dropping them silently changes the selected destination.
    private static final Pattern CANDIDATE = Pattern.compile(
        "(?i)(?<![\\p{L}\\p{M}\\p{N}@._/-])(?:https?://(?:[^\\s<>\\\"']|(?<=[a-z0-9])'(?=[a-z0-9]))++|(?:[a-z0-9-]+\\.){1,127}[a-z]{2,}(?::[0-9]{1,5})?(?:/(?:[^\\s<>\\\"']|(?<=[a-z0-9])'(?=[a-z0-9]))*+)?(?:\\?(?:[^\\s<>\\\"']|(?<=[a-z0-9])'(?=[a-z0-9]))*+)?(?:#(?:[^\\s<>\\\"']|(?<=[a-z0-9])'(?=[a-z0-9]))*+)?(?![:@\\p{L}\\p{M}\\p{N}_-]|\\.(?=[\\p{L}\\p{M}\\p{N}_-])|\\.(?=\\.++[\\p{L}\\p{M}\\p{N}_-])|[%\\\\+=$&~^]))");
    private LinkText() { }

    public static List<String> extract(String text) {
        if (text == null || text.isEmpty()) return Collections.emptyList();
        // A complete copied URL is unambiguous input: do not strip meaningful trailing characters.
        // Embedded prose still uses the existing conservative punctuation-trimming path.
        String whole = text.trim();
        String wholeUrl = whole;
        if (whole.matches("(?i)^https?://.*") && valid(wholeUrl)) {
            return Collections.singletonList(wholeUrl);
        }
        List<String> found = new ArrayList<>();
        Matcher matcher = CANDIDATE.matcher(text);
        while (matcher.find()) {
            String candidate = trimTrailing(matcher.group());
            String normalized = candidate.matches("(?i)^https?://.*") ? candidate : "https://" + candidate;
            if (valid(normalized) && !found.contains(normalized)) found.add(normalized);
        }
        return found;
    }

    /** @why Long runs of unmatched closing punctuation must not stall the Android UI.
     *  @how Count bracket balances once, then move an end index without rescanning
     *  or copying the shrinking string. Equivalent to the historical trimming rule. */
    private static String trimTrailing(String value) {
        int openParen = 0, closeParen = 0, openBracket = 0, closeBracket = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '(') openParen++;
            else if (c == ')') closeParen++;
            else if (c == '[') openBracket++;
            else if (c == ']') closeBracket++;
        }
        int end = value.length();
        while (end > 0) {
            char last = value.charAt(end - 1);
            boolean terminalPunctuation = ".,;!?".indexOf(last) >= 0;
            boolean excessCloseParen = last == ')' && openParen < closeParen;
            boolean excessCloseBracket = last == ']' && openBracket < closeBracket;
            if (!terminalPunctuation && !excessCloseParen && !excessCloseBracket) break;
            if (last == ')') closeParen--;
            else if (last == ']') closeBracket--;
            end--;
        }
        return end == value.length() ? value : value.substring(0, end);
    }

    public static boolean valid(String url) {
        if (url == null || url.length() < 9 || url.length() > MAX_URL_CHARS) return false;
        for (int i = 0; i < url.length(); i++) if (Character.isISOControl(url.charAt(i))) return false;
        try {
            URI parsed = new URI(url);
            String scheme = parsed.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("http"))) return false;
            if (parsed.getHost() == null || parsed.getHost().isEmpty() || parsed.getRawUserInfo() != null) return false;
            if (parsed.getPort() > 65535 || parsed.getPort() < -1) return false;
            return true;
        } catch (Exception e) { return false; }
    }
}
