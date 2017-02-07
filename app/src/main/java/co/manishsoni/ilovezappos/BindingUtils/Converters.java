package co.manishsoni.ilovezappos.BindingUtils;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by manis on 2/6/2017.
 */

public class Converters {
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
