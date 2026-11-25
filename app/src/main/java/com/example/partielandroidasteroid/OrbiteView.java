package com.example.partielandroidasteroid;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class OrbiteView extends View {

    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private double distance;
    private double periode = 365;
    private int referentiel = 0;

    private float angleT = 0;
    private float angleA = 225;

    public OrbiteView(Context c, AttributeSet a) {
        super(c, a);
    }

    public void setReferentiel(int referentiel) {
        this.referentiel = referentiel;
        invalidate();
    }

    public void setPeriode(double periode) {
        this.periode = periode;
        invalidate();
    }

    public void setDistance(double distance) {
        this.distance = distance;
        invalidate();
    }

    public void animer() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 360f);
        anim.setDuration(10000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setInterpolator(new LinearInterpolator());

        anim.addUpdateListener(a -> {
            angleT = (float) a.getAnimatedValue();

            angleA = 225 + (float)
                    (a.getCurrentPlayTime() * 360.0
                            / (10000 * periode / 365));

            invalidate();
        });

        anim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float r = getWidth() * 0.4f;
        float d = (float) (distance / 149597870 * r);

        double angleTerre = Math.toRadians(angleT);
        double angleAstroides = Math.toRadians(angleA);

        float xTerre = (float) (r * Math.cos(angleTerre));
        float yTerre = (float) (r * Math.sin(angleTerre));

        float xAstroides = xTerre + (float)
                (d * Math.cos(angleAstroides));

        float yAstroides = yTerre + (float)
                (d * Math.sin(angleAstroides));

        canvas.save();
        canvas.translate(getWidth() / 2f, getHeight() / 2f);

        if (referentiel != 0) {
            float echelle = r / (r + d + Math.max(r, d));
            canvas.scale(echelle, echelle);
        }

        if (referentiel == 1) {
            canvas.translate(-xTerre, -yTerre);
        } else if (referentiel == 2) {
            canvas.translate(-xAstroides, -yAstroides);
        }

        p.setColor(Color.LTGRAY);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, r, p);
        canvas.drawCircle(xTerre, yTerre, d, p);

        p.setStyle(Paint.Style.FILL);

        p.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, r / 7, p);

        p.setColor(Color.CYAN);
        canvas.drawCircle(xTerre, yTerre, r / 14, p);

        p.setColor(Color.RED);
        canvas.drawCircle(xAstroides, yAstroides, r / 28, p);

        canvas.restore();
    }
}