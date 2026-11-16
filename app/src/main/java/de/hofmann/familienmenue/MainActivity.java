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
  int green=Color.rgb(28,83,56);
  int cream=Color.rgb(251,248,240);
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
    ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);
    LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(22,24,22,24);root.setBackgroundColor(cream);scroll.addView(root);setContentView(scroll);
    LinearLayout hero=new LinearLayout(this);hero.setOrientation(1);hero.setPadding(23,22,23,22);hero.setBackground(bg(green));
    TextView eyebrow=text("UNSERE FAMILIENKÜCHE",12,true);eyebrow.setTextColor(Color.rgb(224,233,211));hero.addView(eyebrow);
    TextView title=text("Was gibt’s heute Leckeres? 🍽️",27,true);title.setTextColor(Color.WHITE);hero.addView(title);
    TextView subtitle=text("19 Gerichte · für Joel, Hannes und Nicole",14,false);subtitle.setTextColor(Color.WHITE);hero.addView(subtitle);
    root.addView(hero);gap(root,14);
    TextView warning=text("Lokale Testversion: Änderungen sind noch nicht zwischen den Handys synchronisiert. Portionszahlen aus der Fotoauswertung müssen auf Doppelbilder geprüft werden. Originalfotos folgen.",13,false);warning.setBackground(bg(Color.rgb(255,244,208)));warning.setPadding(14,14,14,14);root.addView(warning);
    root.addView(text("Wer wählt aus?",17,true));
    user=new Spinner(this);String[] people={"Joel","Hannes","Nicole"};ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,people);user.setAdapter(adapter);String last=prefs.getString("person","Joel");for(int i=0;i<people.length;i++)if(people[i].equals(last))user.setSelection(i);user.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){prefs.edit().putString("person",people[pos]).apply();}});root.addView(user);
    int free=0,res=0,done=0;
    for(String[] m:menus)for(int j=1;j<=Integer.parseInt(m[3]);j++){String v=prefs.getString(key(m[1],j),"");if(v.equals("done"))done++;else if(v.isEmpty())free++;else res++;}
    LinearLayout counts=new LinearLayout(this);counts.setOrientation(0);counts.setGravity(Gravity.CENTER_VERTICAL);
    TextView a=pill("✓ "+free+" frei",green),b=pill("◷ "+res+" reserviert",green),countDone=pill("☑ "+done+" gegessen",green);
    counts.addView(a);counts.addView(b);counts.addView(countDone);root.addView(counts);gap(root,12);
    list=new LinearLayout(this);list.setOrientation(1);root.addView(list);
    for(String[] m:menus){
      LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setPadding(20,18,20,18);card.setBackground(bg(Color.WHITE));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.setMargins(0,10,0,10);
      boolean any=false;for(int j=1;j<=Integer.parseInt(m[3]);j++)if(!prefs.getString(key(m[1],j),"").equals("done"))any=true;
      if(!any)continue;
      LinearLayout imageHeader=new LinearLayout(this);imageHeader.setGravity(Gravity.CENTER);imageHeader.setOrientation(1);
      imageHeader.setBackground(bg(Color.rgb(238,241,227)));imageHeader.setPadding(10,24,10,24);
      TextView pict=text(foodIcon(m[1]),44,true);pict.setGravity(Gravity.CENTER);imageHeader.addView(pict);
      TextView imageNote=text("Illustratives Symbol · Produktfoto folgt",12,false);imageNote.setGravity(Gravity.CENTER);imageHeader.addView(imageNote);
      card.addView(imageHeader);gap(card,12);
      card.addView(text(m[0],22,true));card.addView(text("HOFMANNs  ·  Art. "+m[1]+"  ·  "+m[2]+" g",13,false));
      card.addView(text(m[4],16,false));gap(card,9);
      String officialUrl = m[1].equals("48087") ? "https://hofmanns-shop.de/products/schweinesteak-au-four" : m[1].equals("48073") ? "https://hofmanns-shop.de/products/grunkohl-norddeutsche-art-1" : null;
      if(officialUrl!=null){ card.addView(text("Offizielles HOFMANNs-Produktfoto verfügbar",12,false)); button(card,"Original-Produktfoto ansehen",()->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(officialUrl)))); }
      else card.addView(text("Original-Produktfoto noch nicht verifiziert",12,false));
      for(int j=1;j<=Integer.parseInt(m[3]);j++){
        final String k=key(m[1],j);String v=prefs.getString(k,"");if(v.equals("done"))continue;
        LinearLayout slot=new LinearLayout(this);slot.setOrientation(1);slot.setPadding(10,10,10,10);slot.setBackground(bg(Color.rgb(245,246,239)));
        slot.addView(text("Portion "+j+"  ·  "+(v.isEmpty()?"✓ VERFÜGBAR":"● RESERVIERT VON "+v.toUpperCase(Locale.GERMANY)),14,true));
        if(v.isEmpty())button(slot,"Reservieren und Uhrzeit wählen",()->choosePreparationTime(k,user.getSelectedItem().toString()));
        else {
          String planned=prefs.getString("planned-"+k,"");
          slot.addView(text("Geplante Zubereitung: "+(planned.isEmpty()?"nicht angegeben":planned+" Uhr"),14,false));
          button(slot,"Uhrzeit ändern",()->choosePreparationTime(k,v));
          button(slot,"Reservierung freigeben",()->change(k,""));
        }
        button(slot,"Zubereitet · aus Liste entfernen",()->change(k,"done"));
        LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,-2);sp.setMargins(0,4,0,8);card.addView(slot,sp);
      }
      list.addView(card,cp);
    }
    button(root,showRemoved?"Essenshistorie ausblenden":"Vergangene Mahlzeiten anzeigen",()->{showRemoved=!showRemoved;render();});
    if(showRemoved)for(String[] m:menus)for(int j=1;j<=Integer.parseInt(m[3]);j++){final String k=key(m[1],j);if(prefs.getString(k,"").equals("done")){LinearLayout c=new LinearLayout(this);c.setOrientation(1);c.addView(text(m[0]+" · Portion "+j,15,true));
      String h=prefs.getString("history-"+k,"");
      if(!h.isEmpty()){String[] parts=h.split("\\|",2);c.addView(text("Gegessen von "+parts[0]+(parts.length>1?" am "+parts[1]:""),14,false));}
      else c.addView(text("Zubereitet · keine früheren Personendaten gespeichert",13,false));
      button(c,"Wiederherstellen",()->change(k,""));root.addView(c);}}
  }
}