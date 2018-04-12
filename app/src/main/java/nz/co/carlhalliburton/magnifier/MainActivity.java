package nz.co.carlhalliburton.magnifier;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private Boolean isTorchOn;
    private String mCameraId;

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
            if (setIsFlashAvailable()) {
                toggleFlash();
                return true;
            }
        }
        else if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_camera) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isTorchOn){
            toggleLightOff();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTorchOn){
            toggleLightOff();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTorchOn){
            toggleLightOn();
        }
    }

    //Manage led flash
//---------------------------------------------------------------------------------------------
    public Boolean setIsFlashAvailable() {
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            Toast.makeText(getApplicationContext(), "Flash no available", Toast.LENGTH_LONG).show();
            return false;
            }
            else
                return true;
    }

    public void toggleFlash() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        try {
            if (isTorchOn) {
                isTorchOn = false;
                Toast.makeText(MainActivity.this, "light off", Toast.LENGTH_SHORT).show();
                toggleLightOff();
            } else {
                isTorchOn = true;
                Toast.makeText(MainActivity.this, "light on", Toast.LENGTH_SHORT).show();
                toggleLightOn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleLightOn() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleLightOff() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
