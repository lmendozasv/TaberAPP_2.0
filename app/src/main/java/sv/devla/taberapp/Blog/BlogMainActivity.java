package sv.devla.taberapp.Blog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import sv.devla.taberapp.Blog.BlogUtils.SectionDataModel;
import tyrantgit.explosionfield.ExplosionField;

public class BlogMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final float BLUR_RADIUS = 25f;

    private ExplosionField mExplosionField;
    private AdView mAdView;


    ArrayList<SectionDataModel> allSampleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                try {
                    ImageView imageView = (ImageView) findViewById(R.id.imageViewHeader);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bk4);
                    Bitmap blurredBitmap = blur(bitmap);
                    imageView.setImageBitmap(blurredBitmap);
                }
                catch(Exception s){
                    Log.d("Error",s.toString());
                }

            }
        };


        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setTitle("");







        String android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("CD9BF0A478AF16984B67A0EC13B45623")
                .build();
        mAdView.loadAd(adRequest);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_location);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);



//        YoYo.with(Techniques.Shake)
        //               .duration(7000)
        //               .playOn(findViewById(R.id.editText2));
        //

        /*
        YoYo.with(Techniques.Shake)
                .duration(7000)
                .playOn(findViewById(R.id.textView2));
        */



        allSampleData = new ArrayList<SectionDataModel>();

        createDummyData();

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);

        final CustomAutoCompleteView etd = (CustomAutoCompleteView) findViewById(R.id.etautobusqueda);



        etd.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("click", "onMtouch");



        MainActivity.this.onClick(v);
                //Intent i = new Intent(MainActivity.this, SearchActivity.class);
                 //   startActivity(i);

              //  finish();


                //etd.setFocusable(true);
                //etd.requestFocus();
                //etd.setSelection(etd.getText().length());

                return false;
            }
        });

    }



    public void onClick(View view) {
        //View imageView = findViewById(R.id.imageView);
        View textView = findViewById(R.id.etautobusqueda);
        //View button = findViewById(R.id.button);

        Intent intent = new Intent(this, SearchActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setTransitionName(getString(R.string.activity_mixed_trans));
//            button.setTransitionName(getString(R.string.activity_mixed_trans));

  //          Pair<View, String> pair1 = Pair.create(imageView, imageView.getTransitionName());
            Pair<View, String> pair2 = Pair.create(textView, textView.getTransitionName());
    //        Pair<View, String> pair3 = Pair.create(button, button.getTransitionName());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, pair2);
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }

    public Bitmap makeTransparent(Bitmap bitmap, int opacity) {
        Bitmap mutableBitmap = bitmap.isMutable()
                ? bitmap
                : bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }



    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);

        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        outputBitmap=makeTransparent(outputBitmap,190);
        return outputBitmap;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createDummyData() {
        for (int i = 1; i <= 1; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Section " + i);

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();

            singleItem.add(new SingleItemModel("ACEITES" , "URL " ));
            singleItem.add(new SingleItemModel("HUEVOS Y CARNES" , "URL " ));
            singleItem.add(new SingleItemModel("PAN" , "URL " ));
            singleItem.add(new SingleItemModel("FRUTAS" , "URL " ));
            singleItem.add(new SingleItemModel("GRANOS BÁSICOS" , "URL " ));

            singleItem.add(new SingleItemModel("HARINAS" , "URL " ));
            singleItem.add(new SingleItemModel("LÁCTEOS" , "URL " ));
            singleItem.add(new SingleItemModel("VERDURAS" , "URL " ));
            singleItem.add(new SingleItemModel("PASTAS" , "URL " ));
            singleItem.add(new SingleItemModel("MARGARINAS Y MANTECAS" , "URL " ));


            singleItem.add(new SingleItemModel("ATÚN" , "URL " ));
            singleItem.add(new SingleItemModel("SARDINAS" , "URL " ));
            singleItem.add(new SingleItemModel("BEBIDAS" , "URL " ));
            singleItem.add(new SingleItemModel("SALSAS" , "URL " ));
            singleItem.add(new SingleItemModel("LIMPIEZA Y ASEO PERSONAL" , "URL " ));

            //for (int j = 0; j <= 5; j++) {
            //    singleItem.add(new SingleItemModel("Item " , "URL " ));
            //}

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);

        }
    }
}