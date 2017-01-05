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
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import sv.devla.taberapp.Blog.BlogUtils.CustomAutoCompleteView;
import sv.devla.taberapp.Blog.BlogUtils.RecyclerViewDataAdapter;
import sv.devla.taberapp.Blog.BlogUtils.SectionDataModel;
import sv.devla.taberapp.Blog.BlogUtils.SingleItemModel;
import sv.devla.taberapp.R;
import tyrantgit.explosionfield.ExplosionField;

public class BlogMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final float BLUR_RADIUS = 25f;

    private ExplosionField mExplosionField;
    private AdView mAdView;


    ArrayList<SectionDataModel> allSampleData;
    ArrayList<SectionDataModel> allSampleDataBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.blog_activity_main);
        setContentView(R.layout.blog_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("aslkjdhaskj");
        toolbar.setTitle(Html.fromHtml("<font color='#ff0000'>ActionBarTitle </font>"));
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
           l @Override
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
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bk7);
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


        getSupportActionBar().setTitle("Blog del pastor Jr.");







        String android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.logo);
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
        allSampleDataBooks = new ArrayList<SectionDataModel>();

        createDummyData();
        createDummyDataBooks();

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        RecyclerView my_recycler_view_books = (RecyclerView) findViewById(R.id.my_recycler_view_books);

        my_recycler_view.setHasFixedSize(true);
        my_recycler_view_books.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

        RecyclerViewDataAdapter adapter_books = new RecyclerViewDataAdapter(this, allSampleDataBooks);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view_books.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);

        my_recycler_view_books.setAdapter(adapter_books);






this.setTitle("El Blog del pastor Jr.");
    }


    public void onClick(View view) {

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

            singleItem.add(new SingleItemModel("Año nuevo, ¿Vida vieja?" , "URL " ));
            singleItem.add(new SingleItemModel("No se trata de ti" , "URL " ));
            singleItem.add(new SingleItemModel("Un consejo para el consejo" , "URL " ));
            singleItem.add(new SingleItemModel("Celebrarán el día nacional de la Biblia en El Salvador" , "URL " ));
            singleItem.add(new SingleItemModel("La muerte de un dictador" , "URL " ));
            singleItem.add(new SingleItemModel("Cuando el amor se va" , "URL " ));
            singleItem.add(new SingleItemModel("Justicia sin fronteras" , "URL " ));
            singleItem.add(new SingleItemModel("El negocio del odio" , "URL " ));
            singleItem.add(new SingleItemModel("Hablemos bien de El Salvador" , "URL " ));
            singleItem.add(new SingleItemModel("Los cuatro pilares de la pobreza" , "URL " ));

            //for (int j = 0; j <= 5; j++) {
            //    singleItem.add(new SingleItemModel("Item " , "URL " ));
            //}

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);

        }
    }



    public void createDummyDataBooks() {
        for (int i = 1; i <= 1; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Section " + i);

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();

            singleItem.add(new SingleItemModel("Gotitas de fe 4.0" , "URL " ));
            singleItem.add(new SingleItemModel("Gotitas de fe 3.0" , "URL " ));
            singleItem.add(new SingleItemModel("Gotitas de fe 2.0" , "URL " ));
            singleItem.add(new SingleItemModel("Gotitas de fe 1.0" , "URL " ));
            singleItem.add(new SingleItemModel("Auxilio!, soy casado" , "URL " ));

            //for (int j = 0; j <= 5; j++) {
            //    singleItem.add(new SingleItemModel("Item " , "URL " ));
            //}

            dm.setAllItemsInSection(singleItem);
            allSampleDataBooks.add(dm);

        }
    }


}