    package sv.devla.taberapp;

    import android.Manifest;
    import android.app.Activity;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Handler;
    import android.preference.PreferenceManager;

    import java.util.ArrayList;
    import java.util.List;

    import sv.devla.taberapp.Blog.BlogMainActivity;


    public class SplashActivity extends Activity {

        // Splash screen timer
        private static int SPLASH_TIME_OUT = 2500;
        Intent main=null;
        final int CAMERA=10;
        final int PERMISSION_REQUEST_CODE=10;
        final int PERMISSION_REQUEST_CODEI=100;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            //startService(new Intent(this, DatabaseUpdaterService.class));





               this.startAct();






        }




        @Override
        protected void onStart() {
            super.onStart();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

                List<String> permissions = new ArrayList<String>();

                if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.CAMERA);

                }
                if (!permissions.isEmpty()) {
                    requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
                }
            }


        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
                case 111: {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);


                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);

                        }
                    }
                }
                break;
                default: {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }

        public void startAct(){

                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {




                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                        String logged = preferences.getString("Logged", "0");

                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        main = new Intent(SplashActivity.this, MainActivity.class);

                        if(logged.equals("0")){
                            startActivity(main);


                        }
                        else{
                            startActivity(i);
                        }

                        // close this activity
                        finish();
                    }
                }, SPLASH_TIME_OUT);



        }


    }