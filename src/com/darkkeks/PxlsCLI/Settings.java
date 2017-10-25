package com.darkkeks.PxlsCLI;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.*;

public class Settings {

    private static JsonObject sets;

    public Settings(final String path) {
        try {
            readSettings(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readSettings(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();

        sets = PxlsCLI.gson.parse(fileAsString).getAsJsonObject();
    }

    public String getTokensFilePath() {
        return sets.get("tokensFilePath").getAsString();
    }
    public String getTemplateURI() {
        return sets.get("templateURI").getAsString();
    }
    public int getTemplateOffsetX() {
        return sets.get("templateOffsetX").getAsInt();
    }
    public int getTemplateOffsetY() {
        return sets.get("templateOffsetY").getAsInt();
    }
    public float getTemplateOpacity() {
        return sets.get("templateOpacity").getAsFloat();
    }
    public boolean getTemplateReplacePixels() {
        return sets.get("templateReplacePixels").getAsBoolean();
    }

    private JsonObject getControls() {
        return sets.get("controls").getAsJsonObject();
    }
    public int getControlsUp() {
        return parseKeyCode(getControls().get("up").getAsString());
    }
    public int getControlsRight() {
        return parseKeyCode(getControls().get("right").getAsString());
    }
    public int getControlsLeft() {
        return parseKeyCode(getControls().get("left").getAsString());
    }
    public int getControlsDown() {
        return parseKeyCode(getControls().get("down").getAsString());
    }
    public int getControlsZoomIn() {
        return parseKeyCode(getControls().get("zoomIn").getAsString());
    }
    public int getControlsZoomOut() {
        return parseKeyCode(getControls().get("zoomOut").getAsString());
    }
    public int getControlsShift() {
        return parseKeyCode(getControls().get("shift").getAsString());
    }
    public int getControlsCtrl() {
        return  parseKeyCode(getControls().get("ctrl").getAsString());
    }

    private int parseKeyCode(String key) {
        try {
            if (key.isEmpty()) return 0;
            int c = Integer.parseInt(key);

            if (c < 0) return 0;
            if (c > 9) return c;
        } catch (NumberFormatException e) { }

        key = key.toLowerCase();
        switch (key) {
            case "break":           return 3;
            case "backspace":       return 8;
            case "tab":             return 9;
            case "clear":           return 12;
            case "enter":           return 13;
            case "shift":           return 16;
            case "ctrl":            return 17;
            case "alt":             return 18;
            case "pause/break":     return 19;
            case "pausebreak":      return 19;
            case "pause":           return 19;
            case "capslock":        return 20;
            case "escape":          return 27;
            case "spacebar":        return 32;
            case "space":           return 32;
            case " ":               return 32;
            case "pageup":          return 33;
            case "pagedown":        return 34;
            case "end":             return 35;
            case "home":            return 36;
            case "leftarrow":       return 37;
            case "left":            return 37;
            case "uparrow":         return 38;
            case "up":              return 38;
            case "rightarrow":      return 39;
            case "right":           return 39;
            case "downarrow":       return 40;
            case "down":            return 40;
            case "insert":          return 45;
            case "delete":          return 46;
            case "0":               return 48;
            case "1":               return 49;
            case "2":               return 50;
            case "3":               return 51;
            case "4":               return 52;
            case "5":               return 53;
            case "6":               return 54;
            case "7":               return 55;
            case "8":               return 56;
            case "9":               return 57;
            case "a":               return 65;
            case "b":               return 66;
            case "c":               return 67;
            case "d":               return 68;
            case "e":               return 69;
            case "f":               return 70;
            case "g":               return 71;
            case "h":               return 72;
            case "i":               return 73;
            case "j":               return 74;
            case "k":               return 75;
            case "l":               return 76;
            case "m":               return 77;
            case "n":               return 78;
            case "o":               return 79;
            case "p":               return 80;
            case "q":               return 81;
            case "r":               return 82;
            case "s":               return 83;
            case "t":               return 84;
            case "u":               return 85;
            case "v":               return 86;
            case "w":               return 87;
            case "x":               return 88;
            case "y":               return 89;
            case "z":               return 90;
            case "windowskey":      return 91;
            case "leftwindowskey":  return 91;
            case "rightwindowskey": return 92;
            case "selectkey":       return 93;
            case "select":          return 93;
            case "numpad0":         return 96;
            case "numpad1":         return 97;
            case "numpad2":         return 98;
            case "numpad3":         return 99;
            case "numpad4":         return 100;
            case "numpad5":         return 101;
            case "numpad6":         return 102;
            case "numpad7":         return 103;
            case "numpad8":         return 104;
            case "numpad9":         return 105;
            case "num0":            return 96;
            case "num1":            return 97;
            case "num2":            return 98;
            case "num3":            return 99;
            case "num4":            return 100;
            case "num5":            return 101;
            case "num6":            return 102;
            case "num7":            return 103;
            case "num8":            return 104;
            case "num9":            return 105;
            case "multiply":        return 106;
            case "add":             return 107;
            case "subtract":        return 109;
            case "decimalpoint":    return 110;
            case "divide":          return 111;
            case "*":               return 106;
            case "+":               return 107;
            case "-":               return 109;
            case "num.":            return 110;
            case "numdot":          return 110;
            case "/":               return 111;
            case "f1":              return 112;
            case "f2":              return 113;
            case "f3":              return 114;
            case "f4":              return 115;
            case "f5":              return 116;
            case "f6":              return 117;
            case "f7":              return 118;
            case "f8":              return 119;
            case "f9":              return 120;
            case "f10":             return 121;
            case "f11":             return 122;
            case "f12":             return 123;
            case "numlock":         return 144;
            case "scrolllock":      return 145;
            case "semi-colon":      return 186;
            case "semicolon":       return 186;
            case ":":               return 186;
            case "equalsign":       return 187;
            case "equal":           return 187;
            case "=":               return 187;
            case "comma":           return 188;
            case ",":               return 188;
            case "dash":            return 189;
            case ".":               return 190;
            case "forwardslash":    return 191;
            case "graveaccent":     return 192;
            case "`":               return 192;
            case "openbracket":     return 219;
            case "[":               return 219;
            case "backslash":       return 220;
            case "\\":              return 220;
            case "closebraket":     return 221;
            case "]":               return 221;
            case "singlequote":     return 222;
            case "'":               return 222;
            default:                return 0;
        }
    }
}