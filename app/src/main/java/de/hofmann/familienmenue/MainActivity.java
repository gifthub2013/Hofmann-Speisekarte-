package de.hofmann.familienmenue;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
  int green=Color.rgb(29,103,62);
  @Override public void onCreate(Bundle state) { super.onCreate(state); prefs=getSharedPreferences("menu-v1",MODE_PRIVATE); render(); }
  GradientDrawable bg(int color) { GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(22); return d; }
  TextView text(String s,int sp,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(Color.rgb(33,48,39));if(bold)t.setTypeface(null,Typeface.BOLD);t.setPadding(4,7,4,7);return t;}
  void button(LinearLayout box,String label,Runnable action){Button b=new Button(this);b.setText(label);b.setAllCaps(false);b.setOnClickListener(v->action.run());box.addView(b);}
  String key(String id,int n){return id+"-"+n;}
  void change(String k,String value){prefs.edit().putString(k,value).apply();render();}
  void render(){
    ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);
    LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(22,24,22,24);root.setBackgroundColor(Color.rgb(246,247,242));scroll.addView(root);setContentView(scroll);
    root.addView(text("🍽 Unsere HOFMANNs-Speisekarte",25,true));
    root.addView(text("19 Gerichte · Auswahl für Joel, Hannas und Nicole",14,false));
    TextView warning=text("Lokale Testversion: Änderungen sind noch nicht zwischen den Handys synchronisiert. Portionszahlen aus der Fotoauswertung müssen auf Doppelbilder geprüft werden. Originalfotos folgen.",13,false);warning.setBackground(bg(Color.rgb(255,244,208)));warning.setPadding(14,14,14,14);root.addView(warning);
    root.addView(text("Wer wählt aus?",17,true));
    user=new Spinner(this);String[] people={"Joel","Hannas","Nicole"};ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,people);user.setAdapter(adapter);String last=prefs.getString("person","Joel");for(int i=0;i<people.length;i++)if(people[i].equals(last))user.setSelection(i);user.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){prefs.edit().putString("person",people[pos]).apply();}});root.addView(user);
    int free=0,res=0,done=0;
    for(String[] m:menus)for(int j=1;j<=Integer.parseInt(m[3]);j++){String v=prefs.getString(key(m[1],j),"");if(v.equals("done"))done++;else if(v.isEmpty())free++;else res++;}
    root.addView(text(free+" verfügbar · "+res+" reserviert · "+done+" zubereitet",16,true));
    list=new LinearLayout(this);list.setOrientation(1);root.addView(list);
    for(String[] m:menus){
      LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setPadding(17,14,17,14);card.setBackground(bg(Color.WHITE));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.setMargins(0,10,0,10);
      boolean any=false;for(int j=1;j<=Integer.parseInt(m[3]);j++)if(!prefs.getString(key(m[1],j),"").equals("done"))any=true;
      if(!any)continue;
      card.addView(text(m[0],20,true));card.addView(text("Artikel "+m[1]+" · "+m[2]+" g je Portion",13,false));
      card.addView(text(m[4],15,false));
      card.addView(text("Original-Produktfoto noch nicht eingebunden",12,false));
      for(int j=1;j<=Integer.parseInt(m[3]);j++){
        final String k=key(m[1],j);String v=prefs.getString(k,"");if(v.equals("done"))continue;
        LinearLayout slot=new LinearLayout(this);slot.setOrientation(1);slot.setPadding(10,10,10,10);slot.setBackground(bg(Color.rgb(237,243,239)));
        slot.addView(text("Portion "+j+" · "+(v.isEmpty()?"verfügbar":"reserviert von "+v),14,true));
        if(v.isEmpty())button(slot,"Für ausgewählte Person reservieren",()->change(k,user.getSelectedItem().toString()));
        else button(slot,"Reservierung freigeben",()->change(k,""));
        button(slot,"Zubereitet · aus Liste entfernen",()->change(k,"done"));
        LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,-2);sp.setMargins(0,4,0,8);card.addView(slot,sp);
      }
      list.addView(card,cp);
    }
    button(root,showRemoved?"Entfernte Portionen ausblenden":"Entfernte Portionen anzeigen",()->{showRemoved=!showRemoved;render();});
    if(showRemoved)for(String[] m:menus)for(int j=1;j<=Integer.parseInt(m[3]);j++){final String k=key(m[1],j);if(prefs.getString(k,"").equals("done")){LinearLayout c=new LinearLayout(this);c.setOrientation(1);c.addView(text(m[0]+" · Portion "+j,15,true));button(c,"Wiederherstellen",()->change(k,""));root.addView(c);}}
  }
}