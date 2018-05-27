package beginner.wakaba.akashi33;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button sw;
    String s;
    ImageView iv;
    int mode = 0;
    final int MULTITOUCH = 0;
    final int PINCHZOOM = 1;
    int id,index;
    View.OnClickListener cl;
    float d1 = 0;
    float d2;
    float dtemp = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        sw = findViewById(R.id.sw);
        iv = findViewById(R.id.wakaba);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode){
                    case MULTITOUCH :
                        mode = PINCHZOOM;
                        setTitle("와카바");
                        break;
                    case PINCHZOOM :
                        mode = MULTITOUCH;
                        setTitle("아카시");
                        break;
                }
            }
        };
        sw.setOnClickListener(cl);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action;
        action = event.getAction() & 0xff;
        index = event.getAction() >>8;


        switch (mode){
            case MULTITOUCH :
                switch (action){
                case MotionEvent.ACTION_DOWN :
                    s = String.format("x=%3.0f y=%3.0f i=%2d id=%2d DOWN \n",event.getX(index),event.getY(index),index,event.getPointerId(index));
                    break;
                case MotionEvent.ACTION_UP :
                    s = s+String.format("x=%3.0f y=%3.0f i=%2d id=%2d UP \n",event.getX(index),event.getY(index),index,event.getPointerId(index));
                    break;
                case MotionEvent.ACTION_POINTER_DOWN :
                    s = s+String.format("x=%3.0f y=%3.0f i=%2d id=%2d PTDOWN \n",event.getX(index),event.getY(index),index,event.getPointerId(index));
                    break;
                case MotionEvent.ACTION_POINTER_UP :
                    s = s+String.format("x=%3.0f y=%3.0f i=%2d id=%2d PTUP \n",event.getX(index),event.getY(index),index,event.getPointerId(index));
                    break;
            }
            tv.setText(s);
            break;

            case PINCHZOOM :
                Matrix m;
                m = new Matrix();
                float r;

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        s = String.format("dtemp=%4.4f ",dtemp);
                        m.postScale(dtemp,dtemp);
                        iv.setImageMatrix(m);
                        break;
                }

                if(event.getPointerCount()==2){
                    switch (action){
                        case MotionEvent.ACTION_POINTER_DOWN :
                            d1 = getDistance(event);
                            s = s+String.format("d1=%2.0f ",d1);
                            break;
                        case MotionEvent.ACTION_MOVE :
                            d2 = getDistance(event);
                            s = String.format("d2=%2.0f ",d2);
                            r = (d2/d1)*dtemp;
                            if(r>10){r=10;}
                            else if(r<0.2){r = 0.2f;}
                            s = String.format("dtemp=%4.3f d1=%4.3f d2=%4.3f r=%4.3f ",dtemp,d1,d2,r);

                            m.postScale(r,r);
                            iv.setImageMatrix(m);
                            break;
                        case MotionEvent.ACTION_POINTER_UP :
                            d2 = getDistance(event);
                            r = (d2/d1)*dtemp;
                            if(r>10){r=10;}
                            else if(r<0.2f){r = 0.2f;}
                            dtemp = r;
                            s = s+String.format("dt=%4.4f\n",dtemp);
                            break;
                    }
                }
                tv.setText(s);
                break;
        }
















        return true;
    }
    float getDistance(MotionEvent e){
        float dx = e.getX(1)-e.getX(0);
        float dy = e.getY(1)-e.getY(0);
        return (float) Math.sqrt((dx*dx)+(dy*dy));
    }
}
