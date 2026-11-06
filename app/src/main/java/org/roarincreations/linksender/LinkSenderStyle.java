package org.roarincreations.linksender;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/** Soft sage-green palette with black labels, explicitly readable on older Android UI. */
final class LinkSenderStyle {
    private LinkSenderStyle() {}
    static final int GREEN = Color.rgb(121, 166, 132);    // #79A684 app background
    static final int CARD = Color.rgb(220, 236, 222);     // #DCECDE pale sage pale information panels
    static final int EDITOR = Color.rgb(242, 248, 240);   // #F2F8F0 input/select panels
    static final int BUTTON = Color.rgb(227, 240, 226);   // #E3F0E2 buttons
    static final int FOCUS = Color.rgb(255, 247, 195);    // #FFF7C3 obvious D-pad focus
    static final int PRESSED = Color.rgb(184, 214, 185);  // #B8D6B9
    static final int BORDER = Color.rgb(69, 115, 79);    // #45734F outlines
    static final int INK = Color.BLACK;
    static final int HINT = Color.rgb(68, 76, 69);

    static GradientDrawable surface(int color) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(12f);
        bg.setStroke(2, BORDER);
        return bg;
    }
    /** Yellow follows hardware focus; default remains the owner-liked pale sage. */
    static StateListDrawable spinnerBackground() {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_focused}, surface(FOCUS));
        bg.addState(new int[]{android.R.attr.state_pressed}, surface(PRESSED));
        bg.addState(new int[]{}, surface(EDITOR));
        return bg;
    }

    /** Selected item has a sage base; the moving D-pad row is pale yellow. */
    static StateListDrawable dropdownRowBackground(boolean committed) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_selected}, surface(FOCUS));
        bg.addState(new int[]{android.R.attr.state_focused}, surface(FOCUS));
        bg.addState(new int[]{android.R.attr.state_activated}, surface(FOCUS));
        bg.addState(new int[]{android.R.attr.state_pressed}, surface(PRESSED));
        bg.addState(new int[]{}, surface(committed ? BUTTON : CARD));
        return bg;
    }

    static StateListDrawable buttonBackground() {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_focused}, surface(FOCUS));
        bg.addState(new int[]{android.R.attr.state_pressed}, surface(PRESSED));
        bg.addState(new int[]{}, surface(BUTTON));
        return bg;
    }
}
