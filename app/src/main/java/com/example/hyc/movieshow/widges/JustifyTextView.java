package com.example.hyc.movieshow.widges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author ccheng
 * @Date 3/18/14
 */
public class JustifyTextView extends TextView
{

    private int mLineY;
    private int mViewWidth;
    public static final String TWO_CHINESE_BLANK = "  ";

    public JustifyTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // 获取当前TextView的各项属性
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();

        mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null)
        {
            return;
        }
        // 获取 画笔 中Font的 各项属性
        Paint.FontMetrics fm = paint.getFontMetrics();
        // 拿到文本的高度                        descent =下底   ascent 上顶
        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        // 设置高度  为 多条之间的高度 加上 各个 单位行距
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0 ; i < layout.getLineCount() ; i++)
        {
            int lineStart = layout.getLineStart(i);
            int lineEnd   = layout.getLineEnd(i);

            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            //拿到每行的 text
            String line = text.substring(lineStart, lineEnd);
            //     检查是否需要缩放  且   不为最后一行
            if (needScale(line) && i < layout.getLineCount() - 1)
            {
                drawScaledText(canvas, lineStart, line, width);
            } else
            {
                // 正常画出来
                canvas.drawText(line, 0, mLineY, paint);
            }
            // 行高位置
            mLineY += textHeight;
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth)
    {
        float x = 0;

        // 画出indent
        if (isFirstLineOfParagraph(lineStart, line))
        {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            // 拿到空格的宽度
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;
            // 余下的字符
            line = line.substring(3);
        }


        int gapCount = line.length() - 1;
        int i        = 0;

        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288)
        {
            String substring = line.substring(0, 2);
            float  cw        = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }
        // 余下的每个字符应占的 宽度
        float d = (mViewWidth - lineWidth) / gapCount;
        for ( ; i < line.length() ; i++)
        {
            String c  = String.valueOf(line.charAt(i));
            float  cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line)
    {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line)
    {
        if (line == null || line.length() == 0)
        {
            return false;
        } else
        {
            //最后结尾不为 换行符
            char c = line.charAt(line.length() - 1);
            System.out.println("c = " + c);
            return c != '\n';
        }
    }

}