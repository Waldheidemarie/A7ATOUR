package a7aent.com.artour;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.rendering.ViewSizer;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import a7aent.com.artour.common.helpers.SnackbarHelper;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ArFragment arFragment;
    private ImageView fitToScanView;
    ModelRenderable andyRenderable;
    private DatabaseReference ref;
    ViewRenderable sharda1, sharda2, sharda3, sharda4, sharda6, sharda5, sharda7, sharda8;
    ModelRenderable box;
    ModelRenderable campfire;
    ViewRenderable testViewRenderable, hideout, jagatFarm, su, _106, smsr, sahs, dental, sbsr, set;
    private Button mButton;
    TextToSpeech tts;
    private ImageView trans, vol;
    public void onPause(){
        if(t11 !=null){
            t11.stop();
            t11.shutdown();
        }
        super.onPause();
    }
    // Augmented image and its associated center pose anchor, keyed by the augmented image in
    // the database.
    private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();
    private final Map<AugmentedImage, ARImageNodeBox> augmentedImageMap1 = new HashMap<>();
    private static final String GLTF_ASSET = "https://firebasestorage.googleapis.com/v0/b/fir-2-83e2c.appspot.com/o/model.gltf?alt=media&token=68ec49ce-9b0e-4a5f-a69c-6b657b3da70a";
            //"https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf";
    private static final String dll = "https://firebasestorage.googleapis.com/v0/b/fir-2-83e2c.appspot.com/o/imp.glb?alt=media&token=21b84f46-1bc8-4cd2-8beb-8f005cf45d91";

    private static String place;
    TextToSpeech t11;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trans = (ImageView) findViewById(R.id.translate);
        vol =(ImageView) findViewById(R.id.vol);
      //  vol.setVisibility(View.GONE);
        trans.setVisibility(View.GONE);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        fitToScanView = findViewById(R.id.image_view_fit_to_scan);
        fitToScanView.setVisibility(View.GONE);

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

        place = getIntent().getStringExtra("place");


        t11=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t11.setLanguage(Locale.UK);
                }
            }
        });

        ref = FirebaseDatabase.getInstance().getReference();

        if (!TextUtils.isEmpty(place)){

            if(place.equals("Sharda University")) {
                Toast.makeText(getApplicationContext(), "INSIDE SHARDA's PLANE", Toast.LENGTH_LONG).show();

                ViewRenderable.builder()
                        .setView(this, R.layout.sharda_card)
                        .build()
                        .thenAccept(renderable -> testViewRenderable = renderable);

                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> sharda1 = renderable);

                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> sharda2 = renderable);


                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (testViewRenderable == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());

                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(testViewRenderable);

                            Node node = new Node();
                            node.setParent(andy);
                            node.setLocalPosition(new Vector3(6.0f, 0f, 0f));
                            node.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 0));
                            node.setRenderable(sharda1);

                            Node node1 = new Node();
                            node1.setParent(node);
                            node1.setLocalPosition(new Vector3(4.0f, 0f, 2.0f));
                            node1.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0f), -90));
                            node1.setRenderable(sharda2);




                            trans.setVisibility(View.VISIBLE);
                            vol.setVisibility(View.VISIBLE);


                            fitToScanView.setVisibility(View.GONE);
                            View view = testViewRenderable.getView();

                            Button btn = view.findViewById(R.id.moreAboutButton);
                            TextView title = view.findViewById(R.id.placeTitle);
                            TextView desc = view.findViewById(R.id.placeDesc);
                            WebView mWebview = view.findViewById(R.id.webview);
                            View view1 = sharda1.getView();

                            TextView title1 = view1.findViewById(R.id.placeTitle);
                            TextView desc1 = view1.findViewById(R.id.placeDesc);
                            Button btn1 = view1.findViewById(R.id.moreAboutButton);


                            View v3 = sharda2.getView();
                            TextView t1 = v3.findViewById(R.id.placeTitle);
                            TextView d1 = v3.findViewById(R.id.placeDesc);
                            Button b1 = v3.findViewById(R.id.moreAboutButton);

                            DatabaseReference sharda = ref.child("places").child("sharda");

                            sharda.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String titleS = dataSnapshot.child("k0").child("title").getValue().toString();
                                    String descS = dataSnapshot.child("k0").child("desc").getValue().toString();

                                    title.setText(titleS);
                                    desc.setText("Sharda University is a leading Educational institution based out of Greater Noida, Delhi NCR. A venture of the renowned Sharda Group of Institutions (SGI), The University has established itself as a high quality education provider with prime focus on holistic learning and imbibing competitive abilities in students.");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(desc.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            sharda.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String title = dataSnapshot.child("k1").child("title").getValue().toString();
                                    String desc = dataSnapshot.child("k1").child("desc").getValue().toString();

                                    title1.setText(title);
                                    desc1.setText(desc);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            sharda.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String title = dataSnapshot.child("k2").child("title").getValue().toString();
                                    String desc = dataSnapshot.child("k2").child("desc").getValue().toString();

                                    t1.setText(title);
                                    d1.setText(desc);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });






                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {

                                                TranslateOptions options = TranslateOptions
                                                        .newBuilder()
                                                        .setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY")
                                                        .build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                desc.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                desc.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });


                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://sharda.ac.in");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });
                            andy.select();
                        });


            }

            else if(place.equals("Jagat Farm Market")){
                Toast.makeText(getApplicationContext(), "INSIDE JAGAT PLANE", Toast.LENGTH_LONG).show();
               ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> jagatFarm = renderable);
               
                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (jagatFarm == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(jagatFarm);

                            andy.select();

                            fitToScanView.setVisibility(View.GONE);


                            View v = jagatFarm.getView();
                            TextView tv = v.findViewById(R.id.placeTitle);
                            TextView tv1 = v.findViewById(R.id.placeDesc);
                            Button bt = v.findViewById(R.id.moreAboutButton);

                            WebView mWebview = v.findViewById(R.id.webview);

                            tv.setText("JAGAT FARM MARKET");

                            tv1.setText("The road side food stuff that you get here, is tasty and at the same time cost effective. The place is flooded with Chinese food stalls at the corners of the road, that offer variety of dishes. Also, are there some lavish restaurants to satisfy your taste buds. The choice of food items is immense, from typical north Indian food stuff to Chinese and South Indian items, the list is apparently too long to mention. Drive in here for some fantastic road side food stuff and items.\n" +
                                    "The place has always served the general needs. Shops here offer almost all the daily need items and the place has always been a lifeline for the people living in Greater Noida, students in particular. There are a variety of stationary shops from where the students collect the stuff they need. Jagat farm has seen decent growth in terms of infrastructure and number of shops in the recent past. The place now offers number of shops for all the gadget needs of the gadget freaks.\n" +
                                    "The place also has a lot to offer for the fashion lovers. There are a lot of shops offering clothes of the latest trends and style. The Jagat Plaza which is one of the main shopping complexes at the Jagat Farm, has many such shops that offer stylish clothes at reasonable price. The best thing about the place is that, you are not restricted to the conventional brands, instead you get the real “Dilli Wala” taste of fashion. If you want to grab new shades or may be stylish artificial jewellery, then the road-side shops at the Jagat farm will probably suffice your purpose.");


                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.google.com/search?q=jagat+farm&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjVm-Pd5ovgAhWGNY8KHfTaDjAQ_AUIDygC&biw=1366&bih=638");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }

            else if(place.equals("School of Medical Sciences and Research- Sharda University")){
                Toast.makeText(getApplicationContext(), "INSIDE SMSR PLANE", Toast.LENGTH_LONG).show();
                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> smsr = renderable);

                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (smsr == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(smsr);

                            andy.select();

                            fitToScanView.setVisibility(View.GONE);

                            View vsmsr = smsr.getView();
                            TextView tv = vsmsr.findViewById(R.id.placeTitle);
                            TextView tv1 = vsmsr.findViewById(R.id.placeDesc);
                            Button bt = vsmsr.findViewById(R.id.moreAboutButton);

                            WebView mWebview = vsmsr.findViewById(R.id.webview);

                            tv.setText("SCHOOL OF MEDICAL SCIENCES AND RESEARCH");


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("places").child("smsr");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tv1.setText(dataSnapshot.getValue().toString());
                                    Toast.makeText(getApplicationContext(), "LOADED", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.sharda.ac.in/schools/school-of-medical-sciences-and-research");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }


            else if(place.equals("Sharda University, SET")){
                Toast.makeText(getApplicationContext(), "INSIDE SET PLANE", Toast.LENGTH_LONG).show();
                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> set = renderable);

                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (set == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(set);

                            andy.select();

                            View vsmsr = set.getView();
                            TextView tv = vsmsr.findViewById(R.id.placeTitle);
                            TextView tv1 = vsmsr.findViewById(R.id.placeDesc);
                            Button bt = vsmsr.findViewById(R.id.moreAboutButton);

                            WebView mWebview = vsmsr.findViewById(R.id.webview);

                            tv.setText("SCHOOL OF ENGG AND TECHNOLOGY");

                            fitToScanView.setVisibility(View.GONE);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("places").child("set").child("k0")
                                    .child("desc");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tv1.setText(dataSnapshot.getValue().toString());
                                    Toast.makeText(getApplicationContext(), "LOADED", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.sharda.ac.in/schools/engineering-technology");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }

            else if(place.equals("School Of Health And Allied Sciences")){
                Toast.makeText(getApplicationContext(), "INSIDE SAHS PLANE", Toast.LENGTH_LONG).show();
                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> sahs = renderable);

                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (sahs == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(sahs);

                            andy.select();

                            View vsahs = sahs.getView();
                            TextView tv = vsahs.findViewById(R.id.placeTitle);
                            TextView tv1 = vsahs.findViewById(R.id.placeDesc);
                            Button bt = vsahs.findViewById(R.id.moreAboutButton);

                            WebView mWebview = vsahs.findViewById(R.id.webview);

                            tv.setText("SCHOOL OF ALLIED HEALTH SCIENCES");

                            fitToScanView.setVisibility(View.GONE);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("places").child("sahs");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tv1.setText(dataSnapshot.getValue().toString());
                                    Toast.makeText(getApplicationContext(), "LOADED", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.sharda.ac.in/schools/allied-health-sciences");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }


            else if(place.equals("SHARDA SCHOOL OF DENTAL SCIENCES")){
                Toast.makeText(getApplicationContext(), "INSIDE DENTAL SCIENCE PLANE", Toast.LENGTH_LONG).show();
                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> dental = renderable);

                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (dental == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(sahs);

                            andy.select();

                            View vsahs = sahs.getView();
                            TextView tv = vsahs.findViewById(R.id.placeTitle);
                            TextView tv1 = vsahs.findViewById(R.id.placeDesc);
                            Button bt = vsahs.findViewById(R.id.moreAboutButton);

                            WebView mWebview = vsahs.findViewById(R.id.webview);

                            tv.setText("SCHOOL OF DENTAL SCIENCES");

                            fitToScanView.setVisibility(View.GONE);


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("places").child("dental");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tv1.setText(dataSnapshot.getValue().toString());
                                    Toast.makeText(getApplicationContext(), "LOADED", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.sharda.ac.in/schools/dental-sciences");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }


            else if(place.equals("School of Business Studies (SBS) - Sharda University")){
                Toast.makeText(getApplicationContext(), "INSIDE SBS PLANE", Toast.LENGTH_LONG).show();
                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> dental = renderable);

                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (dental == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(sahs);

                            andy.select();

                            View vsahs = sahs.getView();
                            TextView tv = vsahs.findViewById(R.id.placeTitle);
                            TextView tv1 = vsahs.findViewById(R.id.placeDesc);
                            Button bt = vsahs.findViewById(R.id.moreAboutButton);

                            WebView mWebview = vsahs.findViewById(R.id.webview);

                            tv.setText("SCHOOL OF BUSINESS STUDIES");


                            fitToScanView.setVisibility(View.GONE);


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("places").child("sbs");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tv1.setText(dataSnapshot.getValue().toString());
                                    Toast.makeText(getApplicationContext(), "LOADED", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.sharda.ac.in/schools/business-studies");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }


            else if(place.equals("School of Basic Sciences & Research (SBSR)")){
                Toast.makeText(getApplicationContext(), "INSIDE SBSR PLANE", Toast.LENGTH_LONG).show();
                ViewRenderable.builder()
                        .setView(this, R.layout.about_place)
                        .build()
                        .thenAccept(renderable -> sbsr = renderable);

                arFragment.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                            if (sbsr == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            trans.setVisibility(View.VISIBLE);
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(sbsr);

                            andy.select();

                            View vsmsr = sbsr.getView();
                            TextView tv = vsmsr.findViewById(R.id.placeTitle);
                            TextView tv1 = vsmsr.findViewById(R.id.placeDesc);
                            Button bt = vsmsr.findViewById(R.id.moreAboutButton);

                            WebView mWebview = vsmsr.findViewById(R.id.webview);

                            tv.setText("SCHOOL OF MEDICAL SCIENCES AND RESEARCH");

                            fitToScanView.setVisibility(View.GONE);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("places").child("sbsr");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tv1.setText(dataSnapshot.getValue().toString());
                                    Toast.makeText(getApplicationContext(), "LOADED", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            vol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t11.speak(tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tv1.setVisibility(View.GONE);
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

                                    mWebview.setWebViewClient(new WebViewClient());


                                    mWebview .loadUrl("https://www.sharda.ac.in/schools/basic-sciences-research");

                                    Toast.makeText(getApplicationContext(), "Clicked me", Toast.LENGTH_LONG).show();
                                }
                            });

                            trans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                //Your code goes here
                                                //translate = TranslateOptions.getDefaultInstance().getService();
                                                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyASQnBgOVmiLQIKbnzEa_IDBmh4BBdDrVY").build();
                                                Translate translate = options.getService();

                                                // The text to translate
                                                String text = "Hello, world!";

                                                // Translates some text into Russian
                                                Translation translation =
                                                        translate.translate(
                                                                tv1.getText().toString(),
                                                                Translate.TranslateOption.sourceLanguage("en"),
                                                                Translate.TranslateOption.targetLanguage("ru"));

                                                tv1.setText(translation.getTranslatedText());

                                                //System.out.printf("Text: %s%n", toastMsg);
                                                System.out.printf("Translation: %s%n", translation.getTranslatedText());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                }
                            });
                        });



            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (augmentedImageMap.isEmpty()) {
            //fitToScanView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Registered with the Sceneform Scene object, this method is called at the start of each frame.
     *
     * @param frameTime - time since last frame.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        // If there is no frame or ARCore is not tracking yet, just return.
        if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
            return;
        }



        Collection<AugmentedImage> updatedAugmentedImagesz =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImagesz) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image " + augmentedImage.getName();
                    SnackbarHelper.getInstance().showMessage(this, text);
                    break;

                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    fitToScanView.setVisibility(View.GONE);

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        Toast.makeText(getApplicationContext(), augmentedImage.getName(), Toast.LENGTH_LONG).show();
                        if(TextUtils.equals(augmentedImage.getName(), "hideout2.jpg")){
                            AnchorNode anchorNode = new AnchorNode();
                            anchorNode.setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                            anchorNode.setParent(arFragment.getArSceneView().getScene());


                            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                            node.setLocalRotation(Quaternion.axisAngle(new Vector3(0.5f, 0, 0), -90));
                            node.setLocalScale(new Vector3(0.001f, 0.001f, 0.001f));
                            node.setParent(anchorNode);
                            node.setRenderable(hideout);
                            node.select();

                            ViewRenderable.builder()
                                    .setView(this, R.layout.hideout_layout)
                                    .build()
                                    .thenAccept(renderable -> hideout = renderable);






                        }

                    }
                    //break;

                case STOPPED:
                    augmentedImageMap.remove(augmentedImage);

                    break;
            }
        }

        Collection<AugmentedImage> updatedAugmentedImages =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image " + augmentedImage.getName();
                    SnackbarHelper.getInstance().showMessage(this, text);
                    break;

                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    fitToScanView.setVisibility(View.GONE);

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        Toast.makeText(getApplicationContext(), augmentedImage.getName(), Toast.LENGTH_LONG).show();
                        if(TextUtils.equals(augmentedImage.getName(), "qr.png")){
                            AnchorNode anchorNode = new AnchorNode();
                            anchorNode.setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                            anchorNode.setParent(arFragment.getArSceneView().getScene());


                                TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                                node.setLocalRotation(Quaternion.axisAngle(new Vector3(0.5f, 0, 0), -90));
                                node.setLocalScale(new Vector3(0.001f, 0.001f, 0.001f));
                                node.setParent(anchorNode);
                                node.setRenderable(hideout);
                                node.select();

                                ViewRenderable.builder()
                                        .setView(this, R.layout.team106)
                                        .build()
                                        .thenAccept(renderable -> hideout = renderable);






                        }

                    }
                    //break;

                case STOPPED:
                    augmentedImageMap.remove(augmentedImage);

                    break;
            }
        }

        Collection<AugmentedImage> updatedAugmentedImages2 =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages2) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image " + augmentedImage.getName();
                    SnackbarHelper.getInstance().showMessage(this, text);
                    break;

                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    fitToScanView.setVisibility(View.GONE);

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        Toast.makeText(getApplicationContext(), augmentedImage.getName(), Toast.LENGTH_LONG).show();
                        if(TextUtils.equals(augmentedImage.getName(), "hideout1.jpg") ||TextUtils.equals(augmentedImage.getName(), "hideout2.jpg") ){
                            AnchorNode anchorNode = new AnchorNode();
                            anchorNode.setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                            anchorNode.setParent(arFragment.getArSceneView().getScene());



                            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                            node.setLocalRotation(Quaternion.axisAngle(new Vector3(0.5f, 0, 0), -90));
                            node.setLocalScale(new Vector3(0.00001f, 0.00001f, 0.00001f));
                            node.setParent(anchorNode);
                            node.setRenderable(hideout);
                            node.select();


                            ViewRenderable.builder()
                                    .setView(this, R.layout.hideout_layout)
                                    .build()
                                    .thenAccept(renderable -> hideout = renderable);


                        }

                    }
                    //break;

                case STOPPED:
                    augmentedImageMap.remove(augmentedImage);

                    break;
            }
        }


        Collection<AugmentedImage> updatedAugmentedImages3 =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages3) {

            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image " + augmentedImage.getName();
                    SnackbarHelper.getInstance().showMessage(this, text);
                    break;

                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    fitToScanView.setVisibility(View.GONE);

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap1.containsKey(augmentedImage)) {
                        Toast.makeText(getApplicationContext(), augmentedImage.getName(), Toast.LENGTH_LONG).show();
                        if(TextUtils.equals(augmentedImage.getName(), "qr.jpg")){

                            ViewRenderable.builder()
                                    .setView(this, R.layout.team106)
                                    .build()
                                    .thenAccept(renderable -> _106 = renderable);


                            AnchorNode anchorNode = new AnchorNode();

                            anchorNode.setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                            anchorNode.setParent(arFragment.getArSceneView().getScene());

                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(_106);
                            andy.select();




                        } else{



                        }

                    }
                    //break;

                case STOPPED:
                    augmentedImageMap1.remove(augmentedImage);
                    break;
            }
        }

        Collection<AugmentedImage> updatedAugmentedImages1 =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages1) {

            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image " + augmentedImage.getName();
                    SnackbarHelper.getInstance().showMessage(this, text);
                    break;

                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    fitToScanView.setVisibility(View.GONE);

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap1.containsKey(augmentedImage)) {
                        Toast.makeText(getApplicationContext(), augmentedImage.getName(), Toast.LENGTH_LONG).show();
                        if(TextUtils.equals(augmentedImage.getName(), "t1.jpg") || TextUtils.equals(augmentedImage.getName(), "t2.jpg")|| TextUtils.equals(augmentedImage.getName(), "t3.jpg")|| TextUtils.equals(augmentedImage.getName(), "t4.jpg")|| TextUtils.equals(augmentedImage.getName(), "t5.jpg")){
                           /* ARImageNodeBox node = new ARImageNodeBox(this);
                            node.setImage(augmentedImage);
                            augmentedImageMap1.put(augmentedImage, node);
                            arFragment.getArSceneView().getScene().addChild(node);*/




                            AnchorNode anchorNode = new AnchorNode();

                            anchorNode.setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                            anchorNode.setParent(arFragment.getArSceneView().getScene());

                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            andy.setRenderable(sharda1);
                            andy.select();

                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            anchorNode.setOnTouchListener(new Node.OnTouchListener() {
                                @Override
                                public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                                    Toast.makeText(getApplicationContext(), "You touched me", Toast.LENGTH_LONG).show();
                                    return false;
                                }
                            });

                            ViewRenderable.builder()
                                    .setView(this, R.layout.about_place)
                                    .build()
                                    .thenAccept(renderable -> sharda1 = renderable);



                           /* ModelRenderable.builder()
                                    .setSource(this, RenderableSource.builder().setSource(
                                            this,
                                            Uri.parse(GLTF_ASSET),
                                            RenderableSource.SourceType.GLTF2)
                                            .setScale(0.1f)  // Scale the original model to 50%.
                                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                            .build())
                                    .setRegistryId(GLTF_ASSET)
                                    .build()
                                    .thenAccept(renderable -> andyRenderable = renderable)
                                    .exceptionally(
                                            throwable -> {
                                                Toast toast =
                                                        Toast.makeText(this, "Unable to load renderable " +
                                                                GLTF_ASSET, Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                                return null;
                                            });*/

                         /*
                            ModelRenderable.builder()
                                    .setSource(this, Uri.parse("tree01.sfb"))
                                    .build()
                            .thenAccept(renderable -> andyRenderable = renderable);*/

                        } else{



                        }

                    }
                    break;

                case STOPPED:
                    augmentedImageMap1.remove(augmentedImage);
                    break;
            }
        }
    }

//    private void augmentIt() {
//        ViewRenderable.builder()
//                .setView(this, R.layout.about_place)
//                .build()
//                .thenAccept(renderable -> _106 = renderable);
//
//        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
//            @Override
//            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
//                if ( hitResult == null) {
//                    return;
//                }
//
//                Anchor anchor = hitResult.createAnchor();
//                AnchorNode anchorNode = new AnchorNode(anchor);
//                anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//                // Create the transformable andy and add it to the anchor.
//                TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
//                node.setParent(anchorNode);
//                node.setRenderable(_106);
//
//                anchorNode.setOnTouchListener(new Node.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
//                        Toast.makeText(getApplicationContext(), "You touched me", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                });
//            }
//        });
//    }

    public void speak(String desc){

        Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_LONG).show();
        tts = new TextToSpeech(getApplicationContext(), this);
        tts.setLanguage(Locale.US);
        tts.speak("hello", TextToSpeech.QUEUE_ADD, null);

    }

    @Override
    public void onInit(int status) {

    }
}
