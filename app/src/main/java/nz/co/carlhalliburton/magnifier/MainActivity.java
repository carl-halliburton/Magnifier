package nz.co.carlhalliburton.magnifier;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;
    private Boolean isTorchOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        isTorchOn = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_light) {
            setIsFlashAvailable();
            toggleFlash();
            return true;
        }
        else if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_camera) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Manage led flash
    //---------------------------------------------------------------------------------------------
    public void setIsFlashAvailable() {
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable)
            Toast.makeText(getApplicationContext(), "Flash no available", Toast.LENGTH_LONG).show();
    }

    public void toggleFlash() {
        View v = findViewById(android.R.id.content);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        try {
            if (isTorchOn) {
                toggleLightOn(v);
                isTorchOn = false;
            } else {
                toggleLightOff(v);
                isTorchOn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleLightOn(View v) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
                //mTorchOnOffButton.setImageResource(R.drawable.on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleLightOff(View v) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                //mTorchOnOffButton.setImageResource(R.drawable.off);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
