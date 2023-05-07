package org.progreso.client.util.font

import net.minecraft.client.renderer.texture.DynamicTexture
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

open class CustomFont(
    font: Font,
    antiAlias: Boolean,
    fractionalMetrics: Boolean
) {
    protected open var font: Font = font
        set(value) {
            field = value
            tex = setupTexture(
                font,
                antiAlias,
                fractionalMetrics,
                charData
            )
        }

    protected open var antiAlias: Boolean = antiAlias
        set(value) {
            if (field != value) {
                field = value
                tex = setupTexture(
                    font,
                    antiAlias,
                    fractionalMetrics,
                    charData
                )
            }
        }

    protected open var fractionalMetrics: Boolean = fractionalMetrics
        set(value) {
            if (field != value) {
                field = value
                tex = setupTexture(
                    font,
                    antiAlias,
                    fractionalMetrics,
                    charData
                )
            }
        }

    protected var charData = arrayOfNulls<CharData>(256)
    protected var fontHeight = -1
    protected var charOffset = 0
    protected var tex: DynamicTexture?

    init {
        tex = setupTexture(font, antiAlias, fractionalMetrics, charData)
    }

    protected fun setupTexture(
        font: Font?,
        antiAlias: Boolean,
        fractionalMetrics: Boolean,
        chars: Array<CharData?>
    ): DynamicTexture? {
        val img = generateFontImage(font, antiAlias, fractionalMetrics, chars)
        try {
            return DynamicTexture(img)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun generateFontImage(
        font: Font?,
        antiAlias: Boolean,
        fractionalMetrics: Boolean,
        chars: Array<CharData?>
    ): BufferedImage {
        val bufferedImage = BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_ARGB)
        val g = bufferedImage.graphics as Graphics2D
        g.font = font
        g.color = Color(255, 255, 255, 0)
        g.fillRect(0, 0, IMG_SIZE, IMG_SIZE)
        g.color = Color.WHITE
        g.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS,
            if (fractionalMetrics) RenderingHints.VALUE_FRACTIONALMETRICS_ON else RenderingHints.VALUE_FRACTIONALMETRICS_OFF
        )
        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            if (antiAlias) RenderingHints.VALUE_TEXT_ANTIALIAS_ON else RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        )
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            if (antiAlias) RenderingHints.VALUE_ANTIALIAS_ON else RenderingHints.VALUE_ANTIALIAS_OFF
        )
        val fontMetrics = g.fontMetrics
        var charHeight = 0
        var positionX = 0
        var positionY = 1
        for (i in chars.indices) {
            val ch = i.toChar()
            val charData = CharData()
            val dimensions = fontMetrics.getStringBounds(ch.toString(), g)
            charData.width = dimensions.bounds.width + 8
            charData.height = dimensions.bounds.height
            if (positionX + charData.width >= IMG_SIZE) {
                positionX = 0
                positionY += charHeight
                charHeight = 0
            }
            if (charData.height > charHeight) {
                charHeight = charData.height
            }
            charData.storedX = positionX
            charData.storedY = positionY
            if (charData.height > fontHeight) {
                fontHeight = charData.height
            }
            chars[i] = charData
            g.drawString(
                ch.toString(),
                positionX + 2,
                positionY + fontMetrics.ascent
            )
            positionX += charData.width
        }
        return bufferedImage
    }

    fun drawChar(chars: Array<CharData>, c: Char, x: Float, y: Float) {
        try {
            drawQuad(
                x,
                y,
                chars[c.code].width.toFloat(),
                chars[c.code].height.toFloat(),
                chars[c.code].storedX.toFloat(),
                chars[c.code].storedY.toFloat(),
                chars[c.code].width.toFloat(),
                chars[c.code].height.toFloat()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawQuad(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        srcX: Float,
        srcY: Float,
        srcWidth: Float,
        srcHeight: Float
    ) {
        val rSRCX = srcX / IMG_SIZE
        val rSRCY = srcY / IMG_SIZE
        val rSRCW = srcWidth / IMG_SIZE
        val rSRCH = srcHeight / IMG_SIZE
        GL11.glTexCoord2f(rSRCX + rSRCW, rSRCY)
        GL11.glVertex2d((x + width).toDouble(), y.toDouble())
        GL11.glTexCoord2f(rSRCX, rSRCY)
        GL11.glVertex2d(x.toDouble(), y.toDouble())
        GL11.glTexCoord2f(rSRCX, rSRCY + rSRCH)
        GL11.glVertex2d(x.toDouble(), (y + height).toDouble())
        GL11.glTexCoord2f(rSRCX, rSRCY + rSRCH)
        GL11.glVertex2d(x.toDouble(), (y + height).toDouble())
        GL11.glTexCoord2f(rSRCX + rSRCW, rSRCY + rSRCH)
        GL11.glVertex2d((x + width).toDouble(), (y + height).toDouble())
        GL11.glTexCoord2f(rSRCX + rSRCW, rSRCY)
        GL11.glVertex2d((x + width).toDouble(), y.toDouble())
    }

    val height: Int
        get() = (fontHeight - 10) / 2

    open fun getStringWidth(text: String): Int {
        var width = 0
        for (c in text.toCharArray()) {
            if (c.code < charData.size) {
                width += charData[c.code]!!.width - 8 + charOffset
            }
        }
        return width / 2
    }

    companion object {
        private const val IMG_SIZE = 512
    }
}