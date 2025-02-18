package com.bartodelini.pixel.modules.input;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.awt.event.KeyEvent.getKeyText;

/**
 * A <i>Key</i> represents a key on the keyboard. It has an alias, a name, as well as a numerical value derived from the
 * respective {@linkplain KeyEvent}.
 *
 * @author Bartolini
 * @version 1.2
 */
public enum Key {

    /**
     * Constant used for an unknown key code.
     */
    UNDEFINED("UNDEFINED", getKeyText(java.awt.event.KeyEvent.VK_UNDEFINED), java.awt.event.KeyEvent.VK_UNDEFINED),

    /**
     * Constant used for the key code of the BACK_SPACE key.
     */
    BACK_SPACE("backspace", getKeyText(java.awt.event.KeyEvent.VK_BACK_SPACE), java.awt.event.KeyEvent.VK_BACK_SPACE),

    /**
     * Constant used for the key code of the DELETE key.
     */
    DELETE("delete", getKeyText(java.awt.event.KeyEvent.VK_DELETE), java.awt.event.KeyEvent.VK_DELETE),

    /**
     * Constant used for the key code of the TAB key.
     */
    TAB("tab", getKeyText(java.awt.event.KeyEvent.VK_TAB), java.awt.event.KeyEvent.VK_TAB),

    /**
     * Constant used for the key code of the CLEAR key.
     */
    CLEAR("clear", getKeyText(java.awt.event.KeyEvent.VK_CLEAR), java.awt.event.KeyEvent.VK_CLEAR),

    /**
     * Constant used for the key code of the ENTER key.
     */
    ENTER("enter", getKeyText(java.awt.event.KeyEvent.VK_ENTER), java.awt.event.KeyEvent.VK_ENTER),

    /**
     * Constant used for the key code of the PAUSE key.
     */
    PAUSE("pause", getKeyText(java.awt.event.KeyEvent.VK_PAUSE), java.awt.event.KeyEvent.VK_PAUSE),

    /**
     * Constant used for the key code of the ESCAPE key.
     */
    ESCAPE("escape", getKeyText(java.awt.event.KeyEvent.VK_ESCAPE), java.awt.event.KeyEvent.VK_ESCAPE),

    /**
     * Constant used for the key code of the SPACE key.
     */
    SPACE("space", getKeyText(java.awt.event.KeyEvent.VK_SPACE), java.awt.event.KeyEvent.VK_SPACE),


