package de.hofmann.familienmenue;

import android.app.Activity;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
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
  String expandedArticle="";
  int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+0.5f);}
  int spColor=Color.rgb(32,69,48);
  int green=Color.rgb(28,83,56);
  int cream=Color.rgb(247,249,246);
  int lime=Color.rgb(190,235,83);
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
      if(filter.equals("Frei")&&free==0)continue;
      if(filter.equals("Reserviert")&&reserved==0)continue;
      LinearLayout card=new LinearLayout(this);card.setOrientation(1);
      card.setPadding(15,14,15,14);card.setBackground(bg(Color.WHITE));
      LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.setMargins(0,6,0,8);
      LinearLayout headline=new LinearLayout(this);headline.setOrientation(0);headline.setGravity(Gravity.CENTER_VERTICAL);
      TextView photo=text(foodIcon(m[1]),39,true);photo.setGravity(Gravity.CENTER);
      photo.setBackground(bg(Color.rgb(238,243,230)));
      headline.addView(photo,new LinearLayout.LayoutParams(dp(96),dp(96)));
      LinearLayout details=new LinearLayout(this);details.setOrientation(1);details.setPadding(14,0,0,0);
      details.addView(text(m[0],19,true));
      details.addView(text(m[2]+" g  ·  Art. "+m[1],12,false));
      TextView status=pill(free+" frei  ·  "+reserved+" reserviert",green);details.addView(status);
      headline.addView(details,new LinearLayout.LayoutParams(0,-2,1));
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
