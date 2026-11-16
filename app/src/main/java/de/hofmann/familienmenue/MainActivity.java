package de.hofmann.familienmenue;

import android.app.Activity;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.graphics.Typeface;
import android.view.Gravity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.*;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
  final String[][] menus = {
    {"Tagliatelle mit weißem Spargel","48093","530","2","Tagliatelle in Spargel-Sahnesoße mit Karotten-Brokkoli-Gemüse, verfeinert mit trockenem Weißwein."},
    {"Kartoffel-Gemüseeintopf","6816","450","2","Herzhafter Gemüseeintopf mit Kartoffeln und Zucchini."},
    {"Planted Hack Bolognese Style","48939","600","1","Spaghetti mit veganem Hack auf Sojaproteinbasis in würziger Tomatensoße mit Kräutern und Schalotten."},
    {"Schweinesteak au four","48087","550","1","Schweinesteak mit zartem Hühnerfleisch in heller Soße und Gouda, dazu Erbsengemüse und Pommes frites."},
    {"Grillteller Griechische Art","48076","420","1","Schweinegeschnetzeltes nach Gyros-Art, Hackfleischbällchen und gegrillter Schweinenacken mit Zwiebeln, Paprika und Tomaten-Gemüsereis."},
    {"Herzhafter Kasseler Nacken","7215","530","3","Kasseler Nacken mit Speck, Kartoffelpüree und Sauerkraut."},
    {"Green Chicken Bowl","48528","530","1","Marinierte Hähnchentaler mit Kichererbsen, Belugalinsen, Edamame und Brokkoli in Tomatensoße."},
    {"Chili-Käse-Bällchen","48461","560","2","Rindfleischbällchen in würziger Soße mit grüner Paprika, Mozzarella und Chili, dazu Steakhouse Frites."},
    {"Würstchengulasch","48418","600","2","Bockwurst in würziger Tomatensoße mit Speckwürfeln, dazu Schlingli-Nudeln."},
    {"Kartoffeltaschen","6315","380","4","Kartoffeltaschen mit Frischkäse-Kräuterfüllung und Rahmgemüse."},
    {"Grießauflauf","7914","545","2","Süßer Grießauflauf mit Pfirsichwürfeln."},
    {"Zartes Kalbsschnitzel paniert","8305","490","1","Paniertes Kalbsschnitzel mit Mandelblättchen in Rahmsoße, dazu Brokkoli, Pariser Karotten und Salzkartoffeln."},
    {"Schaschlikpfanne","6720","390","3","Schweinefleisch in pikanter Soße mit Speck, Zwiebeln und Paprika, dazu Langkornreis."},
    {"Rucola-Spinat-Spaghetti","48909","450","1","Spaghetti mit grünem Pesto aus Rucola und Spinat, dazu Grillgemüse."},
    {"Sahne-Tomaten-Hähnchen","48526","490","2","Hähnchenbrustfilet in Sahnesoße mit getrockneten Tomaten und Kräuter-Gnocchi."},
    {"Schweinefleisch Rügener Art","41001","440","2","Schweinefleisch in Sahnesoße mit Salami, Gurkenstreifen, Tomaten und Zwiebeln, dazu Rösti-Ecken."},
    {"Grünkohl Norddeutsche Art","48073","560","2","Grünkohl mit Speck, Bauernwurst und Kasseler Nacken, dazu Bratkartoffeln und Zwiebeln."},
    {"Zarte Entenkeule","7524","560","2","Entenkeule in Bratapfelsoße mit Apfelrotkohl und Kartoffelklößen."},
    {"Jägerschnitzel","48210","550","1","Paniertes Schweineschnitzel mit Champignon-Rahmsoße und Pommes frites."}
  };
  SharedPreferences prefs;
  LinearLayout list;
  Spinner user;
  TextView stats;
  boolean showRemoved=false;
  boolean compactMode=false;
  String searchQuery="";
  String filter="Alle";
  String categoryFilter="Alle";
  String expandedArticle="";
  int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+0.5f);}
  int spColor=Color.rgb(32,69,48);
  int green=Color.rgb(28,83,56);
  int cream=Color.rgb(247,249,246);
  int lime=Color.rgb(190,235,83);
  String photoUrl(String id){
    switch(id){
      case "48093":return "https://img.hellofresh.com/f_auto%2Cfl_lossy%2Ch_640%2Cq_auto%2Cw_1200/hellofresh_s3/image/HF_Y23_R72_W22_BNL_P6768-1_Main__3low-3d47e17e.jpg";
      case "6816":return "https://www.arise-app.com/images/dishes/de/kartoffeleintopf-r25a7k.webp";
      case "48939":return "https://media.hellofresh.com/q_100%2Cw_3840%2Cf_auto%2Cc_limit%2Cfl_lossy/recipes/image/HF220907_R32_W44_DE_R4301-1_MB_Main_2_low-51f3618e.jpg";
      case "48087":return "https://hofmanns-shop.de/cdn/shop/files/48087_Schweinesteak_au_four_720ef597-8081-4fdc-b577-00d11ff5dd09.png?v=1774605637";
      case "48076":return "https://dishlocal.com/restaurants/images/dishes-png/grillteller-mix.png";
      case "7215":return "https://www.daskochrezept.de/sites/daskochrezept.de/files/styles/full_width_tablet_4_3/public/2021-05/kassler_mit_sauerkraut_1.jpg?h=a1e1a043u0026itok%3DSYaA6weH";
      case "48528":return "https://www.unileverfoodsolutions.at/dam/global-ufs/mcos/dach/calcmenu-recipes/ch-recipes/2023/plantmade-q2/plantmade-q2-salatbowle-mit-spinat-und-crispychicken/plantmade-q2-salatbowle-mit-spinat-und-crispychicken-header.jpg";
      case "48461":return "https://s3.eu-west-3.amazonaws.com/fr.du-jour.menu.medias/0/7/8/3/3/407833/25702_1769617595.jpeg";
      case "48418":return "https://fivi.gg/cdn/shop/articles/wurstchengulasch_blogbild_v1.jpg?v=1773673867";
      case "6315":return "https://primary.jwwb.nl/public/i/z/n/temp-tdhskojadwuiusgmymlr/13544-00-high.jpg?enable=upscale&enable-io=true&fit=bounds&width=1200";
      case "7914":return "https://static.chefkoch-cdn.de/ck.de/rezepte/152/152136/1121563-960x720-quark-griess-pfirsich-auflauf.jpg";
      case "8305":return "https://hofmanns-shop.de/cdn/shop/products/zartes-kalbsschnitzel-paniert_48300_2.jpg?v=1773871139&width=1445";
      case "6720":return "https://storefront-prod.de.picnicinternational.com/static/images/recipes/8887f312e0e7f4f44043e8dc17c980626b3a73f06a93245667a44eb47c462be2/filled-600x600.webp";
      case "48909":return "https://hofmanns-shop.de/cdn/shop/files/rucola-spinat-spaghetti_48909_2_9174a22b-51b9-4e46-9d9e-3c83936a82e3.jpg?v=1773871611";
      case "48526":return "https://production-media.gousto.co.uk/cms/mood-image/2032---10-Min-Creamy-Tomato--Chicken-Gnocchi-0086-1638875810949-x700.jpg";
      case "41001":return "https://img.chefkoch-cdn.de/rezepte/1900801309789793/bilder/368419/crop-960x540/schweinefleisch-ruegener-art.jpg";
      case "48073":return "https://www.univers-reisen.de/fileadmin/processed_files/8/a/csm_iStock-2027208998_Gruenkohl_Wurst_Fleisch_xl_0bc288d402.webp";
      case "7524":return "https://hofmanns-shop.de/cdn/shop/files/Weihnachten-Entenkeule_715d3edc-f32d-4176-a4d9-3d87ab9171b9.jpg?v=1700234541&width=1420";
      case "48210":return "https://schmackhaftesleben.de/assets/images/1760135433093-ljqnp16s.jpg";
      default:return null;
    }
  }
  void loadPhoto(ImageView view,String url){
    new Thread(()->{
      try{
        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setConnectTimeout(7000);conn.setReadTimeout(9000);
        Bitmap bitmap=BitmapFactory.decodeStream(conn.getInputStream());conn.disconnect();
        if(bitmap!=null)new Handler(Looper.getMainLooper()).post(()->view.setImageBitmap(bitmap));
      }catch(Exception ignored){}
    }).start();
  }
  String foodIcon(String id){
    switch(id){
      case "48093":case "48939":case "48909":return "🍝";
      case "6816":return "🥣";
      case "48087":case "8305":case "48210":return "🍽️";
      case "48076":case "6720":return "🍖";
      case "7215":case "41001":return "🥩";
      case "48528":case "48526":return "🍗";
      case "48461":return "🧆";
      case "48418":return "🍲";
      case "6315":return "🥔";
      case "7914":return "🍑";
      case "48073":return "🥬";
      case "7524":return "🦆";
      default:return "🍴";
    }
  }
  TextView pill(String label,int color){TextView v=text(label,13,true);v.setTextColor(color);v.setPadding(14,9,14,9);v.setBackground(bg(Color.rgb(237,243,235)));return v;}
  void gap(LinearLayout box,int height){View v=new View(this);box.addView(v,new LinearLayout.LayoutParams(1,height));}
  @Override public void onCreate(Bundle state) { super.onCreate(state); prefs=getSharedPreferences("menu-v1",MODE_PRIVATE); render(); }
  GradientDrawable bg(int color) { GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(22); return d; }
  TextView text(String s,int sp,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(Color.rgb(33,48,39));if(bold)t.setTypeface(null,Typeface.BOLD);t.setPadding(4,7,4,7);return t;}
  void button(LinearLayout box,String label,Runnable action){Button b=new Button(this);b.setText(label);b.setAllCaps(false);b.setOnClickListener(v->action.run());box.addView(b);}
  String key(String id,int n){return id+"-"+n;}
  void choosePreparationTime(String k,String person) {
    Calendar now=Calendar.getInstance();
    String existing=prefs.getString("planned-"+k,"");
    int hour=now.get(Calendar.HOUR_OF_DAY), minute=now.get(Calendar.MINUTE);
    if(existing.matches("\\d{2}:\\d{2}")) {
      hour=Integer.parseInt(existing.substring(0,2));
      minute=Integer.parseInt(existing.substring(3,5));
    }
    new TimePickerDialog(this,(picker,h,m)->{
      String time=String.format(Locale.GERMANY,"%02d:%02d",h,m);
      prefs.edit().putString("planned-"+k,time).apply();
      change(k,person);
    },hour,minute,true).show();
  }
  void change(String k,String value){
    SharedPreferences.Editor e=prefs.edit();
    String old=prefs.getString(k,"");
    if("done".equals(value)&&!"done".equals(old)){
      String person=old.isEmpty()?prefs.getString("person","Hannes"):old;
      String date=new SimpleDateFormat("dd.MM.yyyy HH:mm",Locale.GERMANY).format(new Date());
      e.putString("history-"+k,person+"|"+date);
    }
    if(!"done".equals(value)&&"done".equals(old))e.remove("history-"+k);
    if(value.isEmpty())e.remove("planned-"+k);
    e.putString(k,value).apply();render();
  }
  void render(){
    getWindow().setStatusBarColor(green);
    getWindow().getDecorView().setSystemUiVisibility(0);
    getWindow().setNavigationBarColor(Color.rgb(247,249,246));
    ScrollView scroll=new ScrollView(this);
    LinearLayout root=new LinearLayout(this);
    root.setOrientation(1);root.setPadding(16,18,16,24);root.setBackgroundColor(cream);
    scroll.addView(root);setContentView(scroll);
    LinearLayout hero=new LinearLayout(this);hero.setOrientation(1);hero.setPadding(22,22,22,22);
    hero.setBackground(bg(green));
    TextView eyebrow=text("UNSERE FAMILIENKÜCHE  •  HOFMANNs",12,true);
    eyebrow.setTextColor(lime);hero.addView(eyebrow);
    TextView title=text("Was essen wir heute?",27,true);title.setTextColor(Color.WHITE);hero.addView(title);
    TextView subtitle=text("19 Gerichte  •  36 Portionen",14,false);subtitle.setTextColor(Color.WHITE);hero.addView(subtitle);
    root.addView(hero);gap(root,16);
    TextView who=text("Für wen suchst du etwas?",15,true);root.addView(who);
    user=new Spinner(this);
    String[] people={"Joel","Hannes","Nicole"};
    ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,people);
    user.setAdapter(adapter);
    String last=prefs.getString("person","Joel");
    for(int i=0;i<people.length;i++)if(people[i].equals(last))user.setSelection(i);
    user.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
      public void onNothingSelected(android.widget.AdapterView<?> p){}
      public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){
        prefs.edit().putString("person",people[pos]).apply();
      }
    });
    root.addView(user);gap(root,8);
    int free=0,res=0,done=0;
    for(String[] m:menus)for(int j=1;j<=Integer.parseInt(m[3]);j++){
      String v=prefs.getString(key(m[1],j),"");
      if(v.equals("done"))done++;else if(v.isEmpty())free++;else res++;
    }
    LinearLayout counts=new LinearLayout(this);counts.setOrientation(0);
    TextView a=pill(free+" frei",green),b=pill(res+" reserviert",green),c=pill(done+" gegessen",green);
    counts.addView(a,new LinearLayout.LayoutParams(0,-2,1));
    counts.addView(b,new LinearLayout.LayoutParams(0,-2,1));
    counts.addView(c,new LinearLayout.LayoutParams(0,-2,1));
    root.addView(counts);gap(root,15);
    EditText search=new EditText(this);
    search.setSingleLine(true);search.setTextSize(16);search.setHint("🔎  Gericht oder Artikel suchen");
    search.setPadding(18,14,18,14);search.setBackground(bg(Color.WHITE));
    search.setText(searchQuery);
    search.addTextChangedListener(new android.text.TextWatcher(){
      public void beforeTextChanged(CharSequence x,int st,int count,int after){}
      public void onTextChanged(CharSequence x,int st,int before,int count){searchQuery=x.toString();populateMenuList();}
      public void afterTextChanged(android.text.Editable x){}
    });
    root.addView(search);gap(root,10);
    LinearLayout tabs=new LinearLayout(this);tabs.setOrientation(0);
    for(String label:new String[]{"Alle","Frei","Reserviert"}){
      TextView tabButton=text(label,14,true);tabButton.setGravity(Gravity.CENTER);
      tabButton.setTextColor(label.equals(filter)?Color.WHITE:green);
      tabButton.setBackground(bg(label.equals(filter)?green:Color.rgb(229,237,229)));
      tabButton.setOnClickListener(v->{filter=label;render();});
      LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(46),1);lp.setMargins(dp(2),0,dp(2),0);tabs.addView(tabButton,lp);
    }
    root.addView(tabs);gap(root,8);
    HorizontalScrollView categories=new HorizontalScrollView(this);
    categories.setHorizontalScrollBarEnabled(false);
    LinearLayout chips=new LinearLayout(this);
    for(String label:new String[]{"Alle","Vegan","Vegetarisch","Fleisch","Geflügel","Süßes"}){
      TextView chip=text(label,13,true);
      chip.setPadding(dp(15),dp(11),dp(15),dp(11));
      chip.setTextColor(label.equals(categoryFilter)?Color.WHITE:green);
      chip.setBackground(bg(label.equals(categoryFilter)?green:Color.rgb(232,240,231)));
      chip.setOnClickListener(v->{categoryFilter=label;render();});
      LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-2,-2);lp.setMargins(0,0,dp(7),0);chips.addView(chip,lp);
    }
    categories.addView(chips);root.addView(categories);gap(root,8);
    list=new LinearLayout(this);list.setOrientation(1);root.addView(list);
    populateMenuList();
    gap(root,18);
    button(root,showRemoved?"Vergangene Mahlzeiten ausblenden":"📅  Essenshistorie anzeigen",()->{showRemoved=!showRemoved;render();});
    if(showRemoved)for(String[] m:menus)for(int j=1;j<=Integer.parseInt(m[3]);j++){
      final String k=key(m[1],j);
      if(prefs.getString(k,"").equals("done")){
        LinearLayout history=new LinearLayout(this);history.setOrientation(1);
        history.setPadding(14,10,14,10);history.setBackground(bg(Color.WHITE));
        history.addView(text(m[0]+" · Portion "+j,15,true));
        String h=prefs.getString("history-"+k,"");
        if(!h.isEmpty())history.addView(text(h.replace("|"," · "),13,false));
        button(history,"Wiederherstellen",()->change(k,""));
        root.addView(history);gap(root,7);
      }
    }
    root.addView(text("Auswahl wird auf diesem Gerät gespeichert. Eine Synchronisierung zwischen Handys ist noch nicht eingerichtet.",11,false));
  }
  String category(String id){
    switch(id){
      case "48939":case "48909":return "Vegan";
      case "48093":case "6816":case "6315":return "Vegetarisch";
      case "7914":return "Süßes";
      case "48528":case "48526":case "7524":return "Geflügel";
      default:return "Fleisch";
    }
  }
  void populateMenuList(){
    if(list==null)return;
    list.removeAllViews();
    for(String[] m:menus){
      if(!(m[0]+" "+m[1]+" "+m[4]).toLowerCase(Locale.GERMANY).contains(searchQuery.toLowerCase(Locale.GERMANY)))continue;
      int free=0,reserved=0;
      for(int j=1;j<=Integer.parseInt(m[3]);j++){
        String value=prefs.getString(key(m[1],j),"");
        if(value.isEmpty())free++;else if(!value.equals("done"))reserved++;
      }
      if(free+reserved==0)continue;
      if(!categoryFilter.equals("Alle")&&!categoryFilter.equals(category(m[1])))continue;
      if(filter.equals("Frei")&&free==0)continue;
      if(filter.equals("Reserviert")&&reserved==0)continue;
      LinearLayout card=new LinearLayout(this);card.setOrientation(1);
      card.setPadding(12,12,12,12);card.setBackground(bg(Color.WHITE));
      LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.setMargins(0,6,0,8);
      LinearLayout headline=new LinearLayout(this);headline.setOrientation(1);
      String imageUrl=photoUrl(m[1]);
      if(imageUrl!=null){
        ImageView photo=new ImageView(this);
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photo.setBackground(bg(Color.rgb(238,243,230)));
        headline.addView(photo,new LinearLayout.LayoutParams(-1,dp(188)));
        loadPhoto(photo,imageUrl);
      }else{
        TextView photo=text(foodIcon(m[1]),39,true);photo.setGravity(Gravity.CENTER);
        photo.setBackground(bg(Color.rgb(238,243,230)));
        headline.addView(photo,new LinearLayout.LayoutParams(-1,dp(188)));
      }
      LinearLayout details=new LinearLayout(this);details.setOrientation(1);details.setPadding(0,dp(9),0,0);
      details.addView(text(m[0],19,true));
      details.addView(text(category(m[1])+"  ·  "+m[2]+" g",12,false));
      TextView status=pill(free+" frei  ·  "+reserved+" reserviert",green);details.addView(status);
      headline.addView(details,new LinearLayout.LayoutParams(-1,-2));
      card.addView(headline);
      boolean expanded=expandedArticle.equals(m[1]);
      Button toggle=new Button(this);toggle.setAllCaps(false);
      toggle.setText(expanded?"Details schließen  ⌃":"Auswählen  ⌄");
      toggle.setOnClickListener(v->{expandedArticle=expanded? "":m[1];render();});
      card.addView(toggle);
      if(expanded){
        card.addView(text(m[4],14,false));
        for(int j=1;j<=Integer.parseInt(m[3]);j++){
          final String k=key(m[1],j);
          String value=prefs.getString(k,"");
          if(value.equals("done"))continue;
          LinearLayout slot=new LinearLayout(this);slot.setOrientation(1);
          slot.setPadding(10,10,10,10);slot.setBackground(bg(Color.rgb(245,248,241)));
          slot.addView(text("Portion "+j+"  ·  "+(value.isEmpty()?"Verfügbar":"Reserviert von "+value),14,true));
          if(value.isEmpty())button(slot,"＋ Reservieren & Uhrzeit wählen",()->choosePreparationTime(k,user.getSelectedItem().toString()));
          else{
            String time=prefs.getString("planned-"+k,"");
            slot.addView(text("Zubereitung: "+(time.isEmpty()?"nicht angegeben":time+" Uhr"),13,false));
            button(slot,"Uhrzeit ändern",()->choosePreparationTime(k,value));
            button(slot,"Reservierung freigeben",()->change(k,""));
          }
          button(slot,"✓ Gegessen",()->change(k,"done"));
          card.addView(slot);gap(card,6);
        }
      }
      list.addView(card,cp);
    }
  }
}
