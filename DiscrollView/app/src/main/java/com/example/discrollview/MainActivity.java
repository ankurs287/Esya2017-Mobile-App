package com.example.discrollview;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.cat.CountAnimationTextView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.model.Marker;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.message;
import static java.security.AccessController.getContext;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener
{

    private SliderLayout sliderLayout;
    private SliderLayout sponsorsSliderLayout;
    private Button events;
    private Button djNight;
    private Button comedyNight;
    private Button workshops;
    private Button intiatives;
    private ImageView unfoldedDJ;
    private ImageView foldedDJ;
    final int foldingCellArr[]=new int[6];

    private final LatLng LOCATION_IIITD = new LatLng(28.5459495, 77.2688703);
    private GoogleMap map;
    private Marker myMarker;

    private LinearLayout horizontalOuterLayout;
    private HorizontalScrollView horizontalScrollview;
    private TextView horizontalTextView;
    private int scrollMax;
    private int scrollPos =	0;
    private TimerTask clickSchedule;
    private TimerTask scrollerSchedule;
    private TimerTask faceAnimationSchedule;
    private Button clickedButton	=	null;
    private Timer scrollTimer		=	null;
    private Timer clickTimer		=	null;
    private Timer faceTimer         =   null;
    private Boolean isFaceDown      =   true;
    private String[] nameArray = {"AutoDesk", "RAJA Biscuits", "Bittoo Tikki Wala", "CodeChef", "Coding_Ninjas","Engineers India LTD.", "GitLab", "Hacker Earth", "Happn", "Holiday IQ", "Luxor", "Qnswr", "Rau's IAS Study Circle", "Spykar", "UNIBIC"};
    private String[] imageNameArray = {"autode", "bisc", "btw", "codechef", "coding_ninjas","eil", "gitlab", "hack", "happn", "holiq", "luxor", "qnswr","rauias","spykar","unibic"};


    private CountAnimationTextView eventCount;
    private CountAnimationTextView footfallCount;
    private CountAnimationTextView schoolCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface countertype= Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        eventCount=(CountAnimationTextView) findViewById(R.id.count_animation_textView2);
        eventCount.setAnimationDuration(3000).countAnimation(0,30);
        eventCount.setTypeface(countertype);

        footfallCount=(CountAnimationTextView) findViewById(R.id.count_animation_textView1);
        footfallCount.setAnimationDuration(3000).countAnimation(0,8000);
        footfallCount.setTypeface(countertype);

        schoolCount=(CountAnimationTextView) findViewById(R.id.count_animation_textView);
        schoolCount.setAnimationDuration(3000).countAnimation(0,800);
        schoolCount.setTypeface(countertype);

        horizontalScrollview  = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id);
        horizontalOuterLayout =	(LinearLayout)findViewById(R.id.horiztonal_outer_layout_id);
        horizontalTextView    = (TextView)findViewById(R.id.horizontal_textview_id);

        horizontalScrollview.setHorizontalScrollBarEnabled(false);
        addImagesToView();

        ViewTreeObserver vto 		=	horizontalOuterLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalOuterLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getScrollMaxAmount();
                startAutoScrolling();
            }
        });


        map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        myMarker= map.addMarker(new MarkerOptions().position(LOCATION_IIITD).title("Find us here! IIITD"));

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_IIITD, 15);
        map.animateCamera(update);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if(arg0.getTitle().equals("Find us here! IIITD"))
                {// if marker source is clicked
                     Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                     Uri gmmIntentUri = Uri.parse("google.navigation:q=" + "IIIT Delhi");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                return true;
            }

        });



        unfoldedDJ=(ImageView) findViewById(R.id.unfoldedDJ);
        foldedDJ=(ImageView) findViewById(R.id.iconDJ);
        unfoldedDJ.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels;
        unfoldedDJ.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        unfoldedDJ.requestLayout();

        foldedDJ.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        foldedDJ.getLayoutParams().height= (int) Resources.getSystem().getDisplayMetrics().widthPixels/3;
        foldedDJ.requestLayout();

        foldingCellArr[0]=R.id.folding_cell;
        foldingCellArr[1]=R.id.folding_cell2;
        foldingCellArr[2]=R.id.folding_cell3;
        foldingCellArr[3]=R.id.folding_cell4;
        foldingCellArr[4]=R.id.folding_cell5;
        foldingCellArr[5]=R.id.folding_cell6;

        ImageButton facebook = (ImageButton) findViewById(R.id.facebookEsya);
        ImageButton instagram = (ImageButton) findViewById(R.id.instagramEsya);
        ImageButton twitter = (ImageButton) findViewById(R.id.twitterEsya);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Fe9qooYPLQd"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/EsyaIIITD")));
                }
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/esya_iiitd");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/esya_iiitd")));
                }
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "esyaiiitd")));
                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "esyaiiitd")));
                }
            }
        });

        ImageButton email = (ImageButton) findViewById(R.id.emailEsya);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","esya@iiitd.ac.in", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Query");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });


