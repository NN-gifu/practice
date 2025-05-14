package jp.ac.gifu_u.info.nohara.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MyActivity extends Activity implements LocationListener {

    private LocationManager manager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
    }

    protected void onResume() {
        super.onResume();

        // パーミッションのチェック
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // パーミッションのリクエスト
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            // パーミッションがある場合、位置情報の取得開始
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        }
    }

    protected void onPause(){
        super.onPause();
        manager.removeUpdates(this);
    }

    @SuppressLint("DefaultLocale")
    public void onLocationChanged(@NonNull Location location){
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Toast.makeText(this, String.format("緯度：%.3f, 経度：%.3f", lat, lng), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // パーミッションが許可された場合に位置情報をリクエスト
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                }
            } else {
                // パーミッションが拒否された場合、トーストを表示
                Toast.makeText(this, "パーミッションが拒否されました", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
