package com.todayweather.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private Context mContext;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private int cameraPosition = 1;//0代表后置摄像头，1代表前置摄像头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button btn2 = (Button) findViewById(R.id.button2);
        String data = getIntent().getStringExtra("extra_data");
        mContext = this;

        initSurfaceView();
        Log.i(TAG, "intent: " + data);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i(TAG, "onClick: 第二个页面别点击");
//                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
//                Intent intent = new Intent();
//                intent.putExtra("data_return", "哥哥我又回来了");
//                setResult(RESULT_OK, intent);
//                finish();

                //切换摄像头
                change();
            }
        });
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 1;//请求码，自己定义

    private void initSurfaceView() {

        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)

        {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
        }else{
            //有授权，直接开启摄像头
            boolean device = checkCameraHardware(mContext);
            if (device) {
                //相机是否可用
                Camera cameraInstance = getCameraInstance();
                if (cameraInstance != null) {
                    //相机可用
                    //创建相机的预览
                    previewCamera();
                }
            }
        }






    }

    /**
     * 创建相机的简单预览
     */
    private void previewCamera() {
        SurfaceView sfview = (SurfaceView) findViewById(R.id.surfaceview);
        mHolder = sfview.getHolder();

        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    mCamera.setPreviewDisplay(surfaceHolder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    Log.d(TAG, "Error setting camera preview: " + e.getMessage());
                }
                mCamera.setDisplayOrientation(90);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                // If your preview can change or rotate, take care of those events here.
                // Make sure to stop the preview before resizing or reformatting it.

                if (mHolder.getSurface() == null) {
                    // preview surface does not exist
                    return;
                }

                // stop preview before making changes
                try {
                    mCamera.stopPreview();
                } catch (Exception e) {
                    // ignore: tried to stop a non-existent preview
                }

                // set preview size and make any resize, rotate or
                // reformatting changes here

                // start preview with new settings
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();

                } catch (Exception e) {
                    Log.d(TAG, "Error starting camera preview: " + e.getMessage());
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                releaseCamera();
            }
        });

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    /**
     * Check if this device has a camera
     * 检测该设备是否有摄像头
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.i(TAG, "checkCameraHardware: 设备摄像头数目：" + Camera.getNumberOfCameras());
            // 设备支持摄像头
            return true;
        } else {
            // 设备不支持
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     * 获取相机对象的安全方法
     */
    public Camera getCameraInstance() {
        try {
            //0代表开启后置摄像头 1代表开启前置摄像头
            mCamera = Camera.open(cameraPosition); // attempt to get a Camera instance
            Log.i(TAG, "getCameraInstance: 相机状态：Camera is  available(ok)");

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.i(TAG, "getCameraInstance: 相机状态：Camera is not available (in use or does not exist)");
        }
        return mCamera; // returns null if camera is unavailable
    }


    //释放相机

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断请求码
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA) {
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启摄像头
            } else {
                //授权失败
                Toast.makeText(SecondActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 切换摄像头
     *
     */
    public void change() {
        //切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }
}