//        Button navigate = (Button) findViewById(R.id.navigateEsya);
//
//        navigate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri gmmIntentUri = Uri.parse("google.navigation:q="+"IIIT Delhi");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
//
//
//            }
//        });

        TextView web = (TextView) findViewById(R.id.websiteEsya);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.esya.iiitd.ac.in")));
            }
        });

        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUnfolder(R.id.folding_cell);
                fc.toggle(false);
            }
        });

        final FoldingCell fc2 = (FoldingCell) findViewById(R.id.folding_cell2);
        // attach click listener to folding cell
        fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUnfolder(R.id.folding_cell2);
                fc2.toggle(false);
            }
        });

        final FoldingCell fc3 = (FoldingCell) findViewById(R.id.folding_cell3);
        // attach click listener to folding cell
        fc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUnfolder(R.id.folding_cell3);
                fc3.toggle(false);
            }
        });

        final FoldingCell fc4 = (FoldingCell) findViewById(R.id.folding_cell4);
        // attach click listener to folding cell
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUnfolder(R.id.folding_cell4);
                fc4.toggle(false);
            }
        });

        final FoldingCell fc5 = (FoldingCell) findViewById(R.id.folding_cell5);
        // attach click listener to folding cell
        fc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUnfolder(R.id.folding_cell5);
                fc5.toggle(false);
            }
        });


        final FoldingCell fc6 = (FoldingCell) findViewById(R.id.folding_cell6);
        // attach click listener to folding cell
        fc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUnfolder(R.id.folding_cell6);
                fc6.toggle(false);
            }
        });


        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Image 1", R.drawable.esya01);
        file_maps.put("Image 2", R.drawable.esya02);
        file_maps.put("Image 3", R.drawable.esya03);
        file_maps.put("Image 4", R.drawable.esya04);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            textSliderView

                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);

        }
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(1250);
        sliderLayout.addOnPageChangeListener(this);


