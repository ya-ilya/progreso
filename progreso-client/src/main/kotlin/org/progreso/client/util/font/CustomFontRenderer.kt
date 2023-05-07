package org.progreso.client.util.font

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.DynamicTexture
import org.lwjgl.opengl.GL11
import java.awt.Font
import java.util.*
import java.util.stream.Collectors

@Suppress("unused")
class CustomFontRenderer(
    font: Font,
    antiAlias: Boolean,
    fractionalMetrics: Boolean
) : CustomFont(font, antiAlias, fractionalMetrics) {
    private var boldChars = arrayOfNulls<CharData>(256)
    private var italicChars = arrayOfNulls<CharData>(256)
    private var boldItalicChars = arrayOfNulls<CharData>(256)
    private var texBold: DynamicTexture? = null
    private var texItalic: DynamicTexture? = null
    private var texBoth: DynamicTexture? = null
    private val colorCode = IntArray(32)
    private var shadow = false

    init {
        setupMinecraftColorCodes()
        setupBoldItalicIDs()
    }

    fun drawStringWithShadow(
        text: String,
        x: Double,
        y: Double,
        color: Int
    ): Float {
        val shadowWidth = drawString(text, x + 1.0, y + 1.0, color, true)
        return Math.max(shadowWidth, drawString(text, x, y, color, false))
    }

    fun drawString(text: String, x: Float, y: Float, color: Int): Float {
        return drawString(text, x.toDouble(), y.toDouble(), color, false)
    }

    private fun drawString(
        text: String,
        xInit: Double,
        yInit: Double,
        colorInit: Int,
        shadow: Boolean
    ): Float {
        var x = xInit
        var y = yInit
        var color = colorInit
        x -= 1.0
        if (shadow) {
            if (this.shadow) {
                x -= 0.4
                y -= 0.4
            }
            color = color and 0xFCFCFC shr 2 or (color and -0x1000000)
        }
        var currentData = charData
        val alpha = (color shr 24 and 0xFF) / 255.0f
        var random = false
        var bold = false
        var italic = false
        var strike = false
        var underline = false
        x *= 2.0
        y = (y - 3.0) * 2.0
        GL11.glPushMatrix()
        GlStateManager.scale(0.5, 0.5, 0.5)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(770, 771)
        GlStateManager.color(
            (color shr 16 and 0xFF) / 255.0f,
            (color shr 8 and 0xFF) / 255.0f,
            (color and 0xFF) / 255.0f,
            alpha
        )
        GlStateManager.enableTexture2D()
        GlStateManager.bindTexture(tex!!.glTextureId)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex!!.glTextureId)
        var i = 0
        while (i < text.length) {
            var character = text[i]
            if (character == '\u00a7' && i + 1 < text.length) {
                var colorIndex = COLOR_CODES.indexOf(text[i + 1])
                if (colorIndex < 16) {
                    bold = false
                    italic = false
                    random = false
                    underline = false
                    strike = false
                    GlStateManager.bindTexture(tex!!.glTextureId)
                    currentData = charData
                    if (colorIndex < 0) {
                        colorIndex = 15
                    }
                    if (shadow) {
                        colorIndex += 16
                    }
                    val colorCode = colorCode[colorIndex]
                    GlStateManager.color(
                        (colorCode shr 16 and 0xFF) / 255.0f,
                        (colorCode shr 8 and 0xFF) / 255.0f,
                        (colorCode and 0xFF) / 255.0f,
                        alpha
                    )
                } else if (colorIndex == 16) {
                    random = true
                } else if (colorIndex == 17) {
                    bold = true
                    currentData = if (italic) {
                        GlStateManager.bindTexture(texBoth!!.glTextureId)
                        boldItalicChars
                    } else {
                        GlStateManager.bindTexture(texBold!!.glTextureId)
                        boldChars
                    }
                } else if (colorIndex == 18) {
                    strike = true
                } else if (colorIndex == 19) {
                    underline = true
                } else if (colorIndex == 20) {
                    italic = true
                    currentData = if (bold) {
                        GlStateManager.bindTexture(texBoth!!.glTextureId)
                        boldItalicChars
                    } else {
                        GlStateManager.bindTexture(texItalic!!.glTextureId)
                        italicChars
                    }
                } else if (colorIndex == 21) {
                    bold = false
                    italic = false
                    random = false
                    underline = false
                    strike = false
                    GlStateManager.color(
                        (color shr 16 and 0xFF) / 255.0f,
                        (color shr 8 and 0xFF) / 255.0f,
                        (color and 0xFF) / 255.0f,
                        alpha
                    )
                    GlStateManager.bindTexture(tex!!.glTextureId)
                    currentData = charData
                } else if (colorIndex == 22) {
                    bold = false
                    italic = false
                    random = false
                    underline = false
                    strike = false
                    GlStateManager.bindTexture(tex!!.glTextureId)
                    currentData = charData
                    val h = CharArray(8)
                    if (i + 9 < text.length) {
                        for (j in 0..7) {
                            h[j] = text[i + j + 2]
                        }
                    } else {
                        i++
                        i++
                        continue
                    }
                    val colorCode: Int = try {
                        String(h).toLong(16).toInt()
                    } catch (e: Exception) {
                        i++
                        continue
                    }
                    GlStateManager.color(
                        (colorCode shr 16 and 0xFF) / 255.0f
                            / if (shadow) 4 else 1,
                        (colorCode shr 8 and 0xFF) / 255.0f
                            / if (shadow) 4 else 1,
                        (colorCode and 0xFF) / 255.0f
                            / if (shadow) 4 else 1,
                        (colorCode shr 24 and 0xFF) / 255.0f
                    )
                    i += 9
                    i++
                    continue
                }
                i++
            } else if (character.code < currentData.size) {
                if (random) {
                    val w = currentData[character.code]!!.width
                    val finalCurrentData = currentData
                    val randoms = RANDOM_CHARS
                        .stream()
                        .filter { c: Char ->
                            if (c.code < finalCurrentData.size) {
                                return@filter finalCurrentData[c.code]!!.width == w
                            }
                            false
                        }.collect(Collectors.toList())
                    if (randoms.size != 0) {
                        character = randoms[CHAR_RANDOM.nextInt(randoms.size)]
                    }
                }
                GL11.glBegin(GL11.GL_TRIANGLES)
                drawChar(currentData.filterNotNull().toTypedArray(), character, x.toFloat(), y.toFloat())
                GL11.glEnd()
                if (strike) {
                    drawLine(
                        x,
                        y + currentData[character.code]!!.height / 2.0,
                        x + currentData[character.code]!!.width - 8.0,
                        y + currentData[character.code]!!.height / 2.0
                    )
                }
                if (underline) {
                    drawLine(
                        x,
                        y + currentData[character.code]!!.height - 2.0,
                        x + currentData[character.code]!!.width - 8.0,
                        y + currentData[character.code]!!.height - 2.0
                    )
                }
                x += (currentData[character.code]!!.width - 8 + charOffset).toDouble()
            }
            i++
        }
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE)
        GL11.glPopMatrix()
        return (x / 2.0f).toFloat()
    }

    override fun getStringWidth(text: String): Int {
        var currentData = charData
        var width = 0
        var bold = false
        var italic = false
        var i = 0
        while (i < text.length) {
            val character = text[i]
            if (character == '\u00a7' && i + 1 < text.length) {
                val colorIndex = COLOR_CODES.indexOf(text[i + 1])
                if (colorIndex < 16) {
                    bold = false
                    italic = false
                } else if (colorIndex == 17) {
                    bold = true
                    currentData = if (italic) {
                        boldItalicChars
                    } else {
                        boldChars
                    }
                } else if (colorIndex == 20) {
                    italic = true
                    currentData = if (bold) {
                        boldItalicChars
                    } else {
                        italicChars
                    }
                } else if (colorIndex == 21) {
                    bold = false
                    italic = false
                    currentData = charData
                } else if (colorIndex == 22) {
                    bold = false
                    italic = false
                    currentData = charData
                    i += 9
                    i++
                    continue
                } else if (colorIndex == 23) {
                    bold = false
                    italic = false
                } else if (colorIndex == 24) {
                    bold = false
                    italic = false
                } else {
                    bold = false
                    italic = false
                }
                i++
            } else if (character.code < currentData.size) {
                width += currentData[character.code]!!.width - 8 + charOffset
            }
            i++
        }
        return width / 2
    }

    override var font: Font
        get() = super.font
        set(font) {
            super.font = font
            setupBoldItalicIDs()
        }
    override var antiAlias: Boolean
        get() = super.antiAlias
        set(antiAlias) {
            super.antiAlias = antiAlias
            setupBoldItalicIDs()
        }
    override var fractionalMetrics: Boolean
        get() = super.fractionalMetrics
        set(fractionalMetrics) {
            super.fractionalMetrics = fractionalMetrics
            setupBoldItalicIDs()
        }

    private fun setupBoldItalicIDs() {
        texBold = setupTexture(
            font.deriveFont(Font.BOLD),
            antiAlias,
            fractionalMetrics,
            boldChars
        )
        texItalic = setupTexture(
            font.deriveFont(Font.ITALIC),
            antiAlias,
            fractionalMetrics,
            italicChars
        )
        texBoth = setupTexture(
            font.deriveFont(Font.BOLD or Font.ITALIC),
            antiAlias,
            fractionalMetrics,
            boldItalicChars
        )
    }

    private fun drawLine(
        x: Double,
        y: Double,
        x1: Double,
        y1: Double
    ) {
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glLineWidth(1.0f)
        GL11.glBegin(GL11.GL_LINES)
        GL11.glVertex2d(x, y)
        GL11.glVertex2d(x1, y1)
        GL11.glEnd()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
    }

    fun wrapWords(text: String, width: Double): List<String> {
        val result: MutableList<String> = ArrayList()
        if (getStringWidth(text) > width) {
            val words = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var current = StringBuilder()
            var lastColorCode = 65535.toChar()
            for (word in words) {
                val array = word.toCharArray()
                for (i in array.indices) {
                    val c = array[i]
                    if (c == '\u00a7' && i + 1 < array.size) {
                        lastColorCode = array[i + 1]
                    }
                }
                if (getStringWidth("$current$word ") < width) { // ???
                    current.append(word).append(" ")
                } else {
                    result.add(current.toString())
                    current = StringBuilder("\u00a7")
                        .append(lastColorCode)
                        .append(word)
                        .append(" ")
                }
            }
            if (current.isNotEmpty()) {
                if (getStringWidth(current.toString()) < width) {
                    result.add(
                        "\u00a7"
                            + lastColorCode
                            + current
                            + " "
                    )
                } else {
                    result.addAll(formatString(current.toString(), width))
                }
            }
        } else {
            result.add(text)
        }
        return result
    }

    private fun formatString(string: String, width: Double): List<String> {
        val result: MutableList<String> = ArrayList()
        var current = StringBuilder()
        var lastColorCode = 65535.toChar()
        val chars = string.toCharArray()
        for (i in chars.indices) {
            val c = chars[i]
            if (c == '\u00a7' && i < chars.size - 1) {
                lastColorCode = chars[i + 1]
            }
            if (getStringWidth(current.toString() + c) < width) {
                current.append(c)
            } else {
                result.add(current.toString())
                current = StringBuilder("\u00a7")
                    .append(lastColorCode)
                    .append(c)
            }
        }
        if (current.isNotEmpty()) {
            result.add(current.toString())
        }
        return result
    }

    private fun setupMinecraftColorCodes() {
        for (i in 0..31) {
            val o = (i shr 3 and 0x1) * 85
            var r = (i shr 2 and 0x1) * 170 + o
            var g = (i shr 1 and 0x1) * 170 + o
            var b = (i and 0x1) * 170 + o
            if (i == 6) {
                r += 85
            }
            if (i >= 16) {
                r /= 4
                g /= 4
                b /= 4
            }
            colorCode[i] = r and 0xFF shl 16 or (g and 0xFF shl 8) or (b and 0xFF)
        }
    }

    companion object {
        private const val COLOR_CODES = "0123456789abcdefklmnorzy+-"
        private val CHAR_RANDOM = Random()
        private val RANDOM_CHARS: List<Char> = ArrayList(
            ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da" +
                "\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174" +
                "\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$" +
                "%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcde" +
                "fghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0" +
                "\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9" +
                "\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8" +
                "\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa" +
                "\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592" +
                "\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d" +
                "\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f" +
                "\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565" +
                "\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c" +
                "\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6" +
                "\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264" +
                "\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0" +
                "\u0000").chars().mapToObj { c: Int -> c.toChar() }.collect(Collectors.toList())
        )
    }
}