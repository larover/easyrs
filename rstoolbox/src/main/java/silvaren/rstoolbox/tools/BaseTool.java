package silvaren.rstoolbox.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;

public class BaseTool<T> {

    protected BaseTool(BaseToolScript tool) {
        this.tool = tool;
    }

    interface BaseToolScript<T> {
        void runScript(RSToolboxContext rsToolboxContext, Allocation aout, T scriptParams);
    }

    private final BaseToolScript tool;

    protected Bitmap doComputation(Context context, Bitmap inputBitmap, T scriptParams) {
        Bitmap.Config config = inputBitmap.getConfig();
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(),
                config);
        return doComputation(context, inputBitmap, outputBitmap, scriptParams);
    }

    private Bitmap doComputation(Context context, Bitmap inputBitmap, Bitmap outputBitmap, T scriptParams) {

        RSToolboxContext rsToolboxContext = RSToolboxContext.createFromBitmap(context, inputBitmap);

        Allocation aout = Allocation.createTyped(rsToolboxContext.rs, rsToolboxContext.ain.getType());

        this.tool.runScript(rsToolboxContext, aout, scriptParams);

        aout.copyTo(outputBitmap);

        return outputBitmap;
    }

    protected byte[] doComputation(Context context, byte[] nv21ByteArray, int width,
                                              int height, T scriptParams) {
        byte[] outputNv21ByteArray = new byte[nv21ByteArray.length];
        return doComputation(context, nv21ByteArray, width, height, outputNv21ByteArray,
                scriptParams);
    }

    protected byte[] doComputation(Context context, byte[] nv21ByteArray, int width, int height,
                                   byte[] outputNv21ByteArray, T scriptParams) {
        RSToolboxContext rsToolboxContext = RSToolboxContext.createFromNv21Image(context,
                nv21ByteArray, width, height);

        Allocation aout = Allocation.createTyped(rsToolboxContext.rs, rsToolboxContext.ain.getType());

        this.tool.runScript(rsToolboxContext, aout, scriptParams);

        aout.copyTo(outputNv21ByteArray);

        return outputNv21ByteArray;
    }
}