//        sponsorsSliderLayout=(SliderLayout) findViewById(R.id.sponsors);
//        HashMap<String,Integer> sponsor_maps = new HashMap<String, Integer>();
//        sponsor_maps.put("Image 1", R.drawable.esya01);
//        sponsor_maps.put("Image 2", R.drawable.esya02);
//        sponsor_maps.put("Image 3", R.drawable.esya03);
//        sponsor_maps.put("Image 4", R.drawable.esya04);
//        for(String name : file_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
//            textSliderView
//
//                    .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra", name);
//            sponsorsSliderLayout.addSlider(textSliderView);
//
//        }
//
//        sponsorsSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
//        sponsorsSliderLayout.setCustomAnimation(new DescriptionAnimation());
//        sponsorsSliderLayout.setDuration(2000);
//        sponsorsSliderLayout.addOnPageChangeListener(this);


        events=(Button) findViewById(R.id.events);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventsIntent= new Intent(MainActivity.this, EventsMainActivity.class);
                startActivity(eventsIntent);
            }
        });

        djNight=(Button) findViewById(R.id.djNight);
        djNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent djIntent= new Intent(MainActivity.this, DjNight.class);
                startActivity(djIntent);
            }
        });

        comedyNight=(Button) findViewById(R.id.comedyNight);
        comedyNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void checkUnfolder(int unfolderID)
    {
        for(int i=0;i<6;i++)
        {
            FoldingCell fc=(FoldingCell) findViewById(foldingCellArr[i]);
            if(fc.isUnfolded())
            {
                fc.toggle(false);
            }
        }
    }


    @Override
    protected void onStop(){
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public void getScrollMaxAmount(){
//        int actualWidth = (horizontalOuterLayout.getMeasuredWidth()-512);
        int actualWidth = (horizontalOuterLayout.getMeasuredWidth() - getWindowManager().getDefaultDisplay().getWidth());

        scrollMax   = actualWidth;
    }

    public void startAutoScrolling(){
        if (scrollTimer == null) {
            scrollTimer					=	new Timer();
            final Runnable Timer_Tick 	= 	new Runnable() {
                public void run() {
                    moveScrollView();
                }
            };

            if(scrollerSchedule != null){
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }
            scrollerSchedule = new TimerTask(){
                @Override
                public void run(){
                    runOnUiThread(Timer_Tick);
                }
            };

            scrollTimer.schedule(scrollerSchedule, 30, 30);
        }
    }

    public void moveScrollView(){
        scrollPos							= 	(int) (horizontalScrollview.getScrollX() + 4.0);
//        if(scrollPos >= scrollMax){
//            scrollPos						=	0;
//        }
//        horizontalScrollview.scrollTo(scrollPos, 0);
//
        if (scrollPos >= scrollMax) {
            Log.v("childCount", ""+scrollMax);
            addImagesToView();
            getScrollMaxAmount();
        }
        horizontalScrollview.scrollTo(scrollPos, 0);
    }

    /** Adds the images to view. */
    public void addImagesToView(){
        for (int i=0;i<imageNameArray.length;i++){
            final Button imageButton =	new Button(this);
            int imageResourceId		 =	getResources().getIdentifier(imageNameArray[i], "drawable",getPackageName());
            Drawable image 			 =	this.getResources().getDrawable(imageResourceId);
            imageButton.setBackgroundDrawable(image);
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (12*scale + 0.5f);
            imageButton.setPadding (dpAsPixels , 0, dpAsPixels, 0);
            imageButton.setTag(i);

            imageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    if(isFaceDown){
                        if(clickTimer!= null){
                            clickTimer.cancel();
                            clickTimer			=	null;
                        }
                        clickedButton			=	(Button)arg0;
                        stopAutoScrolling();
                        clickedButton.startAnimation(scaleFaceUpAnimation());
                        clickedButton.setSelected(true);
                        clickTimer				=	new Timer();

                        if(clickSchedule != null) {
                            clickSchedule.cancel();
                            clickSchedule = null;
                        }

                        clickSchedule = new TimerTask(){
                            public void run() {
                                startAutoScrolling();
                            }
                        };

                        clickTimer.schedule( clickSchedule, 1500);
                    }
                }
            });
            int h = image.getIntrinsicHeight();
            int w = image.getIntrinsicWidth();
            int r=h/w;
            if(r==0) {
                r=w/h;
                r=256*r;
            }
            else {
                r = 256 / r;
            }

            LinearLayout.LayoutParams params 	=	new LinearLayout.LayoutParams(r,256);
            params.setMargins(0, dpAsPixels, 0, dpAsPixels);
            imageButton.setLayoutParams(params);
            horizontalOuterLayout.addView(imageButton);
        }
    }

    public Animation scaleFaceUpAnimation(){
        Animation scaleFace = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleFace.setDuration(500);
        scaleFace.setFillAfter(true);
        scaleFace.setInterpolator(new AccelerateInterpolator());
        Animation.AnimationListener	scaleFaceAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                horizontalTextView.setText(nameArray[(Integer) clickedButton.getTag()]);
                isFaceDown = false;
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                if(faceTimer != null){
                    faceTimer.cancel();
                    faceTimer = null;
                }

                faceTimer = new Timer();
                if(faceAnimationSchedule != null){
                    faceAnimationSchedule.cancel();
                    faceAnimationSchedule = null;
                }
                faceAnimationSchedule = new TimerTask() {
                    @Override
                    public void run() {
                        faceScaleHandler.sendEmptyMessage(0);
                    }
                };

                faceTimer.schedule(faceAnimationSchedule, 750);
            }
        };
        scaleFace.setAnimationListener(scaleFaceAnimationListener);
        return scaleFace;
    }

    private Handler faceScaleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(clickedButton.isSelected() == true)
                clickedButton.startAnimation(scaleFaceDownAnimation(500));
        }
    };

    public Animation scaleFaceDownAnimation(int duration){
        Animation scaleFace = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleFace.setDuration(duration);
        scaleFace.setFillAfter(true);
        scaleFace.setInterpolator(new AccelerateInterpolator());
        Animation.AnimationListener	scaleFaceAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {}
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                horizontalTextView.setText("");
                isFaceDown = true;
            }
        };
        scaleFace.setAnimationListener(scaleFaceAnimationListener);
        return scaleFace;
    }

    public void stopAutoScrolling(){
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer	=	null;
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void onDestroy(){
        clearTimerTaks(clickSchedule);
        clearTimerTaks(scrollerSchedule);
        clearTimerTaks(faceAnimationSchedule);
        clearTimers(scrollTimer);
        clearTimers(clickTimer);
        clearTimers(faceTimer);

        clickSchedule         = null;
        scrollerSchedule      = null;
        faceAnimationSchedule = null;
        scrollTimer           = null;
        clickTimer            = null;
        faceTimer             = null;
        super.onDestroy();
    }

    private void clearTimers(Timer timer){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void clearTimerTaks(TimerTask timerTask){
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}