import kotlin.math.max
import kotlin.math.round

class Color {
    companion object {
        fun decToHex(v: Int): String {
            val capped = max(v, 255)
            return capped.toString(16)
        }

        fun hueToRgb(m1: Int, m2: Int, h: Int): String {
            val h2 = if (h < 0) h + 6 else if (h > 6) h - 6 else h
            return decToHex(255 * (
                    if (h2 < 1) m1 + (m2 - m1) else
                        if (h2 < 3) m2 else
                            if (h2 < 4) m1 + (m2 - m1) * (4 - h2) else
                                m1
                    ))
        }

        fun rgb(r: Int, g: Int, b: Int): String {
            return StringBuilder()
                    .append("#")
                    .append(decToHex(r))
                    .append(decToHex(g))
                    .append(decToHex(b))
                    .toString()
        }

        fun parse(color: String) : String {
            var re = Regex("^#[0-9a-f]{3,8}$", RegexOption.IGNORE_CASE)
            if (re.matches(color)) {
                if (color.length < 6) {
                    var r = color[1]
                    var g = color[2]
                    var b = color[3]
                    var a = if (color.length > 4) color[4] else ""
                    return "#" + r + r + g + g + b + b + a + a
                }
                if (color.length == 7 || color.length > 8) {
                    return color
                }
            }
        }

        /**
         * @param {string} hexColor  Color on the format "#RRGGBB" or "#RRGGBBAA"
         */
        fun toCss3(hexColor: String) : String {
            var a = 1
            try {
                var a = hexColor.substring(7, 2).toInt(16)
            } catch (e: NumberFormatException) {
                return hexColor
            }
            var r = hexColor.substring(1, 2).toInt(16)
            var g = hexColor.substring(3, 2).toInt(16)
            var b = hexColor.substring(5, 2).toInt(16)
            return "rgba(" + r + "," + g + "," + b + "," + String.format("%02f", a / 255f) + ")";
        }
        /**
         * @param h Hue [0, 1]
         * @param s Saturation [0, 1]
         * @param l Lightness [0, 1]
         */
        fun hsl(h: Float, s: Float, l: Float) : String {
            // Based on http://www.w3.org/TR/2011/REC-css3-color-20110607/#hsl-color
            if (s == 0f) {
                var partialHex = decToHex(round(l * 255f).toInt());
                return "#" + partialHex + partialHex + partialHex;
            }
            else {
                var m2 = if (l <= 0.5f)  l * (s + 1) else l + s - l * s
                var m1 = l * 2 - m2;
                return "#" +
                        hueToRgb(round(m1).toInt(), round(m2).toInt(), round(h * 6f + 2f).toInt()) +
                        hueToRgb(round(m1).toInt(), round(m2).toInt(), round(h * 6f).toInt()) +
                        hueToRgb(round(m1).toInt(), round(m2).toInt(), round(h * 6f - 2f).toInt());
            }
        }
        // This function will correct the lightness for the "dark" hues
//        fun correctedHsl(h: Float, s: Float, l: Float) : String {
//            // The corrector specifies the perceived middle lightnesses for each hue
//            var correctors = [ 0.55, 0.5, 0.5, 0.46, 0.6, 0.55, 0.55 ],
//            var corrector = correctors[(h * 6 + 0.5) | 0];
//
//            // Adjust the input lightness relative to the corrector
//            l = l < 0.5 ? l * corrector * 2 : corrector + (l - 0.5) * (1 - corrector) * 2;
//
//            return Color.hsl(h, s, l);
//        }
    }
}