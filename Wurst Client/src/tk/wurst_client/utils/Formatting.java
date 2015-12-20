package tk.wurst_client.utils;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global special char (or string) class to prevent the encoding errors from happening again :/
 *
 * @author nulldev
 */
public class Formatting {
    //Section signs
    public static char SECTION_SIGN = '§';
    public static char SS = SECTION_SIGN;

    //Colors (yay)
    public static String BLACK = SS + "0";
    public static String DARK_BLUE = SS + "1";
    public static String DARK_GREEN = SS + "2";
    public static String DARK_AQUA = SS + "3";
    public static String DARK_RED = SS + "4";
    public static String DARK_PURPLE = SS + "5";
    public static String GOLD = SS + "6";
    public static String GRAY = SS + "7";
    public static String DARK_GRAY = SS + "8";
    public static String BLUE = SS + "9";
    public static String GREEN = SS + "a";
    public static String AQUA = SS + "b";
    public static String RED = SS + "c";
    public static String LIGHT_PURPLE = SS + "d";
    public static String YELLOW = SS + "e";
    public static String WHITE = SS + "f";

    private static HashMap<String, String> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("BLACK", BLACK);
        COLOR_MAP.put("DARK_BLUE", DARK_BLUE);
        COLOR_MAP.put("DARK_GREEN", DARK_GREEN);
        COLOR_MAP.put("DARK_AQUA", DARK_AQUA);
        COLOR_MAP.put("DARK_RED", DARK_RED);
        COLOR_MAP.put("DARK_PURPLE", DARK_PURPLE);
        COLOR_MAP.put("GOLD", GOLD);
        COLOR_MAP.put("GRAY", GRAY);
        COLOR_MAP.put("DARK_GRAY", DARK_GRAY);
        COLOR_MAP.put("BLUE", BLUE);
        COLOR_MAP.put("GREEN", GREEN);
        COLOR_MAP.put("AQUA", AQUA);
        COLOR_MAP.put("RED", RED);
        COLOR_MAP.put("LIGHT_PURPLE", LIGHT_PURPLE);
        COLOR_MAP.put("YELLOW", YELLOW);
        COLOR_MAP.put("WHITE", WHITE);
    }

    //Formatting stuff
    public static String OBSFUCATED = SS + "k";
    public static String OBS = OBSFUCATED;
    public static String BOLD = SS + "l";
    public static String STRIKETHROUGH = SS + "m";
    public static String STRIKE = STRIKETHROUGH;
    public static String UNDERLINE = SS + "n";
    public static String UNDER = UNDERLINE;
    public static String ITALIC = SS + "o";
    public static String RESET = SS + "r";
    public static String R = RESET;

    /**
     * Replace all &s in a string with section signs.
     * @param string The string to replace the '&' signs in.
     * @return The new string.
     */
    public static String replaceAnds(String string) {
        return string.replace('&', SS);
    }

    private static ConcurrentHashMap<String, String> PARSE_CACHE = null;
    public static ConcurrentHashMap<String, String> initAndGetParseCache() {
        if(PARSE_CACHE == null) PARSE_CACHE = new ConcurrentHashMap<>();
        return PARSE_CACHE;
    }

    public static boolean isColor(String string) {
        return COLOR_MAP.containsKey(string);
    }

    public static boolean isSectionSign(String string) {
        return string.equals("SECTION_SIGN") || string.equals("SS");
    }

    public static boolean isFormatting(String string) {
        return !isColor(string) && !isSectionSign(string);
    }

    public static String processTag(String string) {
        if(COLOR_MAP.containsKey(string)) {
            return COLOR_MAP.get(string);
        } else {
            try {
                Field field = Formatting.class.getField(string);
                if(isSectionSign(field.getName())) {
                    return (char) field.get(null) + "";
                }
                return (String) field.get(null);
            } catch (NoSuchFieldException e) {
                return null;
            } catch (IllegalAccessException e) {
                throw new InternalError("WTF? Access exception on public field?");
            }
        }
    }

    /**
     * Converts human readable color codes to Minecraft color codes, DOM style! Examples:
     *
     * <RED>Test</RED> = §cTest§f
     * <RED>Alpha<GREEN>Beta</GREEN>Theta</RED> = §cAlpha§aBeta§cTheta§f
     * <OBSFUCATED><RED>Test</RED></OBSFUCATED> = §k§cTest§f§r§f
     *
     * DOM is awesome right? :P
     *
     * @param string The string to parse
     * @return The parsed string
     */
    public static String format(String string) {
        if(initAndGetParseCache().contains(string)) return initAndGetParseCache().get(string);
        Stack<String> currentColor = new Stack<>();
        Stack<String> currentFormatting = new Stack<>();
        char[] asArray = string.toCharArray();
        boolean isEscaping = false;
        boolean inTag = false;
        boolean nextTagCancels = false;
        StringBuilder builtString = new StringBuilder();
        StringBuilder currentTag = new StringBuilder();
        for(char c : asArray) {
            if(!isEscaping) {
                if (c == '<') {
                    inTag = true;
                } else if(c == '/' && inTag) {
                    nextTagCancels = true;
                } else if(c == '>') {
                    if(!inTag) throw new IllegalArgumentException("Invalid input string (unexpected end of tag)!");
                    inTag = false;
                    String tag = currentTag.toString().toUpperCase();
                    boolean updateColor = false;
                    boolean updateFormatting = false;
                    boolean cancelProcess = false;
                    StringBuilder update = new StringBuilder();
                    if(nextTagCancels) {
                        if(isColor(tag)) {
                            if (!tag.equals(currentColor.pop())) {
                                throw new IllegalArgumentException("Invalid input string (mismatched closing tag)!");
                            }
                            updateColor = true;
                        } else if(isFormatting(tag)) {
                            if (!tag.equals(currentFormatting.pop())) {
                                throw new IllegalArgumentException("Invalid input string (mismatched closing tag)!");
                            }
                            updateFormatting = true;
                            cancelProcess = true;
                        } else {
                            throw new IllegalArgumentException("Invalid input string (invalid closing tag)!");
                        }
                    } else if(isColor(tag)) {
                        currentColor.push(tag);
                        updateColor = true;
                    } else if(isFormatting(tag)) {
                        currentFormatting.push(tag);
                        update.append(processTag(tag));
                        cancelProcess = true;
                    }
                    if(updateFormatting) {
                        update.append(RESET);
                        for(String formatting : currentFormatting) {
                            update.append(processTag(formatting));
                        }
                        updateColor = true;
                    }
                    if(updateColor) {
                        if(currentColor.size() < 1) {
                            update.append(WHITE);
                        } else {
                            update.append(processTag(currentColor.peek()));
                        }
                    } else if(!cancelProcess) {
                        update.append(processTag(tag));
                    }
                    builtString.append(update);
                    currentTag.setLength(0);
                    nextTagCancels = false;
                } else if(c == '\\') {
                    isEscaping = true;
                } else {
                    if(inTag) {
                        currentTag.append(c);
                    } else {
                        builtString.append(c);
                    }
                }
            } else {
                isEscaping = false;
            }
        }
        String out = builtString.toString();
        initAndGetParseCache().put(string, out);
        return out;
    }

    /**
     * Alias to:
     * @see Formatting#format(String) format
     */
    public static String f(String string) {
        return format(string);
    }
}