    /**
     * Constant used for the key code of the number pad "0" key.
     */
    NUMPAD0("np_0", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD0), java.awt.event.KeyEvent.VK_NUMPAD0),

    /**
     * Constant used for the key code of the number pad "1" key.
     */
    NUMPAD1("np_1", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD1), java.awt.event.KeyEvent.VK_NUMPAD1),

    /**
     * Constant used for the key code of the number pad "2" key.
     */
    NUMPAD2("np_2", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD2), java.awt.event.KeyEvent.VK_NUMPAD2),

    /**
     * Constant used for the key code of the number pad "3" key.
     */
    NUMPAD3("np_3", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD3), java.awt.event.KeyEvent.VK_NUMPAD3),

    /**
     * Constant used for the key code of the number pad "4" key.
     */
    NUMPAD4("np_4", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD4), java.awt.event.KeyEvent.VK_NUMPAD4),

    /**
     * Constant used for the key code of the number pad "5" key.
     */
    NUMPAD5("np_5", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD5), java.awt.event.KeyEvent.VK_NUMPAD5),

    /**
     * Constant used for the key code of the number pad "6" key.
     */
    NUMPAD6("np_6", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD6), java.awt.event.KeyEvent.VK_NUMPAD6),

    /**
     * Constant used for the key code of the number pad "7" key.
     */
    NUMPAD7("np_7", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD7), java.awt.event.KeyEvent.VK_NUMPAD7),

    /**
     * Constant used for the key code of the number pad "8" key.
     */
    NUMPAD8("np_8", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD8), java.awt.event.KeyEvent.VK_NUMPAD8),

    /**
     * Constant used for the key code of the number pad "9" key.
     */
    NUMPAD9("np_9", getKeyText(java.awt.event.KeyEvent.VK_NUMPAD9), java.awt.event.KeyEvent.VK_NUMPAD9),

    /**
     * Constant used for the key code of the number pad decimal point key.
     */
    NUMPAD_DECIMAL("np_decimal", getKeyText(KeyEvent.VK_DECIMAL), KeyEvent.VK_DECIMAL),

    /**
     * Constant used for the key code of the number pad divide key.
     */
    NUMPAD_DIVIDE("np_divide", getKeyText(java.awt.event.KeyEvent.VK_DIVIDE), java.awt.event.KeyEvent.VK_DIVIDE),

    /**
     * Constant used for the key code of the number pad multiply key.
     */
    NUMPAD_MULTIPLY("np_multiply", getKeyText(java.awt.event.KeyEvent.VK_MULTIPLY), java.awt.event.KeyEvent.VK_MULTIPLY),

    /**
     * Constant used for the key code of the number pad subtract key.
     */
    NUMPAD_SUBTRACT("np_subtract", getKeyText(java.awt.event.KeyEvent.VK_SUBTRACT), java.awt.event.KeyEvent.VK_SUBTRACT),

    /**
     * Constant used for the key code of the number pad add key.
     */
    NUMPAD_ADD("np_add", getKeyText(java.awt.event.KeyEvent.VK_ADD), java.awt.event.KeyEvent.VK_ADD),


    /**
     * Constant used for the key code of the number pad HOME key.
     */
    NUMPAD_HOME("np_home", "NumPad Home", (-1) * java.awt.event.KeyEvent.VK_HOME),

    /**
     * Constant used for the key code of the number pad UP key.
     */
    NUMPAD_UP("np_up", "NumPad Up", (-1) * java.awt.event.KeyEvent.VK_UP),

    /**
     * Constant used for the key code of the number pad PAGE_UP key.
     */
    NUMPAD_PAGE_UP("np_pageup", "NumPad Page Up", (-1) * java.awt.event.KeyEvent.VK_PAGE_UP),

    /**
     * Constant used for the key code of the number pad LEFT key.
     */
    NUMPAD_LEFT("np_left", "NumPad Left", (-1) * java.awt.event.KeyEvent.VK_LEFT),

    /**
     * Constant used for the key code of the number pad RIGHT key.
     */
    NUMPAD_RIGHT("np_right", "NumPad Right", (-1) * java.awt.event.KeyEvent.VK_RIGHT),

    /**
     * Constant used for the key code of the number pad END key.
     */
    NUMPAD_END("np_end", "NumPad End", (-1) * java.awt.event.KeyEvent.VK_END),

    /**
     * Constant used for the key code of the number pad DOWN key.
     */
    NUMPAD_DOWN("np_down", "NumPad Down", (-1) * java.awt.event.KeyEvent.VK_DOWN),

    /**
     * Constant used for the key code of the number pad PAGE_DOWN key.
     */
    NUMPAD_PAGE_DOWN("np_pagedown", "NumPad Page Down", (-1) * java.awt.event.KeyEvent.VK_PAGE_DOWN),

    /**
     * Constant used for the key code of the number pad INSERT key.
     */
    NUMPAD_INSERT("np_insert", "NumPad Insert", (-1) * java.awt.event.KeyEvent.VK_INSERT),

    /**
     * Constant used for the key code of the number pad DELETE key.
     */
    NUMPAD_DELETE("np_delete", "NumPad Delete", (-1) * java.awt.event.KeyEvent.VK_DELETE),

    /**
     * Constant used for the key code of the number pad ENTER key.
     */
    NUMPAD_ENTER("np_enter", "NumPad Enter", (-1) * java.awt.event.KeyEvent.VK_ENTER),


    /**
     * Constant used for the key code of the LEFT key.
     */
    LEFT("left", getKeyText(java.awt.event.KeyEvent.VK_LEFT), java.awt.event.KeyEvent.VK_LEFT),

    /**
     * Constant used for the key code of the UP key.
     */
    UP("up", getKeyText(java.awt.event.KeyEvent.VK_UP), java.awt.event.KeyEvent.VK_UP),

    /**
     * Constant used for the key code of the RIGHT key.
     */
    RIGHT("right", getKeyText(java.awt.event.KeyEvent.VK_RIGHT), java.awt.event.KeyEvent.VK_RIGHT),

    /**
     * Constant used for the key code of the DOWN key.
     */
    DOWN("down", getKeyText(java.awt.event.KeyEvent.VK_DOWN), java.awt.event.KeyEvent.VK_DOWN),

    /**
     * Constant used for the key code of the INSERT key.
     */
    INSERT("insert", getKeyText(java.awt.event.KeyEvent.VK_INSERT), java.awt.event.KeyEvent.VK_INSERT),

    /**
     * Constant used for the key code of the HOME key.
     */
    HOME("home", getKeyText(java.awt.event.KeyEvent.VK_HOME), java.awt.event.KeyEvent.VK_HOME),

    /**
     * Constant used for the key code of the END key.
     */
    END("end", getKeyText(java.awt.event.KeyEvent.VK_END), java.awt.event.KeyEvent.VK_END),

    /**
     * Constant used for the key code of the PAGE_UP key.
     */
    PAGE_UP("pageup", getKeyText(java.awt.event.KeyEvent.VK_PAGE_UP), java.awt.event.KeyEvent.VK_PAGE_UP),

    /**
     * Constant used for the key code of the PAGE_DOWN key.
     */
    PAGE_DOWN("pagedown", getKeyText(java.awt.event.KeyEvent.VK_PAGE_DOWN), java.awt.event.KeyEvent.VK_PAGE_DOWN),


    /**
     * Constant used for the key code of the F1 key.
     */
    F1("f1", getKeyText(java.awt.event.KeyEvent.VK_F1), java.awt.event.KeyEvent.VK_F1),

    /**
     * Constant used for the key code of the F2 key.
     */
    F2("f2", getKeyText(java.awt.event.KeyEvent.VK_F2), java.awt.event.KeyEvent.VK_F2),

    /**
     * Constant used for the key code of the F3 key.
     */
    F3("f3", getKeyText(java.awt.event.KeyEvent.VK_F3), java.awt.event.KeyEvent.VK_F3),

    /**
     * Constant used for the key code of the F4 key.
     */
    F4("f4", getKeyText(java.awt.event.KeyEvent.VK_F4), java.awt.event.KeyEvent.VK_F4),

    /**
     * Constant used for the key code of the F5 key.
     */
    F5("f5", getKeyText(java.awt.event.KeyEvent.VK_F5), java.awt.event.KeyEvent.VK_F5),

    /**
     * Constant used for the key code of the F6 key.
     */
    F6("f6", getKeyText(java.awt.event.KeyEvent.VK_F6), java.awt.event.KeyEvent.VK_F6),

    /**
     * Constant used for the key code of the F7 key.
     */
    F7("f7", getKeyText(java.awt.event.KeyEvent.VK_F7), java.awt.event.KeyEvent.VK_F7),

    /**
     * Constant used for the key code of the F8 key.
     */
    F8("f8", getKeyText(java.awt.event.KeyEvent.VK_F8), java.awt.event.KeyEvent.VK_F8),

    /**
     * Constant used for the key code of the F9 key.
     */
    F9("f9", getKeyText(java.awt.event.KeyEvent.VK_F9), java.awt.event.KeyEvent.VK_F9),

    /**
     * Constant used for the key code of the F10 key.
     */
    F10("f10", getKeyText(java.awt.event.KeyEvent.VK_F10), java.awt.event.KeyEvent.VK_F10),

    /**
     * Constant used for the key code of the F11 key.
     */
    F11("f11", getKeyText(java.awt.event.KeyEvent.VK_F11), java.awt.event.KeyEvent.VK_F11),

    /**
     * Constant used for the key code of the F12 key.
     */
    F12("f12", getKeyText(java.awt.event.KeyEvent.VK_F12), java.awt.event.KeyEvent.VK_F12),

    /**
     * Constant used for the key code of the F13 key.
     */
    F13("f13", getKeyText(java.awt.event.KeyEvent.VK_F13), java.awt.event.KeyEvent.VK_F13),

    /**
     * Constant used for the key code of the F14 key.
     */
    F14("f14", getKeyText(java.awt.event.KeyEvent.VK_F14), java.awt.event.KeyEvent.VK_F14),

    /**
     * Constant used for the key code of the F15 key.
     */
    F15("f15", getKeyText(java.awt.event.KeyEvent.VK_F15), java.awt.event.KeyEvent.VK_F15),


    /**
     * Constant used for the key code of the "0" key.
     */
    ALPHA0("0", getKeyText(java.awt.event.KeyEvent.VK_0), java.awt.event.KeyEvent.VK_0),

    /**
     * Constant used for the key code of the "1" key.
     */
    ALPHA1("1", getKeyText(java.awt.event.KeyEvent.VK_1), java.awt.event.KeyEvent.VK_1),

    /**
     * Constant used for the key code of the "2" key.
     */
    ALPHA2("2", getKeyText(java.awt.event.KeyEvent.VK_2), java.awt.event.KeyEvent.VK_2),

    /**
     * Constant used for the key code of the "3" key.
     */
    ALPHA3("3", getKeyText(java.awt.event.KeyEvent.VK_3), java.awt.event.KeyEvent.VK_3),

    /**
     * Constant used for the key code of the "4" key.
     */
    ALPHA4("4", getKeyText(java.awt.event.KeyEvent.VK_4), java.awt.event.KeyEvent.VK_4),

    /**
     * Constant used for the key code of the "5" key.
     */
    ALPHA5("5", getKeyText(java.awt.event.KeyEvent.VK_5), java.awt.event.KeyEvent.VK_5),

    /**
     * Constant used for the key code of the "6" key.
     */
    ALPHA6("6", getKeyText(java.awt.event.KeyEvent.VK_6), java.awt.event.KeyEvent.VK_6),

    /**
     * Constant used for the key code of the "7" key.
     */
    ALPHA7("7", getKeyText(java.awt.event.KeyEvent.VK_7), java.awt.event.KeyEvent.VK_7),

    /**
     * Constant used for the key code of the "8" key.
     */
    ALPHA8("8", getKeyText(java.awt.event.KeyEvent.VK_8), java.awt.event.KeyEvent.VK_8),

    /**
     * Constant used for the key code of the "9" key.
     */
    ALPHA9("9", getKeyText(java.awt.event.KeyEvent.VK_9), java.awt.event.KeyEvent.VK_9),


    /**
     * Constant used for the key code of the "!" key.
     */
    EXCLAMATION_MARK("!", getKeyText(java.awt.event.KeyEvent.VK_EXCLAMATION_MARK), java.awt.event.KeyEvent.VK_EXCLAMATION_MARK),

    /**
     * Constant used for the key code of the DOUBLE_QUOTE key.
     */
    DOUBLE_QUOTE("double_quote", getKeyText(java.awt.event.KeyEvent.VK_QUOTEDBL), java.awt.event.KeyEvent.VK_QUOTEDBL),

    /**
     * Constant used for the key code of the "#" key.
     */
    HASH("#", getKeyText(java.awt.event.KeyEvent.VK_NUMBER_SIGN), java.awt.event.KeyEvent.VK_NUMBER_SIGN),

    /**
     * Constant used for the key code of the "$" key.
     */
    DOLLAR("$", getKeyText(java.awt.event.KeyEvent.VK_DOLLAR), java.awt.event.KeyEvent.VK_DOLLAR),

    /**
     * Constant used for the key code of the "`" key.
     */
    AMPERSAND("&", getKeyText(java.awt.event.KeyEvent.VK_AMPERSAND), java.awt.event.KeyEvent.VK_AMPERSAND),

    /**
     * Constant used for the key code of the QUOTE key.
     */
    QUOTE("quote", getKeyText(java.awt.event.KeyEvent.VK_QUOTE), java.awt.event.KeyEvent.VK_QUOTE),

    /**
     * Constant used for the key code of the "(" key.
     */
    LEFT_PARENTHESIS("(", getKeyText(java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS), java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS),

    /**
     * Constant used for the key code of the ")" key.
     */
    RIGHT_PARENTHESIS(")", getKeyText(java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS), java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS),

    /**
     * Constant used for the key code of the "*" key.
     */
    ASTERISK("*", getKeyText(java.awt.event.KeyEvent.VK_ASTERISK), java.awt.event.KeyEvent.VK_ASTERISK),

    /**
     * Constant used for the key code of the "+" key.
     */
    PLUS("+", getKeyText(java.awt.event.KeyEvent.VK_PLUS), java.awt.event.KeyEvent.VK_PLUS),

    /**
     * Constant used for the key code of the "," key.
     */
    COMMA(",", getKeyText(java.awt.event.KeyEvent.VK_COMMA), java.awt.event.KeyEvent.VK_COMMA),

    /**
     * Constant used for the key code of the "-" key.
     */
    MINUS("-", getKeyText(java.awt.event.KeyEvent.VK_MINUS), java.awt.event.KeyEvent.VK_MINUS),

    /**
     * Constant used for the key code of the "." key.
     */
    PERIOD(".", getKeyText(java.awt.event.KeyEvent.VK_PERIOD), java.awt.event.KeyEvent.VK_PERIOD),

    /**
     * Constant used for the key code of the "/" key.
     */
    SLASH("/", getKeyText(java.awt.event.KeyEvent.VK_SLASH), java.awt.event.KeyEvent.VK_SLASH),

    /**
     * Constant used for the key code of the ":" key.
     */
    COLON(":", getKeyText(java.awt.event.KeyEvent.VK_COLON), java.awt.event.KeyEvent.VK_COLON),

    /**
     * Constant used for the key code of the ")," key.
     */
    SEMICOLON(";", getKeyText(java.awt.event.KeyEvent.VK_SEMICOLON), java.awt.event.KeyEvent.VK_SEMICOLON),

    /**
     * Constant used for the key code of the "<" key.
     */
    LESS("<", getKeyText(java.awt.event.KeyEvent.VK_LESS), java.awt.event.KeyEvent.VK_LESS),

    /**
     * Constant used for the key code of the "=" key.
     */
    EQUALS("=", getKeyText(java.awt.event.KeyEvent.VK_EQUALS), java.awt.event.KeyEvent.VK_EQUALS),

    /**
     * Constant used for the key code of the ">" key.
     */
    GREATER(">", getKeyText(java.awt.event.KeyEvent.VK_GREATER), java.awt.event.KeyEvent.VK_GREATER),

    /**
     * Constant used for the key code of the "@" key.
     */
    AT("@", getKeyText(java.awt.event.KeyEvent.VK_AT), java.awt.event.KeyEvent.VK_AT),

    /**
     * Constant used for the key code of the "[" key.
     */
    LEFT_BRACKET("[", getKeyText(java.awt.event.KeyEvent.VK_OPEN_BRACKET), java.awt.event.KeyEvent.VK_OPEN_BRACKET),

    /**
     * Constant used for the key code of the "\" key.
     */
    BACK_SLASH("\\", getKeyText(java.awt.event.KeyEvent.VK_BACK_SLASH), java.awt.event.KeyEvent.VK_BACK_SLASH),

    /**
     * Constant used for the key code of the "]" key.
     */
    RIGHT_BRACKET("]", getKeyText(java.awt.event.KeyEvent.VK_CLOSE_BRACKET), java.awt.event.KeyEvent.VK_CLOSE_BRACKET),

    /**
     * Constant used for the key code of the "{" key.
     */
    LEFT_BRACE("{", getKeyText(java.awt.event.KeyEvent.VK_BRACELEFT), java.awt.event.KeyEvent.VK_BRACELEFT),

    /**
     * Constant used for the key code of the "}" key.
     */
    RIGHT_BRACE("}", getKeyText(java.awt.event.KeyEvent.VK_BRACERIGHT), java.awt.event.KeyEvent.VK_BRACERIGHT),

    /**
     * Constant used for the key code of the "^" key.
     */
    CIRCUMFLEX("^", getKeyText(java.awt.event.KeyEvent.VK_CIRCUMFLEX), java.awt.event.KeyEvent.VK_CIRCUMFLEX),

    /**
     * Constant used for the key code of the "_" key.
     */
    UNDERSCORE("_", getKeyText(java.awt.event.KeyEvent.VK_UNDERSCORE), java.awt.event.KeyEvent.VK_UNDERSCORE),

    /**
     * Constant used for the key code of the "\" key.
     */
    BACK_QUOTE("`", getKeyText(java.awt.event.KeyEvent.VK_BACK_QUOTE), java.awt.event.KeyEvent.VK_BACK_QUOTE),


    /**
     * Constant used for the key code of the "A" key.
     */
    A("a", getKeyText(java.awt.event.KeyEvent.VK_A), java.awt.event.KeyEvent.VK_A),

    /**
     * Constant used for the key code of the "B" key.
     */
    B("b", getKeyText(java.awt.event.KeyEvent.VK_B), java.awt.event.KeyEvent.VK_B),

    /**
     * Constant used for the key code of the "C" key.
     */
    C("c", getKeyText(java.awt.event.KeyEvent.VK_C), java.awt.event.KeyEvent.VK_C),

    /**
     * Constant used for the key code of the "D" key.
     */
    D("d", getKeyText(java.awt.event.KeyEvent.VK_D), java.awt.event.KeyEvent.VK_D),

    /**
     * Constant used for the key code of the "E" key.
     */
    E("e", getKeyText(java.awt.event.KeyEvent.VK_E), java.awt.event.KeyEvent.VK_E),

    /**
     * Constant used for the key code of the "F" key.
     */
    F("f", getKeyText(java.awt.event.KeyEvent.VK_F), java.awt.event.KeyEvent.VK_F),

    /**
     * Constant used for the key code of the "G" key.
     */
    G("g", getKeyText(java.awt.event.KeyEvent.VK_G), java.awt.event.KeyEvent.VK_G),

    /**
     * Constant used for the key code of the "H" key.
     */
    H("h", getKeyText(java.awt.event.KeyEvent.VK_H), java.awt.event.KeyEvent.VK_H),

    /**
     * Constant used for the key code of the "I" key.
     */
    I("i", getKeyText(java.awt.event.KeyEvent.VK_I), java.awt.event.KeyEvent.VK_I),

    /**
     * Constant used for the key code of the "J" key.
     */
    J("j", getKeyText(java.awt.event.KeyEvent.VK_J), java.awt.event.KeyEvent.VK_J),

    /**
     * Constant used for the key code of the "K" key.
     */
    K("k", getKeyText(java.awt.event.KeyEvent.VK_K), java.awt.event.KeyEvent.VK_K),

    /**
     * Constant used for the key code of the "L" key.
     */
    L("l", getKeyText(java.awt.event.KeyEvent.VK_L), java.awt.event.KeyEvent.VK_L),

    /**
     * Constant used for the key code of the "M" key.
     */
    M("m", getKeyText(java.awt.event.KeyEvent.VK_M), java.awt.event.KeyEvent.VK_M),

    /**
     * Constant used for the key code of the "N" key.
     */
    N("n", getKeyText(java.awt.event.KeyEvent.VK_N), java.awt.event.KeyEvent.VK_N),

    /**
     * Constant used for the key code of the "O" key.
     */
    O("o", getKeyText(java.awt.event.KeyEvent.VK_O), java.awt.event.KeyEvent.VK_O),

    /**
     * Constant used for the key code of the "P" key.
     */
    P("p", getKeyText(java.awt.event.KeyEvent.VK_P), java.awt.event.KeyEvent.VK_P),

    /**
     * Constant used for the key code of the "Q" key.
     */
    Q("q", getKeyText(java.awt.event.KeyEvent.VK_Q), java.awt.event.KeyEvent.VK_Q),

    /**
     * Constant used for the key code of the "R" key.
     */
    R("r", getKeyText(java.awt.event.KeyEvent.VK_R), java.awt.event.KeyEvent.VK_R),

    /**
     * Constant used for the key code of the "S" key.
     */
    S("s", getKeyText(java.awt.event.KeyEvent.VK_S), java.awt.event.KeyEvent.VK_S),

    /**
     * Constant used for the key code of the "T" key.
     */
    T("t", getKeyText(java.awt.event.KeyEvent.VK_T), java.awt.event.KeyEvent.VK_T),

    /**
     * Constant used for the key code of the "U" key.
     */
    U("u", getKeyText(java.awt.event.KeyEvent.VK_U), java.awt.event.KeyEvent.VK_U),

    /**
     * Constant used for the key code of the "V" key.
     */
    V("v", getKeyText(java.awt.event.KeyEvent.VK_V), java.awt.event.KeyEvent.VK_V),

    /**
     * Constant used for the key code of the "W" key.
     */
    W("w", getKeyText(java.awt.event.KeyEvent.VK_W), java.awt.event.KeyEvent.VK_W),

    /**
     * Constant used for the key code of the "X" key.
     */
    X("x", getKeyText(java.awt.event.KeyEvent.VK_X), java.awt.event.KeyEvent.VK_X),

    /**
     * Constant used for the key code of the "Y" key.
     */
    Y("y", getKeyText(java.awt.event.KeyEvent.VK_Y), java.awt.event.KeyEvent.VK_Y),

    /**
     * Constant used for the key code of the "Z" key.
     */
    Z("z", getKeyText(java.awt.event.KeyEvent.VK_Z), java.awt.event.KeyEvent.VK_Z),


    /**
     * Constant used for the key code of the NUM_LOCK key.
     */
    NUM_LOCK("num_lk", getKeyText(java.awt.event.KeyEvent.VK_NUM_LOCK), java.awt.event.KeyEvent.VK_NUM_LOCK),

    /**
     * Constant used for the key code of the CAPS_LOCK key.
     */
    CAPS_LOCK("capslock", getKeyText(java.awt.event.KeyEvent.VK_CAPS_LOCK), java.awt.event.KeyEvent.VK_CAPS_LOCK),

    /**
     * Constant used for the key code of the SCROLL_LOCK key.
     */
    SCROLL_LOCK("scr_lk", getKeyText(java.awt.event.KeyEvent.VK_SCROLL_LOCK), java.awt.event.KeyEvent.VK_SCROLL_LOCK),

    /**
     * Constant used for the key code of the RIGHT_SHIFT key.
     */
    RIGHT_SHIFT("rshift", "Right Shift", (-1) * java.awt.event.KeyEvent.VK_SHIFT),

    /**
     * Constant used for the key code of the SHIFT key.
     */
    SHIFT("shift", getKeyText(java.awt.event.KeyEvent.VK_SHIFT), java.awt.event.KeyEvent.VK_SHIFT),

    /**
     * Constant used for the key code of the RIGHT_CONTROL key.
     */
    RIGHT_CONTROL("rctrl", "Right Control", (-1) * java.awt.event.KeyEvent.VK_CONTROL),

    /**
     * Constant used for the key code of the CONTROL key.
     */
    CONTROL("ctrl", getKeyText(java.awt.event.KeyEvent.VK_CONTROL), java.awt.event.KeyEvent.VK_CONTROL),

    /**
     * Constant used for the key code of the ALT key.
     */
    ALT("alt", getKeyText(java.awt.event.KeyEvent.VK_ALT), java.awt.event.KeyEvent.VK_ALT),

    /**
     * Constant used for the key code of the META key.
     */
    META("meta", getKeyText(java.awt.event.KeyEvent.VK_META), java.awt.event.KeyEvent.VK_META),

    /**
     * Constant used for the key code of the WINDOWS key.
     */
    WINDOWS("windows", getKeyText(java.awt.event.KeyEvent.VK_WINDOWS), java.awt.event.KeyEvent.VK_WINDOWS),

    /**
     * Constant used for the key code of the HELP key.
     */
    HELP("help", getKeyText(java.awt.event.KeyEvent.VK_HELP), java.awt.event.KeyEvent.VK_HELP),

    /**
     * Constant used for the key code of the PRINTSCREEN key.
     */
    PRINTSCREEN("prt_sc", getKeyText(java.awt.event.KeyEvent.VK_PRINTSCREEN), java.awt.event.KeyEvent.VK_PRINTSCREEN),

    /**
     * Constant used for the key code of the CONTEXT_MENU key.
     */
    CONTEXT_MENU("context_menu", getKeyText(java.awt.event.KeyEvent.VK_CONTEXT_MENU), java.awt.event.KeyEvent.VK_CONTEXT_MENU),

    /**
     * Constant used for the key code of the CANCEL key.
     */
    CANCEL("cancel", getKeyText(java.awt.event.KeyEvent.VK_CANCEL), java.awt.event.KeyEvent.VK_CANCEL);

    private static final Map<String, Key> aliasMap = new HashMap<>();
    private static final Map<String, Key> nameMap = new HashMap<>();
    private static final Map<Integer, Key> valueMap = new HashMap<>();

    static {
        for (Key key : Key.values()) {
            aliasMap.put(key.getAlias(), key);
            nameMap.put(key.getName(), key);
            valueMap.put(key.getValue(), key);
        }
    }

    /**
     * Returns a {@code Key} by its alias.
     *
     * @param alias the alias of the wished {@code Key}.
     * @return the {@code Key} with the matching alias, or {@code null} if no such {@code Key} was found.
     * @throws NullPointerException if the specified alias is {@code null}.
     */
    public static Key getByAlias(String alias) {
        Objects.requireNonNull(alias, "alias must not be null");
        return aliasMap.get(alias);
    }

    /**
     * Returns a {@code Key} by its name.
     *
     * @param name the name of the wished {@code Key}.
     * @return the {@code Key} with the matching name, or {@code null} if no such {@code Key} was found.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public static Key getByName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return nameMap.get(name);
    }

    /**
     * Returns a {@code Key} by its numerical value.
     *
     * @param value the numerical value of the wished {@code Key}.
     * @return the {@code Key} with the numerical value, or {@code null} if no such {@code Key} was found.
     */
    public static Key getByValue(int value) {
        return valueMap.get(value);
    }

    /**
     * Returns a {@code Key} matching the passed in {@linkplain KeyEvent}.
     *
     * @param event the {@code KeyEvent} to return a matchin {@code Key} for.
     * @return a {@code Key} matching the specified {@code KeyEvent}, or {@code null} if no such {@code Key} was found.
     * @throws NullPointerException if the specified {@code KeyEvent} is {@code null}.
     */
    public static Key getFromAWTKeyEvent(KeyEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        return getByValue(getModifiedKeyCodeFromKeyEvent(event));
    }

    /**
     * Helper method used to return the modified key code for a passed in {@linkplain KeyEvent}.
     *
     * @param e the {@code KeyEvent} to get the modified key code for.
     * @return the modified key code for a specified {@code KeyEvent}.
     */
    private static int getModifiedKeyCodeFromKeyEvent(KeyEvent e) {
        if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT || e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SHIFT:
                case KeyEvent.VK_CONTROL:
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_HOME:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_PAGE_UP:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_END:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_PAGE_DOWN:
                case KeyEvent.VK_INSERT:
                case KeyEvent.VK_DELETE:
                    return getKeyCodeForRightKey(e.getKeyCode());
                case KeyEvent.VK_ALT:
                    return KeyEvent.VK_ALT_GRAPH;
            }
        }
        return e.getKeyCode();
    }

    /**
     * Helper method used to return the key code for the right version of a key.
     *
     * @param keyCode the key code of the key to get the right version for.
     * @return the key code for the right version of a key.
     */
    private static int getKeyCodeForRightKey(int keyCode) {
        return (-1) * keyCode;
    }

    private final String alias;
    private final String name;
    private final int value;

    /**
     * Allocates a new {@code Key} object by passing in an alias, a name, as well as a value.
     *
     * @param alias the alias used for this {@code Key}.
     * @param name  the name used for this {@code Key}.
     * @param value the value used for this {@code Key}.
     * @throws NullPointerException if the specified alias or name are {@code null}.
     */
    Key(String alias, String name, int value) {
        this.alias = Objects.requireNonNull(alias, "alias must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.value = value;
    }

    /**
     * Returns the alias of this {@code Key}.
     *
     * @return the alias of this {@code Key}.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Returns the name of this {@code Key}.
     *
     * @return the name of this {@code Key}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the numerical value of this {@code Key}.
     *
     * @return the numerical value of this {@code Key}.
     */
    public int getValue() {
        return value;
    }
}